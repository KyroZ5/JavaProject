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

	  JLabel lblregUsername = new JLabel("Username: ");
	  JLabel lblregPassword = new JLabel("Password: ");
	  JTextField txtregUsername = new JTextField();
	  JPasswordField txtregPassword = new JPasswordField();
	  JButton btnregLogin = new JButton("Register");
	  JButton btnregCancel = new JButton("Cancel");
	  
	  JButton btnLogout = new JButton("Logout");
	  
	  private static final String FILE_NAME = "users.txt"; // File to store users
	  
	public Add(Login loginInstance) {
		
	this.loginInstance = loginInstance; // Store the reference
	
	setSize(screenSize.width, screenSize.height); // Set JFrame to full screen);
	setExtendedState(MAXIMIZED_BOTH); // Maximize window
	setLocationRelativeTo(null);
	   setLayout(null);
	   setTitle("Add Account");
	   
	   add(lblregUsername);
       add(lblregPassword);
       add(txtregUsername);
       add(txtregPassword);
       add(btnregLogin);
       add(btnregCancel);   
	   add(btnLogout);
	   
       lblregUsername.setBounds(70,80,150,30);
       txtregUsername.setBounds(150,70,150,30);
       lblregPassword.setBounds(70,120,150,30);
       txtregPassword.setBounds(150,120,150,30);
       btnregCancel.setBounds(80,170,100,30);
       btnregLogin.setBounds(200,170,100,30);
       btnregLogin.addActionListener(this);	
       
       btnLogout.setBounds(200,220,100,30);
       btnLogout.setEnabled(true);
   	   btnLogout.addActionListener(this);
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
	            }
	        } else {
	            JOptionPane.showMessageDialog(null, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    } else if (ev.getSource() == btnLogout) {
	        setVisible(false);
	        new Login().setVisible(true);
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