package edu.fsu.cs.mobile.testdatabase.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/*
Object class that defines what a "Card" is, both in terms of the Java object
and the Room entity. For Room, each Card represents a single row in the database.
*/

// In this configuration, there is a single table "cards" populated by all cards from all sets.
@Entity( tableName = "cards")
public class Card{
    /*
    Usually a hidden value, necessary to allow for 2 otherwise identical cards
    to exist in the database simultaneously.
    */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /*
    Each @ColumnInfo annotation defines a single column of the "cards" table,
    and how it relates to the Java data it represents.
    */
    @ColumnInfo(name = "setName")
    private String setName;

    @ColumnInfo(name = "front")
    private String front;

    @ColumnInfo(name = "back")
    private String back;

    @Ignore
    public Card() {}

    // Cards are still objects, so we can construct them with this
    public Card(String setName,
                String front,
                String back ) {
        this.setName = setName;
        this.front = front;
        this.back = back;
    }

    // Establish the getters and setters for each of the member variables of Card
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
}