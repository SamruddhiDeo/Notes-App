package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NotesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NOTES = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NOTES +"(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NOTES);
        onCreate(db);
    }

    public void addNote(String title, String description){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,title);
        values.put(KEY_DESCRIPTION,description);

        db.insert(TABLE_NOTES,null,values);
    }

    public ArrayList<NotesModel> fetchNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NOTES,null);

        ArrayList<NotesModel> arrContacts = new ArrayList<>();

        while (cursor.moveToNext()){
            NotesModel model = new NotesModel();
            model.id = cursor.getInt(0);
            model.title = cursor.getString(1);
            model.description = cursor.getString(2);
            arrContacts.add(model);
        }
        return arrContacts;
    }

    public void editNote(int id,String title,String description){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE,title);
        contentValues.put(KEY_DESCRIPTION,description);

        db.update(TABLE_NOTES,contentValues,KEY_ID+" = "+id,null);
    }

    public void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NOTES,KEY_ID+" = ? ",new String[]{String.valueOf(id)});
    }

}
