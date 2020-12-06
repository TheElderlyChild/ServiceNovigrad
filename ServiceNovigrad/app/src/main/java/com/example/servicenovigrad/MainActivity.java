package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
                    Toast.makeText(getApplicationContext(), "Failed to Login",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Failed to Sign Up",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void update(){
        if (currentAccount==null) {return;}
        Toast.makeText(getApplicationContext(), "Login Successful!",Toast.LENGTH_SHORT).show();
        Intent roleIntent;
        switch(currentAccount.getRole()){
            case "Admin":
                roleIntent = new Intent(this, AdminActivity.class);
                roleIntent.putExtra("username", currentAccount.getUsername());
                roleIntent.putExtra("password", currentAccount.getPassword());
                currentAccount=null;
                startActivity(roleIntent);
                break;
            case "Employee":
                roleIntent = new Intent(this, EmployeeActivity.class);
                roleIntent.putExtra("username", currentAccount.getUsername());
                roleIntent.putExtra("password", currentAccount.getPassword());
                startActivity(roleIntent);
                currentAccount=null;
                break;
            case "Customer":
                roleIntent = new Intent(this, CustomerActivity.class);
                roleIntent.putExtra("username", currentAccount.getUsername());
                roleIntent.putExtra("password", currentAccount.getPassword());
                startActivity(roleIntent);
                currentAccount=null;
                break;
        }
    }

    public void newAccount (View view) {
        if (!InputValidator.valUsername(inputUsername.getText().toString())){
            Toast.makeText(getApplicationContext(), "Invalid Username. (Remove spaces between "+
                    "characters in the username",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!InputValidator.valPassword(inputPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "Invalid Password.\n"+
                    "Password must be between 8 and 20 characters\n"+
                    "Password must contain at least one digit, uppercase letter, lower case letter and special character\n"+
                    "Password must not have any spaces",Toast.LENGTH_LONG).show();
            return;
        }
        if (!InputValidator.valStringInput(inputFirstName.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please Enter a First Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!InputValidator.valStringInput(inputLastName.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please Enter a Last Name",Toast.LENGTH_SHORT).show();
            return;
        }

        UserAccount ua = UserAccount.createAccount(inputRole.getSelectedItem().toString(),
                new User(inputFirstName.getText().toString(),
                        inputLastName.getText().toString(),
                        inputUsername.getText().toString(),
                        inputPassword.getText().toString()));

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        if (dbHandler.usernameExists(ua.getUsername())){
            Toast.makeText(getApplicationContext(), "That username has been taken",Toast.LENGTH_SHORT).show();
        }
        else {
            dbHandler.addAccount(ua);
            clearFields();
            currentAccount=ua;
        }
    }

    public void clearFields(){
        inputPassword.setText("");
        inputFirstName.setText("");
        inputLastName.setText("");
        inputUsername.setText("");
    }

    public void lookupAccount (View view) {
        if (!InputValidator.valStringInput(inputUsername.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please Enter a Username",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!InputValidator.valStringInput(inputPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please Enter a Password.",Toast.LENGTH_LONG).show();
            return;
        }
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        UserAccount ua = dbHandler.findAccount(inputUsername.getText().toString(),
            inputPassword.getText().toString());
        if (ua != null) {
            clearFields();
            currentAccount=ua;}
        else{
            Toast.makeText(MainActivity.this, "Login Unsuccessful",Toast.LENGTH_SHORT).show();
        }
    }
}