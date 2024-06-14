package sn.esp.chatroomrest.serveur.controller;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sn.esp.chatroomrest.serveur.model.Message;
import sn.esp.chatroomrest.serveur.model.User;

import java.util.ArrayList;
import java.util.List;

@Path("/chatroom")
public class Serveur {
    private static List<User> users = new ArrayList<>();
    private static List<Message> messages = new ArrayList<>();

    @GET
    @Path("/getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return users;
    }

    @POST
    @Path("/subscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscribe(User user) {
        for (User u : users) {
            if (u.getPseudo().equals(user.getPseudo())) {
                return Response.status(Response.Status.CONFLICT).entity("Username already taken").build();
            }
        }
        users.add(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @POST
    @Path("/unsubscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unsubscribe(User user) {
        users.removeIf(u -> u.getPseudo().equals(user.getPseudo()));
        return Response.status(Response.Status.OK).entity(user.getPseudo()+ " est déconecté").build();
    }

    @GET
    @Path("/getAllMessages")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessages() {
        return messages;
    }

    @POST
    @Path("/postMessage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMessage(Message message) {
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));

        messages.add(message);
        return Response.status(Response.Status.CREATED).entity(message).build();
    }
}
