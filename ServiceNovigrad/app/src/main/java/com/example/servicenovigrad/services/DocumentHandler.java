package com.example.servicenovigrad.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.servicenovigrad.NovigradDBHandler;

public class DocumentHandler{
    //Class Variables
    public static final String TABLE_DOCUMENTS = "documents";
    public static final String COLUMN_ID = "_docId";
    public static final String COLUMN_NAME = "docName";
    public static final String COLUMN_DESCRIPTION = "description";

    public static void createDocuments(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " +
                TABLE_DOCUMENTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT UNIQUE," +
                COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, "Proof Of Residence");
        values.put(COLUMN_DESCRIPTION, "An image of a bank statement or hydro bill that\n" +
                "shows the address");
        db.insert(TABLE_DOCUMENTS, null, values);

        values = new ContentValues();
        values.put(COLUMN_NAME, "Proof Of Status");
        values.put(COLUMN_DESCRIPTION, "An image of a Canadian permanent resident\n" +
                "card or a Canadian passport");
        db.insert(TABLE_DOCUMENTS, null, values);

        values = new ContentValues();
        values.put(COLUMN_NAME, "Personal Photo");
        values.put(COLUMN_DESCRIPTION, "A photo of the customer");
        db.insert(TABLE_DOCUMENTS, null, values);
    }

    public static void addDocument(NovigradDBHandler ndh, DocumentTemplate dt){
        SQLiteDatabase db = ndh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, dt.getName());
        values.put(COLUMN_DESCRIPTION, dt.getDescription());
        db.insert(TABLE_DOCUMENTS, null, values);
        db.close();
    }

    public static DocumentTemplate findDocument(NovigradDBHandler ndh, String name){
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_DOCUMENTS + " WHERE " +
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

        cursor.close();
        db.close();
        return dt;
    }

    public static boolean deleteDocument(NovigradDBHandler ndh, String name){
        boolean result = false;
        SQLiteDatabase db = ndh.getWritableDatabase();
        String query = "Select * FROM " + TABLE_DOCUMENTS + " WHERE " +
                COLUMN_NAME+ " = \""+ name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String nameStr = cursor.getString(1);
            db.delete(TABLE_DOCUMENTS, COLUMN_NAME + " = " + nameStr, null);
            result = true;
        }

        cursor.close();
        db.close();
        return result;
    }
}