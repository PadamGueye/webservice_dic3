import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.*;

public class ChatUserImpl extends UnicastRemoteObject implements ChatUser {
    private String pseudo;
    private JFrame frame;
    private JTextArea txtOutput;
    private JTextField txtMessage;
    private ChatRoom chatRoom;

    public ChatUserImpl(String pseudo, ChatRoom chatRoom) throws RemoteException {
        this.pseudo = pseudo;
        this.chatRoom = chatRoom;
        initGUI();
        chatRoom.subscribe(this, pseudo);
    }

    private void initGUI() {
        frame = new JFrame("Interface de discussion de " + pseudo);
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtMessage = new JTextField();
        JButton btnSend = new JButton("Send");
        JButton btnDisconnect = new JButton("Disconnect");

        btnSend.addActionListener(e -> btnSend_actionPerformed(e));
        btnDisconnect.addActionListener(e -> btnDisconnect_actionPerformed(e));

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(txtOutput), BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(txtMessage, BorderLayout.CENTER);
        panel.add(btnSend, BorderLayout.EAST);
        panel.add(btnDisconnect, BorderLayout.WEST);

        frame.add(panel, BorderLayout.SOUTH);

        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    chatRoom.unsubscribe(pseudo);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        });
    }

    @Override
    public String getPseudo() throws RemoteException {
        return this.pseudo;
    }

    @Override
    public void displayMessage(String message) throws RemoteException {
        txtOutput.append(message + "\n");
    }

    private void btnSend_actionPerformed(ActionEvent e) {
        try {
            chatRoom.postMessage(pseudo, txtMessage.getText());
            txtMessage.setText("");
            txtMessage.requestFocus();
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }

    private void btnDisconnect_actionPerformed(ActionEvent e) {
        try {
            chatRoom.unsubscribe(pseudo);
            frame.dispose();
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String pseudo = JOptionPane.showInputDialog("Enter your pseudo:");
            ChatRoom chatRoom = (ChatRoom) Naming.lookup("rmi://localhost:2000/room");
            new ChatUserImpl(pseudo, chatRoom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
