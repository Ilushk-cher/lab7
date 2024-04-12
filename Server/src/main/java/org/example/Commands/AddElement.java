package org.example.Commands;

import org.example.CommandSpace.DatabaseHandler;
import org.example.Commands.Interfaces.Editor;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Класс команды создания нового элемента и добавления его в коллекцию
 */
public class AddElement extends Command implements Editor {
    private final CollectionManager collectionManager;

    public AddElement(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        if (Objects.isNull(request.getHumanBeing())) {
            return new Response(ResponseStatus.ASKING_OBJECT, resourceBundle.getString("objNeed"));
        } else {
            Long newId = DatabaseHandler.getDatabaseManager().addHumanBeing(request.getHumanBeing(), request.getUser());
            if (newId == -1) return new Response(ResponseStatus.ERROR, resourceBundle.getString("objNotAdded"));
            request.getHumanBeing().setId(newId);
            request.getHumanBeing().setUserLogin(request.getUser().getLogin());
            collectionManager.addElement(request.getHumanBeing());
            return new Response(ResponseStatus.OK, resourceBundle.getString("objAdded"));
        }
    }
}
