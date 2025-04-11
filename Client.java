import java.util.*;
import java.net.*;
import java.io.*;

public class Client
{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) throws Exception
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

    public void disconnect() throws Exception
    {
        in.close();
        out.close();
    }

    public String request(String number) throws Exception 
    {
        out.println(number);
        return in.readLine();
    }
}