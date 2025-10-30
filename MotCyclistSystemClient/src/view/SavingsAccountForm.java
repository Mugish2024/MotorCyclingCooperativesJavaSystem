package view;

import model.Motorcyclist;
import model.SavingsAccount;
import service.SavingsAccountService;
import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavingsAccountForm extends JFrame {
    private JTextField accountNumberField, balanceField, lastInterestDateField, motorcyclistIdField;
    private JButton saveButton;

    public SavingsAccountForm() {
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

        setTitle("Add Savings Account");
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

        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(accountNumberLabel, gbc);

        accountNumberField = new JTextField(20);
        accountNumberField.setFont(fieldFont);
        accountNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(accountNumberField, gbc);

        JLabel balanceLabel = new JLabel("Balance:");
        balanceLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(balanceLabel, gbc);

        balanceField = new JTextField(20);
        balanceField.setFont(fieldFont);
        balanceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(balanceField, gbc);

        JLabel lastInterestDateLabel = new JLabel("Last Interest Date (yyyy-MM-dd):");
        lastInterestDateLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lastInterestDateLabel, gbc);

        lastInterestDateField = new JTextField(20);
        lastInterestDateField.setFont(fieldFont);
        lastInterestDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(lastInterestDateField, gbc);

        JLabel motorcyclistIdLabel = new JLabel("Motorcyclist ID:");
        motorcyclistIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(motorcyclistIdLabel, gbc);

        motorcyclistIdField = new JTextField(20);
        motorcyclistIdField.setFont(fieldFont);
        motorcyclistIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(motorcyclistIdField, gbc);

        saveButton = new JButton("Save Savings Account");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(33, 150, 243));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(66, 165, 245));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(33, 150, 243));
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> saveSavingsAccount());

        add(panel);
        setVisible(true);
    }

    private void saveSavingsAccount() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
            SavingsAccountService service = (SavingsAccountService) registry.lookup("SavingsAccountService");
            System.out.println("Connected to SavingsAccountService at 127.0.0.1:2325");

            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setAccountNumber(accountNumberField.getText().trim());
            savingsAccount.setBalance(Double.parseDouble(balanceField.getText().trim()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date lastInterestDate = sdf.parse(lastInterestDateField.getText().trim());
            savingsAccount.setLastInterestDate(lastInterestDate);

            Motorcyclist motorcyclist = new Motorcyclist();
            motorcyclist.setId(Integer.parseInt(motorcyclistIdField.getText().trim()));
            savingsAccount.setMotorcyclist(motorcyclist);

            service.save(savingsAccount);
            JOptionPane.showMessageDialog(this, "Savings Account saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            accountNumberField.setText("");
            balanceField.setText("");
            lastInterestDateField.setText("");
            motorcyclistIdField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: Balance and Motorcyclist ID must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SavingsAccountForm());
    }
}