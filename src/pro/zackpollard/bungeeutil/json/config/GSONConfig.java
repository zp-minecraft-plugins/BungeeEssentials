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
    }

    public GSONPermissions getPermissions() {

        return permissions;
    }

    public Map<String, Integer> getBlockedCommands() {

        return blockedCommands;
    }

    public Set<String> getBlockedCommandsWhenMuted() {

        return blockedCommandsWhenMuted;
    }

    public GSONProxyPing getProxyPing() {

        return proxyPing;
    }

    public GSONChat getChat() {

        return chat;
    }
}