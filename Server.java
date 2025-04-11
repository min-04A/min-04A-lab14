import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.*;
import java.time.LocalDateTime;
import java.io.*;

public class Server 
{
    private ServerSocket serverSocket;
    private List<LocalDateTime> connectionTimes = new ArrayList<>();
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public void serve(int expectedClients)
    {
        // Handle all the clients given
        for(int i=0; i<expectedClients; i++)
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Check the passcode
                String passcode = in.readLine();
                if(!"12345".equals(passcode))
                {
                    out.println("couldn't handshake");
                    clientSocket.close();
                    continue;
                }

                // Connection Time
                connectionTimes.add(LocalDateTime.now());

                // multi-threads to control many clients
                try 
                {
                    String input = in.readLine();
                    int number;
        
                    try 
                    {
                        number = Integer.parseInt(input);
                        int count = countFactors(number);
                        out.println("The number " + number + " has " + count + " factors");
                    } 
                    catch (Exception e) 
                    {
                        out.println("There was an exception on the server");
                    }

                    finally 
                    {
                        try 
                        {
                            clientSocket.close();
                        } 
                        catch (IOException e) 
                        {
                            e.printStackTrace();
                        }
                    }
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            // exit server if exception
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<LocalDateTime> getConnectionTimes() 
    {
        ArrayList<LocalDateTime> copy;
        synchronized (connectionTimes) 
        {
            copy = new ArrayList<>(connectionTimes);
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
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
