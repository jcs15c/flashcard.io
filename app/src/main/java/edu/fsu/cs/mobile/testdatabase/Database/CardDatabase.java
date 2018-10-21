package edu.fsu.cs.mobile.testdatabase.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/*
This class is for the actual Database object; I probably understand this
file the least, but I imagine it simply creates a database and syncs it with
whatever memory is already used internally for the database.
*/
@Database(entities = {Card.class}, version = 2, exportSchema = false)
public abstract class CardDatabase extends RoomDatabase {
    public abstract CardDAO cardDAO();
    public static final String DATABASE_NAME = "Flashcard-Database";

    private static volatile CardDatabase INSTANCE;

    static CardDatabase getDatabase( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized (CardDatabase.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CardDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}