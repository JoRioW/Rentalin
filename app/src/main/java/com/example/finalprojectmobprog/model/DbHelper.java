package com.example.finalprojectmobprog.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private static DbHelper instance;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context) {
        if (instance == null) instance = new DbHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_USERS = "CREATE TABLE users (userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "password TEXT, " +
                "name TEXT, " +
                "phoneNumber TEXT, " +
                "address TEXT," +
                "driverLicenseNumber TEXT)";

        String TABLE_CAR = "CREATE TABLE cars (carId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "model TEXT, " +
                "year INTEGER, " +
                "licensePlate TEXT, " +
                "carSeats INTEGER," +
                "transmission TEXT," +
                "pricePerDay REAL," +
                "status INTEGER)";

        String TABLE_RENTAL = "CREATE TABLE rentals (rentalId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "startDate TEXT, " +
                "endDate TEXT, " +
                "totalPrice REAL, " +
                "paymentStatus INTEGER, " +
                "userId INTEGER," +
                "carId INTEGER," +
                "FOREIGN KEY (userId) REFERENCES users(userId)," +
                "FOREIGN KEY (carId) REFERENCES cars(carId))";

        String TABLE_IMAGEURL = "CREATE TABLE carimages (imageCarId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "imageurl TEXT, " +
                "carId INTEGER, " +
                "FOREIGN KEY (carId) REFERENCES cars(carId))";

        String TABLE_PAYMENT = "CREATE TABLE payments (paymentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "paymentDate TEXT, " +
                "amount REAL, " +
                "paymentMethod TEXT," +
                "rentalId INTEGER," +
                "FOREIGN KEY (rentalId) REFERENCES rentals(rentalId))";

        db.execSQL(TABLE_USERS);
        db.execSQL(TABLE_CAR);
        db.execSQL(TABLE_RENTAL);
        db.execSQL(TABLE_IMAGEURL);
        db.execSQL(TABLE_PAYMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS cars");
        db.execSQL("DROP TABLE IF EXISTS rentals");
        db.execSQL("DROP TABLE IF EXISTS carimages");
        db.execSQL("DROP TABLE IF EXISTS payments");
        onCreate(db);
    }
}
