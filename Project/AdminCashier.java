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
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;

public class AdminCashier extends JFrame implements ActionListener, ItemListener, ChangeListener {

     Color myColor = new Color(193, 234, 242);

     JPanel leftPanel;   
     JPanel rightPanel;  


     JPanel headerPanel;
     JLabel lblDateTime;
     JLabel bLogo;    

  
     JPanel transactionPanel;
     DefaultTableModel transactionModel;
     JTable transactionTable;
     
     JPanel paymentPanel;
     JPanel barcodePanel;
     JLabel lblBarcode = new JLabel("Barcode:");
     JTextField txtBarcode = new JTextField(15);
     JButton btnAddItem = new JButton("Add Item");
     JButton btnDeleteItem = new JButton("Delete Item");

     JPanel qtyPanel;
     JLabel lblQty = new JLabel("Adjust Qty:");
     JTextField txtQty = new JTextField(5);
     JButton btnApplyQty = new JButton("Apply Qty");

     JPanel cashInputPanel;
     JLabel lblTotal = new JLabel("Total: ₱0.00");
     JLabel lblCashReceived = new JLabel("Cash Received:");
     JTextField txtCashReceived = new JTextField(10);
     JButton btnProcessPayment = new JButton("Process Payment");
     JButton btnReset = new JButton("Reset");
     JLabel lblBalance = new JLabel("Balance: ₱0.00");

     JPanel numPadPanel;
     
     JPanel logoutPanel;
     JButton btnLogout = new JButton("Menu");

     JPanel receiptPanel;
     JTextArea receiptArea = new JTextArea();
     JScrollPane receiptScroll = new JScrollPane(receiptArea);
     JButton btnPrintReceipt = new JButton("Print Receipt");

    private Map<String, InventoryItem> inventoryMap = new HashMap<>();

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

    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
    ImageIcon BLogo = new ImageIcon("./img/logo-dark-transparent.png");
    Image img = BLogo.getImage();
    Image newLogo = img.getScaledInstance(350, 80, Image.SCALE_SMOOTH);
    ImageIcon Logo = new ImageIcon(newLogo);

