/*
 * COMP3100: Assignment - Stage 1
 *
 * Authors: Francisco Butturini, Nathan Shaheen, Nipun Shrestha
 *
 */

import java.io.*;
import java.net.*;
import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


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
            File file = new File("./ds-system.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("server");

            String[][] servers = getServerList(nList);

            // Finding the largest Server
            int max_core_count = Integer.MIN_VALUE;
            String server_max = "";
            String server_id = "";

            for(int i = 0; i< servers.length; i++){
                if(Integer.parseInt(servers[i][1])> max_core_count){
                    max_core_count = Integer.parseInt(servers[i][1]);
                    server_max = servers[i][0];
                    server_id = "0";
                }
            }

            if (((String)in.readLine()).equals("OK")) {
                out.write(("REDY\n").getBytes());
                String msg = "";
                while (!msg.equals("NONE")) {
                    System.out.println("Stuck before readline");
                    String job= in.readLine();
                    System.out.println("Stuck before jobLength");
                    if(job.length()>4)
                    System.out.println("Stuck before jobSubstring");
                    while(job.substring(0,4).equals("JCPL")){
                        System.out.println("Stuck before REDY in JCPL loop");
                        out.write(("REDY\n".getBytes()));
                        System.out.println("Stuck before Readline in JCPL loop");
                        job= in.readLine();
                    }
                    System.out.println("Stuck before job.equals NONE in main loop");
                    if(job.equals("NONE")){
                        break;
                    }
                    // Parse incomming String (Core count, RAM, Disk Space)
                    System.out.println("Stuck before JobSplit");
                    String[] job_info = job.split(" ",0);

                    // Create String ("SCH...") and send to DS-SERVER
                    System.out.println("Stuck before JobSchedule");
                    String job_schedule = "SCHD" + " " + job_info[2] + " " + server_max + " " + server_id + "\n";
                    System.out.println("Stuck before Writing job SCHD");
                    out.write(job_schedule.getBytes());


                    // Send "REDY" to get next job
                    System.out.println("Stuck before REDY in main loop");
                    out.write(("REDY\n".getBytes()));
                    System.out.println("Stuck before readline in main loop");
                    msg= in.readLine();

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
        String user = System.getProperty("user.name");
        try {
            dout.write(("HELO\n").getBytes());

            String str = dis.readLine();
            if (str.equals("OK"))
                dout.write(("AUTH "+user+"\n").getBytes());
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }
    public static String[][] getServerList(NodeList nList){
        String[][] xml = new String[nList.getLength()][2];
            for(int i =0 ; i< nList.getLength(); i++){
                Node nNode = nList.item(i);
                if(nNode.getNodeType()== Node.ELEMENT_NODE){
                    Element eElement = (Element) nNode; 
                    xml[i][0]=eElement.getAttribute("type");
                    xml[i][1]= eElement.getAttribute("coreCount").toString();
                }
            }


        return xml;
    }
}
