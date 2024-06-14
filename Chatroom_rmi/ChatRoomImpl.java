import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChatRoomImpl extends UnicastRemoteObject implements ChatRoom {
    private Map<String, ChatUser> users = new HashMap<>();

    public ChatRoomImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void subscribe(ChatUser user, String pseudo) throws RemoteException {
        this.users.put(pseudo, user);
        postMessage("Server", pseudo + " a rejoint la salle");
    }

    @Override
    public synchronized void unsubscribe(String pseudo) throws RemoteException {
        this.users.remove(pseudo);
        postMessage("Server", pseudo + " a quitté la salle.");
    }

    @Override
    public synchronized void postMessage(String pseudo, String message) throws RemoteException {
        String userMessage = pseudo + " : " + message;
        for (ChatUser user : users.values()) {
            user.displayMessage(userMessage);
        }
    }
}
