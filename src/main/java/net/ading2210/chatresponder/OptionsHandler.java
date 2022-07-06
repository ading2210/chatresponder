package net.ading2210.chatresponder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class OptionsHandler {
    public static Options options = new Options();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Path configPath = FabricLoader.getInstance().getConfigDir().resolve("chatresponder.json");
    public static void openConfig() {
        try {
            FileReader filereader = new FileReader(configPath.toString());
            options = gson.fromJson(filereader, Options.class);
            filereader.close();
        }
        catch (FileNotFoundException ex) {
            saveConfig();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void saveConfig() {
        try {
            FileWriter filewriter = new FileWriter(configPath.toString());
            gson.toJson(options, filewriter);
            filewriter.flush();
            filewriter.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