    private String centerText(String text, int width) {
        int padSize = (width - text.length()) / 2;
        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < padSize; i++) {
            padding.append(" ");
        }
        return padding.toString() + text;
    }

    public AdminCashier() {
        setUndecorated(true);
        setTitle("Pentagram POS (Point-of-Sale) System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage(logo.getImage());
        getContentPane().setBackground(myColor);
        setLayout(new BorderLayout(5, 5));

      
        leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(myColor);

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(myColor);
        bLogo = new JLabel(Logo);
        bLogo.setPreferredSize(new Dimension(375, 105));
        headerPanel.add(bLogo, BorderLayout.CENTER);
        lblDateTime = new JLabel();
        lblDateTime.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDateTime.setFont(new Font("Arial", Font.BOLD, 16));
        lblDateTime.setForeground(Color.BLACK);
        headerPanel.add(lblDateTime, BorderLayout.EAST);
        
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String time = new SimpleDateFormat("hh:mm a").format(new Date());
                lblDateTime.setText("<html><div style='text-align: right;'>" + date + "<br>" + time + "</div></html>");
            }
        });
        timer.start();
        leftPanel.add(headerPanel, BorderLayout.NORTH);

   
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
       
        rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(myColor);
        
        paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment"));
        paymentPanel.setBackground(myColor);
        
        barcodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        barcodePanel.setBackground(myColor);
        barcodePanel.add(lblBarcode);
        barcodePanel.add(txtBarcode);
        barcodePanel.add(btnAddItem);
        barcodePanel.add(btnDeleteItem);
        txtBarcode.addActionListener(this);
        btnAddItem.addActionListener(this);
        btnDeleteItem.addActionListener(this);
        
        qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        qtyPanel.setBackground(myColor);
        qtyPanel.add(lblQty);
        qtyPanel.add(txtQty);
        qtyPanel.add(btnApplyQty);
        btnApplyQty.addActionListener(this);
        
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
        
        numPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        numPadPanel.setBackground(myColor);
        String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "Clear"};
        for(String key : keys) {
            JButton btn = new JButton(key);
            btn.setFont(new Font("Arial", Font.BOLD, 24));
            btn.setPreferredSize(new Dimension(50, 50));
            btn.setFocusable(false); 
            btn.addActionListener(this);
            numPadPanel.add(btn);
        }
        logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(myColor);
        logoutPanel.add(Box.createVerticalStrut(20));
        logoutPanel.add(btnLogout);
        btnLogout.addActionListener(this);
   
        paymentPanel.add(barcodePanel);
        paymentPanel.add(qtyPanel);
        paymentPanel.add(cashInputPanel);
        paymentPanel.add(numPadPanel);
        paymentPanel.add(Box.createVerticalStrut(20));
        paymentPanel.add(logoutPanel);

        receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createTitledBorder("Receipt"));
        receiptPanel.setBackground(myColor);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        JScrollPane recScroll = new JScrollPane(receiptArea);
        recScroll.getViewport().setBackground(myColor);
        receiptPanel.add(recScroll, BorderLayout.CENTER);
        receiptPanel.add(btnPrintReceipt, BorderLayout.SOUTH);
        btnPrintReceipt.addActionListener(this);

        rightPanel.add(paymentPanel, BorderLayout.NORTH);
        rightPanel.add(receiptPanel, BorderLayout.CENTER);
        
     
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
        loadInventoryData();
        
        receiptArea.setText("");
    }
    
    private void loadInventoryData() {
        inventoryMap.clear();
        String filename = "inventory.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = br.readLine()) != null) {
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
    
    private void increaseQuantity() {
       
    }
    
    private void decreaseQuantity() {

    }
    
    private void updateTotal() {
        double total = 0.0;
        for(int i = 0; i < transactionModel.getRowCount(); i++) {
            String subStr = transactionModel.getValueAt(i, 4).toString().replace("₱", "");
            total += Double.parseDouble(subStr);
        }
        lblTotal.setText("Total: ₱" + String.format("%.2f", total));
    }
    
    
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
        
        receiptArea.append("         \n\n");
        receiptArea.append("         PENTAGRAM RECEIPT\n");
        receiptArea.append("---------------------------------------------------------------\n");
        
     
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        receiptArea.append("Date: " + dateFormat.format(new Date()) + "\n\n");
        
        receiptArea.append(String.format("%-3s %-5s %-5s %-5s\n", "Qty ->", "Item ->", "Price ->", "Subtotal"));
        receiptArea.append("---------------------------------------------------------------\n");
        
        for (int i = 0; i < transactionModel.getRowCount(); i++) {
            String itemName = transactionModel.getValueAt(i, 1).toString();
            String qty = transactionModel.getValueAt(i, 2).toString();
            String price = transactionModel.getValueAt(i, 3).toString();
            String subtotal = transactionModel.getValueAt(i, 4).toString();
            receiptArea.append(String.format("%-3s %-31s %-10s %-5s\n", qty, itemName,  price, subtotal));
        }
        receiptArea.append("---------------------------------------------------------------\n");
        receiptArea.append("Total:   P" + String.format("%.2f", total) + "\n");
        receiptArea.append("Cash:    P" + String.format("%.2f", cash) + "\n");
        receiptArea.append("Balance: P" + String.format("%.2f", balance) + "\n\n\n\n");
        receiptArea.append("      THANK YOU FOR SHOPPING!\n");
        receiptArea.append("         \n\n\n\n");
    }
    
    private void printReceiptDirect() {
        try {
            String receiptText = receiptArea.getText();
            byte[] receiptBytes = receiptText.getBytes("UTF-8");
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, null);
            if (printServices.length > 0) {
                PrintService selectedService = printServices[0];
                for (PrintService service : printServices) {
                    if (service.getName().toLowerCase().contains("xprinter-58")) {
                        selectedService = service;
                        break;
                    }
                }
                DocPrintJob printJob = selectedService.createPrintJob();
                Doc doc = new SimpleDoc(receiptBytes, flavor, null);
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                attributes.add(new Copies(1));
                attributes.add(new JobName("Pentagram POS Receipt", null));
                printJob.print(doc, attributes);
            } else {
                JOptionPane.showMessageDialog(this, "No printer found! Please check your USB printer connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Printing error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        receiptArea.setText("");
    }
   
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
        else if(src == btnPrintReceipt) {
            printReceiptDirect();
        }
        else if(src == btnLogout) {
            new SelectionAdmin().setVisible(true);
            dispose();
        }
        else if(src == btnApplyQty) {
            applyQuantityAdjustment();
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminCashier pos = new AdminCashier();
            pos.setVisible(true);
        });
    }
}
