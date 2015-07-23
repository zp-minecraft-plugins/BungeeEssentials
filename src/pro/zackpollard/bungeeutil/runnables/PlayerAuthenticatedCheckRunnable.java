package pro.zackpollard.bungeeutil.runnables;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @author Zack Pollard
 */
public class PlayerAuthenticatedCheckRunnable implements Runnable {

    private final GSONPlayer gsonPlayer;
    private final ProxiedPlayer proxiedPlayer;

    public PlayerAuthenticatedCheckRunnable(GSONPlayer gsonPlayer, ProxiedPlayer proxiedPlayer) {

        this.gsonPlayer = gsonPlayer;
        this.proxiedPlayer = proxiedPlayer;
    }

    public void run() {

        if(!gsonPlayer.isAuthenticated()) {

            proxiedPlayer.disconnect(BungeeEssentials.getInstance().getConfigs().getMessages().generateMessage(false, ChatColor.RED + "You did not authenticate within the allocated window and have been disconnected."));
        }
    }
}
