package fr.classcord.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

import fr.classcord.model.Message;
import fr.classcord.ui.Messagerie;

public class ClientInvite {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String pseudo;
    private Messagerie messagerie;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connecté au serveur " + ip + ":" + port);
           

            // Thread de réception des messages
Thread receiveThread = new Thread(() -> {
    try {
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println("Message reçu : " + line);
            if (messagerie != null) {
                messagerie.envoyerMessage(Message.fromJson(line)); // Affiche dans l'interface
            }
        }
    } catch (IOException e) {
        System.out.println("Déconnexion du serveur.");
        if (messagerie != null) {
            messagerie.consoleMessage("> Déconnecté du serveur.");
        }
    }
});
receiveThread.start();

        } catch (IOException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public void send(String messageText) {
        JSONObject message = new JSONObject();
        message.put("type", "message");
        message.put("subtype", "global");
        message.put("to", "global");
        message.put("from", pseudo);
        message.put("content", messageText);

        out.println(message.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Messagerie messagerie = new Messagerie();
            messagerie.setVisible(true);
            messagerie.setAlwaysOnTop(true);
            messagerie.toFront();
            messagerie.requestFocus();
            messagerie.setAlwaysOnTop(false);
        });
    }   

    public void StartConnexion(ClientInvite client,String ip, int port) {
        client.connect(ip, port);

    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setMessagerie(Messagerie messagerie) {
        this.messagerie = messagerie;
    }
}
