package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Add extends JFrame implements ActionListener, ItemListener, ChangeListener {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	// Panel 1
	JPanel P1 = new JPanel();
	
	// Panel 2
	JPanel P2 = new JPanel();
	
	// Panel 3
	JPanel P3 = new JPanel();
	
	// Panel 4
	JPanel P4 = new JPanel();
	
	public Add() {
	   setSize(screenSize.width, screenSize.height); // Set JFrame to full screen);
	   setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
	   setLocationRelativeTo(null);
	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   setLayout(null);
	   setTitle("Add Account");
	   
	   add(P1);
	   add(P2);
	   add(P3);
	   add(P4);
	   
	   // Panel 1
	   P1.setLayout(null);
	   P1.setBorder(BorderFactory.createTitledBorder("Panel 1"));
	   P1.setBounds(10, 10, 1000, 500);
	   // Panel 2
	   P2.setLayout(null);
	   P2.setBorder(BorderFactory.createTitledBorder("Panel 2"));
	   P2.setBounds(1050, 10, 500, 500);
	   // Panel 3
	   P3.setLayout(null);
	   P3.setBorder(BorderFactory.createTitledBorder("Panel 3"));
	   P3.setBounds(10, 525, 1000, 300);
	   // Panel 4
	   P4.setLayout(null);
	   P4.setBorder(BorderFactory.createTitledBorder("Panel 4"));
	   P4.setBounds(1050, 525, 500, 300);
	   

	}
	
	public static void main(String[] args) {
		Add login = new Add();
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
