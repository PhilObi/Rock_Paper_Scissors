package utilities;

import java.io.Serializable;

/**
 * Creates a player which holds a players information
 * @author Phillip Obiora
 *
 */
public class Player implements Serializable {
	// Attributes
	private String nickName;
	private int score;
	
	//Constructor
	public Player() {

	}
	
	/**
	 * Creates a player class and initialzes
	 * the players nickname and score.
	 * @param nickName
	 * @param score
	 */
	public Player(String nickName) {
		this.nickName = nickName;
	}

	// Getters and Setters
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score + 1;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [nickName=" + nickName + ", score=" + score + "]";
	}
	
}
