package view;

import model.Admin;
import service.AdminService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SignupFrame extends JFrame {
    private JTextField usernameField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField tokenField;
    private JButton signupButton;
    private JButton verifyButton;

    public SignupFrame() {
        setTitle("Sign Up");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 20, 80, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 160, 25);
        panel.add(usernameField);

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setBounds(20, 50, 80, 25);
        panel.add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(100, 50, 160, 25);
        panel.add(fullNameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 80, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 80, 160, 25);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 110, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 110, 160, 25);
        panel.add(passwordField);

        JLabel tokenLabel = new JLabel("Verification Token:");
        tokenLabel.setBounds(20, 140, 120, 25);
        panel.add(tokenLabel);

        tokenField = new JTextField();
        tokenField.setBounds(140, 140, 120, 25);
        panel.add(tokenField);

        signupButton = new JButton("Sign Up");
        signupButton.setBounds(20, 170, 100, 25);
        panel.add(signupButton);

        verifyButton = new JButton("Verify");
        verifyButton.setBounds(140, 170, 100, 25);
        panel.add(verifyButton);

        add(panel);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Basic email validation
                    if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                        JOptionPane.showMessageDialog(null, "Invalid email format!");
                        return;
                    }
                    Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
                    AdminService service = (AdminService) registry.lookup("AdminService");
                    Admin admin = new Admin();
                    admin.setUsername(usernameField.getText());
                    admin.setFullName(fullNameField.getText());
                    admin.setEmail(emailField.getText());
                    admin.setPassword(new String(passwordField.getPassword()));
                    if (service.save(admin) != null) {
                        JOptionPane.showMessageDialog(null, "Signup successful! Check your email for the verification token.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Signup failed!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        verifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
                    AdminService service = (AdminService) registry.lookup("AdminService");
                    if (service.verifyEmail(tokenField.getText())) {
                        JOptionPane.showMessageDialog(null, "Email verified! You can now log in.");
                        new LoginFrame().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid token!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }
}