package pro.zackpollard.bungeeutil.json.config;

public class GSONProxyPing {

    private final boolean editPingResponse;
    private int maxPlayers;
    private final String motd;
    private String offlineSessionsMotd;

    public GSONProxyPing() {

        editPingResponse = true;
        //maxPlayers = BungeeEssentials.getInstance().getProxy().getConfig().getPlayerLimit();
        motd = "%cThis server is running BungeeEssentials\nChange the motd in the config!";
        offlineSessionsMotd = "%6&lSessions are offline but you can still connect! Join now!";
    }

    public boolean isEditPingResponse() {

        return this.editPingResponse;
    }

    public int getMaxPlayers() {

        return this.maxPlayers;
    }

    public String getMotd() {

        return this.motd;
    }

    public String getOfflineSessionsMotd() {
        return offlineSessionsMotd;
    }
}