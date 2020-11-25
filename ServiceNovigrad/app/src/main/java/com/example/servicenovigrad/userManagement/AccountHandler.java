package com.example.servicenovigrad.userManagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.servicenovigrad.NovigradDBHandler;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccountHandler{

    //Class Variables
    public static final String TABLE_ACCOUNTS = "userAccounts";
    public static final String COLUMN_USERNAME = "_username";
    public static final String COLUMN_FIRSTNAME = "firstName";
    public static final String COLUMN_LASTNAME = "lastName";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String TABLE_DETAILS = "employeeDetails";
    public static final String COLUMN_OPENTIME = "openTime";
    public static final String COLUMN_CLOSETIME = "closeTime";
    public static final String COLUMN_WORKDAYS = "workdays";
    public static String DEFAULT_WORKDAYS = "1111100";
    public static String DEFAULT_OPENTIME = "09:00";
    public static String DEFAULT_CLOSETIME = "17:00";


    public static void createAccounts(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " +
                TABLE_ACCOUNTS + "(" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                COLUMN_FIRSTNAME + " TEXT," +
                COLUMN_LASTNAME + " TEXT," +
                COLUMN_ROLE + " TEXT," +
                COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin001");
        values.put(COLUMN_FIRSTNAME, "admin");
        values.put(COLUMN_LASTNAME, "Saitama");
        values.put(COLUMN_PASSWORD, "onePunch");
        values.put(COLUMN_ROLE, "Admin");
        db.insert(TABLE_ACCOUNTS, null, values);
    }

    public static void createEmployeeDetails(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " +
                TABLE_DETAILS + "(" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                COLUMN_OPENTIME + " TEXT NOT NULL DEFAULT \""+DEFAULT_OPENTIME +"\","+
                COLUMN_CLOSETIME + " TEXT NOT NULL DEFAULT \""+DEFAULT_CLOSETIME +"\","+
                COLUMN_WORKDAYS + " TEXT NOT NULL DEFAULT \""+DEFAULT_WORKDAYS +"\")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }

    public static void addAccount(NovigradDBHandler ndh, UserAccount ua){
        SQLiteDatabase db = ndh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, ua.getUsername());
        values.put(COLUMN_FIRSTNAME, ua.getFirstName());
        values.put(COLUMN_LASTNAME, ua.getLastName());
        values.put(COLUMN_PASSWORD, ua.getPassword());
        values.put(COLUMN_ROLE, ua.getRole());
        db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
    }

    public static UserAccount findAccount(NovigradDBHandler ndh, String username, String password){
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_ACCOUNTS + " WHERE " +
                COLUMN_USERNAME+ " = \""+ username + "\"" + " AND " +
                COLUMN_PASSWORD+ " = \""+ password + "\"";
        Cursor cursor = db.rawQuery(query, null);
        UserAccount ua;

        if (cursor.moveToFirst()){
            switch(cursor.getString(3)){
                case "Admin":
                    ua = new Admin(new User(cursor.getString(1),
                            cursor.getString(2),cursor.getString(0),
                            cursor.getString(4)));
                    break;
                case "Employee":
                    ua = new Employee(new User(cursor.getString(1),
                            cursor.getString(2),cursor.getString(0),
                            cursor.getString(4)));
                    break;
                case "Customer":
                    ua = new Customer(new User(cursor.getString(1),
                            cursor.getString(2),cursor.getString(0),
                            cursor.getString(4)));
                    break;
                default:
                    ua = null;
            }

        }
        else{
            ua = null;
        }
        cursor.close();
        db.close();
        return ua;
    }

    public static boolean usernameExists(NovigradDBHandler ndh, String username){
        boolean result = false;
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_ACCOUNTS + " WHERE " +
                COLUMN_USERNAME+ " = \""+ username + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){result=true;}
        return result;
    }

    public static ArrayList<Employee> getEmployeeList(NovigradDBHandler ndh){
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_ACCOUNTS + " WHERE " +
                COLUMN_ROLE+ " = \"Employee\"";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Employee> ale = new ArrayList<Employee>();
        Employee emp;

        while (cursor.moveToNext()){
            emp = new Employee(new User(cursor.getString(1),
                            cursor.getString(2),cursor.getString(0),
                            cursor.getString(4)));
            ale.add(emp);
        }
        cursor.close();
        db.close();
        return ale;
    }

    public static boolean deleteAccount(NovigradDBHandler ndh, String username, String password){
        boolean result = false;
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_ACCOUNTS + " WHERE " +
                COLUMN_USERNAME+ " = \""+ username + "\"" + " AND " +
                COLUMN_PASSWORD+ " = \""+ password + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String usernameStr = cursor.getString(0);
            db.delete(TABLE_ACCOUNTS, COLUMN_USERNAME +
                    " = \"" + usernameStr+ "\"", null);
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean fillEmployeeWithData(NovigradDBHandler ndh, Employee emp){
        boolean result=false;
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_DETAILS + " WHERE " +
                COLUMN_USERNAME+ " = \""+ emp.getUsername() + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            emp.setStartTime(LocalTime.from(DateTimeFormatter.ISO_TIME.parse(cursor.getString(1))));
            emp.setEndTime(LocalTime.from(DateTimeFormatter.ISO_TIME.parse(cursor.getString(2))));
            emp.setWorkdays(Workday.parseWorkdays(cursor.getString(3)));
            result=true;
        }
        else{
            emp.setStartTime(LocalTime.from(DateTimeFormatter.ISO_TIME.parse(DEFAULT_OPENTIME)));
            emp.setEndTime(LocalTime.from(DateTimeFormatter.ISO_TIME.parse(DEFAULT_CLOSETIME)));
            emp.setWorkdays(Workday.parseWorkdays(DEFAULT_WORKDAYS));
        }
        cursor.close();
        db.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateDataFromEmployee(NovigradDBHandler ndh, Employee emp){
        SQLiteDatabase db = ndh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OPENTIME,DateTimeFormatter.ISO_TIME.format(emp.getStartTime()));
        values.put(COLUMN_CLOSETIME,DateTimeFormatter.ISO_TIME.format(emp.getEndTime()));
        values.put(COLUMN_WORKDAYS,Workday.toCompressedString(emp.getWorkdays()));
        String query = "Select * FROM " + TABLE_DETAILS + " WHERE " +
                COLUMN_USERNAME+ " = \""+ emp.getUsername() + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            db.update(TABLE_DETAILS, values,
                    COLUMN_USERNAME + " = \"" + emp.getUsername() + "\"", null);
        }
        else{
            values.put(COLUMN_USERNAME, emp.getUsername());
            db.insert(TABLE_DETAILS,null,values);
        }
    }
}
