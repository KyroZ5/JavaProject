package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class SelectionCashier extends JFrame implements ActionListener{
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	JPanel newLogin = new JPanel();
	ImageIcon ALogo = new ImageIcon("./img/person-add.png");
	Image imgA = ALogo.getImage();
	Image newALogo = imgA.getScaledInstance(80,80,java.awt.Image.SCALE_SMOOTH);
	ImageIcon aLogo = new ImageIcon(newALogo);
	JButton btnAdd = new JButton(aLogo);
	
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
	
	JLabel lblAdd = new JLabel("Create");
	JLabel lblAdd2 = new JLabel("Account");
	JLabel lblCashier = new JLabel("Cashier");
	JLabel lblAdmin = new JLabel("Admin");
	JLabel lblInven = new JLabel("Inventory");
	
	JButton btnLogout = new JButton("Logout");
	
    public SelectionCashier() {
    	setSize(screenSize.width, screenSize.height); // Set JFrame to full screen);
		setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setTitle("Cashier System");
        
        add(newLogin);
        newLogin.add(btnAdd);
        newLogin.add(btnCashier);
        newLogin.add(btnAdmin);
        newLogin.add(btnInventory);
        newLogin.add(btnLogout);
        newLogin.add(lblAdd);
        newLogin.add(lblAdd2);
        newLogin.add(lblCashier);
        newLogin.add(lblAdmin);
        newLogin.add(lblInven);
        newLogin.isOpaque();
        newLogin.setBackground(Color.CYAN);     
        newLogin.setBounds(700,400,460,200);
		newLogin.setLayout(null);
		newLogin.setBorder(BorderFactory.createTitledBorder(""));
		btnAdd.setBounds(40,40,80,80);
		//btnAdd.setText("");
		btnCashier.setBounds(140,40,80,80);
		
		//btnCashier.setText("");
		btnAdmin.setBounds(240,40,80,80);
		//btnAdmin.setText("");
		btnInventory.setBounds(340,40,80,80);
		//btnInventory.setText("");
		btnLogout.setBounds(340,160,100,30);
		lblAdd.setBounds(60,95,120,80);
		lblAdd.setEnabled(false); 
		lblAdd2.setBounds(55,110,120,80);
		lblAdd2.setEnabled(false); 
		lblCashier.setBounds(157,95,120,80);
		lblAdmin.setBounds(260,95,120,80);
		lblAdmin.setEnabled(false); 
		lblInven.setBounds(350,95,120,80);
		lblInven.setEnabled(false); 
		
		btnAdd.setEnabled(false); 
		btnCashier.setEnabled(true); 
		btnAdmin.setEnabled(false);
		btnInventory.setEnabled(false);
		
		btnAdd.addActionListener(this); 
		btnCashier.addActionListener(this); 
		btnAdmin.addActionListener(this);
		btnInventory.addActionListener(this);
		
		btnLogout.addActionListener(this);
    }

	
    public static void main(String[] args) {
    	SelectionCashier login = new SelectionCashier();
        login.setVisible(true);
	
    }

    @Override
   	public void actionPerformed(ActionEvent ev) {
   		if(ev.getSource()==btnAdd) {
   			//JOptionPane.showMessageDialog(null, "Welcome ","Login",JOptionPane.INFORMATION_MESSAGE);
   		}else if(ev.getSource()==btnCashier) {
   			Cashier login = new Cashier();
   			login.setVisible(true);
   			JOptionPane.showMessageDialog(null, "Welcome "+ "Cashier","Login Successful",JOptionPane.INFORMATION_MESSAGE);
   			setVisible(false);
   		}else if(ev.getSource()==btnAdmin) {
   			//JOptionPane.showMessageDialog(null, "Welcome ","Login",JOptionPane.INFORMATION_MESSAGE);
   		}else if(ev.getSource()==btnInventory) {
   			//JOptionPane.showMessageDialog(null, "Welcome ","Login",JOptionPane.INFORMATION_MESSAGE);
   		}else if(ev.getSource()==btnLogout) {
   			Login log = new Login();
   			log.setVisible(true);
   			//JOptionPane.showMessageDialog(null, "Welcome ","Admin",JOptionPane.INFORMATION_MESSAGE);
   			setVisible(false);
   		}
   	}
   }

