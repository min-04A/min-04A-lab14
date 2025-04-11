import java.util.*;
import java.net.*;
import java.io.*;

public class Client
{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) 
    {
        this.socket = new Socket(host,port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Socket getSocket()
    {
        return this.socket;
    }

    public void handshake()
    {
        out.println("12345");
    }

    public void disconnect()
    {
        in.close();
        out.close();
    }
}