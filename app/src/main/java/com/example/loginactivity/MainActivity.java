package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    private EditText emailET,passwordET;
    private Button loginButton;
    private TextView errorEmailTV,errorPasswordTV;
    private TextView tvSignup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        loginButton=findViewById(R.id.loginBTN);
        errorEmailTV=findViewById(R.id.errorEmailTV);
        errorPasswordTV=findViewById(R.id.errorPasswordTV);

        tvSignup=findViewById(R.id.tvSignup);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmailBoolean()&&isPasswordBoolean()){
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent1);
            }
        });



    }

    private boolean isEmailBoolean(){
        String  email=emailET.getText().toString();
        if(email.isEmpty()){
            errorEmailTV.setVisibility(View.VISIBLE);
            errorEmailTV.setText("*Please enter your e-mail.");
            return false;

        } else if(Pattern.compile("^[a-zA-Z0-9]+[.!#$%&'*+/=?^_`{|}~-]*[a-zA-Z0-9]*@[a-zA-Z0-9]+.[a-zA-Z0-9.]+[a-zA-Z0-9]+").matcher(email).matches()){
            errorEmailTV.setVisibility(View.INVISIBLE);
            return true;

        } else {
            errorEmailTV.setVisibility(View.VISIBLE);
            errorEmailTV.setText("*Please enter a valid e-mail. ");
            return false;

        }
    }
    private boolean isPasswordBoolean(){
        String  password=passwordET.getText().toString();
        if(password.isEmpty()){
            errorPasswordTV.setVisibility(View.VISIBLE);
            errorPasswordTV.setText("*Please enter password. ");
            return false;

        } else if(password.length()>7){
            errorPasswordTV.setVisibility(View.INVISIBLE);

            return true;

        } else {
            errorPasswordTV.setVisibility(View.VISIBLE);
            errorPasswordTV.setText("*Please enter password contaning minimum length 8.");
            return false;

        }

    }
}
