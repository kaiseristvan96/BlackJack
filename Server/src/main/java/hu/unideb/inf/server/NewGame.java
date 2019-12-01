package hu.unideb.inf.server;


import hu.unideb.inf.Cards.Card;
import hu.unideb.inf.Cards.Deck;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import javax.inject.Named;


@Named
public class NewGame {
    private int portNumber;

    ArrayList<Card> fullDeck = new ArrayList<>();
    Deck newDeck = new Deck();
    ArrayList<Card> serverCardsInHand = new ArrayList<>();
    Card pickCard = null;
    int playerNumber = 0;


    //initialize socket and input stream
    private Socket socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream  in       =  null;
    private DataOutputStream messageOut = null;
    private ObjectOutputStream objectOut = null;

    //creating a new game at a custom port
    public void startNewGameServer(){
        if (portNumber < 1 || portNumber > 65535 ){
            System.out.println("Please enter a number between 1 " +
                    "and 65535 and try again!");
            return;
        }

        System.out.println("someone clicked a newgame button");
        NewGame server = new NewGame(portNumber);

    }


    // constructor with port
    public NewGame(int port)
    {
        // starts server and waits for a connection
        try
        {// takes input from the client socket


            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            System.out.println("after initializing in");
            // sends output to the socket
            messageOut = new DataOutputStream(socket.getOutputStream());
            objectOut = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("after initializing messageout");
            // sending player's number
            messageOut.writeInt(1);
            System.out.println("after sending out '1'");



            String line = "";
            int bet;



            // reads message from client until "exit" is sent
            while (!line.equals("exit"))
            {
                try
                {

                    line = in.readUTF();
                    switch (line){
                        case "hit":
                            //draw new card, send it back...
                            System.out.println(line);
                            /*for (int i = 0; i<fullDeck.size();i++){
                                System.out.println(fullDeck.get(i));
                            }*/
                            pickCard = new Card();
                            pickCard = fullDeck.get(0);
                            fullDeck.remove(0);
                            System.out.println("Sending: " + pickCard);

                            //messageOut.writeUTF(pickCard.toString());
                            System.out.println("Sending with objectout: " + pickCard);
                            objectOut.writeObject(pickCard);


                            break;
                        case "stand":
                            //skip turn
                            System.out.println(line);
                            playerNumber = in.readInt();
                            System.out.println("player's number: " + playerNumber);
                            checkWinConditions(playerNumber);
                            playerNumber = 0;
                            break;
                        case "double":
                            //draw new card double the bet...
                            System.out.println(line);
                            break;
                        case "split":
                            //2 of the same cards creates 2 new "sets"
                            System.out.println(line);
                            break;
                        case "surrender":
                            //lose half of the bet
                            System.out.println(line);
                            break;
                        case "bet":
                            //place amount of money as bet
                            bet = in.readInt();
                            System.out.println(line +": "+ bet);
                            break;
                        case "exit":
                            System.out.println(line);
                            break;
                        case "newround":
                            roundOne();
                            break;
                        case "won":
                            System.out.println("player won starting new game");
                            roundOne();
                            break;
                        case "lost":
                            System.out.println("player busted starting new game");
                            roundOne();
                            break;
                        case "progress":
                            System.out.println("player progressing");
                            break;
                    }

                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public NewGame() {
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public void beginNewRound(){
        serverCardsInHand.clear();
        fullDeck = newDeck.generateDeck();
        Collections.shuffle(fullDeck);
    }

    public void roundOne(){
        //shuffle and generate new deck on 1st launch
        beginNewRound();
        //players get 2 cards in the beginning

        pickCard = new Card();
        pickCard = fullDeck.get(0);
        fullDeck.remove(0);
        System.out.println("Sending: " + pickCard);
        try {
            objectOut.writeObject(pickCard);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pickCard = new Card();
        pickCard = fullDeck.get(0);
        fullDeck.remove(0);
        System.out.println("Server picking for themselves " + pickCard);
        serverCardsInHand.add(pickCard);


        pickCard = new Card();
        pickCard = fullDeck.get(0);
        fullDeck.remove(0);
        System.out.println("Sending: " + pickCard);
        try {
            objectOut.writeObject(pickCard);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pickCard = new Card();
        pickCard = fullDeck.get(0);
        fullDeck.remove(0);
        System.out.println("Server picking for themselves " + pickCard);
        serverCardsInHand.add(pickCard);
    }

    public void checkWinConditions(int playerNumber){
        int totalNumber = 0;
        String numPerCard;
        int ace = 0;
        for (int i = 0; i<serverCardsInHand.size(); i++){
            numPerCard = serverCardsInHand.get(i).getNumber();
            if(numPerCard.equals("Q") || numPerCard.equals("K") || numPerCard.equals("J")){
                totalNumber += 10;
            }
            else if(numPerCard.equals("A")){
                ace += 1;
                totalNumber += 1;
            }
            else {
                totalNumber += Integer.parseInt(serverCardsInHand.get(i).getNumber());
            }


            if(ace > 0 && totalNumber <= 11 ){
                totalNumber = totalNumber + 10;
            }

        }
        System.out.println("Dealer's cards: "+ serverCardsInHand);
        System.out.println("Dealers totalnumer: " + totalNumber);
        if(totalNumber > 21){
            System.out.println("dealer busted");
            try {
                messageOut.writeUTF("player won");
            } catch (IOException e) {
                e.printStackTrace();
            }
            roundOne();
        }else if(totalNumber<18){
            pickCard = new Card();
            pickCard = fullDeck.get(0);
            fullDeck.remove(0);
            System.out.println("Server picking for themselves " + pickCard);
            serverCardsInHand.add(pickCard);
            checkWinConditions(playerNumber);
        }
        else if( totalNumber == playerNumber){
            System.out.println("push");
            try {
                messageOut.writeUTF("push");
            } catch (IOException e) {
                e.printStackTrace();
            }
            roundOne();
        }else if(totalNumber > playerNumber){
            System.out.println("dealer won");
            try {
                messageOut.writeUTF("dealer won");
            } catch (IOException e) {
                e.printStackTrace();
            }
            roundOne();
        }else if(totalNumber < playerNumber){
            System.out.println("player won");
            try {
                messageOut.writeUTF("player won");
            } catch (IOException e) {
                e.printStackTrace();
            }
            roundOne();
        }
    }
}
