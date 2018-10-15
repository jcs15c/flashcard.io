package edu.fsu.cs.mobile.testdatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Database.CardRepository;
import edu.fsu.cs.mobile.testdatabase.Database.Card;

/*
The ViewModel setup here is apparently the standard for these kinds of databases, allowing
for a single database to persist across versions of an activity. The example they use most
commonly is that it doesn't destroy and recreate the database whenever the screen is rotated.

As a summary, the hierarchy of function calls is as follows:
ViewModel --> Repository --> Data Access Object
 */
public class CardViewModel extends AndroidViewModel {
    private CardRepository cardRepository;

    // I don't know what this declaration does.
    private LiveData<List<Card>> cardLiveData;

    public CardViewModel( Application application ) {
        super( application );
        cardRepository = new CardRepository(application);
    }

    // These methods can be called from the Activity, and do what they say they do.
    LiveData<List<Card>> getSet( String setName ) { return cardRepository.getFullSet( setName ); }
    LiveData<List<Card>> getAllCards( ) { return cardRepository.getAllCards(); }
    LiveData<List<String>> getSetNames( ) { return cardRepository.getSetNames(); }

    public void insertCard( Card card ) { cardRepository.insertCard( card ); }
    public void insertCard(String setName,
                           String front,
                           String back) { cardRepository.insertCard( setName, front, back ); }
    public void deleteSet( String setName ) { cardRepository.deleteSet(setName); }
    public void deleteAllCards() { cardRepository.deleteAllCards(); }
    int countAllCards() { return cardRepository.countAllCards(); }
}
