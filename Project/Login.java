package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener{
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	JLabel lblUsername = new JLabel("Username: ");
	JLabel lblPassword = new JLabel("Password: ");
	JTextField txtUsername = new JTextField(15);
	JPasswordField txtPassword = new JPasswordField(15);
	
	JLabel lblUsernameA = new JLabel("Username: ");
	JLabel lblPasswordA = new JLabel("Password: ");
	JTextField txtUsernameA = new JTextField();
	JPasswordField txtPasswordA = new JPasswordField();
	
	JButton btnLogin = new JButton("Login");
	JButton btnCancel = new JButton("Cancel");
	
	public Login() {
		setSize(screenSize.width, screenSize.height); // Set JFrame to full screen);
		setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("Login Form");
		
		add(lblUsername);
		add(lblPassword);
		add(txtUsername);
		add(txtPassword);
		add(lblUsernameA);
		add(lblPasswordA);
		add(txtUsernameA);
		add(txtPasswordA);
		add(btnLogin);
		add(btnCancel);
		
		lblUsername.setBounds(800,400,100,30);
		txtUsername.setBounds(880,400,150,30);
		lblPassword.setBounds(800,450,100,30);
	    txtPassword.setBounds(880,450,150,30);
		btnLogin.setBounds(800,500,100,30);
		btnCancel.setBounds(920,500,100,30);		
		
		
		btnLogin.addActionListener(this); // Login Function @Override
		btnCancel.addActionListener(this); // Cancel Function @Override
	}
	
	public static void main(String[] args) {
		Login login = new Login();
		login.setVisible(true);
		SwingUtilities.invokeLater(() -> new Login().setVisible(true));
	}
	@Override
	
	public void actionPerformed(ActionEvent ev) {
	    if (ev.getSource() == btnCancel) {
	        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Form", JOptionPane.YES_NO_OPTION);
	        if (x == 0) {
	            System.exit(0);
	        }
	    } else if (ev.getSource() == btnLogin) {
	       
	    	String username,password;
	        username = txtUsername.getText();
	        password = txtPassword.getText();
	  
	        if (username.equalsIgnoreCase("cashier") && password.equalsIgnoreCase("cashier")) {
	        	SelectionCashier frame = new SelectionCashier();
		        frame.setVisible(true);
	            JOptionPane.showMessageDialog(null, "Welcome, " + username, "Login", JOptionPane.INFORMATION_MESSAGE);
	            this.dispose();
	        } else if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
	        	SelectionAdmin frames = new SelectionAdmin();
		        frames.setVisible(true);
	            JOptionPane.showMessageDialog(null, "Welcome, " + username, "Login", JOptionPane.INFORMATION_MESSAGE);
	            this.dispose();
	        } else {
	            JOptionPane.showMessageDialog(null, "Invalid username or password!", "Invalid Login", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}}
                                                                                                                                                                                                                                   