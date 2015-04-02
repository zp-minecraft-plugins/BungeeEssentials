package pro.zackpollard.bungeeutil.json.storage;

import java.util.*;

public class GSONIPAddress {

    private transient boolean fileChanged = false;
    private transient boolean locked = false;

    private String ip;
    private final Set<UUID> uuids = new HashSet<>();
    private UUID lastOnlineUser;
    /**
     * The bans that this player has accumulated.
     */
    private final Set<GSONBan> bans = new HashSet<>();

    /**
     * The last known ban that the player knew was in action for the player.
     * This ban could be expired and so should be checked whenever it is being
     * checked if the player is banned and changed if the player is no longer
     * supposed to be banned.
     */

    private GSONBan currentBan;

    public String getIP() {
        return this.ip;
    }

    public List<UUID> getUUIDs() {

        List<UUID> copy = new ArrayList<>();
        copy.addAll(uuids);
        return copy;
    }

    public void setIP(String ip) {
        this.ip = ip;
        this.fileChanged = true;
    }

    public void setLastUUIDJoined(UUID uuid) {

        this.uuids.add(uuid);
        this.lastOnlineUser = uuid;
        this.fileChanged = true;
    }

    public boolean isFileChanged() {
        return this.fileChanged;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<GSONBan> getBans() {

        List<GSONBan> listCopy = new ArrayList<>();
        listCopy.addAll(this.bans);
        return listCopy;
    }

    public void setCurrentBan(GSONBan currentBan) {

        this.currentBan = currentBan;

        if(currentBan != null) {

            this.bans.add(currentBan);
        }

        this.fileChanged = true;
    }

    public GSONBan getCurrentBan() {
        return this.currentBan;
    }

    public UUID getLastOnlineUser() {

        return this.lastOnlineUser;
    }
}