package com.example.servicenovigrad.services;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.servicenovigrad.userManagement.AccountHandler;

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
                SERVICE_COLUMN_NAME + " TEXT" + ")";
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
                OFFERING_COLUMN_IS_PROV + " BOOLEAN DEFAULT 0" + ")";
        db.execSQL(CREATE_OFFERINGS_TABLE);
    }




    /*

    public void addService(DocumentTemplate dt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, dt.getName());
        db.insert(TABLE_SERVICES, null, values);
        db.close();
    }

    public DocumentTemplate findService(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_SERVICES + " WHERE " +
                COLUMN_NAME+ " = \""+ name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        DocumentTemplate dt;

        if (cursor.moveToFirst()) {
            dt = new DocumentTemplate(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2));
        }
        else{
            dt = null;
        }
        db.close();
        return dt;
    }

    public boolean deleteService(String name){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_SERVICES + " WHERE " +
                COLUMN_NAME+ " = \""+ name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String nameStr = cursor.getString(0);
            db.delete(TABLE_SERVICES, COLUMN_NAME + " = " + nameStr, null);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    */
}