package com.smap.f16.grp12.racketometer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.smap.f16.grp12.racketometer.models.Session;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/*
 * Database helper to handle {@link Session} entities.
 * This class is inspired by a demo project
 * from Aarhus University ITSMAP-01 course lecture 6
 * 2016-04-26
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "racketometer.db";

    public static abstract class SessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "Session";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_USER_ID = "usedId";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_HITS = "hits";
        public static final String COLUMN_NAME_SPEED = "speed";
        public static final String COLUMN_NAME_POWER = "power";
        public static final String COLUMN_NAME_AGILITY = "agility";
        public static final String COLUMN_NAME_LOCATION_LAT = "latitude";
        public static final String COLUMN_NAME_LOCATION_LONG = "longitude";
    }

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INT = " INTEGER";
    private static final String TYPE_DOUBLE = " REAL";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE_SESSION =
            "CREATE TABLE " + SessionEntry.TABLE_NAME + " (" +
                    SessionEntry.COLUMN_NAME_ENTRY_ID + TYPE_INT + " PRIMARY KEY" + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_USER_ID + TYPE_INT + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_TIMESTAMP + TYPE_INT + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_DESCRIPTION + TYPE_TEXT + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_HITS + TYPE_INT + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_SPEED + TYPE_DOUBLE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_POWER + TYPE_DOUBLE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_AGILITY + TYPE_DOUBLE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_LOCATION_LAT + TYPE_DOUBLE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_LOCATION_LONG + TYPE_DOUBLE +
                    " )";

    private static final String SQL_DROP_SESSION_IF_EXISTS =
            "DROP TABLE IF EXISTS " + SessionEntry.TABLE_NAME;

    private static final String SQL_GET_SESSIONS =
            "SELECT * FROM " + SessionEntry.TABLE_NAME;

    private static final String SQL_CLEAR_TABLE =
            "DELETE FROM " + SessionEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SESSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_SESSION_IF_EXISTS);

        onCreate(db);
    }

    /**
     * Close database connection if open.
     */
    public void closeDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();

        if (db == null || !db.isOpen()) {
            return;
        }

        db.close();
    }

    /**
     * Remove all sessions in database.
     */
    public void clearSessions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_CLEAR_TABLE);
    }

    /**
     * Save new {@link Session} instance in database.
     * @param session The {@link Session} to save.
     * @return The entity ID given by the database.
     */
    public long createSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SessionEntry.COLUMN_NAME_TIMESTAMP, session.getDate().getMillis());
        values.put(SessionEntry.COLUMN_NAME_USER_ID, session.getUserId());
        values.put(SessionEntry.COLUMN_NAME_DESCRIPTION, session.getDescription());
        values.put(SessionEntry.COLUMN_NAME_HITS, session.getHits());
        values.put(SessionEntry.COLUMN_NAME_SPEED, session.getSpeed());
        values.put(SessionEntry.COLUMN_NAME_POWER, session.getPower());
        values.put(SessionEntry.COLUMN_NAME_AGILITY, session.getAgility());
        values.put(SessionEntry.COLUMN_NAME_LOCATION_LAT, session.getLatitude());
        values.put(SessionEntry.COLUMN_NAME_LOCATION_LONG, session.getLongitude());

        return db.insert(SessionEntry.TABLE_NAME, null, values);
    }

    /**
     * Get {@link List} of {@link Session}.
     * @return The {@link List} of {@link Session} received.
     */
    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_SESSIONS, null);

        if(cursor.moveToFirst()) {
            do {
                Session session = getSession(cursor);
                sessions.add(session);
            } while (cursor.moveToNext());
        }

        return sessions;
    }

    /**
     * Init {@link Session} instance from database.
     * @param cursor The database cursor.
     * @return An initialized {@link Session} object.
     */
    private Session getSession(Cursor cursor) {
        long id = getLong(cursor, SessionEntry.COLUMN_NAME_ENTRY_ID);
        long timestamp = getLong(cursor, SessionEntry.COLUMN_NAME_TIMESTAMP);
        long userId = getLong(cursor, SessionEntry.COLUMN_NAME_USER_ID);
        String description = getString(cursor, SessionEntry.COLUMN_NAME_DESCRIPTION);
        int hits = getInt(cursor, SessionEntry.COLUMN_NAME_HITS);
        double speed = getDouble(cursor, SessionEntry.COLUMN_NAME_SPEED);
        double power = getDouble(cursor, SessionEntry.COLUMN_NAME_POWER);
        double agility = getDouble(cursor, SessionEntry.COLUMN_NAME_AGILITY);
        double latitude = getDouble(cursor, SessionEntry.COLUMN_NAME_LOCATION_LAT);
        double longitude = getDouble(cursor, SessionEntry.COLUMN_NAME_LOCATION_LONG);

        return new Session(
                id,
                new DateTime(timestamp),
                userId,
                description,
                hits,
                speed,
                power,
                agility,
                latitude,
                longitude
        );
    }

    private long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private double getDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    private String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
