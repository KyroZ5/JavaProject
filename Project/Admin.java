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
    JCheckBox chkShowPassword = new JCheckBox("Show Passwords");

    // Table setup
    private DefaultTableModel tableModel;
    private JTable userTable;
    private static final String FILE_NAME = "users.txt";

    public Admin() {
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin");
        setLayout(null);
        setResizable(false);

        // Buttons
        add(btnAdd);
        add(btnEdit);
        add(btnDel);
        add(btnRefresh);
        add(chkShowPassword);

        btnAdd.setBounds(30, 10, 100, 30);
        btnAdd.addActionListener(this);

        btnEdit.setBounds(140, 10, 100, 30);
        btnEdit.addActionListener(this);

        btnDel.setBounds(250, 10, 100, 30);
        btnDel.addActionListener(this);

        btnRefresh.setBounds(360, 10, 100, 30);
        btnRefresh.addActionListener(this);

        chkShowPassword.setBounds(20, 560, 150, 30);
        chkShowPassword.addActionListener(e -> {
            passwordRenderer.setShowPasswords(chkShowPassword.isSelected());
            userTable.repaint(); // Refresh table when toggled
        });

        // JTable Setup
        tableModel = new DefaultTableModel(new String[]{"Username", "Password"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable manual editing
            }
        };

        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(20, 50, 450, 500);
        add(scrollPane);

        // Apply Password Renderer
        userTable.getColumnModel().getColumn(1).setCellRenderer(passwordRenderer);

        // Load users into table
        loadUsers();
    }

    // Custom Renderer to Mask Passwords
    class PasswordRenderer extends DefaultTableCellRenderer {
        private boolean showPasswords = false;

        public void setShowPasswords(boolean showPasswords) {
            this.showPasswords = showPasswords;
        }

        @Override
        protected void setValue(Object value) {
            if (!showPasswords && value != null) {
                setText("****"); // Hide password
            } else {
                setText(value.toString()); // Show actual password when toggled
            }
        }
    }

    PasswordRenderer passwordRenderer = new PasswordRenderer();

    private void loadUsers() {
        tableModel.setRowCount(0); // Clear table

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Ensure correct format (Username, Password)
                    tableModel.addRow(new String[]{parts[0], parts[1]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Debugging: Check if data is being loaded
        System.out.println("Loaded users count: " + tableModel.getRowCount());
    }

    private void saveUsersToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                out.println(tableModel.getValueAt(i, 0) + "," + tableModel.getValueAt(i, 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == btnAdd) {
            Login loginInstance = new Login();
            new Add(loginInstance).setVisible(true); // Open Add User screen
        } else if (ev.getSource() == btnEdit) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) { 
                String currentUsername = tableModel.getValueAt(selectedRow, 0).toString();
                String currentPassword = tableModel.getValueAt(selectedRow, 1).toString();
                
                String newUsername = JOptionPane.showInputDialog("Enter new username:", currentUsername);
                String newPassword = JOptionPane.showInputDialog("Enter new password:", currentPassword);
                
                if (newUsername != null && newPassword != null) {
                    tableModel.setValueAt(newUsername, selectedRow, 0);
                    tableModel.setValueAt(newPassword, selectedRow, 1);
                    saveUsersToFile();
                    JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a user to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ev.getSource() == btnDel) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                saveUsersToFile();
                JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (ev.getSource() == btnRefresh) {
            loadUsers();
        }
    }

    public static void main(String[] args) {
        new Admin().setVisible(true);
    }
}
