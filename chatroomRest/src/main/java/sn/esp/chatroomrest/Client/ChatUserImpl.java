package sn.esp.chatroomrest.Client;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import javax.swing.*;

public class ChatUserImpl  implements ChatUser {
    private String pseudo;
    private JFrame frame;
    private JTextArea txtOutput;
    private JTextField txtMessage;
    private ChatRoom chatRoom;

    public ChatUserImpl(String pseudo, ChatRoom chatRoom) {
        this.pseudo = pseudo;
        this.chatRoom = chatRoom;
        initGUI();
        chatRoom.subscribe( pseudo);
    }
    public ChatUserImpl(String pseudo)  {
        this.pseudo = pseudo;
        initGUI();
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

                    chatRoom.unsubscribe(pseudo);
            }
        });
    }

    @Override
    public String getPseudo()  {
        return this.pseudo;
    }

    @Override
    public void displayMessage(String message)  {
        txtOutput.append(message + "\n");
    }

    private void btnSend_actionPerformed(ActionEvent e) {

            chatRoom.postMessage(pseudo, txtMessage.getText());
            txtMessage.setText("");
            txtMessage.requestFocus();
    }

    private void btnDisconnect_actionPerformed(ActionEvent e) {

            chatRoom.unsubscribe(pseudo);
            frame.dispose();
    }

}
