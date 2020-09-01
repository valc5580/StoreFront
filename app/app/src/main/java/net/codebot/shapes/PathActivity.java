package net.codebot.shapes;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class PathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        String selectedProductsString= getIntent().getStringExtra("selectedProducts");
        ArrayList<Product> selectedProducts = (ArrayList<Product>) (new Gson()).fromJson(selectedProductsString,new TypeToken<ArrayList<Product>>(){}.getType());

        TextView text= (TextView) findViewById(R.id.textView);

        String s="";

        for(int i=0; i<selectedProducts.size(); i++){
            s=s+"name : "+selectedProducts.get(i).name+", id : "+selectedProducts.get(i).id+", quantitiy : "+selectedProducts.get(i).quantity+", location : "+selectedProducts.get(i).location+"\n";
        }
        text.setText(s);
    }
}
