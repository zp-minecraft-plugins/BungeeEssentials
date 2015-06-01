package pro.zackpollard.bungeeutil.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONMessages;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;
import pro.zackpollard.bungeeutil.runnables.PlayerManagerCleanup;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PlayerManager implements Listener {

    private final BungeeEssentials instance;
    private final File dataFolder;
    private final GSONMessages messages;

    private final Map<String, UUID> playerNameCache;

    /**
     * A map that will be used to store the UUID of each GSONPlayer and the GSONPlayer object so that the GSONPlayer
     * can be located much more efficiently as otherwise it would require looking
     * through every GSONPlayer and getting the UUID of the GSONPlayer until there was a match.
     */
    private final Map<UUID, GSONPlayer> gsonPlayerCache;

    public PlayerManager(BungeeEssentials instance) {

        playerNameCache = new ConcurrentHashMap<>();
        gsonPlayerCache = new ConcurrentHashMap<>();

        this.instance = instance;
        this.messages = instance.getConfigs().getMessages();
        this.dataFolder = new File(instance.getDataFolder().getAbsolutePath() + File.separator + "players");
        this.dataFolder.mkdirs();
        instance.getProxy().getPluginManager().registerListener(instance, this);
        instance.getProxy().getScheduler().schedule(instance, new PlayerManagerCleanup(instance, this), 15, 15, TimeUnit.SECONDS);

        this.populatePlayerNameCache();

        for(ProxiedPlayer player : instance.getProxy().getPlayers()) {

            onPlayerLogin(player);
        }
    }

    private void populatePlayerNameCache() {

        Gson gson = new GsonBuilder().create();

        System.out.println(instance.getConfigs().getMessages().getPrefix() + "Loading playername -> uuid cache!");

        for(File playerFile : dataFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {return name.endsWith(".json");}
        })) {

            try(Reader reader = new InputStreamReader(new FileInputStream(playerFile), "UTF-8")) {

                GSONPlayer gsonPlayer = gson.fromJson(reader, GSONPlayer.class);

                if(gsonPlayer != null) {

                    if(gsonPlayer.getLastKnownName() != null) {

                        playerNameCache.put(gsonPlayer.getLastKnownName().toLowerCase(), gsonPlayer.getUUID());
                    } else {

                        System.out.println("Last known name was null for player with UUID: " + gsonPlayer.getUUID());
                        playerFile.delete();
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        System.out.println(instance.getConfigs().getMessages().getPrefix() + "Loaded " + playerNameCache.size() + " players!");
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {

        GSONPlayer gsonPlayer = this.getPlayer(event.getConnection().getUniqueId());

        if(gsonPlayer != null) {

            BaseComponent[] banned = this.checkPlayerBannedGetMessage(gsonPlayer);

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

        GSONPlayer gsonPlayer = this.loadPlayerCreateIfNotExist(player);

        if(gsonPlayer.getLastKnownName() != null && playerNameCache.containsKey(gsonPlayer.getLastKnownName().toLowerCase())) {

            playerNameCache.remove(gsonPlayer.getLastKnownName().toLowerCase());
        }

        playerNameCache.put(player.getName().toLowerCase(), player.getUniqueId());

        gsonPlayer.setLastKnownName(player.getName());
        gsonPlayer.setPlayerJoinTime(System.currentTimeMillis());
        gsonPlayer.setLastKnownIP(player.getPendingConnection().getAddress().getAddress().getHostAddress());

        this.savePlayer(gsonPlayer);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();
        GSONPlayer gsonPlayer = this.getPlayer(uuid);

        long joinTime = gsonPlayer.getPlayerJoinTime();
        long playTime = System.currentTimeMillis() - joinTime;
        long totalPlayTime = gsonPlayer.getTotalOnlineTime() + playTime;

        gsonPlayer.setTotalOnlineTime(totalPlayTime);
        gsonPlayer.setLastOnlineTime(System.currentTimeMillis());
    }

    @EventHandler
    public void onServerConnect(ServerConnectedEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();
        GSONPlayer gsonPlayer = this.getPlayer(uuid);

        gsonPlayer.setLastConnectedServer(event.getServer().getInfo().getName());
    }

    public BaseComponent[] checkPlayerBannedGetMessage(GSONPlayer gsonPlayer) {

        GSONBan gsonBan = gsonPlayer.getCurrentBan();

        if(gsonBan != null) {

            long banExpiration = gsonBan.getDuration() + gsonBan.getTimestamp();

            if(gsonBan.getDuration() == 0) {

                return this.messages.getPlayerPermBanMessage(
                        getPlayer(gsonBan.getBannerUUID()).getLastKnownName(),
                        gsonBan.getTimestampFormatted(),
                        gsonBan.getReason());
            } else if(banExpiration > System.currentTimeMillis()) {

                return this.messages.getPlayerTempBanMessage(
                        getPlayer(gsonBan.getBannerUUID()).getLastKnownName(),
                        gsonBan.getTimestampFormatted(),
                        gsonBan.getReason(),
                        gsonBan.getRemainingTimeFormatted());
            } else {

                gsonPlayer.setCurrentBan(null);
            }
        }

        return null;
    }

    public void checkPlayerBanned(ProxiedPlayer player, GSONPlayer gsonPlayer) {

        BaseComponent[] message = checkPlayerBannedGetMessage(gsonPlayer);

        if(message != null) {

            player.disconnect(message);
        }
    }

    private GSONPlayer loadPlayer(UUID uuid) {

        GSONPlayer gsonPlayer = this.gsonPlayerCache.get(uuid);

        if(gsonPlayer == null) {

            File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + uuid.toString() + ".json");

            if (playerFile.exists()) {

                try (Reader reader = new InputStreamReader(new FileInputStream(playerFile), "UTF-8")) {

                    Gson gson = new GsonBuilder().create();
                    gsonPlayer = gson.fromJson(reader, GSONPlayer.class);

                } catch (IOException e) {

                    e.printStackTrace();
                }

                this.cachePlayer(gsonPlayer);
            }
        }

        if(gsonPlayer != null) {

            gsonPlayer.accessed();
        }

        return gsonPlayer;
    }

    private GSONPlayer loadPlayerCreateIfNotExist(ProxiedPlayer player) {

        UUID uuid = player.getUniqueId();
        GSONPlayer gsonPlayer = this.gsonPlayerCache.get(uuid);

        if(gsonPlayer == null) {

            File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + uuid.toString() + ".json");

            if (playerFile.exists()) {

                if(playerFile.length() == 0) {

                    playerFile.delete();
                    System.out.println("Had to delete a file as the player file was empty which is wrong.");
                    gsonPlayer = this.createPlayer(player);
                } else {

                    gsonPlayer = this.getPlayer(uuid);
                }
            } else {

                gsonPlayer = this.createPlayer(player);
            }
        }

        if(gsonPlayer != null) {

            gsonPlayer.accessed();
        } else {

            System.out.println("Player file was still null after it should have been loaded or created!");
        }

        return gsonPlayer;
    }

    private GSONPlayer createPlayer(ProxiedPlayer player) {

        File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + player.getUniqueId().toString() + ".json");

        try {

            playerFile.createNewFile();
        } catch (IOException e) {

            e.printStackTrace();
            instance.getLogger().severe("Plugin was unable to create a new file! Check the directories read/write permissions and then report to the developer!");
            return null;
        }

        GSONPlayer gsonPlayer = new GSONPlayer();
        gsonPlayer.setUUID(player.getUniqueId());
        gsonPlayer.setLastKnownIP(player.getAddress().getAddress().getHostAddress());

        if(!this.savePlayer(gsonPlayer, playerFile)) {

            return null;
        }

        this.cachePlayer(gsonPlayer);

        return gsonPlayer;
    }

    private boolean savePlayer(GSONPlayer gsonPlayer, File playerFile) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(gsonPlayer);

        FileOutputStream outputStream;

        try {

            outputStream = new FileOutputStream(playerFile);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            instance.getLogger().severe("GSONPlayer could not be saved for " + gsonPlayer.getUUID() + " as the file couldn't be found on the storage device. Please check the directories read/write permissions and contact the developer!");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().severe("GSONPlayer could not be written to for " + gsonPlayer.getUUID() + " as an error occurred. Please check the directories read/write permissions and contact the developer!");
            return false;
        }

        return true;
    }

    private boolean savePlayer(GSONPlayer gsonPlayer) {

        return this.savePlayer(gsonPlayer, new File(dataFolder.getAbsolutePath() + File.separator + gsonPlayer.getUUID().toString() + ".json"));
    }

    private void cachePlayer(GSONPlayer gsonPlayer) {

        if(gsonPlayer != null) {

            this.gsonPlayerCache.put(gsonPlayer.getUUID(), gsonPlayer);
        } else {

            instance.getLogger().severe("GSONPlayer wasn't loaded correctly and so was null! Check the directories read/write permissions and then report to the developer!");
        }
    }

    public GSONPlayer getPlayer(String name) {

        UUID uuid = this.playerNameCache.get(name.toLowerCase());

        if(uuid != null) {

            return this.getPlayer(uuid);
        }

        return null;
    }

    public GSONPlayer getPlayerBestGuess(String partName) {

        GSONPlayer gsonPlayer;

        partName = partName.toLowerCase();

        gsonPlayer = this.getPlayer(partName);

        if(gsonPlayer == null) {

            for (GSONPlayer player : this.gsonPlayerCache.values()) {

                if (player.getLastKnownName().toLowerCase().startsWith(partName)) {

                    gsonPlayer = instance.getPlayerManager().getPlayer(player.getUUID());
                    break;
                }
            }
        }

        if(gsonPlayer == null) {

            for(String playerName : this.playerNameCache.keySet()) {

                if(playerName.toLowerCase().startsWith(partName)) {

                    UUID uuid = this.playerNameCache.get(playerName);
                    gsonPlayer = this.getPlayer(uuid);
                }
            }
        }

        return gsonPlayer;
    }

    public GSONPlayer getPlayer(UUID uuid) {

        return this.loadPlayer(uuid);
    }

    public boolean unloadPlayer(UUID uuid) {

        GSONPlayer gsonPlayer = this.gsonPlayerCache.get(uuid);

        return gsonPlayer != null && this.unloadPlayer(gsonPlayer);
    }

    public boolean unloadPlayer(GSONPlayer gsonPlayer) {

        if(gsonPlayer.isFileChanged()) {

            if(gsonPlayer.compareLastAccessedWithNow()) {

                File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + gsonPlayer.getUUID().toString() + ".json");

                if(!this.savePlayer(gsonPlayer, playerFile)) {

                    return false;
                }
            }
        }

        this.gsonPlayerCache.remove(gsonPlayer.getUUID());
        return true;
    }

    public Map<UUID, GSONPlayer> getGsonPlayerCache(boolean copy) {

        if(copy) {

            Map<UUID, GSONPlayer> mapCopy = new HashMap<>();
            mapCopy.putAll(this.gsonPlayerCache);

            return mapCopy;
        } else {

            return this.gsonPlayerCache;
        }
    }
}