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
 * Класс команды для вывода суммы скоростей всех героев коллекции
 */
public class SumOfImpactSpeed extends Command {
    private final CollectionManager collectionManager;

    public SumOfImpactSpeed(CollectionManager collectionManager) {
        super("sum_of_impact_speed", "вывести сумму значений поля impactSpeed для всех элементов коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        if (collectionManager.getCollection().isEmpty()) {
            return new Response(ResponseStatus.ERROR, resourceBundle.getString("emptyCollection"));
        }
        return new Response(ResponseStatus.OK, resourceBundle.getString("sumOfImpactSpeed") + ": " +
                collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .mapToInt(HumanBeing::getImpactSpeed).sum());
    }
}