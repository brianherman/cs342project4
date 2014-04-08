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
	private ServerThreadIterface callback = null;
	private String name = null;
	public ServerThread(Socket s, ServerThreadIterface stc)
	{
		socket = s;
		callback = stc;
	}
	@Override
	public void run() {
		callback.log("Connection accepted");
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
			e.printStackTrace();
		}
	}
	public void close(){
		try{
			out.close();
			in.close();
			socket.close();
			callback.remove(name);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
