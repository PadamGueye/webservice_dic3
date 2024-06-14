import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

public class ChatUserImpl {
    private String title = "Chatroom : plateforme de discussion en ligne";
    private String pseudo = null;
    private XmlRpcClient server = null;
    private JFrame window = new JFrame();
    private JTextArea txtOutput = new JTextArea();
    private JTextField txtMessage = new JTextField();
    private JButton btnSend = new JButton("Envoyer");
    private JButton btnUnsubscribe = new JButton("Se désabonner");

    public ChatUserImpl() {
        connectToServer();
        requestPseudo();
        createIHM();
    }

    private void connectToServer() {
        try {
            this.server = new XmlRpcClient("http://localhost/RPC2");
        } catch (Exception exception) {
            System.err.println("Client: " + exception);
        }
    }

    private void createIHM() {
        window.setTitle(title + " - " + this.pseudo);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 500);
        window.setLayout(new BorderLayout());
        window.setLocationRelativeTo(null);

        txtOutput.setEditable(false);
        txtOutput.setBackground(new Color(245, 245, 245));
        txtOutput.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(txtOutput);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        window.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMessage.setMargin(new Insets(5, 5, 5, 5));
        inputPanel.add(txtMessage, BorderLayout.CENTER);

        btnSend.setFont(new Font("Arial", Font.BOLD, 14));
        btnSend.setBackground(new Color(70, 130, 180));
        btnSend.setForeground(Color.WHITE);
        inputPanel.add(btnSend, BorderLayout.EAST);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        btnUnsubscribe.setFont(new Font("Arial", Font.BOLD, 14));
        btnUnsubscribe.setBackground(new Color(255, 78, 15));
        btnUnsubscribe.setForeground(Color.WHITE);
        southPanel.add(btnUnsubscribe, BorderLayout.SOUTH);

        window.add(southPanel, BorderLayout.SOUTH);

        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        btnUnsubscribe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unsubscribe();
            }
        });

        txtMessage.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                if (event.getKeyChar() == '\n') {
                    sendMessage();
                }
            }
        });

        window.setVisible(true);
        txtMessage.requestFocus();
    }

    private void requestPseudo() {
        this.pseudo = JOptionPane.showInputDialog(
                null, "Entrez votre pseudo : ",
                this.title, JOptionPane.PLAIN_MESSAGE
        );
        if (this.pseudo == null) System.exit(0);

        Vector<String> inscrire = new Vector<>();
        inscrire.add(this.pseudo);

        try {
            boolean inscrireOk = (boolean) this.server.execute("sample.subscribe", inscrire);
            if (!inscrireOk) {
                JOptionPane.showMessageDialog(null, "Pseudo existe deja! Veuillez choisir un autre");
                requestPseudo();
            }
        } catch (XmlRpcException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleWindowClosing() {
        unsubscribe();
        System.exit(0);
    }

    private void sendMessage() {
        Vector<String> message = new Vector<>();
        message.add(this.pseudo);
        message.add(this.txtMessage.getText());

        try {
            this.server.execute("sample.postMessage", message);
        } catch (XmlRpcException | IOException e) {
            e.printStackTrace();
        }

        this.txtMessage.setText("");
        this.txtMessage.requestFocus();
    }

    private void unsubscribe() {
        Vector<String> deconnect = new Vector<>();
        deconnect.add(this.pseudo);

        try {
            boolean deconnectOk = (boolean) this.server.execute("sample.unsubscribe", deconnect);
            if (deconnectOk) {
                JOptionPane.showMessageDialog(window, "Vous avez été désabonné avec succès.");
                System.exit(0);
            }
        } catch (XmlRpcException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatUserImpl chatUserImpl = new ChatUserImpl();
        boolean received;
        String msg;
        String last_msg = "";
        boolean is_first = true;

        while (true) {
            try {
                msg = (String) chatUserImpl.server.execute("sample.getMessage", new Vector<>());
                received = true;
                if (!is_first && !last_msg.equals(msg)) {
                    chatUserImpl.txtOutput.append(msg + "\n");
                    last_msg = msg;
                } else if (is_first) {
                    chatUserImpl.txtOutput.append(msg + "\n");
                    last_msg = msg;
                    is_first = false;
                }
            } catch (Exception e) {
                received = false;
            }
        }
    }
}
