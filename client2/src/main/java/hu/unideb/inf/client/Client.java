package hu.unideb.inf.client;// A Java program for a Client


import javax.inject.Named;
import hu.unideb.inf.Cards.Card;
import java.net.*;
import java.io.*;




@Named
public class Client implements PlayerChoice
{

    String line = "";
    String inLine = "";

    int button = 0;
    int currentBet;



    private int portNumber;
    // initialize socket and input output streams
    private Socket socket            = null;
    // input is clients pick
    private DataInputStream   input   = null;
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
            Card cardInHand = new Card();
            try {
                out.writeUTF(line);
                cardInHand = (Card)objectIn.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(cardInHand);
            button = 0;
            line = "";
        }
        //stand
        else if (button == 2 ) {
            try {
                out.writeUTF(line);
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
                input.close();
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