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
	private String choice;

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
	
	/**
	 * Initializes player nick name score and choice;
	 * @param nickName
	 * @param score
	 * @param choice
	 */
	public Player(String nickName, int score, String choice) {
		super();
		this.nickName = nickName;
		this.score = score;
		this.choice = choice;
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
		this.score += 1;
	}

	/**
	 * @return the choice
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * @param choice the choice to set
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [nickName=" + nickName + ", score=" + score + ", Choice=" + choice + "]";
	}
	
	
}
