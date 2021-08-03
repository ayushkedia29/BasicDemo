package com.unbxd.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.unbxd.dao.CrudCollectionsImpl;
import com.unbxd.model.Student;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;
import ro.pippo.demo.common.Contact;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BasicApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(BasicApplication.class);
    private dao.CrudCollections crud;

    @Override
    protected void onInit() {


        // send 'Hello World' as response
        crud = new CrudCollectionsImpl();

        try {
            final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            MongoDatabase database = mongoClient.getDatabase("db1");
            MongoCollection<Document> Collection = database.getCollection("Student_Collection");

            /*
            GET("/student.*", routeContext -> {
                if (routeContext.getSession("username") == null) {
                    routeContext.setSession("originalDestination", routeContext.getRequest().getApplicationUriWithQuery());
                    routeContext.redirect("/login");
                } else {
                    routeContext.next();
                }
            });


            POST("/login", new RouteHandler() {

                @Override
                public void handle(RouteContext routeContext) {
                    String username = routeContext.getParameter("username").toString();
                    String password = routeContext.getParameter("password").toString();
                    if (authenticate(username, password)) {
                        String originalDestination = routeContext.removeSession("originalDestination");
                        routeContext.resetSession();

                        routeContext.setSession("username", username);
                        routeContext.redirect(originalDestination != null ? originalDestination : "/contacts");
                    } else {
                        routeContext.flashError("Authentication failed");
                        routeContext.redirect("/login");
                    }
                }

                private boolean authenticate(String username, String password) {
                    return !username.isEmpty() && !password.isEmpty();
                }

            });
            */


            PUT("/student", new RouteHandler() {
                @Override
                public void handle(RouteContext routeContext) {
                    Student student = new Student();
                    student.setId(routeContext.getParameter("id").toInt());
                    student.setFirstname(routeContext.getParameter("firstname").toString());
                    student.setLastname(routeContext.getParameter("lastname").toString());
                    student.setAge(routeContext.getParameter("age").toInt());
                    crud.insetNew(Collection, student);
                }
            });

            GET("/student/read", new RouteHandler() {
                @Override
                public void handle(RouteContext routeContext) {
                    int id = routeContext.getParameter("id").toInt(0);
                    Student student = new Student();
                    student = crud.readCollection(Collection,id);
                    routeContext.json().send(student);
                }
            });

            GET("/student/{id}", new RouteHandler() {

                @Override
                public void handle(RouteContext routeContext) {
                    int id = routeContext.getParameter("id").toInt(0);
                    String action = routeContext.getParameter("action").toString("new");
                    if ("delete".equals(action)) {
                        crud.deleteCollection(Collection, id);

                    } else if ("update".equals(action)){
                        crud.updateCollection(Collection, id);
                    }


                    Student student = (id > 0) ? crud.readCollection(Collection, id) : new Student();
                    routeContext.setLocal("student", student);
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("action", "save");
                    if (id > 0) {
                        parameters.put("id", id);
                    }
                    routeContext.setLocal("saveUrl", getRouter().uriFor("/student", parameters));
                    routeContext.render("student");
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
