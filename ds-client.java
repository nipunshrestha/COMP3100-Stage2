/*
 * COMP3100: Assignment - Stage 1
 *
 * Authors: Francisco Butturini, Nathan Shaheen, Nipun Shrestha
 *
 */

import java.io.*;
import java.net.*;

public class TCPtest {
    public static void main(String[] args) {
        Socket s = null;

        try {
            // Open a port to the server
            int serverPort = 50000; // TODO: What is the port number?
            s = new Socket("localhost", serverPort);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            // Setup a connection following the defined protocol
            dout.write(("HELO\n").getBytes());

            String str = (String) dis.readLine();

            if (str.equals("OK"))
                dout.write(("AUTH franciscoB\n").getBytes());

            while (str.equals("REDY")) {
                // Handle job
            }
            dout.write(("QUIT\n").getBytes());
            s.close(); // Close the connection
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }
}
