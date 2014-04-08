package cs342project4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread implements Runnable {
	private Socket socket = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ServerThreadCallback callback = null;
	private String name = null;
	public ServerThread(Socket s, ServerThreadCallback stc)
	{
		socket = s;
		callback = stc;
	}
	@Override
	public void run() {
		System.out.println("Connection accepted");
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			while(true){
				Evenlope m = null;
				m = (Evenlope)in.readObject();
				if(m == null){
					break;
				}
				if(m.sender() != null && m.message().equals("Initial Connection."))
				{	
					name = m.sender();
					callback.send(new Evenlope("Server","Join.",callback.getUsers()));
				}
				else if(m.sender() != null && m.message().equals("Leave."))
				{
					callback.send(new Evenlope(name,"Leave.",callback.getUsers()));
					close();
					return;
				}	
				callback.send(m);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	public String name(){
		return name;
	}
	public void send(Evenlope m) 
	{
		try {
			out.writeObject(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		try{
			callback.remove(name);
			out.close();
			in.close();
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
