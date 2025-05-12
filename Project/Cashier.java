package Project;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.imageio.*;

public class Cashier extends JFrame implements ActionListener, ItemListener, ChangeListener {

    // ---------------------------
    // Main containers (auto-resizing)
    // ---------------------------
     JPanel leftPanel;   // Contains Transaction table and control buttons
     JPanel rightPanel;  // Contains Receipt (top) and Payment panel (bottom)

    // ---------------------------
    // Left Panel Components
    // ---------------------------
     JPanel transactionPanel; // Transaction table panel
     JPanel transactionControlPanel; // Increase/Decrease buttons panel

     DefaultTableModel transactionModel;
     JTable transactionTable;
     JButton btnIncrease = new JButton("Increase Qty");
     JButton btnDecrease = new JButton("Decrease Qty");

    // ---------------------------
    // Right Panel Components
    // ---------------------------
    // Receipt Panel
     JPanel receiptPanel;
     JTextArea receiptArea = new JTextArea();
     JScrollPane receiptScroll = new JScrollPane(receiptArea);
     JButton btnSaveReceipt = new JButton("Save Receipt as Image");

    // Payment Panel (combined Barcode Input and Cash Payment, plus number pad, logout and reset)
     JPanel paymentPanel;
    // Barcode sub-panel: for scanning/entering barcode and Delete function
     JPanel barcodePanel;
     JLabel lblBarcode = new JLabel("Barcode:");
     JTextField txtBarcode = new JTextField(15);
     JButton btnAddItem = new JButton("Add Item");
     JButton btnDeleteItem = new JButton("Delete Item");

    // Cash input sub-panel (we are replacing the old cashPanel with a new cashInputPanel)
     JPanel cashInputPanel;
     JLabel lblTotal = new JLabel("Total: ₱0.00");
     JLabel lblCashReceived = new JLabel("Cash Received:");
     JTextField txtCashReceived = new JTextField(10);
     JButton btnProcessPayment = new JButton("Process Payment");
     JButton btnReset = new JButton("Reset");
     JLabel lblBalance = new JLabel("Balance: ₱0.00");

    // Number Pad sub-panel: keypad for entering cash
     JPanel numPadPanel;
    // Logout sub-panel
     JPanel logoutPanel;
     JButton btnLogout = new JButton("Logout");

    // Color
  	 Color myColor = new Color(193, 234, 242); 
    // ---------------------------
    // Inventory Data
    // ---------------------------
    // Each inventory item loaded from file "inventory.txt"
     Map<String, InventoryItem> inventoryMap = new HashMap<>();

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

