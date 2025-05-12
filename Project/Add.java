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
	  
	  JPanel p1 = new JPanel(null); // Keep manual layout for precise positioning
	  
	  JLabel lblregUsername = new JLabel("Username: ");
	  JLabel lblregPassword = new JLabel("Password: ");
	  JTextField txtregUsername = new JTextField();
	  JPasswordField txtregPassword = new JPasswordField();
	  JButton btnregLogin = new JButton("Register");
	  JButton btnregCancel = new JButton("Cancel");
	  
	  private static final String FILE_NAME = "users.txt"; // File to store users
	  
	   //Image Icon
	    ImageIcon logo = new ImageIcon("./img/pos1.jpeg");
	    
	    //Backgound Image
	    JPanel brand = new JPanel();
	    ImageIcon BLogo = new ImageIcon("./img/pos3.jpeg");
	    Image img = BLogo.getImage();
	    Image newLogo = img.getScaledInstance(800,200,java.awt.Image.SCALE_SMOOTH);
	    ImageIcon Logo = new ImageIcon(newLogo);
	    JLabel bLogo = new JLabel();
	    
	public Add(Login loginInstance) {
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
        p1.setBorder(BorderFactory.createTitledBorder("Creating Cashier Account"));
        p1.setBackground(Color.WHITE);
        add(p1);
        
        // Position Components
        lblregUsername.setBounds(50, 50, 100, 30);
        txtregUsername.setBounds(150, 50, 150, 30);
        lblregPassword.setBounds(50, 100, 100, 30);
        txtregPassword.setBounds(150, 100, 150, 30);
     
        btnregCancel.setBounds(50, 250, 100, 30);
        btnregCancel.addActionListener(this);
   	   
        btnregLogin.setBounds(250, 250, 100, 30);
        btnregLogin.addActionListener(this);	
       
	    p1.add(lblregUsername);
	    p1.add(lblregPassword);
	    p1.add(txtregUsername);
	    p1.add(txtregPassword);
	    p1.add(btnregLogin);
	    p1.add(btnregCancel);   
	    
	    // Resize Listener for Dynamic Adjustments
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                brand.setBounds(getWidth() / 2 - 400, 100, 800, 200);
                p1.setBounds(getWidth() / 2 - 200, 350, 400, 300);
                p1.revalidate();
            }
        });

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
	    	 new SelectionAdmin().setVisible(true);
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