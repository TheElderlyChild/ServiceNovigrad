package com.example.servicenovigrad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.userManagement.*;

public class MainActivity extends AppCompatActivity {
    Button buttonSignUp, buttonLogin;
    TextView screenAuth, screenWelcome;
    EditText inputUsername, inputPassword, inputFirstName, inputLastName, inputRole;
    UserAccount currentAccount;
    Accounts acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignUp= (Button) findViewById(R.id.btnSignUp);
        buttonLogin= (Button) findViewById(R.id.btnLogin);
        screenAuth=(TextView) findViewById(R.id.displayAuth);
        inputRole=(EditText) findViewById(R.id.txtRole);
        inputUsername=(EditText) findViewById(R.id.txtUsername);
        inputPassword=(EditText) findViewById(R.id.txtPassword);
        inputFirstName=(EditText) findViewById(R.id.txtFirstName);
        inputLastName=(EditText) findViewById(R.id.txtLastName);
        acc=new Accounts();
        currentAccount=null;

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAccount = acc.getAccount(inputUsername.getText().toString(),
                        inputPassword.getText().toString());
                update();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAccount=acc.createAccount(inputRole.getText().toString(),
                        inputUsername.getText().toString(),
                        inputPassword.getText().toString(),
                        inputFirstName.getText().toString(),
                        inputLastName.getText().toString());
                update();            }
        });
    }

    private void update(){
        if (currentAccount==null){
            screenAuth.setText("Login Unsuccessful");
            return;
        }

        screenAuth.setText("Login Successful");

        setContentView(R.layout.relative);
        screenWelcome=(TextView) findViewById(R.id.displayMsg);
        screenWelcome.setText("Welcome "+currentAccount.getFirstName()+
                "! You are logged in as "+currentAccount.getRole());
    }

}