package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    static final int DB_VERSION = 1;
    static final String DB_NAME = "UWS.DB";
    private static final String[] CREATE_TABLE_ARRAY = new String[2];

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        getCreateTableQueries();
        for (String sql : CREATE_TABLE_ARRAY) {
            db.execSQL(sql);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        for (String tblName : CREATE_TABLE_ARRAY) {
            db.execSQL("DROP TABLE IF EXISTS " + tblName);
        }
        onCreate(db);

    }

    private void getCreateTableQueries() {
        CREATE_TABLE_ARRAY[0] = "CREATE TABLE Cart(CartId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,ProductID VARCHAR(128)  NULL DEFAULT NULL ,VariantID VARCHAR(128) NULL DEFAULT NULL ,Size INT  NULL DEFAULT NULL ,Color VARCHAR(128) NULL DEFAULT NULL  ,ImageSrc VARCHAR(128)  NULL DEFAULT NULL ,AvailbleforSale BOOLEAN NOT NULL ,Quantity INT  NULL DEFAULT NULL ,Title VARCHAR(128)  NULL DEFAULT NULL ,Discount VARCHAR(256)  NULL DEFAULT NULL ,Price VARCHAR(128)  NULL DEFAULT NULL,totalPrice FLOAT(18,2),ComparaterPrice FLOAT(18,2) NULL DEFAULT NULL,Descripation VARCHAR(128)  NULL DEFAULT NULL);";
        CREATE_TABLE_ARRAY[1] = "CREATE TABLE Favourite(FavId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,ProductID VARCHAR(128)  NULL DEFAULT NULL ,VariantID VARCHAR(128) NULL DEFAULT NULL ,Size INT  NULL DEFAULT NULL ,Color VARCHAR(128) NULL DEFAULT NULL  ,ImageSrc VARCHAR(128)  NULL DEFAULT NULL ,AvailbleforSale BOOLEAN NOT NULL ,Quantity INT  NULL DEFAULT NULL ,Title VARCHAR(128)  NULL DEFAULT NULL ,Discount VARCHAR(256)  NULL DEFAULT NULL ,Price VARCHAR(128)  NULL DEFAULT NULL,totalPrice FLOAT(18,2),ComparaterPrice FLOAT(18,2) NULL DEFAULT NULL,Descripation VARCHAR(128)  NULL DEFAULT NULL);";
    }
}
