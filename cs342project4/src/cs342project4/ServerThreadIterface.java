package cs342project4;

import java.util.ArrayList;
/**
 * Allows for a callback so each server thread can call the main server class.
 * @author brianherman
 *
 */
public interface ServerThreadIterface {
	public void send(Evenlope m);
	public ArrayList<String> getUsers();
	public void remove(String user);
	public void log(String log);
}
