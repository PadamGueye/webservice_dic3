import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatUser extends Remote {
    String getPseudo() throws RemoteException;
    void displayMessage(String message) throws RemoteException;
}
