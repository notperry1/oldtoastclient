package toast.client.command;

import net.minecraft.client.MinecraftClient;

import java.util.List;

public abstract class Command {

	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	public abstract String getName();
	public abstract String getDescription();
	public abstract String getSyntax();
	public abstract List<String> getAliases();
	public abstract void onCommand(String command,String[] args) throws Exception;
}
