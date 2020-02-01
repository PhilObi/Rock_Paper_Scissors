package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utilities.InputListener;

public class ClientHandler implements Runnable, PropertyChangeListener {
	// attribute
	private Socket socket1 = null;
	private Socket socket2 = null;
	private ObjectOutputStream oos1;
	private ObjectOutputStream oos2;
	private InputListener lis1;
	private InputListener lis2;
	
	/**
	 * Passes in both clients sockets from the server after
	 * two client have connected.
	 * @param socket1
	 * @param socket2
	 */
	public ClientHandler(Socket socket1, Socket socket2) {
		this.socket1 = socket1;
		this.socket2 = socket2;
		
		// Use sockets to create objectoutputstreams
		try {
			oos1 = new ObjectOutputStream(this.socket1.getOutputStream());
			oos2 = new ObjectOutputStream(this.socket2.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create listeners to check for inputs
		lis1 = new InputListener(1, socket1, this);
		lis2 = new InputListener(2, socket2, this);
	}
	
	
	@Override
	public void run() {
		// Start listeners
		new Thread(lis1).start();
		new Thread(lis2).start();
	}
	
	public void propertyChange(PropertyChangeEvent event){
		InputListener mSource = (InputListener)event.getSource();
		Object newValue = event.getNewValue();
		
		if(mSource.getNumber() == 1) {
			try {
				oos2.writeObject(newValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				oos1.writeObject(newValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
