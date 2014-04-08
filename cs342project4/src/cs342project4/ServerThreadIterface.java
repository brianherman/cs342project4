package cs342project4;

import java.util.ArrayList;

public interface ServerThreadIterface {
	public void send(Evenlope m);
	public ArrayList<String> getUsers();
	public void remove(String user);
	public void log(String log);
}
