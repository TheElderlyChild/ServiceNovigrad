package com.example.servicenovigrad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

        catch(Exception e){Toast.makeText(getApplicationContext(), "Error loading request",Toast.LENGTH_SHORT).show();}

        fillSpinners();


        btnAddFieldValue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    addField();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error adding Field",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddDocValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addDocument();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error adding Document",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Error adding request",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReviewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewRequest();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error viewing request",Toast.LENGTH_SHORT).show();
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addField(){
        if(spinnerFields.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Select a field",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!InputValidator.valStringInput(inputFieldValue.getText().toString())){
            Toast.makeText(getApplicationContext(), "Enter a value for this field",Toast.LENGTH_SHORT).show();
            return;
        }

        FieldTemplate ft = (FieldTemplate) spinnerFields.getSelectedItem();

        if(!InputValidator.validateField(inputFieldValue.getText().toString().trim(),ft.getType())){
            Toast.makeText(getApplicationContext(), "Your input has the wrong format.\n" +
                    "Ensure dates are in dddd-MM-dd format\n" +
                    "And addresses are written properly",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Image Successfully loaded",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Error loading document",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    void addDocument(){
        openImageChooser();

        if(selectedImageBitmap==null){
            Toast.makeText(getApplicationContext(), "Select an Image",Toast.LENGTH_SHORT).show();
        }
        if(spinnerDocuments.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Select a Document",Toast.LENGTH_SHORT).show();
            return;}
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

}