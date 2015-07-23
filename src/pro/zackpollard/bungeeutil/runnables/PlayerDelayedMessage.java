package pro.zackpollard.bungeeutil.runnables;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @author Zack Pollard
 */
public class PlayerDelayedMessage implements Runnable {

    private final ProxiedPlayer proxiedPlayer;
    private final BaseComponent[] message;

    public PlayerDelayedMessage(ProxiedPlayer proxiedPlayer, BaseComponent[] message) {

        this.proxiedPlayer = proxiedPlayer;
        this.message = message;
    }

    public void run() {

        proxiedPlayer.sendMessage(message);
    }
}
