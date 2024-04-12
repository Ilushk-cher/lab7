package org.example.Commands;

import org.example.CommandSpace.DatabaseHandler;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.EmptyStackException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Класс команды для удаления последнего элемента коллекции
 */
public class RemoveLast extends Command {
    private final CollectionManager collectionManager;

    public RemoveLast(CollectionManager collectionManager) {
        super("remove_last", "удалить последний элемент из коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        try {
            Long id = collectionManager.getLast().getId();
            if (!Objects.equals(collectionManager.getById(id).getUserLogin(), request.getUser().getLogin())) {
                return new Response(ResponseStatus.ERROR, resourceBundle.getString("checkProperty"));
            }
            if (DatabaseHandler.getDatabaseManager().deleteHumanBeing(id, request.getUser())) {
                collectionManager.removeElement(collectionManager.getById(id));
                return new Response(ResponseStatus.OK, resourceBundle.getString("objRemoved"));
            } else {
                return new Response(ResponseStatus.ERROR, resourceBundle.getString("objNotRemoved"));
            }
        } catch (EmptyStackException e) {
            return new Response(ResponseStatus.ERROR, resourceBundle.getString("emptyCollection"));
        }
    }
}
