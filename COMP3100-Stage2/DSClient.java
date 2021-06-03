/*
 * COMP3100: Assignment - Stage 1
 *
 * Authors: Francisco Butturini, Nathan Shaheen, Nipun Shrestha
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;
public class DSClient {
    static Map<Integer,Integer> recent_server = new HashMap<>();
    public static void main(String[] args) {
        Socket s = null;
        
        try {
            // Open a port to the server and input/output streams
            int serverPort = 50000;
            s = new Socket("localhost", serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            // Setup a connection following the defined protocol
            handshake(in, out);

            if (((String)in.readLine()).equals("OK")) {
                // Preform scheduling
                String msg = " ";
                while (!msg.equals("NONE")) {

                    out.write(("REDY\n").getBytes());   // Ready for next job

                    // Get the job
                    String job = in.readLine();
                    // System.out.println(job);
                    String[] job_info = job.split(" ",0);

                    while (job_info[0].equals("JCPL")) {    // Disregard JCPL commands
                        out.write(("REDY\n".getBytes()));
                        job = in.readLine();
                        job_info = job.split(" ",0);
                    }

                    if (job.equals("NONE")) {
                        break;
                    }
                     // get server available  
                     String server_avail = getServerAvail(in, out, job_info);

                    String server_capable = "";
                    if (server_avail.equals(".")) {
                        //get server capable 
                        server_capable = getServerCapable(in, out, job_info);
                        String[] server_info = server_capable.split(" ",0);
                        String job_schedule = "SCHD" + " " + job_info[2] + " " + server_info[0] + " " + server_info[1] + "\n";
                        out.write(job_schedule.getBytes());

                    }else{
                            String[] avail_server = server_avail.split(" ", 0);
                            String job_schedule = "SCHD" + " " + job_info[2] + " " + avail_server[0] + " " + avail_server[1] + "\n";
                            out.write(job_schedule.getBytes());    
                    }
                    
                    msg = in.readLine();
                }
            }

            disconnect(s, in, out);
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Performs the initial handshake
     *
     * din: An input stream to read from
     * dout: An output stream to write to
     */
    public static void handshake(BufferedReader din, DataOutputStream dout) {
        String user = System.getProperty("user.name");
        try {
            dout.write(("HELO\n").getBytes());


            String str = din.readLine();
            if (str.equals("OK"))
                dout.write(("AUTH " + user + "\n").getBytes());
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Gets a servers that is immediatly available to process the job
     *
     * in: An input stream to read from
     * out: An output stream to write to
     * job_info : Array containing all the job information
     *
     * returns: An a string containing the server information 
     */

     public static String getServerAvail(BufferedReader in, DataOutputStream out , String[] job_info){
        int count = 0;
        String server = "GETS Avail" + " " + job_info[4] + " " + job_info[5]+ " " + job_info[6] + "\n";
        String server_avail = ".";
        try {
            out.write(server.getBytes());

            String data = in.readLine();
            // System.out.println(data);
            out.write(("OK\n").getBytes());

            String[] data_info = data.split(" ",0);
            // System.out.println(data_info[1]);

            if(Integer.parseInt(data_info[1]) == 0)  {
                in.readLine();
                return server_avail;
            }
            
            for ( int i = 0 ; i< Integer.parseInt(data_info[1]); i++){
                if(count == 0 ){
                    server_avail = in.readLine();
                }else{
                    String server_not = in.readLine();
                }
                count++;
            }
            out.write(("OK\n").getBytes());
            in.readLine();

        } catch (Exception e) {
            System.out.print(e);
        }
       
        return server_avail;

     } 


    /* Disconnect from the socket
     *
     * socket: A socket to disconnect from
     * din: An input stream to read from
     * dout: An output stream to write to
     */
    public static void disconnect(Socket socket, BufferedReader din, DataOutputStream dout) {
        try {
            dout.write(("QUIT\n").getBytes());

            if (((String)din.readLine()).equals("QUIT"))
                socket.close();  // Close the connection
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }
     /* Gets a servers that is capable of processing the current job
     *
     * in: An input stream to read from
     * out: An output stream to write to
     * job_info : Array containing all the job information
     *
     * returns: An a string containing the server information 
     */

    public static String getServerCapable(BufferedReader in, DataOutputStream out , String[] job_info){
            String server_capabale = "";
            String server = "";
            try {
                List<String> servers_capable = new ArrayList<>();
                String server_capable_send = "GETS Capable" + " " + job_info[4] + " " + job_info[5]+ " " + job_info[6] + "\n";
                out.write(server_capable_send.getBytes());        
                String data = in.readLine();
                out.write(("OK\n").getBytes());
                String[] data_info = data.split(" ",0);
                // System.out.println(data_info[1]);
                server_capabale = "";
                for ( int i = 0 ; i< Integer.parseInt(data_info[1]); i++){
                    server_capabale = in.readLine();
                    servers_capable.add(server_capabale);
                }
                out.write(("OK\n").getBytes());
                in.readLine();

                server = "";
                if(recent_server.containsKey(Integer.parseInt(job_info[4]))){
                        int index = recent_server.get(Integer.parseInt(job_info[4]));
                        
                        if((index + 1) >= servers_capable.size()){
                            server = servers_capable.get(index);
                            index=0;
                            
                        }else{
                            index++;
                            server = servers_capable.get(index);
                        }
                        recent_server.put(Integer.parseInt(job_info[4]), index);
                }else{
                        int index = 0;
                        server = servers_capable.get(index);
                        recent_server.put(Integer.parseInt(job_info[4]), index);
                        
                }
               
            } catch (Exception e) {
                //TODO: handle exception
            }

            return server;

    }

}
