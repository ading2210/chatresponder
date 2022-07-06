package net.ading2210.chatresponder.mixin;

import net.ading2210.chatresponder.ChatResponder;
import net.ading2210.chatresponder.EditRegexItemsScreen;
import net.ading2210.chatresponder.OptionsHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    public OptionsScreenMixin(Text text) {
        super(text);
    }

    @Inject(at=@At("TAIL"), method="init")
    private void init(CallbackInfo callback) {
        LiteralText message;
        if (ChatResponder.activated) {
            message = new LiteralText("Mod enabled");
        }
        else {
            message = new LiteralText("Mod disabled");
        }
        ButtonWidget button1 = new ButtonWidget(5, 3, 90, 20, message, button->{
            ChatResponder.activated = !ChatResponder.activated;
            if (ChatResponder.activated) {
                button.setMessage(new LiteralText("Mod enabled"));
            }
            else {
                button.setMessage(new LiteralText("Mod disabled"));
            }
        });
        this.addDrawableChild(button1);

        ButtonWidget button2 = new ButtonWidget(98, 3, 70, 20, new LiteralText("Settings"), button->{
            showOptionsScreen(((OptionsScreen)(Object)this));
        });
        this.addDrawableChild(button2);

        ButtonWidget button3 = new ButtonWidget(this.width-105, 3, 100, 20, new LiteralText("Reload config"), button->{
            OptionsHandler.openConfig();
            OptionsHandler.saveConfig();
        });
        this.addDrawableChild(button3);
    }
    public void showOptionsScreen (Screen parent) {
        EditRegexItemsScreen screen = new EditRegexItemsScreen(new LiteralText("Edit Items"), parent);
        MinecraftClient.getInstance().setScreen(screen);
    }
}
