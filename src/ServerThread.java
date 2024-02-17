import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket socket;
    private  Server server;
    private DataInputStream din;
    public ServerThread(Socket s, Server server) {
        this.socket = s;
        this.server = server;
    }

    @Override
    public void run(){
        try{
            din  = new DataInputStream(socket.getInputStream());
            while (true){
                String msg = din.readUTF();
//                System.out.println("Reception > "+msg + " -- Sender : "+socket);
                server.sendToAll(msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
            server.deleteConncetion(socket);
        }

    }
}
