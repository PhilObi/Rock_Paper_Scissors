package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import utilities.Player;

public class Client extends JFrame {

	// Attributes
	private JPanel contentPane;
	private JTextField nickName;
	private Socket socket;
	private JTextField ipAddress;
	
	/**
	 * Create the frame.
	 */
	public Client() {
		/**
		 * Create content Pane.
		 */
		setResizable(false);
		setTitle("Rock Paper Scissors");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 508, 360);
		
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		/**
		 * Create the nickname label for the text field.
		 */
		JLabel nicknameLabel = new JLabel("Nickname:");
		nicknameLabel.setBounds(84, 184, 67, 15);
		nicknameLabel.setForeground(Color.WHITE);
		nicknameLabel.setFont(new Font("Arial", Font.BOLD, 13));
		
		/**
		 * Text field GUI for user to enter their nick name
		 */
		nickName = new JTextField();
		nickName.setForeground(Color.YELLOW);
		nickName.setBounds(155, 182, 190, 20);
		nickName.setBackground(Color.DARK_GRAY);
		nickName.setColumns(10);
		
		/**
		 * Game instructions text pane with the rules of rock,
		 * paper and scissors.
		 */
		JTextPane txtpnHowToPlay = new JTextPane();
		txtpnHowToPlay.setBounds(138, 208, 236, 118);
		txtpnHowToPlay.setFont(new Font("Arial", Font.PLAIN, 11));
		txtpnHowToPlay.setEditable(false);
		txtpnHowToPlay.setBackground(Color.GRAY);
		txtpnHowToPlay.setForeground(Color.DARK_GRAY);
		txtpnHowToPlay.setText("\tHOW TO PLAY\r\nEach round playes will"
				+ " select a hand signal that will represent the elements"
				+ " of the game rock, paper and scissors.\r\n\t- Rock wins"
				+ " against scissors.\r\n\t- Scissors wins against paper.\r\n\t-"
				+ " Paper wins against rock.\r\nSame picks result in a"
				+ " draw.");
		
		/**
		 * Label which contains image of rock paper and scissors symbols.
		 */
		JLabel startImage = new JLabel("");
		startImage.setBounds(25, 5, 454, 150);
		// Image Source: https://www.esquireme.com/sites/default/files/styles/full_img/public/images/2017/05/29/rock_paper_scissors__2x.png?itok=EB8fRWP9
		startImage.setIcon(new ImageIcon("res/rock_paper_scissors.png"));
		
		contentPane.setLayout(null);
		contentPane.add(nicknameLabel);
		contentPane.add(nickName);
		contentPane.add(startImage);
		contentPane.add(txtpnHowToPlay);
		
		/**
		 * IP address text field
		 */
		ipAddress = new JTextField();
		ipAddress.setForeground(Color.YELLOW);
		ipAddress.setBackground(Color.DARK_GRAY);
		ipAddress.setBounds(155, 161, 190, 20);
		contentPane.add(ipAddress);
		ipAddress.setColumns(10);
		
		JLabel ipLabel = new JLabel("Server IP:");
		ipLabel.setForeground(Color.WHITE);
		ipLabel.setFont(new Font("Arial", Font.BOLD, 13));
		ipLabel.setBounds(84, 161, 67, 15);
		contentPane.add(ipLabel);
		
		/**
		 * Play button creation and button event listener.
		 */
		JButton btnPlay = new JButton("PLAY");
		btnPlay.setBounds(348, 161, 67, 41);
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String nick = nickName.getText();
				String ip = ipAddress.getText();
				
				/**
				 * Set nickname to unknown if field is left empty
				 */
				if(nick == null || nick.equals("")) {
					nick = "Unknown";
				}else {
					nick = nickName.getText();
				}
				
				/**
				 * Check ip text field
				 */
				if(ip == null || ip.equals("")) {
					ip = "localhost";
				}else {
					ip = nickName.getText();
				}
				
				/**
				 * On button click create socket and use socket
				 * to start a object output stream.
				 */
				try {
					socket = new Socket(ip, 3333);
				} catch (UnknownHostException e1) {
					JOptionPane.showMessageDialog(null, 
							"Invalid IP Address(-__-)");
					return;
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, 
							"Connection Faliure...");
					return;
					//e1.printStackTrace();
				}
				
				// Set current GUI to invisible
				setVisible(false);
				
				// Create player object with nickname or unknown if blank
				Player player = new Player(nick);
				
				// play button click event
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						String ip = ipAddress.getText();
						try {
							/**
							 * Create game and pass in player Object and Socket
							 */
							RockPaperScissors frame = new RockPaperScissors(player,socket,ip);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			
		});
		
		btnPlay.setForeground(Color.GRAY);
		btnPlay.setBackground(Color.YELLOW);
		btnPlay.setFont(new Font("Arial", Font.BOLD, 12));
		contentPane.add(btnPlay);
	}
}
