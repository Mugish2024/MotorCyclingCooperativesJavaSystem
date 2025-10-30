package view;

import model.Cooperatives;
import service.CooperativesService;
import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CooperativeForm extends JFrame {
    private JTextField idField, nameField, locationField, interestRateField;
    private JButton saveButton;

    public CooperativeForm() {
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

        setTitle("Add Cooperative");
        setSize(400, 300);
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

        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);

        idField = new JTextField(20);
        idField.setFont(fieldFont);
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(idField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(fieldFont);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(locationLabel, gbc);

        locationField = new JTextField(20);
        locationField.setFont(fieldFont);
        locationField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(locationField, gbc);

        JLabel interestRateLabel = new JLabel("Interest Rate:");
        interestRateLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(interestRateLabel, gbc);

        interestRateField = new JTextField(20);
        interestRateField.setFont(fieldFont);
        interestRateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(interestRateField, gbc);

        saveButton = new JButton("Save Cooperative");
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
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> saveCooperative());

        add(panel);
        setVisible(true);
    }

    private void saveCooperative() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
            CooperativesService service = (CooperativesService) registry.lookup("CooperativesService");
            System.out.println("Connected to CooperativesService at 127.0.0.1:2325");

            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            double interestRate = Double.parseDouble(interestRateField.getText().trim());
            Cooperatives cooperative = new Cooperatives(id, name, location, interestRate);

            service.save(cooperative);
            JOptionPane.showMessageDialog(this, "Cooperative saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            idField.setText("");
            nameField.setText("");
            locationField.setText("");
            interestRateField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: ID and Interest Rate must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Invalid input: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Connection failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}