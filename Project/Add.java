package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.event.*;

public class Add extends JFrame implements ActionListener, ItemListener, ChangeListener {

	  private Login loginInstance;
	  
	JPanel p1 = new JPanel(); // Keep manual layout for precise positioning
	JPanel p2 = new JPanel();
	Color myColor = new Color(193, 234, 242); 
	JLabel lblregUsername = new JLabel("Username: ");
	JLabel lblregPassword = new JLabel("Password: ");
	JTextField txtregUsername = new JTextField();
	JPasswordField txtregPassword = new JPasswordField();
	JButton btnregLogin = new JButton("Register");
	JButton btnregCancel = new JButton("Cancel");
	Font font = new Font("Montserrat", Font.BOLD, 15);
        Font font2 = new Font("Montserrat", Font.BOLD, 20);
        ImageIcon BLogo = new ImageIcon("./img/logo-dark-transparent.png");
        Image img = BLogo.getImage();
        Image newLogo = img.getScaledInstance(350,80,java.awt.Image.SCALE_SMOOTH);
        ImageIcon Logo = new ImageIcon(newLogo);
        JLabel bLogo = new JLabel();
        JLabel newRegis = new JLabel("Register New Account");
        ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
	  
	  private static final String FILE_NAME = "users.txt"; // File to store users
	
	    
	public Add(Login loginInstance) {
	    setSize(450,400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Pentagram");
            setLayout(null);
            setResizable(false);
            setIconImage(logo.getImage());
            getContentPane().setBackground(Color.WHITE);
    	

            add(p1);
            add(p2);
        
            p2.setBounds(0, 0, 450, 130);
            p2.setLayout(null);
            p2.setBorder(BorderFactory.createTitledBorder(""));
            p2.setBackground(myColor);
            p2.add(bLogo);
            p2.setBorder(null);
            bLogo.setBounds(40,30,375,105);
            bLogo.setIcon(Logo);
        
            p1.add(lblregUsername);
            p1.add(lblregPassword);
            p1.add(txtregUsername);
            p1.add(txtregPassword);
            p1.add(btnregLogin);
            p1.add(btnregCancel);   
            p1.add(newRegis);
	   
            p1.setBounds(0, 130, 450, 250);
            p1.setLayout(null);
            p1.setBorder(BorderFactory.createTitledBorder(""));
            p1.setBackground(myColor);
            p1.setBorder(null);
        
        // Position Components
            newRegis.setBounds(120, 0, 250, 30);
            newRegis.setFont(font2);
            lblregUsername.setBounds(80, 50, 100, 30);
            lblregUsername.setFont(font);
            txtregUsername.setBounds(180, 50, 150, 30);
            lblregPassword.setBounds(80, 100, 100, 30);
            lblregPassword.setFont(font);
            txtregPassword.setBounds(180, 100, 150, 30);
     
            btnregCancel.setBounds(80, 170, 100, 30);
            btnregCancel.setFont(font);
            btnregCancel.addActionListener(this);
   	   
            btnregLogin.setBounds(240, 170, 100, 30);
            btnregLogin.setFont(font);
            btnregLogin.addActionListener(this);	

   	    this.loginInstance = loginInstance; // Store the reference
	}
	
	public static void main(String[] args) {
        Login loginInstance = new Login(); // Ensure a Login instance exists
        new Add(loginInstance).setVisible(true); // Pass Login reference
	 
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
	    if (ev.getSource() == btnregLogin) {
	        String user = txtregUsername.getText().trim();
	        String pass = new String(txtregPassword.getPassword()).trim();

	        if (!user.isEmpty() && !pass.isEmpty()) {
	            if (isUsernameTaken(user)) {
	                JOptionPane.showMessageDialog(null, "Username already exists! Please choose a different one.", "Error", JOptionPane.ERROR_MESSAGE);
	            } else {
	                loginInstance.username.add(user);
	                loginInstance.password.add(pass);
	                saveUserToFile(user, pass); // Save user credentials to file
	                JOptionPane.showMessageDialog(null, "User registered successfully!", "Registration", JOptionPane.INFORMATION_MESSAGE);
	                new SelectionAdmin().setVisible(true);
	                setVisible(false);
	            }
	        } else {
	            JOptionPane.showMessageDialog(null, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    } else if (ev.getSource() == btnregCancel) {
             setVisible(false);
             new SelectionAdmin().setVisible(true);
	    }
	}

	private boolean isUsernameTaken(String username) {
	    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            if (parts.length > 0 && parts[0].equals(username)) {
	                return true; // Username found
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return false; // Username not found
	}

	
    private void saveUserToFile(String user, String pass) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(user + "," + pass); // Save username,password format
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}