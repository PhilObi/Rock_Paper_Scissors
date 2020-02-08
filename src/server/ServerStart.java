package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

import server.ClientHandler;

public class ServerStart implements Runnable{
	
		private ServerSocket server = null;
		private Socket socket = null;
		private ArrayList<Socket> sockets = new ArrayList<>();

	@Override
	public void run() {
		try
		{
			server = new ServerSocket(3333);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		/**
		 * Pauses thread for few seconds for dramatic effect
		 */
		try {
			Server.textArea.append("[" +LocalTime.now() + "] Starting Server... \n");
			Server.textArea.append("[" +LocalTime.now() + "] Server is up and running! \n");
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		while(true)
		{
			try
			{
				Server.textArea.append("[" +LocalTime.now() + "] Awaiting Client Connection. \n");
				socket = server.accept();
				sockets.add(socket);
				Server.textArea.append("[" +LocalTime.now() + "] Accepted a Client Connection. \n");
				
				if(sockets.size() == 2) {
					// New thread to handle clients
					new Thread(new ClientHandler(sockets.get(0),
							sockets.get(1))).start();
					sockets.clear();
					Server.textArea.append("[" +LocalTime.now() + "] New game session created. \n");
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
