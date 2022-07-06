package net.ading2210.chatresponder;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ChatResponder implements ModInitializer {
    public static final Logger logger = LoggerFactory.getLogger("chatresponder");
    public static boolean activated = false;
    public static ArrayList<Pattern> regexCompiled = new ArrayList<>();
    @Override
    public void onInitialize() {
        OptionsHandler.openConfig();
        //RegexItem regexItem = new RegexItem("test");
        //regexItem.addCommandItem(new CommandItem("eee", 1000));
        //OptionsHandler.options.regexEntries.add(regexItem);
        //OptionsHandler.saveConfig();
        logger.info("compiling regex");

        for (RegexItem regexItem1: OptionsHandler.options.regexEntries) {
            Pattern pattern = Pattern.compile(regexItem1.regex);
            regexCompiled.add(pattern);
        }

        logger.info("mod started");
    }
}
