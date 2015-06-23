package pro.zackpollard.bungeeutil.managers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;

import java.util.*;

/**
 * @Author zack
 * @Date 20/02/15.
 */
public class PrivateChatManager implements Listener {

    private final BungeeEssentials instance;

    private final Map<UUID, UUID> replier = new HashMap<>();
    private final Map<UUID, ProxiedPlayer> spies = new WeakHashMap<>();
    private final Set<UUID> disabledMessaging = new HashSet<>();

    public PrivateChatManager(BungeeEssentials instance) {

        this.instance = instance;
        instance.getProxy().getPluginManager().registerListener(instance, this);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();

        replier.remove(uuid);

        if (spies.containsKey(uuid)) {

            spies.put(uuid, null);
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();

        if (spies.containsKey(uuid)) {

            spies.put(uuid, event.getPlayer());
        }
    }

    public void sendMessage(ProxiedPlayer from, ProxiedPlayer to, String message) {

        replier.put(from.getUniqueId(), to.getUniqueId());
        replier.put(to.getUniqueId(), from.getUniqueId());

        message = ChatColor.translateAlternateColorCodes('&', message);

        if (instance.getConfigs().getRoles().getRole(from.getUniqueId()) >=
                instance.getConfigs().getMainConfig().getPermissions().getOverridePermissions().getOverrideDisabledPrivateMessaging() ||
                !disabledMessaging.contains(to.getUniqueId())) {

            to.sendMessage(instance.getConfigs().getMessages().getPrivateChatMessageReceivedFormat(from.getName(), message));
            from.sendMessage(instance.getConfigs().getMessages().getPrivateChatMessageSentFormat(to.getName(), message));

            for (ProxiedPlayer player : spies.values()) {

                if (player != null) {

                    if (player != from || player != to) {

                        player.sendMessage(instance.getConfigs().getMessages().getSocialSpyMessageFormat(from.getName(), to.getName(), message));
                    }
                }
            }
        } else {

            from.sendMessage(instance.getConfigs().getMessages().getPrivateChatMessagingDisabled(to.getName()));
        }
    }

    public UUID getLastRecipient(UUID sender) {

        return replier.get(sender);
    }

    public boolean toggleSpying(ProxiedPlayer spy) {

        if (spies.containsKey(spy.getUniqueId())) {

            spies.remove(spy.getUniqueId());
            return false;
        } else {

            spies.put(spy.getUniqueId(), spy);
            return true;
        }
    }

    public Map<UUID, ProxiedPlayer> getSpies() {

        return spies;
    }

    public boolean isSpy(UUID spy) {

        return spies.containsKey(spy);
    }

    public boolean togglePrivateChat(ProxiedPlayer player) {

        if (disabledMessaging.contains(player.getUniqueId())) {

            disabledMessaging.remove(player.getUniqueId());
            return false;
        } else {

            disabledMessaging.add(player.getUniqueId());
            return true;
        }
    }
}