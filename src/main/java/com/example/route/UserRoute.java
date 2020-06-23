package com.example.route;

import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@RouteBase(path = "/api/reactive/v1")
@ApplicationScoped
public class UserRoute {

    // Don't use private field injection (even thought it still work)
    // As it lead to bigger native executable due to graalvm limitations
    // Package-private (or default) is ok.
    @Inject
    UserService userDb;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void init(@Observes Router router) {
        router.errorHandler(500, (RoutingContext context) -> {
            context.failure().printStackTrace();
            // blame the user
            this.responseBadRequest(context);
        });
    }

    // Almost like normal vertx route
    // Here we have to specify the handle type is blocking
    // because our db service is blocking
    @Route(path = "/users", methods = HttpMethod.GET, type = Route.HandlerType.BLOCKING)
    public void fetchAllUser(RoutingContext context) {
        this.responseOk(context, userDb.findAll());
    }

    // Routing exchange just a wrapper with some additional methods
    // like get param as Optional,...
    @Route(path = "/users/:id", methods = HttpMethod.GET, type = Route.HandlerType.BLOCKING)
    public void fetchUser(RoutingExchange exchange) {
        RoutingContext context = exchange.context();
        UUID id = UUID.fromString(context.pathParam("id"));
        Optional<User> user = userDb.findOne(id);
        user.ifPresentOrElse(
            u -> this.responseOk(context, u),
            () -> this.responseNotFound(context)
        );
    }

    @Route(path = "users", methods = HttpMethod.POST, type = Route.HandlerType.BLOCKING)
    public void createUser(RoutingContext context) throws JsonProcessingException {
        User user = objectMapper.readValue(context.getBodyAsString(), User.class);
        user.setId(null);
        userDb.save(user).ifPresentOrElse(
            saved -> this.responseCreated(context, saved),
            () -> this.responseBadRequest(context)
        );
    }

    @Route(path = "/users/:id", methods = HttpMethod.PUT, type = Route.HandlerType.BLOCKING)
    public void updateUser(RoutingContext context) throws JsonProcessingException {
        UUID id = UUID.fromString(context.pathParam("id"));
        User user = objectMapper.readValue(context.getBodyAsString(), User.class);
        user.setId(id);
        if (!userDb.exist(id)) {
            this.responseNotFound(context);
            return;
        }
        userDb.save(user).ifPresentOrElse(
            updated -> this.responseOk(context, updated),
            () -> this.responseBadRequest(context)
        );
    }

    @Route(path = "/users/:id", methods = HttpMethod.DELETE, type = Route.HandlerType.BLOCKING)
    public void deleteUser(RoutingContext context) {
        UUID id = UUID.fromString(context.pathParam("id"));
        userDb.delete(id);
        this.responseOk(context);
    }

    private void responseOk(RoutingContext context, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            this.responseOk(context, jsonData);
        } catch (JsonProcessingException e) {
            context.fail(e);
        }
    }

    private void responseOk(RoutingContext context, String data) {
        context.response()
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .setStatusCode(200)
            .end(data);
    }

    private void responseOk(RoutingContext context) {
        context.response().setStatusCode(200).end();
    }

    private void responseError(RoutingContext context) {
        context.response().setStatusCode(500);
    }

    private void responseNotFound(RoutingContext context) {
        context.response().setStatusCode(404).end();
    }

    private void responseBadRequest(RoutingContext context) {
        context.response().setStatusCode(400).end();
    }

    private void responseCreated(RoutingContext context, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            context.response().setStatusCode(201).end(jsonData);
        } catch (JsonProcessingException e) {
            context.fail(e);
        }
    }
}