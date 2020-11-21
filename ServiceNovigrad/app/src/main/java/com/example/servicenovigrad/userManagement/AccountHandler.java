package com.example.servicenovigrad.userManagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
public class AccountHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    //Class Variables
    private static final String DATABASE_NAME = "accountsDB.db";
    public static final String TABLE_ACCOUNTS = "userAccounts";
    public static final String COLUMN_USERNAME = "_username";
    public static final String COLUMN_FIRSTNAME = "firstName";
    public static final String COLUMN_LASTNAME = "lastName";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";

    private ArrayList<UserAccount> content;

    public AccountHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        onCreate(db);
    }

    public void addAccount(UserAccount ua){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, ua.getUsername());
        values.put(COLUMN_FIRSTNAME, ua.getFirstName());
        values.put(COLUMN_LASTNAME, ua.getLastName());
        values.put(COLUMN_PASSWORD, ua.getPassword());
        values.put(COLUMN_ROLE, ua.getRole());
        db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
    }

    public UserAccount findAccount(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
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
            cursor.close();
        }
        else{
            ua = null;
        }
        db.close();
        return ua;
    }

    public boolean usernameExists(String username){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_ACCOUNTS + " WHERE " +
                COLUMN_USERNAME+ " = \""+ username + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){result=true;}
        return result;
    }

    public boolean deleteAccount(String username, String password){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_ACCOUNTS + " WHERE " +
                COLUMN_USERNAME+ " = \""+ username + "\"" + " AND " +
                COLUMN_PASSWORD+ " = \""+ password + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String usernameStr = cursor.getString(0);
            db.delete(TABLE_ACCOUNTS, COLUMN_USERNAME + " = " + usernameStr, null);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
