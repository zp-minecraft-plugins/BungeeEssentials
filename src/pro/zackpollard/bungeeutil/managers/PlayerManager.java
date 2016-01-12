package pro.zackpollard.bungeeutil.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONMessages;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;
import pro.zackpollard.bungeeutil.runnables.PlayerAuthenticatedCheckRunnable;
import pro.zackpollard.bungeeutil.runnables.PlayerDelayedMessage;
import pro.zackpollard.bungeeutil.runnables.PlayerManagerCleanup;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Collection;
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

        for (ProxiedPlayer player : instance.getProxy().getPlayers()) {

            onPlayerLogin(player);
        }
    }

    private void populatePlayerNameCache() {

        Gson gson = new GsonBuilder().create();

        System.out.println(instance.getConfigs().getMessages().getPrefix() + "Loading playername -> uuid cache!");

        for (File playerFile : dataFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        })) {

            try (Reader reader = new InputStreamReader(new FileInputStream(playerFile), "UTF-8")) {

                GSONPlayer gsonPlayer = gson.fromJson(reader, GSONPlayer.class);

                if (gsonPlayer != null) {

                    if (gsonPlayer.getLastKnownName() != null) {

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
    public void onAsyncPreLoginEvent(PreLoginEvent event) {

        if(!instance.getSessionServerManager().isSessionsOnline()) {

            GSONPlayer gsonPlayer = this.getPlayer(event.getConnection().getName());

            if(gsonPlayer != null && !gsonPlayer.isAuthenticated()) {

                if(instance.getConfigs().getRoles().getRole(gsonPlayer.getUUID()) == 0) {

                    if (gsonPlayer.hasOfflineModePassword()) {

                        event.getConnection().setOnlineMode(false);
                    } else if (gsonPlayer.getLastKnownIP().equals(event.getConnection().getAddress().getAddress().getHostAddress())) {

                        event.getConnection().setOnlineMode(false);
                        gsonPlayer.setAuthenticated(true);
                    } else {

                        event.setCancelled(true);
                        event.setCancelReason(ChatColor.RED + "Sorry, the mojang servers are offline, your IP doesn't match, and you have not setup a password! \nIf you setup a password in-game with /register (password) you will be able to login when the session servers are offline in the future!");
                    }
                } else if(gsonPlayer.hasOfflineModePassword() && gsonPlayer.getKnownIPs().contains(event.getConnection().getAddress().getAddress().getHostAddress())) {

                    event.getConnection().setOnlineMode(false);
                } else {

                    event.setCancelled(true);
                    event.setCancelReason(ChatColor.RED + "Sorry, the mojang servers are offline, your IP doesn't match, and you have not setup a password! \nIf you setup a password in-game with /register (password) you will be able to login when the session servers are offline in the future!");
                }
            } else {

                event.setCancelled(true);
                event.setCancelReason(ChatColor.RED + "Sorry, the mojang servers are offline, your IP doesn't match, and you have not setup a password! \nIf you setup a password in-game with /register (password) you will be able to login when the session servers are offline in the future!");
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {

        GSONPlayer gsonPlayer;

        if(instance.getSessionServerManager().isSessionsOnline()) {

            gsonPlayer = this.getPlayer(event.getConnection().getUniqueId());
        } else {

            gsonPlayer = this.getPlayer(event.getConnection().getName());
        }

        if (gsonPlayer != null) {

            BaseComponent[] banned = this.checkPlayerBannedGetMessage(gsonPlayer);

            if (banned != null) {

                event.setCancelled(true);
                event.setCancelReason(BaseComponent.toLegacyText(banned));
            }

            if(instance.getConfigs().getMainConfig().isMaintenanceMode()) {

                if(instance.getConfigs().getRoles().getRole(gsonPlayer.getUUID()) < instance.getConfigs().getMainConfig().getPermissions().getOverridePermissions().getBypassMaintenanceMode() && !instance.getPlayerManager().getPlayer(gsonPlayer.getUUID()).isMaintenanceModeBypass()) {

                    event.setCancelled(true);
                    event.setCancelReason(BaseComponent.toLegacyText(instance.getConfigs().getMessages().getMaintenanceModeEnabled()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PostLoginEvent event) {

        if(!instance.getSessionServerManager().isSessionsOnline()) {

            setUUIDForPlayer(event.getPlayer());
        }

        onPlayerLogin(event.getPlayer());
    }

    public void onPlayerLogin(ProxiedPlayer player) {

        GSONPlayer gsonPlayer = this.loadPlayerCreateIfNotExist(player);

        if (gsonPlayer.getLastKnownName() != null && playerNameCache.containsKey(gsonPlayer.getLastKnownName().toLowerCase())) {

            playerNameCache.remove(gsonPlayer.getLastKnownName().toLowerCase());
        }

        playerNameCache.put(player.getName().toLowerCase(), player.getUniqueId());

        gsonPlayer.setLastKnownName(player.getName());
        gsonPlayer.setPlayerJoinTime(System.currentTimeMillis());
        gsonPlayer.setLastKnownIP(player.getPendingConnection().getAddress().getAddress().getHostAddress());

        if(player.getPendingConnection().isOnlineMode()) {

            gsonPlayer.setAuthenticated(true);
        } else {

            instance.getProxy().getScheduler().schedule(instance, new PlayerDelayedMessage(player, instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "You have been logged in in offline mode.")), 2, TimeUnit.SECONDS);

            if(gsonPlayer.hasOfflineModePassword()) {

                instance.getProxy().getScheduler().schedule(instance, new PlayerDelayedMessage(player,
                        instance.getConfigs().getMessages().generateMessage(
                                true, ChatColor.YELLOW + "" + ChatColor.BOLD +
                                        "Please authenticate yourself by typing /login (password). You will be disconnected in 30 seconds if you fail to authenticate.")), 2, TimeUnit.SECONDS);
            } else {

                gsonPlayer.setAuthenticated(true);
                instance.getProxy().getScheduler().schedule(instance, new PlayerDelayedMessage(player, instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "It is HIGHLY recommended to set a password for your account using " + ChatColor.BOLD + "/register (password)")), 2, TimeUnit.SECONDS);
                instance.getProxy().getScheduler().schedule(instance, new PlayerDelayedMessage(player, instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "You will only be required to use this password when the minecraft session servers go offline so that you can continue playing regardless!")), 2, TimeUnit.SECONDS);
            }
        }

        if(!gsonPlayer.isAuthenticated()) {

            instance.getProxy().getScheduler().schedule(instance, new PlayerAuthenticatedCheckRunnable(gsonPlayer, player), 30, TimeUnit.SECONDS);
        }

        this.savePlayer(gsonPlayer);
    }

    private void setUUIDForPlayer(ProxiedPlayer player) {

        InitialHandler handler = (InitialHandler) player.getPendingConnection();

        GSONPlayer gsonPlayer = this.getPlayer(player.getName());

        if(gsonPlayer != null) {

            UUID uuid = gsonPlayer.getUUID();

            try {
                Field sf = handler.getClass().getDeclaredField("uniqueId");
                sf.setAccessible(true);
                sf.set(handler, uuid);

                sf = handler.getClass().getDeclaredField("offlineId");
                sf.setAccessible(true);
                sf.set(handler, uuid);

                Collection<String> g = instance.getProxy().getConfigurationAdapter().getGroups(player.getName());
                g.addAll(instance.getProxy().getConfigurationAdapter().getGroups(player.getUniqueId().toString()));

                UserConnection userConnection = (UserConnection) player;

                for (String s : g) {
                    userConnection.addGroups(s);
                }
            } catch (Exception e) {

                player.disconnect(instance.getConfigs().getMessages().generateMessage(false, ChatColor.RED + "Sorry, the mojang servers are offline and we can't authenticate you with our own system!"));

                instance.getLogger().warning("Internal error for " + player.getName() + ", preventing login.");

                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {

        this.onPlayerLeave(event.getPlayer());
    }

    public void onPlayerLeave(ProxiedPlayer player) {

        UUID uuid = player.getUniqueId();
        GSONPlayer gsonPlayer = this.getPlayer(uuid);

        long joinTime = gsonPlayer.getPlayerJoinTime();
        if(joinTime != 0) {

            long playTime = System.currentTimeMillis() - joinTime;
            long totalPlayTime = gsonPlayer.getTotalOnlineTime() + playTime;
            gsonPlayer.setTotalOnlineTime(totalPlayTime);
        }

        gsonPlayer.setLastOnlineTime(System.currentTimeMillis());

        gsonPlayer.setAuthenticated(false);
    }

    @EventHandler
    public void onServerConnect(ServerConnectedEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();
        GSONPlayer gsonPlayer = this.getPlayer(uuid);

        gsonPlayer.setLastConnectedServer(event.getServer().getInfo().getName());
    }

    public BaseComponent[] checkPlayerBannedGetMessage(GSONPlayer gsonPlayer) {

        GSONBan gsonBan = gsonPlayer.getCurrentBan();

        if (gsonBan != null) {

            long banExpiration = gsonBan.getDuration() + gsonBan.getTimestamp();

            if (gsonBan.getDuration() == 0) {

                return this.messages.getPlayerPermBanMessage(
                        getPlayer(gsonBan.getBannerUUID()).getLastKnownName(),
                        gsonBan.getTimestampFormatted(),
                        gsonBan.getReason());
            } else if (banExpiration > System.currentTimeMillis()) {

                return this.messages.getPlayerTempBanMessage(
                        getPlayer(gsonBan.getBannerUUID()).getLastKnownName(),
                        gsonBan.getTimestampFormatted(),
                        gsonBan.getReason(),
                        gsonBan.getRemainingTimeFormatted());
            } else {

                gsonPlayer.unban();
            }
        }

        return null;
    }

    public void checkPlayerBanned(ProxiedPlayer player, GSONPlayer gsonPlayer) {

        BaseComponent[] message = checkPlayerBannedGetMessage(gsonPlayer);

        if (message != null) {

            player.disconnect(message);
        }
    }

    private GSONPlayer loadPlayer(UUID uuid) {

        GSONPlayer gsonPlayer = this.gsonPlayerCache.get(uuid);

        if (gsonPlayer == null) {

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

        if (gsonPlayer != null) {

            gsonPlayer.accessed();
        }

        return gsonPlayer;
    }

    private GSONPlayer loadPlayerCreateIfNotExist(ProxiedPlayer player) {

        UUID uuid = player.getUniqueId();
        GSONPlayer gsonPlayer = this.gsonPlayerCache.get(uuid);

        if (gsonPlayer == null) {

            File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + uuid.toString() + ".json");

            if (playerFile.exists()) {

                if (playerFile.length() == 0) {

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

        if (gsonPlayer != null) {

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

        if (!this.savePlayer(gsonPlayer, playerFile)) {

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

        if (gsonPlayer != null) {

            this.gsonPlayerCache.put(gsonPlayer.getUUID(), gsonPlayer);
        } else {

            instance.getLogger().severe("GSONPlayer wasn't loaded correctly and so was null! Check the directories read/write permissions and then report to the developer!");
        }
    }

    public GSONPlayer getPlayer(String name) {

        UUID uuid = this.playerNameCache.get(name.toLowerCase());

        if (uuid != null) {

            return this.getPlayer(uuid);
        }

        return null;
    }

    public GSONPlayer getPlayerBestGuess(String partName) {

        GSONPlayer gsonPlayer;

        partName = partName.toLowerCase();

        gsonPlayer = this.getPlayer(partName);

        if (gsonPlayer == null) {

            for (GSONPlayer player : this.gsonPlayerCache.values()) {

                if (player.getLastKnownName().toLowerCase().startsWith(partName)) {

                    gsonPlayer = instance.getPlayerManager().getPlayer(player.getUUID());
                    break;
                }
            }
        }

        if (gsonPlayer == null) {

            for (String playerName : this.playerNameCache.keySet()) {

                if (playerName.toLowerCase().startsWith(partName)) {

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

        return this.unloadPlayer(uuid, false);
    }

    public boolean unloadPlayer(UUID uuid, boolean force) {

        GSONPlayer gsonPlayer = this.gsonPlayerCache.get(uuid);

        return gsonPlayer != null && this.unloadPlayer(gsonPlayer, force);
    }

    public boolean unloadPlayer(GSONPlayer gsonPlayer, boolean force) {

        if (gsonPlayer.compareLastAccessedWithNow() || force) {

            if (gsonPlayer.isFileChanged()) {

                File playerFile = new File(dataFolder.getAbsolutePath() + File.separator + gsonPlayer.getUUID().toString() + ".json");

                if (!this.savePlayer(gsonPlayer, playerFile)) {

                    return false;
                }
            }

            this.gsonPlayerCache.remove(gsonPlayer.getUUID());
            return true;
        }

        return false;
    }

    public Map<UUID, GSONPlayer> getGsonPlayerCache(boolean copy) {

        if (copy) {

            Map<UUID, GSONPlayer> mapCopy = new HashMap<>();
            mapCopy.putAll(this.gsonPlayerCache);

            return mapCopy;
        } else {

            return this.gsonPlayerCache;
        }
    }
}