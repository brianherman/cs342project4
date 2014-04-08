package cs342project4;

import java.net.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
public class Client extends JFrame{
	private JList Users;
	private JTextArea chat;
	private JTextField message;
	private Socket echoSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private DefaultListModel usersModel;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem connect;
	private JMenuItem quit;
	private JMenuItem leave;

	private static Client c = new Client();
	
	private String name;
	public Client(){
		setLayout(new BorderLayout());
		usersModel = new DefaultListModel();
		Users = new JList(usersModel);
		Users.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		Users.setLayoutOrientation(JList.VERTICAL);
		Users.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(Users);
		listScroller.setPreferredSize(new Dimension(80, 80));
		
		chat = new JTextArea();
		chat.setEditable(false);
		message = new JTextField();
		ClientActionListener CAL = new ClientActionListener();
		message.addActionListener(CAL);
		
		menuBar = new JMenuBar();
		file = new JMenu("File");
		connect = new JMenuItem("Connect");
		leave = new JMenuItem("Leave");
		quit = new JMenuItem("Quit");
		leave.addActionListener(CAL);
		connect.addActionListener(CAL);
		quit.addActionListener(CAL);
		
		file.add(connect);
		file.add(leave);
		file.add(quit);

		menuBar.add(file);
		
		setJMenuBar(menuBar);
		
		add(chat, BorderLayout.CENTER);
		add(listScroller,BorderLayout.EAST);
		add(message, BorderLayout.SOUTH);

		setSize(640,480);

		setVisible(true);
	}
	private class ClientActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(connect == e.getSource())
			{
				c.connect();
			}
			if(leave == e.getSource())
			{
				c.leave();
			}
			if(quit == e.getSource()){
				c.leave();
				c.close();
				System.exit(0);
			}
			if(message == e.getSource())
			{
				Object selectedUsers[] = Users.getSelectedValues();
				ArrayList<String> recipiants = new ArrayList<String>();

				for(int i=0; i<selectedUsers.length; i++)
				{	
					recipiants.add((String)selectedUsers[i]);
					System.out.println("adding "+ selectedUsers[i]);
				}
				if(selectedUsers.length == 0)
				{
					for(int i=0; i<usersModel.size(); i++)
					{	
						recipiants.add((String)usersModel.getElementAt(i));
					}
				}
				Evenlope ev = new Evenlope(name,message.getText(),recipiants);
				try {
					out.writeObject(ev);
					out.flush();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		}

	}
	public void connect(){
		String ipAddress = (String)JOptionPane.showInputDialog(
				null,
				"Enter the server's ip address:\n",
						"Customized Dialog",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						"127.0.0.1");
		name = (String)JOptionPane.showInputDialog(
				null,
				"Enter the username you wish to use:\n",
						"Customized Dialog",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						"brian");
		try{
			echoSocket = new Socket(ipAddress,25565);
			out = new ObjectOutputStream(echoSocket.getOutputStream());
			in  = new ObjectInputStream(echoSocket.getInputStream());
			ArrayList<String> recipiants = new ArrayList<String>();
			recipiants.add("Server");
			Evenlope e = new Evenlope(name, "Initial Connection.", recipiants);
			out.writeObject(e);
			out.flush();
			e = (Evenlope)in.readObject();
			for(String s : e.recipiants())
				usersModel.addElement(s);

		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	public void leave(){
		try {
			ArrayList<String> recipiants = new ArrayList<String>();
			recipiants.add(name);
			Evenlope end = new Evenlope(name,"Leave.", recipiants);
			if(out != null)
			{
				out.writeObject(end);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void listen(){
		new Thread(new ClientThread()).start();
	}
	public void close(){
		try{
			if(out != null || in != null)
			{
				out.close();
				in.close();
			}
		}catch(IOException e){

		}
	}
	public static void main(String[] args) {
		c.connect();
		c.listen();	
	}
	private class ClientThread implements Runnable {
		@Override
		public void run() {
			Evenlope e = null;
			try {

				while((e=(Evenlope)in.readObject()) != null)
				{
					if(e.sender().equals("Server") && e.message().equals("Join."))
					{
						usersModel.removeAllElements();
						for(String s : e.recipiants())
							usersModel.addElement(s);
					}
					if(e.message().equals("Leave."))
					{
						System.out.println(e.sender() + "left.");
						usersModel.removeElement(e.sender());
					}
					chat.setText(chat.getText() + e.sender() + ": "+ e.message() +"\n");
					Thread.sleep(1000);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}		
		}

	}

}
