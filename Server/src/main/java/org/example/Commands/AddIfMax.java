package org.example.Commands;

import org.example.CollectionModel.HumanBeing;
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
 * Класс команды создания нового элемента и добавления его в коллекцию, если тот больше остальных
 */
public class AddIfMax extends Command implements Editor {
    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        super("add_if_max", "{element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        if (Objects.isNull(request.getHumanBeing())) {
            return new Response(ResponseStatus.ASKING_OBJECT, resourceBundle.getString("objNeed"));
        }
        if (request.getHumanBeing().compareTo(collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .max(HumanBeing::compareTo)
                .orElse(null)) >= 1) {
            Long newId = DatabaseHandler.getDatabaseManager().addHumanBeing(request.getHumanBeing(), request.getUser());
            if (newId == -1) return new Response(ResponseStatus.ERROR, resourceBundle.getString("objNotAdded"));
            request.getHumanBeing().setId(newId);
            request.getHumanBeing().setUserLogin(request.getUser().getLogin());
            collectionManager.addElement(request.getHumanBeing());
            return new Response(ResponseStatus.OK, resourceBundle.getString("objAdded"));
        }
        return new Response(ResponseStatus.ERROR, resourceBundle.getString("lessObject"));
    }
}
