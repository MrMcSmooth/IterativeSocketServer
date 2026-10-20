import java.io.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
       ServerSocket serverSocket = new ServerSocket(2220);
       ConnectionCounter counter = new ConnectionCounter();
       while(true){
           Socket socket = null;
           try{
               socket = serverSocket.accept();
               System.out.println("Client is connected");
               counter.clientConnect();

               BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               OutputStream output = socket.getOutputStream();
               PrintWriter writer = new PrintWriter(output, true);

               String clientRequest = reader.readLine();

               switch (clientRequest){
               //DateSender
                   case "1":
                       writer.println(new Date().toString());
                       break;

               //UptimeTracker
                   case "2":
                       RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                       long upTime = runtime.getUptime();
                       writer.println(upTime);
                       break;

               //Memory Usage
                   case "3":
                       OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                       long totalMemory = os.getTotalMemorySize();
                       long freeMemory = os.getFreeMemorySize();
                       long usedMemory = totalMemory - freeMemory;
                       writer.println("The servers total memory: " + totalMemory);
                       writer.println("The servers used memory: " + usedMemory);
                       break;

               //netstat
                   case "4":
                       writer.println("The total connections to the server: " + counter.getActive());
                       break;

               //Total Users
                   case "5":
                       Process clientProcess = new ProcessBuilder("who").start();
                       BufferedReader clientreader = new BufferedReader(new InputStreamReader(clientProcess.getInputStream()));
                       String user;
                       while ((user = clientreader.readLine()) != null) {
                           writer.println(user);
                       }
                       clientreader.close();
                       break;

               //Running Processes
                   case "6":
                   /* Linux Way
                   Process process = Runtime.getRuntime().exec("ps -e");
                   BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    */

                   /*Windows
                   Process process = Runtime.getRuntime().exec(System.getenv("windr") + "\\System32\\" + "tasklist.exe");
                   BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    */
                       break;
               }

           } catch (IOException e) {
               e.printStackTrace();
           }finally{
               if(socket != null){
                   counter.clientDisconect();
                   try{
                       socket.close();
                   }catch(IOException e){
                       e.printStackTrace();
                   }
               }
           }

       }
    }
}