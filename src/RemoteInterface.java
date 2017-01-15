
import java.rmi.*;
import java.util.Date;
import java.util.Vector;

public interface RemoteInterface extends Remote {
	public int login(String userName,String password)throws RemoteException;
	public int register(String userName,String password) throws RemoteException;
	public int add(String firstUser,String password,String secondUser,
			String title,Date start,Date end) throws RemoteException;
	public Vector<Meeting> query(String userName,String password,Date start,Date end)throws RemoteException;
	public int delete(String userName,String password,int meetingId)throws RemoteException;
	public int clear(String userName,String password)throws RemoteException;
}
