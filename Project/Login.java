package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Login extends JFrame implements ActionListener{
	
    ArrayList<Integer> employeeNumber = new ArrayList<>();
    ArrayList<String> username = new ArrayList();
    ArrayList<String> password = new ArrayList();   
    int size; 
    
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	JPanel p1 = new JPanel();
	JPanel log = new JPanel();
	
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
		
		add(p1);
		//add(log);
		p1.add(lblUsername);
		p1.add(lblPassword);
		p1.add(txtUsername);
		p1.add(txtPassword);
		p1.add(lblUsernameA);
		p1.add(lblPasswordA);
		p1.add(txtUsernameA);
		p1.add(txtPasswordA);
		p1.add(btnLogin);
		p1.add(btnCancel);
		p1.isOpaque();
		//p1.setBackground(Color.CYAN);
		p1.setBounds(750,350,400,300);
		p1.setLayout(null);
		p1.setBorder(BorderFactory.createTitledBorder(""));

		//p1.setBorder(BorderFactory.createTitledBorder("Sign In"));
		//p1.setSize(screenSize.width, screenSize.height);
		
		//log.isOpaque();
		//log.setBackground(Color.CYAN);     
		//log.setSize(screenSize.width, screenSize.height);
		//log.setLayout(null);
	
		
		lblUsername.setBounds(50,50,100,30);
	
		txtUsername.setBounds(150,50,150,30);
	
		lblPassword.setBounds(50,100,100,30);

	    txtPassword.setBounds(150,100,150,30);
	
		btnLogin.setBounds(250,250,100,30);
	
		btnCancel.setBounds(50,250,100,30);		
		
		
		btnLogin.addActionListener(this); // Login Function @Override
		btnCancel.addActionListener(this); // Cancel Function @Override
	}
		
	public static void main(String[] args) {
		Login log = new Login();
		log.setVisible(true);
	
	}
	@Override
	public void actionPerformed(ActionEvent ev) {
	    if (ev.getSource() == btnCancel) {
	        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Form", JOptionPane.YES_NO_OPTION);
	        if (x == 0) {
	            System.exit(0);
	        }
	    } else if (ev.getSource() == btnLogin) {
	    	
	    	for (int i = 1; i <= size ; i++) {   
                String UserL,PassL;
                UserL = txtUsername.getText();
            PassL = txtPassword.getText();
                if (UserL.equalsIgnoreCase(username.get(i)) && PassL.equals(password.get(i))) {
        	SelectionCashier frame = new SelectionCashier();
	        frame.setVisible(true);
            JOptionPane.showMessageDialog(null, "Welcome, " + username, "Login", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
                }
                 else if (UserL.equalsIgnoreCase("admin") && PassL.equalsIgnoreCase("admin")) {
        	SelectionAdmin frames = new SelectionAdmin();
	        frames.setVisible(true);
            JOptionPane.showMessageDialog(null, "Welcome, " + username, "Login", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password!", "Invalid Login", JOptionPane.ERROR_MESSAGE);
        }
}}}}
                                                                                                                                                                                                                                   