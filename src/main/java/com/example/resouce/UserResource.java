package com.example.resouce;

import com.example.model.User;
import com.example.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    // Don't use private field injection (even thought it still work)
    // As it lead to bigger native executable due to graalvm limitations
    // Package-private (or default) is ok.
    @Inject
    UserService userDb;

    // Can return object like spring.
    // It follow the Jax-RS annotations
    // Most of annotations are spring-like, but not all. find in doc folder for conversions
    @GET
    public List<User> fetchAllUser(@QueryParam("name") String name) {
        if (name != null) {
            return userDb.findAll()
                .stream()
                .filter(user -> user.getName().contains(name))
                .collect(Collectors.toList());
        }
        return userDb.findAll();
    }

    @GET
    @Path("/{id}")
    public Response fetchUser(@PathParam("id") UUID id) {
        Optional<User> user = userDb.findOne(id);
        return user
            .map(u -> Response.ok(u).build())
            .orElse(Response.status(404).build());
    }

    // Body automatically bind to first un-annotated parameter
    // It can be of any type: String, Object,... or models
    @POST
    public Response createUser(User user) {
        user.setId(null);
        return userDb.save(user)
            .map(saved -> Response.status(201).entity(saved).build())
            .orElse(Response.status(400).build());
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") UUID id, User user) {
        user.setId(id);
        if (!userDb.exist(id)) {
            return Response.status(404).build();
        }
        return userDb.save(user)
            .map(updated -> Response.ok(updated).build())
            .orElse(Response.status(400).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") UUID id) {
        userDb.delete(id);
        return Response.status(200).build();
    }
}