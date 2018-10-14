package edu.fsu.cs.mobile.testdatabase.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity( tableName = "cards")
public class Card{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "setName")
    private String setName;

    @ColumnInfo(name = "front")
    private String front;

    @ColumnInfo(name = "back")
    private String back;

    @Ignore
    public Card() {}

    public Card(String setName,
                String front,
                String back ) {
        this.setName = setName;
        this.front = front;
        this.back = back;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String set) {
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