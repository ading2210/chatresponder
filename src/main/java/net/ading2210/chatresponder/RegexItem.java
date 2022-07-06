package net.ading2210.chatresponder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.ArrayList;

public class RegexItem {
    public String regex;
    public ArrayList<CommandItem> steps = new ArrayList<>();
    public transient TextFieldWidget regexWidget;
    public transient ButtonWidget deleteButton;
    public transient TextFieldWidget addCommandWidget;
    public transient TextFieldWidget addDelayWidget;
    public transient ButtonWidget addButton;
    public RegexItem(String regex) {
        this.regex = regex;
        CommandItem[] steps = new CommandItem[0];
    }
    public void addCommandItem(CommandItem command) {
        steps.add(command);
    }
}
