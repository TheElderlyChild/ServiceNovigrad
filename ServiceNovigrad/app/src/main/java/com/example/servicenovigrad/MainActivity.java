package com.example.servicenovigrad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.userManagement.UserAccount;

public class MainActivity extends AppCompatActivity {
    Button buttonSignUp, buttonLogin;
    TextView screenAuth, screenWelcome;
    EditText inputUsername, inputPassword;
    UserAccount currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}