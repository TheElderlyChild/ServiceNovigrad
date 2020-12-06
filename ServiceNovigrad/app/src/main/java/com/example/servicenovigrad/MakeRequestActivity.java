package com.example.servicenovigrad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.services.DocumentTemplate;
import com.example.servicenovigrad.services.FieldTemplate;
import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.services.ServiceRequest;
import com.example.servicenovigrad.userManagement.Customer;
import com.example.servicenovigrad.userManagement.Employee;

import java.io.IOException;

public class MakeRequestActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;

    TextView displayInfo;
    Button btnAddFieldValue, btnAddDocValue, btnAddRequest, btnReviewRequest;
    Spinner spinnerFields, spinnerDocuments;
    EditText inputFieldValue;
    Customer currentAccount;
    Employee selectedBranch;
    Service selectedService;
    ServiceRequest request;
    Uri selectedImageUri;
    Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);

        displayInfo=(TextView) findViewById(R.id.displayInfo);
        btnAddFieldValue=(Button) findViewById(R.id.btnAddFieldValue);
        btnAddDocValue=(Button) findViewById(R.id.btnAddDocValue);
        btnAddRequest=(Button) findViewById(R.id.btnAddRequest);
        btnReviewRequest=(Button) findViewById(R.id.btnReviewRequest);
        spinnerFields=(Spinner) findViewById(R.id.spinnerFields);
        spinnerDocuments=(Spinner) findViewById(R.id.spinnerDocuments);
        inputFieldValue=(EditText) findViewById(R.id.inputFieldValue);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        currentAccount = (Customer) dbHandler.findAccount(username, password);

        username = intent.getStringExtra("branchUsername");
        password = intent.getStringExtra("branchPassword");

        selectedBranch = (Employee) dbHandler.findAccount(username, password);

        String serviceName=intent.getStringExtra("serviceName");
        selectedService = (Service) dbHandler.findService(serviceName);

        request=new ServiceRequest(-1,selectedBranch.getUsername(),currentAccount.getUsername(),selectedService,0);
        dbHandler.makeServiceRequest(request);

        try {
            request=dbHandler.findRequest(currentAccount.getUsername(),selectedBranch.getUsername(),selectedService.getId());
            displayInfo.setText(request.toString());
            dbHandler.fillRequestWithData(request);}

        catch(Exception e){displayInfo.setText(e.toString());}

        fillSpinners();


        btnAddFieldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addField();
                } catch (Exception e) {
                    displayInfo.setText(e.toString());
                }
            }
        });

        btnAddDocValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addDocument();
                } catch (Exception e) {
                    displayInfo.setText(e.toString());
                }
            }
        });

        btnAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    makeRequest();
                    //displayInfo.setText(request.toString()+request.getInformation().toString()+ request.getRequirements().toString());
                } catch (Exception e) {
                    displayInfo.setText(e.toString());
                }
            }
        });

        btnReviewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewRequest();
                } catch (Exception e) {
                    displayInfo.setText(e.toString());
                }
            }
        });

    }

    public void fillSpinners(){
        ArrayAdapter<FieldTemplate> fieldArrayAdapter = new ArrayAdapter<FieldTemplate>(this,
                android.R.layout.simple_spinner_dropdown_item, request.getService().getInformation());
        spinnerFields.setAdapter(fieldArrayAdapter);

        ArrayAdapter<DocumentTemplate> docArrayAdapter = new ArrayAdapter<DocumentTemplate>(this,
                android.R.layout.simple_spinner_dropdown_item, request.getService().getRequirements());
        spinnerDocuments.setAdapter(docArrayAdapter);
    }


    public void addField(){
        if(!validateField(inputFieldValue.getText().toString())){
            return;
        }
        if(spinnerFields.getSelectedItem()==null){return;}
        FieldTemplate template = (FieldTemplate) spinnerFields.getSelectedItem();

        ServiceRequest.Field selectField=null;
        for (ServiceRequest.Field field : request.getInformation()){
                if(field.getFieldId()==template.getId()){
                    selectField=field;
                }
        }
        if (selectField==null){
            request.getInformation().add(new ServiceRequest.Field(template.getId(),inputFieldValue.getText().toString()));
        }
        else{
            selectField.setValue(inputFieldValue.getText().toString());
        }
    }

    // Choose an image from Gallery
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        displayInfo.setText(e.toString());
                    }
                }
            }
        }
    }

    void addDocument(){
        openImageChooser();

        if(selectedImageBitmap==null){
            displayInfo.setText("Select an image");
        }
        if(spinnerDocuments.getSelectedItem()==null){return;}
        DocumentTemplate template = (DocumentTemplate) spinnerDocuments.getSelectedItem();

        ServiceRequest.Document selectDocument=null;
        for (ServiceRequest.Document doc : request.getRequirements()){
            if(doc.getDocId()==template.getId()){
                selectDocument=doc;
            }
        }
        if (selectDocument==null){
            request.getRequirements().add(new ServiceRequest.Document(template.getId(),selectedImageBitmap));
        }
        else{
            selectDocument.setValue(selectedImageBitmap);
        }
        selectedImageBitmap=null;
    }

    public void viewRequest(){
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.updateDataFromRequest(request);
        dbHandler.fillRequestWithData(request);
        Intent viewIntent = new Intent(this, ViewRequestActivity.class);
        viewIntent.putExtra("requestId", request.getId());
        startActivity(viewIntent);

    }

    public void makeRequest(){
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.updateDataFromRequest(request);
    }


    public boolean validateField(String input){
        return true;
    }
}