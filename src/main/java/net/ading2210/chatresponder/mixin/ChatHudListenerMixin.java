package net.ading2210.chatresponder.mixin;

import net.ading2210.chatresponder.ChatResponder;
import net.ading2210.chatresponder.OptionsHandler;
import net.ading2210.chatresponder.RegexItem;
import net.ading2210.chatresponder.Utils;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatHudListener.class)
public class ChatHudListenerMixin {
    @Inject(at=@At("TAIL"), method="onChatMessage")
    public void onChatMessage(MessageType type, Text message, UUID sender, CallbackInfo callback) {
        if (ChatResponder.activated) {
            String messageString = message.getString();
            String messageFiltered = messageString.replaceAll("§.", "");

            for (int i = 0; i < OptionsHandler.options.regexEntries.size(); i++) {
                Pattern pattern = ChatResponder.regexCompiled.get(i);
                Matcher matcher = pattern.matcher(messageFiltered);
                if (matcher.find()) {
                    RegexItem regexItem = OptionsHandler.options.regexEntries.get(i);
                    Utils.sendClientMessage("§7§oGot match for regex: "+regexItem.regex+"§r");
                    Utils.executeEntry(i);
                }
            }
        }
    }
}
