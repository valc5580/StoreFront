package net.codebot.shapes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import static android.app.Activity.RESULT_OK;

public class GridOnClickListener implements View.OnClickListener{
    int row_num;
    int col_num;
    int floorNum;
    Context context;
    Activity act;
    char [] letters = {'A','B','C','D','E','F','G','H','I','J'};


    public GridOnClickListener(Context context,  int row_num, int col_num, int floorNum){
        this.context = context;
        this.row_num = row_num;
        this.col_num = col_num;
        this.floorNum = floorNum;
        this.act = (Activity) context;
    }
    @Override
    public void onClick(View v)
    {
        Intent retVal = new Intent();
        retVal.putExtra("ROW_NUM", this.letters[this.row_num]);
        retVal.putExtra("COL_NUM", this.col_num);
        retVal.putExtra("floorNum",this.floorNum);
        this.act.setResult(RESULT_OK,retVal);
        this.act.finish();
    }
}
