package com.git.toastclient.command.commands;

import com.git.toastclient.command.Command;
import com.git.toastclient.command.syntax.SyntaxChunk;

import java.util.ArrayList;

public class BindCommand extends Command {


    public BindCommand(String label, SyntaxChunk[] syntaxChunks, ArrayList<String> aliases) {
        super(label, syntaxChunks, aliases);
    }
}
