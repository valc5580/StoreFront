package net.codebot.shapes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;



public class OwnerLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);
    }


    public void TryLogin(View v) {

        ProgressBar progressBar= (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        TextView LoginStatus = findViewById(R.id.LoginResult);
        LoginStatus.setText("");

        EditText user = findViewById(R.id.Username);
        String username = user.getText().toString().trim();

        EditText pass = findViewById(R.id.Password);
        String password = pass.getText().toString();

        LoginService.validateLogin(username, password).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull final Task<Boolean> task) {
                Log.i("onComplete", "got to onComplete");

                progressBar.setVisibility(View.INVISIBLE);
                if (!task.isSuccessful()) {
                    Log.i("ERROR", "Exception");
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                        Log.i("Error", ffe.getMessage());
                        Toast.makeText(OwnerLogin.this, "Error Validating Login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("RETURNED", "Returned Login " + task.getResult());
                    boolean auth_status = task.getResult();
                    if (auth_status) {
                        LoginStatus.setText("Login Successful!");
                        LoginStatus.setTextColor(Color.GREEN);
                        Intent intent = new Intent(getApplicationContext(), OwnerView.class);
                        startActivity(intent);
                    } else {
                        LoginStatus.setText("Login Failed. Try Again!");
                        LoginStatus.setTextColor(Color.RED);
                    }

                }
            }
        });
    }
}
