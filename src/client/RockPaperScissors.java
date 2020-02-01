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

public class RockPaperScissors extends JFrame implements PropertyChangeListener{

	private JPanel contentPane;
	private JTextField chatMessage;
	private Player player;
	private Socket socket;
	private ObjectOutputStream oos;
	private InputListener lis;
	private JTextArea textArea;
	private Message message;

	/**
	 * Create the frame.
	 */
	public RockPaperScissors(Player player, Socket socket) {
		this.player = player;
		this.socket = socket;
		
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
		scrollPane.setBounds(414, 29, 315, 341);
		contentPane.add(scrollPane);
		
		/**
		 * Text area for chat and game status
		 */
		textArea = new JTextArea();
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
		label.setBounds(404, 11, 335, 399);
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
					message = new Message(player, msg);
					
					// Send message 
					try {
						oos.writeObject(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					// Output the message to the textArea
					textArea.append(player.getNickName() + ": " + message.getMessage() + "\n");
					
					// Clear chat message
					chatMessage.setText("");
				}
			}
		});
		sendMessage.setBounds(650, 375, 79, 25);
		sendMessage.setBackground(Color.LIGHT_GRAY);
		contentPane.add(sendMessage);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// Read in the new message and display
		Message newMessage = (Message)event.getNewValue();
		// Output the message to the textArea
		textArea.append(newMessage.getPlayer().getNickName() + ": " + newMessage.getMessage() + "\n");

	}
	
}
