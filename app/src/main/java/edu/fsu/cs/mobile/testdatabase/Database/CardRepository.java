package edu.fsu.cs.mobile.testdatabase.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
/*
This class serves as another interface for the Database, containing within it a single Database
member data, and a variety of operations on said database. Some of these operations are encapsulated
within an AsyncTask, so that the Room operations don't run on the main thread of the application,
as that would make it slow. I don't know why some, like countAllCards, need to be in an AsyncTask
while others like "getFullSet" don't. Both work in their present state for some reason.
 */
public class CardRepository {

    private CardDatabase cardDatabase;

    public CardRepository(Application application) {
        cardDatabase = CardDatabase.getDatabase(application);
    }

    // Allows for input of a card into the database based only on its values, or a Card object.
    public void insertCard(String setName,
                           String front,
                           String back) {

        Card card = new Card();
        card.setSetName(setName);
        card.setFront(front);
        card.setBack(back);

        insertCard(card);
    }

    // I don't know if these AsyncTask warnings are important, or how to tell if there's a leak
    public void insertCard(final Card card) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cardDatabase.cardDAO().insertCard(card);
                return null;
            }
        }.execute();
    }

    // Each method here follows the same general format; call the DAO on the database, then
    //      call the corresponding DAO method for each repository method.
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
        }
        // I don't know if this is the right way to handle the exceptions here.
        //      countAllCards is mostly used for debugging purposes though, so it's not a big deal.
        catch( java.util.concurrent.ExecutionException e ) { return -1; }
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