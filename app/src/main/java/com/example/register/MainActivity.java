package com.example.register;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private UsersSQLiteHelper dbHelper;
    private EditText etIdCard, etName, etSurname;
    private Spinner spCycle;
    private RadioButton rbFirst, rbSecond;
    private RadioGroup rgCourse;
    private Button bAdd, bRemove, bUpdate, bFindId, bFindCycle, bFindCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        setSpinner();
        dbHelper = new UsersSQLiteHelper(this);
        setOnClickListeners();

    }

    public void setOnClickListeners (){
        bAdd.setOnClickListener(v -> insertStudent());
        bRemove.setOnClickListener(v -> deleteStudent());
        bUpdate.setOnClickListener(v -> updateStudent());
    }

    public void setSpinner (){
        String[] cycles = {"ASIX", "DAM", "DAW"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, cycles);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spCycle.setAdapter(adapter);
        spCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void insertStudent (){
        String idCard = etIdCard.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String cycle = spCycle.getSelectedItem().toString();
        String course = (rgCourse.getCheckedRadioButtonId() == R.id.rbFirst) ? getString(R.string.first) : getString(R.string.second);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (!isFullEmpty(idCard, name, surname)){
            values.put(UsersDatabaseContract.UsersTable.COLUMN_ID, idCard);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_NAME, name);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_SURNAME, surname);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_CYCLE, cycle);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_COURSE, course);
            long newRowId = db.insert(UsersDatabaseContract.UsersTable.TABLE, null, values);

            if (newRowId != -1){
                showToast(getString(R.string.studentAdded));
                resetData();
            } else {
                showToast(getString(R.string.repeatedStudent));
            }
        }
    }

    public boolean isFullEmpty (String idCard, String name, String surname){
        if (idCard.isEmpty()){
            showToast(getString(R.string.missingIdCard));
            return true;
        } else if (name.isEmpty()){
            showToast(getString(R.string.missingName));
            return true;
        } else if (surname.isEmpty()){
            showToast(getString(R.string.missingSurname));
            return true;
        }
        return false;
    }

    public void deleteStudent (){
        String idCard = etIdCard.getText().toString().trim();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (idCard.isEmpty()){
            showToast(getString(R.string.missingIdCard));
        } else {
            eliminarContacte(idCard, db);
        }
    }

    public void eliminarContacte (String idCard, SQLiteDatabase db){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Estas segur que vols borrar les dades de l'estudiant ? ");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalDelete(idCard, db);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void finalDelete(String idCard, SQLiteDatabase db){
        String selection = UsersDatabaseContract.UsersTable.COLUMN_ID + " = ?";
        String[] selectionArgs = {idCard};
        long newRowId = db.delete(UsersDatabaseContract.UsersTable.TABLE, selection, selectionArgs);

        if (newRowId > 0){
            showToast(getString(R.string.studentDeleted));
            resetData();
        } else {
            showToast(getString(R.string.notFoundDelete));
        }
    }

    public void updateStudent(){
        String idCard = etIdCard.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String cycle = spCycle.getSelectedItem().toString();
        String course = (rgCourse.getCheckedRadioButtonId() == R.id.rbFirst) ? getString(R.string.first) : getString(R.string.second);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (!isFullEmpty(idCard, name, surname)){
            values.put(UsersDatabaseContract.UsersTable.COLUMN_ID, idCard);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_NAME, name);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_SURNAME, surname);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_CYCLE, cycle);
            values.put(UsersDatabaseContract.UsersTable.COLUMN_COURSE, course);

            String selection = UsersDatabaseContract.UsersTable.COLUMN_ID + " = ?";
            String[] selectionArgs = {idCard};

            long newRowId = db.update(UsersDatabaseContract.UsersTable.TABLE, values, selection, selectionArgs);

            if (newRowId > 0){
                showToast(getString(R.string.studentUpdate));
                resetData();
            } else {
                showToast(getString(R.string.notFoundUpdate));
            }
        }
    }

    public void resetData(){
        etIdCard.setText("");
        etSurname.setText("");
        etName.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void initialize (){
        etIdCard = findViewById(R.id.etIdCard);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        spCycle = findViewById(R.id.spCycle);
        rgCourse = findViewById(R.id.rgCourse);
        rbFirst = findViewById(R.id.rbFirst);
        rbSecond = findViewById(R.id.rbSecond);
        bAdd = findViewById(R.id.bAdd);
        bRemove = findViewById(R.id.bRemove);
        bUpdate = findViewById(R.id.bUpdate);
        bFindId = findViewById(R.id.bFindId);
        bFindCycle = findViewById(R.id.bFindCycle);
        bFindCourse = findViewById(R.id.bFindCourse);
    }
}