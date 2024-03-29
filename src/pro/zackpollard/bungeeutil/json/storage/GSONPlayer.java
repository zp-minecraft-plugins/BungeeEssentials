package pro.zackpollard.bungeeutil.json.storage;

import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.utils.Utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

public class GSONPlayer extends Lockable {

    private transient boolean fileChanged = false;
    private transient long playerJoinTime = 0;
    private transient boolean authenticated = false;

    /**
     * The uuid of the player in question.
     */
    private UUID uuid;

    /**
     * The bans that this player has accumulated.
     */
    private final List<GSONBan> bans = new LinkedList<>();

    /**
     * The last known ban that the player knew was in action for the player.
     * This ban could be expired and so should be checked whenever it is being
     * checked if the player is banned and changed if the player is no longer
     * supposed to be banned.
     */
    private boolean banned;
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
    private long firstSeenTime;
    private byte[] offlineModePasswordHash;
    private byte[] offlineModePasswordSalt;
    private boolean maintenanceModeBypass;
    private int forumUserID;

    public GSONPlayer() {

        firstSeenTime = System.currentTimeMillis();
    }

    public UUID getUUID() {

        return this.uuid;
    }

    public List<GSONBan> getBans() {

        List<GSONBan> listCopy = new ArrayList<>();
        listCopy.addAll(this.bans);
        return this.bans;
    }

    public String getLastKnownIP() {
        return this.lastKnownIP;
    }

    public void setUUID(UUID uuid) {

        this.uuid = uuid;
        this.fileChanged = true;
    }

    public void setCurrentBan(GSONBan currentBan) {

        this.banned = true;

        if(currentBan != null) {
            this.bans.add(currentBan);
        }

        this.fileChanged = true;
    }

    public void unban() {

        this.banned = false;
        this.fileChanged = true;
    }

    public void setCurrentMute(GSONMute currentMute) {

        this.currentMute = currentMute;
        this.fileChanged = true;
    }

    public void setLastKnownIP(String lastKnownIP) {

        if (!Objects.equals(this.lastKnownIP, lastKnownIP)) {

            this.lastKnownIP = lastKnownIP;
        }

        if (!this.knownIPs.contains(lastKnownIP)) {

            this.knownIPs.add(lastKnownIP);
        }

        this.fileChanged = true;
    }

    public boolean isFileChanged() {

        return this.fileChanged ||
                (this.banned && this.getCurrentBan().isFileChanged()) ||
                (this.currentMute != null && this.currentMute.isFileChanged());
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

        if (BungeeEssentials.getInstance().getProxy().getPlayer(this.uuid) != null) {

            return "Online now!";
        } else {

            SimpleDateFormat format = new SimpleDateFormat(BungeeEssentials.getInstance().getConfigs().getMessages().getTimeStampFormat());
            Date date = new Date(this.lastOnlineTime);
            return format.format(date);
        }
    }

    public String getFirstSeenString() {

        SimpleDateFormat format = new SimpleDateFormat(BungeeEssentials.getInstance().getConfigs().getMessages().getTimeStampFormat());
        Date date = new Date(this.firstSeenTime);
        return format.format(date);
    }

    public String getTotalOnlineTimeFormatted() {

        long amountTimeSeconds = this.getTotalOnlineTime();

        long playTime = 0;

        if (playerJoinTime != 0) {

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

    public boolean hasOfflineModePassword() {

        return this.offlineModePasswordHash != null;
    }

    public void setOfflineModePassword(String offlineModePassword) {

        Random random = new Random();

        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(offlineModePassword.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f;

        byte[] hash = new byte[0];

        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        this.offlineModePasswordHash = hash;
        this.offlineModePasswordSalt = salt;

        this.fileChanged = true;
    }

    public boolean compareOfflinePassword(String providedPassword) {

        if(hasOfflineModePassword()) {

            KeySpec spec = new PBEKeySpec(providedPassword.toCharArray(), this.offlineModePasswordSalt, 65536, 128);
            SecretKeyFactory f = null;

            byte[] hash = new byte[0];

            try {
                f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                hash = f.generateSecret(spec).getEncoded();
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }

            if(Arrays.equals(hash, this.offlineModePasswordHash)) {

                return true;
            }
        }

        return false;
    }

    public void setAuthenticated(boolean authenticated) {

        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {

        return authenticated;
    }

    public GSONBan getCurrentBan() {

        if(this.banned) {

            if (this.bans.size() != 0) {

                return this.bans.get(this.bans.size() - 1);
            }
        }
        return null;
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

    public boolean isMaintenanceModeBypass() {

        return maintenanceModeBypass;
    }

    public boolean toggleMaintenanceModeBypass() {

        this.fileChanged = true;
        return maintenanceModeBypass = !maintenanceModeBypass;
    }

    public int getForumUserID() {

        return forumUserID;
    }

    public void setForumUserID(int forumUserID) {

        this.fileChanged = true;
        this.forumUserID = forumUserID;
    }
}