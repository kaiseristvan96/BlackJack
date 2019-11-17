package hu.unideb.inf.client;

import javax.inject.Named;

//play.xhtml része, ezek gombok lesznek

public interface PlayerChoice {
    // asking for a card
    public void hit();

    // not asking for a card this turn
    public void stand();

    // doubling the player's bet and asking for 1 card (he can't ask for more cards)
    public void doubleDown();

    // splitting the cards
    public void split();

    //surrendering, losing half of the bet
    public void surrender();
}
