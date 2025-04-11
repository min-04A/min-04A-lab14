import java.util.*;
import java.net.*;
import java.time.LocalDateTime;
import java.io.*;

public class Server 
{
    private ServerSocket serverSocket;
    private List<LocalDateTime> connectionTimes = new ArrayList<>();

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
}
