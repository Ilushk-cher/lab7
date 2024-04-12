package org.example.Commands;

import org.example.CommandSpace.DatabaseHandler;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Класс команды для обновления элемента коллекции по его id
 */
public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update", "id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (request.getArgs().isBlank()) throw new InvalidArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        try {
            long id = Long.parseLong(request.getArgs().trim());
            if (!collectionManager.checkExistById(id)) throw new NoSuchId();
            if (!Objects.equals(collectionManager.getById(id).getUserLogin(), request.getUser().getLogin())) {
                return new Response(ResponseStatus.ERROR, resourceBundle.getString("checkProperty"));
            }
            if (Objects.isNull(request.getHumanBeing())) {
                return new Response(ResponseStatus.ASKING_OBJECT, resourceBundle.getString("objNeed"));
            }
            if (DatabaseHandler.getDatabaseManager().updateHumanBeing(id, request.getHumanBeing(), request.getUser())) {
                commandLogger.debug("Обновить в бд получилось");
                collectionManager.editById(id, request.getHumanBeing());
                commandLogger.debug("Обновить в коллекции получилось");
                return new Response(ResponseStatus.OK, resourceBundle.getString("objUpdated"));
            } else {
                return new Response(ResponseStatus.ERROR, resourceBundle.getString("objNotUpdated"));
            }
        } catch (NoSuchId e) {
            return new Response(ResponseStatus.ERROR, resourceBundle.getString("noSuchObject"));
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.WRONG_ARGS, resourceBundle.getString("idLong"));
        }
    }
}