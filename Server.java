import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.Scanner;
import java.util.ArrayList;

class BetterServer {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static BufferedReader in;
    
    
    private static PrintWriter out;
    private static String output = "";
    private static String eor = "[EOR]"; // a code for end-of-response
    
    // establishing a connection
    private static void setup() throws IOException {
        
        serverSocket = new ServerSocket(0);
        toConsole("Server port is " + serverSocket.getLocalPort());
        
        clientSocket = serverSocket.accept();

        // get the input stream and attach to a buffered reader
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        // get the output stream and attach to a printwriter
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        toConsole("Accepted connection from "
                 + clientSocket.getInetAddress() + " at port "
                 + clientSocket.getPort());
            
        sendGreeting();
    }
    
    // the initial message sent from server to client
    private static void sendGreeting()
    {
        appendOutput("Welcome to BetterServer!\n");
        appendOutput("Enter username:");
        sendOutput();
    }
    
    // what happens while client and server are connected
    private static void talk() throws IOException {
        /* placing echo functionality into a separate private method allows it to be easily swapped for a different behaviour */
        echoClient();
        disconnect();
    }
    
    private static void echoClient() throws IOException
    {
        System.out.println("Username requested");

       String username ;
       String password;
       String command; 
       String input;
       
       // Scanner inp = new Scanner(System.inp);
       // ArrayList<String> inputs = new ArrayList<String>();
       int count = 0;

       Stack<String> stack = new Stack<String>(); 
        while ((username = in.readLine())  != null && count <= 3 ) {
            

            if (username.equals("Dougal")){
                appendOutput("Enter password:");
                sendOutput();
                password = in.readLine();

                if (password.equals("moo")){

                    appendOutput("Welcome Dougal!");
                    appendOutput("Enter Commands(push x / pop / print)");
                    sendOutput();
                    
                    // inputs.add(inp.nextLine());

                    while((input = in.readLine())  != null ){

                    if(input.split("")[0] == "push"){
                        appendOutput("the push is working");
                        sendOutput();
                    }
                    else if(input.equals("pop")){
                        
                        System.out.println("The previous stack was:" + stack);

                        appendOutput("Item removed from stack:" + stack.pop());
                        sendOutput();


                    }
                    else if(input.equals("print")){
                        appendOutput("stack contains:" + stack);
                        sendOutput();

                      System.out.println("print");  
                    }
                        
                    
                    else{
                        appendOutput(input.split(" ")[1] + " " + "added to stack");
                        sendOutput();

                        
                        System.out.println("push:" + "" + input.split(" ")[1]);

                        stack.push(input.split(" ")[1]);
                        System.out.println(stack);
                    }

                    
                }


                }
                else {
                    appendOutput("password is wrong");
                    sendOutput();
                }

                System.out.println("password requested");
            }
            else{
                appendOutput("Username not recognised");
                appendOutput("Enter username1");
                sendOutput();

                 System.out.println("Name entered:" + username);
                 System.out.println("Username not recognised");

                 
                 count = count + 1 ;
            }


    }

             
           
}

   
    
    private static void disconnect() throws IOException {
        out.close(); 
        toConsole("Disconnected.");
        System.exit(0);
    }
    
    // add a line to the next message to be sent to the client
    private static void appendOutput(String line) {
        output += line + "\r";
    }
    
    // send next message to client
    private static void sendOutput() {
        out.println( output + "[EOR]");
        out.flush();
        output = "";
    }
    
    // because it makes life easier!
    private static void toConsole(String message) {
        System.out.println(message);
    }
    
    public static void main(String[] args) {
        try {
            setup();
            talk();
        }
        catch( IOException ioex ) {
            toConsole("Error: " + ioex );
        }
    }
}