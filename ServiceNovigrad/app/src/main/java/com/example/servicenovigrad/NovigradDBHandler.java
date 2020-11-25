package com.example.servicenovigrad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.servicenovigrad.services.DocumentHandler;
import com.example.servicenovigrad.services.DocumentTemplate;
import com.example.servicenovigrad.services.FieldHandler;
import com.example.servicenovigrad.services.FieldTemplate;
import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.services.ServiceHandler;
import com.example.servicenovigrad.services.ServiceRequest;
import com.example.servicenovigrad.userManagement.AccountHandler;
import com.example.servicenovigrad.userManagement.Employee;
import com.example.servicenovigrad.userManagement.UserAccount;

import java.util.ArrayList;

public class NovigradDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;

    //Class Variables
    private static final String DATABASE_NAME = "novigradDB.db";

    public NovigradDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AccountHandler.createAccounts(db);
        AccountHandler.createEmployeeDetails(db);
        DocumentHandler.createDocuments(db);
        FieldHandler.createFields(db);
        ServiceHandler.createServices(db);
        ServiceHandler.createRequirements(db);
        ServiceHandler.createInformation(db);
        ServiceHandler.createOfferings(db);
        ServiceHandler.createServiceRequests(db);
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
        db.execSQL("DROP TABLE IF EXISTS " + AccountHandler.TABLE_DETAILS);
        AccountHandler.createEmployeeDetails(db);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceHandler.TABLE_SERVICE_REQUESTS);
        ServiceHandler.createServiceRequests(db);
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

    public boolean deleteAllOfferings(String username){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + ServiceHandler.TABLE_OFFERINGS + " WHERE " +
                AccountHandler.COLUMN_USERNAME+ " = \""+ username + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            db.delete(ServiceHandler.TABLE_OFFERINGS, AccountHandler.COLUMN_USERNAME +
                    " = \""+ username + "\"", null);
            result = true;
        }

        cursor.close();
        db.close();
        return result;
    }

    public boolean deleteEmployee(String username, String password){
        deleteAllOfferings(username);
        return deleteAccount(username,password);
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

    public void addInformation(int serviceId, int fieldId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ServiceHandler.SERVICE_COLUMN_ID, serviceId);
        values.put(FieldHandler.COLUMN_ID, fieldId);
        db.insert(ServiceHandler.TABLE_INFORMATION, null, values);
        db.close();
    }

    public ArrayList<FieldTemplate> findAllInformation(String serviceName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<FieldTemplate> alf = new ArrayList<FieldTemplate>();
        String query = "Select " + FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_ID + ", " +
                FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_NAME + ", " +
                FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_TYPE +
                " FROM ((" + ServiceHandler.TABLE_INFORMATION +

                " LEFT JOIN " + ServiceHandler.TABLE_SERVICES +
                " ON " + ServiceHandler.TABLE_INFORMATION + "." + ServiceHandler.SERVICE_COLUMN_ID + " = " +
                ServiceHandler.TABLE_SERVICES + "." + ServiceHandler.SERVICE_COLUMN_ID+ ")" +

                " LEFT JOIN " + FieldHandler.TABLE_FIELDS +
                " ON " + ServiceHandler.TABLE_INFORMATION + "." + FieldHandler.COLUMN_ID + " = " +
                FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_ID + ")" +

                " WHERE " +  ServiceHandler.TABLE_SERVICES + "." + ServiceHandler.SERVICE_COLUMN_NAME+
                " = \""+ serviceName+ "\"";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            alf.add(new FieldTemplate(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2)));
        }

        cursor.close();
        db.close();
        return alf;
    }

    public boolean deleteInformation(int serviceID, int fieldID){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + ServiceHandler.TABLE_INFORMATION + " WHERE " +
                ServiceHandler.SERVICE_COLUMN_ID+ " = "+ String.valueOf(serviceID) + " AND " +
                FieldHandler.COLUMN_ID+ " = "+ String.valueOf(fieldID);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            db.delete(ServiceHandler.TABLE_INFORMATION, ServiceHandler.SERVICE_COLUMN_ID+ " = "+
                    String.valueOf(serviceID) + " AND " +
                    FieldHandler.COLUMN_ID+ " = "+ String.valueOf(fieldID), null);
            result = true;
        }

        cursor.close();
        db.close();
        return result;
    }

    public void addRequirements(int serviceId, int docId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ServiceHandler.SERVICE_COLUMN_ID, serviceId);
        values.put(DocumentHandler.COLUMN_ID, docId);
        db.insert(ServiceHandler.TABLE_REQUIREMENTS, null, values);
        db.close();
    }

    public ArrayList<DocumentTemplate> findAllRequirements(String serviceName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DocumentTemplate> ald = new ArrayList<DocumentTemplate>();
        String query = "Select " + DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_ID + ", " +
                DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_NAME + ", " +
                DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_DESCRIPTION +
                " FROM ((" + ServiceHandler.TABLE_REQUIREMENTS +

                " LEFT JOIN " + ServiceHandler.TABLE_SERVICES +
                " ON " + ServiceHandler.TABLE_REQUIREMENTS + "." + ServiceHandler.SERVICE_COLUMN_ID + " = " +
                ServiceHandler.TABLE_SERVICES + "." + ServiceHandler.SERVICE_COLUMN_ID+ ")" +

                " LEFT JOIN " + DocumentHandler.TABLE_DOCUMENTS +
                " ON " + ServiceHandler.TABLE_REQUIREMENTS + "." + DocumentHandler.COLUMN_ID + " = " +
                DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_ID + ")" +

                " WHERE " +  ServiceHandler.TABLE_SERVICES + "." + ServiceHandler.SERVICE_COLUMN_NAME+
                " = \""+ serviceName+ "\"";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            ald.add(new DocumentTemplate(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2)));
        }

        cursor.close();
        db.close();
        return ald;
    }

    public boolean deleteRequirements(int serviceID, int docID){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + ServiceHandler.TABLE_REQUIREMENTS + " WHERE " +
                ServiceHandler.SERVICE_COLUMN_ID+ " = "+ String.valueOf(serviceID) + " AND " +
                DocumentHandler.COLUMN_ID+ " = "+ String.valueOf(docID);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            db.delete(ServiceHandler.TABLE_REQUIREMENTS, ServiceHandler.SERVICE_COLUMN_ID+ " = "+
                    String.valueOf(serviceID) + " AND " +
                    DocumentHandler.COLUMN_ID+ " = "+ String.valueOf(docID), null);
            result = true;
        }

        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<DocumentTemplate> getAllDocuments(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + DocumentHandler.TABLE_DOCUMENTS;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<DocumentTemplate> ald = new ArrayList<DocumentTemplate>();
        DocumentTemplate dt;

        while (cursor.moveToNext()){
            dt=new DocumentTemplate(cursor.getInt(0),
                    cursor.getString(1),cursor.getString(2));
            ald.add(dt);
        }
        cursor.close();
        db.close();
        return ald;
    }

    public ArrayList<FieldTemplate> getAllFields(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + FieldHandler.TABLE_FIELDS;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<FieldTemplate> alf = new ArrayList<FieldTemplate>();
        FieldTemplate ft;

        while (cursor.moveToNext()){
            ft=new FieldTemplate(cursor.getInt(0),
                    cursor.getString(1),cursor.getString(2));
            alf.add(ft);
        }
        cursor.close();
        db.close();
        return alf;
    }

    public void updateOffering(String username, int serviceId, boolean isProvided){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int intIsProvided = isProvided ? 1 : 0;
        values.put(ServiceHandler.OFFERING_COLUMN_IS_PROV, intIsProvided);
        db.update(ServiceHandler.TABLE_OFFERINGS,values,AccountHandler.COLUMN_USERNAME+ " = \""+
                username + "\"" + " AND " + ServiceHandler.SERVICE_COLUMN_ID+ " = "+
                String.valueOf(serviceId), null);

        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean fillEmployeeWithData(Employee employee){
        return AccountHandler.fillEmployeeWithData(this,employee);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDataFromEmployee(Employee employee){
        AccountHandler.updateDataFromEmployee(this,employee);
    }


    public ServiceRequest findServiceRequest(int requestID){
        return ServiceHandler.findServiceRequest(this, requestID);
    }

    public ArrayList<ServiceRequest> findAllServiceRequests(String employeeName){
        return ServiceHandler.findAllServiceRequests(this, employeeName);
    }

    public void updateRequestApproval(int requestID, int value){
        ServiceHandler.updateRequestApproval(this, requestID, value);
    }
}
