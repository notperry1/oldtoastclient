package toast.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class ToastLogger {

	public static void infoMessage(String s) {
		try{ MinecraftClient.getInstance().inGameHud.getChatHud()
			.addMessage(new LiteralText("§5[ToastClient] §9§lINFO: §9" + s));
		}catch(Exception e) { System.out.println("[ToastClient] INFO: " + s); }
	}
	
	public static void warningMessage(String s) {
		try{ MinecraftClient.getInstance().inGameHud.getChatHud()
			.addMessage(new LiteralText("§5[ToastClient] §e§lWARN: §e" + s));
		}catch(Exception e) { System.out.println("[ToastClient] WARN: " + s); }
	}
	
	public static void errorMessage(String s) {
		try{ MinecraftClient.getInstance().inGameHud.getChatHud()
			.addMessage(new LiteralText("§5[ToastClient] §c§lERROR: §c" + s));
		}catch(Exception e) { System.out.println("[ToastClient] ERROR: " + s); }
	}
	
	public static void noPrefixMessage(String s) {
		try{ MinecraftClient.getInstance().inGameHud.getChatHud()
			.addMessage(new LiteralText(s));
		}catch(Exception e) { System.out.println(s); }
	}
}
