package hu.unideb.inf.Cards;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<String> suits = List.of("Club","Diamond","Heart","Spade");
    private List<String> indexes = List.of("A","2","3","4","5","6","7","8","9","10","J","Q","K");


    public ArrayList<Card> generateDeck(){
        ArrayList<Card> allCards = new ArrayList<>();
        for( int i = 0; i < 13 ; i++){
            for(int j = 0; j < 4; j++){
                Card created = new Card();
                created.setName(suits.get(j));
                created.setNumber(indexes.get(i));
                allCards.add(created);
            }
        }
        return allCards;
    }

}
