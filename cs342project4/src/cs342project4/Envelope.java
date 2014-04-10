package cs342project4;

import java.io.Serializable;
import java.util.ArrayList;

public class Envelope implements Serializable{
	private String message;
	private String sender;
	private ArrayList<String> Recipiants = new ArrayList<String>();
	/**
	 * Constructor for envelope.
	 * @param s, sender
	 * @param m, message
	 * @param r, recipiants.
	 */
	public Envelope(String s, String m, ArrayList<String> r)
	{
		sender = s;
		message = m;
		Recipiants = r;
	}
	/**
	 * returns the sender.
	 * @return
	 */
	public String sender()
	{
		return sender;
	}
	/**
	 * Returns the message.
	 * @return
	 */
	public String message()
	{
		return message;
	}
	/**
	 * Retruns the arraylist of recipiants.
	 * @return
	 */
	public ArrayList<String> recipiants()
	{
		return Recipiants;
	}
	@Override
	public String toString(){
		
		return sender +": "+ message + Recipiants;
	}
}
