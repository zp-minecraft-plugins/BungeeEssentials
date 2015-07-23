package pro.zackpollard.bungeeutil.managers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author Zack Pollard
 */
public class SessionServerManager implements Listener {

    private final BungeeEssentials instance;

    private boolean sessionsOnline;

    public SessionServerManager(BungeeEssentials instance) {

        this.instance = instance;
        this.sessionsOnline = true;
        instance.getProxy().getScheduler().schedule(instance, new SessionServerChecker(this), 1, 10, TimeUnit.SECONDS);
        instance.getProxy().getPluginManager().registerListener(instance, this);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {

        GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());

        if(!event.getPlayer().getServer().getInfo().getName().equalsIgnoreCase("Hub")) {

            if (!gsonPlayer.isAuthenticated()) {

                event.getPlayer().disconnect(instance.getConfigs().getMessages().generateMessage(false, ChatColor.RED + "You cannot switch servers as you are not authenticated with the server. \n Re-connect and type /login (password) to authenticate."));
            }
        }
    }

    @EventHandler
    public void onPlayerSendCommand(ChatEvent event) {

        if(event.getSender() instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayer(player.getUniqueId());

            if(!gsonPlayer.isAuthenticated() && !event.getMessage().startsWith("/login")) {

                event.setCancelled(true);
                player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot send commands or chat as you are not authenticated with the server."));
            }
        }
    }

    public boolean isSessionsOnline() {

        return sessionsOnline;
    }

    private void setSessionsOnline(boolean sessionsOnline) {

        if(this.sessionsOnline != sessionsOnline) {

            this.sessionsOnline = sessionsOnline;
            if (sessionsOnline) {

                instance.getProxy().broadcast(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Mojang Session servers just came back online!"));
            } else {

                instance.getProxy().broadcast(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Mojang Session servers just went offline. If you haven't setup a password you should do so now with /register (password) so you can login even while they are down!"));
            }
        }
    }

    private class SessionServerChecker implements Runnable {

        private final SessionServerManager sessionServerManager;

        public SessionServerChecker(SessionServerManager sessionServerManager) {

            this.sessionServerManager = sessionServerManager;
        }

        @Override
        public void run() {

            try {

                Socket socket = new Socket();

                socket.connect(new InetSocketAddress("sessionserver.mojang.com", 443), 2500);

                socket.close();

                setSessionsOnline(true);

            } catch (IOException e) {

                setSessionsOnline(false);
            }
        }
    }

    private class ServerReport {

        private ServerStatus report;
    }

    private class ServerStatus {

        private SessionServerStatus session;
    }

    private class SessionServerStatus {

        private String status;
    }
}
