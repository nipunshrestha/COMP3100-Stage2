/*
 * COMP3100: Assignment - Stage 1
 *
 * Authors: Francisco Butturini, Nathan Shaheen, Nipun Shrestha
 *
 */

import java.io.*;  
import java.net.*;

public class DSClient {  
    public static void main(String[] args) {
        Socket s = null;

        try {
            // Open a port to the server
            int serverPort = 50000;  // TODO: What is the port number?      
            s = new Socket("localhost", serverPort);  
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            // Setup a connection following the defined protocol
            handshake(in, out);
            
            // Read XML

            if (((String)in.readLine()).equals("OK")) {
                out.write(("REDY\n").getBytes());

                while (!((String)in.readLine()).equals("NONE")) {
                    // Parse incomming String (Core count, RAM, Disk Space)

                    // Create String ("SCH...") and send to DS-SERVER

                    // Send "REDY" to get next job
                }
            }

            out.write(("QUIT\n").getBytes());

            if (((String)in.readLine()).equals("QUIT"))
                s.close();  // Close the connection 
        }
        
        catch (Exception e) {
            System.out.println(e);
        }  
    }

    // Performs the initial handshake
    public static void handshake(BufferedReader dis, DataOutputStream dout) {
        try {
            dout.write(("HELO\n").getBytes());

            String str = dis.readLine();
            if (str.equals("OK"))
                dout.write(("AUTH Nathan\n").getBytes());
        }
        
        catch (Exception e) {
            System.out.println(e);
