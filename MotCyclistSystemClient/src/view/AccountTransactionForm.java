package view;

import model.AccountTransaction;
import model.SavingsAccount;
import service.AccountTransactionService;
import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountTransactionForm extends JFrame {
    private JTextField amountField, typeField, transactionDateField, accountIdField;
    private JButton saveButton;

    public AccountTransactionForm() {
        // Set Nimbus Look and Feel
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

        setTitle("Add Account Transaction");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fonts
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Amount
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(amountLabel, gbc);

        amountField = new JTextField(20);
        amountField.setFont(fieldFont);
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(amountField, gbc);

        // Type
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(typeLabel, gbc);

        typeField = new JTextField(20);
        typeField.setFont(fieldFont);
        typeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(typeField, gbc);

        // Transaction Date
        JLabel transactionDateLabel = new JLabel("Transaction Date (yyyy-MM-dd):");
        transactionDateLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(transactionDateLabel, gbc);

        transactionDateField = new JTextField(20);
        transactionDateField.setFont(fieldFont);
        transactionDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(transactionDateField, gbc);

        // Savings Account ID
        JLabel accountIdLabel = new JLabel("Savings Account ID:");
        accountIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(accountIdLabel, gbc);

        accountIdField = new JTextField(20);
        accountIdField.setFont(fieldFont);
        accountIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(accountIdField, gbc);

        // Save Button
        saveButton = new JButton("Save Transaction");
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

        saveButton.addActionListener(e -> saveAccountTransaction());

        add(panel);
        setVisible(true);
    }

    private void saveAccountTransaction() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
            AccountTransactionService service = (AccountTransactionService) registry.lookup("AccountTransactionService");
            System.out.println("Connected to AccountTransactionService at 127.0.0.1:2325");

            AccountTransaction transaction = new AccountTransaction();
            transaction.setAmount(Double.parseDouble(amountField.getText().trim()));
            transaction.setType(typeField.getText().trim());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date transactionDate = sdf.parse(transactionDateField.getText().trim());
            transaction.setTransactionDate(transactionDate);

            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setAccountId(Integer.parseInt(accountIdField.getText().trim()));
            transaction.setSavingsAccount(savingsAccount);

            service.save(transaction);
            JOptionPane.showMessageDialog(this, "Transaction saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            amountField.setText("");
            typeField.setText("");
            transactionDateField.setText("");
            accountIdField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: Amount and Savings Account ID must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> new AccountTransactionForm());
    }
}
