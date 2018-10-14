package edu.fsu.cs.mobile.testdatabase.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Card.class}, version = 1, exportSchema = false)
public abstract class CardDatabase extends RoomDatabase {
    public abstract CardDAO cardDAO();
    public static final String DATABASE_NAME = "Flashcard-Database";

    private static volatile CardDatabase INSTANCE;

    static CardDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized (CardDatabase.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CardDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

}