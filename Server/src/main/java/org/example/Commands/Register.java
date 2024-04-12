package org.example.Commands;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.Database.DatabaseManager;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class Register extends Command {
    DatabaseManager databaseManager;

    public Register(DatabaseManager databaseManager) {
        super("register", "регистрация пользователя");
        this.databaseManager = databaseManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        this.commandLogger.debug("Получен пользователь: " + request.getUser());
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        try {
            databaseManager.addUser(request.getUser());
        } catch (SQLException e) {
            commandLogger.error("Не удалось добавить пользователя в таблицу");
            return new Response(ResponseStatus.FAIL_LOGIN, resourceBundle.getString("failLogin"));
        }
        return new Response(ResponseStatus.OK, resourceBundle.getString("register"));
    }
}
