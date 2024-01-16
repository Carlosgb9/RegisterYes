package com.example.register;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private UsersSQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new UsersSQLiteHelper(this);

    }

    private void insertOnClick (View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UsersDatabaseContract.UsersTable.COLUMN_NAME, "John Doe");
        long newRowId = db.insert(UsersDatabaseContract.UsersTable.TABLE, null, values);
    }
}