package pro.zackpollard.bungeeutil.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONMessages;

import java.io.*;

public class MessagesManager {

    private final BungeeEssentials instance;
    private GSONMessages config;
    private final File configFileFolder;
    private File configFile = null;
    private final String fileName;

    public MessagesManager(BungeeEssentials instance) {

        this.instance = instance;
        this.fileName = "messages.json";
        configFileFolder = new File(instance.getDataFolder().getAbsolutePath() + File.separator + "configs");
        configFileFolder.mkdirs();
        this.init();
    }

    public GSONMessages getMessages() {

        return config;
    }

    private void init() {

        configFile = new File(configFileFolder.getAbsolutePath() + File.separator + fileName);

        if(configFile.exists()) {

            config = this.loadConfig();
        } else {

            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            config = new GSONMessages();

            this.saveConfig();
        }

        if(config == null) {

            instance.getLogger().severe("The config could not be loaded. This will cause many issues. Fix the config and restart bungee!");
        }
    }

    private GSONMessages loadConfig() {

        GSONMessages loadedConfig;

        try(Reader reader = new InputStreamReader(new FileInputStream(configFile), "UTF-8")) {

            Gson gson = new GsonBuilder().create();
            loadedConfig = gson.fromJson(reader, GSONMessages.class);

            return loadedConfig;
        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    public boolean saveConfig() {

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(config);

        FileOutputStream outputStream;

        try {

            outputStream = new FileOutputStream(configFile);
            outputStream.write(json.getBytes());
            outputStream.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            instance.getLogger().severe("The config could not be saved as the file couldn't be found on the storage device. Please check the directories read/write permissions and contact the developer!");
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().severe("The config could not be written to as an error occured. Please check the directories read/write permissions and contact the developer!");
        }

        return false;
    }
}