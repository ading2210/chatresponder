package net.ading2210.chatresponder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Utils {
    private static final MinecraftClient mcClient = MinecraftClient.getInstance();
    public static void sendClientMessage(String message) {
        mcClient.player.sendMessage(Text.of(message), false);
    }
    public static void executeEntry(int index) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                boolean stopped = false;
                RegexItem regexItem = OptionsHandler.options.regexEntries.get(index);
                for (CommandItem command: regexItem.steps) {
                    try {
                        for (int i = 0; i < command.delay/10; i++) {
                            if (!(ChatResponder.activated)) {
                                stopped = true;
                                break;
                            }
                            Thread.sleep(10);
                        }
                        if (stopped) {
                            break;
                        }
                    }
                    catch (InterruptedException ex) {
                        break;
                    }
                    mcClient.player.sendChatMessage(command.command);
                }
            }
        });
        thread.start();
    }
}
