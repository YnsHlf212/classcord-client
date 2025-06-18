package fr.classcord.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import fr.classcord.model.Message;
import fr.classcord.network.ClientInvite;


public class Messagerie extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
    private JLabel lblConnexion = new JLabel();
    private boolean flagconnected = false;
    private ClientInvite clientInvite = new ClientInvite();
	private JTextField inputField;
	private JPanel panelMessages;
	private JScrollPane scrollPane;
	private JButton btnEnvoyer;
	private boolean flagPseudo = false;
	

    private void btnConnection_click() {
    btnEnvoyer.setEnabled(true); // active bouton envoyer
    String ip = textField.getText();
    int port = Integer.parseInt(textField_1.getText());
	consoleMessage("> Entre ton pseudo :");
    new Thread(() -> {
        clientInvite.setMessagerie(this);  // Pour interagir avec l'interface
        clientInvite.connect(ip, port);
    	}).start();
	}
	
	public Messagerie() {
		setTitle("Messagerie");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblStartAServer = new JLabel("IP du serveur :");
		lblStartAServer.setBounds(31, 21, 88, 14);
		contentPane.add(lblStartAServer);

		textField = new JTextField("10.0.108.56");
		textField.setBounds(119, 18, 105, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblPortDuServeur = new JLabel("Port du serveur :");
		lblPortDuServeur.setBounds(246, 21, 127, 14);
		contentPane.add(lblPortDuServeur);

		textField_1 = new JTextField("12345");
		textField_1.setColumns(10);
		textField_1.setBounds(356, 18, 69, 20);
		contentPane.add(textField_1);

		JLabel lblConnexion = new JLabel("Connecté au serveur");
		lblConnexion.setBounds(448, 11, 170, 30);
		lblConnexion.setForeground(Color.GREEN);
		lblConnexion.setFont(new Font("Arial", Font.BOLD, 16));
		lblConnexion.setVisible(false);
		contentPane.add(lblConnexion);

		JButton btnConnection = new JButton("Connection");
		btnConnection.setBounds(653, 15, 120, 26);
		btnConnection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnConnection_click();
			}
		});
		contentPane.add(btnConnection);

		// Panneau des messages
		panelMessages = new JPanel();
		panelMessages.setLayout(new BoxLayout(panelMessages, BoxLayout.Y_AXIS));
		panelMessages.setBackground(Color.WHITE);

		// Scroll pane contenant les messages
		scrollPane = new JScrollPane(panelMessages);
		scrollPane.setBounds(31, 55, 715, 400);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);

		// Champ de saisie de message
		inputField = new JTextField();
		inputField.setBounds(31, 470, 600, 35);
		contentPane.add(inputField);
		

		

		// Bouton d'envoi
		btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.setBounds(640, 470, 106, 35);
		btnEnvoyer.setEnabled(false); // Désactivé au départ
		contentPane.add(btnEnvoyer);
		
		
		// Envoi message via Entrée ou Bouton Envoyer
		inputField.addActionListener(e -> {
	if (!flagPseudo) {
		clientInvite.setPseudo(inputField.getText().trim());
		consoleMessage("> Tu es connecté avec " + clientInvite.getPseudo() + " comme pseudo !");
		flagPseudo = true;
		System.err.println("Pseudo entré : " + clientInvite.getPseudo());
	} else {
		envoyerMessage();
	}
	});				
    btnEnvoyer.addActionListener(e -> {
	if (!flagPseudo) {
		clientInvite.setPseudo(inputField.getText().trim());
		consoleMessage("> Tu es connecté avec " + clientInvite.getPseudo() + " comme pseudo !");
		flagPseudo = true;
		System.err.println("Pseudo entré : " + clientInvite.getPseudo());
	} else {
		envoyerMessage();
	}
	});
		
    }
	
	// Envoi du message et affichage dans le chat
	private void envoyerMessage() {
	String message = inputField.getText().trim();
	if (!message.isEmpty()) {
		JLabel messageLabel = new JLabel(clientInvite.getPseudo() + " : " + message);
		messageLabel.setFont(new Font("Monospaced", Font.PLAIN, 14)); // police console
		messageLabel.setForeground(Color.BLACK); // texte noir
		messageLabel.setOpaque(true); // nécessaire pour afficher le fond
		messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		panelMessages.add(messageLabel);
		panelMessages.revalidate();
		panelMessages.repaint();
	}
}

// Envoi du message et affichage dans le chat
public void envoyerMessage(String message) {
	if (!message.isEmpty()) {
		JLabel messageLabel = new JLabel(clientInvite.getPseudo() + " : " + message);
		messageLabel.setFont(new Font("Monospaced", Font.PLAIN, 14)); // police console
		messageLabel.setForeground(Color.BLACK); // texte noir
		messageLabel.setOpaque(true); // nécessaire pour afficher le fond
		messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		panelMessages.add(messageLabel);
		panelMessages.revalidate();
		panelMessages.repaint();
		
	}
}

// Envoi d'un message de type Message et affichage dans le chat
public void envoyerMessage(Message message) {
    if (message.getFrom() != null && message.getContent() != null) {
        JLabel messageLabel = new JLabel(message.getFrom() + " : " + message.getContent());
        messageLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        messageLabel.setForeground(Color.BLACK); 
        messageLabel.setOpaque(true);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelMessages.add(messageLabel);
        panelMessages.revalidate();
        panelMessages.repaint();
    }
}
	// Affichage d'un message dans la console
	public void consoleMessage(String message) {
		if (!message.isEmpty()) {
			JLabel messageLabel = new JLabel( message);
			messageLabel.setFont(new Font("Monospaced", Font.PLAIN, 14)); // police console
			messageLabel.setForeground(Color.GREEN); // texte vert
			messageLabel.setOpaque(true); // nécessaire pour afficher le fond
			messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
			panelMessages.add(messageLabel);
			panelMessages.revalidate();
			panelMessages.repaint();
		}
	}


    public String getTextfield() {
        return textField.getText();
    }

	public int getTextfield_1() {
		return Integer.parseInt(textField_1.getText());
	}

    public boolean getFlagconnected() {
		return this.flagconnected;
	}

	public void setFlagconnected(boolean flagconnected) {
		this.flagconnected = flagconnected;
	}
}
