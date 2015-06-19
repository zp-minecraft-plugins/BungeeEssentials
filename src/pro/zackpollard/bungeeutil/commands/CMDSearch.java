package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONIPAddress;
import pro.zackpollard.bungeeutil.json.storage.GSONMute;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.util.List;
import java.util.UUID;

/**
 * @Author zack
 * @Date 06/02/15.
 */
public class CMDSearch extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDSearch(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getSearch(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /search (playername/ip address)
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (hasAccess(sender)) {

            if (args.length == 1) {

                if (!args[0].contains(".")) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                    if (gsonPlayer != null) {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "-----=====(" + ChatColor.AQUA + ChatColor.BOLD + gsonPlayer.getLastKnownName() + " Lookup" + ChatColor.RESET + ChatColor.DARK_AQUA + ")=====-----"));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "First Seen: " + ChatColor.RESET + ChatColor.AQUA + gsonPlayer.getFirstSeenTime()));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Last Seen: " + ChatColor.RESET + ChatColor.AQUA + gsonPlayer.getLastOnlineString()));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Last Known IP: " + ChatColor.RESET + ChatColor.AQUA + gsonPlayer.getLastKnownIP()));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Last Server: " + ChatColor.RESET + ChatColor.AQUA + gsonPlayer.getLastConnectedServer()));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Total Online Time: " + ChatColor.RESET + ChatColor.AQUA + gsonPlayer.getTotalOnlineTimeFormatted()));

                        GSONBan gsonBan = gsonPlayer.getCurrentBan();

                        if (gsonBan != null) {

                            if (gsonBan.getDuration() == 0) {

                                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Permanently Banned"));
                            } else {

                                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Temporarily Banned"));
                                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Time Remaining: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getRemainingTimeFormatted()));
                            }

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Timestamp: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getTimestampFormatted()));
                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Reason: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getReason()));
                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Banned By: " + ChatColor.RESET + ChatColor.AQUA + instance.getPlayerManager().getPlayer(gsonBan.getBannerUUID()).getLastKnownName()));
                        } else {

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Not Banned!"));
                        }

                        GSONMute gsonMute = gsonPlayer.getCurrentMute();

                        if (gsonMute != null) {

                            if (gsonMute.getDuration() == 0) {

                                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Mute Status: " + ChatColor.RESET + ChatColor.AQUA + "Permanently Banned"));
                            } else {

                                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Mute Status: " + ChatColor.RESET + ChatColor.AQUA + "Temporarily Banned"));
                                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Mute Time Remaining: " + ChatColor.RESET + ChatColor.AQUA + gsonMute.getRemainingTimeFormatted()));
                            }

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Mute Timestamp: " + ChatColor.RESET + ChatColor.AQUA + gsonMute.getTimestampFormatted()));
                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Mute Reason: " + ChatColor.RESET + ChatColor.AQUA + gsonMute.getReason()));
                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Muted By: " + ChatColor.RESET + ChatColor.AQUA + instance.getPlayerManager().getPlayer(gsonMute.getMuterUUID()).getLastKnownName()));
                        } else {

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Mute Status: " + ChatColor.RESET + ChatColor.AQUA + "Not Muted!"));
                        }

                        List<String> ips = gsonPlayer.getKnownIPs();

                        if (ips.size() >= 5) {

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "--==(" + ChatColor.AQUA + "Showing 5/" + ips.size() + " Previous IPs" + ChatColor.RESET + ChatColor.DARK_AQUA + ")==--"));
                        } else {

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "--==(" + ChatColor.AQUA + "All Previous IPs" + ChatColor.RESET + ChatColor.DARK_AQUA + ")==--"));
                        }

                        int i = 0;

                        for (String ipAddress : ips) {

                            if (i > 4) {

                                break;
                            }

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - " + ChatColor.RESET + ChatColor.AQUA + ipAddress));
                            ++i;
                        }
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with that name could not be found!"));
                    }
                } else {

                    GSONIPAddress gsonipAddress = instance.getIPManager().getIP(args[0]);

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "-----=====(" + ChatColor.AQUA + ChatColor.BOLD + gsonipAddress.getIP() + " Lookup" + ChatColor.RESET + ChatColor.DARK_AQUA + ")=====-----"));
                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Last Player: " + ChatColor.RESET + ChatColor.AQUA + gsonipAddress.getLastOnlineUser()));

                    GSONBan gsonBan = gsonipAddress.getCurrentBan();

                    if (gsonBan != null) {

                        if (gsonBan.getDuration() == 0) {

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Permanently Banned"));
                        } else {

                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Temporarily Banned"));
                            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Time Remaining: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getRemainingTimeFormatted()));
                        }

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Timestamp: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getTimestampFormatted()));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Reason: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getReason()));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Banned By: " + ChatColor.RESET + ChatColor.AQUA + instance.getPlayerManager().getPlayer(gsonBan.getBannerUUID()).getLastKnownName()));
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Not Banned!"));
                    }

                    List<UUID> accounts = gsonipAddress.getUUIDs();

                    if (accounts.size() >= 5) {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "--==(" + ChatColor.AQUA + "Showing 5/" + accounts.size() + " Linked Accounts" + ChatColor.RESET + ChatColor.DARK_AQUA + ")==--"));
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "--==(" + ChatColor.AQUA + "All Linked Accounts" + ChatColor.RESET + ChatColor.DARK_AQUA + ")==--"));
                    }

                    int i = 0;

                    for (UUID account : accounts) {

                        if (i > 4) {

                            break;
                        }

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - " + ChatColor.RESET + ChatColor.AQUA + instance.getPlayerManager().getPlayer(account).getLastKnownName()));
                        ++i;
                    }
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /search (ip/playername)"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}