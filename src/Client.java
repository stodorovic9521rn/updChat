import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client extends JFrame {

    private JButton button;
    private JTextPane textPane;
    private JTextField textField;

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] bafer;


    public Client(DatagramSocket datagramSocket, InetAddress inetAddress){
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
        initialise();
    }

    private void initialise(){
        setTitle("UDP chat - Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(420, 420);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(196, 194, 206));
        this.getContentPane().add(panel);

        ImageIcon image = new ImageIcon("ikonica.png");
        this.setIconImage(image.getImage());

        textPane = new JTextPane();
        textPane.setBackground(new Color(142, 140, 152));
        textPane.setPreferredSize(new Dimension(320,320));
        textPane.setVisible(true);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setMinimumSize(new Dimension(320,320));
        scrollPane.getViewport().getView().setBackground(new Color(142, 140, 152));
        scrollPane.getViewport().getView().setForeground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.NORTH);

        JPanel panel1 = new JPanel(new FlowLayout());
        panel1.setBackground(new Color(196, 194, 206));

        panel1.add(new Label("Klijent: "));

        textField = new JTextField("Poruka");
        textField.setBackground(Color.WHITE);
        textField.setPreferredSize(new Dimension(100, 25));
        textField.setVisible(true);

        panel1.add(textField);

        ImageIcon sendIcon = new ImageIcon("send.png");
        Image sendImage = sendIcon.getImage();
        Image newSendImage = sendImage.getScaledInstance(20,20, Image.SCALE_SMOOTH);
        sendIcon = new ImageIcon(newSendImage);

        button = new JButton();
        button.setToolTipText("Posalji");
        button.setIcon(sendIcon);
        button.setMargin(new Insets(0,0,0,0));
        button.setBackground(new Color(196, 194, 206));

        panel1.add(button);

        JLabel klijentovaPoruka = new JLabel("Posalji datagram pakete serveru.");
        panel1.add(klijentovaPoruka);

        panel.add(panel1, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public void posalji(){
        button.addActionListener(e -> {
            Scanner scanner = new Scanner(textField.getText());

            while(scanner.hasNext()){
                try{
                    String poruka = scanner.nextLine();
                    bafer = poruka.getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(bafer, bafer.length, inetAddress, 1234);
                    datagramSocket.send(datagramPacket);
                    datagramSocket.receive(datagramPacket);
                    String odServera = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    textPane.setText(textPane.getText().concat("\nServer kaze da si poslao: " + odServera));
                } catch (IOException e1) {
                    e1.printStackTrace();
                    break;
                }
            }
        });
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Client klijent = new Client(datagramSocket, inetAddress);
        klijent.posalji();
    }
}
