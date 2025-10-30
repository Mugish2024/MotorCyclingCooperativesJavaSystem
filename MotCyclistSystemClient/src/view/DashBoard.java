package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import view.LoginFrame; // Import LoginFrame for logout
import model.Admin; // Import Admin model
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DashBoard extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel statusLabel;
    private JButton currentActiveButton;
    private final Admin admin; // Store logged-in admin

    public DashBoard(Admin admin) {
        this.admin = admin;
        setTitle("Cooperative System Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("Cooperative System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Welcome, " + admin.getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(200, 200, 200));
        headerPanel.add(userLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content area
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Color.WHITE);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Menu items
        String[] menuItems = {"Cooperatives", "Riders", "Savings", "Transactions", "Loans", "Logout"};
        String[] iconNames = {"🏢", "🏍️", "💰", "💳", "🏦", "🚪"};
        String[] panelNames = {"COOPERATIVES", "RIDERS", "SAVINGS", "TRANSACTIONS", "LOANS", "LOGOUT"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createSidebarButton(menuItems[i], iconNames[i]);
            final String panelName = panelNames[i];
            button.addActionListener(e -> {
                if ("LOGOUT".equals(panelName)) {
                    int confirm = JOptionPane.showConfirmDialog(
                        this, 
                        "Are you sure you want to logout?", 
                        "Confirm Logout", 
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        dispose();
                        new LoginFrame().setVisible(true); // Return to login
                    }
                } else {
                    showPanel(panelName);
                    if (currentActiveButton != null) {
                        currentActiveButton.setBackground(new Color(44, 62, 80));
                    }
                    button.setBackground(new Color(52, 152, 219));
                    currentActiveButton = button;
                }
            });
            sidebar.add(button);
            if (i < menuItems.length - 1) {
                sidebar.add(Box.createVerticalStrut(5));
            }
        }

        sidebar.add(Box.createVerticalGlue());
        contentArea.add(sidebar, BorderLayout.WEST);

        // Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        // Add content cards
        contentPanel.add(createContentCard("Cooperatives", "Manage cooperative organizations", new Color(52, 152, 219)), "COOPERATIVES");
        contentPanel.add(createContentCard("Riders", "Manage rider information", new Color(155, 89, 182)), "RIDERS");
        contentPanel.add(createContentCard("Savings", "View and manage savings accounts", new Color(46, 204, 113)), "SAVINGS");
        contentPanel.add(createContentCard("Transactions", "Process financial transactions", new Color(241, 196, 15)), "TRANSACTIONS");
        contentPanel.add(createContentCard("Loans", "Manage loan applications", new Color(231, 76, 60)), "LOANS");

        contentArea.add(contentPanel, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        statusBar.setPreferredSize(new Dimension(getWidth(), 30));

        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel connectionLabel = new JLabel("Connected to localhost");
        connectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        connectionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(connectionLabel, BorderLayout.EAST);

        contentArea.add(statusBar, BorderLayout.SOUTH);
        mainPanel.add(contentArea, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createSidebarButton(String text, String icon) {
        JButton button = new JButton("   " + icon + "  " + text); // Fixed: Use JButton
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(44, 62, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(220, 45)); // Use preferredSize
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(new Color(52, 73, 94));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(new Color(44, 62, 80));
                }
            }
        });

        return button;
    }

    private JPanel createContentCard(String title, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(color);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(120, 120, 120));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);

        header.add(textPanel, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // Content placeholder
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(245, 247, 250));
        content.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel placeholder = new JLabel(title + " content will appear here");
        placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        placeholder.setForeground(new Color(150, 150, 150));
        content.add(placeholder);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        statusLabel.setText(" Showing " + panelName.toLowerCase());
    }
}