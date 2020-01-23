package com.git.toastclient.command;

import com.git.toastclient.command.syntax.SyntaxChunk;
import com.git.toastclient.setting.Setting;
import com.git.toastclient.util.Wrapper;
import com.sun.scenario.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Command {

    protected String label;
    protected String syntax;
    protected String description;
    protected ArrayList<String> aliases;

    public final MinecraftClient mc = Wrapper.getMinecraft();

    protected SyntaxChunk[] syntaxChunks;

    public static Setting<String> commandPrefix = Settings.s("commandPrefix", ".");

    public Command(String label, SyntaxChunk[] syntaxChunks, ArrayList<String> aliases) {
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
        this.aliases = aliases;
    }

    public Command(String label, SyntaxChunk[] syntaxChunks, String... aliases) {
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
        this.aliases = new ArrayList<String>(Arrays.asList(aliases));
    }

    public static void sendChatMessage(String message) {
        sendRawChatMessage("&7[&a" + "Mush" + "&7] &r" + message);
    }

    public static void sendErrorMessage(String message) {
        sendRawChatMessage("&7[&4" + "Mush" + "&7] &r" + message);
    }

    public static void sendWarningMessage(String message) {
        sendRawChatMessage("&7[&6" + "Mush" + "&7] &r" + message);
    }

    public static void sendStringChatMessage(String[] messages) {
        sendChatMessage("");
        for (String s : messages) sendRawChatMessage(s);
    }

    public static void sendRawChatMessage(String message) {
        if (isSendable()) {
            Wrapper.getPlayer().sendMessage(new ChatMessage(message));
        } else {
            System.out.println("Blue Mushroom: Avoided NPE by logging to file instead of chat\n" + message);
        }
    }

    public static boolean isSendable() {
        if (Wrapper.getPlayer() == null) {
            return false;
        } else {
            return true;
        }
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String getCommandPrefix() {
        return commandPrefix.getValue();
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public void call(String[] args) {

    }

    public SyntaxChunk[] getSyntaxChunks() {
        return syntaxChunks;
    }

    public static class ChatMessage extends BaseText {

        String text;

        public ChatMessage(String text) {

            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(text);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String replacement = "\u00A7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }

            m.appendTail(sb);

            this.text = sb.toString();
        }

        public String getUnformattedComponentText() {
            return text;
        }

        @Override
        public Text clone() {
            return new ChatMessage(text);
        }

        @Override
        public String asString() {
            return null;
        }

        @Override
        public Text copy() {
            return null;
        }
    }

    protected SyntaxChunk getSyntaxChunk(String name) {
        for (SyntaxChunk c : syntaxChunks) {
            if (c.getType().equals(name))
                return c;
        }
        return null;
    }
}
