package com.example.servicenovigrad.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.servicenovigrad.NovigradDBHandler;

public class FieldHandler{
    //Class Variables
    public static final String TABLE_FIELDS = "fields";
    public static final String COLUMN_ID = "_fieldId";
    public static final String COLUMN_NAME = "fieldName";
    public static final String COLUMN_TYPE = "type";

    public static void createFields(SQLiteDatabase db) {
        String CREATE_FIELDS_TABLE = "CREATE TABLE " +
                TABLE_FIELDS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_FIELDS_TABLE);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, "First Name");
        values.put(COLUMN_TYPE, "TEXT");
        db.insert(TABLE_FIELDS, null, values);

        values = new ContentValues();
        values.put(COLUMN_NAME, "Last Name");
        values.put(COLUMN_TYPE, "TEXT");
        db.insert(TABLE_FIELDS, null, values);

        values = new ContentValues();
        values.put(COLUMN_NAME, "Date Of Birth");
        values.put(COLUMN_TYPE, "DATE");
        db.insert(TABLE_FIELDS, null, values);

        values = new ContentValues();
        values.put(COLUMN_NAME, "Address");
        values.put(COLUMN_TYPE, "ADDRESS");
        db.insert(TABLE_FIELDS, null, values);

        values = new ContentValues();
        values.put(COLUMN_NAME, "License Type");
        values.put(COLUMN_TYPE, "TEXT");
        db.insert(TABLE_FIELDS, null, values);
    }

    public static void addField(NovigradDBHandler ndh, FieldTemplate ft){
        SQLiteDatabase db = ndh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ft.getName());
        values.put(COLUMN_TYPE, ft.getType());
        db.insert(TABLE_FIELDS, null, values);
        db.close();
    }

    public static FieldTemplate findField(NovigradDBHandler ndh, String name){
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_FIELDS + " WHERE " +
                COLUMN_NAME+ " = \""+ name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        FieldTemplate ft;

        if (cursor.moveToFirst()) {
            ft = new FieldTemplate(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2));
        }
        else{
            ft = null;
        }
        db.close();
        return ft;
    }

    public static boolean deleteField(NovigradDBHandler ndh, String name){
        boolean result = false;
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_FIELDS + " WHERE " +
                COLUMN_NAME+ " = \""+ name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String nameStr = cursor.getString(0);
            db.delete(TABLE_FIELDS, COLUMN_NAME + " = " + nameStr, null);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}