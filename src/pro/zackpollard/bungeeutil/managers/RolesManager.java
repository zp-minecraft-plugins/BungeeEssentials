package pro.zackpollard.bungeeutil.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONRoles;

import java.io.*;
import java.util.*;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class RolesManager {

    private final BungeeEssentials instance;
    private GSONRoles config;
    private final File configFileFolder;
    private File configFile = null;
    private final String fileName;
    private final Set<UUID> vanishedUsers = new HashSet<>();

    public RolesManager(BungeeEssentials instance) {

        this.instance = instance;
        this.fileName = "roles.json";
        configFileFolder = new File(instance.getDataFolder().getAbsolutePath() + File.separator + "configs");
        configFileFolder.mkdirs();
        this.init();
    }

    public GSONRoles getRoles() {

        return config;
    }

    private void init() {

        configFile = new File(configFileFolder.getAbsolutePath() + File.separator + fileName);

        if (configFile.exists()) {

            config = this.loadConfig();
        } else {

            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            config = new GSONRoles();

            this.saveConfig();
        }

        if (config == null) {

            instance.getLogger().severe("The config could not be loaded. This will cause many issues. Fix the config and restart bungee!");
        }
    }

    private GSONRoles loadConfig() {

        GSONRoles loadedConfig;

        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), "UTF-8")) {

            Gson gson = new GsonBuilder().create();
            loadedConfig = gson.fromJson(reader, GSONRoles.class);

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

    public int getRole(UUID player) {

        return this.config.getRole(player);
    }

    public boolean setRole(UUID player, String role) {

        boolean result = this.config.setRole(player, role);
        this.saveConfig();

        return result;
    }

    public boolean removeRole(UUID player) {

        boolean result = this.config.removeRole(player);
        this.saveConfig();

        return result;
    }

    public int isStaff(UUID player) {

        return this.config.getRole(player);
    }

    public String getRoleColoredName(int roleID) {

        return this.config.getRoleColoredName(roleID);
    }

    public Map<UUID, Integer> getUsersOnlineWithRoles() {

        Map<UUID, Integer> map = new HashMap<>(this.config.getUsersOnlineWithRoles());

        for (UUID uuid : vanishedUsers) {

            map.remove(uuid);
        }

        return map;
    }

    public Map<UUID, Integer> getUsersWithRoles() {

        return this.config.getUsersWithRoles();
    }

    public Set<UUID> getVanishedUsers() {

        return this.vanishedUsers;
    }

    public boolean toggleVanished(UUID uuid) {

        if (vanishedUsers.contains(uuid)) {

            vanishedUsers.remove(uuid);
            return false;
        } else {

            vanishedUsers.add(uuid);
            return true;
        }
    }


    public void sendMessageToRole(BaseComponent[] message, int role) {

        for (ProxiedPlayer proxiedPlayer : instance.getProxy().getPlayers()) {

            if (getRole(proxiedPlayer.getUniqueId()) >= role) {

                proxiedPlayer.sendMessage(message);
            }
        }
    }
}