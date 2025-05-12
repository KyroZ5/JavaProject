package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.event.*;

public class Add extends JFrame implements ActionListener, ItemListener, ChangeListener {

	  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	  private Login loginInstance;
	  
	  JPanel p1 = new JPanel(); // Keep manual layout for precise positioning
	  //JPanel p2 = new JPanel();
	  Color myColor = new Color(193, 234, 242); 
	  JLabel lblregUsername = new JLabel("Username: ");
	  JLabel lblregPassword = new JLabel("Password: ");
	  JTextField txtregUsername = new JTextField();
	  JPasswordField txtregPassword = new JPasswordField();
	  JButton btnregLogin = new JButton("Register");
	  JButton btnregCancel = new JButton("Cancel");
	  Font font = new Font("Montserrat", Font.BOLD, 15);
	  
	  private static final String FILE_NAME = "users.txt"; // File to store users
	  
	   //Image Icon
	    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
	 
	    
	public Add(Login loginInstance) {
		setSize(450,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login Form");
        setLayout(null);
        setResizable(false);
        setIconImage(logo.getImage());
    	getContentPane().setBackground(Color.WHITE);
    	

    	add(p1);
        // Login Panel Setup
        p1.add(lblregUsername);
	    p1.add(lblregPassword);
	    p1.add(txtregUsername);
	    p1.add(txtregPassword);
	    p1.add(btnregLogin);
	    p1.add(btnregCancel);   
	   
        p1.setBounds(0, 0, 450, 370);
        p1.setLayout(null);
        p1.setBorder(BorderFactory.createTitledBorder(""));
        p1.setBackground(myColor);
        p1.setBorder(null);
        
        // Position Components
        lblregUsername.setBounds(50, 50, 100, 30);
        lblregUsername.setFont(font);
        txtregUsername.setBounds(150, 50, 150, 30);
        lblregPassword.setBounds(50, 100, 100, 30);
        lblregPassword.setFont(font);
        txtregPassword.setBounds(150, 100, 150, 30);
     
        btnregCancel.setBounds(80, 170, 100, 30);
        btnregCancel.setFont(font);
        btnregCancel.addActionListener(this);
   	   
        btnregLogin.setBounds(280, 170, 100, 30);
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