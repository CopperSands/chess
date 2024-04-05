package server.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import server.ErrorMessageRes;
import service.LoginService;
import service.LogoutService;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class AuthHandler {

    private AuthDAO authDAO;
    private UserDAO userDAO;
    private RegisterService registerService;
    private LogoutService logoutService;
    private LoginService loginService;

    public AuthHandler(){
        authDAO = new PersistentAuthDAO();
        userDAO = new PersistentUserDAO();
        registerService = new RegisterService(userDAO,authDAO);
        logoutService = new LogoutService(authDAO);
        loginService = new LoginService(userDAO,authDAO);
    }

    public Object registrationHandler(Request req, Response res){
        Gson gson = new Gson();
        UserData user = gson.fromJson(req.body(),UserData.class);
        try{
            AuthData authToken = registerService.register(user.username(),user.password(),user.email());
            return gson.toJson(authToken);
        }catch(DataAccessException e){
            if (e.getMessage() == "Error username is taken"){
                res.status(403);
            }else if (e.getMessage() == "Error bad request"){
                res.status(400);
            }
            else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

    public Object loginHandler(Request req, Response res){
        Gson gson = new Gson();
        UserData user = gson.fromJson(req.body(),UserData.class);
        try{
            AuthData authToken = loginService.login(user.username(),user.password());
            return gson.toJson(authToken);
        }catch (DataAccessException e){
            if (e.getMessage() == "Error Incorrect password" || e.getMessage() == "Error user does not exit"){
                res.status(401);
            }else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

    public Object logoutHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try{
            logoutService.logout(authToken);
            return "";
        }catch (DataAccessException e){
            if (e.getMessage() == "Error unauthorized" || false){
                res.status(401);
            }else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

}
