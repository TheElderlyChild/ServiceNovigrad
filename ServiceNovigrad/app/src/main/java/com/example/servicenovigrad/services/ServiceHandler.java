package com.example.servicenovigrad.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.servicenovigrad.NovigradDBHandler;
import com.example.servicenovigrad.userManagement.AccountHandler;

import java.util.ArrayList;

public class ServiceHandler{
    //Class Variables
    public static final String TABLE_SERVICES = "services";
    public static final String SERVICE_COLUMN_ID = "_serviceId";
    public static final String SERVICE_COLUMN_NAME = "serviceName";

    public static final String TABLE_REQUIREMENTS="requirements";
    public static final String TABLE_INFORMATION="information";
    public static final String TABLE_OFFERINGS="offerings";
    public static final String OFFERING_COLUMN_IS_PROV="isProvided";




    public static void createServices(SQLiteDatabase db) {
        String CREATE_SERVICES_TABLE = "CREATE TABLE " +
                TABLE_SERVICES + "(" +
                SERVICE_COLUMN_ID + " INTEGER PRIMARY KEY," +
                SERVICE_COLUMN_NAME + " TEXT UNIQUE" + ")";
        db.execSQL(CREATE_SERVICES_TABLE);

        ContentValues values = new ContentValues();
        values.put(SERVICE_COLUMN_NAME, "Driver's License");
        db.insert(TABLE_SERVICES, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_NAME, "Health Card");
        db.insert(TABLE_SERVICES, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_NAME, "Photo ID");
        db.insert(TABLE_SERVICES, null, values);
    }

    public static void createRequirements(SQLiteDatabase db) {
        String CREATE_REQUIREMENTS_TABLE = "CREATE TABLE " +
                TABLE_REQUIREMENTS + "(" +
                SERVICE_COLUMN_ID + " INTEGER NOT NULL," +
                DocumentHandler.COLUMN_ID + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_REQUIREMENTS_TABLE);

