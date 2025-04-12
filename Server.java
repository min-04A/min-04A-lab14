import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.*;
import java.time.LocalDateTime;
import java.io.*;

public class Server 
{
    private ServerSocket serverSocket;
    private List<LocalDateTime> connectionTimes = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public Server(int port) throws Exception 
    {
        this.serverSocket = new ServerSocket(port);
    }

    public void serve(int expectedClients)
    {
        // Handle all the clients given
        for(int i=0; i<expectedClients; i++)
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> clientHandle(clientSocket));
            }

            // exit server if exception
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void clientHandle(Socket clientSocket)
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Check the passcode
            String passcode = in.readLine();
            if(!"12345".equals(passcode))
            {
                out.println("couldn't handshake");
                clientSocket.close();
                return;
            }

            // Connection Time
            connectionTimes.add(LocalDateTime.now());

            String input = in.readLine();
            int number;

            // multi-threads to control many clients
            try 
            {
                number = Integer.parseInt(input);

                int count = countFactors((int)number);
                out.println("The number " + number + " has " + count + " factors");
            } 
            
            catch (Exception e) 
            {
                out.println("There was an exception on the server");
            }

            finally 
            {
                clientSocket.close();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<LocalDateTime> getConnectedTimes() 
    {
        ArrayList<LocalDateTime> copy;
        synchronized (connectionTimes) 
        {
            copy = new ArrayList<>(connectionTimes);
            Collections.sort(copy);
        }
        return copy;
    }

    private int countFactors(int number) 
    {
        int count = 0;
        for (int i = 1; i <= number; i++) 
        {
            if (number % i == 0)
            {
                count++;
            }
        }
        return count;
    }

    public void disconnect() 
    {
        try 
        {
            serverSocket.close();
            threadPool.shutdownNow();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
