/*
 * COMP3100: Assignment - Stage 1
 *
 * Authors: Francisco Butturini, Nathan Shaheen, Nipun Shrestha
 *
 */


import java.io.*;  
import java.net.*;

public class TCPTest {  
    public static void main(String[] args) {
        Socket s = null;

        try {
            // Open a port to the server
            int serverPort = 5000;  // TODO: What is the port number?      
            s = new Socket(args[1], serverPort);  
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            // Setup a connection following the defined protocol
            dout.writeUTF("HELO");

            String str = (String)dis.readUTF();
            if (str.equals("OK"))
                    dout.writeUTF("AUTH xxx");  // TODO: How do you get auth number?

            if (str.equals("OK")) {
                // TODO: Read ds-system.xml

                // Begin job scheduling
                while (true) {
                    dout.writeUTF("REDY");

                    // TODO: Job scheduling

                    break;  // Remove (for testing purposes)

                }
            }

            s.close();  // Close the connection 
        }
        
        catch (Exception e) {
            System.out.println(e);
        }  
    }  
}
