package toast.client.event.events;

import toast.client.event.Event;
import net.minecraft.client.gui.screen.Screen;

public class EventOpenScreen extends Event {

	private Screen screen;
	
	public EventOpenScreen(Screen screen) {
		this.screen = screen;
	}
	
	public Screen getScreen() {
		return screen;
	}
}
