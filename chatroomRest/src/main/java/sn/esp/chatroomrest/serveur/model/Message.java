package sn.esp.chatroomrest.serveur.model;

public class Message {
    private String content;
    private String timestamp;

    // Constructors, getters, and setters
    public Message() {}

    public Message( String content, String timestamp) {

        this.content = content;
        this.timestamp = timestamp;
    }



    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
