package net.codebot.shapes;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateStoreActivity extends AppCompatActivity {
    String storeJSON;
    Store testStore;
    int currentFloor;
    int mRows = 10;
    int mCols = 10;
    int [][] currentFloorArray;
    int currRow;
    int currCol;
    ArrayList<String> products;
    String use;
    Spinner spinner;
    char [] letters = {'A','B','C','D','E','F','G','H','I','J'};
    String floorString = "";
    ArrayList<String> productFloors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        // called in showPath given product array
        // called in add product to select location
        //
        // called in StoreLAyout for owner (Create Store)
        // use, "create", "addProd", "showPath"

        use = getIntent().getStringExtra("use");
        Log.i("USEVALUE" , use);

        products = new ArrayList<>();
        productFloors = new ArrayList<>();

        if (use.equals("showPath")) {

            products = getIntent().getStringArrayListExtra("locations");
            floorString = " Products on Floors : ";

            for (int i = 0; i <products.size(); i++){
                String loc = products.get(i);
                String floorNum = Character.toString(loc.charAt(0));

                if (!productFloors.contains(floorNum)){
                    productFloors.add(floorNum);
                    floorString +=  floorNum + "  ";
                }

            }

            getPath(products);






        }

        TextView productInfo = (TextView)findViewById(R.id.textView2);
        productInfo.setText(floorString);

        getStore();



    }

    void setupButtons(){



        Button updateStoreBtn = (Button)findViewById(R.id.sendStoreButton);
        Button addFloorBtn = (Button) findViewById(R.id.addFloorButton);
        if (use.equals("create")){
            addFloorBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int [][] newGrid = new int[10][10];
                    Floor newFloor = new Floor(newGrid);
                    currentFloor = testStore.getFloors().size();
                    testStore.addFloor(newFloor);
                    currentFloorArray = newGrid;
                    displaySpinner();
                    renderGrid();
                }
            });


            updateStoreBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Floor updatedFloor = new Floor(currentFloorArray);
                    testStore.updateFloor(updatedFloor,currentFloor);
                    updateStore(testStore);
                }
            });


        }
        else {
            addFloorBtn.setVisibility(View.INVISIBLE);
            updateStoreBtn.setVisibility(View.INVISIBLE);

        }

    }
    void displaySpinner(){
        //Spinner stuff
        spinner = (Spinner) findViewById(R.id.spinner1);

        int numFloors = testStore.getFloors().size();

        // create Spinner options
        String [] items = new String[numFloors];

        for (int i = 0; i<numFloors; i++){
            items[i] = "Floor " + i;
        }
        spinner.setSelection(currentFloor);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFloor = position;
                currentFloorArray = testStore.getFloor(currentFloor).getGrid();
                renderGrid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void renderGrid(){
        ConstraintLayout layout = findViewById(R.id.outter_layout);
        int color1 = ContextCompat.getColor(CreateStoreActivity.this, R.color.black);
        int color2 = ContextCompat.getColor(CreateStoreActivity.this, R.color.white);
        int color3 = ContextCompat.getColor(CreateStoreActivity.this, R.color.legitWhite);
        int red = ContextCompat.getColor(CreateStoreActivity.this, R.color.red);
        TextView textView;
        ConstraintLayout.LayoutParams lp;
        int id;
        //int [][] gridArray = testStore.getFloors().get(0).getGrid();
        //currentFloorArray = gridArray;

        int [][]idArray = new int[10][10];

        ConstraintSet cs = new ConstraintSet();
        char [] letters = {'A','B','C','D','E','F','G','H','I','J'};

        // Add our views to the ConstraintLayout.
        //currCol = 0;
        //currRow = 0;
        for (int iRow = 0; iRow < mRows; iRow++) {
            for (int iCol = 0; iCol < mCols; iCol++) {
                textView = new TextView(this);
                lp = new ConstraintLayout.LayoutParams(ConstraintSet.MATCH_CONSTRAINT,
                        ConstraintSet.MATCH_CONSTRAINT);
                id = View.generateViewId();
                idArray[iRow][iCol] = id;
                textView.setId(id);
                currCol = iCol;
                currRow = iRow;
                final int currentiCol= iCol;
                final int currentiRow= iRow;
                if (use.equals("create")){
                    textView.setClickable(true);
                    //textView.setOnClickListener(new CreateStoreOnClickListener(this,iRow,iCol,currentFloorArray));
                    textView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (currentFloorArray[currentiRow][currentiCol] ==1){
                                currentFloorArray[currentiRow][currentiCol] =0;
                            }
                            else{
                                currentFloorArray[currentiRow][currentiCol] = 1;
                            }
                            renderGrid();
                        }
                    });
                }

                textView.setGravity(Gravity.CENTER);
                if (currentFloorArray[iRow][iCol] == 1){

                    textView.setBackgroundColor(color1);
                    String dispVal = letters[iRow]+ Integer.toString(iCol);
                    String fullName = Integer.toString(currentFloor) + dispVal;
                    textView.setText(dispVal);
                    textView.setTextColor(color3);


                    if (use.equals("addProd")){
                        textView.setClickable(true);
                        textView.setOnClickListener(new GridOnClickListener(this,iRow,iCol,currentFloor));


                    }
                    else if(use.equals("showPath")){
                        if (products != null) {
                            if (products.contains(fullName)) {
                                int index = products.indexOf(fullName);
                                if (index != -1){
                                    textView.setBackgroundColor(red);
                                    textView.setText(Integer.toString(index + 1));
                                }
                            }
                        }
                    }

                }


                else if (currentFloorArray[iRow][iCol] == 0){
                    textView.setBackgroundColor(color2);
                    textView.setBackgroundResource(R.drawable.my_border);

                }
                else if (currentFloorArray[iRow][iCol] == 2){
                    textView.setText("start");
                    textView.setTextColor(color3);
                    textView.setBackgroundColor(getResources().getColor(R.color.blueGrid)); //hardcoded blue background
                }
                else {
                    textView.setText("stairs");
                    textView.setTextColor(color3);
                    textView.setBackgroundColor(getResources().getColor(R.color.blueGrid)); //hardcoded blue background
                }
                //textView.setBackgroundColor(((iRow + iCol) % 2 == 0) ? color1 : color2);
                layout.addView(textView, lp);
            }
        }


        // Create horizontal chain for each row and set the 1:1 dimensions.
        // but first make sure the layout frame has the right ratio set.
        cs.clone(layout);
        cs.setDimensionRatio(R.id.gridFrame, mCols + ":" + mRows);
        for (int iRow = 0; iRow < mRows; iRow++) {
            for (int iCol = 0; iCol < mCols; iCol++) {
                id = idArray[iRow][iCol];
                cs.setDimensionRatio(id, "1:1");
                if (iRow == 0) {
                    // Connect the top row to the top of the frame.
                    cs.connect(id, ConstraintSet.TOP, R.id.gridFrame, ConstraintSet.TOP);
                } else {
                    // Connect top to bottom of row above.
                    cs.connect(id, ConstraintSet.TOP, idArray[iRow - 1][0], ConstraintSet.BOTTOM);
                }
            }
            // Create a horiontal chain that will determine the dimensions of our squares.
            // Could also be createHorizontalChainRtl() with START/END.
            cs.createHorizontalChain(R.id.gridFrame, ConstraintSet.LEFT,
                    R.id.gridFrame, ConstraintSet.RIGHT,
                    idArray[iRow], null, ConstraintSet.CHAIN_PACKED);
        }

        cs.applyTo(layout);
    }

    public void getStore(){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        StoreService.getStore().addOnCompleteListener(new OnCompleteListener<Store>() {
            @Override
            public void onComplete(@NonNull final Task<Store> task) {
                Log.i("onComplete", "got to onComplete");
                progressBar.setVisibility(View.INVISIBLE);
                if (!task.isSuccessful()) {
                    Log.i("ERROR", "Exception");
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                        Log.i("Error", details.toString());
                        Log.i("Error", ffe.getMessage());
                        Toast.makeText(CreateStoreActivity.this, "Error Fetching Store Layout", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    testStore = task.getResult();
                    currentFloorArray = testStore.getFloors().get(0).getGrid();
                    currentFloor = 0;
                    Floor floor = testStore.getFloors().get(0);
                    findViewById(R.id.gridFrame).setVisibility(View.VISIBLE);
                    String testStr = StoreService.convertStoreToGSON(testStore);
                    Log.i("RETURNED", "Returned "+  Arrays.deepToString(floor.getGrid()));

                    Toast.makeText(CreateStoreActivity.this, "Store Layout Successfully Fetched", Toast.LENGTH_SHORT).show();
                    displaySpinner();
                    setupButtons();
                    renderGrid();
                }
            }
        });

    }

    void updateStore(Store store){
        StoreService.updateStore(store).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull final Task<String> task) {
                Log.i("onComplete", "got to onComplete");
                if (!task.isSuccessful()) {
                    Log.i("ERROR", "Exception");
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                        //Log.i("Error", details.toString());
                        Log.i("Error", ffe.getMessage());
                        Toast.makeText(CreateStoreActivity.this, "Error Updating Store", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("RETURNED", "Returned " + task.getResult());
                    Toast.makeText(CreateStoreActivity.this, "Store Successfully updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void getPath(ArrayList<String> productLocations ){
        StoreService.getPath(productLocations).addOnCompleteListener(new OnCompleteListener<ArrayList<String>>() {
            @Override
            public void onComplete(@NonNull final Task<ArrayList<String>> task) {
                Log.i("onComplete", "got to onComplete");
                if (!task.isSuccessful()) {
                    Log.i("ERROR", "Exception");
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                        //Log.i("Error", details.toString());
                        Log.i("Error", ffe.getMessage());
                        Toast.makeText(CreateStoreActivity.this, "Error getting path", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("RETURNED", "Returned path " + task.getResult());
                    products = task.getResult();

                    Toast.makeText(CreateStoreActivity.this, "Path successfully fetched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
