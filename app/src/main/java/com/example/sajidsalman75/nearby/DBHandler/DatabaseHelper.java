package com.example.sajidsalman75.nearby.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.Model.Subcategory;
import com.example.sajidsalman75.nearby.Model.Town;
import com.example.sajidsalman75.nearby.Model.Users;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Places.db";

    //Users table
    public static final String TABLE_USERS = "users_table";
    public static final String COL_ID = "ID";
    public static final String COL_FNAME = "FIRSTNAME";
    public static final String COL_LNAME = "LASTNAME";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_PASSWORD = "PASSWORD";
    public static final String COL_ROLE = "ROLE";

    //City Table
    public static final String TABLE_CITY = "city_table";
    public static final String CITY_ID = "ID";
    public static final String CITY_NAME = "NAME";

    //Town Table
    public static final String TABLE_TOWN = "town_table";
    public static final String TOWN_ID = "ID";
    public static final String TOWN_NAME = "NAME";
    public static final String TOWN_CITYID = "CITYID";

    //Places Table
    public static final String TABLE_PLACES = "places_table";
    public static final String PLACE_ID = "ID";
    public static final String PLACE_NAME = "NAME";
    public static final String PLACE_ADDRESS = "ADDRESS";
    public static final String PLACES_USERID = "USERID";
    public static final String PLACES_OPENINGTIME = "OPENINGTIME";
    public static final String PLACES_CLOSINGTIME = "CLOSINGTIME";
    public static final String PLACES_CITYID = "CITYID";
    public static final String PLACES_TOWNID = "TOWNID";
    public static final String PLACES_CATEGORYID = "CATEGORYID";
    public static final String PLACES_SUBCATEGORYID = "SUBCATEGORYID";

    //Sub Category Table
    public static final String TABLE_SUBCAT = "subcategory_table";
    public static final String SUBCAT_ID = "ID";
    public static final String SUBCAT_NAME = "NAME";
    public static final String SUBCAT_CATID = "CATID";

    //Pictures Table
    public static final String TABLE_PICTURES = "pictures_table";
    public static final String PICTURE_ID = "ID";
    public static final String PICTURES_PATH1 = "PATH1";
    public static final String PICTURES_PLACEID = "PLACEID";

    //Category Table
    public static final String TABLE_CATEGORY = "category_table";
    public static final String CAT_ID = "ID";
    public static final String CAT_NAME = "NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 12);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME TEXT," +
                "LASTNAME TEXT,EMAIL TEXT UNIQUE,PASSWORD TEXT,ROLE TEXT)");
        db.execSQL("create table " + TABLE_PLACES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT," +
                "CITYID INTEGER,TOWNID INTEGER,CATEGORYID INTEGER,SUBCATEGORYID INTEGER,ADDRESS TEXT," +
                "OPENINGTIME INTEGER,CLOSINGTIME INTEGER,USERID INTEGER)");
        db.execSQL("create table " + TABLE_CATEGORY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT)");
        db.execSQL("create table " + TABLE_SUBCAT + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,CATID INTEGER)");
        db.execSQL("create table " + TABLE_CITY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT)");
        db.execSQL("create table " + TABLE_TOWN + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,CITYID INTEGER)");
        db.execSQL("create table " + TABLE_PICTURES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PATH1 BLOB, PLACEID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOWN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURES);
        onCreate(db);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_USERS, null);
        return res;
    }

    public Cursor getAllPlaces(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_PLACES, null);
        return res;
    }

    public Integer deletePlace(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PICTURES, "PLACEID = ?", new String[]{id});
        return db.delete(TABLE_PLACES, "ID = ?", new String[]{id});
    }

    public Cursor login(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE EMAIL = '" + email +
                "' AND PASSWORD = '" + password + "';";
        Log.i("Mera", query);
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public boolean insertUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FNAME, user.getFIRSTNAME());
        contentValues.put(COL_LNAME, user.getLASTNAME());
        contentValues.put(COL_EMAIL, user.getEMAIL());
        contentValues.put(COL_PASSWORD, user.getPASSWORD());
        contentValues.put(COL_ROLE, user.getROLE());
        long result = db.insert(TABLE_USERS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public long insertPlace(Places place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_NAME, place.getNAME());
        contentValues.put(PLACES_CITYID, place.getCITYID());
        contentValues.put(PLACES_TOWNID, place.getTOWNID());
        contentValues.put(PLACES_CATEGORYID, place.getCATID());
        contentValues.put(PLACES_SUBCATEGORYID, place.getSUBCATID());
        contentValues.put(PLACE_ADDRESS, place.getADDRESS());
        contentValues.put(PLACES_OPENINGTIME, place.getOPENINGTIME());
        contentValues.put(PLACES_CLOSINGTIME, place.getCLOSINGTIME());
        contentValues.put(PLACES_USERID, place.getUSERID());
        long result = db.insert(TABLE_PLACES, null, contentValues);
        return result;
    }

    public Cursor showPlaces(String name, String city, String category){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = null;
        if(name == null && city == null && category != null){
            query = "SELECT * FROM " + TABLE_PLACES + " WHERE CATEGORY = '" + category +
                    "';";
        }
        else if (name == null && city != null && category == null){
            query = "SELECT * FROM " + TABLE_PLACES + " WHERE CITY = '" + city +
                    "';";
        }
        else if (name == null && city != null && category != null){
            query = "SELECT * FROM " + TABLE_PLACES + " WHERE CATEGORY = '" + category +
                    "' AND CITY= '" + city + "';";
        }
        else if (name != null && city == null && category == null) {
            query = "SELECT * FROM " + TABLE_PLACES + " WHERE NAME LIKE '%" + name +
                    "%';";
        }
        else if (name != null && city == null && category != null){
            query = "SELECT * FROM " + TABLE_PLACES + " WHERE CATEGORY = '" + category +
                    "' AND NAME LIKE '%" + name + "%';";
        }
        else if (name != null && city != null && category == null){
            query = "SELECT * FROM " + TABLE_PLACES + " WHERE CITY = '" + city +
                    "' AND NAME LIKE '%" + name +"%';";
        }
        else if (name != null && city != null && category != null){
            query = "SELECT * FROM " + TABLE_PLACES + " WHERE CATEGORY = '" + category +
                    "' AND CITY = '" + city + "' AND NAME LIKE '%"+ name + "%';";
        }
        Log.i("Mera", query);
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public int createAdmin(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ROLE, "admin");
        return db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{id});
    }

    public boolean updateUserInfo(String id, Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FNAME, user.getFIRSTNAME());
        contentValues.put(COL_LNAME, user.getLASTNAME());
        contentValues.put(COL_EMAIL, user.getEMAIL());
        contentValues.put(COL_PASSWORD, user.getPASSWORD());
        db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{id});
        return true;
    }
    public boolean updatePlaceInfo(Places place){
        SQLiteDatabase db = this.getWritableDatabase();
        String id = Integer.toString(place.getID());
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_NAME, place.getNAME());
        contentValues.put(PLACES_CITYID, place.getCITYID());
        contentValues.put(PLACES_CATEGORYID, place.getCATID());
        contentValues.put(PLACE_ADDRESS, place.getADDRESS());
        contentValues.put(PLACES_CLOSINGTIME, place.getCLOSINGTIME());
        contentValues.put(PLACES_OPENINGTIME, place.getOPENINGTIME());
        contentValues.put(PLACES_TOWNID, place.getTOWNID());
        contentValues.put(PLACES_SUBCATEGORYID, place.getSUBCATID());
        db.update(TABLE_PLACES, contentValues, PLACE_ID + " = ?", new String[]{id});
        return true;
    }

    public Cursor getAllAdmins() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_USERS + " WHERE ROLE = 'admin';", null);
        return res;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_USERS + " WHERE ROLE = 'user';", null);
        return res;
    }

    public int removeAdmin(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ROLE, "user");
        return db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{id});
    }

    public void superAdmin(Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FNAME, user.getFIRSTNAME());
        contentValues.put(COL_LNAME, user.getLASTNAME());
        contentValues.put(COL_EMAIL, user.getEMAIL());
        contentValues.put(COL_PASSWORD, user.getPASSWORD());
        contentValues.put(COL_ROLE, user.getROLE());
        long result = db.insert(TABLE_USERS, null, contentValues);
    }

    public Cursor adminWisePlaces(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_PLACES + " WHERE USERID = '" + id + "';", null);
        return res;
    }

    public Boolean searchIfExistPlace(Places place, String city, String category){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean flag = false;
        Cursor res = db.rawQuery("select * from " + TABLE_PLACES + " WHERE NAME = '" + place.getNAME() + "';", null);
        if (res.getCount() > 0){
            res = db.rawQuery("select * from " + TABLE_CITY + " WHERE NAME = '" + city + "';", null);
            if (res.getCount() > 0){
                res = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY + " WHERE NAME = '" + category + "';", null);
                if (res.getCount() > 0){
                    flag = true;
                }
            }
        }
        return flag;
    }

    public int disableUser(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ROLE, "disable");
        return db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{id});
    }

    public boolean addCity(String city){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_NAME, city);
        long result = db.insert(TABLE_CITY, null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addCategory(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_NAME, category);
        long result = db.insert(TABLE_CATEGORY, null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getAllCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CATEGORY + ";", null);
        return res;
    }

    public Cursor getAllSubcategories(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_SUBCAT + " WHERE CATID = '" + id + "';" , null);
        return res;
    }

    public Cursor getAllTown(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_TOWN + " WHERE CITYID = '" + id + "';", null);
        return res;
    }

    public Cursor getAllCitites(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CITY + ";", null);
        return res;
    }

    public boolean addSubcategory(Subcategory subcat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBCAT_NAME, subcat.getNAME());
        contentValues.put(SUBCAT_CATID, subcat.getCATID());
        long result = db.insert(TABLE_SUBCAT, null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addTown(Town town){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOWN_NAME, town.getNAME());
        contentValues.put(TOWN_CITYID, town.getCITYID());
        long result = db.insert(TABLE_TOWN, null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getCityById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select NAME from " + TABLE_CITY + " WHERE ID = '" + id + "';", null);
        return res;
    }

    public Cursor getCategoryById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select NAME from " + TABLE_CATEGORY + " WHERE ID = '" + id + "';", null);
        return res;
    }

    public Cursor getSubcategoryByid(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT NAME FROM " + TABLE_SUBCAT + " WHERE ID = '" + id + "'", null);
        return res;
    }

    public Cursor getTownById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT NAME FROM " + TABLE_TOWN + " WHERE ID = '" + id + "';", null);
        return  res;
    }

    public Cursor search(String name, int cityid, int catid, int townid, int subCategoryid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_PLACES + " WHERE CITYID = '" + cityid + "'"
                + " AND CATEGORYID = '" + catid + "' AND TOWNID = '" + townid + "' AND SUBCATEGORYID = '" + subCategoryid + "'", null);
        return res;
    }

    public long insertImage(byte[] image, long id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PICTURES_PATH1, image);
        contentValues.put(PICTURES_PLACEID, id);
        long result = db.insert(TABLE_PICTURES, null, contentValues);
        return result;
    }

    public Cursor getImage(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_PICTURES + " WHERE PLACEID = '" + id + "';",null);
        return res;
    }

}