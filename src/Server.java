import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class Server {
    Hashtable<Socket, DataOutputStream> clients  = new Hashtable<>();

    public Server(int port) {
        listen(port);
    }

    public void listen (int port){
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server Listening : "+ss);
            while (true){
                Socket s = ss.accept();
//                System.out.println("Connection for : "+s);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                clients.put(s,dout);
                new ServerThread(s,this).start();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void sendToAll(String msg){
        try {
            synchronized (clients){
                for(Enumeration  e = clients.elements() ; e.hasMoreElements();){
                    DataOutputStream dout  = (DataOutputStream) e.nextElement();
                    dout.writeUTF(msg);
                    System.out.println("Display : "+msg);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteConncetion(Socket s){
        try{
            synchronized (clients){
                System.out.println("Deconnection : "+s);
                clients.remove(s);
            }
        }catch (Exception e){
            System.out.println("Error "+e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server(9999);
    }

}
