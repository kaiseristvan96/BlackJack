package hu.unideb.inf.server;


import hu.unideb.inf.Cards.Card;
import hu.unideb.inf.Cards.Deck;

import java.io.*;
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


            // reads message from client until "Over" is sent
            while (!line.equals("exit"))
            {
                try
                {
                    beginNewRound();
                    line = in.readUTF();
                    switch (line){
                        case "hit":
                            //draw new card, send it back...
                            System.out.println(line);
                            /*for (int i = 0; i<fullDeck.size();i++){
                                System.out.println(fullDeck.get(i));
                            }*/
                            Card pickCard = fullDeck.get(0);
                            fullDeck.remove(0);
                            System.out.println("Sending: " + pickCard);

                            //messageOut.writeUTF(pickCard.toString());
                            System.out.println("Sending with objectout: " + pickCard);
                            objectOut.writeObject(pickCard);
                            break;
                        case "stand":
                            //skip turn
                            System.out.println(line);
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
                    }
                    //line2 = in.readInt();

                    //System.out.println(line2);


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
        fullDeck = newDeck.generateDeck();
        Collections.shuffle(fullDeck);
    }

    public void checkWinConditions(){

    }
}
