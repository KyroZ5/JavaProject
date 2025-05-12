package Project;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.imageio.ImageIO;

public class Cashier extends JFrame implements ActionListener, ItemListener, ChangeListener {

    // Main container panels for auto-resizing
    JPanel leftPanel = new JPanel(new BorderLayout(5, 5));   // Holds Transaction and Payment panels
    JPanel rightPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // Holds Receipt and Barcode panels

    // Sub-panels
    JPanel P1 = new JPanel(); // Transaction Table Panel
    JPanel P3 = new JPanel(); // Payment Panel
    JPanel P2 = new JPanel(); // Receipt Panel (now the top part of the right panel)
    JPanel P4 = new JPanel(); // Barcode Input Panel (now the bottom part of the right panel)

    // Transaction Table components
    DefaultTableModel transactionModel;
    JTable transactionTable;

    // Barcode Input components (P4)
    JLabel lblBarcode = new JLabel("Barcode:");
    JTextField txtBarcode = new JTextField(15);
    JButton btnAddItem = new JButton("Add Item");
    JButton btnDeleteItem = new JButton("Delete Item");

    // Payment components (P3)
    JLabel lblTotal = new JLabel("Total: ₱0.00");
    JLabel lblCashReceived = new JLabel("Cash Received:");
    JTextField txtCashReceived = new JTextField(10);
    JButton btnProcessPayment = new JButton("Process Payment");
    JLabel lblBalance = new JLabel("Balance: ₱0.00");

    // Receipt Panel components (P2)
    JTextArea receiptArea = new JTextArea();
    JScrollPane receiptScroll = new JScrollPane(receiptArea);
    JButton btnSaveReceipt = new JButton("Save Receipt as Image");

    // Inventory data (loaded from file)
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

    // Color
  	Color myColor = new Color(193, 234, 242); 
  	
    public Cashier() {
        // Frame settings using BorderLayout for auto-resizing
        setTitle("Pentagram POS (Point-of-Sale) System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage(new ImageIcon("./img/logo-icon-dark-transparent.png").getImage());
        setLayout(new BorderLayout(5, 5));
        
        // ********************
        // LEFT PANEL (Center)
        // ********************
        // P1: Transaction Table Panel
        P1.setLayout(new BorderLayout());
        P1.setBorder(BorderFactory.createTitledBorder("Transaction"));
        transactionModel = new DefaultTableModel(new String[]{"Barcode", "Item Name", "Qty", "Price (₱)", "Subtotal (₱)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(transactionModel);
        transactionTable.setRowHeight(25);
        // Center table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < transactionTable.getColumnCount(); i++) {
            transactionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane transScroll = new JScrollPane(transactionTable);
        P1.add(transScroll, BorderLayout.CENTER);
        P1.setBackground(myColor);
        // P3: Payment Panel using FlowLayout
        P3.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        P3.setBorder(BorderFactory.createTitledBorder("Payment"));
        P3.add(lblTotal);
        P3.add(lblCashReceived);
        P3.add(txtCashReceived);
        P3.add(btnProcessPayment);
        P3.add(lblBalance);
        P3.setBackground(myColor);
        btnProcessPayment.addActionListener(this);

        // Place P1 and P3 into leftPanel
        leftPanel.add(P1, BorderLayout.CENTER);
        leftPanel.add(P3, BorderLayout.SOUTH);

        // ********************
        // RIGHT PANEL (East)
        // ********************
        // P2: Receipt Panel (will display the receipt)
        P2.setLayout(new BorderLayout());
        P2.setBorder(BorderFactory.createTitledBorder("Receipt"));
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        P2.add(receiptScroll, BorderLayout.CENTER);
        P2.add(btnSaveReceipt, BorderLayout.SOUTH);
        P2.setBackground(myColor);
        btnSaveReceipt.addActionListener(this);

        // P4: Barcode Input Panel (for scanning or manual entry), using FlowLayout
        P4.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        P4.setBorder(BorderFactory.createTitledBorder("Scan / Enter Barcode"));
        P4.add(lblBarcode);
        P4.add(txtBarcode);
        P4.add(btnAddItem);
        P4.add(btnDeleteItem);
        P4.setBackground(myColor);
        // Add action listeners for barcode input and buttons
        txtBarcode.addActionListener(this);
        btnAddItem.addActionListener(this);
        btnDeleteItem.addActionListener(this);

        // Add P2 (Receipt) and P4 (Barcode) to rightPanel (Receipt goes top, Barcode goes bottom)
        rightPanel.add(P2);
        rightPanel.add(P4);

        // Add leftPanel and rightPanel to the main frame
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Load inventory data from "inventory.txt"
        loadInventoryData();

        // Initialize receipt content
        receiptArea.setText("               Pentagram POS Receipt\n");
        receiptArea.append("-------------------------------------------------------------\n");
    }

    // Load inventory from file "inventory.txt"
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

    // Add item to the current transaction using its barcode
    private void addItemToTransaction(String barcode) {
        if (barcode.isEmpty()) return;
        if (!inventoryMap.containsKey(barcode)) {
            JOptionPane.showMessageDialog(this, "Item not found in inventory!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        InventoryItem item = inventoryMap.get(barcode);
        int rowIndex = -1;
        for (int i = 0; i < transactionModel.getRowCount(); i++) {
            if (transactionModel.getValueAt(i, 0).toString().equals(barcode)) {
                rowIndex = i;
                break;
            }
        }
        if (rowIndex != -1) {
            int currentQty = Integer.parseInt(transactionModel.getValueAt(rowIndex, 2).toString());
            currentQty++;
            transactionModel.setValueAt(currentQty, rowIndex, 2);
            double subtotal = currentQty * item.price;
            transactionModel.setValueAt(String.format("₱%.2f", subtotal), rowIndex, 4);
        } else {
            int qty = 1;
            double subtotal = qty * item.price;
            transactionModel.addRow(new Object[]{
                barcode,
                item.itemName,
                qty,
                String.format("₱%.2f", item.price),
                String.format("₱%.2f", subtotal)
            });
        }
        updateTotal();
    }

    // Delete the selected transaction row
    private void deleteSelectedItem() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow != -1) {
            transactionModel.removeRow(selectedRow);
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to delete from the transaction.", "Error", JOptionPane.ERROR_MESSAGE);
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

    // Process payment and generate the receipt
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
    }

    // Generate receipt text with properly aligned columns (monospaced output)
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

    // Save the contents of receiptArea (the receipt text) as an image (PNG)
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
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Action handling for buttons and input fields
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddItem || e.getSource() == txtBarcode) {
            String barcode = txtBarcode.getText().trim();
            addItemToTransaction(barcode);
            txtBarcode.setText("");
        } else if (e.getSource() == btnProcessPayment) {
            processPayment();
        } else if (e.getSource() == btnSaveReceipt) {
            saveReceiptAsImage();
        } else if (e.getSource() == btnDeleteItem) {
            deleteSelectedItem();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Not used
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Cashier pos = new Cashier();
            pos.setVisible(true);
        });
    }
}
