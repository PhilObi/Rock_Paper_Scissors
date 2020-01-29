package utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class InputListener implements Runnable {
	// Attributes
	private ArrayList<PropertyChangeListener> observers = new ArrayList<>();
	private Socket socket = null;
	private ObjectInputStream ois;
	private int number;
	
	public InputListener(Socket socket, PropertyChangeListener observer) {
		observers.add(observer);
		this.socket = socket;
	}
	
	public InputListener(int number, Socket socket, PropertyChangeListener observer) {
		observers.add(observer);
		this.number = number;
		this.socket = socket;		
	}
			
	public void run() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				notifyObservers(ois.readObject());	
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the number of the input stream
	 * @return number of the unput listener
	 */
	public int getNumber() {
		return this.number;
	}
	
	private void notifyObservers(Object newValue)
	{
		for( PropertyChangeListener observer : observers )
		{
			observer.propertyChange(new PropertyChangeEvent(this, null, null, newValue));
		}
	}
}
