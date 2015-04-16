package pro.zackpollard.bungeeutil.json.storage;

import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class GSONPlayer {

    private transient boolean fileChanged = false;
    private transient long playerJoinTime = 0;
    private transient long lastAccessed = 0;

    /**
     * The uuid of the player in question.
     */
    private UUID uuid;

    /**
     * The bans that this player has accumulated.
     */
    private final List<GSONBan> bans = new ArrayList<>();

    /**
     * The last known ban that the player knew was in action for the player.
     * This ban could be expired and so should be checked whenever it is being
     * checked if the player is banned and changed if the player is no longer
     * supposed to be banned.
     */
    private GSONBan currentBan;
    private GSONMute currentMute;

    /**
     * The last known IP of the player which can be used to get the GSONIPAddress
     * object which can be used in turn to get the other accounts that have used
     * this IP address.
     */
    private String lastKnownIP;
    private final List<String> knownIPs = new ArrayList<>();
    private long totalOnlineTime;
    private long lastOnlineTime;
    private String lastKnownName;
    private String lastConnectedServer;

    public UUID getUUID() {

        return this.uuid;
    }

    public List<GSONBan> getBans() {

        List<GSONBan> listCopy = new ArrayList<>();
        listCopy.addAll(this.bans);
        return listCopy;
    }

    public String getLastKnownIP() {
        return this.lastKnownIP;
    }

    public void setUUID(UUID uuid) {

        this.uuid = uuid;
        this.fileChanged = true;
    }

    public void setCurrentBan(GSONBan currentBan) {

        this.currentBan = currentBan;

        if(currentBan != null) {

            this.bans.add(currentBan);
        }

        this.fileChanged = true;
    }

    public void setCurrentMute(GSONMute currentMute) {

        this.currentMute = currentMute;
        this.fileChanged = true;
    }

    public void setLastKnownIP(String lastKnownIP) {

        if(this.lastKnownIP.equals(lastKnownIP)) {

            this.lastKnownIP = lastKnownIP;
        }

        if(!this.knownIPs.contains(lastKnownIP)) {

            this.knownIPs.add(lastKnownIP);
        }

        this.fileChanged = true;
    }

    public boolean isFileChanged() {

        return this.fileChanged ||
                this.currentBan != null && this.currentBan.isFileChanged() ||
                this.currentMute != null && this.currentMute.isFileChanged();

    }

    public long getPlayerJoinTime() {
        return this.playerJoinTime;
    }

    public void setPlayerJoinTime(long playerJoinTime) {

        this.playerJoinTime = playerJoinTime;
    }

    public List<String> getKnownIPs() {

        List<String> listCopy = new ArrayList<>();
        listCopy.addAll(this.knownIPs);
        return listCopy;
    }

    public long getTotalOnlineTime() {
        return this.totalOnlineTime;
    }

    public String getLastOnlineString() {

        if(BungeeEssentials.getInstance().getProxy().getPlayer(this.uuid) != null) {

            return "Online now!";
        } else {

            SimpleDateFormat format = new SimpleDateFormat(BungeeEssentials.getInstance().getConfigs().getMessages().getTimeStampFormat());
            Date date = new Date(this.lastOnlineTime);
            return format.format(date);
        }
    }

    public String getTotalOnlineTimeFormatted() {

        long amountTimeSeconds = this.getTotalOnlineTime();

        long playTime = 0;

        if(playerJoinTime != 0) {

             playTime = System.currentTimeMillis() - playerJoinTime;
        }

        String message = BungeeEssentials.getInstance().getConfigs().getMessages().getTotalOnlineTimeFormat();

        return Utils.parseStringTimeInfo(message, amountTimeSeconds + playTime);
    }

    public void setTotalOnlineTime(long totalOnlineTime) {
        this.totalOnlineTime = totalOnlineTime;
        this.fileChanged = true;
    }

    public void setLastOnlineTime(long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
        this.fileChanged = true;
    }

    public String getLastKnownName() {
        return this.lastKnownName;
    }

    public void setLastKnownName(String lastKnownName) {
        this.lastKnownName = lastKnownName;
        this.fileChanged = true;
    }

    public GSONBan getCurrentBan() {
        return this.currentBan;
    }

    public String getLastConnectedServer() {
        return this.lastConnectedServer;
    }

    public void setLastConnectedServer(String lastConnectedServer) {
        this.lastConnectedServer = lastConnectedServer;
        this.fileChanged = true;
    }

    public GSONMute getCurrentMute() {
        return currentMute;
    }

    public void accessed() {

        lastAccessed = System.currentTimeMillis();
    }

    public long getLastAccessed() {

        return lastAccessed;
    }

    public boolean compareLastAccessedWithNow() {

        return compareLastAccessedWithNow(true);
    }

    public boolean compareLastAccessedWithNow(boolean isBeforeNow) {

        if(lastAccessed == System.currentTimeMillis()) {

            return isBeforeNow;
        } else {

            return !isBeforeNow;
        }
    }
}