package view;

import dao.CooperativesDao;
import model.Cooperatives;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CooperativesView extends JFrame {
    // Components
    private JTable cooperativesTable;
    private JButton addButton, editButton, deleteButton, refreshButton, backButton, saveButton;
    private CooperativesDao cooperativesDao;

    public CooperativesView() {
        setupFrame();
        initializeComponents();
        loadCooperativesData();
        setupEventHandlers();
    }

    private void setupFrame() {
        setTitle("Cooperatives Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void initializeComponents() {
        // Create button panel with modern styling
        JPanel buttonPanel = createButtonPanel();
        
        // Create table with scroll pane
        cooperativesTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(cooperativesTable);
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
        addButton = createStyledButton("Add", new Color(76, 175, 80));
        editButton = createStyledButton("Edit", new Color(33, 150, 243));
        deleteButton = createStyledButton("Delete", new Color(244, 67, 54));
        refreshButton = createStyledButton("Refresh", new Color(255, 152, 0));
        backButton = createStyledButton("Back to Dashboard", new Color(158, 158, 158));

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
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
        // Table model with column names
        String[] columns = {"ID", "Name", "Location", "Interest Rate"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable table
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        return table;
    }

    private void loadCooperativesData() {
        cooperativesDao = new CooperativesDao();
        List<Cooperatives> cooperativesList = cooperativesDao.findAll();
        DefaultTableModel model = (DefaultTableModel) cooperativesTable.getModel();
        model.setRowCount(0); // Clear existing data

        for (Cooperatives coop : cooperativesList) {
            model.addRow(new Object[]{
                coop.getId(),
                coop.getName(),
                coop.getLocation(),
                String.format("%.2f%%", coop.getInterestRate())
            });
        }
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> showCooperativeForm(null));
        editButton.addActionListener(e -> editSelectedCooperative());
        deleteButton.addActionListener(e -> deleteSelectedCooperative());
        refreshButton.addActionListener(e -> loadCooperativesData());
        backButton.addActionListener(e -> returnToDashboard());
    }

    private void showCooperativeForm(Cooperatives coop) {
        JDialog dialog = new JDialog(this, "Cooperative Form", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 300);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form fields
        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField interestField = new JTextField(20);
        saveButton = createStyledButton("Save", new Color(76, 175, 80));

        if (coop != null) {
            nameField.setText(coop.getName());
            locationField.setText(coop.getLocation());
            interestField.setText(String.valueOf(coop.getInterestRate()));
        }

        // Add components to dialog
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        dialog.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Interest Rate (%):"), gbc);
        gbc.gridx = 1;
        dialog.add(interestField, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            saveCooperative(
                coop, 
                nameField.getText(), 
                locationField.getText(), 
                interestField.getText()
            );
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void saveCooperative(Cooperatives existingCoop, String name, String location, String interestStr) {
        try {
            double interestRate = Double.parseDouble(interestStr);
            Cooperatives coop = existingCoop != null ? existingCoop : new Cooperatives();
            
            coop.setName(name);
            coop.setLocation(location);
            coop.setInterestRate(interestRate);

            if (existingCoop == null) {
                cooperativesDao.add(coop);
            } else {
                cooperativesDao.update(coop);
            }

            loadCooperativesData();
            Window window = SwingUtilities.getWindowAncestor(saveButton);
            if (window != null) {
                window.dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for interest rate", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedCooperative() {
        int selectedRow = cooperativesTable.getSelectedRow();
        if (selectedRow < 0) {
            showSelectionError("Please select a cooperative to edit");
            return;
        }

        Cooperatives coop = new Cooperatives();
        coop.setId((Integer) cooperativesTable.getValueAt(selectedRow, 0));
        coop.setName((String) cooperativesTable.getValueAt(selectedRow, 1));
        coop.setLocation((String) cooperativesTable.getValueAt(selectedRow, 2));
        coop.setInterestRate(Double.parseDouble(
            cooperativesTable.getValueAt(selectedRow, 3).toString().replace("%", ""))
        );

        showCooperativeForm(coop);
    }

    private void deleteSelectedCooperative() {
        int selectedRow = cooperativesTable.getSelectedRow();
        if (selectedRow < 0) {
            showSelectionError("Please select a cooperative to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this cooperative?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (Integer) cooperativesTable.getValueAt(selectedRow, 0);
            cooperativesDao.delete(id);
            loadCooperativesData();
        }
    }

    private void showSelectionError(String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Selection Required", 
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void returnToDashboard() {
        new Dashbord().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CooperativesView().setVisible(true);
        });
    }
}