package com.matpacheco.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppNameHere"; //CHANGE THIS LATER :)

    public static final String TABLE_NAME = "flashcards";
    public static final String KEY_CARD_ID = "flashcard_id";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_NEXTREVIEW = "next_review";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_DECK = "deck";

    //Create Table Query
    private static final String SQL_CREATE_FLASHCARDS =
            "CREATE TABLE flashcards (" + KEY_CARD_ID + " INTEGER PRIMARY KEY, "
            + KEY_QUESTION + " TEXT, " + KEY_ANSWER + " TEXT, " + KEY_NOTES + " TEXT, "
            + KEY_NEXTREVIEW + " TEXT, " + KEY_DECK + " TEXT, " + KEY_LEVEL + " INTEGER );";

    private static final String SQL_DELETE_FLASHCARDS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_FLASHCARDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_FLASHCARDS);
        this.onCreate(db);
    }

    //Adds a new flashcard to the database
    public long addFlashcard(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues flashcard_details = new ContentValues();
        flashcard_details.put(KEY_QUESTION, item.getQuestion());
        flashcard_details.put(KEY_ANSWER, item.getAnswer());
        flashcard_details.put(KEY_NOTES, item.getNotes());
        flashcard_details.put(KEY_NEXTREVIEW, item.getNext_review().getTimeInMillis());
        flashcard_details.put(KEY_DECK, item.getDeck());
        flashcard_details.put(KEY_LEVEL, item.getLevel());

        long newRowId = db.insert(TABLE_NAME, null, flashcard_details);
        db.close();
        return newRowId;
    }

    //Returns list of all flashcards
    public ArrayList getAllFlashcards()
    {
        ArrayList items = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_DECK + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                Item flashcard = new Item();
                flashcard.setId(cursor.getInt(0));
                flashcard.setQuestion(cursor.getString(1));
                flashcard.setAnswer(cursor.getString(2));
                flashcard.setNotes(cursor.getString(3));
                flashcard.setNext_review(cursor.getString(4));
                flashcard.setDeck(cursor.getString(5));
                flashcard.setLevel(cursor.getInt(6));

                //Add flashcard to list
                items.add(flashcard);
            }while(cursor.moveToNext());
        }
        return items;
    }

    //Returns specified flashcard
    public Item getFlashcard(int flashcard_id)
    {
        Item flashcard = new Item();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns =
                {KEY_CARD_ID, KEY_QUESTION, KEY_ANSWER, KEY_NOTES, KEY_NEXTREVIEW, KEY_DECK, KEY_LEVEL};
        String selection = KEY_CARD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(flashcard_id)};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
            flashcard.setId(cursor.getInt(0));
            flashcard.setQuestion(cursor.getString(1));
            flashcard.setAnswer(cursor.getString(2));
            flashcard.setNotes(cursor.getString(3));
            flashcard.setNext_review(cursor.getString(4));
            flashcard.setDeck(cursor.getString(5));
            flashcard.setLevel(cursor.getInt(6));
        }

        db.close();
        return flashcard;
    }

    //Updates the details of flashcard
    public void updateFlashcard(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String cardIds[] = {String.valueOf(item.getId())};

        ContentValues flashcard_details = new ContentValues();
        flashcard_details.put(KEY_QUESTION, item.getQuestion());
        flashcard_details.put(KEY_ANSWER, item.getAnswer());
        flashcard_details.put(KEY_NOTES, item.getNotes());
        flashcard_details.put(KEY_NEXTREVIEW, item.getNext_review().getTimeInMillis());
        flashcard_details.put(KEY_DECK, item.getDeck());
        flashcard_details.put(KEY_LEVEL, item.getLevel());
        db.update(TABLE_NAME, flashcard_details, KEY_CARD_ID + " = ?", cardIds);
        db.close();
    }

    //Deletes the specified flashcard
    public void deleteFlashcard(int flashcard_id)
    {
        String cardIds[] = {String.valueOf(flashcard_id)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_CARD_ID + " = ?", cardIds);
        db.close();
    }
}
