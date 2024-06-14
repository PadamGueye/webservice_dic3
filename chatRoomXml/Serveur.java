import java.util.*;

import org.apache.xmlrpc.WebServer;
public class Serveur{

    private static Vector<String> user = new Vector<String>();
    String msg_courant = null;

    public static void main (String [] args){
        try {
            System.out.println("Attempting to start XML-RPC Server...");
            WebServer server = new WebServer(80);
            server.addHandler("sample", new Serveur());
            server.start();
            System.out.println("Started successfully.");
            System.out.println("Accepting requests. (Halt program to stop.)");
        }
        catch (Exception exception){ System.err.println("JavaServer: " + exception); }
    }

    public boolean subscribe(String pseudo) {
        boolean ok = false;
        if(!user.contains(pseudo)){
            user.add(pseudo);
            ok = true;
        }
        return ok;
    }

    public String postMessage(String pseudo, String message) {
        String messageEntier = "["+ pseudo + "] : " + message;
        System.out.println(messageEntier);
        msg_courant = messageEntier;
        return messageEntier;
    }

    public String getMessage(){
        return msg_courant;
    }

    public boolean unsubscribe(String pseudo) {
        boolean ok = false;
        if(user.contains(pseudo)){
            user.remove(pseudo);
            ok = true;
            postMessage(pseudo, " deconnected");
        }
        return ok;
    }
}