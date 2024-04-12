package org.example.Commands;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.ResourceBundle;

/**
 * Класс команды для вывода текущей информации о коллекции
 */
public class Info extends Command {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        String lastInitTime = (collectionManager.getLastInitTime() == null)
                ? resourceBundle.getString("noCollectionInSession")
                : collectionManager.getLastInitTime();
        String lastSaveTime = (collectionManager.getLastSaveTime() == null)
                ? "-"
                : collectionManager.getLastSaveTime();
        String resultInfo = "*** " + resourceBundle.getString("collectionInfo") + " ***" + "\n" +
                resourceBundle.getString("type") + ": " + collectionManager.getCollectionType() + "\n" +
                resourceBundle.getString("countObjects") + ": " + collectionManager.getSize() + "\n" +
                resourceBundle.getString("lastInitTime") + ": " + lastInitTime + "\n" +
                resourceBundle.getString("lastSaveTime") + ": " + lastSaveTime + "\n";
        return new Response(ResponseStatus.OK, resultInfo);
    }
}
