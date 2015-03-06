package pro.zackpollard.bungeeutil.managers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChatManager implements Listener {

    private final BungeeEssentials instance;
    private final Set<UUID> chatEnabled;

    public StaffChatManager(BungeeEssentials instance) {

        this.instance = instance;
        this.chatEnabled = new HashSet<>();
        instance.getProxy().getPluginManager().registerListener(instance, this);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {

        chatEnabled.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {

        if(!event.isCommand()) {

            if (event.getSender() instanceof ProxiedPlayer) {

                ProxiedPlayer player = (ProxiedPlayer) event.getSender();

                if (chatEnabled.contains(player.getUniqueId())) {

                    event.setCancelled(true);

                    sendMessageToStaff(player, event.getMessage());
                }
            }
        }
    }

    public void sendMessageToStaff(ProxiedPlayer from, String message) {

        message = ChatColor.translateAlternateColorCodes('&', message);

        for(ProxiedPlayer proxiedPlayer : instance.getProxy().getPlayers()) {

            if(chatEnabled.contains(proxiedPlayer.getUniqueId())) {

                proxiedPlayer.sendMessage(instance.getConfigs().getMessages().getStaffChatFormat(from.getName(), message));
            }
        }
    }

    public void enableChat(UUID uuid) {

        chatEnabled.add(uuid);
    }

    public void disableChat(UUID uuid) {

        chatEnabled.remove(uuid);
    }

    public boolean toggleChat(UUID uuid) {

        if(chatEnabled.contains(uuid)) {

            disableChat(uuid);
            return false;
        } else {

            enableChat(uuid);
            return true;
        }
    }
}
