package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Admin extends JFrame implements ActionListener {

    // Buttons
    JButton btnAdd = new JButton("Add");
    JButton btnEdit = new JButton("Edit");
    JButton btnDel = new JButton("Delete");
    JButton btnRefresh = new JButton("Refresh");
    JButton btnLogout = new JButton("Back");

    // Table setup (Now includes Name column)
    private DefaultTableModel tableModel;
    private JTable userTable;
    private static final String FILE_NAME = "users.txt";

    // Image Icons
    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");

    // Color
    Color myColor = new Color(193, 234, 242);

    public Admin() {
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin");
        setLayout(null);
        setResizable(false);
        setIconImage(logo.getImage());
        getContentPane().setBackground(myColor);

        // Buttons (Aligned in a row)
        add(btnAdd);
        add(btnEdit);
        add(btnDel);
        add(btnRefresh);
        add(btnLogout);

        btnAdd.setBounds(10, 10, 80, 30);
        btnEdit.setBounds(100, 10, 80, 30);
        btnDel.setBounds(190, 10, 80, 30);
        btnRefresh.setBounds(280, 10, 80, 30);
        btnLogout.setBounds(390, 10, 80, 30);

        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDel.addActionListener(this);
        btnRefresh.addActionListener(this);
        btnLogout.addActionListener(this);

        // JTable Setup (Updated with Name, Username, and Password columns)
        tableModel = new DefaultTableModel(new String[]{"Name", "Username", "Password"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent manual editing
            }
        };

        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(10, 50, 460, 500);
        add(scrollPane);

        // Apply Password Renderer to mask the password column.
        userTable.getColumnModel().getColumn(2).setCellRenderer(new PasswordRenderer());

        loadUsers();
    }

    // Custom Renderer to Mask Passwords (always displays ****)
    class PasswordRenderer extends DefaultTableCellRenderer {
        private boolean showPasswords = false; // If set to true, passwords display in plain text.
        @Override
        protected void setValue(Object value) {
            if (value != null && !showPasswords) {
                setText("****");
            } else if (value != null) {
                setText(value.toString());
            }
        }
    }

    private void loadUsers() {
        tableModel.setRowCount(0); // Clear table before loading
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { // Format: Name, Username, Password
                    tableModel.addRow(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsersToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                out.println(tableModel.getValueAt(i, 0) + "," 
                          + tableModel.getValueAt(i, 1) + "," 
                          + tableModel.getValueAt(i, 2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == btnAdd) {
            // Launch the Add User dialog (assumes an Add class exists)
            Login loginInstance = new Login();
            new Add(loginInstance).setVisible(true);
        } else if (ev.getSource() == btnEdit) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String currentName = tableModel.getValueAt(selectedRow, 0).toString();
                String currentUsername = tableModel.getValueAt(selectedRow, 1).toString();
                String currentPassword = tableModel.getValueAt(selectedRow, 2).toString();
                
                // Use a JPasswordField to mask the input
                JPasswordField pf = new JPasswordField();
                int okCxl = JOptionPane.showConfirmDialog(this, pf, "Enter user current password to confirm:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (okCxl == JOptionPane.OK_OPTION) {
                    String enteredPassword = new String(pf.getPassword());
                    if (enteredPassword.equals(currentPassword)) {
                        String newName = JOptionPane.showInputDialog("Enter new name:", currentName);
                        String newUsername = JOptionPane.showInputDialog("Enter new username:", currentUsername);
                        // Use a JPasswordField for editing the new password
                        JPasswordField pfNew = new JPasswordField();
                        int okNew = JOptionPane.showConfirmDialog(this, pfNew, "Enter new password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (okNew == JOptionPane.OK_OPTION) {
                            String newPassword = new String(pfNew.getPassword());
                            if (newName != null && newUsername != null && newPassword != null) {
                                tableModel.setValueAt(newName, selectedRow, 0);
                                tableModel.setValueAt(newUsername, selectedRow, 1);
                                tableModel.setValueAt(newPassword, selectedRow, 2);
                                saveUsersToFile();
                                JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Password confirmation failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a user to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ev.getSource() == btnDel) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                // Use a JPasswordField to mask deletion password confirmation
                JPasswordField pf = new JPasswordField();
                int okCxl = JOptionPane.showConfirmDialog(this, pf, "Enter admin password to delete:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (okCxl == JOptionPane.OK_OPTION) {
                    String enteredPassword = new String(pf.getPassword());
                    if (enteredPassword.equals("admin")) {
                        tableModel.removeRow(selectedRow);
                        saveUsersToFile();
                        JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password! Deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ev.getSource() == btnRefresh) {
            loadUsers();
            JOptionPane.showMessageDialog(this, "List refreshed!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else if (ev.getSource() == btnLogout) {
            new SelectionAdmin().setVisible(true);
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new Admin().setVisible(true);
    }
}
