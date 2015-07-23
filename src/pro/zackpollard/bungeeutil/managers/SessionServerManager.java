package pro.zackpollard.bungeeutil.managers;

import com.google.gson.Gson;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;
import pro.zackpollard.bungeeutil.utils.Utils;

import java.util.concurrent.TimeUnit;

/**
 * @author Zack Pollard
 */
public class SessionServerManager implements Listener {

    private final BungeeEssentials instance;

    private boolean sessionsOnline = true;

    public SessionServerManager(BungeeEssentials instance) {

        this.instance = instance;
        instance.getProxy().getScheduler().schedule(instance, new SessionServerChecker(this), 10, 10, TimeUnit.SECONDS);
        instance.getProxy().getPluginManager().registerListener(instance, this);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {

        GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());

        if(!gsonPlayer.isAuthenticated()) {

            event.getPlayer().disconnect(instance.getConfigs().getMessages().generateMessage(false, ChatColor.RED + "You cannot switch servers as you are not authenticated with the server. \n Re-connect and type /login (password) to authenticate."));
        }
    }

    public boolean isSessionsOnline() {

        return sessionsOnline;
    }

    private void setSessionsOnline(boolean sessionsOnline) {

        this.sessionsOnline = sessionsOnline;
        instance.getProxy().broadcast(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Mojang Session servers just went offline. If you haven't setup a password you should do so now with /register (password) so you can login even while they are down!"));
    }

    private class SessionServerChecker implements Runnable {

        private final SessionServerManager sessionServerManager;

        public SessionServerChecker(SessionServerManager sessionServerManager) {

            this.sessionServerManager = sessionServerManager;
        }

        @Override
        public void run() {

            ServerStatus serverStatus;

            String json = Utils.readUrl("http://xpaw.ru/mcstatus/status.json");

            if (json != null) {

                Gson gson = new Gson();
                serverStatus = gson.fromJson(json, ServerStatus.class);

                if (serverStatus != null) {

                    if (serverStatus.session.status.equals("up")) {

                        sessionServerManager.setSessionsOnline(true);
                    } else {
                        sessionServerManager.setSessionsOnline(false);
                    }
                }
            }
        }
    }

    private class ServerStatus {

        private SessionServerStatus session;
    }

    private class SessionServerStatus {

        private String status;
    }
}
