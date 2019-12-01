package hu.unideb.inf.client;// A Java program for a Client


import javax.inject.Named;
import hu.unideb.inf.Cards.Card;
import java.net.*;
import java.io.*;
import java.util.ArrayList;



@Named
public class Client implements PlayerChoice
{

    String line = "";


    int button = 0;
    int currentBet = 0;

    //player lost, progress means not winning or losing, player won
    String playerStanding;

    Card cardInHand = new Card();
    ArrayList<Card> allCardsInHand = new ArrayList<>();
    ArrayList<Card> allCardsInHand2 = new ArrayList<>();





    private int portNumber;
    // initialize socket and input output streams
    private Socket socket            = null;
    // message in is the servers response
    private DataInputStream   messageIn       =  null;
    private ObjectInputStream objectIn = null;

    private DataOutputStream out     = null;

    private int playerNumber;


    public void joinGameServer(){
        try
        {
            socket = new Socket("127.0.0.1", portNumber);
            System.out.println("Connected");

            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
            System.out.println("after initializing out with socket");
            // takes input from the server
            messageIn = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            objectIn = new ObjectInputStream(socket.getInputStream());
            System.out.println("after initializing massageIn");

            playerNumber = messageIn.readInt();
            System.out.println("Client read in the 'number': " + playerNumber);

            try {
                line = "newround";
                out.writeUTF(line);
                roundOne();

                playerStanding = checkWinCondition();
                out.writeUTF(playerStanding);
                isNewGame(playerStanding);

                playerStanding = "";
                button = 0;
                line = "";

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }



    public void sendData(){

        //hit
        if (button == 1) {
            try {
                cardInHand = new Card();
                out.writeUTF(line);
                cardInHand = (Card)objectIn.readObject();
                allCardsInHand.add(cardInHand);
                System.out.println("your cards now are: " + allCardsInHand);

                playerStanding = checkWinCondition();
                out.writeUTF(playerStanding);
                isNewGame(playerStanding);

                playerStanding = "";
                button = 0;
                line = "";
            } catch (IOException e) {
                e.printStackTrace();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
            //stand
        else if (button == 2 ) {
            try {
                out.writeUTF(line);
                checkWinCondition();
                out.writeInt(playerNumber);
                System.out.println("Players total number: " + playerNumber);
                line = messageIn.readUTF();
                if(line.equals("player won")){
                    System.out.println(line);
                    //raise players money by bet
                }
                else if(line.equals("dealer won")){
                    System.out.println(line);
                    //subtract bet from players money
                }
                else if(line.equals("push")){
                    System.out.println(line);
                    //dont change players money
                }

                roundOne();
            } catch (IOException e) {
                e.printStackTrace();
            }
            button = 0;
            line = "";
        }
        //double down
        else if (button == 3 ) {
            try {
                out.writeUTF(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            button = 0;
            line = "";
        }
        //split
        else if (button == 4 ) {
            try {
                out.writeUTF(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            button = 0;
            line = "";
        }
        //surrender
        else if (button == 5 ) {
            try {
                out.writeUTF(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            button = 0;
            line = "";
        }
        //bet
        else if(button == 6){
            try {
                out.writeUTF(line);
                out.writeInt(getCurrentBet());
            } catch (IOException e) {
                e.printStackTrace();
            }

            button = 0;
            line = "";
        }
        //exit
        else if (button == 7) {
            // close the connection
            try {
                out.writeUTF(line);
                line = "";
                messageIn.close();
                out.close();
                socket.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        } else {
            System.out.println("default case, line is currently: " + line);

        }
    }

    // asking for a card
    public void hit(){
        button = 1;
        line = "hit";
        System.out.println("someone clicked hit");
    }

    // not asking for a card this turn
    public void stand(){
        button = 2;
        line = "stand";
        System.out.println("someone clicked stand");
    }

    // doubling the player's bet and asking for 1 card (he can't ask for more cards)
    public void doubleDown(){
        button = 3;
        line = "double";
        System.out.println("someone clicked double down");
    }

    // splitting the cards
    public void split(){
        button = 4;
        line = "split";
        System.out.println("someone clicked split");
    }

    //surrendering, losing half of the bet
    public void surrender(){
        button = 5;
        line = "surrender";
        System.out.println("someone clicked surrender");
    }

    // place the amount the player wants and can
    public void placeBet() {
        button = 6;
        line = "bet";

        System.out.println("someone clicked placebet");
    }

    // exit game
    public void exitGame(){
    button = 7;
    line = "exit";
    System.out.println("someone clicked exit");
    }

    //round one of the game, get 2 cards
    public void roundOne(){
        playerNumber = 0;
        allCardsInHand.clear();
        allCardsInHand2.clear();
        //read in the first 2 cards given to the player
        System.out.println("before new card");
        cardInHand = new Card();
        System.out.println("before accepting card");
        try {
            cardInHand = (Card)objectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("before putting in arraylist");
        allCardsInHand.add(cardInHand);

        cardInHand = new Card();
        try {
            cardInHand = (Card)objectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        allCardsInHand.add(cardInHand);
        System.out.println("your cards: " + allCardsInHand);
        if(checkWinCondition().equals("won")){
            stand();
            sendData();
        }
    }

    public String checkWinCondition(){
        int totalNumber = 0;
        String numPerCard;
        int ace = 0;
        for (int i = 0; i<allCardsInHand.size(); i++){
            numPerCard = allCardsInHand.get(i).getNumber();
            if(numPerCard.equals("Q") || numPerCard.equals("K") || numPerCard.equals("J")){
                totalNumber += 10;
            }
            else if(numPerCard.equals("A")){
                ace += 1;
                totalNumber += 1;
            }
            else {
                totalNumber += Integer.parseInt(allCardsInHand.get(i).getNumber());
            }
            if(ace > 0 && totalNumber <= 11 ){
                totalNumber = totalNumber + 10;
            }
        }
        if(totalNumber == 21){
            System.out.println("this player won");
            return "won";
        }
        else if(totalNumber > 21){
            System.out.println("This player lost");
            return "lost";
        }
        playerNumber = totalNumber;
        return "progress";
    }

    public void isNewGame(String condition){
        if(condition.equals("won") || condition.equals("lost")){
            roundOne();
        }
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public Client() {
    }

}