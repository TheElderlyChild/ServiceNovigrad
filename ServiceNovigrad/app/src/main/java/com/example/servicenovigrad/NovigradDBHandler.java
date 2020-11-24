package com.example.servicenovigrad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.servicenovigrad.services.DocumentHandler;
import com.example.servicenovigrad.services.DocumentTemplate;
import com.example.servicenovigrad.services.FieldHandler;
import com.example.servicenovigrad.services.FieldTemplate;
import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.services.ServiceHandler;
import com.example.servicenovigrad.userManagement.AccountHandler;
import com.example.servicenovigrad.userManagement.Employee;
import com.example.servicenovigrad.userManagement.UserAccount;

import java.util.ArrayList;

public class NovigradDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;

    //Class Variables
    private static final String DATABASE_NAME = "novigradDB.db";

    public NovigradDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AccountHandler.createAccounts(db);
        DocumentHandler.createDocuments(db);
        FieldHandler.createFields(db);
        ServiceHandler.createServices(db);
        ServiceHandler.createRequirements(db);
        ServiceHandler.createInformation(db);
        ServiceHandler.createOfferings(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AccountHandler.TABLE_ACCOUNTS);
        AccountHandler.createAccounts(db);
        db.execSQL("DROP TABLE IF EXISTS " + DocumentHandler.TABLE_DOCUMENTS);
        DocumentHandler.createDocuments(db);
        db.execSQL("DROP TABLE IF EXISTS " + FieldHandler.TABLE_FIELDS);
        FieldHandler.createFields(db);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceHandler.TABLE_SERVICES);
        ServiceHandler.createServices(db);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceHandler.TABLE_REQUIREMENTS);
        ServiceHandler.createRequirements(db);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceHandler.TABLE_INFORMATION);
        ServiceHandler.createInformation(db);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceHandler.TABLE_OFFERINGS);
        ServiceHandler.createOfferings(db);
    }

    public void addAccount(UserAccount ua){
        AccountHandler.addAccount(this, ua);
    }

    public UserAccount findAccount(String username, String password){
        return AccountHandler.findAccount(this, username, password);
    }

    public boolean usernameExists(String username){
        return AccountHandler.usernameExists(this, username);
    }

    public ArrayList<Employee> getEmployeeList(){
        return AccountHandler.getEmployeeList(this);
    }

    public boolean deleteAccount(String username, String password){
        return AccountHandler.deleteAccount(this, username, password);
    }
    public void addDocument(DocumentTemplate dt){
        DocumentHandler.addDocument(this, dt);
    }

    public DocumentTemplate findDocument(String name){
        return DocumentHandler.findDocument(this, name);
    }

    public boolean deleteDocument(String name){
        return DocumentHandler.deleteDocument(this, name);
    }

    public void addField(FieldTemplate ft){
        FieldHandler.addField(this, ft);
    }

    public FieldTemplate findField(String name){
        return FieldHandler.findField(this, name);
    }

    public boolean deleteField(String name){
        return FieldHandler.deleteField(this, name);
    }

    public void addService(Service service){
        ServiceHandler.addService(this, service);
    }

    public Service findService(String name){
        return ServiceHandler.findService(this, name);
    }

    public boolean deleteService(String name){
        return ServiceHandler.deleteService(this, name);
    }

    public void addOffering(String empUsername, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountHandler.COLUMN_USERNAME, empUsername);
        values.put(ServiceHandler.SERVICE_COLUMN_ID, id);
        db.insert(ServiceHandler.TABLE_OFFERINGS, null, values);
        db.close();
    }

    public void addOffering(String username, String serviceName){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * FROM " + AccountHandler.TABLE_ACCOUNTS + " WHERE " +
                AccountHandler.COLUMN_USERNAME+ " = \""+ username + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            if (!(cursor.getString(3).equals("Employee"))){
                return;
            }
        }
        else{return;}

        query = "Select * FROM " + ServiceHandler.TABLE_SERVICES + " WHERE " +
                ServiceHandler.SERVICE_COLUMN_NAME+ " = \""+ serviceName + "\"";
        cursor = db.rawQuery(query, null);
        int serviceID;

        if (cursor.moveToFirst()) {
            serviceID=cursor.getInt(0);
            addOffering(username, serviceID);
        }
        cursor.close();
        db.close();

    }

    public ArrayList<String[]> findOfferings(String empUsername){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String[]> als = new ArrayList<String[]>();
        String query = "Select " + ServiceHandler.TABLE_SERVICES + "." + ServiceHandler.SERVICE_COLUMN_NAME + ", " +
                ServiceHandler.TABLE_OFFERINGS + "." + ServiceHandler.OFFERING_COLUMN_IS_PROV +
                " FROM ((" + ServiceHandler.TABLE_OFFERINGS +

                " LEFT JOIN " + AccountHandler.TABLE_ACCOUNTS +
                " ON " + AccountHandler.TABLE_ACCOUNTS + "." + AccountHandler.COLUMN_USERNAME + " = " +
                ServiceHandler.TABLE_OFFERINGS + "." + AccountHandler.COLUMN_USERNAME + ")" +

                " LEFT JOIN " + ServiceHandler.TABLE_SERVICES +
                " ON " + ServiceHandler.TABLE_SERVICES + "." + ServiceHandler.SERVICE_COLUMN_ID + " = " +
                ServiceHandler.TABLE_OFFERINGS + "." + ServiceHandler.SERVICE_COLUMN_ID + ")" +

                " WHERE " +  AccountHandler.TABLE_ACCOUNTS + "." + AccountHandler.COLUMN_USERNAME+
                " = \""+ empUsername+ "\"";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            als.add(new String[]{cursor.getString(0),
                cursor.getString(1)});
        }

        cursor.close();
        db.close();
        return als;
    }

    public boolean deleteOffering(String username, int id){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + ServiceHandler.TABLE_OFFERINGS + " WHERE " +
                AccountHandler.COLUMN_USERNAME+ " = \""+ username + "\"" + " AND " +
                ServiceHandler.SERVICE_COLUMN_ID+ " = "+ String.valueOf(id);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            db.delete(ServiceHandler.TABLE_OFFERINGS, AccountHandler.COLUMN_USERNAME +
                    " = \""+ username + "\"" + " AND " + ServiceHandler.SERVICE_COLUMN_ID+ " = "
                    + String.valueOf(id), null);
            result = true;
        }

        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<Service> getAllServices(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + ServiceHandler.TABLE_SERVICES;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Service> als = new ArrayList<Service>();
        Service service;

        while (cursor.moveToNext()){
            service = findService(cursor.getString(1));
            als.add(service);
        }
        cursor.close();
        db.close();
        return als;
    }
}
