import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Serveur {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(2000);

            ChatRoom room = new ChatRoomImpl();
            Naming.bind("rmi://localhost:2000/room", room);
            System.out.println("Server is ready.");
        } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
