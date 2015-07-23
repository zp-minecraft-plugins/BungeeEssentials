package pro.zackpollard.bungeeutil;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginClassloader;
import net.md_5.bungee.api.plugin.PluginDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import pro.zackpollard.bungeeutil.json.storage.GSONIPAddress;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;
import pro.zackpollard.bungeeutil.managers.*;
import sun.plugin.security.PluginClassLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class BungeeEssentials extends Plugin {

    private static BungeeEssentials instance;
    private PlayerManager playerManager;
    private ConfigManager configManager;
    private IPManager ipManager;
    private StaffChatManager staffChatManager;
    private PrivateChatManager privateChatManager;
    private ServerChatManager serverChatManager;
    private ServerPingManager serverPingManager;
    private CommandManager commandManager;
    private SessionServerManager sessionServerManager;

    public static BungeeEssentials getInstance() {

        return instance;
    }

    public void onEnable() {

        instance = this;
        instance.getDataFolder().mkdirs();
        this.configManager = new ConfigManager(this);
        this.sessionServerManager = new SessionServerManager(this);
        this.playerManager = new PlayerManager(this);
        this.ipManager = new IPManager(this);
        this.staffChatManager = new StaffChatManager(this);
        this.privateChatManager = new PrivateChatManager(this);
        this.serverChatManager = new ServerChatManager(this);
        this.serverPingManager = new ServerPingManager(this);
        this.commandManager = new CommandManager(this);

        //TODO: http://pastebin.com/HZE8uh8C
    }

    public void onDisable() {

        this.onDisable(false);
    }

    public void onDisable(boolean reload) {

        if (!reload) {

            this.configManager.saveConfigs();
        }

        this.getProxy().getPluginManager().unregisterCommands(this);
        this.getProxy().getPluginManager().unregisterListeners(this);
        this.getProxy().getScheduler().cancel(this);

        for (UUID uuid : this.playerManager.getGsonPlayerCache(true).keySet()) {

            GSONPlayer gsonPlayer = this.playerManager.getPlayer(uuid);

            ProxiedPlayer player = this.getProxy().getPlayer(uuid);
            if(player != null) {

                this.getPlayerManager().onPlayerLeave(player);
            }

            this.playerManager.unloadPlayer(uuid, true);
        }

        for (GSONIPAddress gsonIP : this.ipManager.getIPCache(true).values()) {

            this.ipManager.unloadIP(gsonIP.getIP());
        }

        instance = null;
        this.playerManager = null;
        this.ipManager = null;
        this.configManager = null;
        this.staffChatManager = null;
        this.privateChatManager = null;
        this.serverChatManager = null;
        this.serverPingManager = null;
        this.commandManager = null;
    }

    public void reload() {

        this.onDisable(true);
        this.onEnable();
    }

    public void restart() {

        this.onDisable(false);
        this.reloadNewInstance();
    }

    public void reloadNewInstance() {

        try {

            Map<String, Plugin> type;
            Map<String, PluginDescription> type2;
            Set<PluginClassloader> type3;

            Field plugins = this.getProxy().getPluginManager().getClass().getDeclaredField("plugins");
            Field toLoad = this.getProxy().getPluginManager().getClass().getDeclaredField("toLoad");
            Field classLoaders = this.getClass().getClassLoader().getClass().getDeclaredField("allLoaders");

            plugins.setAccessible(true);
            toLoad.setAccessible(true);
            classLoaders.setAccessible(true);

            toLoad.set(this.getProxy().getPluginManager(), new HashMap<>());

            type = (Map<String, Plugin>) plugins.get(this.getProxy().getPluginManager());
            type2 = (Map<String, PluginDescription>) toLoad.get(this.getProxy().getPluginManager());

            type.remove("BungeeUtils");

            BungeeEssentials.class.getClassLoader();

            //TODO: Remove classloader instance from the static field in the classloader.

            File file = new File(this.getFile().getAbsolutePath());

            Constructor yamlConstructor = new Constructor();
            PropertyUtils propertyUtils = yamlConstructor.getPropertyUtils();
            propertyUtils.setSkipMissingProperties(true);
            yamlConstructor.setPropertyUtils(propertyUtils);
            Yaml yaml = new Yaml(yamlConstructor);

            if (file.isFile() && file.getName().endsWith(".jar")) {

                JarFile jar = new JarFile(file);
                JarEntry pdf = jar.getJarEntry("bungee.yml");

                if (pdf == null) {

                    pdf = jar.getJarEntry("plugin.yml");
                }

                Preconditions.checkNotNull(pdf, "Plugin must have a plugin.yml or bungee.yml");

                InputStream in = jar.getInputStream(pdf);
                PluginDescription desc = yaml.loadAs(in, PluginDescription.class);
                desc.setFile(file);

                type2.put(desc.getName(), desc);
            }

            this.getProxy().getPluginManager().loadPlugins();
            this.getProxy().getPluginManager().getPlugin("BungeeEssentials").onEnable();

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        } catch (IOException e) {

            instance.getProxy().getLogger().log(Level.WARNING, "Could not load BungeeUtils from file!");
        }
    }

    public PlayerManager getPlayerManager() {

        return this.playerManager;
    }

    public ConfigManager getConfigs() {

        return configManager;
    }

    public IPManager getIPManager() {

        return this.ipManager;
    }

    public StaffChatManager getStaffChatManager() {

        return staffChatManager;
    }

    public PrivateChatManager getPrivateChatManager() {

        return privateChatManager;
    }

    public ServerChatManager getServerChatManager() {

        return serverChatManager;
    }

    public ServerPingManager getServerPingManager() {

        return serverPingManager;
    }

    public CommandManager getCommandManager() {

        return commandManager;
    }

    public SessionServerManager getSessionServerManager() {

        return sessionServerManager;
    }
}
