package pro.zackpollard.bungeeutil.json.config;

public class GSONProxyPing {

    private final boolean editPingResponse;
    private int maxPlayers;
    private final String motd;

    public GSONProxyPing() {

        editPingResponse = true;
        //maxPlayers = BungeeEssentials.getInstance().getProxy().getConfig().getPlayerLimit();
        motd = "%cThis server is running BungeeEssentials\nChange the motd in the config!";
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
}