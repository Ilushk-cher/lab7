package org.example;

import org.example.CommandSpace.BlankConsole;
import org.example.CommandSpace.Console;
import org.example.CommandSpace.Printable;
import org.example.Exceptions.InvalidArguments;
import org.example.Gui.GuiManager;

public class Main {
    private static String host = "localhost";
    private static int port = 8080;
    private static Printable console = new BlankConsole();

    public static void main(String[] args) {
        console = new Console();
        Client client = new Client(host, port, 5000, 5, console);
//        console.println("Добро пожаловать!");
//        new RuntimeManager(console, new Scanner(System.in), client).runInteractive();
        new GuiManager(client);
    }
}