package com.howls.flashcard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by howls on 17/11/23.
 */

public class MyDBHandle extends SQLiteOpenHelper {

    private static final String TABLE_FLASHCARD = "flashcard";
    private static final String COLUMN_FLASHCARD_NAME = "word";
    private static final String COLUMN_FLASHCARD_READ = "read";
    private static final String COLUMN_FLASHCARD_TRANSLATE = "translate";
    private static final String COLUMN_FLASHCARD_LANGUAGE = "language";
    private static final String COLUMN_FLASHCARD_SOUND = "sound";
    private static final String COLUMN_FLASHCARD_ALBUM_ID = "albumId";

    private static final String TABLE_ALBUM = "album";
    private static final String COLUMN_ALBUM_NAME = "name";
    private static final String COLUMN_ALBUM_DESCRIPTION = "description";

    private String query;


    public MyDBHandle(Context context) {
        super(context, "lang13.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        query = "create table " + TABLE_ALBUM + " (id integer primary key autoincrement, "+COLUMN_ALBUM_NAME+" text, "+COLUMN_ALBUM_DESCRIPTION+" text)";
        sqLiteDatabase.execSQL(query);
        query = "create table " + TABLE_FLASHCARD + " (id integer primary key autoincrement, "+COLUMN_FLASHCARD_NAME+" text, "+COLUMN_FLASHCARD_READ+" text, "+COLUMN_FLASHCARD_TRANSLATE+" text, "+COLUMN_FLASHCARD_LANGUAGE+" text, "+COLUMN_FLASHCARD_SOUND+" text, "+COLUMN_FLASHCARD_ALBUM_ID+" integer, foreign key("+COLUMN_FLASHCARD_ALBUM_ID+") REFERENCES "+TABLE_ALBUM+"("+COLUMN_FLASHCARD_ALBUM_ID+"))";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        query = "DROP TABLE IF EXISTS " + TABLE_ALBUM;
        sqLiteDatabase.execSQL(query);
        query = "DROP TABLE IF EXISTS " + TABLE_FLASHCARD;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    public void addFlashcard(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FLASHCARD_NAME,flashcard.getWord());
        values.put(COLUMN_FLASHCARD_READ,flashcard.getRead());
        values.put(COLUMN_FLASHCARD_TRANSLATE,flashcard.getTranslate());
        values.put(COLUMN_FLASHCARD_LANGUAGE,flashcard.getLanguage());
        values.put(COLUMN_FLASHCARD_SOUND,flashcard.getSound());
        values.put(COLUMN_FLASHCARD_ALBUM_ID,flashcard.getAlbumId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_FLASHCARD,null,values);
        db.close();
    }

    public void addAlbum(Album album) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALBUM_NAME,album.getName());
        values.put(COLUMN_ALBUM_DESCRIPTION,album.getDescription());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ALBUM,null,values);
        db.close();
    }

    public Flashcard getFlashcard(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery( "select * from "+TABLE_FLASHCARD+" where id="+id, null );
        if (cursor != null)
            cursor.moveToFirst();
        Flashcard flashcard = new Flashcard(cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
        flashcard.setId(Integer.parseInt(cursor.getString(0)));
        return flashcard;

    }

    public Album getAlbum(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery( "select * from "+TABLE_ALBUM+" where id="+id, null );
        if (cursor != null)
            cursor.moveToFirst();
        Album album = new Album(cursor.getString(1), cursor.getString(2));
        album.setId(Integer.parseInt(cursor.getString(0)));
        return album;

    }

    public List<Flashcard> getAllFashcards(String albumId) {
        List<Flashcard> flashcardList = new ArrayList<Flashcard>();

        String selectQuery = "select * from " + TABLE_FLASHCARD+" where "+COLUMN_FLASHCARD_ALBUM_ID+"="+albumId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Flashcard flashcard = new Flashcard(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5), cursor.getString(6));
                flashcard.setId(Integer.parseInt(cursor.getString(0)));
                flashcardList.add(flashcard);
            } while (cursor.moveToNext());
        }
        return flashcardList;
    }

    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<Album>();

        String selectQuery = "select * from " + TABLE_ALBUM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Album album = new Album(cursor.getString(1),cursor.getString(2));
                album.setId(Integer.parseInt(cursor.getString(0)));
                albumList.add(album);
            } while (cursor.moveToNext());
        }
        return albumList;
    }
}
