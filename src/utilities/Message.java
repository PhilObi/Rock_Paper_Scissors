package utilities;

import java.io.Serializable;

public class Message implements Serializable {
	// Attributes
	private Player player;
	private String message;
	
	/**
	 * Default Contructor
	 */
	public Message() {
		
	}
	
	/**
	 * Passes in a player object and the message
	 * @param player
	 * @param message
	 */
	public Message(Player player, String message) {
		super();
		this.player = player;
		this.message = message;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message [player=" + player + ", message=" + message + "]";
	}
	
	
}
