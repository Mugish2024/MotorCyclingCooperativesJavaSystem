package view;

import model.Admin;
import service.AdminService;
import view.DashBoard; // Import the new DashBoard

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 20, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 20, 160, 25);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 160, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(20, 80, 80, 25);
        panel.add(loginButton);

        signupButton = new JButton("Sign Up");
        signupButton.setBounds(180, 80, 80, 25);
        panel.add(signupButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
                    AdminService service = (AdminService) registry.lookup("AdminService");
                    Admin admin = service.login(emailField.getText(), new String(passwordField.getPassword()));
                    if (admin != null) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        new DashBoard(admin).setVisible(true); // Open new DashBoard
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials or unverified email!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignupFrame().setVisible(true);
                dispose();
            }
        });
    }
}