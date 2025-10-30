package view;

import model.Cooperatives;
import service.CooperativesService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class CooperativePage extends JFrame {
    private JTextField idField, nameField, locationField, interestRateField, searchIdField;
    private JTable cooperativeTable;
    private DefaultTableModel tableModel;
    private JButton saveButton, updateButton, deleteButton, refreshButton, searchButton;
    private CooperativesService service;
    
    // Modern color palette
    private final Color PRIMARY_COLOR = new Color(30, 136, 229);
    private final Color PRIMARY_DARK = new Color(25, 118, 210);
    private final Color ACCENT_COLOR = new Color(255, 193, 7);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color DANGER_COLOR = new Color(244, 67, 54);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private final Color TEXT_SECONDARY = new Color(108, 117, 125);
    private final Color BORDER_COLOR = new Color(222, 226, 230);

    public CooperativePage() {
        setTitle("🏢 Cooperative Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set Look and Feel: " + e.getMessage());
        }

        // Connect to RMI service
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
            service = (CooperativesService) registry.lookup("CooperativesService");
            System.out.println("Connected to CooperativesService at 127.0.0.1:2325");
        } catch (Exception e) {
            showStyledMessage("Server connection failed: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.err.println("Connection failed: " + e.getMessage());
            System.exit(1);
        }

        initializeComponents();
        loadCooperatives();
        pack();
        setVisible(true);
    }

    private void initializeComponents() {
        // Main container with gradient background
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_COLOR);
        
        // Header panel with gradient
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel for form
        JPanel leftPanel = createFormPanel();
        contentPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right panel for table
        JPanel rightPanel = createTablePanel();
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        add(mainContainer);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    getWidth(), getHeight(), PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Title label
        JLabel titleLabel = new JLabel("🏢 Cooperative Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Status label
        JLabel statusLabel = new JLabel("✓ Connected to Server");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(200, 255, 200));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 30));
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setPreferredSize(new Dimension(400, 0));
        formContainer.setBackground(BACKGROUND_COLOR);
        
        // Form card
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(CARD_COLOR);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Add shadow effect
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Form title
        JLabel formTitle = new JLabel("📋 Cooperative Details");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(TEXT_PRIMARY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formCard.add(formTitle);
        
        // Form fields
        idField = createStyledTextField("ID (Auto-generated)", false);
        nameField = createStyledTextField("Cooperative Name", true);
        locationField = createStyledTextField("Location", true);
        interestRateField = createStyledTextField("Interest Rate (%)", true);
        searchIdField = createStyledTextField("Search by ID", true);
        
        formCard.add(createFieldPanel("🆔 ID:", idField));
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(createFieldPanel("🏢 Name:", nameField));
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(createFieldPanel("📍 Location:", locationField));
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(createFieldPanel("💰 Interest Rate:", interestRateField));
        formCard.add(Box.createVerticalStrut(20));
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        formCard.add(separator);
        formCard.add(Box.createVerticalStrut(20));
        
        formCard.add(createFieldPanel("🔍 Search:", searchIdField));
        formCard.add(Box.createVerticalStrut(25));
        
        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        formCard.add(buttonsPanel);
        
        formContainer.add(formCard, BorderLayout.NORTH);
        return formContainer;
    }

    private JTextField createStyledTextField(String placeholder, boolean editable) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(editable ? Color.WHITE : new Color(248, 249, 250));
        field.setEditable(editable);
        
        if (!editable) {
            field.setForeground(TEXT_SECONDARY);
        }
        
        // Add focus effects
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(9, 11, 9, 11)
                ));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
        
        return field;
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(CARD_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonsPanel.setBackground(CARD_COLOR);
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        saveButton = createStyledButton("💾 Save", SUCCESS_COLOR);
        updateButton = createStyledButton("✏️ Update", PRIMARY_COLOR);
        deleteButton = createStyledButton("🗑️ Delete", DANGER_COLOR);
        refreshButton = createStyledButton("🔄 Refresh", new Color(108, 117, 125));
        searchButton = createStyledButton("🔍 Search", ACCENT_COLOR);
        JButton clearButton = createStyledButton("🧹 Clear", new Color(108, 117, 125));
        
        // Add action listeners
        saveButton.addActionListener(e -> saveCooperative());
        updateButton.addActionListener(e -> updateCooperative());
        deleteButton.addActionListener(e -> deleteCooperative());
        refreshButton.addActionListener(e -> loadCooperatives());
        searchButton.addActionListener(e -> searchById());
        clearButton.addActionListener(e -> clearFields());
        
        buttonsPanel.add(saveButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(searchButton);
        buttonsPanel.add(clearButton);
        
        return buttonsPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 40));
        
        // Add hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private JPanel createTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(BACKGROUND_COLOR);
        
        // Table card
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CARD_COLOR);
        tableCard.setBorder(new ShadowBorder());
        
        // Table header
        JLabel tableTitle = new JLabel("📊 Cooperatives Database");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_PRIMARY);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        tableCard.add(tableTitle, BorderLayout.NORTH);
        
        // Create table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Location", "Interest Rate (%)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cooperativeTable = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(cooperativeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Add padding around table
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(CARD_COLOR);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        
        tableCard.add(tableWrapper, BorderLayout.CENTER);
        tableContainer.add(tableCard, BorderLayout.CENTER);
        
        return tableContainer;
    }

    private void styleTable() {
        cooperativeTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cooperativeTable.setRowHeight(45);
        cooperativeTable.setShowGrid(false);
        cooperativeTable.setIntercellSpacing(new Dimension(0, 0));
        cooperativeTable.setSelectionBackground(new Color(232, 245, 255));
        cooperativeTable.setSelectionForeground(TEXT_PRIMARY);
        
        // Style header
        JTableHeader header = cooperativeTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
        
        // Style cells
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 249, 249));
                }
                
                return this;
            }
        };
        
        for (int i = 0; i < cooperativeTable.getColumnCount(); i++) {
            cooperativeTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Add selection listener
        cooperativeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && cooperativeTable.getSelectedRow() != -1) {
                int row = cooperativeTable.getSelectedRow();
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                locationField.setText(tableModel.getValueAt(row, 2).toString());
                interestRateField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
    }

    // Shadow border class for card effect
    private class ShadowBorder implements javax.swing.border.Border {
        private final Color shadowColor = new Color(0, 0, 0, 20);
        private final int shadowSize = 8;
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw shadow
            g2d.setColor(shadowColor);
            for (int i = 0; i < shadowSize; i++) {
                g2d.drawRoundRect(x + i, y + i, width - 2 * i - 1, height - 2 * i - 1, 10, 10);
            }
            
            // Draw border
            g2d.setColor(BORDER_COLOR);
            g2d.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private void saveCooperative() {
        try {
            if (nameField.getText().trim().isEmpty() || locationField.getText().trim().isEmpty() || 
                interestRateField.getText().trim().isEmpty()) {
                showStyledMessage("Please fill in all required fields.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Cooperatives cooperative = new Cooperatives();
            cooperative.setName(nameField.getText().trim());
            cooperative.setLocation(locationField.getText().trim());
            cooperative.setInterestRate(Double.parseDouble(interestRateField.getText().trim()));

            service.save(cooperative);
            showStyledMessage("✅ Cooperative saved successfully!", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadCooperatives();
        } catch (NumberFormatException ex) {
            showStyledMessage("❌ Invalid interest rate: Must be a number.", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showStyledMessage("❌ Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.err.println("Save failed: " + ex.getMessage());
        }
    }

    private void updateCooperative() {
        try {
            if (idField.getText().isEmpty()) {
                showStyledMessage("Please select a cooperative to update.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nameField.getText().trim().isEmpty() || locationField.getText().trim().isEmpty() || 
                interestRateField.getText().trim().isEmpty()) {
                showStyledMessage("Please fill in all required fields.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Cooperatives cooperative = new Cooperatives();
            cooperative.setId(Integer.parseInt(idField.getText().trim()));
            cooperative.setName(nameField.getText().trim());
            cooperative.setLocation(locationField.getText().trim());
            cooperative.setInterestRate(Double.parseDouble(interestRateField.getText().trim()));

            service.update(cooperative);
            showStyledMessage("✅ Cooperative updated successfully!", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadCooperatives();
        } catch (NumberFormatException ex) {
            showStyledMessage("❌ Invalid input: ID and Interest Rate must be numbers.", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showStyledMessage("❌ Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.err.println("Update failed: " + ex.getMessage());
        }
    }

    private void deleteCooperative() {
        try {
            if (idField.getText().isEmpty()) {
                showStyledMessage("Please select a cooperative to delete.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "⚠️ Are you sure you want to delete this cooperative?\nThis action cannot be undone.", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                Cooperatives cooperative = service.findById(Integer.parseInt(idField.getText().trim()));
                if (cooperative == null) {
                    showStyledMessage("❌ Cooperative not found.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                service.delete(cooperative);
                showStyledMessage("✅ Cooperative deleted successfully!", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadCooperatives();
            }
        } catch (NumberFormatException ex) {
            showStyledMessage("❌ Invalid ID: Must be a number.", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showStyledMessage("❌ Error deleting cooperative: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.err.println("Delete failed: " + ex.getMessage());
        }
    }

    private void searchById() {
        try {
            String searchId = searchIdField.getText().trim();
            if (searchId.isEmpty()) {
                showStyledMessage("Please enter an ID to search.", JOptionPane.WARNING_MESSAGE);
                loadCooperatives();
                return;
            }
            
            int id = Integer.parseInt(searchId);
            Cooperatives cooperative = service.findById(id);
            tableModel.setRowCount(0);
            
            if (cooperative != null) {
                tableModel.addRow(new Object[]{
                    cooperative.getId(),
                    cooperative.getName() != null ? cooperative.getName() : "",
                    cooperative.getLocation() != null ? cooperative.getLocation() : "",
                    cooperative.getInterestRate()
                });
                showStyledMessage("✅ Found 1 cooperative with ID " + id, JOptionPane.INFORMATION_MESSAGE);
            } else {
                showStyledMessage("❌ No cooperative found with ID " + id, JOptionPane.INFORMATION_MESSAGE);
            }
            searchIdField.setText("");
        } catch (NumberFormatException ex) {
            showStyledMessage("❌ Invalid ID: Must be a number.", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showStyledMessage("❌ Error searching cooperative: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.err.println("Search failed: " + ex.getMessage());
        }
    }

    private void loadCooperatives() {
        try {
            tableModel.setRowCount(0);
            List<Cooperatives> cooperatives = service.findAll();
            if (cooperatives != null) {
                for (Cooperatives c : cooperatives) {
                    tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getName() != null ? c.getName() : "",
                        c.getLocation() != null ? c.getLocation() : "",
                        c.getInterestRate()
                    });
                }
            }
        } catch (Exception ex) {
            showStyledMessage("❌ Error loading cooperatives: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.err.println("Load failed: " + ex.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        interestRateField.setText("");
        searchIdField.setText("");
    }

    private void showStyledMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, 
            messageType == JOptionPane.ERROR_MESSAGE ? "Error" : 
            messageType == JOptionPane.WARNING_MESSAGE ? "Warning" : "Information", 
            messageType);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CooperativePage());
    }
}