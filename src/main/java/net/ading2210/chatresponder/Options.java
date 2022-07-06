package net.ading2210.chatresponder;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.ArrayList;

public class Options {
    public ArrayList<RegexItem> regexEntries = new ArrayList<>();
    public transient TextFieldWidget addRegexWidget;
    public transient ButtonWidget addButton;
}
