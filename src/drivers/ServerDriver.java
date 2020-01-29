package drivers;

import java.awt.EventQueue;

import server.Server;

public class ServerDriver {

	public static void main(String[] args) {
		/**
		 * Launch the Application
		 */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
