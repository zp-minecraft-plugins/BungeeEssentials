package pro.zackpollard.bungeeutil.managers;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.config.GSONReplaceWords;
import pro.zackpollard.bungeeutil.runnables.ServerChatManagerCleanup;
import pro.zackpollard.bungeeutil.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author zack
 * @Date 22/02/15.
 */
public class ServerChatManager implements Listener {

    private final BungeeEssentials instance;
    private final Set<String> chatLockedServers;
    private final Set<String> slowChatServers;
    private final Map<String, Map<String, Integer>> slowChatCooldowns;
    private final Map<String, String> lastMessages;

    public ServerChatManager(BungeeEssentials instance) {

        this.instance = instance;
        chatLockedServers = new HashSet<>();
        slowChatServers = new HashSet<>();
        slowChatCooldowns = new ConcurrentHashMap<>();
        lastMessages = new HashMap<>();
        instance.getProxy().getPluginManager().registerListener(instance, this);
        instance.getProxy().getScheduler().schedule(instance, new ServerChatManagerCleanup(instance, this), 1, 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {

        if (!event.isCommand()) {

            if (event.getSender() instanceof ProxiedPlayer) {

                ProxiedPlayer player = (ProxiedPlayer) event.getSender();
                ServerInfo server = player.getServer().getInfo();

                String lastMessage = lastMessages.get(player.getName());

                if (chatLockedServers.contains(player.getServer().getInfo().getName())) {

                    event.setCancelled(true);
                    player.sendMessage(instance.getConfigs().getMessages().getChatLockedMessage(player.getServer().getInfo().getName()));
                } else {

                    int role = instance.getConfigs().getRoles().getRole(player.getUniqueId());

                    for (GSONReplaceWords gsonReplaceWords : instance.getConfigs().getMainConfig().getChat().getReplaceWords()) {

                        if (gsonReplaceWords.getBypassRole() > role) {

                            if (event.getMessage().toLowerCase().contains(gsonReplaceWords.getBlocked())) {

                                if (gsonReplaceWords.isBlockMessage()) {

                                    event.setCancelled(true);
                                    player.sendMessage(instance.getConfigs().getMessages().getMessageBlockedFromBannedWord(gsonReplaceWords.getBlocked()));
                                } else {

                                    event.setMessage(gsonReplaceWords.doReplacement(event.getMessage()));
                                }
                            }
                        }
                    }
                }

                if (event.isCancelled()) return;

                if (slowChatServers.contains(server.getName())) {

                    if (lastMessage != null) {

                        int maxMessageSimilarity = instance.getConfigs().getMainConfig().getChat().getSlowChat().getMaxMessageSimilarity();

                        if (Utils.getStringSimilarity(event.getMessage(), lastMessage) > maxMessageSimilarity) {

                            event.setCancelled(true);
                            player.sendMessage(instance.getConfigs().getMessages().getChatSimilarityBlocked(maxMessageSimilarity));
                        }
                    }

                    if (event.isCancelled()) return;

                    Integer remainingTime = slowChatCooldowns.get(server.getName()).get(player.getName());

                    if (remainingTime != null) {

                        event.setCancelled(true);
                        player.sendMessage(instance.getConfigs().getMessages().getSlowChatMessage(server.getName(), remainingTime.toString()));
                    } else {

                        slowChatCooldowns.get(server.getName()).put(player.getName(), instance.getConfigs().getMainConfig().getChat().getSlowChat().getSecondsBetweenMessage());
                    }
                } else {

                    if (lastMessage != null) {

                        int maxMessageSimilarity = instance.getConfigs().getMainConfig().getChat().getMaxMessageSimilarity();

                        if (Utils.getStringSimilarity(event.getMessage(), lastMessage) > maxMessageSimilarity) {

                            event.setCancelled(true);
                            player.sendMessage(instance.getConfigs().getMessages().getChatSimilarityBlocked(maxMessageSimilarity));
                        }
                    }
                }

                if (event.isCancelled()) return;

                if (instance.getConfigs().getMainConfig().getChat().getAdvertisingBlocking().matchesBlocks(event.getMessage())) {

                    event.setCancelled(true);
                    player.sendMessage(instance.getConfigs().getMessages().getAdvertisingBlocked());
                }

                if (event.isCancelled()) return;

                lastMessages.put(player.getName(), event.getMessage());
            }
        }
    }

    public boolean toggleChatLock(String serverName) {

        if (chatLockedServers.contains(serverName)) {

            chatLockedServers.remove(serverName);
            return false;
        } else {

            chatLockedServers.add(serverName);
            return true;
        }
    }

    public boolean toggleSlowChat(String serverName) {

        if (slowChatServers.contains(serverName)) {

            slowChatServers.remove(serverName);
            slowChatCooldowns.remove(serverName);
            return false;
        } else {

            slowChatServers.add(serverName);
            slowChatCooldowns.put(serverName, new ConcurrentHashMap<String, Integer>());
            return true;
        }
    }

    public Map<String, Map<String, Integer>> getSlowChatCooldowns() {

        return this.slowChatCooldowns;
    }
}