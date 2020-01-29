package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends JFrame {
	
	private JPanel contentPane;
	static JTextArea textArea;
	
	ArrayList<Socket> sockets = new ArrayList<>();
	
	/**
	 * Create the frame.
	 */
	public Server() {
		setResizable(false);
		setBackground(Color.GRAY);
		setTitle("Rock Paper Scissors Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 517);
		getContentPane().setLayout(new GridLayout(1, 1, 5, 5));
		
		/**
		 * Create content pane
		 */
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
	    TitledBorder title = new TitledBorder("Server Status");
	    title.setTitleColor(Color.yellow);
	    
	    /**
	     * Title label
	     */
	    JLabel titleLabel = new JLabel("Rock Paper Scissors");
	    titleLabel.setBackground(Color.GRAY);
	    titleLabel.setForeground(Color.YELLOW);
	    titleLabel.setFont(new Font("Agency FB", Font.BOLD, 20));
	    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    contentPane.add(titleLabel, BorderLayout.NORTH);
	    
	    /**
	     * Text area to display server status
	     */
	    textArea = new JTextArea();
	    JScrollPane scrollable = new JScrollPane(textArea); 
	    scrollable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    textArea.setLineWrap(true);
	    textArea.setForeground(Color.YELLOW);
	    textArea.setBackground(Color.DARK_GRAY);
	    contentPane.add(scrollable, BorderLayout.CENTER);
	    
	    /**
	     * Button to Start the server
	     * To stop the server used just has to exit
	     */
	    JButton startButton = new JButton("Start");
	    startButton.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		// Disable button
	    		startButton.setEnabled(false);
	    		startButton.setVisible(false);
	    		// Start server
	    		ServerStart ss = new ServerStart();
	    		Thread go = new Thread(ss);
	    		go.start();
	    	}
	    });
	    contentPane.add(startButton, BorderLayout.SOUTH);  
	}

	
}
