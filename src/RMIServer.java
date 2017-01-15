import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class RMIServer {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("Parameter error. Usage: RMIServer <hostName> <port number>");
			System.exit(0);
		} else {
			
			String hostName = args[0];
			int portNumber = 0;
			try {
				portNumber = Integer.parseInt(args[1]);
			} catch (NumberFormatException e1) {
				System.err.println("Cannot resolve port number.");
				e1.printStackTrace();
				System.exit(0);
			}
			try {
				RMIService service = new RMIService();
				LocateRegistry.createRegistry(portNumber);
				try {
					Naming.bind("rmi://"+hostName+":"+portNumber+"/RMIInterface",service);
				} catch (AlreadyBoundException e) {
					e.printStackTrace();
				}
		//		String registration = "rmi://" + hostName + ":" + portNumber + "/RMIInterface";
		//		Naming.rebind(registration, service);
				System.out.println("RMI Server is running.");
			} catch (RemoteException e) {
				e.printStackTrace();
				System.exit(0);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}
