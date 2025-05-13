package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class InventoryAdmin extends JFrame implements ActionListener {
	
    JPanel inventoryPanel = new JPanel();
    JPanel controlPanel = new JPanel();

    DefaultTableModel tableModel;
    JTable inventoryTable;

    JButton btnAdd = new JButton("Add Item");
    JButton btnEdit = new JButton("Edit Item");
    JButton btnDelete = new JButton("Delete Item");
    JButton btnRefresh = new JButton("Refresh List");
    JButton btnBack = new JButton("Back");

    private static final String FILE_NAME = "inventory.txt";

    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");

    Color myColor = new Color(193, 234, 242);

    public InventoryAdmin() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Inventory System");
        setLayout(new BorderLayout());
        setIconImage(logo.getImage());

        tableModel = new DefaultTableModel(new String[]{"Barcode", "Item Name", "Stock", "Price (₱)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        inventoryTable = new JTable(tableModel);
        inventoryTable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < inventoryTable.getColumnCount(); i++) {
            inventoryTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(inventoryTable);

        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory List"));
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);
        inventoryPanel.setBackground(myColor);

        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Admin Controls"));
        controlPanel.setBackground(myColor);

        controlPanel.add(btnAdd);
        controlPanel.add(btnEdit);
        controlPanel.add(btnDelete);
        controlPanel.add(btnRefresh);
        controlPanel.add(btnBack);

        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnRefresh.addActionListener(this);
        btnBack.addActionListener(this);

        loadInventory();

        add(inventoryPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void loadInventory() {
        tableModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { 
                    tableModel.addRow(new Object[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), "₱" + parts[3].trim()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveInventory() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String price = tableModel.getValueAt(i, 3).toString().replace("₱", "");
                out.println(
                        tableModel.getValueAt(i, 0) + "," +
                        tableModel.getValueAt(i, 1) + "," +
                        tableModel.getValueAt(i, 2) + "," +
                        price
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            String barcode = JOptionPane.showInputDialog("Enter barcode:");
            String itemName = JOptionPane.showInputDialog("Enter item name:");
            String stock = JOptionPane.showInputDialog("Enter stock quantity:");
            String price = JOptionPane.showInputDialog("Enter price:");

            if (barcode != null && itemName != null && stock != null && price != null) {
                tableModel.addRow(new Object[]{barcode.trim(), itemName.trim(), stock.trim(), "₱" + price.trim()});
                saveInventory();
                JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == btnEdit) {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                String currentItem = tableModel.getValueAt(selectedRow, 1).toString();
                String currentStock = tableModel.getValueAt(selectedRow, 2).toString();
                String currentPrice = tableModel.getValueAt(selectedRow, 3).toString().replace("₱", "");

                JPasswordField pf = new JPasswordField();
                int okCxl = JOptionPane.showConfirmDialog(this, pf, "Enter admin password to confirm:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (okCxl == JOptionPane.OK_OPTION) {
                    String enteredPassword = new String(pf.getPassword());
                    
                    if (enteredPassword.equals("admin")) {
                        String newItem = JOptionPane.showInputDialog("Edit item name:", currentItem);
                        String newStock;
                       
                        while (true) {
                            newStock = JOptionPane.showInputDialog("Edit stock quantity:", currentStock);
                            
                            if (newStock == null) {
                                return; 
                            }
                            
                            try {
                                Integer.parseInt(newStock.trim());
                                break; 
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Invalid stock quantity! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        String newPrice = JOptionPane.showInputDialog("Edit price:", currentPrice);
                        
                        if (newItem != null && newPrice != null) {
                            tableModel.setValueAt(newItem.trim(), selectedRow, 1);
                            tableModel.setValueAt(newStock.trim(), selectedRow, 2);
                            tableModel.setValueAt("₱" + newPrice.trim(), selectedRow, 3);
                            saveInventory();
                            JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password! Edit failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an item to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnDelete) {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                JPasswordField pf = new JPasswordField();
                int okCxl = JOptionPane.showConfirmDialog(this, pf, "Enter admin password to confirm deletion:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (okCxl == JOptionPane.OK_OPTION) {
                    String enteredPassword = new String(pf.getPassword());
                    if (enteredPassword.equals("admin")) {
                        tableModel.removeRow(selectedRow);
                        saveInventory();
                        JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password! Deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnRefresh) {
            loadInventory();
            JOptionPane.showMessageDialog(this, "Inventory refreshed!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == btnBack) {
            new SelectionAdmin().setVisible(true);
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new InventoryAdmin().setVisible(true);
    }
}
