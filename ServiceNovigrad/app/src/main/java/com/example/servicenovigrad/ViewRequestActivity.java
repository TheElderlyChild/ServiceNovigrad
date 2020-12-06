package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.services.FieldTemplate;
import com.example.servicenovigrad.services.ServiceRequest;

public class ViewRequestActivity extends AppCompatActivity {
    TextView displayFields;
    Spinner spinnerDocs;
    ImageView docImage;
    ServiceRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        displayFields=(TextView) findViewById(R.id.displayFields);
        spinnerDocs=(Spinner) findViewById(R.id.spinnerDocs);
        docImage=(ImageView) findViewById(R.id.docImage);

        Intent intent = getIntent();
        int reqId = intent.getIntExtra("requestId",0);
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        if(reqId==0){finish();}

        try{request=dbHandler.findServiceRequest(reqId);
        }
        catch(Exception e){displayFields.setText(e.toString());}
        dbHandler.fillRequestWithData(request);
        fillFields();
        fillDocuments();

        spinnerDocs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateImage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    public void fillFields(){
        String info=request.toString()+"\n";
        for (ServiceRequest.Field field : request.getInformation()){
            for(FieldTemplate template : request.getService().getInformation()){
                if(template.getId()==field.getFieldId()){
                    info+=template.getName()+": ";
                }
            }
            info+=field.getValue()+"\n";
        }
        displayFields.setText(info);
    }

    public void updateImage(){
        if(spinnerDocs.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Select a document",Toast.LENGTH_SHORT).show();
            return;}
        ServiceRequest.Document doc=(ServiceRequest.Document) spinnerDocs.getSelectedItem();
        docImage.setImageBitmap(doc.getValue());
    }

    public void fillDocuments(){
        ArrayAdapter<ServiceRequest.Document> docArrayAdapter = new ArrayAdapter<ServiceRequest.Document>(this,
                android.R.layout.simple_spinner_dropdown_item, request.getRequirements());
        spinnerDocs.setAdapter(docArrayAdapter);
    }
}