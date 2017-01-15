import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class RMIClient {
	private String method;
	private String userName;
	private String password;
	private String anotherUserName;
	private Date startDate;
	private Date endDate;
	private String meetingTitle;
	private int meetingId;
	
	private RemoteInterface service;
	
	private static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	
	public RMIClient(RemoteInterface service) {
		this.service = service;
	}
	
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("Parameter error. Usage: RMIClient <Server name> <port number>");
			System.exit(0);
		} else {
			String serverName = args[0];
			int portNumber = 0;
			try {
				portNumber = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.err.println("Cannot resolve port number.");
				e.printStackTrace();
				System.exit(0);
			}
			String registration = "rmi://" + serverName + ":" + portNumber
				+ "/RMIInterface";
			
			try {
				RemoteInterface service = (RemoteInterface)Naming.lookup(registration);
				RMIClient client = new RMIClient(service);
				client.resolveCommand();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
		}		
	}
	
	public void resolveCommand() throws RemoteException {
		while (true) {
			int option = getOptions();
			switch (option) {
			case 1:
			{
				resolveRegister();
				int returnValue = service.register(userName, password);
				if(returnValue == -1) {
					System.err.println("User already existed.");
				} else if(returnValue == -2) {
					System.err.println("Password is empty.");
				} else {
					System.out.println("Register success.");
				}
				break;
			}
			case 2:
			{
				resolveAdd();
				int returnValue = service.add(userName, password, anotherUserName, 
						meetingTitle, startDate, endDate);
				if(returnValue == -2) {
					System.err.println("User name doesn't exist or wrong password.");
				} else if(returnValue == -1) {
					System.err.println("Meeting date error.");
				} else if(returnValue == 0) {
					System.err.println("Date overlaps.");
				} else {
					System.out.println("Add success!");
				}
				break;
			}
			case 3:
			{
				resolveQuery();
				Vector<Meeting> returnValue = service.query(userName, 
						password, startDate, endDate);
				if(returnValue == null) {
					System.err.println("User name doesn't exist or wrong password, or Start date is after end date.");
				} else if(returnValue.isEmpty()) {
					System.err.println("No match found.");
				} else {
					System.out.println("Query results:");
					for(int i = 0;i < returnValue.size();i++) {
						System.out.println(returnValue.get(i).toString());
					}
				}
				break;
			}
			case 4:
			{
				resolveDelete();
				int returnValue = service.delete(anotherUserName, password, meetingId);
				if(returnValue == -1) {
					System.err.println("User name doesn't exist or wrong password.");
				} else if(returnValue == 0) {
					System.err.println("The meeting doesn't exist.");
				} else {
					System.out.println("Delete success.");
				}
				break;
			}
			case 5:
			{
				resolveClear();
				int returnValue = service.clear(anotherUserName, password);
				if(returnValue == -1) {
					System.err.println("User name doesn't exist or wrong password.");
				} else if(returnValue == 0) {
					System.err.println("The meeting doesn't exist.");
				} else {
					System.out.println("clear success.");
				}
				break;
			}
			default:
				break;
			}
		}
	}	
	
	private int getOptions() {
		System.out.println("Welcome! Please select operattions.\n");
		while (true) {
			System.out.println("\t\t 1\t Register a new member.\n"
					+ "\t\t 2\t Add a new meeting.\n"
					+ "\t\t 3\t Query meetings.\n"
					+ "\t\t 4\t Delete a meeting.\n"
					+ "\t\t 5\t Clear meetings related to you.\n"
					+ "\t\t 6\t Exit.\n");
			System.out.print("Please input your options>");
			System.out.flush();
			int option = 0;
			try {
				option = Integer.parseInt(stdIn.readLine());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.err.println("Cannot resolve your option, please try again.");
				continue;
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error.");
				e.printStackTrace();
				System.exit(0);
			}
			
			if(option > 6 || option < 1) {
				System.err.println("Your option is wrong, please try again.");
				continue;
			} else if(option == 6) {
				System.out.println("Thank you!");
				System.exit(1);
			} else {
				return option;
			}
		}
	}
	
	private void resolveRegister() {
		while (true) {
			System.out.println("You are going to register. Please input command.");
			System.out.println("Foamat:register [username] [password]");
			System.out.print(">");
			System.out.flush();
			String registerCommand = new String();
			try {
				registerCommand = stdIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error.");
				e.printStackTrace();
				System.exit(0);
			}
			String[] splitedRegisterCommand = registerCommand.split(" ");
			if (splitedRegisterCommand.length != 3) {
				System.err.println("Command error, please try again.");
				continue;
			} else {
				method = splitedRegisterCommand[0];
				userName = splitedRegisterCommand[1];
				password = splitedRegisterCommand[2];
				if(!method.equals("register")) {
					System.err.println("Your method is wrong. it should be register. please try again.");
					continue;
				}
			}
			return;
		}
	}
	
	private void resolveAdd() {
		while (true) {
			System.out.println("You are going to add a new meeting. Please input command.");
			System.out.println("Format:add [username] [password] [otherusername] [start date] [end date] [title]");
			System.out.println("PS: Date format: YYYY-MM-DD-HH-mm");
			System.out.print(">");
			System.out.flush();
			String addCommand = new String();
			try {
				addCommand = stdIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error.");
				e.printStackTrace();
				System.exit(0);
			}
			String[] splitedaddCommand = addCommand.split(" ");
			if (splitedaddCommand.length != 7) {
				System.err.println("Command error, please try again.");
				continue;
			} else {
				method = splitedaddCommand[0];				
				if (!method.equals("add")) {
					System.err.println("Your method is wrong. it should be add. please try again.");
					continue;
				}
				userName = splitedaddCommand[1];
				password = splitedaddCommand[2];
				anotherUserName = splitedaddCommand[3];
				String startDateString = splitedaddCommand[4];
				String endDateString = splitedaddCommand[5];
				startDate = resolveDate(startDateString);
				endDate = resolveDate(endDateString);
				if(startDate == null || endDate == null) {
					System.err.println("Date format error.");
					continue;
				}
				
				meetingTitle = splitedaddCommand[6];
			}
			return;
		}
	}
	
	private void resolveQuery() {
		while (true) {
			System.out.println("You are going to query meetings. Please input command.");
			System.out.println("Format:query [username] [password] [start date] [end date]");
			System.out.println("PS: Date format: YYYY-MM-DD-HH-mm");
			System.out.print(">");
			System.out.flush();
			String addCommand = new String();
			try {
				addCommand = stdIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error.");
				e.printStackTrace();
				System.exit(0);
			}
			String[] splitedaddCommand = addCommand.split(" ");
			if (splitedaddCommand.length != 5) {
				System.err.println("Command error, please try again.");
				continue;
			} else {
				method = splitedaddCommand[0];				
				if (!method.equals("query")) {
					System.err.println("Your method is wrong. it should be query. please try again.");
					continue;
				}
				userName = splitedaddCommand[1];
				password = splitedaddCommand[2];
				String startDateString = splitedaddCommand[3];
				String endDateString = splitedaddCommand[4];
				if((startDate = resolveDate(startDateString)) != null &&
						(endDate = resolveDate(endDateString)) != null) {					
				} else {
					System.err.println("Date format error.");
					continue;
				}
			}
			return;
		}
	}
	
	private void resolveDelete() {
		while (true) {
			System.out.println("Attention!You are going to delete a meeting. Please input command.");
			System.out.println("Format: delete [username] [password] [meetingid]");
			System.out.print(">");
			System.out.flush();
			String addCommand = new String();
			try {
				addCommand = stdIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error.");
				e.printStackTrace();
				System.exit(0);
			}
			String[] splitedaddCommand = addCommand.split(" ");
			if (splitedaddCommand.length != 4) {
				System.err.println("Command error, please try again.");
				continue;
			} else {
				method = splitedaddCommand[0];				
				if (!method.equals("delete")) {
					System.err.println("Your method is wrong. it should be delete. please try again.");
					continue;
				}
				userName = splitedaddCommand[1];
				password = splitedaddCommand[2];
				try {
					meetingId = Integer.parseInt(splitedaddCommand[3]);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					System.err.println("Cannot resolve meeting ID, please try again.");
					continue;
					//e.printStackTrace();
				}
			}
			return;
		}
	}
	
	private void resolveClear() {
		while (true) {
			System.out.println("Attention!You are going to clear meetings. Please input command.");
			System.out.println("Format: clear [username] [password]");
			System.out.print(">");
			System.out.flush();
			String addCommand = new String();
			try {
				addCommand = stdIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error.");
				e.printStackTrace();
				System.exit(0);
			}
			String[] splitedaddCommand = addCommand.split(" ");
			if (splitedaddCommand.length != 3) {
				System.err.println("Command error, please try again.");
				continue;
			} else {
				method = splitedaddCommand[0];				
				if (!method.equals("clear")) {
					System.err.println("Your method is wrong. it should be clear. please try again.");
					continue;
				}
				userName = splitedaddCommand[1];
				password = splitedaddCommand[2];
			}
			return;
		}
	}
	
	private Date resolveDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		dateFormat.setLenient(false);
		//ParsePosition position = new ParsePosition(0);
		Date newDate = null;
		try {
			newDate = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Date format error.");
			//e.printStackTrace();
		}
		return newDate;
	}	
}
