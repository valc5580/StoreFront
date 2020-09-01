package net.codebot.shapes;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class CreateStoreOnClickListener implements View.OnClickListener {
    int row_num;
    int col_num;
    Context context;
    Activity act;
    char [] letters = {'A','B','C','D','E','F','G','H','I','J'};
    int [][] currentFloorArray;

    public CreateStoreOnClickListener(Context context, int row_num, int col_num, int [][] currentFloorArray){
        this.context = context;
        this.row_num = row_num;
        this.col_num = col_num;
        this.currentFloorArray = currentFloorArray;

        this.act = (Activity) context;
    }
    @Override
    public void onClick(View v)
    {
        //Log.i("ROW_NUM",Integer.toString(this.row_num));
        //Log.i("COL_NUM",Integer.toString(this.col_num));

        if (this.currentFloorArray[this.row_num][this.col_num] ==1){
            currentFloorArray[row_num][col_num] =0;

        }
        else{
            currentFloorArray[row_num][col_num] = 1;
        }

    }

}
