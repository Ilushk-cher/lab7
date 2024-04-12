package org.example.Commands;

import org.example.CollectionModel.HumanBeing;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Класс команды для вывода уникальных значений настроения героев
 */
public class PrintUniqueMood extends Command {
    private final CollectionManager collectionManager;

    public PrintUniqueMood(CollectionManager collectionManager) {
        super("print_unique_mood", "вывести уникальные значения поля mood всех элементов коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        if (collectionManager.getSize() == 0) {
            return new Response(ResponseStatus.ERROR, resourceBundle.getString("emptyCollection"));
        }
        StringBuilder result = new StringBuilder();
        collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .map(HumanBeing::getMood)
                .distinct()
                .forEach(x -> result.append(String.valueOf(x)).append("\n"));
        return new Response(ResponseStatus.OK, result.toString());
    }
}
