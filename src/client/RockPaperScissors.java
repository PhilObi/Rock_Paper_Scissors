package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import utilities.InputListener;
import utilities.Message;
import utilities.Player;

import javax.swing.UIManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;

public class RockPaperScissors extends JFrame implements PropertyChangeListener{
	// Constants
	final private String INIT = "init";
	final private String CHAT = "chat";
	final private String STATUS = "status";
	final private String ROCK = "rock";
	final private String PAPER = "paper";
	final private String SCISSORS = "scissors";
	final private String REVEAL = "reveal";
	final private String LEAVING = "leaving";
	
	// Attributes
	private JPanel contentPane;
	private JTextField chatMessage;
	private ObjectOutputStream oos;
	private InputListener lis;
	private JTextArea textArea;
	private JTextArea textArea1;
	private Message message;
	private JLabel player2;
	private JLabel playerRock2;
	private JLabel playerPaper2;
	private JLabel playerScissors2;
	private Player player;
	private JLabel playerRock1;
	private JLabel playerPaper1;
	private JLabel playerScissors1;
	private boolean lockedIn = false;
	private JLabel resultLabel;
	private JButton lockChoice;
	private JButton rock;
	private JButton paper;
	private JButton scissors;
	private JLabel rematch;
	private JButton againYes;
	private JButton againNo;
	private Socket socket;
	private String ip;

	/**
	 * Create the frame.
	 */
	public RockPaperScissors(Player player, Socket socket, String ip) {
		this.player = player;
		this.socket = socket;
		this.ip = ip;
		/**
		 * Create Object Output Stream with passed in socket
		 */
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		lis = new InputListener(socket, this);
		Thread t = new Thread(lis);
		t.start();
		
		/**
		 * Create content pane
		 */
		setTitle("Rock Paper Scissors");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 755, 450);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		/**
		 * Text field for chat
		 */
		chatMessage = new JTextField();
		chatMessage.setForeground(Color.YELLOW);
		chatMessage.setBackground(Color.DARK_GRAY);
		chatMessage.setBounds(414, 375, 230, 24);
		chatMessage.setFont(new Font("Arial", Font.PLAIN, 11));
		chatMessage.setHorizontalAlignment(SwingConstants.LEFT);
		chatMessage.setColumns(10);
		contentPane.add(chatMessage);
		
		/**
		 * ScrollPane for text area
		 */
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(414, 121, 315, 249);
		contentPane.add(scrollPane);
		
