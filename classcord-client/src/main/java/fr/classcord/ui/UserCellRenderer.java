package fr.classcord.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import fr.classcord.model.User;

public class UserCellRenderer extends JPanel implements ListCellRenderer<User> {
    private JLabel nameLabel;

    public UserCellRenderer() {
        setLayout(null);
        nameLabel = new JLabel();
        add(nameLabel);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        removeAll();
        setLayout(null);

        String username = user.getUsername();

        nameLabel = new JLabel(username);
        nameLabel.setBounds(20, 0, list.getWidth() - 25, 20);
        add(nameLabel);

        

        setPreferredSize(new java.awt.Dimension(list.getWidth(), 20));
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            nameLabel.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            nameLabel.setForeground(list.getForeground());
        }

        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        User user = null;
        if (getClientProperty("rendererUser") instanceof User u) {
            user = u;
        }
        Color color = Color.GREEN;

        if (user != null) {
            color = switch (user.getStatus()) {
                case "online" -> Color.GREEN;
                case "away" -> Color.ORANGE;
                case "dnd" -> Color.RED;
                case "invisible" -> Color.GRAY;
                case "none" -> Color.WHITE;
                default -> Color.WHITE;
            };
        }

        g.setColor(color);
        g.fillOval(4, 4, 10, 10);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        putClientProperty("rendererUser", null);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (nameLabel != null) {
            nameLabel.setBounds(20, 0, width - 25, height);
        }
    }

    public Component    prepareRenderer(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {
        putClientProperty("rendererUser", value);
        return getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
