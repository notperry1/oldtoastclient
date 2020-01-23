package com.git.toastclient.command;

import com.mojang.brigadier.Command;
import com.sun.beans.finder.ClassFinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CommandManager {

    private ArrayList<Command> commands;

    public CommandManager(){
        commands = new ArrayList<>();

        Set<Class> classList = ClassFinder.findClasses(BindCommand.class.getPackage().getName(), Command.class);

    }

    public void callCommand(String command) {
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Split by every space if it isn't surrounded by quotes



    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        List result = new LinkedList();

        for (int i = 0; i < input.length; i++) {
            if (i != indexToDelete) result.add(input[i]);
        }

        return (String[]) result.toArray(input);
    }


    private static String strip(String str, String key) {
        if (str.startsWith(key) && str.endsWith(key)) return str.substring(key.length(), str.length() - key.length());
        return str;
    }

    public Command getCommandByLabel(String commandLabel) {
        for (Command c : commands) {
            if (c.getLabel().equals(commandLabel)) return c;
        }
        return null;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}