		/**
		 * Text area for chat
		 */
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.YELLOW);
		scrollPane.setViewportView(textArea);
		
		/**
		 * Label border
		 */
		JLabel label = new JLabel("");
		label.setBackground(Color.WHITE);
		label.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label.setBounds(404, 104, 335, 306);
	    label.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), 
	    		"Chat", TitledBorder.LEADING, TitledBorder.TOP,
	    		null, new Color(255, 255, 0)));
		contentPane.add(label);
		
		/**
		 * Button used to send send chat
		 */
		JButton sendMessage = new JButton("Send");
		sendMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Retrieve user message from textfield
				String msg =  chatMessage.getText();
				// Check if chatMessage is empty or null
				if(msg == null || msg.equals("")){
					return;
				}
				else {
					// add to message object with current players info
					message = new Message(player, msg, CHAT);
					
					// Send message 
					try {
						oos.writeObject(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					// Output the message to the textArea
					textArea.append("[" + player.getNickName() + "] " + 
							message.getMessage() + "\n");
					
					// Clear chat message
					chatMessage.setText("");
				}
			}
		});
		sendMessage.setBounds(650, 375, 79, 25);
		sendMessage.setBackground(Color.LIGHT_GRAY);
		contentPane.add(sendMessage);
		
		/**
		 * Player 1 name label
		 */
		JLabel player1 = new JLabel("");
		player1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		player1.setForeground(Color.YELLOW);
		player1.setBounds(36, 11, 118, 24);
		player1.setText(player.getNickName());
		contentPane.add(player1);
		
		/**
		 * Player 2 name label
		 */
		player2 = new JLabel((String) null);
		player2.setHorizontalAlignment(SwingConstants.TRAILING);
		player2.setForeground(Color.YELLOW);
		player2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		player2.setBounds(243, 11, 118, 24);
		contentPane.add(player2);
		
		JLabel versus = new JLabel("VS");
		versus.setForeground(Color.YELLOW);
		versus.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		versus.setBounds(189, 29, 27, 14);
		contentPane.add(versus);
		
		/**
		 * Status label border
		 */
		JLabel label_1 = new JLabel("");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Status", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(255, 255, 0)));
		label_1.setBackground(Color.WHITE);
		label_1.setBounds(404, 11, 335, 90);
		contentPane.add(label_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(414, 25, 315, 68);
		contentPane.add(scrollPane_1);
		
		/**
		 * Text area for game status
		 */
		textArea1 = new JTextArea();
		textArea1.setLineWrap(true);
		textArea1.setEditable(false);
		textArea1.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textArea1.setBackground(Color.DARK_GRAY);
		textArea1.setForeground(Color.YELLOW);
		scrollPane_1.setViewportView(textArea1);
		
		/**
		 * Element images
		 */
		playerRock1 = new JLabel("");
		playerRock1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.PNG")));
		playerRock1.setBounds(22, 105, 174, 160);
		playerRock1.setVisible(false);
		contentPane.add(playerRock1);

		playerRock2 = new JLabel("");
		playerRock2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.PNG")));
		playerRock2.setBounds(230, 105, 174, 160);
		playerRock2.setVisible(false);
		contentPane.add(playerRock2);
		
		playerPaper1 = new JLabel("");
		playerPaper1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.PNG")));
		playerPaper1.setBounds(22, 105, 174, 160);
		playerPaper1.setVisible(false);
		contentPane.add(playerPaper1);

		playerPaper2 = new JLabel(""); 
		playerPaper2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.PNG")));
		playerPaper2.setBounds(230, 105, 174, 160);
		playerPaper2.setVisible(false);
		contentPane.add(playerPaper2);
		
		playerScissors1 = new JLabel("");
		playerScissors1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissors.PNG")));
		playerScissors1.setBounds(22, 105, 174, 160);
		playerScissors1.setVisible(false);
		contentPane.add(playerScissors1);

		playerScissors2 = new JLabel("");
		playerScissors2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissors.PNG")));
		playerScissors2.setBounds(230, 105, 174, 160);
		playerScissors2.setVisible(false);
		contentPane.add(playerScissors2);
		
		/**
		 * Rock button on hover displays image
		 */
		rock = new JButton("Rock");
		rock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Set paper or scissors to invisible
				playerPaper1.setVisible(false);
				playerScissors1.setVisible(false);
				
				// set rock visibility to true
				playerRock1.setVisible(true);
				player.setChoice(ROCK);
			}
		});
		rock.setForeground(Color.DARK_GRAY);
		rock.setBackground(Color.YELLOW);
		rock.setBounds(22, 288, 89, 44);
		contentPane.add(rock);
		
		/**
		 * Paper button
		 */
		paper = new JButton("Paper");
		paper.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Set rock or scissors to invisible
				playerRock1.setVisible(false);
				playerScissors1.setVisible(false);
				
				// Set paper to visible
				playerPaper1.setVisible(true);
				player.setChoice(PAPER);
			}
		});
		paper.setForeground(Color.DARK_GRAY);
		paper.setBackground(Color.YELLOW);
		paper.setBounds(154, 288, 89, 44);
		contentPane.add(paper);
		
		/**
		 * Scissors button
		 */
		scissors = new JButton("Scissors");
		scissors.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Set rock or paper to invisible
				playerRock1.setVisible(false);
				playerPaper1.setVisible(false);
				
				// Set scissors to visible
				playerScissors1.setVisible(true);
				player.setChoice(SCISSORS);
			}
		});
		scissors.setForeground(Color.DARK_GRAY);
		scissors.setBackground(Color.YELLOW);
		scissors.setBounds(284, 288, 89, 44);
		contentPane.add(scissors);
		
		
		
		/**
		 * Lock in choice and run game options
		 */
		lockChoice = new JButton("Lock In Choice");
		lockChoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(player.getChoice() == null) {
					JOptionPane.showMessageDialog(null, 
							"Choice not selected!");
					return;
				}else {
					String nickname = player.getNickName();
					String choice = player.getChoice();
					int score = player.getScore();
					
					Player p = new Player(nickname, score, choice);
					// Send status message
					message = new Message(p, " has chosen ", STATUS);
					textArea1.append("You have chosen " + choice + "\n");
					lockedIn = true;
					lockChoice.setBackground(Color.GREEN);
					
					try {
						oos.writeObject(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		lockChoice.setForeground(Color.DARK_GRAY);
		lockChoice.setBackground(Color.LIGHT_GRAY);
		lockChoice.setBounds(111, 362, 174, 37);
		contentPane.add(lockChoice);
		
		resultLabel = new JLabel("---");
		resultLabel.setForeground(Color.YELLOW);
		resultLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setBounds(36, 56, 325, 37);
		contentPane.add(resultLabel);
		
		rematch = new JLabel("Play Again?");
		rematch.setHorizontalAlignment(SwingConstants.CENTER);
		rematch.setForeground(Color.YELLOW);
		rematch.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		rematch.setBounds(140, 330, 112, 37);
		rematch.setVisible(false);
		contentPane.add(rematch);
		
		againYes = new JButton("Yes");
		againYes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset every thing for a new game
				rock.setEnabled(true);
				paper.setEnabled(true);
				scissors.setEnabled(true);
				lockChoice.setVisible(true);
				rematch.setVisible(false);
				againYes.setVisible(false);
				againNo.setVisible(false);
				playerRock1.setVisible(false);
				playerRock2.setVisible(false);
				playerPaper1.setVisible(false);
				playerPaper2.setVisible(false);
				playerScissors1.setVisible(false);
				playerScissors2.setVisible(false);
				lockedIn = false;
				resultLabel.setText("---");
				lockChoice.setBackground(Color.LIGHT_GRAY);
				
				player.setChoice(null);
				
			}
		});
		againYes.setForeground(Color.DARK_GRAY);
		againYes.setBackground(Color.GREEN);
		againYes.setBounds(111, 362, 79, 37);
		againYes.setVisible(false);
		contentPane.add(againYes);
		
		againNo = new JButton("No");
		againNo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String bye = " has left the game.";
				
				message = new Message(player, bye, LEAVING);
				try {
					oos.writeObject(message);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
				// Close Game
				System.exit(0);
			}
		});
		againNo.setForeground(Color.DARK_GRAY);
		againNo.setBackground(Color.RED);
		againNo.setBounds(200, 362, 85, 37);
		againNo.setVisible(false);
		contentPane.add(againNo);
		
		/**
		 * Send out an introduction message object when
		 * the gui is first created.
		 */
		String introMsg = " has joined the Game!\nYou may now pick an element!";
		Message message = new Message(player, introMsg, INIT);
		
		try {
			oos.writeObject(message);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	@Override
	public synchronized void propertyChange(PropertyChangeEvent event) {
		// Read in the new message
		Message newMessage = (Message)event.getNewValue();
		
		// Current player choice
		String cChoice = player.getChoice();
		// Current player nickname
		String cNickname = player.getNickName();
		//Current score
		int cScore = player.getScore();
		
		// Received nickname
		String rNickname = newMessage.getPlayer().getNickName();

		// Received message
		String rMessage = newMessage.getMessage();
		// Received message type
		String rType = newMessage.getType();
		// Received Choice
		String rChoice = newMessage.getPlayer().getChoice();
		
		// String to hold result to send back to other player
		String result = null;
		
		if(rType.equals(INIT)) {
			// Initialize player label fields
			player2.setText(rNickname);
						
			// Output the status message to the textArea1
			textArea1.append(rNickname + rMessage + "\n");
			
		}else if(rType.equals(STATUS)) {
			// Check if recieving player has locked in 
			if(lockedIn == false) {
				textArea1.append("Opponent has made a choice!\n");
			}else {
				if(rChoice.equals(ROCK)) {
					playerRock2.setVisible(true);
				}else if(rChoice.equals(PAPER)) {
					playerPaper2.setVisible(true);
				}else {
					playerScissors2.setVisible(true);
				}
				
				textArea1.append(rNickname + " has chosen " + rChoice + "\n");
				// Game logic 
				if(cChoice.equals(rChoice)) {
					result = "It's a tie!!!!";
					
				}else if(cChoice.equals(ROCK)) {
					if(rChoice.equals(SCISSORS)) {
						result = cNickname + " Wins!";
						
					}else if(rChoice.equals(PAPER)) {
						result = rNickname + " Wins!";
						
					}
				}else if(cChoice.equals(PAPER)) {
					if(rChoice.equals(ROCK)) {
						result = cNickname + " Wins!";
						
					}else if(rChoice.equals(SCISSORS)) {
						result = rNickname + " Wins!";
						
					}
				}else if(cChoice.equals(SCISSORS)) {
					if(rChoice.equals(PAPER)) {
						result = cNickname + " Wins!";
						
					}else if(rChoice.equals(ROCK)) {
						result = rNickname + " Wins!";
						
					}
				}else {
					// Do nothing 
				}
				
				
				resultLabel.setText(result);
				
				// Send winner information to other player
				Player p = new Player(cNickname, cScore, cChoice);
				message = new Message(p, result, REVEAL);
				try {
					oos.writeObject(message);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				// update Buttons
				rock.setEnabled(false);
				paper.setEnabled(false);
				scissors.setEnabled(false);
				lockChoice.setVisible(false);
				rematch.setVisible(true);
				againYes.setVisible(true);
				againNo.setVisible(true);
				
			}
			
		}else if(rType.equals(CHAT)) {
			// Output the message to the textArea
			textArea.append("[" + rNickname + "] " + rMessage + "\n");
		
		/**
		 *  Reveal message contains information about winner and makes,
		 *  necessary changes to gui
		 */
		}else if(rType.equals(REVEAL)){
			if(rChoice.equals(ROCK)) {
				playerRock2.setVisible(true);
			}else if(rChoice.equals(PAPER)) {
				playerPaper2.setVisible(true);
			}else{
				playerScissors2.setVisible(true);
			}
			
			textArea1.append(rNickname + " has chosen " + rChoice + "\n");
			// Displayer Winner 
			resultLabel.setText(rMessage);
			
			// Update buttons
			rock.setEnabled(false);
			paper.setEnabled(false);
			scissors.setEnabled(false);
			lockChoice.setVisible(false);
			rematch.setVisible(true);
			againYes.setVisible(true);
			againNo.setVisible(true);
			
		}else if(rType.equals(LEAVING)) {
			// Initialize player label fields
			player2.setText("");
									
			// Output the status message to the textArea1
			textArea1.append(rNickname + rMessage + "\n");
			
			// Close sockets and all related streams
			try {
				oos.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Make listener null
			lis = null;
			
			/**
			 * Reconnect to the server 
			 */
			try {
				socket = new Socket(ip, 3333);
				oos = new ObjectOutputStream(socket.getOutputStream());
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, 
						"Error");
				return;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, 
						"Connection Faliure...");
				return;
			}
			
			lis = new InputListener(socket, this);
			Thread t = new Thread(lis);
			t.start();
			
			/**
			 * Send out an introduction message object when
			 * the gui is first created.
			 */
			String introMsg = " has joined the Game!\nYou may now pick an element!";
			Message message = new Message(player, introMsg, INIT);
			
			try {
				oos.writeObject(message);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
				
		}
		
	}
}
