
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class RMIService extends UnicastRemoteObject implements RemoteInterface {
	private static final long serialVersionUID = 1473768713550041016L;
	private HashMap<String, String> userDB;
	private Vector<Meeting> meetings;
	private int counter;
	
	public RMIService() throws RemoteException {
		userDB = new HashMap<String, String>();
		meetings = new Vector<Meeting>();
		counter = 0;
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return An Integer indicates login state.-1 means wrong user name. 0 means wrong password, 1 means login ssuccess.
	 */
	public int login(String userName,String password) throws RemoteException{
		if(!userDB.containsKey(userName)) {
			//System.err.println("User doesn't exist.");
			return -1;
		} else if(userDB.get(userName).equals(password)) {
			//System.out.println("Login success.");
			return 1;
		} else {
			//System.err.println("wrong password.");
			return 0;
		}
	}
	
	/**
	 * @param userName
	 * @param password
	 * @return An integer indicates register state.
	 * -1 means the user has existed. 
	 * -2 means a empty password. 
	 * 1 means register success.
	 * @throws RemoteException
	 */	
	public int register(String userName,String password) throws RemoteException {
		if(userDB.containsKey(userName)) {
			System.err.println("User already existed.");
			return -1;
		} else if (password.equals("")){
			System.err.println("Password is empty.");
			return -2;
		} else {
			userDB.put(userName, password);
			System.out.println("Register success.");
			return 1;
		}
	}
	
	/**
	 * @return An integer value which indicates the add meeting state.
	 * -2 means wrong password or user name.
	 * -1 means meeting date error.
	 * 0 means the new meeting's date overlaps with a meeting already exist.
	 * 1 means add a meeting success.
	 */
	public int add(String firstUser,String password,String secondUser,
					String title,Date start,Date end) throws RemoteException {
		if(login(firstUser, password) != 1) {
			System.err.println("User name doesn't exist or wrong password.");
			return -2;
		} else if(start.after(end)) {
			System.err.println("Meeting date error.");
			return -1;
		}else {
			String[] users = new String[2];
			for(int i = 0;i < meetings.size();i++) {
				Meeting meeting = meetings.get(i);
				users = meeting.getUsers();
				if(users[0].equals(firstUser) || users[1].equals(firstUser) ||
					users[0].equals(secondUser) || users[1].equals(secondUser)) {
					Date startDate = meeting.getStart();
					Date endDate = meeting.getEnd();
					if((startDate.before(end) && start.before(startDate) || 
							(start.before(endDate) && endDate.before(end)) || 
							startDate.before(start) && end.before(endDate))) {
						System.err.println("Date overlaps.");
						return 0;
					}
				}				
			}
			counter++;
			users[0] = firstUser;
			users[1] = secondUser;				
			meetings.add(new Meeting(counter, firstUser, secondUser, title, start, end));
			System.out.println("Add success!");
			return 1;
		}
	}
	
	public Vector<Meeting> query(String userName,String password,Date start,Date end) throws RemoteException{
		Vector<Meeting> result = new Vector<Meeting>();
		if(login(userName, password) != 1) {
			System.err.println("User name doesn't exist or wrong password.");
			return null;
		} else if(!start.before(end)){
			System.err.println("Start date is after end date.");
			return null;
		} else {
			for(Iterator<Meeting> iterator = meetings.iterator();iterator.hasNext();) {
				Meeting meeting = iterator.next();
				String[] users = meeting.getUsers();
				if(users[0].equals(userName) || users[1].equals(userName)) {
					Date startDate = meeting.getStart();
					Date endDate = meeting.getEnd();
					if(start.before(startDate) ||  endDate.before(end)) {
						result.add(meeting);
					}
				}
			}
			if(result.isEmpty()) {
				System.err.println("No match found.");
			} else {
				System.out.println("Query success!");
			}
			return result;
		}
	}
	
	/**
	 * @return An integer which indicates the state of delete meeting.
	 * -1 means wrong password or user name.
	 * 0 means the meeting doesn't exist.
	 * 1 means delete success.
	 */
	public int delete(String userName,String password,int meetingId) throws RemoteException{
		if(login(userName, password) != 1) {
			System.err.println("User name doesn't exist or wrong password.");
			return -1;
		} else {
			int isDelete = 0;
			for(int i = 0; i < meetings.size();i++) {
				if(meetings.get(i).getMeetingId() == meetingId) {
					meetings.remove(i);
					isDelete = 1;
				}
			}
			return isDelete;			
		}
	}
	
	/**
	 * @return An integer which indicates the state of clear meetings.
	 * -1 means wrong password or user name.
	 * 0 means the meeting doesn't exist.
	 * 1 means clear success.
	 */
	public int clear(String userName,String password) throws RemoteException{
		if(login(userName, password) != 1) {
			System.err.println("User name doesn't exist or wrong password.");
			return -1;
		} else {
			int isRemove = 0;
			for(int i = 0;i < meetings.size();i++) {
				String[] userNames = new String[2];
				userNames = meetings.get(i).getUsers();
				
				if(userNames[0].equals(userName) || userNames[1].equals(userName)) {
					meetings.remove(i);
					i--;
					isRemove = 1;
				}
			}
			return isRemove;
		}
	}	
}
