package org.example.CommandSpace;

import org.example.Gui.Actions.ExecuteScriptAction;
import org.example.Gui.Components.ExecutionPanel;

import javax.swing.*;

/**
 * Класс консоли для вывода
 */
public class Console implements Printable {
    private static boolean fileMode = false;
    private ExecutionPanel executionPanel = null;

    public Console() {
    }

    public Console(ExecutionPanel executionPanel) {
        this.executionPanel = executionPanel;
    }

    public static boolean getFileMode() {
        return fileMode;
    }

    /**
     * Задать режим работы с файлом
     * @param fileMode
     */
    public static void setFileMode(boolean fileMode) {
        Console.fileMode = fileMode;
    }

    @Override
    public void println(String stringLine) {
        System.out.println(stringLine);
        if (executionPanel != null) {
            executionPanel.printConsoleText(stringLine);
        }
    }

    @Override
    public void print(String string) {
        System.out.print(string);
    }

    @Override
    public void printError(String stringError) {
        System.out.println("\u001B[31m" + stringError + "\u001B[0m");
        if (executionPanel != null) {
            executionPanel.printErrorText(stringError);
        }
    }
}
