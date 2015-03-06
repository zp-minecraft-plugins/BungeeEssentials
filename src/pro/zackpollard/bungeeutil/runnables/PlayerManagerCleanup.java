package pro.zackpollard.bungeeutil.runnables;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.managers.PlayerManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManagerCleanup implements Runnable {

    private final BungeeEssentials instance;
    private final PlayerManager playerManager;

    public PlayerManagerCleanup(BungeeEssentials instance, PlayerManager playerManager) {

        this.instance = instance;
        this.playerManager = playerManager;
    }

    @Override
    public void run() {
        Set<UUID> onlinePlayers = new HashSet<>();

        for(ProxiedPlayer player : instance.getProxy().getPlayers()) {

            onlinePlayers.add(player.getUniqueId());
        }

        for(UUID uuid : playerManager.getGsonPlayerCache(true).keySet()) {

            if(!onlinePlayers.contains(uuid)) {

                playerManager.unloadPlayer(uuid);
            }
        }
    }
}