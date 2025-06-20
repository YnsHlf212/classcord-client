package fr.classcord.ui;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import fr.classcord.network.ClientInvite;

public class GuestConnectFrame extends JFrame {

    private JTextField ipField;
    private JTextField portField;
    private ClientInvite client = new ClientInvite();

    public GuestConnectFrame() {
        setTitle("Connexion Invité");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("IP du serveur:"));
        ipField = new JTextField("");
        add(ipField);

        add(new JLabel("Port:"));
        portField = new JTextField("");
        add(portField);

        JButton connectButton = new JButton("Connexion");
        connectButton.addActionListener(e -> connectAsGuest());
        add(new JLabel()); // vide pour espacement
        add(connectButton);

        setVisible(true);
    }

    private void connectAsGuest() {
        String ip = ipField.getText().trim();
        int port;

        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Port invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String guestName = "Invité" + new Random().nextInt(10000);

        try {
             client.connect(ip, port);
            
            new ChatFrame(client, guestName);
            dispose(); // ferme la fenêtre de connexion
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Impossible de se connecter au serveur", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}