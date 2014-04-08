package cs342project4;

import java.util.ArrayList;

public interface ServerThreadCallback {
	public void send(Evenlope m);
	public ArrayList<String> getUsers();

}
