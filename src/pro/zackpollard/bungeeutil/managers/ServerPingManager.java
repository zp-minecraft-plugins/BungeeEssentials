package pro.zackpollard.bungeeutil.managers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONProxyPing;

public class ServerPingManager implements Listener {

    private final BungeeEssentials instance;

    public ServerPingManager(BungeeEssentials instance) {

        this.instance = instance;
        instance.getProxy().getPluginManager().registerListener(instance, this);
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {

        GSONProxyPing pingConfig = instance.getConfigs().getMainConfig().getProxyPing();

        if (pingConfig.isEditPingResponse()) {

            ServerPing ping = event.getResponse();
            ping.setDescription(ChatColor.translateAlternateColorCodes('%', pingConfig.getMotd()));
            ServerPing.Players players = ping.getPlayers();
            players.setMax(pingConfig.getMaxPlayers());
            event.setResponse(ping);
        }
    }
}