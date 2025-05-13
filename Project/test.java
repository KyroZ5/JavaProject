package Project;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class test extends JFrame implements ActionListener, ItemListener, ChangeListener {

    // Global background color
    private Color myColor = new Color(193, 234, 242);

    // ---------------------------
    // Main Containers
    // ---------------------------
    private JPanel headerPanel;  // Spans the top of the frame – contains logo and date/time
    private JPanel leftPanel;    // Contains Transaction Table
    private JPanel rightPanel;   // Contains Payment Panel (top) & Receipt Panel (bottom)

    // ---------------------------
    // Header Panel Components
    // ---------------------------
    private JLabel bLogo;       // Scaled logo
    private JLabel lblDate;     // Date label (e.g., "2025-05-13")
    private JLabel lblTime;     // Time label (e.g., "05:00 PM")

    // ---------------------------
    // Left Panel: Transaction Table
    // ---------------------------
    private JPanel transactionPanel;
    private DefaultTableModel transactionModel;
    private JTable transactionTable;
    // (Increase/Decrease buttons removed as per request)

    // ---------------------------
    // Right Panel: Payment and Receipt Panels
    // ---------------------------
    // Payment Panel (top of right panel)
    private JPanel paymentPanel;
    // Payment Sub-panels:
    //   - Barcode Sub-panel
    //   - Quantity Adjustment Sub-panel
    //   - Cash Input Sub-panel
    //   - Number Pad Sub-panel
    //   - Logout Sub-panel
    private JPanel barcodePanel;
    private JLabel lblBarcode = new JLabel("Barcode:");
    private JTextField txtBarcode = new JTextField(15);
    private JButton btnAddItem = new JButton("Add Item");
    private JButton btnDeleteItem = new JButton("Delete Item");

    // Quantity Adjustment Sub-panel
    private JPanel qtyPanel;
    private JLabel lblQty = new JLabel("Adjust Qty:");
    private JTextField txtQty = new JTextField(5);
    private JButton btnApplyQty = new JButton("Apply Qty");

    // Cash Input Sub-panel
    private JPanel cashInputPanel;
    private JLabel lblTotal = new JLabel("Total: ₱0.00");
    private JLabel lblCashReceived = new JLabel("Cash Received:");
    private JTextField txtCashReceived = new JTextField(10);
    private JButton btnProcessPayment = new JButton("Process Payment");
    private JButton btnReset = new JButton("Reset");
    private JLabel lblBalance = new JLabel("Balance: ₱0.00");

    // Number Pad Sub-panel
    private JPanel numPadPanel;

    // Logout Sub-panel
    private JPanel logoutPanel;
    private JButton btnLogout = new JButton("Logout");

    // Receipt Panel (bottom of right panel)
    private JPanel receiptPanel;
    private JTextArea receiptArea = new JTextArea();
    private JScrollPane receiptScroll = new JScrollPane(receiptArea);
    private JButton btnSaveReceipt = new JButton("Save Receipt as Image");

    // ---------------------------
    // Inventory Data
    // ---------------------------
    private Map<String, InventoryItem> inventoryMap = new HashMap<>();

    // Inner class representing an inventory item.
    class InventoryItem {
        String barcode;
        String itemName;
        int stock;
        double price;
        InventoryItem(String barcode, String itemName, int stock, double price) {
            this.barcode = barcode;
            this.itemName = itemName;
            this.stock = stock;
            this.price = price;
        }
    }

    // Load and scale the logo image using your code.
    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
    ImageIcon BLogo = new ImageIcon("./img/logo-dark-transparent.png");
    Image img = BLogo.getImage();
    Image newLogo = img.getScaledInstance(350, 80, Image.SCALE_SMOOTH);
    ImageIcon Logo = new ImageIcon(newLogo);

    public test() {
        // Frame Settings
        setTitle("Pentagram POS (Point-of-Sale) System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage(new ImageIcon("./img/logo-dark-transparent.png").getImage());
        getContentPane().setBackground(myColor);
        setLayout(new BorderLayout(5, 5));

        // ---------------------------
        // Header Panel: Spanning the top of the frame
        // ---------------------------
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(myColor);
        
        // Left side of Header: Scaled Logo
        bLogo = new JLabel(Logo);
        bLogo.setPreferredSize(new Dimension(375, 105));
        headerPanel.add(bLogo, BorderLayout.WEST);
        
        // Right side of Header: Date and Time split vertically
        JPanel dtPanel = new JPanel();
        dtPanel.setLayout(new BoxLayout(dtPanel, BoxLayout.Y_AXIS));
        dtPanel.setBackground(myColor);
        lblDate = new JLabel();
        lblDate.setFont(new Font("Arial", Font.BOLD, 16));
        lblDate.setForeground(Color.BLACK);
        lblDate.setAlignmentX(Component.RIGHT_ALIGNMENT);
        lblTime = new JLabel();
        lblTime.setFont(new Font("Arial", Font.BOLD, 16));
        lblTime.setForeground(Color.BLACK);
        lblTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        dtPanel.add(lblDate);
        dtPanel.add(lblTime);
        headerPanel.add(dtPanel, BorderLayout.EAST);
        
        // Timer to update the date and time every second in 12-hr format
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String time = new SimpleDateFormat("hh:mm a").format(new Date());
                lblDate.setText(date);
                lblTime.setText(time);
            }
        });
        timer.start();
        
        add(headerPanel, BorderLayout.NORTH);
        
        // ---------------------------
        // Left Panel: Transaction Table
        // ---------------------------
        leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(myColor);
        
        transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBackground(myColor);
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Transaction"));
        transactionModel = new DefaultTableModel(new String[]{"Barcode", "Item Name", "Qty", "Price (₱)", "Subtotal (₱)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        transactionTable = new JTable(transactionModel);
        transactionTable.setRowHeight(25);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < transactionTable.getColumnCount(); i++) {
            transactionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane transScroll = new JScrollPane(transactionTable);
        transactionPanel.add(transScroll, BorderLayout.CENTER);
        leftPanel.add(transactionPanel, BorderLayout.CENTER);
        
        // ---------------------------
        // Right Panel: Payment (Top) and Receipt (Bottom)
        // ---------------------------
        rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(myColor);
        
        // Payment Panel
        paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment"));
        paymentPanel.setBackground(myColor);
        
        // Barcode Sub-panel
        barcodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        barcodePanel.setBackground(myColor);
        barcodePanel.add(lblBarcode);
        barcodePanel.add(txtBarcode);
        barcodePanel.add(btnAddItem);
        barcodePanel.add(btnDeleteItem);
        txtBarcode.addActionListener(this);
        btnAddItem.addActionListener(this);
        btnDeleteItem.addActionListener(this);
        
        // Quantity Adjustment Sub-panel
        qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        qtyPanel.setBackground(myColor);
        qtyPanel.add(lblQty);
        qtyPanel.add(txtQty);
        qtyPanel.add(btnApplyQty);
        btnApplyQty.addActionListener(this);
        
        // Cash Input Sub-panel
        cashInputPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        cashInputPanel.setBackground(myColor);
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(myColor);
        totalPanel.add(lblTotal);
        JPanel cashReceivedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cashReceivedPanel.setBackground(myColor);
        cashReceivedPanel.add(lblCashReceived);
        cashReceivedPanel.add(txtCashReceived);
        cashReceivedPanel.add(btnProcessPayment);
        cashReceivedPanel.add(btnReset);
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balancePanel.setBackground(myColor);
        balancePanel.add(lblBalance);
        cashInputPanel.add(totalPanel);
        cashInputPanel.add(cashReceivedPanel);
        cashInputPanel.add(balancePanel);
        btnProcessPayment.addActionListener(this);
        btnReset.addActionListener(this);
        
        // Number Pad Sub-panel with Larger Buttons
        numPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        numPadPanel.setBackground(myColor);
        String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "Clear"};
        for (String key : keys) {
            JButton btn = new JButton(key);
            btn.setFont(new Font("Arial", Font.BOLD, 24));
            btn.setPreferredSize(new Dimension(80, 80));
            btn.setFocusable(false);
            btn.addActionListener(this);
            numPadPanel.add(btn);
        }
        
        // Logout Sub-panel with extra vertical space
        logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(myColor);
        logoutPanel.add(Box.createVerticalStrut(20));
        logoutPanel.add(btnLogout);
        btnLogout.addActionListener(this);
        
        // Assemble the Payment Panel
        paymentPanel.add(barcodePanel);
        paymentPanel.add(qtyPanel);
        paymentPanel.add(cashInputPanel);
        paymentPanel.add(numPadPanel);
        paymentPanel.add(Box.createVerticalStrut(20));
        paymentPanel.add(logoutPanel);
        
        // Receipt Panel
        receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createTitledBorder("Receipt"));
        receiptPanel.setBackground(myColor);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        JScrollPane recScroll = new JScrollPane(receiptArea);
        recScroll.getViewport().setBackground(myColor);
        receiptPanel.add(recScroll, BorderLayout.CENTER);
        receiptPanel.add(btnSaveReceipt, BorderLayout.SOUTH);
        btnSaveReceipt.addActionListener(this);
        
        // Add Payment Panel (top) and Receipt Panel (bottom) to Right Panel
        rightPanel.add(paymentPanel, BorderLayout.NORTH);
        rightPanel.add(receiptPanel, BorderLayout.CENTER);
        
        // ---------------------------
        // Main Frame Layout: Add Left and Right Panels
        // ---------------------------
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
        // Load Inventory Data
        loadInventoryData();
        
        // Initialize Receipt Header
        receiptArea.setText("               Pentagram POS Receipt\n");
        receiptArea.append("-------------------------------------------------------------\n");
    }
    
    // ---------------------------
    // Inventory Data Methods
    // ---------------------------
    private void loadInventoryData() {
        inventoryMap.clear();
        String filename = "inventory.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length == 4) {
                    String barcode = parts[0].trim();
                    String itemName = parts[1].trim();
                    int stock = Integer.parseInt(parts[2].trim());
                    double price = Double.parseDouble(parts[3].trim());
                    inventoryMap.put(barcode, new InventoryItem(barcode, itemName, stock, price));
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveInventoryData() {
        String filename = "inventory.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for(InventoryItem item : inventoryMap.values()) {
                out.println(item.barcode + "," + item.itemName + "," + item.stock + "," + item.price);
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // ---------------------------
    // Transaction Methods
    // ---------------------------
    private void addItemToTransaction(String barcode) {
        if(barcode.isEmpty()) return;
        if(!inventoryMap.containsKey(barcode)) {
            JOptionPane.showMessageDialog(this, "Item not found in inventory!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        InventoryItem item = inventoryMap.get(barcode);
        if(item.stock <= 0) {
            JOptionPane.showMessageDialog(this, "This item is out of stock!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int rowIndex = -1;
        for(int i = 0; i < transactionModel.getRowCount(); i++) {
            if(transactionModel.getValueAt(i, 0).toString().equals(barcode)) {
                rowIndex = i;
                break;
            }
        }
        int newQty = (rowIndex != -1) ? Integer.parseInt(transactionModel.getValueAt(rowIndex, 2).toString()) + 1 : 1;
        if(newQty > item.stock) {
            JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + item.stock, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(rowIndex != -1) {
            transactionModel.setValueAt(newQty, rowIndex, 2);
            double subtotal = newQty * item.price;
            transactionModel.setValueAt(String.format("₱%.2f", subtotal), rowIndex, 4);
        } else {
            double subtotal = 1 * item.price;
            transactionModel.addRow(new Object[]{barcode, item.itemName, 1, String.format("₱%.2f", item.price), String.format("₱%.2f", subtotal)});
        }
        updateTotal();
    }
    
    private void deleteSelectedItem() {
        int selectedIndex = transactionTable.getSelectedRow();
        if(selectedIndex != -1) {
            transactionModel.removeRow(selectedIndex);
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Increase/Decrease functions removed as per your request.
    
    private void updateTotal() {
        double total = 0.0;
        for(int i = 0; i < transactionModel.getRowCount(); i++) {
            String subStr = transactionModel.getValueAt(i, 4).toString().replace("₱", "");
            total += Double.parseDouble(subStr);
        }
        lblTotal.setText("Total: ₱" + String.format("%.2f", total));
    }
    
    // ---------------------------
    // Payment and Receipt Methods
    // ---------------------------
    private void processPayment() {
        double total = 0.0;
        for(int i = 0; i < transactionModel.getRowCount(); i++) {
            String subStr = transactionModel.getValueAt(i, 4).toString().replace("₱", "");
            total += Double.parseDouble(subStr);
        }
        double cash;
        try {
            cash = Double.parseDouble(txtCashReceived.getText().trim());
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid cash amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(cash < total) {
            JOptionPane.showMessageDialog(this, "Insufficient cash!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double balance = cash - total;
        lblBalance.setText("Balance: ₱" + String.format("%.2f", balance));
        generateReceipt(total, cash, balance);
        updateInventoryAfterSale();
    }
    
    private void generateReceipt(double total, double cash, double balance) {
        receiptArea.setText("");
        receiptArea.append("               Pentagram POS Receipt\n");
        receiptArea.append("-------------------------------------------------------------\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        receiptArea.append("Date: " + dateFormat.format(new Date()) + "\n\n");
        receiptArea.append(String.format("%-10s %-30s %-5s %-10s %-10s\n", "Barcode", "Item", "Qty", "Price", "Subtotal"));
        receiptArea.append("-------------------------------------------------------------\n");
        for(int i = 0; i < transactionModel.getRowCount(); i++) {
            String barcode = transactionModel.getValueAt(i, 0).toString();
            String itemName = transactionModel.getValueAt(i, 1).toString();
            String qty = transactionModel.getValueAt(i, 2).toString();
            String price = transactionModel.getValueAt(i, 3).toString();
            String subtotal = transactionModel.getValueAt(i, 4).toString();
            receiptArea.append(String.format("%-10s %-30s %-5s %-10s %-10s\n", barcode, itemName, qty, price, subtotal));
        }
        receiptArea.append("-------------------------------------------------------------\n");
        receiptArea.append("Total:   ₱" + String.format("%.2f", total) + "\n");
        receiptArea.append("Cash:    ₱" + String.format("%.2f", cash) + "\n");
        receiptArea.append("Balance: ₱" + String.format("%.2f", balance) + "\n\n");
        receiptArea.append("         THANK YOU FOR SHOPPING!\n");
    }
    
    private void saveReceiptAsImage() {
        try {
            Dimension size = receiptArea.getPreferredSize();
            BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, size.width, size.height);
            receiptArea.setOpaque(true);
            receiptArea.setBackground(Color.WHITE);
            receiptArea.setSize(size);
            receiptArea.paint(g2d);
            g2d.dispose();
            File file = new File("receipt.png");
            ImageIO.write(image, "png", file);
            JOptionPane.showMessageDialog(this, "Receipt saved as " + file.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            resetSale();
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void updateInventoryAfterSale() {
        for(int i = 0; i < transactionModel.getRowCount(); i++) {
            String barcode = transactionModel.getValueAt(i, 0).toString();
            int soldQty = Integer.parseInt(transactionModel.getValueAt(i, 2).toString());
            if(inventoryMap.containsKey(barcode)) {
                InventoryItem item = inventoryMap.get(barcode);
                item.stock -= soldQty;
                if(item.stock <= 0) {
                    JOptionPane.showMessageDialog(this, "Warning: " + item.itemName + " is now out of stock!", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                    item.stock = 0;
                }
            }
        }
        saveInventoryData();
    }
    
    private void resetSale() {
        transactionModel.setRowCount(0);
        txtCashReceived.setText("");
        txtBarcode.setText("");
        txtQty.setText("");
        lblTotal.setText("Total: ₱0.00");
        lblBalance.setText("Balance: ₱0.00");
        receiptArea.setText("               Pentagram POS Receipt\n");
        receiptArea.append("-------------------------------------------------------------\n");
    }
    
    // ---------------------------
    // New Quantity Adjustment Method
    // ---------------------------
    private void applyQuantityAdjustment() {
        int selectedRow = transactionTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item in the transaction table to adjust quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String barcode = transactionModel.getValueAt(selectedRow, 0).toString();
        InventoryItem item = inventoryMap.get(barcode);
        try {
            int newQty = Integer.parseInt(txtQty.getText().trim());
            if(newQty < 1) {
                JOptionPane.showMessageDialog(this, "Quantity must be at least 1.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(newQty > item.stock) {
                JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + item.stock, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            transactionModel.setValueAt(newQty, selectedRow, 2);
            double subtotal = newQty * item.price;
            transactionModel.setValueAt(String.format("₱%.2f", subtotal), selectedRow, 4);
            updateTotal();
            txtQty.setText("");
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ---------------------------
    // Action Handling
    // ---------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == btnAddItem || src == txtBarcode) {
            String barcode = txtBarcode.getText().trim();
            addItemToTransaction(barcode);
            txtBarcode.setText("");
        }
        else if(src == btnDeleteItem) {
            deleteSelectedItem();
        }
        else if(src == btnProcessPayment) {
            processPayment();
        }
        else if(src == btnReset) {
            resetSale();
        }
        else if(src == btnSaveReceipt) {
            saveReceiptAsImage();
        }
        else if(src == btnLogout) {
            new SelectionCashier().setVisible(true);
            dispose();
        }
        else if(src == btnApplyQty) {
            applyQuantityAdjustment();
        }
        // Number Pad actions:
        else if(src instanceof JButton) {
            String cmd = e.getActionCommand();
            if(cmd.equals("Clear")) {
                if(txtBarcode.isFocusOwner()) {
                    txtBarcode.setText("");
                } else if(txtCashReceived.isFocusOwner()) {
                    txtCashReceived.setText("");
                } else if(txtQty.isFocusOwner()) {
                    txtQty.setText("");
                } else {
                    txtBarcode.requestFocusInWindow();
                    txtBarcode.setText("");
                }
            }
            else if("0123456789.".contains(cmd)) {
                if(txtBarcode.isFocusOwner()) {
                    txtBarcode.setText(txtBarcode.getText() + cmd);
                } else if(txtCashReceived.isFocusOwner()) {
                    txtCashReceived.setText(txtCashReceived.getText() + cmd);
                } else if(txtQty.isFocusOwner()) {
                    txtQty.setText(txtQty.getText() + cmd);
                } else {
                    txtBarcode.requestFocusInWindow();
                    txtBarcode.setText(txtBarcode.getText() + cmd);
                }
            }
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) { }
    
    @Override
    public void stateChanged(ChangeEvent e) { }
    
    // ---------------------------
    // Main Method
    // ---------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            test pos = new test();
            pos.setVisible(true);
        });
    }
}
