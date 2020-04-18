package toast.client.gui.window;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class AbstractWindowScreen extends Screen {

	public List<Window> windows = new ArrayList<>();
	
	public AbstractWindowScreen(Text text_1) {
		super(text_1);
	}
	
	public void render(int int_1, int int_2, float float_1) {
		boolean close = true;
		int noneSelected = -1;
		int selected = -1;
		int count = 0;
		for(Window w: windows) {
			if(!w.closed) {
				close = false;
				if(!w.selected) {
					onRenderWindow(count, int_1, int_2);
				}else {
					selected = count;
				}
				
				if(noneSelected >= -1) noneSelected = count;
			}
			
			if(w.selected && !w.closed) {
				noneSelected = -2;
			}
			count++;
		}
		
		if(selected >= 0) onRenderWindow(selected, int_1, int_2);
		if(noneSelected >= 0) windows.get(noneSelected).selected = true;
		if(close) this.onClose();
		
		super.render(int_1, int_2, float_1);
	}
	
	public void onRenderWindow(int window, int mX, int mY) {
		if(!windows.get(window).closed) {
			windows.get(window).render(mX, mY);
		}
	}
	
	public void drawButton(String text, int x1, int y1, int x2, int y2) {
		Screen.fill(x1, y1, x2 - 1, y2 - 1, 0xffb0b0b0);
		Screen.fill(x1 + 1, y1 + 1, x2, y2, 0xff000000);
		Screen.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xff858585);
		drawCenteredString(font, text, x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2 - 4, -1);
	}
	
	public void selectWindow(int window) {
		int count = 0;
		for(Window w: windows) {
			w.selected = (count == window);
			count++;
		}
	}
	
	public boolean mouseClicked(double double_1, double double_2, int int_1) {
		/* Handle what window will be selected when clicking */
		int count = 0;
		int nextSelected = -1;
		for(Window w: windows) {
			w.onMousePressed((int) double_1, (int) double_2);
			
			if(w.shouldClose((int) double_1, (int) double_2)) w.closed = true;
			
			if(double_1 > w.x1 && double_1 < w.x2 && double_2 > w.y1 && double_2 < w.y2 && !w.closed) {
				if(w.selected) {
					nextSelected = -1;
					break;
				}else {
					nextSelected = count;
				}
			}
			count++;
		}
		
		if(nextSelected >= 0) {
			for(Window w: windows) w.selected = false;
			windows.get(nextSelected).selected = true;
		}
		
		return super.mouseClicked(double_1, double_2, int_1);
	}
	
	public boolean mouseReleased(double double_1, double double_2, int int_1) {
		for(Window w: windows) {
			w.onMouseReleased((int) double_1, (int) double_2);
		}
		
		return super.mouseReleased(double_1, double_2, int_1);
	}

}
