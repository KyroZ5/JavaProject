package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class SelectionCashier extends JFrame implements ActionListener{
	
	JPanel newLogin = new JPanel();
	
	ImageIcon ALogo = new ImageIcon("./img/next.png");
	Image imgA = ALogo.getImage();
	Image newALogo = imgA.getScaledInstance(80,80,java.awt.Image.SCALE_SMOOTH);
	ImageIcon aLogo = new ImageIcon(newALogo);
	JButton btnLogout = new JButton(aLogo);
	
	ImageIcon BLogo = new ImageIcon("./img/cashier.jpg");
	Image imgB = BLogo.getImage();
	Image newBLogo = imgB.getScaledInstance(80,80,java.awt.Image.SCALE_SMOOTH);
	ImageIcon bLogo = new ImageIcon(newBLogo);
	JButton btnCashier = new JButton(bLogo);
	
	ImageIcon CLogo = new ImageIcon("./img/admin.png");
	Image imgC = CLogo.getImage();
	Image newCLogo = imgC.getScaledInstance(75,80,java.awt.Image.SCALE_SMOOTH);
	ImageIcon cLogo = new ImageIcon(newCLogo);
	JButton btnAdmin = new JButton(cLogo);
	
	ImageIcon DLogo = new ImageIcon("./img/inventory.png");
	Image imgD = DLogo.getImage();
	Image newDLogo = imgD.getScaledInstance(80,80,java.awt.Image.SCALE_SMOOTH);
	ImageIcon dLogo = new ImageIcon(newDLogo);
	JButton btnInventory = new JButton(dLogo);
	
	JLabel lblLogout = new JLabel("Logout");
	JLabel lblCashier = new JLabel("Cashier");
	JLabel lblAdmin = new JLabel("Admin");
	JLabel lblInven = new JLabel("Inventory");
	Color myColor = new Color(193, 234, 242); 
    Font font = new Font("Montserrat", Font.BOLD, 15);
    ImageIcon logo = new ImageIcon("./img/logo-icon-dark-transparent.png");
    public SelectionCashier() {
    	setSize(470, 235);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setTitle("Pentagram");
        setIconImage(logo.getImage());
        add(newLogin);

        newLogin.add(btnCashier);
        newLogin.add(btnAdmin);
        newLogin.add(btnInventory);
        newLogin.add(btnLogout);
        newLogin.add(lblLogout);
        newLogin.add(lblCashier);
        newLogin.add(lblAdmin);
        newLogin.add(lblInven);
        newLogin.isOpaque();
        newLogin.setBackground(myColor);     
        newLogin.setBounds(0,0,460,200);
	    newLogin.setLayout(null);
	    newLogin.setBorder(BorderFactory.createTitledBorder(""));
	    btnInventory.setBounds(40,40,80,80);
	    btnCashier.setBounds(140,40,80,80);
	    btnAdmin.setBounds(240,40,80,80);
	    lblInven.setBounds(45,95,120,80);
	    lblInven.setEnabled(false); 
        lblInven.setFont(font);
	    lblCashier.setBounds(157,95,120,80);
        lblCashier.setFont(font);
	    lblAdmin.setBounds(260,95,120,80);
	    lblAdmin.setEnabled(false); 
        lblAdmin.setFont(font);
	    btnLogout.setBounds(350,40,80,80);
        lblLogout.setBounds(360,95,120,80);
	    lblLogout.setEnabled(true); 
        lblLogout.setFont(font);
	    btnLogout.setEnabled(true); 
	    btnCashier.setEnabled(true); 
	    btnAdmin.setEnabled(false);
	    btnInventory.setEnabled(false);
	    btnLogout.addActionListener(this); 
	    btnCashier.addActionListener(this); 
	    btnAdmin.addActionListener(this);
	    btnInventory.addActionListener(this);
		
    }

	
    public static void main(String[] args) {
    	SelectionCashier login = new SelectionCashier();
        login.setVisible(true);
	
    }

    @Override
   	public void actionPerformed(ActionEvent ev) {
   		if(ev.getSource()==btnLogout) {
   			Login log = new Login();
   			log.setVisible(true);
   			setVisible(false);
 
   		}else if(ev.getSource()==btnCashier) {
   			Cashier login = new Cashier();
   			login.setVisible(true);
   			setVisible(false);
   		}
   	}
   }

