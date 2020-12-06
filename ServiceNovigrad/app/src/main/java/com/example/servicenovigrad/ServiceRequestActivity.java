package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.services.ServiceRequest;
import com.example.servicenovigrad.userManagement.Employee;

import java.util.ArrayList;

public class ServiceRequestActivity extends AppCompatActivity {

    TextView displayText;
    Button btnChooseRequest, btnRejectRequest, btnApproveRequest, btnViewRequestInfo;
    Spinner spinnerAvailableRequests;
    Employee currentAccount;
    ServiceRequest chosenRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);

        displayText=(TextView) findViewById(R.id.textDisplay);
        btnChooseRequest=(Button) findViewById(R.id.btnChooseRequest);
        btnRejectRequest=(Button) findViewById(R.id.btnRejectRequest);
        btnApproveRequest=(Button) findViewById(R.id.btnApproveRequest);
        btnViewRequestInfo=(Button) findViewById(R.id.btnViewInfo);
        spinnerAvailableRequests=(Spinner) findViewById(R.id.availableRequests);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        currentAccount = (Employee) dbHandler.findAccount(username, password);
        try{
            updateRequestOptions();
        }
        catch(Exception e){displayText.setText(e.toString());}

        btnChooseRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectRequest(v);
                }
                catch(Exception e){
                    displayText.setText(e.toString());
                }
            }
        });

        btnRejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    rejectRequest(v);
                }
                catch(Exception e){
                    displayText.setText(e.toString());
                }
            }
        });

        btnApproveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    approveRequest(v);
                }
                catch(Exception e){
                    displayText.setText(e.toString());
                }
            }
        });

        btnViewRequestInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewInfo();
                } catch (Exception e) {
                    displayText.setText(e.toString());
                }
            }
        });

    }

    public void updateRequestOptions(){
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayList<ServiceRequest> requests=dbHandler.findAllServiceRequests(currentAccount.getUsername());
        displayText.setText(requests.toString());
        ArrayAdapter<ServiceRequest> offeringAdapter = new ArrayAdapter<ServiceRequest>(this,
                android.R.layout.simple_spinner_item, requests);
        spinnerAvailableRequests.setAdapter(offeringAdapter);
    }

    public void approveRequest(View view){
        if(chosenRequest==null){
            return;
        }
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.updateRequestApproval(chosenRequest.getId(), 1);
    }

    public void viewInfo(){
        Intent viewIntent = new Intent(this, ViewRequestActivity.class);
        viewIntent.putExtra("requestId", chosenRequest.getId());
        startActivity(viewIntent);
    }

    public void rejectRequest(View view){
        if(chosenRequest==null){
            return;
        }
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.updateRequestApproval(chosenRequest.getId(), -1);
    }

    public void selectRequest(View view){
        if(spinnerAvailableRequests.getSelectedItem()==null){
            return;
        }
        chosenRequest=(ServiceRequest) spinnerAvailableRequests.getSelectedItem();
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.fillRequestWithData(chosenRequest);
        displayText.setText(chosenRequest.toString());
    }
}
