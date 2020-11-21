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
        currentAccount=null;

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    lookupAccount(v);
                    update();}
                catch(Exception e){
                    screenAuth.setText(e.toString());
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAccount(v);
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

    public void newAccount (View view) {

        UserAccount ua = UserAccount.createAccount(inputRole.getText().toString(),
                new User(inputFirstName.getText().toString(),
                        inputLastName.getText().toString(),
                        inputUsername.getText().toString(),
                        inputPassword.getText().toString()));

        AccountHandler dbHandler = new AccountHandler(this);
        dbHandler.addAccount(ua);

        inputPassword.setText("");
        inputFirstName.setText("");
        inputLastName.setText("");
        inputRole.setText("");
        inputUsername.setText("");

        currentAccount=ua;
    }


    public void lookupAccount (View view) {

        AccountHandler dbHandler = new AccountHandler(this);
        UserAccount ua = dbHandler.findAccount(inputUsername.getText().toString(),
            inputPassword.getText().toString());

        if (ua != null) {
            inputRole.setText(String.valueOf(ua.getRole()));
            inputFirstName.setText(String.valueOf(ua.getFirstName()));
            inputLastName.setText(String.valueOf(ua.getLastName()));
            currentAccount=ua;}

    }
}