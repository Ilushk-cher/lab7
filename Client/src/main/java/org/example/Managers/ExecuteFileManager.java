package org.example.Managers;

import org.example.Client;
import org.example.CollectionModel.HumanBeing;
import org.example.CommandSpace.*;
import org.example.CommandSpace.Console;
import org.example.CommandSpace.Forms.HumanBeingForm;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Exceptions.ExitProgram;
import org.example.Exceptions.FileModeException;
import org.example.Gui.Actions.ExecuteScriptAction;
import org.example.Gui.GuiManager;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Objects;

import static org.example.Gui.GuiManager.resourceBundle;

public class ExecuteFileManager implements Inputable {
    private static final ArrayDeque<String> paths = new ArrayDeque<>();
    private static final ArrayDeque<BufferedReader> fileReader = new ArrayDeque<>();

    private User user;
    private final Client client;
    private final Printable console;

    public ExecuteFileManager() {
        this.user = null;
        this.client = null;
        this.console = new BlankConsole();
    }

    public ExecuteFileManager(User user, Client client, Console console) {
        this.user = user;
        this.client = client;
        this.console = console;
    }

    public void executeFile(String args) throws ExitProgram {
        if (args == null || args.isEmpty()) {
            console.printError(resourceBundle.getString("missPath"));
            return;
        } else console.println(resourceBundle.getString("pathReceived"));
        args = args.trim();
        try {
            pushFile(args);
            for (String line = readLine(); line != null; line = readLine()) {
                String[] command = (line.trim() + " ").split(" ", 2);
                command[1] = command[1].trim();
                if (command[0].isBlank()) return;
                if (command[0].equals("execute_script")) {
                    if (isFileRepeat(command[1])) {
                        console.printError(resourceBundle.getString("recursion") + " " + new File(command[1]).getAbsolutePath());
                        continue;
                    }
                }
                console.println(resourceBundle.getString("commandInProgress") + " " + command[0]);
                if (client == null) continue;
                Response response = client.getResponse(new Request(command[0], command[1], user, GuiManager.getLocale()));
                printResponse(response);
                switch (response.getResponseStatus()) {
                    case ASKING_OBJECT -> {
                        HumanBeing humanBeing;
                        try {
                            humanBeing = new HumanBeingForm(console).build();
                        } catch (FileModeException exc) {
                            console.printError(resourceBundle.getString("invalidField"));
                            continue;
                        }
                        Response newResponse = client.getResponse(
                                new Request(command[0].trim(), command[1].trim(), user, humanBeing, GuiManager.getLocale())
                        );
                        if (newResponse.getResponseStatus() != ResponseStatus.OK) {
                            console.printError(newResponse.getResponse());
                        } else printResponse(newResponse);
                    }
                    case EXIT -> throw new ExitProgram();
                    case EXECUTE_SCRIPT -> {
                        executeFile(response.getResponse());
                        popRecursion();
                    }
                    case FAIL_LOGIN -> {
                        console.printError(resourceBundle.getString("reLogin"));
                        this.user = null;
                    }
                    default -> {}
                }
            }
            popFile();
        } catch (FileNotFoundException e) {
            console.printError(resourceBundle.getString("fileNotFound"));
        } catch (IOException e) {
            console.printError(resourceBundle.getString("ioExc"));
        }
    }

    public static void pushFile(String path) throws FileNotFoundException {
        paths.push(String.valueOf(Paths.get(path)));
        fileReader.push(new BufferedReader(new InputStreamReader(new FileInputStream(path))));
    }

    public static void popFile() throws IOException {
        fileReader.getFirst().close();
        fileReader.pop();
        if (!paths.isEmpty()) {
            paths.pop();
        }
    }

    public static void popRecursion() {
        if (!paths.isEmpty()) {
            paths.pop();
        }
    }

    public File getFile() {
        return new File(paths.getLast());
    }

    public static String readLine() throws IOException {
        return fileReader.getFirst().readLine();
    }

    private void printResponse(Response response) {
        switch (response.getResponseStatus()) {
            case OK -> {
                if (Objects.isNull(response.getCollection())) {
                    console.println(response.getResponse());
                } else {
                    console.println(response.getResponse() + "\n" + response.getCollection().toString());
                }
            }
            case ERROR -> console.printError(response.getResponse());
            case WRONG_ARGS -> console.printError(resourceBundle.getString("checkArgs"));
            default -> {}
        }
    }

    /**
     * Метод для обнаружения рекурсий в скриптах
     */
    public static boolean isFileRepeat(String path) {
        return paths.contains(new File(path).getAbsolutePath());
    }

    @Override
    public String nextLine() {
        try {
            return readLine();
        } catch (IOException e) {
            return "";
        }
    }
}
