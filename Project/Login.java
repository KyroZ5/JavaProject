package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.*;

public class Login extends JFrame implements ActionListener {
	
    ArrayList<Integer> employeeNumber = new ArrayList<>();
    ArrayList<String> username = new ArrayList<>();
    ArrayList<String> password = new ArrayList<>();
    int size; 
    
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
 
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
    Image newLogo = img.getScaledInstance(350,80,java.awt.Image.SCALE_SMOOTH);
    ImageIcon Logo = new ImageIcon(newLogo);
    JLabel bLogo = new JLabel();
    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
    Font font = new Font("Montserrat", Font.BOLD, 15);

    public Login() {
    	setSize(450,400);
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
        p1.setBorder(BorderFactory.createTitledBorder(""));
        p1.setBackground(myColor);
        p1.setBorder(null);
        
        p2.setBounds(0, 0, 450, 130);
        p2.setLayout(null);
        p2.setBorder(BorderFactory.createTitledBorder(""));
        p2.setBackground(myColor);
        p2.add(bLogo);
        p2.setBorder(null);
        bLogo.setBounds(40,30,375,105);
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

        // Sample users
        employeeNumber.add(101);
        username.add("cashier");
        password.add("cashier");

        employeeNumber.add(102);
        username.add("admin");
        password.add("admin");

        // **Load registered users**
        loadUsersFromFile();  

        size = username.size(); // Ensure size is updated correctly
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
                    JOptionPane.showMessageDialog(null, "Welcome, " + UserL, "Login Successful", JOptionPane.INFORMATION_MESSAGE);

                    if (UserL.equalsIgnoreCase("admin") && PassL.equals("admin")) { 
                        new SelectionAdmin().setVisible(true);
                        setVisible(false);
                    } else if (UserL.equalsIgnoreCase(username.get(i))) { 
                        new SelectionCashier().setVisible(true);
                        setVisible(false);
                    }
                }
            }

            if (!authenticated) {
                JOptionPane.showMessageDialog(null, "Invalid username or password!", "Invalid Login", JOptionPane.ERROR_MESSAGE);
            }
        }
    }private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    username.add(parts[0]);
                    password.add(parts[1]);
                }
            }
            size = username.size(); // Update size
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}
