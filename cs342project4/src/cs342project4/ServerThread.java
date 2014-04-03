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
	public ServerThread(Socket s)
	{
		socket = s;
	}
	@Override
	public void run() {
		System.out.println("Connection accepted");
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			while(true){
				socket.setSoTimeout(10000);
				Evenlope m = null;
				m = (Evenlope)in.readObject();
//				for(ServerThread st : serverThreads)
//					st.send(m);
				if(m == null){
					break;
				}
				System.out.println(m);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			out.close();
			in.close();
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
