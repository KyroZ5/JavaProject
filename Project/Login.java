package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Login extends JFrame implements ActionListener {
    
    ArrayList<String> employeeName = new ArrayList<>();
    ArrayList<String> username = new ArrayList<>();
    ArrayList<String> password = new ArrayList<>();
    int size; 
    
    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();

    JLabel lblUsername = new JLabel("Username: ");
    JLabel lblPassword = new JLabel("Password: ");
    JTextField txtUsername = new JTextField(15);
    JPasswordField txtPassword = new JPasswordField(15);   
    Color myColor = new Color(193, 234, 242); 
    JButton btnLogin = new JButton("Login");
    JButton btnCancel = new JButton("Exit");
       
    ImageIcon BLogo = new ImageIcon("./img/logo-dark-transparent.png");
    Image img = BLogo.getImage();
    Image newLogo = img.getScaledInstance(350, 80, Image.SCALE_SMOOTH);
    ImageIcon Logo = new ImageIcon(newLogo);
    JLabel bLogo = new JLabel();
    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
    Font font = new Font("Montserrat", Font.BOLD, 15);

    public Login() {
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login Form");
        setLayout(null);
        setResizable(false);
        setIconImage(logo.getImage());

        add(p2);
        add(p1);

        p1.add(lblUsername);
        p1.add(lblPassword);
        p1.add(txtUsername);
        p1.add(txtPassword);
        p1.add(btnLogin);
        p1.add(btnCancel);
        p1.setBounds(0, 130, 450, 250);
        p1.setLayout(null);
        p1.setBackground(myColor);

        p2.setBounds(0, 0, 450, 130);
        p2.setLayout(null);
        p2.setBackground(myColor);
        p2.add(bLogo);
        bLogo.setBounds(40, 30, 375, 105);
        bLogo.setIcon(Logo);

        lblUsername.setBounds(80, 35, 100, 30);
        lblUsername.setFont(font);
        txtUsername.setBounds(180, 35, 150, 30);
        lblPassword.setBounds(80, 85, 100, 30);
        lblPassword.setFont(font);
        txtPassword.setBounds(180, 85, 150, 30);

        btnLogin.setBounds(280, 170, 100, 30);
        btnLogin.setFont(font);
        btnCancel.setBounds(80, 170, 100, 30);
        btnCancel.setFont(font);

        btnLogin.addActionListener(this);
        btnCancel.addActionListener(this);

        username.add("cashier");
        password.add("cashier");

        username.add("admin");
        password.add("admin");
        
        // **Load registered users including names**
        loadUsersFromFile();  

        size = username.size();
    }

    public static void main(String[] args) {
        Login log = new Login();
        log.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == btnCancel) {
            int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Form", JOptionPane.YES_NO_OPTION);
            if (x == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (ev.getSource() == btnLogin) {
            String UserL = txtUsername.getText().trim();
            String PassL = new String(txtPassword.getPassword()).trim(); // Proper handling for passwords
            boolean authenticated = false;

            for (int i = 0; i < size; i++) {
                if (username.get(i).equalsIgnoreCase(UserL) && password.get(i).equals(PassL)) {
                    authenticated = true;
                   

                    if (UserL.equalsIgnoreCase("admin") && PassL.equals("admin")) { 
                        new SelectionAdmin().setVisible(true);
                        setVisible(false);
                        JOptionPane.showMessageDialog(null, "Welcome, " + UserL, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                    } else if (UserL.equalsIgnoreCase(username.get(i))) { 
                        new SelectionCashier().setVisible(true);
                        setVisible(false);
                        JOptionPane.showMessageDialog(null, "Welcome, " + UserL, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

            if (!authenticated) {
                JOptionPane.showMessageDialog(null, "Invalid username or password!", "Invalid Login", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    }

    private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { 
                    employeeName.add(parts[0]);
                    username.add(parts[1]);
                    password.add(parts[2]);
                }
            }
            size = username.size();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded users: " + employeeName);
    }
}
