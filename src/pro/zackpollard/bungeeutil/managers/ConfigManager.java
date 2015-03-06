package pro.zackpollard.bungeeutil.managers;

import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONConfig;
import pro.zackpollard.bungeeutil.json.config.GSONMessages;

public class ConfigManager {

    private final BungeeEssentials instance;
    private final MessagesManager messagesManager;
    private final RolesManager rolesManager;
    private final MainConfigManager mainConfigManager;

    public ConfigManager(BungeeEssentials instance) {

        this.instance = instance;
        this.messagesManager = new MessagesManager(instance);
        this.rolesManager = new RolesManager(instance);
        this.mainConfigManager = new MainConfigManager(instance);
    }

    public GSONMessages getMessages() {

        return messagesManager.getMessages();
    }

    public RolesManager getRoles() {

        return rolesManager;
    }

    public GSONConfig getMainConfig() {

        return mainConfigManager.getConfig();
    }

    public void saveConfigs() {

        messagesManager.saveConfig();
        rolesManager.saveConfig();
        mainConfigManager.saveConfig();
    }
}