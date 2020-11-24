package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.userManagement.Admin;
import com.example.servicenovigrad.userManagement.Employee;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminActivity extends AppCompatActivity {

    Button btnSelectEmployee, btnChooseEmployee, btnRemoveEmployee, btnDeleteEmployee, btnAddEdit;
    Spinner spinnerEmployee;
    TextView textDisplay;
    Spinner spinnerAvailable, spinnerChosen;
    Admin currentAccount;
    Employee selectedEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnSelectEmployee= (Button) findViewById(R.id.btnSelect);
        btnChooseEmployee= (Button) findViewById(R.id.btnChoose);
        btnRemoveEmployee= (Button) findViewById(R.id.btnRemove);
        btnDeleteEmployee= (Button) findViewById(R.id.btnDelEmployee);
        btnAddEdit= (Button) findViewById(R.id.btnAddEdit);
        spinnerEmployee = (Spinner) findViewById(R.id.txtEmployee);
        textDisplay= (TextView) findViewById(R.id.displayMsg);
        spinnerAvailable = (Spinner) findViewById(R.id.selectAvailable);
        spinnerChosen = (Spinner) findViewById(R.id.selectChosen);
        selectedEmployee=null;

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        currentAccount = (Admin) dbHandler.findAccount(username, password);

        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this,
                android.R.layout.simple_spinner_dropdown_item, dbHandler.getEmployeeList());
        spinnerEmployee.setAdapter(adapter);

        btnSelectEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectEmployee(v);
                }
                catch(Exception e){
                    textDisplay.setText(e.toString());
                }
            }
        });

        btnChooseEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    addServiceToEmployee(v);
                }
                catch(Exception e){
                    textDisplay.setText(e.toString());
                }
            }
        });

        btnRemoveEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    removeServiceFromEmployee(v);
                }
                catch(Exception e){
                    textDisplay.setText(e.toString());
                }
            }
        });

        btnDeleteEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    deleteEmployee(v);
                }
                catch(Exception e){
                    textDisplay.setText(e.toString());
                }
            }
        });
    }

    public void selectEmployee (View view) {
        if (spinnerEmployee.getSelectedItem()==null){
            textDisplay.setText("Choose an Employee");
            return;
        }
        selectedEmployee = (Employee) spinnerEmployee.getSelectedItem();
        updateOptions(view);

    }

    public void addServiceToEmployee (View view) {
        if(selectedEmployee==null){
            textDisplay.setText("You need to select an Employee");
            return;
        }
        if (spinnerAvailable.getSelectedItem()==null){
            textDisplay.setText("Choose a Service to Add");
            return;
        }

        Service selService= (Service) spinnerAvailable.getSelectedItem();

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.addOffering(selectedEmployee.getUsername(),
                selService.getId());

        updateOptions(view);
    }

    public void removeServiceFromEmployee (View view) {
        if(selectedEmployee==null){
            textDisplay.setText("You need to select an Employee");
            return;
        }
        if (spinnerChosen.getSelectedItem()==null){
            textDisplay.setText("Choose a Service to Remove");
            return;
        }

        Service selService= (Service) spinnerChosen.getSelectedItem();

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.deleteOffering(selectedEmployee.getUsername(),
                selService.getId());

        updateOptions(view);
    }

    public void deleteEmployee (View view) {
        if (selectedEmployee==null){
            textDisplay.setText("Choose an Employee");
            return;
        }
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.deleteAccount(selectedEmployee.getUsername(),selectedEmployee.getPassword());

        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this,
                android.R.layout.simple_spinner_dropdown_item, dbHandler.getEmployeeList());
        spinnerEmployee.setAdapter(adapter);

        selectedEmployee=null;
        updateOptions(view);

    }

    public void updateOptions(View view){
        if(selectedEmployee==null){
            String[] emptyList = new String[0];
            Arrays.fill(emptyList, null);
            ArrayAdapter emptyAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, emptyList);
            spinnerAvailable.setAdapter(emptyAdapter);
            spinnerChosen.setAdapter(emptyAdapter);
            return;
        }


        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayList<Service> all= dbHandler.getAllServices();

        ArrayList<String[]> offerings = dbHandler.findOfferings(selectedEmployee.getUsername());
        ArrayList<Service> offered = new ArrayList<Service>();
        ArrayList<Service> available = new ArrayList<Service>();


        for (Service service : all){
            boolean isInOfferings=false;
            for (String[] offering : offerings){
                if (service.getName().equals(offering[0])){isInOfferings=true;}
            }
            if(isInOfferings){ offered.add(service); }
            else{available.add(service);}
        }

        ArrayAdapter<Service> chosenAdapter = new ArrayAdapter<Service>(this,
                android.R.layout.simple_spinner_dropdown_item, offered);

        spinnerChosen.setAdapter(chosenAdapter);

        ArrayAdapter<Service> availableAdapter = new ArrayAdapter<Service>(this,
                android.R.layout.simple_spinner_dropdown_item, available);
        spinnerAvailable.setAdapter(availableAdapter);
    }


}