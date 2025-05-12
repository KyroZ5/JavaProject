package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Admin extends JFrame implements ActionListener, ItemListener, ChangeListener {
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	JButton btnAdd = new JButton("Add");
	JButton btnEdit = new JButton("Edit");
	JButton btnDel = new JButton("Delete");
	
	// Panel 1
	JPanel p1 = new JPanel();


	Color myColor = new Color(193, 234, 242); 
	 ImageIcon BLogo = new ImageIcon("./img/logo-dark-transparent.png");
	 Image img = BLogo.getImage();
	 Image newLogo = img.getScaledInstance(350,80,java.awt.Image.SCALE_SMOOTH);
	 ImageIcon Logo = new ImageIcon(newLogo);
	 JLabel bLogo = new JLabel();
	 ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
	 Font font = new Font("Montserrat", Font.BOLD, 15);

    public Admin() {
    	setSize(600,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin");
        setLayout(null);
        setResizable(false);
        setIconImage(logo.getImage());
        
     

	   add(p1);


	   // Panel 1
	   p1.setLayout(null);
	   p1.setBorder(BorderFactory.createTitledBorder(""));
	   p1.setBounds(10, 50, 565, 500);

	   
	   // Components
	   add(btnAdd);  
	   add(btnEdit);  
	   add(btnDel);  
	   
	   btnAdd.setBounds(5, 5, 100, 20);
	   btnAdd.addActionListener(this);
  	   
       btnEdit.setBounds(120, 5, 100, 20);
       btnEdit.addActionListener(this);	
       
       
       btnDel.setBounds(235, 5, 100, 20);
       btnDel.addActionListener(this);	
	}
	
	public static void main(String[] args) {
		Admin login = new Admin();
        login.setVisible(true);
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
   		if(ev.getSource()==btnAdd) {
   		 Login loginInstance = new Login(); // Ensure a Login instance exists
         new Add(loginInstance).setVisible(true); // Pass Login reference
   			
   			//setVisible(false);
   		}else if(ev.getSource()==btnEdit) {
   			
   			setVisible(false);
   		}
   	}
  }

