
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Main Swing GUI for the Dry Cleaning & Laundry Service Management System.
 */
public class LaundryGUI extends JFrame {

    private LaundryManager manager;
    private JTabbedPane tabbedPane;

    // Register Order Tab Fields
    private JTextField txtCustId, txtCustName, txtCustPhone, txtOrderId, txtOrderDate, txtReadyDate;
    private JComboBox<String> cmbGarmentType, cmbServiceType, cmbPaymentStatus;
    private JTextField txtGarmentQty;
    private JTable tblTempGarments;
    private DefaultTableModel modelTempGarments;
    private List<Garment> tempGarmentsList;

    // View Orders Tab Fields
    private JTable tblOrders;
    private DefaultTableModel modelOrders;

    // Update Status Tab Fields
    private JTextField txtUpdateOrderId;
    private JComboBox<ServiceStatus> cmbUpdateStatus;
    private JComboBox<String> cmbUpdatePayment;
    private JTextArea txtStatusHistory;

    // Search Tab Fields
    private JTextField txtSearchOrderId, txtSearchCustName;
    private JTable tblSearchResults;
    private DefaultTableModel modelSearchResults;

    // Generate Bill Tab Fields
    private JTextField txtBillOrderId;
    private JTextArea txtBillReceipt;

    // Delete Tab Fields
    private JTextField txtDeleteOrderId;

    // Service Report Tab Fields
    private JLabel lblTotalOrders, lblReceivedCount, lblWashingCount, lblPressingCount, lblReadyCount, lblTotalRevenue;

