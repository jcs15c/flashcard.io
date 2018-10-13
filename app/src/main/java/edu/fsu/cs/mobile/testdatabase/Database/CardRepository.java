package edu.fsu.cs.mobile.testdatabase.Database;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.fsu.cs.mobile.testdatabase.Model.Card;

public class CardRepository {

    private CardDatabase cardDatabase;

    public CardRepository(Application application) {
        cardDatabase = CardDatabase.getDatabase(application);
    }

    public void insertCard(String setName,
                           String front,
                           String back) {

        Card card = new Card();
        card.setSetName(setName);
        card.setFront(front);
        card.setBack(back);

        insertCard(card);
    }

    public void insertCard(final Card card) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cardDatabase.cardDAO().insertCard(card);
                return null;
            }
        }.execute();
    }

    public LiveData<List<String>> getSetNames() {
        return cardDatabase.cardDAO().getSetNames();
    }

    public LiveData<Card> getCard(int id) {
        return cardDatabase.cardDAO().getCard(id);
    }

    public LiveData<List<Card>> getAllCards() { return cardDatabase.cardDAO().getAllCards(); }
    public LiveData<List<Card>> getFullSet(String setName) {
        return cardDatabase.cardDAO().getFullSet(setName);
    }

    public int countAllCards() {
        try {
            return new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    return cardDatabase.cardDAO().countAllCards();
                }
            }.execute().get();
        } catch( java.util.concurrent.ExecutionException e ) { return -1; }
          catch( java.lang.InterruptedException e ) { return -1; }


    }

    public void deleteCard(final int id) {
        final LiveData<Card> card = getCard(id);
        if(card != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    cardDatabase.cardDAO().deleteCard(card.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteSet(final String setName) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cardDatabase.cardDAO().deleteSet(setName);
                return null;
            }
        }.execute();
    }

    public void deleteAllCards() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cardDatabase.cardDAO().deleteAllCards();
                return null;
            }
        }.execute();
    }

    public void deleteCard(final Card card) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cardDatabase.cardDAO().deleteCard(card);
                return null;
            }
        }.execute();
    }

    public void updateCard(final Card card) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cardDatabase.cardDAO().updateCard(card);
                return null;
            }
        }.execute();
    }


}