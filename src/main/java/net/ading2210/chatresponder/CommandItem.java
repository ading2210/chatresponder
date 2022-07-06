package net.ading2210.chatresponder;

import com.google.gson.annotations.Expose;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class CommandItem {
    public String command;
    public int delay;
    public transient TextFieldWidget commandWidget;
    public transient TextFieldWidget delayWidget;
    public transient ButtonWidget deleteButton;
    public CommandItem (String command, int delay) {
        this.command = command;
        this.delay = delay;
    }
}
