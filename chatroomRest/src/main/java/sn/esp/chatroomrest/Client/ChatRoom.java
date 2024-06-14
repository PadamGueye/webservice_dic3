package sn.esp.chatroomrest.Client;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private String baseUrl; // URL de base du serveur

    public ChatRoom(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public synchronized   void subscribe(String pseudo)  {
        JSONObject requestData = new JSONObject();
        requestData.put("pseudo", pseudo);

        sendRequest(baseUrl + "/subscribe", "POST", requestData.toString());
        postMessage("Server", pseudo + " a rejoint la salle");
    }


    public synchronized void unsubscribe(String pseudo)  {
        JSONObject requestData = new JSONObject();
        requestData.put("pseudo", pseudo);

        sendRequest(baseUrl + "/unsubscribe", "POST", requestData.toString());
        postMessage("Server", pseudo + " a quitte la salle .");
    }

    public void postMessage(String pseudo, String message) {
        JSONObject requestData = new JSONObject();
        requestData.put("pseudo", pseudo);
        requestData.put("message", pseudo + " : " + message);

        String msg = sendRequest(baseUrl + "/postMessage", "POST", requestData.toString());
        List<ChatUser> users = getAllUsers();
        for (ChatUser user : users) {
            user.displayMessage(msg);
        }

    }

    public List<ChatUser> getAllUsers() {
        List<ChatUser> userList = new ArrayList<>();

        try {
            String endpoint = baseUrl + "/getAllUsers";
            String response = sendRequest(endpoint, "GET", null);
            JSONArray usersArray = new JSONArray(response);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userJson = usersArray.getJSONObject(i);
                String username = userJson.getString("pseudo"); // Remplacer avec le champ correct du JSON
                ChatUser user = new ChatUserImpl(username); // Créer une instance de ChatUser avec le pseudo
                userList.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    private String sendRequest(String urlString, String method, String body) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");

            if (body != null) {
                conn.setDoOutput(true);
                conn.getOutputStream().write(body.getBytes("utf-8"));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                System.err.println("Error during HTTP request: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) {
        String baseUrl = "http://localhost:8080/chatroomRest_war_exploded/api/chatroom";
        String pseudo = JOptionPane.showInputDialog("Enter your pseudo:");
        new ChatUserImpl(pseudo, new ChatRoom(baseUrl));
    }
}