    public LaundryGUI() {
        manager = new LaundryManager();
        tempGarmentsList = new ArrayList<>();

        // Frame Setup
        setTitle("Dry Cleaning & Laundry Service Management System");
        setSize(1020, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1020, 60));
        JLabel lblHeader = new JLabel("DRY CLEANING & LAUNDRY MANAGEMENT SYSTEM");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);

        // Tabbed Pane Setup
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Adding Panels/Tabs
        tabbedPane.addTab("1. Register Order", createRegisterOrderPanel());
        tabbedPane.addTab("2. View Orders", createViewOrdersPanel());
        tabbedPane.addTab("3. Update Status", createUpdateStatusPanel());
        tabbedPane.addTab("4. Search Order", createSearchOrderPanel());
        tabbedPane.addTab("5. Generate Bill", createGenerateBillPanel());
        tabbedPane.addTab("6. Delete Order", createDeleteOrderPanel());
        tabbedPane.addTab("7. Service Report", createServiceReportPanel());
        tabbedPane.addTab("8. Exit", createExitPanel());

        // Refresh tables on tab switch
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 1) { // View Orders
                refreshOrdersTable(manager.getAllOrders());
            } else if (selectedIndex == 6) { // Service Report
                refreshReportPanel();
            }
        });

        // Add to Frame
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Initialize with default Order ID & Dates
        resetRegisterForm();
    }

    // =========================================================================
    // 1. REGISTER ORDER PANEL
    // =========================================================================
    private JPanel createRegisterOrderPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Customer & Order Form Panel (Left Side)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer & Order Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        formPanel.add(new JLabel("Customer ID:"), getGbc(0, row));
        txtCustId = new JTextField(12);
        formPanel.add(txtCustId, getGbc(1, row++));

        formPanel.add(new JLabel("Customer Name:"), getGbc(0, row));
        txtCustName = new JTextField(12);
        formPanel.add(txtCustName, getGbc(1, row++));

        formPanel.add(new JLabel("Phone Number:"), getGbc(0, row));
        txtCustPhone = new JTextField(12);
        formPanel.add(txtCustPhone, getGbc(1, row++));

        formPanel.add(new JLabel("Order ID:"), getGbc(0, row));
        txtOrderId = new JTextField(12);
        formPanel.add(txtOrderId, getGbc(1, row++));

        formPanel.add(new JLabel("Order Date (YYYY-MM-DD):"), getGbc(0, row));
        txtOrderDate = new JTextField(12);
        txtOrderDate.setText(LocalDate.now().toString());
        formPanel.add(txtOrderDate, getGbc(1, row++));

        formPanel.add(new JLabel("Ready Date (YYYY-MM-DD):"), getGbc(0, row));
        txtReadyDate = new JTextField(12);
        txtReadyDate.setText(LocalDate.now().plusDays(2).toString());
        formPanel.add(txtReadyDate, getGbc(1, row++));

        formPanel.add(new JLabel("Payment Status:"), getGbc(0, row));
        cmbPaymentStatus = new JComboBox<>(new String[]{"PAID", "PENDING"});
        formPanel.add(cmbPaymentStatus, getGbc(1, row++));

        // Garment Tagging Subpanel (Right Side)
        JPanel garmentPanel = new JPanel(new BorderLayout(5, 5));
        garmentPanel.setBorder(BorderFactory.createTitledBorder("Garment Details"));

        // Top control box for garments input
        JPanel garmentInputBox = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        cmbGarmentType = new JComboBox<>(new String[]{"Shirt", "Pant", "T-Shirt", "Jacket", "Saree", "Suit"});
        cmbServiceType = new JComboBox<>(new String[]{"Washing", "Dry Cleaning", "Pressing"});
        txtGarmentQty = new JTextField("1", 4);

        row1.add(new JLabel("Type:"));
        row1.add(cmbGarmentType);
        row1.add(new JLabel("Service:"));
        row1.add(cmbServiceType);
        row1.add(new JLabel("Qty:"));
        row1.add(txtGarmentQty);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnAddGarment = new JButton("➕ Add Garment to Order");
        btnAddGarment.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAddGarment.setBackground(new Color(52, 152, 219));
        btnAddGarment.setForeground(Color.BLACK);
        row2.add(btnAddGarment);

        garmentInputBox.add(row1);
        garmentInputBox.add(row2);

        // Table for added garments
        String[] columns = {"Garment Type", "Service", "Quantity", "Unit Price", "Total (Rs.)"};
        modelTempGarments = new DefaultTableModel(columns, 0);
        tblTempGarments = new JTable(modelTempGarments);
        JScrollPane scrollGarments = new JScrollPane(tblTempGarments);

        garmentPanel.add(garmentInputBox, BorderLayout.NORTH);
        garmentPanel.add(scrollGarments, BorderLayout.CENTER);

        // Action: Add Garment
        btnAddGarment.addActionListener(e -> addSelectedGarmentToTempList());

        // Submit Button Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSubmitOrder = new JButton("Register Order");
        btnSubmitOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmitOrder.setBackground(new Color(46, 204, 113));
        btnSubmitOrder.setForeground(Color.BLACK);
        
        JButton btnClear = new JButton("Clear Form");

        bottomPanel.add(btnClear);
        bottomPanel.add(btnSubmitOrder);

        btnClear.addActionListener(e -> resetRegisterForm());

        btnSubmitOrder.addActionListener(e -> {
            try {
                String custId = txtCustId.getText().trim();
                String custName = txtCustName.getText().trim();
                String custPhone = txtCustPhone.getText().trim();
                String orderId = txtOrderId.getText().trim();
                String orderDateStr = txtOrderDate.getText().trim();
                String readyDateStr = txtReadyDate.getText().trim();
                String paymentStatus = (String) cmbPaymentStatus.getSelectedItem();

                ValidationUtils.validateCustomerName(custName);
                ValidationUtils.validatePhoneNumber(custPhone);
                ValidationUtils.validateOrderId(orderId);

                // Smart Fallback: If user forgot to click "Add Garment", add the currently selected garment automatically!
                if (tempGarmentsList.isEmpty()) {
                    addSelectedGarmentToTempList();
                }

                if (tempGarmentsList.isEmpty()) {
                    throw new IllegalArgumentException("Please add at least one garment to the order.");
                }

                LocalDate orderDate, readyDate;
                try {
                    orderDate = LocalDate.parse(orderDateStr);
                    readyDate = LocalDate.parse(readyDateStr);
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException("Invalid Date Format. Please use YYYY-MM-DD.");
                }

                Customer customer = new Customer(custId.isEmpty() ? "CUST" + (System.currentTimeMillis() % 1000) : custId, custName, custPhone);
                Order order = new Order(orderId, customer, orderDate, readyDate, paymentStatus);
                for (Garment g : tempGarmentsList) {
                    order.addGarment(g);
                }

                boolean success = manager.addOrder(order);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Order " + orderId + " Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    resetRegisterForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Order ID " + orderId + " already exists. Please use a unique Order ID.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(garmentPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void addSelectedGarmentToTempList() {
        try {
            String garmentType = (String) cmbGarmentType.getSelectedItem();
            String serviceType = (String) cmbServiceType.getSelectedItem();
            String qtyStr = txtGarmentQty.getText();

            ValidationUtils.validateQuantity(qtyStr);
            int qty = Integer.parseInt(qtyStr.trim());

            Garment garment = new Garment(garmentType, qty, serviceType);
            tempGarmentsList.add(garment);

            modelTempGarments.addRow(new Object[]{
                    garment.getGarmentType(),
                    garment.getServiceType(),
                    garment.getQuantity(),
                    garment.getUnitPrice(),
                    garment.getTotalPrice()
            });

            txtGarmentQty.setText("1");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Garment Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private GridBagConstraints getGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void resetRegisterForm() {
        int nextId = 101 + manager.getAllOrders().size();
        txtCustId.setText("CUST" + nextId);
        txtCustName.setText("");
        txtCustPhone.setText("");
        txtOrderId.setText("ORD" + nextId);
        txtOrderDate.setText(LocalDate.now().toString());
        txtReadyDate.setText(LocalDate.now().plusDays(2).toString());
        cmbPaymentStatus.setSelectedIndex(0);
        cmbGarmentType.setSelectedIndex(0);
        cmbServiceType.setSelectedIndex(0);
        txtGarmentQty.setText("1");
        tempGarmentsList.clear();
        modelTempGarments.setRowCount(0);
    }

    // =========================================================================
    // 2. VIEW ORDERS PANEL
    // =========================================================================
    private JPanel createViewOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBorder(BorderFactory.createTitledBorder("View & Sort Controls"));

        JButton btnAll = new JButton("All Orders");
        JButton btnSortDate = new JButton("Sort by Order Date");
        JButton btnSortBill = new JButton("Sort by Bill Amount");
        JButton btnTreeMap = new JButton("View Ready Date Schedule (TreeMap)");
        JButton btnRefresh = new JButton("Refresh");

        toolBar.add(btnAll);
        toolBar.add(btnSortDate);
        toolBar.add(btnSortBill);
        toolBar.add(btnTreeMap);
        toolBar.add(btnRefresh);

        String[] cols = {"Order ID", "Customer Name", "Phone", "Items Qty", "Order Date", "Ready Date", "Total Amount", "Payment", "Status Stage"};
        modelOrders = new DefaultTableModel(cols, 0);
        tblOrders = new JTable(modelOrders);
        JScrollPane scrollPane = new JScrollPane(tblOrders);

        btnAll.addActionListener(e -> refreshOrdersTable(manager.getAllOrders()));
        btnSortDate.addActionListener(e -> refreshOrdersTable(manager.getOrdersSortedByOrderDate()));
        btnSortBill.addActionListener(e -> refreshOrdersTable(manager.getOrdersSortedByBillAmount()));
        btnRefresh.addActionListener(e -> refreshOrdersTable(manager.getAllOrders()));

        btnTreeMap.addActionListener(e -> {
            TreeMap<LocalDate, List<Order>> readyMap = manager.getReadyDateMap();
            StringBuilder sb = new StringBuilder();
            sb.append("=== ORDERS SORTED BY READY DATE (TreeMap) ===\n\n");
            for (Map.Entry<LocalDate, List<Order>> entry : readyMap.entrySet()) {
                sb.append("Ready Date: ").append(entry.getKey()).append("\n");
                for (Order o : entry.getValue()) {
                    sb.append("   - ").append(o.getOrderId()).append(" | ").append(o.getCustomer().getName())
                      .append(" | ").append(o.getStatus()).append(" | Rs. ").append(o.calculateTotalAmount()).append("\n");
                }
                sb.append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            JScrollPane sp = new JScrollPane(textArea);
            sp.setPreferredSize(new Dimension(500, 350));
            JOptionPane.showMessageDialog(this, sp, "TreeMap Ready Date Schedule", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshOrdersTable(List<Order> list) {
        modelOrders.setRowCount(0);
        for (Order o : list) {
            modelOrders.addRow(new Object[]{
                    o.getOrderId(),
                    o.getCustomer().getName(),
                    o.getCustomer().getPhoneNumber(),
                    o.getTotalGarmentCount(),
                    o.getOrderDate(),
                    o.getReadyDate(),
                    "Rs. " + String.format("%.2f", o.calculateTotalAmount()),
                    o.getPaymentStatus(),
                    o.getStatus()
            });
        }
    }

    // =========================================================================
    // 3. UPDATE STATUS PANEL
    // =========================================================================
    private JPanel createUpdateStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        formPanel.setBorder(BorderFactory.createTitledBorder("Update Stage / Status"));

        txtUpdateOrderId = new JTextField(10);
        cmbUpdateStatus = new JComboBox<>(ServiceStatus.values());
        cmbUpdatePayment = new JComboBox<>(new String[]{"PAID", "PENDING"});
        JButton btnUpdateStatus = new JButton("Update Status");
        JButton btnViewHistory = new JButton("View Stage History");

        formPanel.add(new JLabel("Order ID:"));
        formPanel.add(txtUpdateOrderId);
        formPanel.add(new JLabel("New Stage:"));
        formPanel.add(cmbUpdateStatus);
        formPanel.add(new JLabel("Payment Status:"));
        formPanel.add(cmbUpdatePayment);
        formPanel.add(btnUpdateStatus);
        formPanel.add(btnViewHistory);

        txtStatusHistory = new JTextArea();
        txtStatusHistory.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtStatusHistory.setEditable(false);
        JScrollPane scrollHistory = new JScrollPane(txtStatusHistory);
        scrollHistory.setBorder(BorderFactory.createTitledBorder("Stage History Tracking (LinkedList)"));

        btnUpdateStatus.addActionListener(e -> {
            String orderId = txtUpdateOrderId.getText().trim();
            ServiceStatus newStatus = (ServiceStatus) cmbUpdateStatus.getSelectedItem();
            String newPayment = (String) cmbUpdatePayment.getSelectedItem();

            if (orderId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Order ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Order order = manager.getOrderById(orderId);
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Order ID " + orderId + " not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.updateOrderStatus(orderId, newStatus);
            manager.updatePaymentStatus(orderId, newPayment);

            JOptionPane.showMessageDialog(this, "Order " + orderId + " status updated to " + newStatus, "Status Updated", JOptionPane.INFORMATION_MESSAGE);

            // Pickup Notification popup when status becomes READY
            if (newStatus == ServiceStatus.READY) {
                JOptionPane.showMessageDialog(this,
                        "🔔 PICKUP NOTIFICATION:\nOrder " + orderId + " (" + order.getCustomer().getName() + ") is ready for pickup!",
                        "Pickup Notification",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            displayStatusHistory(order);
        });

        btnViewHistory.addActionListener(e -> {
            String orderId = txtUpdateOrderId.getText().trim();
            if (orderId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Order ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Order order = manager.getOrderById(orderId);
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Order ID " + orderId + " not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            displayStatusHistory(order);
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollHistory, BorderLayout.CENTER);

        return panel;
    }

    private void displayStatusHistory(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID       : ").append(order.getOrderId()).append("\n");
        sb.append("Customer Name  : ").append(order.getCustomer().getName()).append("\n");
        sb.append("Current Stage  : ").append(order.getStatus()).append("\n");
        sb.append("Payment Status : ").append(order.getPaymentStatus()).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("PROCESSING STAGE HISTORY (LinkedList):\n");
        sb.append("--------------------------------------------------\n");

        int step = 1;
        for (ServiceStatus stage : order.getStatusHistory()) {
            sb.append("Step ").append(step++).append(" : ").append(stage).append("\n");
        }

        txtStatusHistory.setText(sb.toString());
    }

    // =========================================================================
    // 4. SEARCH ORDER PANEL
    // =========================================================================
    private JPanel createSearchOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchBarPanel.setBorder(BorderFactory.createTitledBorder("Search Filters"));

        txtSearchOrderId = new JTextField(10);
        txtSearchCustName = new JTextField(12);
        JButton btnSearchId = new JButton("Search by Order ID (HashMap)");
        JButton btnSearchName = new JButton("Search by Customer Name");

        searchBarPanel.add(new JLabel("Order ID:"));
        searchBarPanel.add(txtSearchOrderId);
        searchBarPanel.add(btnSearchId);

        searchBarPanel.add(new JLabel("Customer Name:"));
        searchBarPanel.add(txtSearchCustName);
        searchBarPanel.add(btnSearchName);

        String[] cols = {"Order ID", "Customer Name", "Phone", "Items Count", "Order Date", "Ready Date", "Total Amount", "Payment", "Status"};
        modelSearchResults = new DefaultTableModel(cols, 0);
        tblSearchResults = new JTable(modelSearchResults);
        JScrollPane scrollPane = new JScrollPane(tblSearchResults);

        btnSearchId.addActionListener(e -> {
            String orderId = txtSearchOrderId.getText().trim();
            modelSearchResults.setRowCount(0);
            if (orderId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Order ID to search.", "Input Needed", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Order order = manager.getOrderById(orderId);
            if (order != null) {
                addOrderToSearchTable(order);
            } else {
                JOptionPane.showMessageDialog(this, "No order found with Order ID: " + orderId, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnSearchName.addActionListener(e -> {
            String custName = txtSearchCustName.getText().trim();
            modelSearchResults.setRowCount(0);
            if (custName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Customer Name to search.", "Input Needed", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<Order> matches = manager.getOrdersByCustomerName(custName);
            if (!matches.isEmpty()) {
                for (Order o : matches) {
                    addOrderToSearchTable(o);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No orders found for customer: " + custName, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(searchBarPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addOrderToSearchTable(Order o) {
        modelSearchResults.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomer().getName(),
                o.getCustomer().getPhoneNumber(),
                o.getTotalGarmentCount(),
                o.getOrderDate(),
                o.getReadyDate(),
                "Rs. " + String.format("%.2f", o.calculateTotalAmount()),
                o.getPaymentStatus(),
                o.getStatus()
        });
    }

    // =========================================================================
    // 5. GENERATE BILL PANEL
    // =========================================================================
    private JPanel createGenerateBillPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topBar.setBorder(BorderFactory.createTitledBorder("Bill Generation"));

        txtBillOrderId = new JTextField(10);
        JButton btnGenerateBill = new JButton("Generate Bill Receipt");

        topBar.add(new JLabel("Enter Order ID:"));
        topBar.add(txtBillOrderId);
        topBar.add(btnGenerateBill);

        txtBillReceipt = new JTextArea();
        txtBillReceipt.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBillReceipt.setEditable(false);
        JScrollPane scrollReceipt = new JScrollPane(txtBillReceipt);
        scrollReceipt.setBorder(BorderFactory.createTitledBorder("Receipt Output (StringBuilder)"));

        btnGenerateBill.addActionListener(e -> {
            String orderId = txtBillOrderId.getText().trim();
            if (orderId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Order ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Order order = manager.getOrderById(orderId);
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Order ID " + orderId + " not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Bill bill = new Bill("BILL-" + orderId, order);
            txtBillReceipt.setText(bill.generateReceipt());
        });

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(scrollReceipt, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // 6. DELETE ORDER PANEL
    // =========================================================================
    private JPanel createDeleteOrderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel deleteCard = new JPanel(new GridLayout(3, 1, 10, 10));
        deleteCard.setBorder(BorderFactory.createTitledBorder("Delete Order Record"));
        deleteCard.setPreferredSize(new Dimension(450, 180));

        JPanel line1 = new JPanel(new FlowLayout());
        txtDeleteOrderId = new JTextField(12);
        line1.add(new JLabel("Order ID to Delete:"));
        line1.add(txtDeleteOrderId);

        JButton btnDelete = new JButton("Delete Order");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.BLACK);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 13));

        deleteCard.add(line1);
        deleteCard.add(btnDelete);

        btnDelete.addActionListener(e -> {
            String orderId = txtDeleteOrderId.getText().trim();
            if (orderId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Order ID to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Order order = manager.getOrderById(orderId);
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Order ID " + orderId + " not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete order " + orderId + " (" + order.getCustomer().getName() + ")?",
                    "Confirm Order Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteOrder(orderId);
                JOptionPane.showMessageDialog(this, "Order " + orderId + " deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                txtDeleteOrderId.setText("");
                refreshOrdersTable(manager.getAllOrders());
            }
        });

        panel.add(deleteCard);
        return panel;
    }

    // =========================================================================
    // 7. SERVICE REPORT PANEL
    // =========================================================================
    private JPanel createServiceReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 15, 15));

        lblTotalOrders = createReportCard("Total Orders", "0", new Color(52, 152, 219), cardsPanel);
        lblReceivedCount = createReportCard("RECEIVED Orders", "0", new Color(241, 196, 15), cardsPanel);
        lblWashingCount = createReportCard("WASHING Orders", "0", new Color(155, 89, 182), cardsPanel);
        lblPressingCount = createReportCard("PRESSING Orders", "0", new Color(230, 126, 34), cardsPanel);
        lblReadyCount = createReportCard("READY Orders", "0", new Color(46, 204, 113), cardsPanel);
        lblTotalRevenue = createReportCard("Total Revenue", "Rs. 0.00", new Color(39, 174, 96), cardsPanel);

        JButton btnRefreshReport = new JButton("Refresh Service Report");
        btnRefreshReport.addActionListener(e -> refreshReportPanel());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(btnRefreshReport);

        panel.add(cardsPanel, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createReportCard(String title, String initialVal, Color bg, JPanel parent) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(Color.WHITE);

        JLabel valLbl = new JLabel(initialVal, SwingConstants.CENTER);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLbl.setForeground(Color.WHITE);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLbl, BorderLayout.CENTER);
        parent.add(card);

        return valLbl;
    }

    private void refreshReportPanel() {
        Map<String, Object> report = manager.generateServiceReport();
        lblTotalOrders.setText(String.valueOf(report.get("Total Orders")));
        lblReceivedCount.setText(String.valueOf(report.get("RECEIVED Stage")));
        lblWashingCount.setText(String.valueOf(report.get("WASHING Stage")));
        lblPressingCount.setText(String.valueOf(report.get("PRESSING Stage")));
        lblReadyCount.setText(String.valueOf(report.get("READY Stage")));
        lblTotalRevenue.setText("Rs. " + report.get("Total Revenue (Rs.)"));
    }

    // =========================================================================
    // 8. EXIT PANEL
    // =========================================================================
    private JPanel createExitPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        JButton btnExit = new JButton("Exit Application");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExit.setBackground(new Color(192, 57, 43));
        btnExit.setForeground(Color.BLACK);
        btnExit.setPreferredSize(new Dimension(200, 50));

        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit System", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        panel.add(btnExit);
        return panel;
    }
}
