package net.codebot.shapes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    String user= "owner";

    private static final int ADD_PROD_ACTIVITY_REQUEST_CODE = 0;

    ArrayList<Product> productList;

    ArrayList<Product> selectedProducts;

    void clearTable(){
        TableLayout table = (TableLayout) findViewById(R.id.product_table);
        while(table.getChildCount()>1) {
            TableRow child = (TableRow) table.getChildAt(1);
            table.removeView(child);
        }
    }



    //populates table based on productList array
    public void populateTable(){
        TableLayout table = (TableLayout) findViewById(R.id.product_table);


        for(int i=0; i<productList.size(); i++){
            Log.i("update","Updating product "+i);
            TableRow row = new TableRow(ProductListActivity.this);
            //row.setId(productList.get(i).id);
            //row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(1);
            row.setGravity(Gravity.CENTER);




            LinearLayout imageBox= new LinearLayout(ProductListActivity.this);
            imageBox.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT));
            imageBox.setGravity(Gravity.CENTER);
            imageBox.setOrientation(LinearLayout.VERTICAL);


            ImageView pic= new ImageView(ProductListActivity.this);
            pic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200));
//
//            byte[] imageBytes = Base64.decode(productList.get(i).pic, Base64.DEFAULT);
//            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            pic.setImageBitmap(decodedImage);

            Picasso.get().load(productList.get(i).pic).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(pic);
            imageBox.addView(pic);


            TextView itemName= new TextView(ProductListActivity.this);
            itemName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            itemName.setGravity(Gravity.CENTER);
            itemName.setText(productList.get(i).name);
            itemName.setTextColor(Color.BLACK);
            itemName.setTextSize(12);
            itemName.setTypeface(Typeface.DEFAULT_BOLD);

            imageBox.addView(itemName);

            row.addView(imageBox);


            TextView quantity= new TextView(ProductListActivity.this);
            quantity.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT));
            quantity.setGravity(Gravity.CENTER);
            quantity.setText(Integer.toString(productList.get(i).quantity));
            quantity.setTextColor(Color.BLACK);
            quantity.setTextSize(18);
            quantity.setTypeface(Typeface.DEFAULT_BOLD);

            row.addView(quantity);

            TextView location= new TextView(ProductListActivity.this);
            location.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT));
            location.setGravity(Gravity.CENTER);
            location.setText(productList.get(i).location);
            location.setTextColor(Color.BLACK);
            location.setTextSize(18);
            location.setTypeface(Typeface.DEFAULT_BOLD);

            row.addView(location);

            final Button add_remove = new Button(ProductListActivity.this);
            add_remove.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT));
            if (user.equals("shopper")){
                add_remove.setText("Add To Cart");
                add_remove.setBackgroundColor(Color.GREEN);
                final int index=i;
                add_remove.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Log.i("button text", add_remove.getText().toString());
                        if (add_remove.getText().toString().equals("Add To Cart")){
                            selectedProducts.add(productList.get(index));
                            add_remove.setText("Remove From Cart");
                            add_remove.setBackgroundColor(Color.GRAY);
                            Toast.makeText(ProductListActivity.this, "Product added to cart: "+productList.get(index).name, Toast.LENGTH_SHORT).show();
                        } else {
                            add_remove.setText("Add To Cart");
                            add_remove.setBackgroundColor(Color.GREEN);
                            for (int j=0; j<selectedProducts.size(); j++){
                                if (selectedProducts.get(j).id.equals(productList.get(index).id)){
                                    selectedProducts.remove(j);
                                    break;
                                }
                            }
                            Toast.makeText(ProductListActivity.this, "Product removed from cart: "+productList.get(index).name, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            } else {
                add_remove.setText("Remove");
                add_remove.setBackgroundColor(Color.RED);
                final int index=i;
                add_remove.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ((TextInputEditText) findViewById(R.id.search_text)).setText("");
                        removeProduct(index, productList.get(index).id);

                    }
                });
            }

            row.addView(add_remove);

            table.addView(row);


        }


    }

    void showProgressBar(){
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
    }

    void hideProgressBar(){
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
    }


    void getProductsAndPopulateTable(){

        showProgressBar();

        ProductListService.getProducts()
                .addOnCompleteListener(new OnCompleteListener<ArrayList<Product>>() {
                    @Override
                    public void onComplete(@NonNull final Task<ArrayList<Product>> task) {
                        Log.i("onComplete", "got to onComplete");
                        if (!task.isSuccessful()) {
                            Log.i("ERROR", "Exception");
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                //Object details = ffe.getDetails();
                               // Log.i("Error", details.toString());
                                Log.i("Error", ffe.getMessage());
                                Toast.makeText(ProductListActivity.this, "Error Fetching Products", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.i("RETURNED", "Returned in prodlistactivity product array list : "+ new Gson().toJson(task.getResult()));

                            productList=task.getResult();
                            clearTable();
                            populateTable();


                        }
                        hideProgressBar();
                    }
                });
    }




    void addProduct(Product prod){

        showProgressBar();

        ProductListService.addProduct(prod)
        .addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull final Task<String> task) {
                Log.i("onComplete", "got to onComplete");
                if (!task.isSuccessful()) {
                    Log.i("ERROR", "Exception");
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();

                        //Object details = ffe.getDetails();

                        Log.i("Error", ffe.getMessage());
                        hideProgressBar();
                        Toast.makeText(ProductListActivity.this, "Error Adding Product", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("RETURNED", "Returned " + task.getResult());
                    Toast.makeText(ProductListActivity.this, "Product Successfully Added", Toast.LENGTH_SHORT).show();
                    getProductsAndPopulateTable();

                }
            }
        });
    }

    void removeProduct(int index, String productID){

        showProgressBar();

        ProductListService.removeProduct(productID)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull final Task<String> task) {
                        Log.i("onComplete", "got to onComplete");
                        if (!task.isSuccessful()) {
                            Log.i("ERROR", "Exception");
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                //Object details = ffe.getDetails();
                                //Log.i("Error", details.toString());
                                Log.i("Error", ffe.getMessage());
                                Toast.makeText(ProductListActivity.this, "Error Removing Product", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.i("RETURNED", "Returned " + task.getResult());
                            Toast.makeText(ProductListActivity.this, "Product Successfully Removed", Toast.LENGTH_SHORT).show();
                            productList.remove(index);
                            clearTable();
                            populateTable();
                        }
                        hideProgressBar();
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);

        productList= new ArrayList<Product>();
        selectedProducts= new ArrayList<Product>();

        user=getIntent().getStringExtra("user");

        Button add_or_path_button= (Button) findViewById(R.id.add_or_path_button);
        if (user.equals("shopper")){
            add_or_path_button.setText("View Path");
            add_or_path_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    ArrayList<String> prodLocations= new ArrayList<String>();
                    for(int i=0; i<selectedProducts.size(); i++) {
                        prodLocations.add(selectedProducts.get(i).location);
                    }
                    Intent intent= new Intent(ProductListActivity.this, CreateStoreActivity.class);
                    intent.putStringArrayListExtra("locations", prodLocations);
                    intent.putExtra("use", "showPath");
                    startActivity(intent);
                }
            });
        } else {
            add_or_path_button.setText("Add Product");
            add_or_path_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(ProductListActivity.this, ProdAddActivity.class);
                    startActivityForResult(intent, ADD_PROD_ACTIVITY_REQUEST_CODE);
                }
            });
        }



        Button searchButton= (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                TextInputEditText searchText= (TextInputEditText) findViewById(R.id.search_text);
                String text= searchText.getText().toString();
                Log.i("Button", "Search text is "+text);
                TableLayout table = (TableLayout) findViewById(R.id.product_table);

                for(int i=1; i<table.getChildCount(); i++){
                    TableRow row = (TableRow) table.getChildAt(i);
                    String productName=((TextView)((LinearLayout)row.getChildAt(0)).getChildAt(1)).getText().toString();
                    Log.i("Button", "Product name is "+productName);
                    if (productName.contains(text)){
                        row.setVisibility(View.VISIBLE);
                    } else {
                        row.setVisibility(View.GONE);
                    }
                }
            }
        });

        getProductsAndPopulateTable();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == ADD_PROD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                String returnString = data.getStringExtra("product");
                Product newProd=(new Gson()).fromJson(returnString,new TypeToken<Product>(){}.getType());

                Log.i("Adding product: ", returnString);


                //restoring image, converting to Base64, adding to product list, and then refreshing table
                try {
                    File f=new File(newProd.pic, "newProd.jpg");
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                    byte[] imageBytes2 = baos2.toByteArray();
                    newProd.pic = Base64.encodeToString(imageBytes2, Base64.DEFAULT);



                    addProduct(newProd);


                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

            }
        }
    }
}
