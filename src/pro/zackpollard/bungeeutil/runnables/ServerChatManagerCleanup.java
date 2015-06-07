package pro.zackpollard.bungeeutil.runnables;

import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.managers.ServerChatManager;

import java.util.Map;

public class ServerChatManagerCleanup implements Runnable {

    private final BungeeEssentials instance;
    private final ServerChatManager serverChatManager;

    public ServerChatManagerCleanup(BungeeEssentials instance, ServerChatManager serverChatManager) {

        this.instance = instance;
        this.serverChatManager = serverChatManager;
    }

    public void run() {

        Map<String, Map<String, Integer>> cooldowns = serverChatManager.getSlowChatCooldowns();

        for (String server : cooldowns.keySet()) {

            Map<String, Integer> playerCooldowns = cooldowns.get(server);

            for (String player : playerCooldowns.keySet()) {

                int time = playerCooldowns.get(player) - 1;

                if (time <= 0) {

                    playerCooldowns.remove(player);
                } else {

                    playerCooldowns.put(player, time);
                }
            }
        }
    }
}
