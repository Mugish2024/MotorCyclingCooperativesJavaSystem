package view;

import dao.CooperativesDao;
import dao.MotorcyclistDao;
import dao.SavingAccountDao;
import dao.TransactionDao;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Transaction;

public class Dashbord extends javax.swing.JFrame {
    
    public Dashbord() {
        initComponents();
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Load summary numbers
        CooperativesDao coopDao = new CooperativesDao();
        MotorcyclistDao riderDao = new MotorcyclistDao();
        SavingAccountDao accountDao = new SavingAccountDao();
        
        lblTotalCooperatives.setText(String.valueOf(coopDao.findAll().size()));
        lblTotalRiders.setText(String.valueOf(riderDao.findAllMotorcyclist().size()));
        lblTotalAccounts.setText(String.valueOf(accountDao.getAllAccounts().size()));
        
        // Load recent transactions
        TransactionDao transactionDao = new TransactionDao();
        List<Transaction> transactions = transactionDao.getAllTransactions();
        DefaultTableModel model = (DefaultTableModel) tblTransactions.getModel();
        model.setRowCount(0); // Clear existing data
        
        // Show only last 5 transactions
        int limit = Math.min(transactions.size(), 5);
        for (int i = 0; i < limit; i++) {
            Transaction t = transactions.get(i);
            model.addRow(new Object[]{
                t.getTransactionDate(),
                t.getTransactionType(),
                t.getAmount() + " KES",
                t.getAccountNumber()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        summaryPanel = new javax.swing.JPanel();
        coopPanel = new javax.swing.JPanel();
        lblCoopTitle = new javax.swing.JLabel();
        lblTotalCooperatives = new javax.swing.JLabel();
        ridersPanel = new javax.swing.JPanel();
        lblRidersTitle = new javax.swing.JLabel();
        lblTotalRiders = new javax.swing.JLabel();
        accountsPanel = new javax.swing.JPanel();
        lblAccountsTitle = new javax.swing.JLabel();
        lblTotalAccounts = new javax.swing.JLabel();
        loansPanel = new javax.swing.JPanel();
        lblLoansTitle = new javax.swing.JLabel();
        lblTotalLoans = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        btnManageCoop = new javax.swing.JButton();
        btnManageRiders = new javax.swing.JButton();
        btnViewAccounts = new javax.swing.JButton();
        btnProcessInterest = new javax.swing.JButton();
        btnViewLoans = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();
        transactionsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTransactions = new javax.swing.JTable();
        btnViewAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Motorcycle Cooperative System - Dashboard");

        headerPanel.setBackground(new java.awt.Color(44, 62, 80));

        lblTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("MOTORCYCLE COOPERATIVE SYSTEM - DASHBOARD");

        lblWelcome.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(250, 250, 250));
        lblWelcome.setText("Welcome, Admin");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addComponent(lblWelcome))
                .addContainerGap(403, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblWelcome)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        summaryPanel.setLayout(new java.awt.GridLayout(2, 2, 10, 10));

        coopPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        coopPanel.setLayout(new java.awt.BorderLayout());

        lblCoopTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblCoopTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCoopTitle.setText("Total Cooperatives");
        coopPanel.add(lblCoopTitle, java.awt.BorderLayout.NORTH);

        lblTotalCooperatives.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTotalCooperatives.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalCooperatives.setText("0");
        coopPanel.add(lblTotalCooperatives, java.awt.BorderLayout.CENTER);

        summaryPanel.add(coopPanel);

        ridersPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ridersPanel.setLayout(new java.awt.BorderLayout());

        lblRidersTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblRidersTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRidersTitle.setText("Total Riders");
        ridersPanel.add(lblRidersTitle, java.awt.BorderLayout.NORTH);

        lblTotalRiders.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTotalRiders.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalRiders.setText("0");
        ridersPanel.add(lblTotalRiders, java.awt.BorderLayout.CENTER);

        summaryPanel.add(ridersPanel);

        accountsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        accountsPanel.setLayout(new java.awt.BorderLayout());

        lblAccountsTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblAccountsTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAccountsTitle.setText("Total Accounts");
        accountsPanel.add(lblAccountsTitle, java.awt.BorderLayout.NORTH);

        lblTotalAccounts.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTotalAccounts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalAccounts.setText("0");
        accountsPanel.add(lblTotalAccounts, java.awt.BorderLayout.CENTER);

        summaryPanel.add(accountsPanel);

        loansPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        loansPanel.setLayout(new java.awt.BorderLayout());

        lblLoansTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblLoansTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoansTitle.setText("Active Loans");
        loansPanel.add(lblLoansTitle, java.awt.BorderLayout.NORTH);

        lblTotalLoans.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTotalLoans.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalLoans.setText("0");
        loansPanel.add(lblTotalLoans, java.awt.BorderLayout.CENTER);

        summaryPanel.add(loansPanel);

        buttonsPanel.setLayout(new java.awt.GridLayout(2, 3, 10, 10));

        btnManageCoop.setBackground(new java.awt.Color(52, 152, 219));
        btnManageCoop.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnManageCoop.setText("Manage Cooperatives");
        buttonsPanel.add(btnManageCoop);

        btnManageRiders.setBackground(new java.awt.Color(52, 152, 219));
        btnManageRiders.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnManageRiders.setText("Manage Riders");
        buttonsPanel.add(btnManageRiders);

        btnViewAccounts.setBackground(new java.awt.Color(52, 152, 219));
        btnViewAccounts.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnViewAccounts.setText("View Accounts");
        buttonsPanel.add(btnViewAccounts);

        btnProcessInterest.setBackground(new java.awt.Color(52, 152, 219));
        btnProcessInterest.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnProcessInterest.setText("Process Interest");
        buttonsPanel.add(btnProcessInterest);

        btnViewLoans.setBackground(new java.awt.Color(52, 152, 219));
        btnViewLoans.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnViewLoans.setText("View Loans");
        buttonsPanel.add(btnViewLoans);

        btnReports.setBackground(new java.awt.Color(52, 152, 219));
        btnReports.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnReports.setText("Reports");
        buttonsPanel.add(btnReports);

        transactionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Recent Transactions (Last 5)"));
        transactionsPanel.setLayout(new java.awt.BorderLayout());

        tblTransactions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Date", "Type", "Amount", "Account"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblTransactions);

        transactionsPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        btnViewAll.setText("View All Transactions");
        transactionsPanel.add(btnViewAll, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(summaryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(transactionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summaryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transactionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashbord().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel accountsPanel;
    private javax.swing.JButton btnManageCoop;
    private javax.swing.JButton btnManageRiders;
    private javax.swing.JButton btnProcessInterest;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnViewAccounts;
    private javax.swing.JButton btnViewAll;
    private javax.swing.JButton btnViewLoans;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel coopPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAccountsTitle;
    private javax.swing.JLabel lblCoopTitle;
    private javax.swing.JLabel lblLoansTitle;
    private javax.swing.JLabel lblRidersTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalAccounts;
    private javax.swing.JLabel lblTotalCooperatives;
    private javax.swing.JLabel lblTotalLoans;
    private javax.swing.JLabel lblTotalRiders;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel loansPanel;
    private javax.swing.JPanel ridersPanel;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JTable tblTransactions;
    private javax.swing.JPanel transactionsPanel;
    // End of variables declaration                   
}