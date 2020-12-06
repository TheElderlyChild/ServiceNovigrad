package com.example.servicenovigrad;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.userManagement.Customer;
import com.example.servicenovigrad.userManagement.Employee;
import com.example.servicenovigrad.userManagement.Workday;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CustomerActivity extends AppCompatActivity {

    Button btnSearchWorkHour, btnSearchService, btnTimePicker, btnSelectBranch, btnChooseService, btnMakeRequest;
    Spinner spinnerSearchService, spinnerWeekday, spinnerChooseBranch, spinnerChooseService;
    TextView displayBranch;
    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    Customer currentAccount;
    Employee selectedEmployee;
    Service selectedService;
    int selectHour;
    int selectMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        btnSearchService = (Button) findViewById(R.id.btnSearchService);
        btnSearchWorkHour = (Button) findViewById(R.id.btnSearchWorkHour);
        btnTimePicker=(Button) findViewById(R.id.btnTimePicker);
        btnSelectBranch=(Button) findViewById(R.id.btnSelectBranch);
        btnChooseService=(Button) findViewById(R.id.btnChooseService);
        btnMakeRequest=(Button) findViewById(R.id.btnMakeRequest);
        displayBranch=(TextView) findViewById(R.id.displayBranch);
        spinnerSearchService=(Spinner) findViewById(R.id.spinnerSearchService);
        spinnerChooseBranch=(Spinner) findViewById(R.id.spinnerChooseBranch);
        spinnerChooseService=(Spinner) findViewById(R.id.spinnerChooseService);
        spinnerWeekday=(Spinner) findViewById(R.id.spinnerWeekday);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        currentAccount = (Customer) dbHandler.findAccount(username, password);

        fillWeekday();
        try {
            fillServiceSearchBar();
        }
        catch(Exception e){
            displayBranch.setText(e.toString());
        }

        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this,
                android.R.layout.simple_spinner_dropdown_item, dbHandler.getEmployeeList());
        spinnerChooseBranch.setAdapter(adapter);

        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourofday, int minute) {
                selectHour = hourofday;
                selectMinute = minute;
            }
        };

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar =  Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);


                TimePickerDialog mTimePickerDialog = new TimePickerDialog(
                        CustomerActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnTimeSetListener,
                        hour,minute,true);

                mTimePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePickerDialog.show();

            }
        });

        btnSelectBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectBranch();
                }
                catch(Exception e){
                    displayBranch.setText(e.toString());
                }
            }
        });

        btnChooseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectService();
                }
                catch(Exception e){
                    displayBranch.setText(e.toString());
                }
            }
        });

        btnMakeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    makeRequest();
                }
                catch(Exception e){
                    displayBranch.setText(e.toString());
                }
            }
        });

    }

    public void fillWeekday(){
        ArrayAdapter<String> weekdayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Workday.daysInString);
        spinnerWeekday.setAdapter(weekdayAdapter);
    }

    public void fillServiceSearchBar(){
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayAdapter<Service> serviceArrayAdapter = new ArrayAdapter<Service>(this,
                android.R.layout.simple_spinner_dropdown_item, dbHandler.getAllServices());
        spinnerSearchService.setAdapter(serviceArrayAdapter);
    }

    public void selectBranch(){
        if (spinnerChooseBranch.getSelectedItem()==null){return;}
        selectedEmployee = (Employee) spinnerChooseBranch.getSelectedItem();
        updateBranchOptions();
    }

    public void selectService(){
        if (spinnerChooseService.getSelectedItem()==null){return;}
        selectedService = (Service) spinnerChooseService.getSelectedItem();
    }

    public void updateBranchOptions(){
        if (selectedEmployee==null){
            String[] emptyList = new String[0];
            Arrays.fill(emptyList, null);
            ArrayAdapter emptyAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, emptyList);
            spinnerChooseService.setAdapter(emptyAdapter);
            return;
        }

        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayList<String[]> offerings = dbHandler.findOfferings(selectedEmployee.getUsername());
        ArrayList<Service> availableServices = new ArrayList<Service>();
        for(String[] offering : offerings){
            if(offering[1].equals("1")){
                availableServices.add(dbHandler.findService(offering[0]));
            }
        }

        ArrayAdapter<Service> serviceAdapter = new ArrayAdapter<Service>(this,
                android.R.layout.simple_spinner_item, availableServices);
        spinnerChooseService.setAdapter(serviceAdapter);
    }

    public void makeRequest(){
        if (selectedService==null || selectedEmployee==null){
            displayBranch.setText("Select a service to view");
            return;
        }

        Intent intent = new Intent(this, MakeRequestActivity.class);
        Service service = (Service) selectedService;
        intent.putExtra("serviceName", service.getName());
        intent.putExtra("username", currentAccount.getUsername());
        intent.putExtra("password", currentAccount.getPassword());
        intent.putExtra("branchUsername", selectedEmployee.getUsername());
        intent.putExtra("branchPassword", selectedEmployee.getPassword());
        startActivity(intent);
    }
}