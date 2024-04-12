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
 * Класс команды для удаления элемента коллекции по его id
 */
public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id", "id","удалить элемент из коллекции по его id");
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
            if (DatabaseHandler.getDatabaseManager().deleteHumanBeing(id, request.getUser())) {
                collectionManager.removeElement(collectionManager.getById(id));
                return new Response(ResponseStatus.OK, resourceBundle.getString("objRemoved"));
            } else {
                return new Response(ResponseStatus.ERROR, resourceBundle.getString("objNotRemoved"));
            }
        } catch (NoSuchId e) {
            return new Response(ResponseStatus.ERROR, resourceBundle.getString("noSuchObject"));
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.WRONG_ARGS, resourceBundle.getString("idLong"));
        }
    }
}
