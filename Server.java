import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int portNo;

    public Server(int portNo){
        this.portNo=portNo;
    }

    private void processConnection() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);

        //*** Application Protocol *****
        String buffer = in.readLine();
        while(buffer != null && buffer.length() != 0){
            System.out.println(buffer);
            buffer = in.readLine();
            if(buffer.contains("GET"){
                request("docroot/home.html" , "text/html", out);
            }
        }

        //out.printf("HTTP/1.1 200 OK\n");
        //out.printf("Content-Length: 34\n");
        //out.printf("Content-Type: text/html\n\n");
       
        //out.printf("<h1>Welcome to web server</h1>");
        in.close();
        out.close();
    }

     void request(String filePath , String contentType , PrintWriter out) throws IOException{
        File file = new File(filePath);
        int content = (int) file.length();

        out.printf("HTTP/1.1 200 OK\n");
        out.printf("Content-Length: %d\n", contentLength);
        out.printf("Content-Type: %s\n\n", contentType);

        System.out.printf("HTTP/1.1 200 OK\n");
        System.out.printf("Content-Length: %d\n", contentLength);
        System.out.printf("Content-Type: %s\n\n", contentType);

        if(contentType.equals("image/png")){
            OutputStream out = (clientSocket.getOutputStream());
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = in.read(buffer)) != -1){
                out.write(buffer, 0 , bytesRead);
           }
        } else {
            BufferedReader br = new BufferedReader(new FileReader (file));
            String buffer = br.readLine();
            while(buffer != null){
                out.println(buffer);
                buffer = br.readLine();
            } 
        }
    }


    public void run() throws IOException{
        boolean running = true;
       
        serverSocket = new ServerSocket(portNo);
        System.out.printf("Listen on Port: %d\n",portNo);
        while(running){
            clientSocket = serverSocket.accept();
            //** Application Protocol
            processConnection();
            clientSocket.close();
        }
        serverSocket.close();
    }
    public static void main(String[] args0) throws IOException{
        Server server = new Server(8080);
        server.run();
    }
}
