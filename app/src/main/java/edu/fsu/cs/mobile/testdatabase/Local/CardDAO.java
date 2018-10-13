package edu.fsu.cs.mobile.testdatabase.Local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.provider.ContactsContract;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Model.Card;

@Dao
public interface CardDAO{

    @Insert
    void insertCard( Card card );

    @Query("SELECT * FROM cards WHERE setName=:setName")
    LiveData<List<Card>> getFullSet(String setName);

    @Query("SELECT DISTINCT setName FROM cards")
    LiveData<List<String>> getSetNames();

    @Query("SELECT * FROM cards WHERE id=:id")
    LiveData<Card> getCard(int id);

    @Query("SELECT * FROM cards")
    LiveData<List<Card>> getAllCards();

    @Query("SELECT COUNT( * ) FROM cards")
    int countAllCards();

    @Delete
    void deleteCard( Card card );

    @Query("DELETE FROM cards WHERE setName=:setName")
    void deleteSet(String setName);

    @Query("DELETE FROM cards")
    void deleteAllCards();

    @Update
    void updateCard( Card card );
}