package com.example.mos3if;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements ContactsAdapter.OnItemClickListener {

    private ImageButton btn_addContact;
    private ImageButton btn_goBack;
    private RecyclerView contactList;
    private TextView tapTxt,createAccount;
    private ImageView image;
    private View lineSeparator;
    private ConstraintLayout noContactsLayout;
    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contacts;
    private static final int REQUEST_CODE_EDIT_CONTACT = 1;
    private static final int REQUEST_CODE_ADD_CONTACT = 2;
    private ContactDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        tapTxt = findViewById(R.id.tv_tap);
        image = findViewById(R.id.imageView2);
        lineSeparator = findViewById(R.id.line_seperator);
        noContactsLayout = findViewById(R.id.alternative_layout);
        createAccount = findViewById(R.id.tv_create_contact);

        dbHelper = new ContactDatabaseHelper(this);
        contacts = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(this, contacts, this);
        loadContacts();
        contactList = findViewById(R.id.rv_contacts);
        contactList.setAdapter(contactsAdapter);
        contactList.setLayoutManager(new LinearLayoutManager(this));


        checkContactsAvailability();

        btn_addContact = findViewById(R.id.btn_addContact);
        btn_goBack = findViewById(R.id.btn_back);

        btn_addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),AddContactActivity.class),REQUEST_CODE_ADD_CONTACT);
            }
        });

        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),AddContactActivity.class),REQUEST_CODE_ADD_CONTACT);
            }
        });
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onItemClick(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + contact.getPhone()));
        startActivity(intent);
    }
    @Override
    public void onEditClick(Contact contact) {
        Intent intent = new Intent(ContactsActivity.this, EditContactActivity.class);
        intent.putExtra("contact_id", contact.getId());
        startActivityForResult(intent, REQUEST_CODE_EDIT_CONTACT);
    }

    @Override
    public void onDeleteClick(Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
        builder.setMessage("Are you sure you want to delete " + contact.getName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.deleteContact(contact);
                        loadContacts();
                        checkContactsAvailability();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });
        builder.create().show();
    }
  /* @Override
   public void onDeleteClick(Contact contact) {

       Dialog dialog;
       dialog = new Dialog(getApplicationContext(), R.style.DialogTheme);

       dialog.setContentView(R.layout.dialog_layout);
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       dialog.setCancelable(false);
       dialog.show();

   }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_CONTACT && resultCode == RESULT_OK) {
            // Reload the contacts list when EditContactActivity returns with RESULT_OK
            loadContacts();
        }else if (requestCode == REQUEST_CODE_ADD_CONTACT && resultCode == RESULT_OK) {
            // Reload the contacts list when AddContactActivity returns with RESULT_OK
            loadContacts();
            checkContactsAvailability();
        }

    }


    private void loadContacts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"_id", "name", "phone"};
        Cursor cursor = db.query("contacts", projection, null, null, null, null, null);

        contacts.clear(); // clear the existing contacts before loading new ones

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

            Contact contact = new Contact(id, name, phone);
            contacts.add(contact);
        }

        cursor.close();
        contactsAdapter.notifyDataSetChanged();
    }

    private void checkContactsAvailability() {
        if (contacts.isEmpty()){
            contactList.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            tapTxt.setVisibility(View.GONE);
            lineSeparator.setVisibility(View.GONE);
            noContactsLayout.setVisibility(View.VISIBLE);
        }else {
            contactList.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            tapTxt.setVisibility(View.VISIBLE);
            lineSeparator.setVisibility(View.VISIBLE);
            noContactsLayout.setVisibility(View.GONE);
        }
    }
}