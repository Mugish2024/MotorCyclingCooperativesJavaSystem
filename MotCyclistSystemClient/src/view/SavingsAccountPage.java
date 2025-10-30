
package view;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.AccountTransaction;
import model.Motorcyclist;
import model.SavingsAccount;
import model.Cooperatives;
import service.AccountTransactionService;
import service.MotorcyclistService;
import service.SavingsAccountService;
import java.awt.Color;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SavingsAccountPage extends javax.swing.JFrame {
    private SavingsAccountService savingsService;
    private AccountTransactionService transactionService;
    private MotorcyclistService motorcyclistService;
    private DefaultTableModel tableModel;
    private static final double INTEREST_RATE = 0.05; // 5% annual

    public SavingsAccountPage() {
        initComponents();
        AccountIdTxt.setEditable(false);
        AccountIdTxt.setBackground(new Color(248, 249, 250));
        AccountIdTxt.setForeground(new Color(108, 117, 125));
        AccountNumberTxt.setEditable(false);
        BalanceTxt.setEditable(false);
        LastInterestDateTxt.setEditable(false);
        initializeServices();
        initializeTable();
        loadAccounts();
    }

    private void initializeServices() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
            savingsService = (SavingsAccountService) registry.lookup("SavingsAccountService");
            transactionService = (AccountTransactionService) registry.lookup("AccountTransactionService");
            motorcyclistService = (MotorcyclistService) registry.lookup("MotorcyclistService");
            if (savingsService == null || transactionService == null || motorcyclistService == null) {
                throw new Exception("Failed to lookup services");
            }
            System.out.println("Connected to services at 127.0.0.1:2325");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(new String[]{"Account ID", "Account Number", "Balance", "Last Interest Date", "Motorcyclist Name"}, 0);
        jTable1.setModel(tableModel);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0); // Hide Account ID
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                int row = jTable1.getSelectedRow();
                try {
                    int accountId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    SavingsAccount account = savingsService.findById(accountId);
                    if (account != null) {
                        AccountIdTxt.setText(String.valueOf(accountId));
                        AccountNumberTxt.setText(account.getAccountNumber() != null ? account.getAccountNumber() : "");
                        BalanceTxt.setText(String.valueOf(account.getBalance()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        LastInterestDateTxt.setText(account.getLastInterestDate() != null ? sdf.format(account.getLastInterestDate()) : "");
                        MotorcyclistIdTxt.setText(account.getMotorcyclist() != null ? String.valueOf(account.getMotorcyclist().getId()) : "");
                        AmountTxt.setText("");
                    }
                } catch (Exception ex) {
                    System.err.println("Error fetching savings account for selection: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    private void loadAccounts() {
        try {
            if (savingsService == null) {
                JOptionPane.showMessageDialog(this, "Service not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            tableModel.setRowCount(0);
            List<SavingsAccount> accounts = savingsService.findAll();
            if (accounts != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (SavingsAccount account : accounts) {
                    if (account != null) {
                        tableModel.addRow(new Object[]{
                            account.getAccountId(),
                            account.getAccountNumber() != null ? account.getAccountNumber() : "",
                            account.getBalance(),
                            account.getLastInterestDate() != null ? sdf.format(account.getLastInterestDate()) : "",
                            account.getMotorcyclist() != null ? account.getMotorcyclist().getName() : ""
                        });
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading accounts: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Load failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String generateAccountNumber() {
        try {
            List<SavingsAccount> accounts = savingsService.findAll();
            int maxNumber = 1000; // Start at ACC1000
            for (SavingsAccount account : accounts) {
                if (account.getAccountNumber() != null && account.getAccountNumber().startsWith("ACC")) {
                    try {
                        int number = Integer.parseInt(account.getAccountNumber().substring(3));
                        maxNumber = Math.max(maxNumber, number);
                    } catch (NumberFormatException ignored) {}
                }
            }
            return "ACC" + (maxNumber + 1);
        } catch (Exception ex) {
            System.err.println("Error generating account number: " + ex.getMessage());
            return "ACC" + System.currentTimeMillis(); // Fallback
        }
    }

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (MotorcyclistIdTxt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Motorcyclist ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int motorcyclistId;
            try {
                motorcyclistId = Integer.parseInt(MotorcyclistIdTxt.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Motorcyclist ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Motorcyclist motorcyclist = motorcyclistService.findById(motorcyclistId);
            if (motorcyclist == null) {
                JOptionPane.showMessageDialog(this, "Motorcyclist not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SavingsAccount existingAccount = savingsService.findByMotorcyclistId(motorcyclistId);
            if (existingAccount != null) {
                JOptionPane.showMessageDialog(this, "Motorcyclist already has a savings account.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SavingsAccount account = new SavingsAccount();
            account.setAccountNumber(generateAccountNumber());
            account.setBalance(0.0);
            account.setLastInterestDate(new Date());
            account.setMotorcyclist(motorcyclist);
            savingsService.save(account);
            JOptionPane.showMessageDialog(this, "Savings account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            MotorcyclistIdTxt.setText("");
            loadAccounts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Save failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void DepositButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (AccountNumberTxt.getText().trim().isEmpty() || AmountTxt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Account number and amount are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(AmountTxt.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String accountNumber = AccountNumberTxt.getText().trim();
            SavingsAccount account = null;
            for (SavingsAccount acc : savingsService.findAll()) {
                if (accountNumber.equals(acc.getAccountNumber())) {
                    account = acc;
                    break;
                }
            }
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            account.setBalance(account.getBalance() + amount);
            savingsService.update(account);
            AccountTransaction transaction = new AccountTransaction();
            transaction.setAmount(amount);
            transaction.setType("DEPOSIT");
            transaction.setTransactionDate(new Date());
            transaction.setSavingsAccount(account);
            transactionService.save(transaction);
            JOptionPane.showMessageDialog(this, "Deposit successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            AccountNumberTxt.setText("");
            AmountTxt.setText("");
            loadAccounts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error processing deposit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Deposit failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void WithdrawButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (AccountNumberTxt.getText().trim().isEmpty() || AmountTxt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Account number and amount are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(AmountTxt.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String accountNumber = AccountNumberTxt.getText().trim();
            SavingsAccount account = null;
            for (SavingsAccount acc : savingsService.findAll()) {
                if (accountNumber.equals(acc.getAccountNumber())) {
                    account = acc;
                    break;
                }
            }
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (account.getBalance() < amount) {
                JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            account.setBalance(account.getBalance() - amount);
            savingsService.update(account);
            AccountTransaction transaction = new AccountTransaction();
            transaction.setAmount(amount);
            transaction.setType("WITHDRAWAL");
            transaction.setTransactionDate(new Date());
            transaction.setSavingsAccount(account);
            transactionService.save(transaction);
            JOptionPane.showMessageDialog(this, "Withdrawal successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            AccountNumberTxt.setText("");
            AmountTxt.setText("");
            loadAccounts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error processing withdrawal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Withdrawal failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void ApplyInterestButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (AccountNumberTxt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Account number is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String accountNumber = AccountNumberTxt.getText().trim();
            SavingsAccount account = null;
            for (SavingsAccount acc : savingsService.findAll()) {
                if (accountNumber.equals(acc.getAccountNumber())) {
                    account = acc;
                    break;
                }
            }
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (account.getLastInterestDate() == null) {
                JOptionPane.showMessageDialog(this, "Last interest date is missing.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String lastInterestStr = sdf.format(account.getLastInterestDate());
            String currentStr = sdf.format(new Date());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate lastInterestDate = LocalDate.parse(lastInterestStr, formatter);
            LocalDate currentDate = LocalDate.parse(currentStr, formatter);
            double years = Period.between(lastInterestDate, currentDate).getDays() / 365.0;
            double interest = account.getBalance() * INTEREST_RATE * years;
            if (interest <= 0) {
                JOptionPane.showMessageDialog(this, "No interest accrued.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            account.setBalance(account.getBalance() + interest);
            account.setLastInterestDate(new Date());
            savingsService.update(account);
            AccountTransaction transaction = new AccountTransaction();
            transaction.setAmount(interest);
            transaction.setType("INTEREST");
            transaction.setTransactionDate(new Date());
            transaction.setSavingsAccount(account);
            transactionService.save(transaction);
            JOptionPane.showMessageDialog(this, "Interest applied successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            AccountNumberTxt.setText("");
            AmountTxt.setText("");
            loadAccounts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error applying interest: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Interest application failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void SearchByIdButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String searchId = SearchIdTxt.getText().trim();
            if (searchId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter an account ID to search.", "Error", JOptionPane.ERROR_MESSAGE);
                loadAccounts();
                return;
            }
            int accountId = Integer.parseInt(searchId);
            SavingsAccount account = savingsService.findById(accountId);
            tableModel.setRowCount(0);
            if (account != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tableModel.addRow(new Object[]{
                    account.getAccountId(),
                    account.getAccountNumber() != null ? account.getAccountNumber() : "",
                    account.getBalance(),
                    account.getLastInterestDate() != null ? sdf.format(account.getLastInterestDate()) : "",
                    account.getMotorcyclist() != null ? account.getMotorcyclist().getName() : ""
                });
            } else {
                JOptionPane.showMessageDialog(this, "No account found with ID " + accountId, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            SearchIdTxt.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Account ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Search failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (AccountIdTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select an account to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this account?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int accountId = Integer.parseInt(AccountIdTxt.getText());
                SavingsAccount account = savingsService.findById(accountId);
                if (account == null) {
                    savingsService.delete(account);
                    JOptionPane.showMessageDialog(this, "Account deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearAccountFields();
                    loadAccounts();
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid account ID: Must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Delete failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearAccountFields() {
        AccountIdTxt.setText("");
        AccountNumberTxt.setText("");
        BalanceTxt.setText("");
        LastInterestDateTxt.setText("");
        MotorcyclistIdTxt.setText("");
        AmountTxt.setText("");
        SearchIdTxt.setText("");
    }

    @SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
private void initComponents() {
    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    AccountIdTxt = new javax.swing.JTextField();
    AccountNumberTxt = new javax.swing.JTextField();
    BalanceTxt = new javax.swing.JTextField();
    LastInterestDateTxt = new javax.swing.JTextField();
    MotorcyclistIdTxt = new javax.swing.JTextField();
    AmountTxt = new javax.swing.JTextField();
    SearchIdTxt = new javax.swing.JTextField();
    SaveButton = new javax.swing.JButton();
    DepositButton = new javax.swing.JButton();
    WithdrawButton = new javax.swing.JButton();
    ApplyInterestButton = new javax.swing.JButton();
    SearchByIdButton = new javax.swing.JButton();
    DeleteButton = new javax.swing.JButton(); // Fixed: Correct JButton initialization
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Savings Account Management");

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Savings Account Details"));

    jLabel1.setText("Account ID:");
    jLabel2.setText("Account Number:");
    jLabel3.setText("Balance:");
    jLabel4.setText("Last Interest Date:");
    jLabel5.setText("Motorcyclist ID:");
    jLabel6.setText("Amount:");
    jLabel7.setText("Search by ID:");

    SaveButton.setText("Save");
    SaveButton.addActionListener(this::SaveButtonActionPerformed);

    DepositButton.setText("Deposit");
    DepositButton.addActionListener(this::DepositButtonActionPerformed);

    WithdrawButton.setText("Withdraw");
    WithdrawButton.addActionListener(this::WithdrawButtonActionPerformed);

    ApplyInterestButton.setText("Apply Interest");
    ApplyInterestButton.addActionListener(this::ApplyInterestButtonActionPerformed);

    SearchByIdButton.setText("Search");
    SearchByIdButton.addActionListener(this::SearchByIdButtonActionPerformed);

    DeleteButton.setText("Delete");
    DeleteButton.addActionListener(this::DeleteButtonActionPerformed);

    jTable1.setModel(new javax.swing.table.DefaultTableModel());
    jScrollPane1.setViewportView(jTable1);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(AccountIdTxt)
                        .addComponent(AccountNumberTxt)
                        .addComponent(BalanceTxt)
                        .addComponent(LastInterestDateTxt)
                        .addComponent(MotorcyclistIdTxt)
                        .addComponent(AmountTxt)
                        .addComponent(SearchIdTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(SaveButton)
                        .addComponent(DepositButton)
                        .addComponent(WithdrawButton)
                        .addComponent(ApplyInterestButton)
                        .addComponent(SearchByIdButton)
                        .addComponent(DeleteButton))
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(AccountIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(SaveButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(AccountNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(DepositButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(BalanceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(WithdrawButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(LastInterestDateTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ApplyInterestButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(MotorcyclistIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(SearchByIdButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(AmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(DeleteButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7)
                .addComponent(SearchIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    pack();
}// </editor-fold>                       

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new SavingsAccountPage().setVisible(true));
    }

    // Variables declaration - Generated by NetBeans
    private javax.swing.JTextField AccountIdTxt;
    private javax.swing.JTextField AccountNumberTxt;
    private javax.swing.JTextField AmountTxt;
    private javax.swing.JTextField BalanceTxt;
    private javax.swing.JButton ApplyInterestButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JButton DepositButton;
    private javax.swing.JTextField LastInterestDateTxt;
    private javax.swing.JTextField MotorcyclistIdTxt;
    private javax.swing.JButton SaveButton;
    private javax.swing.JTextField SearchIdTxt;
    private javax.swing.JButton SearchByIdButton;
    private javax.swing.JButton WithdrawButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration                   
}
