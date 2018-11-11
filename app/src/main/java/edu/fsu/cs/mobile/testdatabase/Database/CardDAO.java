package edu.fsu.cs.mobile.testdatabase.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;
/*
The Data Access Object provides the interface between SQL commands and Java functions.
The annotations describe the kind of SQL operation and its definition, while
the Java function describes the equivalent Java method for the Database object.
*/
@Dao
public interface CardDAO{
    @Insert
    void insertCard( Card card );

    /*
    This and most other values are returned as LiveData wrapped values so that
    they can be observed in each activity.
    */

    // Only return non-dummy cards
    @Query("SELECT * FROM cards WHERE setName=:setName AND front != '' AND back != '' ")
    LiveData<List<Card>> getSetCards(String setName);

    @Query("SELECT * FROM cards WHERE setName=:setName AND front != '' AND back != '' ")
    List<Card> getStaticSet(String setName);

    @Query("SELECT DISTINCT setName FROM cards")
    LiveData<List<String>> getSetNames();

    @Query("SELECT * FROM cards WHERE id=:id")
    Card getCard(String id);

    @Query("SELECT * FROM cards")
    LiveData<List<Card>> getAllCards();

    // I don't know why this one isn't in a LiveData wrapper; it works fine without it somehow.
    @Query("SELECT COUNT( * ) FROM cards")
    int countAllCards();

    @Query("SELECT COUNT( * ) FROM cards WHERE setName=:setName")
    int countCardSet(String setName);

    // Use a card with empty string as the front and back as the dummy card per set
    @Query("SELECT COUNT( * ) FROM cards WHERE setName=:setName AND front='' AND back=''")
    int setExists(String setName);

    @Delete
    void deleteCard( Card card );

    @Delete
    void deleteCards( Card... card );

    //@Query("DELETE FROM cards WHERE setName=:setName AND front=:front AND back=:back")
    //void deleteCard( String setName, String front, String back);

    @Query("DELETE FROM cards WHERE setName=:setName")
    void deleteSet(String setName);

    @Query("DELETE FROM cards")
    void deleteAllCards();

    @Update
    void updateCard( Card card );

    // Change setName column of cards
    @Query("UPDATE cards SET setName=:newSetName WHERE setName=:oldSetName")
    void renameSet(String oldSetName, String newSetName);

    @Update
    void updateCards( Card... cards );


}