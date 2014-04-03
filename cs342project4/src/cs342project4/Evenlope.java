package cs342project4;

import java.io.Serializable;
import java.util.ArrayList;

public class Evenlope implements Serializable{
	private String message;
	private String sender;
	private ArrayList<String> Recipiants = new ArrayList<String>();
	
	public Evenlope(String s, String m, ArrayList<String> r)
	{
		sender = s;
		message = m;
		Recipiants = r;
	}
	public String sender()
	{
		return sender;
	}
	public String message()
	{
		return message;
	}
	public ArrayList<String> recipiants()
	{
		return Recipiants;
	}
	@Override
	public String toString(){
		return sender +": "+ message;
	}
}
