package net.codebot.shapes;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProdAddActivity extends AppCompatActivity {
    int CAMERA_REQUEST = 1888;
    int GRID_REQUEST = 1;

    String imagePath="noPic";
    String location="noLoc";




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GRID_REQUEST){
            if (resultCode == RESULT_OK) {
                char row_num = data.getCharExtra("ROW_NUM", 'A');
                int col_num = data.getIntExtra("COL_NUM", 0);
                int floorNum = data.getIntExtra("floorNum", 0);
                location = Integer.toString(floorNum) + row_num + Integer.toString(col_num);
                Log.i("Location",location);
                Button b = (Button) findViewById(R.id.prod_location);
                b.setText(location + " (Click to edit)");
            } else {
                location="noLoc";
            }
        }
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK){
                Log.i("Done", "Took pic!");
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                ImageView pic= (ImageView)findViewById(R.id.pic_display);
                pic.setImageBitmap(photo);

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                // Create imageDir
                File mypath=new File(directory,"newProd.jpg");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                imagePath=directory.getAbsolutePath();
            } else {
                imagePath="noPic";
            }



        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_add);

        Button takePic= (Button) findViewById(R.id.take_pic);
        takePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("putSomething", true);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        Button selectLocation= (Button) findViewById(R.id.prod_location);
        selectLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ProdAddActivity.this, CreateStoreActivity.class);
                intent.putExtra("use", "addProd");
                startActivityForResult(intent, GRID_REQUEST);
            }
        });





        Button addProd= (Button) findViewById(R.id.add_product_button);
        addProd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText nameInput= ((EditText)findViewById(R.id.prod_name));
                String name=nameInput.getText().toString();
                if(name.isEmpty()){
                    nameInput.setError("Product Name Required");
                    return;
                }
                EditText quantityInput= ((EditText)findViewById(R.id.prod_quantity));
                if(quantityInput.getText().toString().isEmpty()){
                    quantityInput.setError("Product Quantity Required");
                    return;
                }
                int quantity=Integer.parseInt(quantityInput.getText().toString());


                String pic=imagePath;
                if(pic.equals("noPic")){
                    Toast.makeText(ProdAddActivity.this, "Take Product Picture Before Submitting", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(location.equals("noLoc")){
                    Toast.makeText(ProdAddActivity.this, "Select Product Location Before Submitting", Toast.LENGTH_SHORT).show();
                    return;
                }

                Product newProd= new Product("none Yet",name,pic,quantity,location);

                String stringToPassBack= (new Gson()).toJson(newProd);

                Intent intent = new Intent();
                intent.putExtra("product", stringToPassBack);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}
