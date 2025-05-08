package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Add extends JFrame implements ActionListener, ItemListener, ChangeListener {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	  private Login loginInstance;

	  JLabel lblregUsername = new JLabel("Username: ");
	  JLabel lblregPassword = new JLabel("Password: ");
	  JTextField txtregUsername = new JPasswordField(15);
	  JPasswordField txtregPassword = new JPasswordField(15);
	  JButton btnregLogin = new JButton("Register");
	  JButton btnregCancel = new JButton("Cancel");
	  
	public Add(Login loginInstance) {
		
	this.loginInstance = loginInstance; // Store the reference
	
	setSize(screenSize.width, screenSize.height); // Set JFrame to full screen);
	setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
	setLocationRelativeTo(null);
	   setLayout(null);
	   setTitle("Add Account");
	   
	   add(lblregUsername);
       add(lblregPassword);
       add(txtregUsername);
       add(txtregPassword);
       add(btnregLogin);
       add(btnregCancel);   
	   
       lblregUsername.setBounds(70,80,150,30);
       txtregUsername.setBounds(150,70,150,30);
       lblregPassword.setBounds(70,120,150,30);
       txtregPassword.setBounds(150,120,150,30);
       btnregCancel.setBounds(80,170,100,30);
       btnregLogin.setBounds(200,170,100,30);
      
       btnregLogin.addActionListener(this);	
	   

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
	                loginInstance.username.add(user);
	                loginInstance.password.add(pass);
	                JOptionPane.showMessageDialog(null, "User registered successfully!", "Registration", JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                JOptionPane.showMessageDialog(null, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
	}