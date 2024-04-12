package org.example.Commands;

import org.example.CollectionModel.HumanBeing;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Класс команды для вывода всей коллекции
 */
public class Show extends Command {
    private CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        Collection<HumanBeing> collection = collectionManager.getCollection();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        if (collectionManager.getCollection() == null) {
            return new Response(ResponseStatus.ERROR, resourceBundle.getString("noCollectionInSession"));
        }
        return new Response(ResponseStatus.OK, "Коллекция", collection);
    }
}