        ContentValues values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 1);
        values.put(DocumentHandler.COLUMN_ID, 1);
        db.insert(TABLE_REQUIREMENTS, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 2);
        values.put(DocumentHandler.COLUMN_ID, 1);
        db.insert(TABLE_REQUIREMENTS, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 2);
        values.put(DocumentHandler.COLUMN_ID, 2);
        db.insert(TABLE_REQUIREMENTS, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 3);
        values.put(DocumentHandler.COLUMN_ID, 1);
        db.insert(TABLE_REQUIREMENTS, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 3);
        values.put(DocumentHandler.COLUMN_ID, 3);
        db.insert(TABLE_REQUIREMENTS, null, values);
    }

    public static void createInformation(SQLiteDatabase db) {
        String CREATE_INFORMATION_TABLE = "CREATE TABLE " +
                TABLE_INFORMATION + "(" +
                SERVICE_COLUMN_ID + " INTEGER NOT NULL," +
                FieldHandler.COLUMN_ID + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_INFORMATION_TABLE);

        ContentValues values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 1);
        values.put(FieldHandler.COLUMN_ID, 1);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 1);
        values.put(FieldHandler.COLUMN_ID, 2);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 1);
        values.put(FieldHandler.COLUMN_ID, 3);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 1);
        values.put(FieldHandler.COLUMN_ID, 4);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 1);
        values.put(FieldHandler.COLUMN_ID, 5);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 2);
        values.put(FieldHandler.COLUMN_ID, 1);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 2);
        values.put(FieldHandler.COLUMN_ID, 2);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 2);
        values.put(FieldHandler.COLUMN_ID, 3);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 2);
        values.put(FieldHandler.COLUMN_ID, 4);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 3);
        values.put(FieldHandler.COLUMN_ID, 1);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 3);
        values.put(FieldHandler.COLUMN_ID, 2);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 3);
        values.put(FieldHandler.COLUMN_ID, 3);
        db.insert(TABLE_INFORMATION, null, values);

        values = new ContentValues();
        values.put(SERVICE_COLUMN_ID, 3);
        values.put(FieldHandler.COLUMN_ID, 4);
        db.insert(TABLE_INFORMATION, null, values);
    }

    public static void createOfferings(SQLiteDatabase db) {
        String CREATE_OFFERINGS_TABLE = "CREATE TABLE " +
                TABLE_OFFERINGS + "(" +
                AccountHandler.COLUMN_USERNAME + " TEXT NOT NULL," +
                SERVICE_COLUMN_ID + " INTEGER NOT NULL," +
                OFFERING_COLUMN_IS_PROV + " BOOLEAN DEFAULT 0, " +
                "UNIQUE(" + AccountHandler.COLUMN_USERNAME + ", " +
                SERVICE_COLUMN_ID+")"+
                ")";
        db.execSQL(CREATE_OFFERINGS_TABLE);
    }






    public static void addService(NovigradDBHandler ndh, Service service){
        ndh.deleteService(service.getName());
        SQLiteDatabase db = ndh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SERVICE_COLUMN_NAME, service.getName());
        db.insert(TABLE_SERVICES, null, values);

        int serviceId;
        int documentId;
        int fieldId;
        String query = "Select * FROM " + TABLE_SERVICES + " WHERE " +
                SERVICE_COLUMN_NAME+ " = \""+ service.getName() + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            serviceId = cursor.getInt(0);
        }
        else{
            cursor.close();
            return;
        }

        for (DocumentTemplate doc : service.getRequirements()){
            query = "Select * FROM " + DocumentHandler.TABLE_DOCUMENTS + " WHERE " +
                    DocumentHandler.COLUMN_NAME+ " = \""+ doc.getName() + "\"";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToNext()) {
                documentId = cursor.getInt(0);
                values = new ContentValues();
                values.put(SERVICE_COLUMN_ID, serviceId);
                values.put(DocumentHandler.COLUMN_ID, documentId);
                db.insert(TABLE_REQUIREMENTS, null, values); }
        }

        for (FieldTemplate field : service.getInformation()){
            query = "Select * FROM " + FieldHandler.TABLE_FIELDS + " WHERE " +
                    FieldHandler.COLUMN_NAME+ " = \""+ field.getName() + "\"";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToNext()) {
                fieldId = cursor.getInt(0);
                values = new ContentValues();
                values.put(SERVICE_COLUMN_ID, serviceId);
                values.put(FieldHandler.COLUMN_ID, fieldId);
                db.insert(TABLE_INFORMATION, null, values); }
        }

        cursor.close();
        db.close();
    }

    public static Service findService(NovigradDBHandler ndh, String name){
        SQLiteDatabase db = ndh.getWritableDatabase();

        String query = "Select * FROM " + TABLE_SERVICES + " WHERE " +
                SERVICE_COLUMN_NAME+ " = \""+ name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        Service service;
        int serviceID;

        if (cursor.moveToFirst()) {
            serviceID=cursor.getInt(0);
        }
        else{
            return null;
        }

        ArrayList<DocumentTemplate> adt = new ArrayList<DocumentTemplate>();
        ArrayList<FieldTemplate> aft = new ArrayList<FieldTemplate>();

        query = "Select " + DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_ID + ", " +
                DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_NAME + ", " +
                DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_DESCRIPTION +
                " FROM ((" + TABLE_REQUIREMENTS +

                " LEFT JOIN " + TABLE_SERVICES +
                " ON " + TABLE_SERVICES + "." + SERVICE_COLUMN_ID + " = " +
                TABLE_REQUIREMENTS + "." + SERVICE_COLUMN_ID + ")" +

                " LEFT JOIN " + DocumentHandler.TABLE_DOCUMENTS +
                " ON " + DocumentHandler.TABLE_DOCUMENTS + "." + DocumentHandler.COLUMN_ID + " = " +
                TABLE_REQUIREMENTS + "." + DocumentHandler.COLUMN_ID + ")" +

                " WHERE " + SERVICE_COLUMN_NAME+ " = \""+ name + "\"";

        cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            adt.add(new DocumentTemplate(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2)));
        }

        query = "Select " + FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_ID + ", " +
                FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_NAME + ", " +
                FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_TYPE +
                " FROM ((" + TABLE_INFORMATION +

                " LEFT JOIN " + TABLE_SERVICES +
                " ON " + TABLE_SERVICES + "." + SERVICE_COLUMN_ID + " = " +
                TABLE_INFORMATION + "." + SERVICE_COLUMN_ID + ")" +

                " LEFT JOIN " + FieldHandler.TABLE_FIELDS +
                " ON " + FieldHandler.TABLE_FIELDS + "." + FieldHandler.COLUMN_ID + " = " +
                TABLE_INFORMATION + "." + FieldHandler.COLUMN_ID + ")" +

                " WHERE " + SERVICE_COLUMN_NAME+ " = \""+ name + "\"";

        cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            aft.add(new FieldTemplate(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2)));
        }


        service = new Service (serviceID, name, adt, aft);
        cursor.close();
        db.close();
        return service;
    }

    public static boolean deleteService(NovigradDBHandler ndh, String name){
        boolean result = false;
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_SERVICES + " WHERE " +
                SERVICE_COLUMN_NAME+ " = \""+ name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String serviceID=cursor.getString(0);
            String nameStr = cursor.getString(1);
            db.delete(TABLE_SERVICES, SERVICE_COLUMN_NAME + " = \""+ nameStr + "\"", null);
            db.delete(TABLE_REQUIREMENTS, SERVICE_COLUMN_ID + " = " + serviceID, null);
            db.delete(TABLE_INFORMATION, SERVICE_COLUMN_ID + " = " + serviceID, null);
            db.delete(TABLE_OFFERINGS, SERVICE_COLUMN_ID + " = "+ serviceID, null);
            result = true;
        }

        cursor.close();
        db.close();
        return result;
    }



}