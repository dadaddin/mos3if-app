package com.example.mos3if;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts.db";

    private static final String SQL_CREATE_CONTACTS_TABLE =
            "CREATE TABLE contacts (_id INTEGER PRIMARY KEY, name TEXT, phone TEXT)";

    public ContactDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not implemented in this case
    }
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts", "_id = ?", new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

    public Contact getContactById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {"_id", "name", "phone"};
        String selection = "_id=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query("contacts", projection, selection, selectionArgs, null, null, null);

        Contact contact = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            contact = new Contact(id, name, phone);
        }

        cursor.close();
        return contact;
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phone", contact.getPhone());
        db.update("contacts", values, "_id = ?", new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


}
