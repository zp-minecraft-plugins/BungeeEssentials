package pro.zackpollard.bungeeutil.json.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class GSONConfig {

    private final GSONProxyPing proxyPing;
    private final GSONPermissions permissions;
    private final GSONChat chat;
    private final Map<String, Integer> blockedCommands;
    private final Set<String> blockedCommandsWhenMuted;

    private final int configVersionDoNotChange;
    private final transient int currentConfigVersion = 1;

    private transient boolean blockedCommandsConverted;
    private transient boolean blockedCommandsMutedConverted;

    public GSONConfig() {

        proxyPing = new GSONProxyPing();

        permissions = new GSONPermissions();

        chat = new GSONChat();

        blockedCommands = new HashMap<>();

        blockedCommands.put("examplecommand1", 1);
        blockedCommands.put("you can block subcommands too", 1);
        blockedCommands.put("this will be unblocked for role level 3 and above", 3);
        blockedCommands.put("this will be blocked for all users regardless of role", -1);

        blockedCommandsWhenMuted = new HashSet<>();
        blockedCommandsWhenMuted.add("me");
        blockedCommandsWhenMuted.add("you can block subcommands here too");

        configVersionDoNotChange = 1;
    }

    public GSONPermissions getPermissions() {

        return permissions;
    }

    public Map<String, Integer> getBlockedCommands() {

        if (blockedCommandsConverted) {

            for (Map.Entry<String, Integer> entry : new HashMap<>(blockedCommands).entrySet()) {

                blockedCommands.remove(entry.getKey());
                blockedCommands.put(entry.getKey().toLowerCase(), entry.getValue());
            }

            blockedCommandsConverted = true;
        }

        return blockedCommands;
    }

    public Set<String> getBlockedCommandsWhenMuted() {

        if (blockedCommandsMutedConverted) {

            for (String string : new HashSet<>(blockedCommandsWhenMuted)) {

                blockedCommandsWhenMuted.remove(string);
                blockedCommandsWhenMuted.add(string.toLowerCase());
            }

            blockedCommandsMutedConverted = true;
        }

        return blockedCommandsWhenMuted;
    }

    public GSONProxyPing getProxyPing() {

        return proxyPing;
    }

    public GSONChat getChat() {

        return chat;
    }

    public int getConfigVersionDoNotChange() {
        return configVersionDoNotChange;
    }

    public int getCurrentConfigVersion() {
        return currentConfigVersion;
    }
}