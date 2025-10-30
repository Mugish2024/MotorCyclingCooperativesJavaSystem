package view;

import model.Cooperatives;
import model.Motorcyclist;
import service.MotorcyclistService;
import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MotorcyclistForm extends JFrame {
    private JTextField nameField, licenseNumberField, phoneField, dateOfBirthField, cooperativeIdField;
    private JButton saveButton;

    public MotorcyclistForm() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to set Nimbus Look and Feel: " + e.getMessage());
        }

        setTitle("Add Motorcyclist");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(fieldFont);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);

        JLabel licenseNumberLabel = new JLabel("License Number:");
        licenseNumberLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(licenseNumberLabel, gbc);

        licenseNumberField = new JTextField(20);
        licenseNumberField.setFont(fieldFont);
        licenseNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(licenseNumberField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(phoneLabel, gbc);

        phoneField = new JTextField(20);
        phoneField.setFont(fieldFont);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(phoneField, gbc);

        JLabel dateOfBirthLabel = new JLabel("Date of Birth (yyyy-MM-dd):");
        dateOfBirthLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(dateOfBirthLabel, gbc);

        dateOfBirthField = new JTextField(20);
        dateOfBirthField.setFont(fieldFont);
        dateOfBirthField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(dateOfBirthField, gbc);

        JLabel cooperativeIdLabel = new JLabel("Cooperative ID:");
        cooperativeIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(cooperativeIdLabel, gbc);

        cooperativeIdField = new JTextField(20);
        cooperativeIdField.setFont(fieldFont);
        cooperativeIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(cooperativeIdField, gbc);

        saveButton = new JButton("Save Motorcyclist");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(33, 150, 243));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(66, 165, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(33, 150, 243));
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> saveMotorcyclist());

        add(panel);
        setVisible(true);
    }

    private void saveMotorcyclist() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
            MotorcyclistService service = (MotorcyclistService) registry.lookup("MotorcyclistService");
            System.out.println("Connected to MotorcyclistService at 127.0.0.1:2325");

            Motorcyclist motorcyclist = new Motorcyclist();
            motorcyclist.setName(nameField.getText().trim());
            motorcyclist.setLicenseNumber(licenseNumberField.getText().trim());
            motorcyclist.setPhone(phoneField.getText().trim());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date dateOfBirth = sdf.parse(dateOfBirthField.getText().trim());
            motorcyclist.setDateOfBirth(dateOfBirth);

            int cooperativeId = Integer.parseInt(cooperativeIdField.getText().trim());
            Cooperatives cooperative = new Cooperatives();
            cooperative.setId(cooperativeId);
            motorcyclist.setCooperative(cooperative);

            service.save(motorcyclist);
            JOptionPane.showMessageDialog(this, "Motorcyclist saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            nameField.setText("");
            licenseNumberField.setText("");
            phoneField.setText("");
            dateOfBirthField.setText("");
            cooperativeIdField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: Cooperative ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Invalid input: " + ex.getMessage());
            ex.printStackTrace();
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format: Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Invalid date: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Connection failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}