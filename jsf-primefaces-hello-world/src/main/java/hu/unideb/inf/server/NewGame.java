package hu.unideb.inf.server;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.inject.Named;

@Named
public class NewGame {
    private int portNumber;



    //initialize socket and input stream
    private Socket socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream  in       =  null;
    private DataOutputStream messageOut = null;

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
            System.out.println("after initializing messageout");
            // sending player's number
            messageOut.writeInt(1);
            System.out.println("after sending out '1'");



            String line = "";
            int line2;
            // reads message from client until "Over" is sent
            while (!line.equals("exit"))
            {
                try
                {


                    line = in.readUTF();
                    //line2 = in.readInt();
                    System.out.println(line);
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
}
