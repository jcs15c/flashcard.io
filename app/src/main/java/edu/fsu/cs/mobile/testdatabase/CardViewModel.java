package edu.fsu.cs.mobile.testdatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Database.CardRepository;
import edu.fsu.cs.mobile.testdatabase.Model.Card;

public class CardViewModel extends AndroidViewModel {
    private CardRepository cardRepository;

    private LiveData<List<Card>> cardLiveData;

    public CardViewModel( Application application ) {
        super( application );
        cardRepository = new CardRepository(application);
    }

    LiveData<List<Card>> getSet( String setName ) { return cardRepository.getFullSet( setName ); }
    LiveData<List<Card>> getAllCards( ) { return cardRepository.getAllCards(); }

    public void insertCard( Card card ) { cardRepository.insertCard( card ); }
    public void insertCard(String setName,
                           String front,
                           String back) { cardRepository.insertCard( setName, front, back ); }
    public void deleteSet( String setName ) { cardRepository.deleteSet(setName); }
    public void deleteAllCards() { cardRepository.deleteAllCards(); }
    int countAllCards() { return cardRepository.countAllCards(); }
}
