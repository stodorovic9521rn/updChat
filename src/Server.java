import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends JFrame {

    private JTextPane textPane;
    private JPanel panel;

    private DatagramSocket datagramSocket;
    private byte[] bafer = new byte[256];


    public Server(DatagramSocket datagramSocket){
        this.datagramSocket = datagramSocket;
        initialise();
    }

    private void initialise(){
        setTitle("UDP chat - Server");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(420, 420);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(new Color(232, 189, 184));
        this.getContentPane().add(panel);

        ImageIcon image = new ImageIcon("ikonica.png");
        this.setIconImage(image.getImage());

        JLabel serverPoruka = new JLabel("Datagram paketi od klijenta.");
        panel.add(serverPoruka, BorderLayout.NORTH);

        textPane = new JTextPane();
        textPane.setBackground(new Color(232, 189, 184));
        textPane.setForeground(Color.BLACK);
        textPane.setPreferredSize(new Dimension(420,420));
        textPane.setVisible(true);

        panel.add(textPane, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public void primi(){
        while(true){
            try{
                DatagramPacket datagramPacket = new DatagramPacket(bafer, bafer.length);
                datagramSocket.receive(datagramPacket);
                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                String poruka = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                textPane.setText(textPane.getText().concat("\nPoruka od klijenta: " + poruka));
                datagramPacket = new DatagramPacket(bafer, bafer.length, inetAddress, port);
                datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(1234);
        Server server = new Server(datagramSocket);
        server.primi();
    }
}
