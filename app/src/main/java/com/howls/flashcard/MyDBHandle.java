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
    private static final String COLUMN_FLASHCARD_SOUND = "sound";
    private static final String COLUMN_FLASHCARD_ALBUM = "albumId";
    private static final String COLUMN_FLASHCARD_EXAMPLES = "examples";
    private static final String COLUMN_FLASHCARD_NOTES = "notes";

    private static final String TABLE_ALBUM = "album";
    private static final String COLUMN_ALBUM_NAME = "name";

    private String query;


    public MyDBHandle(Context context) {
        super(context, "lang26.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        query = "create table " + TABLE_ALBUM + " (id integer primary key autoincrement, "+COLUMN_ALBUM_NAME+" text)";
        sqLiteDatabase.execSQL(query);
        query = "create table " + TABLE_FLASHCARD + " (id integer primary key autoincrement, "+COLUMN_FLASHCARD_NAME+" text, "+COLUMN_FLASHCARD_READ+" text, "+COLUMN_FLASHCARD_TRANSLATE+" text, "+COLUMN_FLASHCARD_SOUND+" text, "+COLUMN_FLASHCARD_EXAMPLES+" text, "+COLUMN_FLASHCARD_NOTES+" text, "+COLUMN_FLASHCARD_ALBUM+" integer, FOREIGN KEY("+COLUMN_FLASHCARD_ALBUM+") REFERENCES "+TABLE_ALBUM+"(id) ON DELETE CASCADE)";
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
        values.put(COLUMN_FLASHCARD_SOUND,flashcard.getSound());
        values.put(COLUMN_FLASHCARD_ALBUM,flashcard.getAlbumId());
        values.put(COLUMN_FLASHCARD_EXAMPLES,flashcard.getExamples());
        values.put(COLUMN_FLASHCARD_NOTES,flashcard.getNotes());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_FLASHCARD,null,values);
        db.close();
    }

    public void upgradeFlashcard(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FLASHCARD_NAME,flashcard.getWord());
        values.put(COLUMN_FLASHCARD_READ,flashcard.getRead());
        values.put(COLUMN_FLASHCARD_TRANSLATE,flashcard.getTranslate());
        values.put(COLUMN_FLASHCARD_SOUND,flashcard.getSound());
        values.put(COLUMN_FLASHCARD_ALBUM,flashcard.getAlbumId());
        values.put(COLUMN_FLASHCARD_EXAMPLES,flashcard.getExamples());
        values.put(COLUMN_FLASHCARD_NOTES,flashcard.getNotes());
        SQLiteDatabase db = getWritableDatabase();

        String where = "id=?";
        String[] whereArgs = new String[] {String.valueOf(flashcard.getId())};

        db.update(TABLE_FLASHCARD,values,where,whereArgs);


    }

    public void addAlbum(Album album) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALBUM_NAME,album.getName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ALBUM,null,values);
        db.close();
    }

    public Flashcard getFlashcard(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery( "select * from "+TABLE_FLASHCARD+" where id="+id,null);

        if (cursor != null)
            cursor.moveToFirst();
        Flashcard flashcard = new Flashcard(cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(7));
        flashcard.setId(Integer.parseInt(cursor.getString(0)));
        flashcard.setExamples(cursor.getString(5));
        flashcard.setNotes(cursor.getString(6));
        db.close();
        return flashcard;

    }

    public void deleteFlashcard(String id) {
        SQLiteDatabase db = getReadableDatabase();
        query = "DELETE FROM " + TABLE_FLASHCARD+" WHERE id="+id;
        db.execSQL(query);
        db.close();
    }

    public void deleteAlbum(String id) {
        SQLiteDatabase db = getReadableDatabase();
        query = "DELETE FROM " + TABLE_ALBUM+" WHERE id="+id;
        db.execSQL(query);
        db.close();
    }

    public Album getAlbum(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery( "select * from "+TABLE_ALBUM+" where id="+id, null );

        if (cursor != null)
            cursor.moveToFirst();
        Album album = new Album(cursor.getString(1));
        album.setId(Integer.parseInt(cursor.getString(0)));
        db.close();
        return album;

    }

    public List<Flashcard> getAllFashcards(String id) {
        List<Flashcard> flashcardList = new ArrayList<Flashcard>();

        String selectQuery = "select * from " + TABLE_FLASHCARD+" where albumID="+id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int position = 0;
        if (cursor.moveToFirst()) {
            do {
                Flashcard flashcard = new Flashcard(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(7));
                flashcard.setId(Integer.parseInt(cursor.getString(0)));
                flashcard.setExamples(cursor.getString(5));
                flashcard.setNotes(cursor.getString(6));
                flashcard.setPosition(position);
                flashcardList.add(flashcard);
                position ++;
            } while (cursor.moveToNext());
        }
        db.close();
        return flashcardList;
    }

    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<Album>();

        String selectQuery = "select * from " + TABLE_ALBUM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Album album = new Album(cursor.getString(1));
                album.setId(Integer.parseInt(cursor.getString(0)));
                albumList.add(album);
            } while (cursor.moveToNext());
        }
        db.close();
        return albumList;
    }
}
