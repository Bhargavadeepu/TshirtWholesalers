package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.FovuroriteModel;

import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public void insertCartItems(CartModel data) {
        ContentValues contentValue = new ContentValues();
        //contentValue.put("CartId", data.getCartId());
        contentValue.put("ProductID", data.getProductID());
        contentValue.put("VariantID", data.getVariantID());
        contentValue.put("Size", data.getSize());
        contentValue.put("Color", data.getColor());
        contentValue.put("ImageSrc", data.getImageSrc());
        contentValue.put("AvailbleforSale", data.getAvailbleforSale());
        contentValue.put("Quantity", data.getQuantity());
        contentValue.put("Title", data.getTitle());
        contentValue.put("Discount", data.getDiscount());
        contentValue.put("Price", data.getPrice());
        contentValue.put("totalPrice", data.getTotalPrice());
        contentValue.put("ComparaterPrice", data.getComparaterPrice());
        contentValue.put("Descripation", data.getDescripation());
        database.insert("Cart", null, contentValue);
    }


    public int updateCartItmes(CartModel data) {

        ContentValues contentValue = new ContentValues();
        contentValue.put("CartId", data.getCartId());
        contentValue.put("Quantity", data.getQuantity());
        contentValue.put("totalPrice", data.getTotalPrice());
        contentValue.put("Discount", data.getDiscount());
        //contentValue.put("ComparaterPrice", data.getComparaterPrice());
       /* contentValue.put("Price", data.getPrice());
        contentValue.put("ProductID", data.getProductID());
        contentValue.put("VariantID", data.getVariantID());
        contentValue.put("Size", data.getSize());
        contentValue.put("Color", data.getColor());
        contentValue.put("ImageSrc", data.getImageSrc());
        contentValue.put("AvailbleforSale", data.getAvailbleforSale());
        contentValue.put("Title", data.getTitle());
        contentValue.put("Discount", data.getDiscount());*/
        int i = database.update("Cart", contentValue, "CartId = " + data.getCartId(), null);
        return i;

    }


    public int updateCartItmesData(CartModel data) {

        ContentValues contentValue = new ContentValues();
        contentValue.put("CartId", data.getCartId());
        contentValue.put("Quantity", data.getQuantity());
        contentValue.put("totalPrice", data.getTotalPrice());
        contentValue.put("Discount", data.getDiscount());
        contentValue.put("Price", data.getPrice());
        contentValue.put("ProductID", data.getProductID());
        contentValue.put("VariantID", data.getVariantID());
        contentValue.put("Size", data.getSize());
        contentValue.put("Color", data.getColor());
        contentValue.put("ImageSrc", data.getImageSrc());
        contentValue.put("AvailbleforSale", data.getAvailbleforSale());
        contentValue.put("Title", data.getTitle());
        contentValue.put("Descripation", data.getDescripation());
        int i = database.update("Cart", contentValue, "CartId = " + data.getCartId(), null);
        return i;

    }


    private Cursor fetchCarts() {

        String strQry = "";

        strQry = "select * from Cart";

        Cursor cursor = database.rawQuery(strQry, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }


    public ArrayList<CartModel> GetCartDetails() {
        Cursor c = fetchCarts();

        if (c.getCount() != 0) {

            CartModel cartModel = new CartModel();
            ArrayList<CartModel> cartArrylist = new ArrayList<CartModel>();
            try {

                if (c.moveToFirst()) {


                    do {
                        cartModel = new CartModel();
                        cartModel.setCartId(c.getInt(c.getColumnIndex("CartId")));
                        cartModel.setProductID(c.getString(c.getColumnIndex("ProductID")));
                        cartModel.setVariantID(c.getString(c.getColumnIndex("VariantID")));
                        cartModel.setSize(c.getString(c.getColumnIndex("Size")));
                        cartModel.setColor(c.getString(c.getColumnIndex("Color")));
                        cartModel.setImageSrc(c.getString(c.getColumnIndex("ImageSrc")));
                        cartModel.setAvailbleforSale(c.getString(c.getColumnIndex("AvailbleforSale")));
                        cartModel.setQuantity(c.getString(c.getColumnIndex("Quantity")));
                        cartModel.setTitle(c.getString(c.getColumnIndex("Title")));
                        cartModel.setDiscount(c.getString(c.getColumnIndex("Discount")));
                        cartModel.setPrice(c.getString(c.getColumnIndex("Price")));
                        cartModel.setTotalPrice(c.getFloat(c.getColumnIndex("totalPrice")));
                        cartModel.setComparaterPrice(c.getFloat(c.getColumnIndex("ComparaterPrice")));
                        cartModel.setDescripation(c.getString(c.getColumnIndex("Descripation")));
                        cartArrylist.add(cartModel);

                    } while (c.moveToNext());
                }


            } finally {
                try {
                    c.close();
                } catch (Exception ignore) {
                }
            }

            return cartArrylist;
        } else {
            return null;

        }


    }


    public void UpdateCartDetails(CartModel data) {


        if (data.getCartId() != 0) {
            updateCartItmes(data);
        } else {
            insertCartItems(data);
        }


    }

    public void deleteCart(int id) {
        database.delete("Cart", "CartId=" + id, null);
    }


    /****    Favourite Table  ******/


    public void insertFavouriteItems(FovuroriteModel data) {
        ContentValues contentValue = new ContentValues();
        //contentValue.put("CartId", data.getCartId());
        contentValue.put("ProductID", data.getProductID());
        contentValue.put("VariantID", data.getVariantID());
        contentValue.put("Size", data.getSize());
        contentValue.put("Color", data.getColor());
        contentValue.put("ImageSrc", data.getImageSrc());
        contentValue.put("AvailbleforSale", data.getAvailbleforSale());
        contentValue.put("Quantity", data.getQuantity());
        contentValue.put("Title", data.getTitle());
        contentValue.put("Discount", data.getDiscount());
        contentValue.put("Price", data.getPrice());
        contentValue.put("totalPrice", data.getTotalPrice());
        contentValue.put("ComparaterPrice", data.getComparaterPrice());
        contentValue.put("Descripation", data.getDescripation());
        database.insert("Favourite", null, contentValue);
    }




    public void InsertFavouriteDetails(FovuroriteModel data) {
        insertFavouriteItems(data);
    }

    public void deleteFavourite(int id) {
        database.delete("Favourite", "FavId=" + id, null);
    }


    public ArrayList<FovuroriteModel> GetFavouriteDetails() {
        Cursor c = fetchfavitem();

        if (c.getCount() != 0) {


            FovuroriteModel fovuroriteModel = new FovuroriteModel();
            ArrayList<FovuroriteModel> favoriteArrylist = new ArrayList<FovuroriteModel>();
            try {

                if (c.moveToFirst()) {


                    do {
                        fovuroriteModel = new FovuroriteModel();
                        fovuroriteModel.setFavId(c.getInt(c.getColumnIndex("FavId")));
                        fovuroriteModel.setProductID(c.getString(c.getColumnIndex("ProductID")));
                        fovuroriteModel.setVariantID(c.getString(c.getColumnIndex("VariantID")));
                        fovuroriteModel.setSize(c.getString(c.getColumnIndex("Size")));
                        fovuroriteModel.setColor(c.getString(c.getColumnIndex("Color")));
                        fovuroriteModel.setImageSrc(c.getString(c.getColumnIndex("ImageSrc")));
                        fovuroriteModel.setAvailbleforSale(c.getString(c.getColumnIndex("AvailbleforSale")));
                        fovuroriteModel.setQuantity(c.getDouble(c.getColumnIndex("Quantity")));
                        fovuroriteModel.setTitle(c.getString(c.getColumnIndex("Title")));
                        fovuroriteModel.setDiscount(c.getDouble(c.getColumnIndex("Discount")));
                        fovuroriteModel.setPrice(c.getDouble(c.getColumnIndex("Price")));
                        fovuroriteModel.setTotalPrice(c.getFloat(c.getColumnIndex("totalPrice")));
                        fovuroriteModel.setDescripation(c.getString(c.getColumnIndex("Descripation")));
                        fovuroriteModel.setComparaterPrice(c.getFloat(c.getColumnIndex("ComparaterPrice")));
                        favoriteArrylist.add(fovuroriteModel);

                    } while (c.moveToNext());
                }


            } finally {
                try {
                    c.close();
                } catch (Exception ignore) {
                }
            }

            return favoriteArrylist;
        } else {
            return null;

        }


    }

    private Cursor fetchfavitem() {

        String strQry = "";

        strQry = "select * from Favourite";

        Cursor cursor = database.rawQuery(strQry, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

/*
    public void deleteCartitemBasedOnvarId(String variantId) {
        database.delete("Cart", "VariantID=" + "Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8zNDc1NjA4MjUwNQ==", null);
    }*/



    public void deleteCartitemBasedOnvarId(String variantId) {
        database.delete("Cart", "VariantID= '" + variantId + "'", null);

       // int i = database.update("Users", contentValue, "Id = '" + data.getUserId() + "'", null);
    }

}
