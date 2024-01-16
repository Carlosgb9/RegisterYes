package com.example.register;


import android.provider.BaseColumns;

public class UsersDatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UsersDatabaseContract() {
    }

    /* Inner class that defines the table contents */
    public static class UsersTable implements BaseColumns {
        public static final String TABLE = "student";
        public static final String COLUMN_ID = "id_card";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_CYCLE = "cycle";
        public static final String COLUMN_COURSE = "course";
    }
}
