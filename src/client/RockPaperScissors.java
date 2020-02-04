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
import javax.swing.ImageIcon;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;

public class RockPaperScissors extends JFrame implements PropertyChangeListener{
	// Constants
	final private String INIT = "init";
	final private String CHAT = "chat";
	final private String STATUS = "status";
	final private String GAME = "game";
	final private String ROCK = "rock";
	final private String PAPER = "paper";
	final private String SCISSORS = "scissors";
	
	// Attributes
	private JPanel contentPane;
	private JTextField chatMessage;
	private ObjectOutputStream oos;
	private InputListener lis;
	private JTextArea textArea;
	private JTextArea textArea1;
	private Message message;
	private JLabel player2;
	private JLabel playerScore1;
	private JLabel playerScore2;
	private JLabel playerRock2;
	private JLabel playerPaper2;
	private JLabel playerScissors2;
	private Player player;

	/**
	 * Create the frame.
	 */
	public RockPaperScissors(Player player, Socket socket) {
		this.player = player;
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
		 * Player 1 Score label
		 */
		playerScore1 = new JLabel("");
		playerScore1.setForeground(Color.YELLOW);
		playerScore1.setHorizontalAlignment(SwingConstants.CENTER);
		playerScore1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		playerScore1.setBounds(59, 40, 48, 38);
		playerScore1.setText(Integer.toString(player.getScore()));
		contentPane.add(playerScore1);
		
		/**
		 * Player 2 Score label
		 */
		playerScore2 = new JLabel("");
		playerScore2.setForeground(Color.YELLOW);
		playerScore2.setHorizontalAlignment(SwingConstants.CENTER);
		playerScore2.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		playerScore2.setBounds(291, 40, 48, 38);
		contentPane.add(playerScore2);
		
		/**
		 * Element images
		 */
		JLabel playerRock1 = new JLabel("");
		playerRock1.setIcon(new ImageIcon("res/rock.PNG"));
		playerRock1.setBounds(22, 105, 174, 160);
		playerRock1.setVisible(false);
		contentPane.add(playerRock1);

		playerRock2 = new JLabel("");
		playerRock2.setIcon(new ImageIcon("res/rock.PNG"));
		playerRock2.setBounds(230, 105, 174, 160);
		playerRock2.setVisible(false);
		contentPane.add(playerRock2);
		
		JLabel playerPaper1 = new JLabel("");
		playerPaper1.setIcon(new ImageIcon("res/paper.PNG"));
		playerPaper1.setBounds(22, 105, 174, 160);
		playerPaper1.setVisible(false);
		contentPane.add(playerPaper1);

		playerPaper2 = new JLabel("");
		playerPaper2.setIcon(new ImageIcon("res/paper.PNG"));
		playerPaper2.setBounds(230, 105, 174, 160);
		playerPaper2.setVisible(false);
		contentPane.add(playerPaper2);
		
		JLabel playerScissors1 = new JLabel("");
		playerScissors1.setIcon(new ImageIcon("res/scissors.PNG"));
		playerScissors1.setBounds(22, 105, 174, 160);
		playerScissors1.setVisible(false);
		contentPane.add(playerScissors1);

		playerScissors2 = new JLabel("");
		playerScissors2.setIcon(new ImageIcon("res/scissors.PNG"));
		playerScissors2.setBounds(230, 105, 174, 160);
		playerScissors2.setVisible(false);
		contentPane.add(playerScissors2);
		
		/**
		 * Rock button on hover displays image
		 */
		JButton rock = new JButton("Rock");
		rock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Set paper or scissors to invisible
				playerPaper1.setVisible(false);
				playerScissors1.setVisible(false);
				
				// set rock visibility to true
				playerRock1.setVisible(true);
				
				// Set player choice
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
		JButton paper = new JButton("Paper");
		paper.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Set rock or scissors to invisible
				playerRock1.setVisible(false);
				playerScissors1.setVisible(false);
				
				// Set paper to visible
				playerPaper1.setVisible(true);
				
				// Set player choice to paper
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
		JButton scissors = new JButton("Scissors");
		scissors.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Set rock or paper to invisible
				playerRock1.setVisible(false);
				playerPaper1.setVisible(false);
				
				// Set scissors to visible
				playerScissors1.setVisible(true);
				
				// Set player choice to scissors
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
		JButton lockChoice = new JButton("Lock In Choice");
		lockChoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(player.getChoice() == null) {
					JOptionPane.showMessageDialog(null, 
							"Choice not selected!");
					return;
				}else {
					String choice = player.getChoice();
					// Send status message
					message = new Message(player, " has chosen ", STATUS);
					textArea1.append("You have chosen " + choice + "\n");
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
	public void propertyChange(PropertyChangeEvent event) {
		// Read in the new message
		Message newMessage = (Message)event.getNewValue();
		
		// Received nickname
		String rNickname = newMessage.getPlayer().getNickName();
		// Received score
		String rScore = Integer.toString(newMessage.getPlayer().getScore());
		// Received message
		String rMessage = newMessage.getMessage();
		// Received message type
		String rType = newMessage.getType();
		// Received Choice
		String rChoice = newMessage.getPlayer().getChoice();

		
		if(rType.equals(INIT)) {
			// Initialize player label fields
			player2.setText(rNickname);
			
			// Initialize player scores
			playerScore2.setText(rScore);
						
			// Output the status message to the textArea1
			textArea1.append(rNickname + rMessage + "\n");
			
		}else if(rType.equals(STATUS)) {
			textArea1.append(rNickname + rMessage + rChoice + "\n");
//			if(player.getChoice() != null ) {
//				// Reveal player choice 
//				if(rMessage.equals(ROCK)) {
//					playerRock2.setVisible(true);
//				}else if(rMessage.equals(PAPER)) {
//					playerPaper2.setVisible(true);
//				}else if(rMessage.equals(SCISSORS)) {
//					playerScissors2.setVisible(true);
//				}else {
//					// Do nothing 
//				}
//				
//				String winner = getResult(player.getChoice(), rMessage);
//				if(winner.equals("tie")) {
//					textArea1.append("Tie!!!!!!\n");
//				}else {
//					message = new Message();
//					textArea1.append(winner +" is the WINNER!!!!!!\n");
//				}
//			}else {
//				// Do nothing
//			}
//		
//			
		}else if(rType.equals(CHAT)) {
			// Output the message to the textArea
			textArea.append("[" + rNickname + "] " + rMessage + "\n");
			
		}else {
			// Do nothing
		}
	}
	
	/**
	 * Compares both players choices and returns the winner
	 * @param p1
	 * @param p2
	 * @return winner
	 */
	public String getResult(String p1 , String p2) {
		String winner = "";
		if(p1.equals(p2)) {
			winner = "tie";
		}else if(p1.equals(ROCK)) {
			if(p2.equals(SCISSORS)) {
				winner = p1;
			}else if(p2.equals(PAPER)) {
				winner = p2;
			}
		}else if(p1.equals(PAPER)) {
			if(p2.equals(ROCK)) {
				winner = p1;
			}else if(p2.equals(SCISSORS)) {
				winner = p2;
			}
		}else if(p1.equals(SCISSORS)) {
			if(p2.equals(PAPER)) {
				winner = p1;
			}else if(p2.equals(ROCK)) {
				winner = p2;
			}
		}else {
			// Do nothing 
		}
		return winner;
	}
}
