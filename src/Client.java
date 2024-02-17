import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends JFrame implements Runnable {

    private String nom;
    private Socket s;
    private DataOutputStream dout;
    private DataInputStream din;
    JTextField tf;
    JTextArea ta;



    public Client(String nom , String host, int port) {
        super(nom);
        tf = new JTextField();
        ta = new JTextArea();
        this.add(tf,BorderLayout.NORTH);
        this.add(ta,BorderLayout.CENTER);

        tf.addActionListener(e -> {
            try {
                dout.writeUTF(nom + " says : "+tf.getText());
                tf.setText("");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        try {
            s = new Socket(host,port);
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setVisible(true);
        setSize(300,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        new Thread(this).start();

    }

    @Override
    public void run() {
        while(true){
            try{
                String msg = din.readUTF();
                ta.append(msg+"\n");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Client("abdo1","127.0.0.1",9999);
        new Client("abdo2","127.0.0.1",9999);
    }
}
