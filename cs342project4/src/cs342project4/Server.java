package cs342project4;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server implements Runnable {
	Socket socket = null;
	ArrayList<String> users = new ArrayList<String>();
	ArrayList<Evenlope> messages = new ArrayList<Evenlope>();
	public Server(Socket s)
	{
		socket = s;
	}
	@Override
	public void run() {
		System.out.println("Connection accepted");
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			while(true){
				socket.setSoTimeout(10000);
				Evenlope m = null;
				m = (Evenlope)in.readObject();
				if(m == null){
					break;
				}
				if(m.sender() != null && m.message().equals("Initial Connection")
						&& m.recipiants().get(0).equals("Server"))
				{
					users.add(m.sender());
				}
				for(Evenlope e : messages){
					for(String s : e.recipiants()){
						if(s.equals(m.sender()))
						{
							out.writeObject(e);
						}
					}
				}
		
			}
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(25565);
			try{
				while(true)
				{
					serverSocket.setSoTimeout(10000);
					try{
						new Thread(new Server(serverSocket.accept())).start();
					}catch(SocketTimeoutException ste){
						System.err.println("Timeout Occured");
					}
				}
			}catch(IOException e){
				System.err.println("Accept failed.");
				System.exit(-1);
			}
		}catch(IOException e){
			System.err.println("Couldn't listen on port.");
			System.exit(-1);
		}finally{
			try{
				serverSocket.close();
			}catch(IOException e){
				System.err.println("Couldn't close port. 25565");
				System.exit(-1);
			}
		}

	}

}
