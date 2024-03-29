package com.example.servicenovigrad;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.services.ServiceRequest;
import com.example.servicenovigrad.userManagement.Customer;
import com.example.servicenovigrad.userManagement.Employee;
import com.example.servicenovigrad.userManagement.Workday;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CustomerActivity extends AppCompatActivity {

    Button btnSearchWorkHour, btnSearchService, btnTimePicker, btnSelectBranch, btnChooseService, btnMakeRequest, btnRate;
    Spinner spinnerSearchService, spinnerWeekday, spinnerChooseBranch, spinnerChooseService;
    TextView displayBranch;
    RatingBar branchRating;
    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    Customer currentAccount;
    Employee selectedEmployee;
    Service selectedService;
    int selectHour=-1;
    int selectMinute=-1;

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
        btnRate=(Button) findViewById(R.id.btnRate);
        displayBranch=(TextView) findViewById(R.id.displayBranch);
        spinnerSearchService=(Spinner) findViewById(R.id.spinnerSearchService);
        spinnerChooseBranch=(Spinner) findViewById(R.id.spinnerChooseBranch);
        spinnerChooseService=(Spinner) findViewById(R.id.spinnerChooseService);
        spinnerWeekday=(Spinner) findViewById(R.id.spinnerWeekday);
        branchRating=(RatingBar) findViewById(R.id.branchRating);

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
            Toast.makeText(getApplicationContext(), "Error loading services",Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this,
                android.R.layout.simple_spinner_dropdown_item, dbHandler.getEmployeeList());
        spinnerChooseBranch.setAdapter(adapter);

        String alertMsg=requestAlert();
        if(!alertMsg.equals("")){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerActivity.this);
            builder1.setMessage(alertMsg);
            builder1.setCancelable(true);
            AlertDialog alert = builder1.create();
            alert.show();
        }

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
                    Toast.makeText(getApplicationContext(), "Error selecting branch",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Error selecting service",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Error making request",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSearchService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    branchServiceSearch();
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error searching for service",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSearchWorkHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try{
                    Toast.makeText(getApplicationContext(), "Error searching for specific time",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    displayBranch.setText(e.toString());
                }
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    makeRating();
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error rating branch",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String requestAlert(){
        String result="";
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        ArrayList<ServiceRequest> requestList = dbHandler.findAllCustomerRequests(currentAccount.getUsername());
        for(ServiceRequest request: requestList){
            if(request.getApproved()==1){
                result=result+request.toString()+" has been accepted\n";
            }
            else if(request.getApproved()==-1){
                result=result+request.toString()+" has been rejected\n";
            }
        }
        return result;
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
        if (spinnerChooseBranch.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Choose a Branch",Toast.LENGTH_SHORT).show();
            return;}
        selectedEmployee = (Employee) spinnerChooseBranch.getSelectedItem();
        updateBranchOptions();
        updateRating();
    }

    public void selectService(){
        if (spinnerChooseService.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Choose a service",Toast.LENGTH_SHORT).show();
            return;}
        selectedService = (Service) spinnerChooseService.getSelectedItem();
    }

    public void branchServiceSearch(){
        if (spinnerSearchService.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Choose a Service to search for",Toast.LENGTH_SHORT).show();
            return;}
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        Service service = (Service) spinnerSearchService.getSelectedItem();
        ArrayList<Employee> ale = dbHandler.getEmployeeList();
        ArrayList<Employee> newAle = new ArrayList<Employee>();

        for(Employee branch: ale){
            for(String[] offering : dbHandler.findOfferings(branch.getUsername())){
                if(offering[0].equals(service.getName()) && offering[1].equals("1")){
                    newAle.add(branch);
                }
            }
        }
        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this,
                android.R.layout.simple_spinner_dropdown_item, newAle);
        spinnerChooseBranch.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void branchWorkDaySearch(){
        if (spinnerWeekday.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Select a Weekday",Toast.LENGTH_SHORT).show();
            return;}
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        String day = (String) spinnerWeekday.getSelectedItem();
        ArrayList<Employee> ale = dbHandler.getEmployeeList();
        ArrayList<Employee> newAle = new ArrayList<Employee>();

        for(Employee branch: ale){
            dbHandler.fillEmployeeWithData(branch);
            for(Workday workday : branch.getWorkdays()){
                if(Workday.daysInString[workday.getDay()]==day && workday.isAvailable()){
                    newAle.add(branch);
                }
            }
        }

        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this,
                android.R.layout.simple_spinner_dropdown_item, newAle);
        spinnerChooseBranch.setAdapter(adapter);

        if (selectHour==-1 || selectMinute==-1){
            Toast.makeText(getApplicationContext(), "Select A Time Of Availability for a more refined search",Toast.LENGTH_SHORT).show();
            return;}

        for(Employee branch: newAle){
            boolean compareStart=LocalTime.of(selectHour,selectMinute).compareTo(branch.getStartTime())>=0;
            boolean compareEnd=LocalTime.of(selectHour,selectMinute).compareTo(branch.getEndTime())<0;
            ArrayList<Employee> secondAle = new ArrayList<Employee>();

            if (compareStart && compareEnd){
                    secondAle.add(branch);
            }

            adapter = new ArrayAdapter<Employee>(this,
                    android.R.layout.simple_spinner_dropdown_item, secondAle);
            spinnerChooseBranch.setAdapter(adapter);
        }

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
            Toast.makeText(getApplicationContext(), "Select a Service to view",Toast.LENGTH_SHORT).show();
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

    public void updateRating(){
        if(selectedEmployee==null){return;}
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        branchRating.setRating(dbHandler.findRating(selectedEmployee.getUsername(),currentAccount.getUsername()));
        displayBranch.setText(selectedEmployee.toString()+" "+
                String.valueOf(dbHandler.findAverageRating(selectedEmployee.getUsername())));
    }

    public void makeRating(){
        if(selectedEmployee==null){
            Toast.makeText(getApplicationContext(), "Select a branch to rate",Toast.LENGTH_SHORT).show();
            return;
        }
        NovigradDBHandler dbHandler = new NovigradDBHandler(this);
        dbHandler.addRating(selectedEmployee.getUsername(),currentAccount.getUsername(),branchRating.getRating());
        updateRating();
    }
}