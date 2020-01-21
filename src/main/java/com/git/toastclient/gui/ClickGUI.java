package com.git.toastclient.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;

public class ClickGUI extends LightweightGuiDescription {

    public ClickGUI () {

        WToggleButton toggleButton = new WToggleButton();
        WPlainPanel root = new WPlainPanel();
        WLabel label =  new WLabel("Mushroom Client");
        WLabel scafLabel =  new WLabel("Scaffold");

        setRootPanel(root);
        root.setSize(100,100);
        root.add(label, 0, 0);
        root.add(toggleButton, 80, 20, 10, 10);
        root.add(scafLabel, 0, 25);
    }

}
