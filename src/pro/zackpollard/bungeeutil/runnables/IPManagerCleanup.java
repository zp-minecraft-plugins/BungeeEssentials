package pro.zackpollard.bungeeutil.runnables;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.managers.IPManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IPManagerCleanup implements Runnable {

    private final BungeeEssentials instance;
    private final IPManager ipManager;

    public IPManagerCleanup(BungeeEssentials instance, IPManager ipManager) {

        this.instance = instance;
        this.ipManager = ipManager;
    }

    @Override
    public void run() {

        Collection<ProxiedPlayer> onlinePlayers = instance.getProxy().getPlayers();
        List<String> ips = new ArrayList<>();

        for (ProxiedPlayer player : onlinePlayers) {

            ips.add(player.getAddress().getAddress().getHostAddress());
        }

        for (String ipAddress : ipManager.getIPCache(true).keySet()) {

            if (!ips.contains(ipAddress)) {

                ipManager.unloadIP(ipAddress);
            }
        }
    }
}