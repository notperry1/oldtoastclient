package bleach.hack.gui;

import bleach.hack.utils.FabricReflect;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;

public class ProtocolScreen extends Screen {

	private TextFieldWidget nameField;
	private TextFieldWidget protocolField; // int
	private TextFieldWidget targetField;
	private TextFieldWidget packVerField; // int
	private MultiplayerScreen serverScreen;
	
	public ProtocolScreen(MultiplayerScreen serverScreen) {
		super(new LiteralText("Protocol Screen"));
		this.serverScreen = serverScreen;
	}
	
	public void init() {
		addButton(new ButtonWidget(width / 2 - 100, height / 2 + 50, 98, 20, "Update", button -> {
			try {
				int i = Integer.parseInt(protocolField.getText());
				int i1 = Integer.parseInt(packVerField.getText());
				
				FabricReflect.writeField(SharedConstants.getGameVersion(), nameField.getText(), "field_16733", "name");
				FabricReflect.writeField(SharedConstants.getGameVersion(), i, "field_16735", "protocolVersion");
				FabricReflect.writeField(SharedConstants.getGameVersion(), i1, "field_16734", "packVersion");
				FabricReflect.writeField(SharedConstants.getGameVersion(), targetField.getText(), "field_16740", "releaseTarget");
				System.out.println("Set Protocol");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		addButton(new ButtonWidget(width / 2 + 2, height / 2 + 50, 98, 20, "Done", button -> {
			minecraft.openScreen(serverScreen);
		}));
		
		nameField = new TextFieldWidget(font, width / 2 - 98, height / 2 - 60, 196, 18, "");
		nameField.setText(SharedConstants.getGameVersion().getName());
		protocolField = new TextFieldWidget(font, width / 2 - 98, height / 2 - 35, 196, 18, "");
		protocolField.setText(SharedConstants.getGameVersion().getProtocolVersion() + "");
		targetField = new TextFieldWidget(font, width / 2 - 98, height / 2 - 10, 196, 18, "");
		targetField.setText(SharedConstants.getGameVersion().getReleaseTarget());
		packVerField = new TextFieldWidget(font, width / 2 - 98, height / 2 + 15, 196, 18, "");
		packVerField.setText(SharedConstants.getGameVersion().getPackVersion() + "");
		//ipField.changeFocus(true);
	}
	
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		renderBackground();
		drawRightAlignedString(font, "§7Name:", width / 2 - 103, height / 2 - 55, -1);
		drawRightAlignedString(font, "§7Protocol:", width / 2 - 103, height / 2 - 30, -1);
		drawRightAlignedString(font, "§7Target Ver:", width / 2 - 103, height / 2 - 5, -1);
		drawRightAlignedString(font, "§7Packet Ver:", width / 2 - 103, height / 2 + 20, -1);
		nameField.render(p_render_1_, p_render_2_, p_render_3_);
		protocolField.render(p_render_1_, p_render_2_, p_render_3_);
		targetField.render(p_render_1_, p_render_2_, p_render_3_);
		packVerField.render(p_render_1_, p_render_2_, p_render_3_);
		
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
	
	public void onClose() {
		minecraft.openScreen(serverScreen);
	}
	
	public boolean mouseClicked(double double_1, double double_2, int int_1) {
		if(double_1 >= nameField.x && double_1 <= nameField.x + nameField.getWidth()
				&& double_2 >= nameField.y && double_2 <= nameField.y + 18) {
			nameField.changeFocus(true);
			protocolField.setSelected(false);
			targetField.setSelected(false);
			packVerField.setSelected(false);
		}
		if(double_1 >= protocolField.x && double_1 <= protocolField.x + protocolField.getWidth()
				&& double_2 >= protocolField.y && double_2 <= protocolField.y + 18) {
			nameField.setSelected(false);
			protocolField.changeFocus(true);
			targetField.setSelected(false);
			packVerField.setSelected(false);
		}
		if(double_1 >= targetField.x && double_1 <= targetField.x + targetField.getWidth()
				&& double_2 >= targetField.y && double_2 <= targetField.y + 18) {
			nameField.setSelected(false);
			protocolField.setSelected(false);
			targetField.changeFocus(true);
			packVerField.setSelected(false);
		}
		if(double_1 >= packVerField.x && double_1 <= packVerField.x + packVerField.getWidth()
				&& double_2 >= packVerField.y && double_2 <= packVerField.y + 18) {
			nameField.setSelected(false);
			protocolField.setSelected(false);
			targetField.setSelected(false);
			packVerField.changeFocus(true);
		}
		return super.mouseClicked(double_1, double_2, int_1);
	}
	
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		if(nameField.isFocused()) nameField.charTyped(p_charTyped_1_, p_charTyped_2_);
		if(protocolField.isFocused()) protocolField.charTyped(p_charTyped_1_, p_charTyped_2_);
		if(targetField.isFocused()) targetField.charTyped(p_charTyped_1_, p_charTyped_2_);
		if(packVerField.isFocused()) packVerField.charTyped(p_charTyped_1_, p_charTyped_2_);
		return super.charTyped(p_charTyped_1_, p_charTyped_2_);
	}
	
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if(nameField.isFocused()) nameField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		if(protocolField.isFocused()) protocolField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		if(targetField.isFocused()) targetField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		if(packVerField.isFocused()) packVerField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	public void tick() {
		nameField.tick();
		protocolField.tick();
		targetField.tick();
		packVerField.tick();
		super.tick();
	}
}
