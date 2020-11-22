package com.example.servicenovigrad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.servicenovigrad.services.DocumentHandler;
import com.example.servicenovigrad.services.DocumentTemplate;
import com.example.servicenovigrad.services.FieldHandler;
import com.example.servicenovigrad.services.FieldTemplate;
import com.example.servicenovigrad.services.ServiceHandler;
import com.example.servicenovigrad.userManagement.AccountHandler;
import com.example.servicenovigrad.userManagement.UserAccount;

public class NovigradDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

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
        ServiceHandler.createServices(db);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceHandler.TABLE_REQUIREMENTS);
        ServiceHandler.createServices(db);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceHandler.TABLE_REQUIREMENTS);
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


}
