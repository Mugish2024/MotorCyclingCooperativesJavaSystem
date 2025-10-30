package view;

import dao.SavingAccountDao;
import model.SavingsAccount;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SavingsAccountView extends JFrame {
    // UI Components
    private JTable accountTable;
    private JButton createButton, depositButton, withdrawButton, transferButton, 
                   interestButton, refreshButton, backButton;
    
    // Data Access
    private SavingAccountDao accountDao;
    
    // Formatters
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    private DecimalFormat numericFormat = new DecimalFormat("#,##0");

    public SavingsAccountView() {
        setupFrame();
        initializeComponents();
        loadAccountData();
        setupEventHandlers();
    }

    private void setupFrame() {
        setTitle("Savings Account Management System");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void initializeComponents() {
        // Create button panel with modern styling
        JPanel buttonPanel = createButtonPanel();
        
        // Create table with custom styling and rendering
        accountTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        // Initialize buttons with consistent styling
        createButton = createStyledButton("Create Account", new Color(76, 175, 80));
        depositButton = createStyledButton("Deposit", new Color(33, 150, 243));
        withdrawButton = createStyledButton("Withdraw", new Color(255, 193, 7));
        transferButton = createStyledButton("Transfer", new Color(156, 39, 176));
        interestButton = createStyledButton("Apply Interest", new Color(255, 152, 0));
        refreshButton = createStyledButton("Refresh", new Color(158, 158, 158));
        backButton = createStyledButton("Back to Dashboard", new Color(158, 158, 158));

        panel.add(createButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transferButton);
        panel.add(interestButton);
        panel.add(refreshButton);
        panel.add(backButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    private JTable createStyledTable() {
        String[] columns = {"Account ID", "Account Number", "Rider ID", "Balance", "Last Interest Date", "Rider Name"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 || columnIndex == 2 ? Integer.class : 
                       columnIndex == 3 ? Double.class : String.class;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Set custom renderers for numeric columns
        table.getColumnModel().getColumn(0).setCellRenderer(new NumericRenderer()); // Account ID
        table.getColumnModel().getColumn(2).setCellRenderer(new NumericRenderer()); // Rider ID
        table.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer()); // Balance
        
        return table;
    }

    // Custom renderer for numeric values (IDs)
    private class NumericRenderer extends DefaultTableCellRenderer {
        public NumericRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                value = numericFormat.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, 
                    hasFocus, row, column);
        }
    }

    // Custom renderer for currency values
    private class CurrencyRenderer extends DefaultTableCellRenderer {
        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Double) {
                value = currencyFormat.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, 
                    hasFocus, row, column);
        }
    }

    private void loadAccountData() {
        accountDao = new SavingAccountDao();
        List<SavingsAccount> accounts = accountDao.getAllAccounts();
        DefaultTableModel model = (DefaultTableModel) accountTable.getModel();
        model.setRowCount(0); // Clear existing data

        for (SavingsAccount account : accounts) {
            model.addRow(new Object[]{
                account.getAccountId(),
                account.getAccountNumber(),
                account.getRiderId(),
                account.getBalance(), // Store as Double
                account.getLastInterestDate() != null ? dateFormat.format(account.getLastInterestDate()) : "N/A",
                getRiderName(account.getRiderId())
            });
        }
    }

    private String getRiderName(int riderId) {
        // TODO: Implement actual rider name lookup
        return "Rider #" + riderId;
    }

    private void setupEventHandlers() {
        createButton.addActionListener(e -> showCreateAccountForm());
        depositButton.addActionListener(e -> showTransactionDialog("Deposit"));
        withdrawButton.addActionListener(e -> showTransactionDialog("Withdraw"));
        transferButton.addActionListener(e -> showTransferDialog());
        interestButton.addActionListener(e -> showInterestDialog());
        refreshButton.addActionListener(e -> loadAccountData());
        backButton.addActionListener(e -> returnToDashboard());
    }

   private void showCreateAccountForm() {
    JDialog dialog = new JDialog(this, "Create New Account", true);
    dialog.setLayout(new GridBagLayout());
    dialog.setSize(400, 200);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField riderIdField = new JTextField(20);
    JButton saveButton = createStyledButton("Create Account", new Color(76, 175, 80));

    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Rider ID:"), gbc);
    gbc.gridx = 1;
    dialog.add(riderIdField, gbc);

    gbc.gridx = 1; gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    dialog.add(saveButton, gbc);

    saveButton.addActionListener(e -> {
        try {
            int riderId = Integer.parseInt(riderIdField.getText());
            SavingsAccount account = new SavingsAccount();
            account.setRiderId(riderId);
            account.setBalance(0.0);
            account.setLastInterestDate(new java.util.Date());
            
            if (accountDao.create(riderId)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Account created successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadAccountData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to create account", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, 
                "Please enter a valid Rider ID", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void showTransactionDialog(String transactionType) {
    int selectedRow = accountTable.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, 
            "Please select an account", 
            "Selection Required", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    String accountNumber = (String) accountTable.getValueAt(selectedRow, 1);
    JDialog dialog = new JDialog(this, transactionType, true);
    dialog.setLayout(new GridBagLayout());
    dialog.setSize(400, 200);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField amountField = new JTextField(20);
    JButton actionButton = createStyledButton(transactionType, 
        transactionType.equals("Deposit") ? new Color(33, 150, 243) : new Color(255, 193, 7));

    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Account Number:"), gbc);
    gbc.gridx = 1;
    dialog.add(new JLabel(accountNumber), gbc);

    gbc.gridx = 0; gbc.gridy = 1;
    dialog.add(new JLabel("Amount:"), gbc);
    gbc.gridx = 1;
    dialog.add(amountField, gbc);

    gbc.gridx = 1; gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    dialog.add(actionButton, gbc);

    actionButton.addActionListener(e -> {
        try {
            double amount = Double.parseDouble(amountField.getText());
            boolean success = false;
            
            if (transactionType.equals("Deposit")) {
                success = accountDao.deposit(accountNumber, amount);
            } else {
                success = accountDao.withdraw(accountNumber, amount);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, 
                    transactionType + " successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadAccountData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    transactionType + " failed. Check balance for withdrawals.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, 
                "Please enter a valid amount", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void showTransferDialog() {
    int selectedRow = accountTable.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, 
            "Please select a source account", 
            "Selection Required", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    String fromAccount = (String) accountTable.getValueAt(selectedRow, 1);
    JDialog dialog = new JDialog(this, "Transfer Funds", true);
    dialog.setLayout(new GridBagLayout());
    dialog.setSize(500, 250);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField toAccountField = new JTextField(20);
    JTextField amountField = new JTextField(20);
    JButton transferButton = createStyledButton("Transfer", new Color(156, 39, 176));

    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("From Account:"), gbc);
    gbc.gridx = 1;
    dialog.add(new JLabel(fromAccount), gbc);

    gbc.gridx = 0; gbc.gridy = 1;
    dialog.add(new JLabel("To Account Number:"), gbc);
    gbc.gridx = 1;
    dialog.add(toAccountField, gbc);

    gbc.gridx = 0; gbc.gridy = 2;
    dialog.add(new JLabel("Amount:"), gbc);
    gbc.gridx = 1;
    dialog.add(amountField, gbc);

    gbc.gridx = 1; gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.EAST;
    dialog.add(transferButton, gbc);

    transferButton.addActionListener(e -> {
        try {
            String toAccount = toAccountField.getText();
            double amount = Double.parseDouble(amountField.getText());
            
            if (accountDao.transfer(fromAccount, toAccount, amount)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Transfer successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadAccountData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Transfer failed. Check account numbers and balance.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, 
                "Please enter valid amount", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void showInterestDialog() {
    JDialog dialog = new JDialog(this, "Apply Monthly Interest", true);
    dialog.setLayout(new GridBagLayout());
    dialog.setSize(400, 200);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField coopIdField = new JTextField(20);
    JButton applyButton = createStyledButton("Apply Interest", new Color(255, 152, 0));

    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Cooperative ID:"), gbc);
    gbc.gridx = 1;
    dialog.add(coopIdField, gbc);

    gbc.gridx = 1; gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    dialog.add(applyButton, gbc);

    applyButton.addActionListener(e -> {
        try {
            int coopId = Integer.parseInt(coopIdField.getText());
            
            if (accountDao.applyMonthlyInterest(coopId)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Interest applied successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadAccountData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to apply interest. Check cooperative ID.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, 
                "Please enter a valid Cooperative ID", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void returnToDashboard() {
    // Replace with your actual dashboard class
    new Dashbord().setVisible(true);
    this.dispose();
    JOptionPane.showMessageDialog(this, 
        "Returning to dashboard...", 
        "Info", 
        JOptionPane.INFORMATION_MESSAGE);
}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SavingsAccountView().setVisible(true);
        });
    }
}