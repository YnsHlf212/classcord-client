package fr.classcord.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import fr.classcord.model.User;
import fr.classcord.network.ClientInvite;

public class ChatFrame extends JFrame {
    private final String username;
    private final JTextField messageField;
    private final JButton sendButton;
    private final JPanel messagePanel;
    private final JScrollPane scrollPane;
    private final ClientInvite client;

    private Set<String> onlineUsers = new HashSet<>();
    private DefaultListModel<User> userListModel = new DefaultListModel<>();
    private JList<User> userList = new JList<>(userListModel);
    private Map<String, String> userStatuses = new HashMap<>();

    private JLabel conversationLabel;
    private JComboBox<String> statusComboBox;

    public ChatFrame(ClientInvite client, String username) {
        this.client = client;
        this.username = username;

        setTitle("Classcord - Connecté en tant que " + username);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(messagePanel);
        add(scrollPane, BorderLayout.CENTER);

        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setCellRenderer(new UserCellRenderer());
        JScrollPane userListScroll = new JScrollPane(userList);
        userListScroll.setPreferredSize(new Dimension(200, 0));
        add(userListScroll, BorderLayout.EAST);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Envoyer");
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        conversationLabel = new JLabel("Discussion : Global");
        conversationLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 50));
        add(conversationLabel, BorderLayout.NORTH);

        String[] statuses = {"online", "away", "dnd", "invisible"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setSelectedItem("online");

        statusComboBox.addActionListener(e -> {
        String selectedStatus = (String) statusComboBox.getSelectedItem();
        sendStatusChange(username, selectedStatus);

        // Met à jour la map des statuts (si tu l'utilises pour autre chose)
        userStatuses.put(username, selectedStatus);

        // Met à jour ton propre User dans le modèle de la JList
        for (int i = 0; i < userListModel.size(); i++) {
            User user = userListModel.get(i);
            if (user.getUsername().equals(username)) {
                // Remplace l'ancien User par un nouveau avec le statut mis à jour
                userListModel.set(i, new User(username, selectedStatus));
                break;
            }
        }

    // Redessine la liste pour que la nouvelle couleur apparaisse
    userList.repaint();
    });

JPanel topPanel = new JPanel(new BorderLayout());
topPanel.add(conversationLabel, BorderLayout.CENTER);
topPanel.add(statusComboBox, BorderLayout.EAST);

add(topPanel, BorderLayout.NORTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        userList.addListSelectionListener(e -> updateConversationLabel());

        client.addMessageListener(message -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    JSONObject json = new JSONObject(message);
                    String type = json.getString("type");

                    switch (type) {
                        case "message" -> handleMessage(json);
                        case "status" -> handleStatus(json);
                        case "users" -> handleListUsersResponse(json);
                    }
                } catch (Exception ignored) {}
            });
        });

        onlineUsers.add(username);
        refreshUserList();
        requestOnlineUsersList();
        setVisible(true);
    }

    private void requestOnlineUsersList() {
        try {
            JSONObject request = new JSONObject();
            request.put("type", "users");
            client.send(request.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleListUsersResponse(JSONObject json) {
        onlineUsers.clear();
        onlineUsers.add(username);
        for (Object obj : json.getJSONArray("users")) {
            String user = (String) obj;
            onlineUsers.add(user);
        }
        refreshUserList();
    }

    private void handleMessage(JSONObject json) {
        String from = json.getString("from");
        String content = json.getString("content");
        String subtype = json.optString("subtype", "global");

        if ("private".equals(subtype)) {
            String to = json.optString("to", "");
            if (from.equals(username) || to.equals(username)) {
                displayMessage("MP de " + from, content, "private");
            }
        } else {
            displayMessage(from, content, "global");
        }
    }


    private void handleStatus(JSONObject json) {
    String user = json.getString("user");
    String state = json.getString("state");

    userStatuses.put(user, state);

    if ("invisible".equals(state) || "offline".equals(state)) {
        onlineUsers.remove(user);
    } else {
        onlineUsers.add(user);
    }

    refreshUserList();
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (text.isEmpty()) return;

        JSONObject json = new JSONObject();
        json.put("type", "message");
        json.put("from", username);
        json.put("content", text);

        User selectedUser = userList.getSelectedValue();
        String selectedUsername = selectedUser != null ? selectedUser.getUsername() : null;

        if (selectedUsername != null && !selectedUsername.equals("Global") && !selectedUsername.equals(username)) {
            json.put("subtype", "private");
            json.put("to", selectedUsername);
            displayMessage("Moi → " + selectedUsername, text, "private");
        } else {
            json.put("subtype", "global");
            displayMessage(username, text, "global");
        }

        try {
            client.send(json.toString());
            messageField.setText("");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'envoi du message", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayMessage(String from, String content, String subtype) {
    JPanel messageLine = new JPanel();
    messageLine.setLayout(new BoxLayout(messageLine, BoxLayout.X_AXIS));
    messageLine.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    messageLine.setOpaque(false);

    boolean isMine = from.startsWith("Moi") || from.equals(username);

    // Utilisation de JTextArea pour gérer le retour à la ligne + éviter les "..."
    JTextArea textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setOpaque(false);
    textArea.setBorder(null);

    String text;
    if ("private".equals(subtype)) {
        text = from + " : " + content;
        textArea.setText(text);
        textArea.setFont(new Font("gg sans", Font.ITALIC, 14));
        textArea.setForeground(new Color(0, 90, 0)); // Vert foncé
    } else {
        text = from + " : " + content;
        textArea.setText(text);
        textArea.setFont(new Font("gg sans", Font.PLAIN, 14));
        textArea.setForeground(isMine ? Color.BLACK : Color.BLACK);
    }

    JPanel bubble = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics g2 = g.create();

            Color bubbleColor;
            if ("private".equals(subtype)) {
                bubbleColor = isMine ? new Color(130, 255, 160) : new Color(210, 210, 255); // bleu clair à gauche
            } else {
                bubbleColor = isMine ? new Color(130, 255, 160) : new Color(210, 210, 255); // bleu clair à gauche
            }

            g2.setColor(bubbleColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
        }
    };

    bubble.setLayout(new BorderLayout());
    bubble.add(textArea, BorderLayout.CENTER);
    bubble.setOpaque(false);
    bubble.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

    // Largeur maximale de la bulle : 60% de la fenêtre
    int maxWidth = (int) (getWidth() * 0.2);
    bubble.setMaximumSize(new Dimension(maxWidth, maxWidth/3));
    bubble.setAlignmentX(isMine ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

    if (isMine) {
        messageLine.add(Box.createHorizontalGlue());
        messageLine.add(bubble);
    } else {
        messageLine.add(bubble);
        messageLine.add(Box.createHorizontalGlue());
    }

    messageLine.setAlignmentX(Component.LEFT_ALIGNMENT);
    messagePanel.add(messageLine);
    messagePanel.revalidate();
    messagePanel.repaint();

    SwingUtilities.invokeLater(() -> {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    });
}


    private void refreshUserList() {
    userListModel.clear();
    userListModel.addElement(new User("Global", "none"));

    List<String> sortedUsers = new ArrayList<>(onlineUsers);
    Collections.sort(sortedUsers);

    for (String user : sortedUsers) {
        if (!user.equals("Global")) {
            String status = userStatuses.getOrDefault(user, "online"); // statut réel
            userListModel.addElement(new User(user, status));
        }
    }

    if (userList.getSelectedValue() == null) {
        userList.setSelectedIndex(0);
    }

    updateConversationLabel();
}

    private void updateConversationLabel() {
        User selected = userList.getSelectedValue();
        if (selected == null || selected.getUsername().equals("Global")) {
            conversationLabel.setText("Discussion : Global");
        } else {
            conversationLabel.setText("Discussion : MP avec " + selected.getUsername());
        }
    }

    private void sendStatusChange(String username, String status) {
        try {
            JSONObject statusChange = new JSONObject();
            statusChange.put("type", "status");
            statusChange.put("user", username);
            statusChange.put("state", status);
            client.send(statusChange.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class UserCellRenderer extends JPanel implements ListCellRenderer<User> {
    private JLabel nameLabel = new JLabel();
    private StatusIndicator statusIndicator = new StatusIndicator();

    public UserCellRenderer() {
        setLayout(new BorderLayout(5, 0));
        add(nameLabel, BorderLayout.CENTER);
        add(statusIndicator, BorderLayout.EAST);
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        setOpaque(false);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index,
                                              boolean isSelected, boolean cellHasFocus) {
    nameLabel.setText(user.getUsername());

    if ("Global".equals(user.getUsername())) {
        statusIndicator.setVisible(false);
    } else {
        statusIndicator.setVisible(true);
        statusIndicator.setStatus(user.getStatus());
    }

    if (isSelected) {
        setBackground(list.getSelectionBackground());
        nameLabel.setForeground(list.getSelectionForeground());
    } else {
        setBackground(Color.WHITE); // Arrière-plan blanc non sélectionné
        nameLabel.setForeground(list.getForeground());
    }

    return this;
}

    private static class StatusIndicator extends JPanel {
        private String status; 
        private ClientInvite client;

        public void setStatus(String status) {
            this.status = status;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            System.err.println(status);
            if (status == null) return;

           Color color = switch (status) {
            case "online" -> Color.GREEN;
            case "away" -> Color.ORANGE;
            case "dnd" -> Color.RED;
            case "none" -> Color.WHITE; // Pour le cas où le statut n'est pas défini
            case "invisible" -> Color.GRAY;
        default -> Color.WHITE;
};

            int d = 10;
            int x = (getWidth() - d) / 2;
            int y = (getHeight() - d) / 2;

            g.setColor(color);
            g.fillOval(x, y, d, d);

            
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(20, 20);
            
        }

        private void sendStatusChange(String username, String status) {
    try {
        JSONObject statusChange = new JSONObject();
        statusChange.put("type", "status");
        statusChange.put("user", username);
        statusChange.put("state", status);
        client.send(statusChange.toString());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    }
}
