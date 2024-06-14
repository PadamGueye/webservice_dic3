package sn.esp.chatroomrest.serveur.model;

public class User {

    private  String pseudo;

    public User(){};
    public User(String pseudo){
        this.pseudo = pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }
}
