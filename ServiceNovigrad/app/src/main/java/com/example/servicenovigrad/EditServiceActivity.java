package com.example.servicenovigrad;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.Arrays;

public class EditServiceActivity extends AppCompatActivity {

    TextView displayInstr;
    EditText editServiceName;
    Button btnSelService, btnChooseDoc, btnChooseField, btnRemoveDoc, btnRemoveField;
    Spinner spinnerAvailableFields, spinnerAvailableDocs, spinnerChosenFields, spinnerChosenDocs;
    Service selectedService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        displayInstr = (TextView) findViewById(R.id.instrDisplay);
        editServiceName = (EditText) findViewById(R.id.editServiceName);
        btnSelService = (Button) findViewById(R.id.btnSelectService);
        btnChooseDoc = (Button) findViewById(R.id.btnChooseDocs);
        btnChooseField = (Button) findViewById(R.id.btnAddFields);
        btnRemoveDoc = (Button) findViewById(R.id.btnRemoveDocs);
        btnRemoveField = (Button) findViewById(R.id.btnRemoveFields);
        spinnerAvailableFields = (Spinner) findViewById(R.id.availableFields);
        spinnerAvailableDocs = (Spinner) findViewById(R.id.availableDocs);
        spinnerChosenFields = (Spinner) findViewById(R.id.chosenFields);
        spinnerChosenDocs = (Spinner) findViewById(R.id.chosenDocs);

        btnSelService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectService(v);
                }
                catch(Exception e){
                    displayInstr.setText(e.toString());
                }
            }
        });

        btnChooseDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    chooseDoc(v);
                }
                catch(Exception e){
                    displayInstr.setText(e.toString());
                }
            }
        });

        btnRemoveDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    removeDoc(v);
                }
                catch(Exception e){
                    displayInstr.setText(e.toString());
                }
            }
        });

        btnChooseField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    chooseField(v);
                }
                catch(Exception e){
                    displayInstr.setText(e.toString());
                }
            }
        });

        btnRemoveField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    removeField(v);
                }
                catch(Exception e){
                    displayInstr.setText(e.toString());
                }
            }
        });

    }


    public void updateOptions(View view){
        if(selectedService==null){
            String[] emptyList = new String[0];
            Arrays.fill(emptyList, null);
            ArrayAdapter emptyAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, emptyList);
            spinnerAvailableFields.setAdapter(emptyAdapter);
            spinnerChosenDocs.setAdapter(emptyAdapter);
            spinnerChosenFields.setAdapter(emptyAdapter);
            spinnerAvailableDocs.setAdapter(emptyAdapter);
            return;
        }


        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayList<DocumentTemplate> allDocs= dbHandler.getAllDocuments();
        ArrayList<FieldTemplate> allFields= dbHandler.getAllFields();

        ArrayList<DocumentTemplate> chosenDocs = dbHandler.findAllRequirements(selectedService.getName());
        ArrayList<DocumentTemplate> availableDocs = new ArrayList<DocumentTemplate>();

        for (DocumentTemplate dt : allDocs){
            boolean isInChosen=false;
            for (DocumentTemplate dtChosen : chosenDocs){
                if (dt.getName().equals(dtChosen.getName())){isInChosen=true;}
            }
            if(!isInChosen){ availableDocs.add(dt); }
        }

        ArrayList<FieldTemplate> chosenFields = dbHandler.findAllInformation(selectedService.getName());
        ArrayList<FieldTemplate> availableFields = new ArrayList<FieldTemplate>();

        for (FieldTemplate ft : allFields){
            boolean isInChosen=false;
            for (FieldTemplate ftChosen : chosenFields){
                if (ft.getName().equals(ftChosen.getName())){isInChosen=true;}
            }
            if(!isInChosen){ availableFields.add(ft); }
        }

        ArrayAdapter<DocumentTemplate> chosenDocsAdapter = new ArrayAdapter<DocumentTemplate>(this,
                android.R.layout.simple_spinner_dropdown_item, chosenDocs);

        spinnerChosenDocs.setAdapter(chosenDocsAdapter);

        ArrayAdapter<DocumentTemplate> availableDocsAdapter = new ArrayAdapter<DocumentTemplate>(this,
                android.R.layout.simple_spinner_dropdown_item, availableDocs);
        spinnerAvailableDocs.setAdapter(availableDocsAdapter);

        ArrayAdapter<FieldTemplate> chosenFieldsAdapter = new ArrayAdapter<FieldTemplate>(this,
                android.R.layout.simple_spinner_dropdown_item, chosenFields);

        spinnerChosenFields.setAdapter(chosenFieldsAdapter);

        ArrayAdapter<FieldTemplate> availableFieldsAdapter = new ArrayAdapter<FieldTemplate>(this,
                android.R.layout.simple_spinner_dropdown_item, availableFields);
        spinnerAvailableFields.setAdapter(availableFieldsAdapter);
    }

    public void selectService(View view){
        if (TextUtils.isEmpty(editServiceName.getText().toString())) {
            displayInstr.setText("Enter a service");
        }

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        selectedService=dbHandler.findService(editServiceName.getText().toString());
        if (selectedService==null){
            selectedService=new Service(editServiceName.getText().toString(),
                    new ArrayList<DocumentTemplate>(), new ArrayList<FieldTemplate>());
            dbHandler.addService(selectedService);
            selectedService=dbHandler.findService(selectedService.getName());
        }
        updateOptions(view);
    }

    public void chooseDoc(View view){
        if(selectedService==null){
            displayInstr.setText("You need to select a Service");
            return;
        }
        if (spinnerAvailableDocs.getSelectedItem()==null){
            displayInstr.setText("Choose a Document to add");
            return;
        }

        DocumentTemplate dt = (DocumentTemplate) spinnerAvailableDocs.getSelectedItem();

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.addRequirements(selectedService.getId(),
                dt.getId());

        updateOptions(view);
    }

    public void removeDoc(View view){
        if(selectedService==null){
            displayInstr.setText("You need to select a Service");
            return;
        }
        if (spinnerChosenDocs.getSelectedItem()==null){
            displayInstr.setText("Choose a Document to Remove");
            return;
        }

        DocumentTemplate dt = (DocumentTemplate) spinnerChosenDocs.getSelectedItem();

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.deleteRequirements(selectedService.getId(),
                dt.getId());

        updateOptions(view);
    }

    public void chooseField(View view){
        if(selectedService==null){
            displayInstr.setText("You need to select a Service");
            return;
        }
        if (spinnerAvailableFields.getSelectedItem()==null){
            displayInstr.setText("Choose a Field to add");
            return;
        }

        FieldTemplate ft = (FieldTemplate) spinnerAvailableFields.getSelectedItem();

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.addInformation(selectedService.getId(),
                ft.getId());

        updateOptions(view);
    }

    public void removeField(View view){
        if(selectedService==null){
            displayInstr.setText("You need to select a Service");
            return;
        }
        if (spinnerChosenFields.getSelectedItem()==null){
            displayInstr.setText("Choose a Field to Remove");
            return;
        }

        FieldTemplate ft = (FieldTemplate) spinnerChosenFields.getSelectedItem();

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.deleteInformation(selectedService.getId(),
                ft.getId());

        updateOptions(view);
    }


}
