package net.codebot.shapes;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OwnerLoginView(View v) {
        Intent intent = new Intent(this, OwnerLogin.class);
        startActivity(intent);
    }

    public void SearchView(View v) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("user", "shopper");
        startActivity(intent);

    }
}
