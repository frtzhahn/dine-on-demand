import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DineOnDemandApp extends JFrame {
    private final OrderManager orderManager;
    private final List<MenuItem> menuItems;
    
    // UI Colors - FlatLaf Compatible Palette
    private static final Color PRIMARY_BG = new Color(245, 246, 250);
    private static final Color SECONDARY_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);

    // Fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    // UI Components
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JComboBox<String> discountCombo;
    private JRadioButton dineInButton;
    private JRadioButton takeOutButton;

    public DineOnDemandApp() {
        // Initialize FlatLaf for a modern look
        try {
            FlatLightLaf.setup();
            // Global UI Customizations
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Table.selectionBackground", new Color(232, 244, 253));
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 10);
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        this.orderManager = new OrderManager();
        this.menuItems = MenuData.getInitialMenu();
        
        initializeUI();
    }

    private void initializeUI() {
        setTitle("DineOn-Demand POS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        getContentPane().setBackground(PRIMARY_BG);
        setLayout(new BorderLayout(20, 20));

        // Header Panel
        add(createHeader(), BorderLayout.NORTH);

        // LEFT: Menu Panel (with padding)
        JPanel menuContainer = new JPanel(new BorderLayout());
        menuContainer.setOpaque(false);
        menuContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0));
        
        JPanel menuPanel = createMenuPanel();
        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.setBorder(null);
        menuScroll.getViewport().setOpaque(false);
        menuScroll.setOpaque(false);
        menuContainer.add(menuScroll, BorderLayout.CENTER);
        
        add(menuContainer, BorderLayout.CENTER);

        // RIGHT: Order Summary Panel
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(SECONDARY_BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 221, 225)));
        JLabel footerLabel = new JLabel("DineOn-Demand: A New Era in Meal Accessibility");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footerLabel.setForeground(TEXT_LIGHT);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SECONDARY_BG);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 221, 225)));

        JLabel logo = new JLabel("  DINEON-DEMAND");
        logo.setFont(TITLE_FONT);
        logo.setForeground(ACCENT_COLOR);
        header.add(logo, BorderLayout.WEST);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        info.setOpaque(false);
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(NORMAL_FONT);
        dateLabel.setForeground(TEXT_LIGHT);
        info.add(dateLabel);
        header.add(info, BorderLayout.EAST);

        return header;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 3, 15, 15));
        panel.setOpaque(false);

        for (MenuItem item : menuItems) {
            JButton itemButton = createMenuItemButton(item);
            panel.add(itemButton);
        }
        return panel;
    }

    private JButton createMenuItemButton(MenuItem item) {
        String content = String.format(
            "<html><div style='text-align: center; width: 100px;'>" +
            "<span style='font-size: 11px; color: #7f8c8d;'>%s</span><br>" +
            "<b style='font-size: 13px; color: #2c3e50;'>%s</b><br>" +
            "<span style='font-size: 14px; color: #3498db;'>₱%.2f</span>" +
            "</div></html>", 
            item.getCategory(), item.getName(), item.getPrice()
        );
        
        JButton button = new JButton(content);
        button.setPreferredSize(new Dimension(140, 110));
        button.setBackground(SECONDARY_BG);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 248, 255));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_BG);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });

        button.addActionListener(e -> addItemToOrder(item));
        return button;
    }

    private JPanel createRightPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 0));
        container.setPreferredSize(new Dimension(420, 0));
        container.setBackground(SECONDARY_BG);
        container.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(220, 221, 225)));

        JLabel title = new JLabel("Current Order");
        title.setFont(SUBTITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        container.add(title, BorderLayout.NORTH);

        // Table Styling
        String[] columnNames = {"Item", "Price", "Qty", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        orderTable = new JTable(tableModel);
        styleTable(orderTable);
        
        JScrollPane tableScroll = new JScrollPane(orderTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        tableScroll.getViewport().setBackground(SECONDARY_BG);
        container.add(tableScroll, BorderLayout.CENTER);

        // Controls
        JPanel controls = new JPanel(new GridBagLayout());
        controls.setOpaque(false);
        controls.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        // Order Mode
        gbc.gridy = 0;
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        modePanel.setOpaque(false);
        dineInButton = new JRadioButton("Dine-In", true);
        takeOutButton = new JRadioButton("Take-Out");
        dineInButton.setOpaque(false);
        takeOutButton.setOpaque(false);
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(dineInButton);
        modeGroup.add(takeOutButton);
        modePanel.add(dineInButton);
        modePanel.add(new Box.Filler(new Dimension(20, 0), new Dimension(20, 0), new Dimension(20, 0)));
        modePanel.add(takeOutButton);
        controls.add(modePanel, gbc);

        // Discounts
        gbc.gridy = 1;
        JPanel discPanel = new JPanel(new BorderLayout(10, 0));
        discPanel.setOpaque(false);
        JLabel discIco = new JLabel("Discount Type:");
        discIco.setFont(NORMAL_FONT);
        discPanel.add(discIco, BorderLayout.WEST);
        discountCombo = new JComboBox<>(new String[]{"None", "Senior Citizen (20%)", "PWD (20%)", "Promo Code (10%)"});
        discountCombo.addActionListener(this::applyDiscountShortcut);
        discPanel.add(discountCombo, BorderLayout.CENTER);
        controls.add(discPanel, gbc);

        // Totals
        gbc.gridy = 2;
        JPanel totalsPanel = new JPanel(new GridLayout(3, 2, 0, 5));
        totalsPanel.setOpaque(false);
        totalsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(240, 240, 240)),
            BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));

        subtotalLabel = createTotalLabel("₱0.00", false);
        discountLabel = createTotalLabel("₱0.00", false);
        totalLabel = createTotalLabel("₱0.00", true);
        
        totalsPanel.add(new JLabel("Subtotal:"));
        totalsPanel.add(subtotalLabel);
        totalsPanel.add(new JLabel("Discount:"));
        totalsPanel.add(discountLabel);
        JLabel totalText = new JLabel("Total Amount:");
        totalText.setFont(SUBTITLE_FONT);
        totalsPanel.add(totalText);
        totalsPanel.add(totalLabel);
        controls.add(totalsPanel, gbc);

        // Actions
        gbc.gridy = 3;
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        actionPanel.setOpaque(false);
        
        JButton clearButton = new JButton("Clear Order");
        styleSecondaryButton(clearButton);
        clearButton.addActionListener(e -> clearOrder());
        
        JButton checkoutButton = new JButton("Checkout");
        stylePrimaryButton(checkoutButton);
        checkoutButton.addActionListener(e -> processCheckout());
        
        actionPanel.add(clearButton);
        actionPanel.add(checkoutButton);
        controls.add(actionPanel, gbc);

        container.add(controls, BorderLayout.SOUTH);
        return container;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(NORMAL_FONT);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setBackground(SECONDARY_BG);
        header.setForeground(TEXT_LIGHT);
        header.setFont(SUBTITLE_FONT);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Qty
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer); // Price
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Total
    }

    private JLabel createTotalLabel(String text, boolean isMain) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(isMain ? TITLE_FONT : SUBTITLE_FONT);
        if (isMain) label.setForeground(ACCENT_COLOR);
        return label;
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(SECONDARY_BG);
        button.setForeground(DANGER_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.putClientProperty("JButton.outlineColor", DANGER_COLOR);
    }

    private void addItemToOrder(MenuItem item) {
        orderManager.addItem(item, 1);
        updateOrderDisplay();
    }

    private void updateOrderDisplay() {
        tableModel.setRowCount(0);
        for (OrderLine line : orderManager.getItems()) {
            tableModel.addRow(new Object[]{
                line.getItem().getName(),
                String.format("%.2f", line.getItem().getPrice()),
                line.getQuantity(),
                String.format("%.2f", line.getSubtotal())
            });
        }
        
        subtotalLabel.setText(String.format("₱%.2f", orderManager.calculateSubtotal()));
        discountLabel.setText(String.format("-₱%.2f", orderManager.calculateDiscountAmount()));
        totalLabel.setText(String.format("₱%.2f", orderManager.calculateTotal()));
    }

    private void applyDiscountShortcut(ActionEvent e) {
        String selected = (String) discountCombo.getSelectedItem();
        if (selected.equals("None")) {
            orderManager.setDiscount("None", 0.0);
        } else if (selected.contains("Senior") || selected.contains("PWD")) {
            orderManager.setDiscount(selected, 0.20);
        } else if (selected.contains("Promo")) {
            orderManager.setDiscount("Promo", 0.10);
        }
        updateOrderDisplay();
    }

    private void clearOrder() {
        orderManager.clear();
        discountCombo.setSelectedIndex(0);
        dineInButton.setSelected(true);
        updateOrderDisplay();
    }

    private void processCheckout() {
        if (orderManager.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Order is empty!", "Empty Order", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = {"Cash", "GCash", "Card"};
        int response = JOptionPane.showOptionDialog(this, "Choose Payment Method", "Payment",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (response == -1) return;

        String paymentMethod = options[response];
        String mockDetails = "";

        if (paymentMethod.equals("GCash")) {
            mockDetails = JOptionPane.showInputDialog(this, "Enter GCash Number (e.g., 0912xxxxxxx):");
            if (mockDetails == null || mockDetails.trim().isEmpty()) return;
        } else if (paymentMethod.equals("Card")) {
            mockDetails = JOptionPane.showInputDialog(this, "Enter Card Number (Mock):");
            if (mockDetails == null || mockDetails.trim().isEmpty()) return;
        }

        showReceipt(paymentMethod, mockDetails);
    }

    private void showReceipt(String method, String details) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("-----------------------------\n");
        receipt.append("      DINEON-DEMAND         \n");
        receipt.append("-----------------------------\n");
        receipt.append("Mode: ").append(orderManager.isDineIn() ? "DINE-IN" : "TAKE-OUT").append("\n");
        receipt.append("-----------------------------\n");

        for (OrderLine line : orderManager.getItems()) {
            receipt.append(String.format("%-15s x%d  ₱%7.2f\n", 
                line.getItem().getName(), line.getQuantity(), line.getSubtotal()));
        }

        receipt.append("-----------------------------\n");
        receipt.append(String.format("Subtotal:         ₱%8.2f\n", orderManager.calculateSubtotal()));
        if (!orderManager.getDiscountType().equals("None")) {
            receipt.append(String.format("Discount (%s):   -₱%8.2f\n", 
                orderManager.getDiscountType(), orderManager.calculateDiscountAmount()));
        }
        receipt.append(String.format("TOTAL:            ₱%8.2f\n", orderManager.calculateTotal()));
        receipt.append("-----------------------------\n");
        receipt.append("Payment: ").append(method).append("\n");
        if (!details.isEmpty()) receipt.append("Details: ").append(details).append("\n");
        receipt.append("-----------------------------\n");
        receipt.append("      Thank You for dining!  \n");

        JTextArea textArea = new JTextArea(receipt.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
        
        clearOrder();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DineOnDemandApp().setVisible(true);
        });
    }
}
