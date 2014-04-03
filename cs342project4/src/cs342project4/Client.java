package cs342project4;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
public class Client {

	public static void main(String[] args) {
        Socket echoSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try{
        	echoSocket = new Socket("127.0.0.1",25565);
        	out = new ObjectOutputStream(echoSocket.getOutputStream());
        	in  = new ObjectInputStream(echoSocket.getInputStream());
        	ArrayList<String> recipiants = new ArrayList<String>();
        	recipiants.add("Server");
        	Evenlope e = new Evenlope("brian","Initial Connection.", recipiants);
        	out.writeObject(e);
        	ArrayList<String> recipiants2 = new ArrayList<String>();
        	recipiants2.add("brian");
        	Evenlope e2 = new Evenlope("brian","hello",recipiants2);
        	out.writeObject(e2);
        	Evenlope message = (Evenlope)in.readObject();
        	System.out.println(message);
        }catch(IOException e){
			e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
