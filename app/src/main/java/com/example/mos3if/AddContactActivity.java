package com.example.mos3if;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddContactActivity extends AppCompatActivity {
    private ContactDatabaseHelper dbHelper;
    private Button addContact;
    private EditText name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        dbHelper = new ContactDatabaseHelper(this);

        addContact = findViewById(R.id.btn_add);
        name = findViewById(R.id.et_name);
        phone = findViewById(R.id.et_phone);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTxt = name.getText().toString().trim();
                String phoneTxt = phone.getText().toString().trim();

                if (nameTxt.isEmpty()){
                    name.setError("Full Name is required");
                    name.requestFocus();
                    return;
                }
                if (phoneTxt.isEmpty()){
                    phone.setError("Phone Number is required");
                    phone.requestFocus();
                    return;
                }
                if (!Patterns.PHONE.matcher(phoneTxt).matches()){
                    phone.setError("Please provide a valid phone number");
                    phone.requestFocus();
                    return;
                }
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", nameTxt);
                values.put("phone", phoneTxt);
                db.insert("contacts", null, values);

                setResult(RESULT_OK);
                finish();


            }
        });
    }
}