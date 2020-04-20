package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.command.CommandManager;
import toast.client.utils.ToastLogger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CmdAliases extends Command {

    static String message = "";

    @Override
    public String getName() {
        return "aliases";
    }

    @Override
    public String getDescription() {
        return "Get aliases of each command";
    }

    @Override
    public String getSyntax() {
        return "aliases";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (Objects.equals(message, "")) {
            for (Command c : CommandManager.getCommands()) {
                if (!c.getAliases().isEmpty()) {
                    message = message.concat("§5" + c.getName() + "§f - ");
                    c.getAliases().forEach(alias -> message = message.concat("§6" + alias + "§f, "));
                    message = message
                            .substring(0, message.length() - 2)
                            .concat("\n");
                }
            }
            if (message != null && message.endsWith("\n")) message = message.substring(0, message.length() - 1);
        }
        ToastLogger.noPrefixMessage("§eAliases:\n" + message);
    }
}
