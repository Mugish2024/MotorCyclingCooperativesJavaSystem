package view;

import model.Loan;
import model.Motorcyclist;
import service.LoanService;
import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanForm extends JFrame {
    private JTextField amountField, interestRateField, issueDateField, dueDateField, motorcyclistIdField;
    private JCheckBox isRepaidCheckBox;
    private JButton saveButton;

    public LoanForm() {
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

        setTitle("Add Loan");
        setSize(400, 350);
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

        // Interest Rate
        JLabel interestRateLabel = new JLabel("Interest Rate:");
        interestRateLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(interestRateLabel, gbc);

        interestRateField = new JTextField(20);
        interestRateField.setFont(fieldFont);
        interestRateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(interestRateField, gbc);

        // Issue Date
        JLabel issueDateLabel = new JLabel("Issue Date (yyyy-MM-dd):");
        issueDateLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(issueDateLabel, gbc);

        issueDateField = new JTextField(20);
        issueDateField.setFont(fieldFont);
        issueDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(issueDateField, gbc);

        // Due Date
        JLabel dueDateLabel = new JLabel("Due Date (yyyy-MM-dd):");
        dueDateLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(dueDateLabel, gbc);

        dueDateField = new JTextField(20);
        dueDateField.setFont(fieldFont);
        dueDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(dueDateField, gbc);

        // Is Repaid
        JLabel isRepaidLabel = new JLabel("Is Repaid:");
        isRepaidLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(isRepaidLabel, gbc);

        isRepaidCheckBox = new JCheckBox();
        isRepaidCheckBox.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(isRepaidCheckBox, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Reset for next components

        // Motorcyclist ID
        JLabel motorcyclistIdLabel = new JLabel("Motorcyclist ID:");
        motorcyclistIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(motorcyclistIdLabel, gbc);

        motorcyclistIdField = new JTextField(20);
        motorcyclistIdField.setFont(fieldFont);
        motorcyclistIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(motorcyclistIdField, gbc);

        // Save Button
        saveButton = new JButton("Save Loan");
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
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> saveLoan());

        add(panel);
        setVisible(true);
    }

    private void saveLoan() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
            LoanService service = (LoanService) registry.lookup("LoanService");
            System.out.println("Connected to LoanService at 127.0.0.1:2325");

            Loan loan = new Loan();
            loan.setAmount(Double.parseDouble(amountField.getText().trim()));
            loan.setInterestRate(Double.parseDouble(interestRateField.getText().trim()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date issueDate = sdf.parse(issueDateField.getText().trim());
            loan.setIssueDate(issueDate);

            Date dueDate = sdf.parse(dueDateField.getText().trim());
            loan.setDueDate(dueDate);

            loan.setIsRepaid(isRepaidCheckBox.isSelected());

            Motorcyclist motorcyclist = new Motorcyclist();
            motorcyclist.setId(Integer.parseInt(motorcyclistIdField.getText().trim()));
            loan.setMotorcyclist(motorcyclist);

            service.save(loan);
            JOptionPane.showMessageDialog(this, "Loan saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            amountField.setText("");
            interestRateField.setText("");
            issueDateField.setText("");
            dueDateField.setText("");
            isRepaidCheckBox.setSelected(false);
            motorcyclistIdField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: Amount, Interest Rate, and Motorcyclist ID must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> new LoanForm());
    }
}