package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.userManagement.User;
import com.example.servicenovigrad.userManagement.UserAccount;

public class MainActivity extends AppCompatActivity {
    Button buttonSignUp, buttonLogin;
    TextView screenAuth, screenWelcome;
    EditText inputUsername, inputPassword, inputFirstName, inputLastName;
    Spinner inputRole;
    UserAccount currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignUp= (Button) findViewById(R.id.btnSignUp);
        buttonLogin= (Button) findViewById(R.id.btnLogin);
        screenAuth=(TextView) findViewById(R.id.displayAuth);
        inputRole=(Spinner) findViewById(R.id.txtRole);
        inputUsername=(EditText) findViewById(R.id.txtUsername);
        inputPassword=(EditText) findViewById(R.id.txtPassword);
        inputFirstName=(EditText) findViewById(R.id.txtFirstName);
        inputLastName=(EditText) findViewById(R.id.txtLastName);
        currentAccount=null;

        String[] roleChoices = new String[]{"Customer", "Employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roleChoices);
        inputRole.setAdapter(adapter);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    lookupAccount(v);
                    update();
                    }
                catch(Exception e){
                    screenAuth.setText(e.toString());
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    newAccount(v);
                    update();}
                catch(Exception e){
                    screenAuth.setText("Sign Up Failure");
                }           }
        });
    }

    private void update(){

        if (currentAccount!=null) {
            screenAuth.setText("Login Successful");
            Intent roleIntent;
            switch(currentAccount.getRole()){
                case "Admin":
                    roleIntent = new Intent(this, AdminActivity.class);
                    roleIntent.putExtra("username", currentAccount.getUsername());
                    roleIntent.putExtra("password", currentAccount.getPassword());
                    startActivity(roleIntent);
                    break;
                case "Employee":
                    roleIntent = new Intent(this, EmployeeActivity.class);
                    roleIntent.putExtra("username", currentAccount.getUsername());
                    roleIntent.putExtra("password", currentAccount.getPassword());
                    startActivity(roleIntent);
                    break;
                case "Customer":
                    roleIntent = new Intent(this, CustomerActivity.class);
                    roleIntent.putExtra("username", currentAccount.getUsername());
                    roleIntent.putExtra("password", currentAccount.getPassword());
                    startActivity(roleIntent);
                    break;
            }
        }
    }

    public void newAccount (View view) {

        UserAccount ua = UserAccount.createAccount(inputRole.getSelectedItem().toString(),
                new User(inputFirstName.getText().toString(),
                        inputLastName.getText().toString(),
                        inputUsername.getText().toString(),
                        inputPassword.getText().toString()));

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        if (dbHandler.usernameExists(ua.getUsername())){
            screenAuth.setText("The Username has already been taken");
        }
        else {
            dbHandler.addAccount(ua);
            inputPassword.setText("");
            inputFirstName.setText("");
            inputLastName.setText("");
            inputUsername.setText("");

            currentAccount=ua;
        }
    }


    public void lookupAccount (View view) {

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        UserAccount ua = dbHandler.findAccount(inputUsername.getText().toString(),
            inputPassword.getText().toString());

        if (ua != null) {
            inputFirstName.setText(String.valueOf(ua.getFirstName()));
            inputLastName.setText(String.valueOf(ua.getLastName()));
            currentAccount=ua;}
        else{
            screenAuth.setText("Login Unsuccessful");
        }
    }
}