    public Cashier() {
        // Frame settings using BorderLayout for auto-resizing
        setTitle("Pentagram POS (Point-of-Sale) System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage(new ImageIcon("./img/logo-icon-dark-transparent.png").getImage());
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(myColor);
        // ---------------------------
        // Left Panel: Transaction Table + Increase/Decrease buttons
        // ---------------------------
        leftPanel = new JPanel(new BorderLayout(5, 5));
        // Transaction Panel
        transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Transaction"));
        transactionModel = new DefaultTableModel(
                new String[]{"Barcode", "Item Name", "Qty", "Price (₱)", "Subtotal (₱)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        transactionTable = new JTable(transactionModel);
        transactionTable.setRowHeight(25);
        // Center-align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < transactionTable.getColumnCount(); i++) {
            transactionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane transScroll = new JScrollPane(transactionTable);
        transactionPanel.add(transScroll, BorderLayout.CENTER);
        transactionPanel.setBackground(myColor);
        // Transaction Control Panel: Increase/Decrease Quantity buttons
        transactionControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        transactionControlPanel.add(btnIncrease);
        transactionControlPanel.add(btnDecrease);
        transactionControlPanel.setBackground(myColor);
        btnIncrease.addActionListener(this);
        btnDecrease.addActionListener(this);

        leftPanel.add(transactionPanel, BorderLayout.CENTER);
        leftPanel.setBackground(myColor);
        leftPanel.add(transactionControlPanel, BorderLayout.SOUTH);
        

        // ---------------------------
        // Right Panel: Receipt (top) and Payment Panel (bottom)
        // ---------------------------
        rightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        rightPanel.setBackground(myColor);
        
        // Receipt Panel
        receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createTitledBorder("Receipt"));
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        receiptPanel.add(receiptScroll, BorderLayout.CENTER);
        receiptPanel.add(btnSaveReceipt, BorderLayout.SOUTH);
        receiptPanel.setBackground(myColor);
        btnSaveReceipt.addActionListener(this);
       
        // Payment Panel: Combined Barcode Input, Cash Payment, Number Pad, Logout, and Reset
        paymentPanel = new JPanel();
        paymentPanel.setBackground(myColor);
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment"));

        // Barcode sub-panel
        barcodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        barcodePanel.add(lblBarcode);
        barcodePanel.add(txtBarcode);
        barcodePanel.add(btnAddItem);
        barcodePanel.add(btnDeleteItem);
        txtBarcode.addActionListener(this);
        btnAddItem.addActionListener(this);
        btnDeleteItem.addActionListener(this);

        // New Cash Input sub-panel with vertical stacking:
        cashInputPanel = new JPanel();
        cashInputPanel.setLayout(new GridLayout(3, 1, 5, 5));
        // Row 1: Total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.add(lblTotal);
        // Row 2: Cash Received with text field and Process Payment/Reset buttons
        JPanel cashReceivedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cashReceivedPanel.add(lblCashReceived);
        cashReceivedPanel.add(txtCashReceived);
        cashReceivedPanel.add(btnProcessPayment);
        cashReceivedPanel.add(btnReset);
        // Row 3: Balance
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balancePanel.add(lblBalance);
        cashInputPanel.add(totalPanel);
        cashInputPanel.add(cashReceivedPanel);
        cashInputPanel.add(balancePanel);
        btnProcessPayment.addActionListener(this);
        btnReset.addActionListener(this);

        // Number Pad sub-panel
        numPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "Clear"};
        for (String key : keys) {
            JButton btn = new JButton(key);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.addActionListener(this);
            numPadPanel.add(btn);
        }

        // Logout sub-panel; add some vertical spacer to push logout button lower.
        logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(Box.createVerticalStrut(20)); // vertical space
        logoutPanel.setBackground(myColor);
        logoutPanel.add(btnLogout);
        btnLogout.addActionListener(this);

        // Add sub-panels to Payment Panel
        paymentPanel.add(barcodePanel);
        paymentPanel.add(cashInputPanel);
        paymentPanel.add(numPadPanel);
        paymentPanel.add(Box.createVerticalStrut(20)); // extra space before logout
        paymentPanel.add(logoutPanel);

        rightPanel.add(receiptPanel);
        rightPanel.add(paymentPanel);

        // ---------------------------
        // Main Frame Layout: add Left and Right panels
        // ---------------------------
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Load inventory data from file
        loadInventoryData();

        // Initialize receipt header
        receiptArea.setText("               Pentagram POS Receipt\n");
        receiptArea.append("-------------------------------------------------------------\n");
    }

    // ---------------------------
    // Inventory Data Methods
    // ---------------------------
    // Load inventory from "inventory.txt"
    private void loadInventoryData() {
        inventoryMap.clear();
        String filename = "inventory.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String barcode = parts[0].trim();
                    String itemName = parts[1].trim();
                    int stock = Integer.parseInt(parts[2].trim());
                    double price = Double.parseDouble(parts[3].trim());
                    inventoryMap.put(barcode, new InventoryItem(barcode, itemName, stock, price));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Save updated inventory data back to "inventory.txt"
    private void saveInventoryData() {
        String filename = "inventory.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (InventoryItem item : inventoryMap.values()) {
                out.println(item.barcode + "," + item.itemName + "," + item.stock + "," + item.price);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ---------------------------
    // Transaction Methods
    // ---------------------------
    // Add an item to the current transaction using its barcode,
    // checking available stock.
    private void addItemToTransaction(String barcode) {
        if (barcode.isEmpty()) return;
        if (!inventoryMap.containsKey(barcode)) {
            JOptionPane.showMessageDialog(this, "Item not found in inventory!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        InventoryItem item = inventoryMap.get(barcode);
        if (item.stock <= 0) {
            JOptionPane.showMessageDialog(this, "This item is out of stock!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int rowIndex = -1;
        // Check if the item is already in the transaction table
        for (int i = 0; i < transactionModel.getRowCount(); i++) {
            if (transactionModel.getValueAt(i, 0).toString().equals(barcode)) {
                rowIndex = i;
                break;
            }
        }
        int newQty = (rowIndex != -1) ? Integer.parseInt(transactionModel.getValueAt(rowIndex, 2).toString()) + 1 : 1;
        if (newQty > item.stock) {
            JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + item.stock, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (rowIndex != -1) {
            transactionModel.setValueAt(newQty, rowIndex, 2);
            double subtotal = newQty * item.price;
            transactionModel.setValueAt(String.format("₱%.2f", subtotal), rowIndex, 4);
        } else {
            double subtotal = 1 * item.price;
            transactionModel.addRow(new Object[]{
                barcode,
                item.itemName,
                1,
                String.format("₱%.2f", item.price),
                String.format("₱%.2f", subtotal)
            });
        }
        updateTotal();
    }

    // Delete the selected transaction row
    private void deleteSelectedItem() {
        int selectedIndex = transactionTable.getSelectedRow();
        if (selectedIndex != -1) {
            transactionModel.removeRow(selectedIndex);
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Increase quantity for the selected transaction row, checking available stock
    private void increaseQuantity() {
        int selectedIndex = transactionTable.getSelectedRow();
        if (selectedIndex != -1) {
            String barcode = transactionModel.getValueAt(selectedIndex, 0).toString();
            InventoryItem item = inventoryMap.get(barcode);
            int qty = Integer.parseInt(transactionModel.getValueAt(selectedIndex, 2).toString());
            if (qty + 1 > item.stock) {
                JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + item.stock, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            qty++;
            transactionModel.setValueAt(qty, selectedIndex, 2);
            double subtotal = qty * item.price;
            transactionModel.setValueAt(String.format("₱%.2f", subtotal), selectedIndex, 4);
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to increase quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Decrease quantity for the selected transaction row
    private void decreaseQuantity() {
        int selectedIndex = transactionTable.getSelectedRow();
        if (selectedIndex != -1) {
            String barcode = transactionModel.getValueAt(selectedIndex, 0).toString();
            InventoryItem item = inventoryMap.get(barcode);
            int qty = Integer.parseInt(transactionModel.getValueAt(selectedIndex, 2).toString());
            if (qty > 1) {
                qty--;
                transactionModel.setValueAt(qty, selectedIndex, 2);
                double subtotal = qty * item.price;
                transactionModel.setValueAt(String.format("₱%.2f", subtotal), selectedIndex, 4);
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Quantity is 1. Remove item?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    transactionModel.removeRow(selectedIndex);
                }
            }
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to decrease quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update the total cost displayed
    private void updateTotal() {
        double total = 0.0;
        for (int i = 0; i < transactionModel.getRowCount(); i++) {
            String subStr = transactionModel.getValueAt(i, 4).toString().replace("₱", "");
            total += Double.parseDouble(subStr);
        }
        lblTotal.setText("Total: ₱" + String.format("%.2f", total));
    }

    // ---------------------------
    // Payment and Receipt Methods
    // ---------------------------
    // Process payment, generate receipt, and update inventory stock.
    private void processPayment() {
        double total = 0.0;
        for (int i = 0; i < transactionModel.getRowCount(); i++) {
            String subStr = transactionModel.getValueAt(i, 4).toString().replace("₱", "");
            total += Double.parseDouble(subStr);
        }
        double cash;
        try {
            cash = Double.parseDouble(txtCashReceived.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid cash amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cash < total) {
            JOptionPane.showMessageDialog(this, "Insufficient cash!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double balance = cash - total;
        lblBalance.setText("Balance: ₱" + String.format("%.2f", balance));
        generateReceipt(total, cash, balance);
        // Update inventory stock based on sold items
        updateInventoryAfterSale();
    }

    // Generate the receipt text with proper alignment using a monospaced font
    private void generateReceipt(double total, double cash, double balance) {
        receiptArea.setText("");
        receiptArea.append("               Pentagram POS Receipt\n");
        receiptArea.append("-------------------------------------------------------------\n");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        receiptArea.append("Date: " + dateFormat.format(new Date()) + "\n\n");
        // Columns: Barcode (10), Item (30), Qty (5), Price (10), Subtotal (10)
        receiptArea.append(String.format("%-10s %-30s %-5s %-10s %-10s\n", "Barcode", "Item", "Qty", "Price", "Subtotal"));
        receiptArea.append("-------------------------------------------------------------\n");
        for (int i = 0; i < transactionModel.getRowCount(); i++) {
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

    // Save the contents of receiptArea as an image (PNG)
    private void saveReceiptAsImage() {
        try {
            Dimension size = receiptArea.getPreferredSize();
            BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.WHITE); // White background
            g2d.fillRect(0, 0, size.width, size.height);
            receiptArea.setOpaque(true);
            receiptArea.setBackground(Color.WHITE);
            receiptArea.setSize(size);
            receiptArea.paint(g2d);
            g2d.dispose();
            File file = new File("receipt.png");
            ImageIO.write(image, "png", file);
            JOptionPane.showMessageDialog(this, "Receipt saved as " + file.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            // After saving the receipt, reset everything for the next sale.
            resetSale();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // ---------------------------
    // Update inventory stock after sale and save back to file.
    // Show a warning if any item's stock reaches 0.
    private void updateInventoryAfterSale() {
        // For each transaction, subtract the sold quantity from inventory stock.
        for (int i = 0; i < transactionModel.getRowCount(); i++) {
            String barcode = transactionModel.getValueAt(i, 0).toString();
            int soldQty = Integer.parseInt(transactionModel.getValueAt(i, 2).toString());
            if (inventoryMap.containsKey(barcode)) {
                InventoryItem item = inventoryMap.get(barcode);
                item.stock = item.stock - soldQty;
                if (item.stock <= 0) {
                    JOptionPane.showMessageDialog(this, "Warning: " + item.itemName + " is now out of stock!", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                    item.stock = 0; // Ensure stock does not go negative.
                }
            }
        }
        saveInventoryData();
    }

    // Reset the sale interface for a new transaction.
    private void resetSale() {
        // Clear the transaction table
        transactionModel.setRowCount(0);
        // Clear cash and barcode text fields
        txtCashReceived.setText("");
        txtBarcode.setText("");
        // Reset total and balance labels
        lblTotal.setText("Total: ₱0.00");
        lblBalance.setText("Balance: ₱0.00");
        // Reset receipt area header
        receiptArea.setText("               Pentagram POS Receipt\n");
        receiptArea.append("-------------------------------------------------------------\n");
    }

    // ---------------------------
    // Action Handling
    // ---------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        // Barcode actions: add item via Add button or Enter in barcode text field.
        if (src == btnAddItem || src == txtBarcode) {
            String barcode = txtBarcode.getText().trim();
            addItemToTransaction(barcode);
            txtBarcode.setText("");
        }
        // Delete item from transaction table.
        else if (src == btnDeleteItem) {
            deleteSelectedItem();
        }
        // Increase/Decrease quantity.
        else if (src == btnIncrease) { 
            increaseQuantity();
        } else if (src == btnDecrease) {
            decreaseQuantity();
        }
        // Process Payment.
        else if (src == btnProcessPayment) {
            processPayment();
        }
        // Reset Sale functionality.
        else if (src == btnReset) {
            resetSale();
        }
        // Save Receipt as Image.
        else if (src == btnSaveReceipt) {
            saveReceiptAsImage();
        }
        // Logout: go to SelectionCashier and close this window.
        else if (src == btnLogout) {
            new SelectionCashier().setVisible(true);
            dispose();
        }
        // Number pad actions: If a key button is clicked on the numPadPanel.
        else if (src instanceof JButton) {
            String cmd = e.getActionCommand();
            if (cmd.equals("Clear")) {
                txtCashReceived.setText("");
            } else if ("0123456789.".contains(cmd)) {
                txtCashReceived.setText(txtCashReceived.getText() + cmd);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Not used.
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // Not used.
    }

    // ---------------------------
    // Main Method
    // ---------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Cashier pos = new Cashier();
            pos.setVisible(true);
        });
    }
}
