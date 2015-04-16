package pro.zackpollard.bungeeutil.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONMessages;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONIPAddress;
import pro.zackpollard.bungeeutil.runnables.IPManagerCleanup;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class IPManager implements Listener {

    private final BungeeEssentials instance;
    private final File dataFolder;
    private final GSONMessages messages;
    private final PlayerManager playerManager;

    public IPManager(BungeeEssentials instance) {

        this.instance = instance;
        this.messages = instance.getConfigs().getMessages();
        this.dataFolder = new File(instance.getDataFolder().getAbsolutePath() + File.separator + "ips");
        this.dataFolder.mkdirs();
        this.playerManager = instance.getPlayerManager();

        instance.getProxy().getScheduler().schedule(instance, new IPManagerCleanup(instance, this), 15, 15, TimeUnit.SECONDS);
        instance.getProxy().getPluginManager().registerListener(instance, this);

        for(ProxiedPlayer player : instance.getProxy().getPlayers()) {

            onPlayerLogin(player);
        }
    }

    /**
     * A map that will be used to store a GSONIPAddress object while the player is online.
     * The map will be cleared at pre-defined intervals or when a player goes offline.
     * The map will also have a boolean value which will be changed when the GSONIPAddress
     * has been changed by another class. This will be a simple way of checking as to
     * whether the GSONIPAddress needs to be saved to disk when unloaded from the cache.
     */

    private final Map<String, GSONIPAddress> ipCache = new HashMap<>();

    /**
     *
     * @param event
     *
     */

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {

        GSONIPAddress gsonIP = this.getIP(event.getConnection().getAddress().getAddress().getHostAddress());

        if(gsonIP != null) {

            BaseComponent[] banned = this.checkIPBannedGetMessage(gsonIP);

            if(banned != null) {

                event.setCancelled(true);
                event.setCancelReason(BaseComponent.toLegacyText(banned));
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PostLoginEvent event) {

        onPlayerLogin(event.getPlayer());
    }

    public void onPlayerLogin(ProxiedPlayer player) {

        GSONIPAddress gsonIP = this.loadIPCreateIfNotExist(player.getAddress().getAddress().getHostAddress());

        gsonIP.setLastUUIDJoined(player.getUniqueId());
    }

    public BaseComponent[] checkIPBannedGetMessage(GSONIPAddress gsonIP) {

        GSONBan gsonBan = gsonIP.getCurrentBan();

        if(gsonBan != null) {

            long banExpiration = gsonBan.getDuration() + gsonBan.getTimestamp();

            if (gsonBan.getDuration() == 0) {

                return this.messages.getIPPermBanMessage(
                        playerManager.getPlayer(gsonBan.getBannerUUID()).getLastKnownName(),
                        gsonBan.getTimestampFormatted(),
                        gsonBan.getReason());
            } else if (banExpiration > System.currentTimeMillis()) {

                return this.messages.getIPTempBanMessage(
                        playerManager.getPlayer(gsonBan.getBannerUUID()).getLastKnownName(),
                        gsonBan.getTimestampFormatted(),
                        gsonBan.getReason(),
                        gsonBan.getRemainingTimeFormatted());
            } else {

                gsonIP.setCurrentBan(null);
            }
        }

        return null;
    }


    public void checkIPBanned(ProxiedPlayer player, GSONIPAddress gsonIP) {

        BaseComponent[] message = checkIPBannedGetMessage(gsonIP);

        if(message != null) {

            player.disconnect(message);
        }
    }

    private GSONIPAddress loadIPAddress(String ipAddress) {

        GSONIPAddress gsonIP = this.ipCache.get(ipAddress);

        if(gsonIP != null) {

            return gsonIP;
        }

        File ipFile = new File(dataFolder.getAbsolutePath() + File.separator + ipAddress + ".json");

        if(ipFile.exists()) {

            if(ipFile.length() == 0) {

                gsonIP = this.createIP(ipAddress);
            }

            try (Reader reader = new InputStreamReader(new FileInputStream(ipFile), "UTF-8")) {

                Gson gson = new GsonBuilder().create();
                gsonIP = gson.fromJson(reader, GSONIPAddress.class);

            } catch (IOException e) {

                e.printStackTrace();
            }

            this.cacheIP(gsonIP);
        } else {

            return null;
        }

        return gsonIP;
    }

    private GSONIPAddress loadIPCreateIfNotExist(String ipAddress) {

        GSONIPAddress gsonIP = this.ipCache.get(ipAddress);

        if(gsonIP != null) {

            return gsonIP;
        }

        File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + ipAddress + ".json");

        if(playerFile.exists()) {

            if(playerFile.length() == 0) {

                playerFile.delete();
                System.out.println("Had to delete a file as the ip file was empty which is wrong.");
                gsonIP = this.createIP(ipAddress);
            } else {

                gsonIP = this.getIP(ipAddress);
            }
        } else {

            gsonIP = this.createIP(ipAddress);
        }

        return gsonIP;
    }

    private GSONIPAddress createIP(String ipAddress) {

        File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + ipAddress + ".json");

        try {
            playerFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().severe("Plugin was unable to create a new file! Check the directories read/write permissions and then report to the developer!");
        }

        GSONIPAddress gsonIP = new GSONIPAddress();
        gsonIP.setIP(ipAddress);

        this.saveIP(gsonIP, playerFile);

        this.cacheIP(gsonIP);

        return gsonIP;
    }

    private void cacheIP(GSONIPAddress gsonIP) {

        if(gsonIP != null) {

            this.ipCache.put(gsonIP.getIP(), gsonIP);
        } else {

            instance.getLogger().severe("GSONIPAddress wasn't loaded correctly and so was null! Check the directories read/write permissions and then report to the developer!");
        }
    }

    public GSONIPAddress getIP(String ipAddress) {

        return this.loadIPAddress(ipAddress);
    }

    public boolean unloadIP(String ipAddress) {

        GSONIPAddress gsonIP = this.ipCache.get(ipAddress);

        return gsonIP != null && this.unloadIP(gsonIP);
    }

    private boolean saveIP(GSONIPAddress gsonIP, File playerFile) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(gsonIP);

        FileOutputStream outputStream;

        try {

            outputStream = new FileOutputStream(playerFile);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            instance.getLogger().severe("GSONIPAddress could not be saved for " + gsonIP.getIP() + " as the file couldn't be found on the storage device. Please check the directories read/write permissions and contact the developer!");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().severe("GSONIPAddress could not be written to for " + gsonIP.getIP() + " as an error occurred. Please check the directories read/write permissions and contact the developer!");
            return false;
        }

        return true;
    }

    public boolean unloadIP(GSONIPAddress gsonIP) {

        if(gsonIP.isFileChanged()) {

            if(gsonIP.compareLastAccessedWithNow()) {

                File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + gsonIP.getIP() + ".json");

                this.saveIP(gsonIP, playerFile);
            }

            this.ipCache.remove(gsonIP.getIP());
            return true;
        }

        return false;
    }

    public Map<String, GSONIPAddress> getIPCache(boolean copy) {

        if(copy) {

            Map<String, GSONIPAddress> mapCopy = new HashMap<>();
            mapCopy.putAll(this.ipCache);

            return mapCopy;
        } else {

            return this.ipCache;
        }
    }
}