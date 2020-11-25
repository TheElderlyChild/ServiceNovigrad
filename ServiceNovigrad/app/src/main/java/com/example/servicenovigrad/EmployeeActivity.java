package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.userManagement.Employee;
import com.example.servicenovigrad.userManagement.Workday;

import java.time.LocalTime;
import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {

    Button btnViewService, btnViewRequests, btnSetTime, btnGetTime;
    TimePicker pickOpenTime, pickCloseTime;
    TextView labelOpen, labelClosed, commentDisplay;
    Spinner spinnerAvailableServices, spinnerDaySelect;
    Switch switchOffered, switchOpen;
    Employee currentAccount;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        btnViewService = (Button) findViewById(R.id.btnViewService);
        btnViewRequests=(Button) findViewById(R.id.btnViewRequests);
        btnSetTime=(Button) findViewById(R.id.btnSetTime);
        btnGetTime=(Button) findViewById(R.id.btnGetTime);
        pickOpenTime=(TimePicker) findViewById(R.id.openTime);
        pickCloseTime=(TimePicker) findViewById(R.id.closeTime);
        labelOpen=(TextView) findViewById(R.id.openTimeLabel);
        labelClosed=(TextView) findViewById(R.id.closeTimeLabel);
        commentDisplay=(TextView) findViewById(R.id.displayComment);
        spinnerAvailableServices=(Spinner) findViewById(R.id.availableServices);
        spinnerDaySelect=(Spinner) findViewById(R.id.daySelect);
        switchOffered=(Switch) findViewById(R.id.switchOffered);
        switchOpen=(Switch) findViewById(R.id.switchOpen);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        currentAccount = (Employee) dbHandler.findAccount(username, password);


        try{
            fillWorkdaySpinner();
        }
        catch(Exception e){commentDisplay.setText(e.toString());}

        try{ fillOfferingSpinner(); }
        catch(Exception e){commentDisplay.setText(e.toString());}

        switchOffered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try{updateOfferingWithSwitch(buttonView, isChecked);;}
                catch(Exception e){commentDisplay.setText(e.toString());}
            }
        });

        switchOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try{updateWorkdayWithSwitch(buttonView, isChecked); }
                catch(Exception e){commentDisplay.setText(e.toString());}
            }
        });


        spinnerAvailableServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateSwitchWithOffering();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        spinnerDaySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateSwitchWithWorkday();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        btnViewService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    viewService(v);
                }
                catch(Exception e){
                    commentDisplay.setText(e.toString());
                }
            }
        });

        btnViewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    viewServiceRequests(v);
                }
                catch(Exception e){
                    commentDisplay.setText(e.toString());
                }
            }
        });

        btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    setToTime(v);
                }
                catch(Exception e){
                    commentDisplay.setText(e.toString());
                }
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    setTimes(v);
                }
                catch(Exception e){
                    commentDisplay.setText(e.toString());
                }
            }
        });

    }

    public void fillOfferingSpinner(){
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayList<String[]> offerings = dbHandler.findOfferings(currentAccount.getUsername());
        ArrayList<Employee.Offering> offeringsClass = new ArrayList<Employee.Offering>();
        Employee.Offering offeringClass;
        for(String[] offering : offerings){
            offeringClass=new Employee.Offering(
                    dbHandler.findService(offering[0]), offering[1].equals("1"));
            offeringsClass.add(offeringClass);
        }

        ArrayAdapter<Employee.Offering> offeringAdapter = new ArrayAdapter<Employee.Offering>(this,
                android.R.layout.simple_spinner_item, offeringsClass);
        spinnerAvailableServices.setAdapter(offeringAdapter);
    }

    public void updateOfferingSpinner(){
        if(spinnerAvailableServices.getSelectedItem()==null){return;}
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayList<String[]> offerings = dbHandler.findOfferings(currentAccount.getUsername());
        ArrayList<Employee.Offering> offeringsClass = new ArrayList<Employee.Offering>();
        Employee.Offering offeringClass;
        for(String[] offering : offerings){
            offeringClass=new Employee.Offering(
                    dbHandler.findService(offering[0]), offering[1].equals("1"));
            offeringsClass.add(offeringClass);
        }

        int position=spinnerAvailableServices.getSelectedItemPosition();
        ArrayAdapter<Employee.Offering> offeringAdapter = new ArrayAdapter<Employee.Offering>(this,
                android.R.layout.simple_spinner_item, offeringsClass);
        spinnerAvailableServices.setAdapter(offeringAdapter);
        spinnerAvailableServices.setSelection(position);
    }

    public void updateOfferingWithSwitch(CompoundButton buttonView, boolean isChecked){
        if(spinnerAvailableServices.getSelectedItem()==null){return;}
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        Employee.Offering offering = (Employee.Offering) spinnerAvailableServices.getSelectedItem();
        dbHandler.updateOffering(currentAccount.getUsername(), offering.getService().getId(), isChecked);
        updateOfferingSpinner();
    }

    public void updateSwitchWithOffering(){
        Employee.Offering offering = (Employee.Offering) spinnerAvailableServices.getSelectedItem();
        switchOffered.setChecked(offering.getIsProvided());
    }

    public void viewService(View view){
        if (spinnerAvailableServices.getSelectedItem()==null){
            commentDisplay.setText("Select a service to view");
            return;
        }

        Intent serviceIntent = new Intent(this, ServiceActivity.class);
        Employee.Offering offering = (Employee.Offering) spinnerAvailableServices.getSelectedItem();
        serviceIntent.putExtra("serviceName", offering.getService().getName());
        startActivity(serviceIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setToTime(View view){
        pickOpenTime.setHour(currentAccount.getStartTime().getHour());
        pickOpenTime.setMinute(currentAccount.getStartTime().getMinute());
        pickCloseTime.setHour(currentAccount.getEndTime().getHour());
        pickCloseTime.setMinute(currentAccount.getEndTime().getMinute());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTimes(View view){
        if (LocalTime.of(pickOpenTime.getHour(),pickOpenTime.getMinute()).compareTo(
                LocalTime.of(pickCloseTime.getHour(),pickCloseTime.getMinute()))>=0){
            commentDisplay.setText("Start Time cannot be more than or equal to end time");
            return;
        }
        currentAccount.setStartTime(LocalTime.of(pickOpenTime.getHour(),pickOpenTime.getMinute()));
        currentAccount.setEndTime(LocalTime.of(pickCloseTime.getHour(),pickCloseTime.getMinute()));
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.updateDataFromEmployee(currentAccount);
        int position=spinnerDaySelect.getSelectedItemPosition();
        fillWorkdaySpinner();
        spinnerDaySelect.setSelection(position);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fillWorkdaySpinner(){
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.fillEmployeeWithData(currentAccount);
        ArrayAdapter<Workday> workdayAdapter = new ArrayAdapter<Workday>(this,
                android.R.layout.simple_spinner_item, currentAccount.getWorkdays());
        spinnerDaySelect.setAdapter(workdayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateWorkdayWithSwitch(CompoundButton buttonView, boolean isChecked){
        if(spinnerDaySelect.getSelectedItem()==null){return;}
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);

        Workday workday= (Workday) spinnerDaySelect.getSelectedItem();
        workday.setAvailable(isChecked);
        dbHandler.updateDataFromEmployee(currentAccount);
        int position=spinnerDaySelect.getSelectedItemPosition();
        fillWorkdaySpinner();
        spinnerDaySelect.setSelection(position);

    }

    public void updateSwitchWithWorkday(){
        if(spinnerDaySelect.getSelectedItem()==null){return;}
        Workday workday= (Workday) spinnerDaySelect.getSelectedItem();
        switchOpen.setChecked(workday.isAvailable());
    }

    public void viewServiceRequests(View view){

        Intent serviceRequestIntent = new Intent(this, ServiceRequestActivity.class);
        serviceRequestIntent.putExtra("username", currentAccount.getUsername());
        serviceRequestIntent.putExtra("password", currentAccount.getPassword());
        startActivity(serviceRequestIntent);
    }



}