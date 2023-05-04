package com.example.mos3if;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditContactActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etPhone;
    private Button updateButton;
    private long contactId;


    private ContactDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        updateButton = findViewById(R.id.btn_update);

        dbHelper = new ContactDatabaseHelper(this);

        // Get the contact ID from the intent extra
        Intent intent = getIntent();
        contactId = intent.getLongExtra("contact_id", -1);

        if (contactId == -1) {
            // Invalid contact ID, return to previous activity
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else {
            // Retrieve the contact details from the database
            Contact contact = dbHelper.getContactById(contactId);
            if (contact == null) {
                // Contact not found, return to previous activity
                setResult(Activity.RESULT_CANCELED);
                finish();
            } else {
                // Populate the EditText fields with the contact details
                etName.setText(contact.getName());
                etPhone.setText(contact.getPhone());
            }
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Update the contact in the database
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();

                if (name.isEmpty()){
                    etName.setError("Full Name is required");
                    etName.requestFocus();
                    return;
                }
                if (phone.isEmpty()){
                    etPhone.setError("Phone Number is required");
                    etPhone.requestFocus();
                    return;
                }

                Contact updatedContact = new Contact(contactId, name, phone);
                dbHelper.updateContact(updatedContact);

                // Return to previous activity
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


    }
}