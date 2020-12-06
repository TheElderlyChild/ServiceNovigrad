package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.services.DocumentTemplate;
import com.example.servicenovigrad.services.FieldTemplate;
import com.example.servicenovigrad.services.Service;

public class ServiceActivity extends AppCompatActivity {

    TextView textService, textInfo, textDoc;
    Service currentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        textService= (TextView) findViewById(R.id.serviceText);
        textInfo= (TextView) findViewById(R.id.infoText);
        textDoc= (TextView) findViewById(R.id.docText);

        Intent intent = getIntent();
        String serviceName = intent.getStringExtra("serviceName");
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        currentService=dbHandler.findService(serviceName);
        textService.setText(textService.getText()+": "+currentService.getName());

        fillInformation();
        fillRequirements();

    }

    public void fillInformation(){
        if(currentService==null){
            Toast.makeText(getApplicationContext(), "Could not find service",Toast.LENGTH_SHORT).show();
            finish();
            return;}
        String text = textInfo.getText()+": \n";
        for(FieldTemplate field : currentService.getInformation()){
            text = text + field.getName()+ "\n";
        }

        textInfo.setText(text);
    }

    public void fillRequirements(){
        if(currentService==null){
            Toast.makeText(getApplicationContext(), "Could not find service",Toast.LENGTH_SHORT).show();
            finish();
            return;}
        String text = textDoc.getText()+": \n";
        for(DocumentTemplate doc : currentService.getRequirements()){
            text = text + doc.getName()+ " - " + doc.getDescription()+
                    "\n";
        }
        textDoc.setText(text);
    }

}