package view;

import dao.MotorcyclistDao;
import model.Motorcyclist;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MotorcyclistView extends JFrame {
    // Components
    private JTable motorcyclistTable;
    private JButton addButton, editButton, deleteButton, refreshButton, backButton;
    private MotorcyclistDao motorcyclistDao;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MotorcyclistView() {
        setupFrame();
        initializeComponents();
        loadMotorcyclistData();
        setupEventHandlers();
    }

    private void setupFrame() {
        setTitle("Motorcyclist Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private void initializeComponents() {
        // Create button panel with modern styling
        JPanel buttonPanel = createButtonPanel();
        
        // Create table with scroll pane
        motorcyclistTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(motorcyclistTable);
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
        addButton = createStyledButton("Add Rider", new Color(76, 175, 80));
        editButton = createStyledButton("Edit Rider", new Color(33, 150, 243));
        deleteButton = createStyledButton("Delete Rider", new Color(244, 67, 54));
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
        String[] columns = {"ID", "Name", "License Number", "Phone", "Date of Birth", "Cooperative", "Cooperative ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable table
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Hide the cooperative ID column by default (can be made visible if needed)
        table.removeColumn(table.getColumnModel().getColumn(6));
        
        return table;
    }

    private void loadMotorcyclistData() {
        motorcyclistDao = new MotorcyclistDao();
        List<Motorcyclist> riders = motorcyclistDao.findAllMotorcyclist();
        DefaultTableModel model = (DefaultTableModel) motorcyclistTable.getModel();
        model.setRowCount(0); // Clear existing data

        for (Motorcyclist rider : riders) {
            model.addRow(new Object[]{
                rider.getId(),
                rider.getName(),
                rider.getLicenseNumber(),
                rider.getPhone(),
                rider.getDateOfBirth() != null ? dateFormat.format(rider.getDateOfBirth()) : "N/A",
                rider.getCooperativeName(),
                rider.getCooperativeId()
            });
        }
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> showMotorcyclistForm(null));
        editButton.addActionListener(e -> editSelectedMotorcyclist());
        deleteButton.addActionListener(e -> deleteSelectedMotorcyclist());
        refreshButton.addActionListener(e -> loadMotorcyclistData());
        backButton.addActionListener(e -> returnToDashboard());
    }

    private void showMotorcyclistForm(Motorcyclist rider) {
        JDialog dialog = new JDialog(this, rider == null ? "Add New Rider" : "Edit Rider", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(450, 350);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form fields
        JTextField nameField = new JTextField(20);
        JTextField licenseField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField dobField = new JTextField(20);
        JTextField coopIdField = new JTextField(20);
        JButton saveButton = createStyledButton("Save", new Color(76, 175, 80));

        if (rider != null) {
            nameField.setText(rider.getName());
            licenseField.setText(rider.getLicenseNumber());
            phoneField.setText(rider.getPhone());
            dobField.setText(rider.getDateOfBirth() != null ? dateFormat.format(rider.getDateOfBirth()) : "");
            coopIdField.setText(String.valueOf(rider.getCooperativeId()));
        }

        // Add components to dialog
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("License Number:"), gbc);
        gbc.gridx = 1;
        dialog.add(licenseField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        dialog.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dialog.add(dobField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Cooperative ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(coopIdField, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            saveMotorcyclist(
                rider,
                nameField.getText(),
                licenseField.getText(),
                phoneField.getText(),
                dobField.getText(),
                coopIdField.getText()
            );
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void saveMotorcyclist(Motorcyclist existingRider, String name, String license, 
                                String phone, String dobStr, String coopIdStr) {
        try {
            Motorcyclist rider = existingRider != null ? existingRider : new Motorcyclist();
            
            rider.setName(name);
            rider.setLicenseNumber(license);
            rider.setPhone(phone);
            rider.setDateOfBirth(dobStr.isEmpty() ? null : dateFormat.parse(dobStr));
            rider.setCooperativeId(Integer.parseInt(coopIdStr));

            if (existingRider == null) {
                motorcyclistDao.add(rider);
            } else {
                motorcyclistDao.update(rider);
            }

            loadMotorcyclistData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error saving rider: " + ex.getMessage(), 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedMotorcyclist() {
        int selectedRow = motorcyclistTable.getSelectedRow();
        if (selectedRow < 0) {
            showSelectionError("Please select a rider to edit");
            return;
        }

        Motorcyclist rider = motorcyclistDao.findMotorcyclistById(
            (Integer) motorcyclistTable.getValueAt(selectedRow, 0)
        );
        
        if (rider != null) {
            showMotorcyclistForm(rider);
        }
    }

    private void deleteSelectedMotorcyclist() {
        int selectedRow = motorcyclistTable.getSelectedRow();
        if (selectedRow < 0) {
            showSelectionError("Please select a rider to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this rider?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (Integer) motorcyclistTable.getValueAt(selectedRow, 0);
            motorcyclistDao.delete(id);
            loadMotorcyclistData();
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
        // Replace with your actual dashboard class
        new Dashbord().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MotorcyclistView().setVisible(true);
        });
    }
}