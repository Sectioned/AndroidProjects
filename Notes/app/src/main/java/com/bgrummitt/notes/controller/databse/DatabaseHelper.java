package com.bgrummitt.notes.controller.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String TAG = DatabaseHelper.class.getSimpleName();

    private final static String TO_COMPLETE_TABLE_NAME = "NOTES_TO_COMPLETE";
    private final static String COMPLETED_TABLE_NAME = "NOTES_COMPLETED";
    public final static String ID_COLUMN_NAME = "NOTE_ID";
    public final static String SUBJECT_COLUMN_NAME = "SUBJECT";
    public final static String NOTE_COLUMN_NAME = "NOTE";
    public final static String DATE_COLUMN_NAME = "DATE_COMPLETED";

    private Context mContext;
    private int toBeCompletedCurrentMaxID;

    public DatabaseHelper(Context context, String db_name) {
        super(context, db_name, null, 1);

        this.mContext = context;

        //Check if db is empty
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + TO_COMPLETE_TABLE_NAME;
        Cursor mCursor = db.rawQuery(count, null);
        mCursor.moveToFirst();
        if(mCursor.getInt(0) > 0){
            // If the db is not empty select all columns order by largest first descending order
            Cursor tempCursor = db.rawQuery("SELECT " + ID_COLUMN_NAME + " FROM " + TO_COMPLETE_TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC;", null);
            tempCursor.moveToFirst();
            // First item is the largest ID so set max id of off its id column
            toBeCompletedCurrentMaxID = tempCursor.getInt(0);
            tempCursor.close();
            mCursor.close();
        }else {
            // If empty set to 0
            toBeCompletedCurrentMaxID = 0;
        }
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableNotesToComplete = "CREATE TABLE " + TO_COMPLETE_TABLE_NAME + "(" + ID_COLUMN_NAME +" INTEGER PRIMARY KEY, " + SUBJECT_COLUMN_NAME + " TEXT, " + NOTE_COLUMN_NAME + " TEXT)";
        String CreateTableNotesCompleted = "CREATE TABLE " + COMPLETED_TABLE_NAME + "(" + ID_COLUMN_NAME +" INTEGER PRIMARY KEY, " + SUBJECT_COLUMN_NAME + " TEXT, " + NOTE_COLUMN_NAME + " TEXT, " + DATE_COLUMN_NAME + " DATE)";

        db.execSQL(CreateTableNotesCompleted);
        db.execSQL(CreateTableNotesToComplete);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COMPLETED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TO_COMPLETE_TABLE_NAME);

        onCreate(db);
    }

    public boolean addNoteToBeCompleted(String subject, String note){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ID_COLUMN_NAME, toBeCompletedCurrentMaxID + 1);
        toBeCompletedCurrentMaxID += 1;
        cv.put(SUBJECT_COLUMN_NAME, subject);
        cv.put(NOTE_COLUMN_NAME, note);

        long result = db.insert(TO_COMPLETE_TABLE_NAME, null, cv);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public void moveNoteToCompleted(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        boolean complete = db.delete(TO_COMPLETE_TABLE_NAME, ID_COLUMN_NAME + " = " + id, null) > 0;

        Log.d(TAG, String.format("ID deleting = %d, SUCCESS = %b", id, complete));

        String query = "SELECT * FROM " + TO_COMPLETE_TABLE_NAME + " WHERE " + ID_COLUMN_NAME  + " > " + id;

        Cursor mCursor = db.rawQuery(query, null);

        if(mCursor.moveToFirst()){
            int columnID = mCursor.getColumnIndex(ID_COLUMN_NAME);
            int posID;
            while(!mCursor.isAfterLast()){
                posID = mCursor.getInt(columnID);
                query = "UPDATE " + TO_COMPLETE_TABLE_NAME + " SET " + ID_COLUMN_NAME + " = " + (posID - 1) + " WHERE " + ID_COLUMN_NAME  + " = " + posID;
                db.execSQL(query);
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        toBeCompletedCurrentMaxID -= 1;
    }

    private String tableQuery = "SELECT * FROM %s";

    public Cursor getNotesToBeCompleted(){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(String.format(tableQuery, TO_COMPLETE_TABLE_NAME), null);
    }

    public Cursor getNotesCompleted(){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(String.format(tableQuery, COMPLETED_TABLE_NAME), null);
    }

    public int getToBeCompletedCurrentMaxID(){
        return toBeCompletedCurrentMaxID;
    }

}
