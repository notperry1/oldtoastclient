package toast.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import org.lwjgl.opengl.GL11;

public class ToastCheckbox extends AbstractPressableButtonWidget {

	public boolean checked;
	
	public ToastCheckbox(int int_1, int int_2, String text, boolean pressed) {
		super(int_1, int_2, int_1 + 10, int_2 + 10 + MinecraftClient.getInstance().textRenderer.getStringWidth(text), text);
		checked = pressed;
	}

	@Override
	public void onPress() {
		checked = !checked;
	}

	public void renderButton(int int_1, int int_2, float float_1) {
		MinecraftClient minecraftClient_1 = MinecraftClient.getInstance();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		TextRenderer textRenderer = minecraftClient_1.textRenderer;
		int color = int_1 > x && int_1 < x + 10 && int_2 > y && int_2 < y + 10 ? 0xFFD0F0D0 : 0xFFA0A0A0;
		fill(x, y, x + 11, y + 11, 0xFF000000);
		fill(x, y, x + 1, y + 11, color);
		fill(x, y + 10, x + 11, y + 11, color);
		fill(x, y, x + 10, y + 1, color);
		fill(x + 10, y, x + 11, y + 11, color);
		if (checked) fill(x + 3, y + 5, x + 8, y + 6, color);
		drawString(textRenderer, getMessage(), x + 15, y + 2, 0xC0C0C0);
	}
}
