package fr.classcord.app;
import fr.classcord.model.Message;
import fr.classcord.model.User;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        User user = new User("Alice", "online");
        Message message = new Message("text", "chat", user.getUsername(), "Bob", "Hello, Bob!", "2025-16-06:12:15");
        System.out.println(message); // Appelle toString()
    }
}
