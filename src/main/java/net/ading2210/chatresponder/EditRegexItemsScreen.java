package net.ading2210.chatresponder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditRegexItemsScreen extends Screen {
    public Screen parent;
    public int yOffset = 0;
    public ButtonWidget cancelButton;
    public ButtonWidget saveButton;
    public EditRegexItemsScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }
    public void init(){
        //todo: use ElementListWidget instead of this terrible scrolling method
        OptionsHandler.openConfig();
        this.initItems();
    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (RegexItem regexEntry: OptionsHandler.options.regexEntries) {
            if (regexEntry.addCommandWidget.getText().length() < 1) {
                regexEntry.addCommandWidget.setSuggestion("Chat message goes here. Prefix with / to make it a command.");
            }
            else {
                regexEntry.addCommandWidget.setSuggestion("");
            }
            if (regexEntry.addDelayWidget.getText().length() < 1) {
                regexEntry.addDelayWidget.setSuggestion("Delay");
            }
            else {
                regexEntry.addDelayWidget.setSuggestion("");
            }
        }
        if (OptionsHandler.options.addRegexWidget.getText().length() < 1) {
            OptionsHandler.options.addRegexWidget.setSuggestion("Regex string goes here. Regex guide: regex101.com");
        }
        else {
            OptionsHandler.options.addRegexWidget.setSuggestion("");
        }

        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
    }
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        this.yOffset += amount * 16;
        if (this.yOffset > 0) {
            this.yOffset = 0;
        }
        this.updateItems();
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    public void initItems() {
        this.clearChildren();
        int currentY = 0;
        for (int i = 0; i < OptionsHandler.options.regexEntries.size(); i++) {
            int finalI = i;
            RegexItem regexEntry = OptionsHandler.options.regexEntries.get(finalI);
            regexEntry.regexWidget = new TextFieldWidget(this.textRenderer, 8, currentY+30+this.yOffset, this.width-40, 16, new LiteralText(""));
            regexEntry.regexWidget.setMaxLength(99999);
            regexEntry.regexWidget.setText(regexEntry.regex);

            regexEntry.deleteButton = new ButtonWidget(this.width-28, currentY+28+this.yOffset, 20, 20, new LiteralText("-"), button -> {
                OptionsHandler.options.regexEntries.remove(finalI);
                this.initItems();
            });

            this.addDrawableChild(regexEntry.regexWidget);
            this.addDrawableChild(regexEntry.deleteButton);

            for (int j = 0; j < regexEntry.steps.size(); j++) {
                int finalJ = j;
                currentY += 25;

                CommandItem commandItem = regexEntry.steps.get(finalJ);
                commandItem.commandWidget = new TextFieldWidget(this.textRenderer, 18, currentY+30+this.yOffset, this.width-106, 16, new LiteralText(""));
                commandItem.commandWidget.setMaxLength(256);
                commandItem.commandWidget.setText(commandItem.command);

                commandItem.deleteButton = new ButtonWidget(this.width-28, currentY+28+this.yOffset, 20, 20, new LiteralText("-"), button -> {
                    OptionsHandler.options.regexEntries.get(finalI).steps.remove(finalJ);
                    this.initItems();
                });

                commandItem.delayWidget = new TextFieldWidget(this.textRenderer, this.width-82, currentY+30+this.yOffset, 50, 16, new LiteralText(""));
                commandItem.delayWidget.setText(Integer.toString(commandItem.delay));

                this.addDrawableChild(commandItem.commandWidget);
                this.addDrawableChild(commandItem.delayWidget);
                this.addDrawableChild(commandItem.deleteButton);
            }
            currentY += 25;

            regexEntry.addCommandWidget = new TextFieldWidget(this.textRenderer, 18, currentY+30+this.yOffset, this.width-106, 16, new LiteralText(""));
            regexEntry.addCommandWidget.setMaxLength(256);
            regexEntry.addDelayWidget = new TextFieldWidget(this.textRenderer, this.width-82, currentY+30+this.yOffset, 50, 16, new LiteralText(""));
            regexEntry.addDelayWidget.setSuggestion("Delay");

            regexEntry.addButton = new ButtonWidget(this.width-28, currentY+28+this.yOffset, 20, 20, new LiteralText("+"), button -> {
                if (regexEntry.addCommandWidget.getText().length() > 0) {
                    if (regexEntry.addDelayWidget.getText().length() > 0 && regexEntry.addDelayWidget.getText().matches("\\d++")) {
                        CommandItem commandItem = new CommandItem(regexEntry.addCommandWidget.getText(), Integer.parseInt(regexEntry.addDelayWidget.getText()));
                        regexEntry.steps.add(commandItem);
                        this.initItems();
                    }
                }
            });

            this.addDrawableChild(regexEntry.addCommandWidget);
            this.addDrawableChild(regexEntry.addButton);
            this.addDrawableChild(regexEntry.addDelayWidget);

            currentY += 25;
        }

        OptionsHandler.options.addRegexWidget = new TextFieldWidget(this.textRenderer, 8, currentY+30+this.yOffset, this.width-40, 16, new LiteralText(""));
        OptionsHandler.options.addRegexWidget.setMaxLength(99999);
        OptionsHandler.options.addButton = new ButtonWidget(this.width-28, currentY+28+this.yOffset, 20, 20, new LiteralText("+"), button -> {
            if (OptionsHandler.options.addRegexWidget.getText().length() > 0) {
                RegexItem regexItem = new RegexItem(OptionsHandler.options.addRegexWidget.getText());
                OptionsHandler.options.regexEntries.add(regexItem);
                this.initItems();
            }
        });

        this.addDrawableChild(OptionsHandler.options.addRegexWidget);
        this.addDrawableChild(OptionsHandler.options.addButton);

        this.cancelButton = new ButtonWidget(5, this.height-25, 90, 20, new LiteralText("Cancel"), button -> MinecraftClient.getInstance().setScreen(this.parent));
        this.addDrawableChild(this.cancelButton);

        this.saveButton = new ButtonWidget(100, this.height-25, 90, 20, new LiteralText("Save"), button -> {
            for (int i = 0; i < OptionsHandler.options.regexEntries.size(); i++) {
                RegexItem regexEntry = OptionsHandler.options.regexEntries.get(i);
                regexEntry.regex = regexEntry.regexWidget.getText();
                for (int j = 0; j < regexEntry.steps.size(); j++) {
                    CommandItem commandItem = regexEntry.steps.get(j);
                    commandItem.command = commandItem.commandWidget.getText();
                    commandItem.delay = Integer.parseInt(commandItem.delayWidget.getText());
                }
            }
            
            OptionsHandler.saveConfig();
            ChatResponder.regexCompiled = new ArrayList<>();
            for (RegexItem regexItem1: OptionsHandler.options.regexEntries) {
                Pattern pattern = Pattern.compile(regexItem1.regex);
                ChatResponder.regexCompiled.add(pattern);
            }
            MinecraftClient.getInstance().setScreen(this.parent);
        });
        this.addDrawableChild(this.saveButton);
    }
    public void updateItems() {
        int currentY = 0;
        for (int i = 0; i < OptionsHandler.options.regexEntries.size(); i++) {
            RegexItem regexEntry = OptionsHandler.options.regexEntries.get(i);
            regexEntry.regexWidget.y = currentY+30+this.yOffset;
            regexEntry.deleteButton.y = currentY+28+this.yOffset;
            for (int j = 0; j < regexEntry.steps.size(); j++) {
                CommandItem commandItem = regexEntry.steps.get(j);
                currentY += 25;
                commandItem.commandWidget.y = currentY+30+this.yOffset;
                commandItem.delayWidget.y = currentY+30+this.yOffset;
                commandItem.deleteButton.y = currentY+28+this.yOffset;
            }
            currentY += 25;
            regexEntry.addCommandWidget.y = currentY+30+this.yOffset;
            regexEntry.addDelayWidget.y = currentY+30+this.yOffset;
            regexEntry.addButton.y = currentY+28+this.yOffset;
            currentY += 25;
        }
        OptionsHandler.options.addRegexWidget.y = currentY+30+this.yOffset;
        OptionsHandler.options.addButton.y = currentY+28+this.yOffset;
    }
}
