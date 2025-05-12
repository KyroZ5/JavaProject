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
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
 
    JPanel p1 = new JPanel(null); // Keep manual layout for precise positioning

    JLabel lblUsername = new JLabel("Username: ");
    JLabel lblPassword = new JLabel("Password: ");
    JTextField txtUsername = new JTextField(15);
    JPasswordField txtPassword = new JPasswordField(15);

    JButton btnLogin = new JButton("Login");
    JButton btnCancel = new JButton("Exit");

    //Image Icon
    ImageIcon logo = new ImageIcon("./img/pos1.jpeg");
    
    //Backgound Image
    JPanel brand = new JPanel();
    ImageIcon BLogo = new ImageIcon("./img/pos3.jpeg");
    Image img = BLogo.getImage();
    Image newLogo = img.getScaledInstance(800,200,java.awt.Image.SCALE_SMOOTH);
    ImageIcon Logo = new ImageIcon(newLogo);
    JLabel bLogo = new JLabel();
    
    public Login() {
    	setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);	
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setTitle("Pentagram POS (Point-of-Sale) System");
    	setIconImage(logo.getImage());
    	getContentPane().setBackground(Color.WHITE);
    	
    	setLayout(null); // Keep manual layout for precise positioning
    	
    	// Background Image Panel Setup
        brand.setBounds(getWidth() / 2 - 400, 100, 800, 200); // Center it properly
        bLogo.setIcon(Logo);
        brand.add(bLogo);
        brand.setBackground(Color.WHITE);
        add(brand);

        // Login Panel Setup
        p1.setBounds(getWidth() / 2 - 200, 350, 400, 300); // Center login panel
        p1.setBorder(BorderFactory.createTitledBorder(""));
        p1.setBackground(Color.WHITE);
        add(p1);

        // Position Components
        lblUsername.setBounds(50, 50, 100, 30);
        txtUsername.setBounds(150, 50, 150, 30);
        lblPassword.setBounds(50, 100, 100, 30);
        txtPassword.setBounds(150, 100, 150, 30);
        btnLogin.setBounds(250, 250, 100, 30);
        btnCancel.setBounds(50, 250, 100, 30);

        // Add Components to Panel
        p1.add(lblUsername);
        p1.add(txtUsername);
        p1.add(lblPassword);
        p1.add(txtPassword);
        p1.add(btnLogin);
        p1.add(btnCancel);

        btnLogin.addActionListener(this);
        btnCancel.addActionListener(this);
        
        // Resize Listener for Dynamic Adjustments
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                brand.setBounds(getWidth() / 2 - 400, 100, 800, 200);
                p1.setBounds(getWidth() / 2 - 200, 350, 400, 300);
                p1.revalidate();
            }
        });

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
