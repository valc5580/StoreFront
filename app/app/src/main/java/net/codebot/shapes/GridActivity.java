package net.codebot.shapes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

public class GridActivity extends AppCompatActivity {

    int mRows = 10;
    int mCols = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        ConstraintLayout layout = findViewById(R.id.outter_layout);

        boolean showPath = getIntent().getBooleanExtra("showPath",false);
        ArrayList<String> products = new ArrayList<String>();
        if (showPath) {
            products = getIntent().getStringArrayListExtra("locations");
            if (products != null) {
                for (int i = 0; i < products.size(); i++) {
                    Log.i("Product", products.get(i));
                }
                Collections.sort(products);

            }
        }

        int color1 = ContextCompat.getColor(GridActivity.this, R.color.black);
        int color2 = ContextCompat.getColor(GridActivity.this, R.color.white);
//        int color2 = ContextCompat.getDrawable(, );
        int color3 = ContextCompat.getColor(GridActivity.this, R.color.legitWhite);
        int red = ContextCompat.getColor(GridActivity.this, R.color.red);
        TextView textView;
        ConstraintLayout.LayoutParams lp;
        int id;
        //int[][] idArray = new int[mRows][mCols];

        int [][] gridArray = new int[][] {{1,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,0,0,0,0,0},
                {1,1,1,1,1,0,0,0,1,1},
                {0,0,0,0,0,0,1,1,1,1},
                {0,0,0,0,0,0,1,1,1,1},
                {1,1,1,1,1,0,1,1,1,1},
                {1,1,1,1,1,0,0,0,1,1},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
        };

        int [][]idArray = new int[10][10];

        ConstraintSet cs = new ConstraintSet();
        char [] letters = {'A','B','C','D','E','F','G','H','I','J'};


        // Add our views to the ConstraintLayout.
        for (int iRow = 0; iRow < mRows; iRow++) {
            for (int iCol = 0; iCol < mCols; iCol++) {
                textView = new TextView(this);
                lp = new ConstraintLayout.LayoutParams(ConstraintSet.MATCH_CONSTRAINT,
                        ConstraintSet.MATCH_CONSTRAINT);
                id = View.generateViewId();
                idArray[iRow][iCol] = id;
                textView.setId(id);
                //textView.setText(String.valueOf(id));

                textView.setGravity(Gravity.CENTER);
                if (gridArray[iRow][iCol] == 1){
                    textView.setBackgroundColor(color1);
                    String dispVal = letters[iRow]+ Integer.toString(iCol);
                    textView.setText(dispVal);
                    textView.setTextColor(color3);
                    if (!showPath){
                        textView.setClickable(true);
                        //textView.setOnClickListener(new GridOnClickListener(this,iRow,iCol));
                    }
                    else{
                        //int exists = Collections.binarySearch(products,dispVal);
                        //products.contains(dispVal);
                        if (products != null) {
                            if (products.contains(dispVal)) {
                                textView.setBackgroundColor(red);
                            }
                        }
                    }

                }
                else{
                    textView.setBackgroundColor(color2);
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

}
