package net.ading2210.chatresponder.mixin;

import net.ading2210.chatresponder.Utils;
import net.ading2210.chatresponder.ChatResponder;
import net.minecraft.client.network.ClientPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at=@At("HEAD"), method="sendChatMessage", cancellable=true)
    public void sendChatMessage(String message, CallbackInfo callback) {
        if (message.equals("!toggle")) {
            ChatResponder.activated = !ChatResponder.activated;
            if (ChatResponder.activated) {
                Utils.sendClientMessage("Mod is active");
            }
            else {
                Utils.sendClientMessage("Mod is no longer active");
            }
            callback.cancel();
        }
    }

}
