package fr.classcord.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import fr.classcord.network.ClientInvite;

public class LoginFrame extends JFrame {

    private JTextField ipField = new JTextField("");
    private JTextField portField = new JTextField("");
    private JTextField usernameField = new JTextField("");
    private JPasswordField passwordField = new JPasswordField("");
    private JButton loginButton = new JButton("Se connecter");
    private JButton registerButton = new JButton("S'inscrire");
    private JButton guestButton = new JButton("Se connecter en tant qu'invité");


    private JLabel statusLabel = new JLabel(" ");

    private ClientInvite client;
    private boolean listenerAdded = false; // Pour ne pas ajouter plusieurs fois

    public LoginFrame(ClientInvite client) {
    super("Connexion");

    this.client = client;

    setSize(300, 250);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;

    // Ligne IP
    add(new JLabel("IP serveur :"), gbc);
    gbc.gridx = 1;
    add(ipField, gbc);

    // Ligne Port
    gbc.gridy++;
    gbc.gridx = 0;
    add(new JLabel("Port :"), gbc);
    gbc.gridx = 1;
    add(portField, gbc);

    // Ligne nom d'utilisateur
    gbc.gridy++;
    gbc.gridx = 0;
    add(new JLabel("Nom d'utilisateur :"), gbc);
    gbc.gridx = 1;
    add(usernameField, gbc);

    // Ligne mot de passe
    gbc.gridy++;
    gbc.gridx = 0;
    add(new JLabel("Mot de passe :"), gbc);
    gbc.gridx = 1;
    add(passwordField, gbc);

    // Boutons login/register
    gbc.gridy++;
    gbc.gridx = 0;
    add(loginButton, gbc);
    gbc.gridx = 1;
    add(registerButton, gbc);

    // Bouton invité centré
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    add(guestButton, gbc);
    gbc.anchor = GridBagConstraints.WEST; // reset pour la suite
    gbc.gridwidth = 1;

    // Label de statut
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    add(statusLabel, gbc);

    loginButton.addActionListener(new LoginListener());
    registerButton.addActionListener(e -> new RegisterFrame(client));
    guestButton.addActionListener(e -> new GuestConnectFrame());

    setVisible(true);
}

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleConnection("login");
        }
    }

    private void handleConnection(String actionType) {
        String ip = ipField.getText().trim();
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException ex) {
            statusLabel.setText("Port invalide");
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs");
            return;
        }

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        statusLabel.setText("Connexion en cours...");

        new Thread(() -> {
            try {
                client.connect(ip, port);

                // Ajoute le listener une seule fois
                if (!listenerAdded) {
                    client.addMessageListener(message -> {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                JSONObject response = new JSONObject(message);
                                String type = response.optString("type");
                                String status = response.optString("status");

                                if (status.equals("ok") && type.equals("login")) {
                                    new ChatFrame(client, username);
                                    dispose();
                                } else if (type.equals("error")) {
                                    statusLabel.setText("Erreur : " + response.optString("message", "Inconnue"));
                                    loginButton.setEnabled(true);
                                    registerButton.setEnabled(true);
                                }
                            } catch (Exception ex) {
                                statusLabel.setText("Erreur JSON");
                                loginButton.setEnabled(true);
                                registerButton.setEnabled(true);
                            }
                        });
                    });
                    listenerAdded = true;
                }

                JSONObject json = new JSONObject();
                json.put("type", actionType);
                json.put("username", username);
                json.put("password", password);
                client.send(json.toString());

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Erreur de connexion : " + ex.getMessage());
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                });
            }
        }).start();
    }
}
