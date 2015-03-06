package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONIPAddress;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.util.List;

/**
 * @Author zack
 * @Date 06/02/15.
 */
public class CMDBans extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDBans(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getBanlist(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /bans (playername)
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if (args.length == 1 || args.length == 2) {

                    int page = 1;

                    if (args.length == 2) {
                        try {
                            page = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The page number (second argument) you entered was not an integer!"));
                            return;
                        }
                    }

                    if (page <= 0) {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The page number (second argument) you entered was less than or equal to zero!"));
                        return;
                    }

                    String identityString = null;
                    List<GSONBan> bans = null;

                    if(args[0].contains(".")) {

                        GSONIPAddress gsonipAddress = instance.getIPManager().getIP(args[0]);
                        if(gsonipAddress != null) {
                            identityString = gsonipAddress.getIP();
                            bans = gsonipAddress.getBans();
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "This IP was not found in our save system!"));
                        }
                    } else {

                        GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                        if(gsonPlayer != null) {
                            identityString = gsonPlayer.getLastKnownName();
                            bans = gsonPlayer.getBans();
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "This player was not found in our save system!"));
                        }
                    }

                    if (bans != null && identityString != null) {

                        int maxPage = bans.size();

                        if (maxPage == 0) {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "There were no bans found for this account!"));
                            return;
                        }

                        if (page > maxPage) {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The page number (second argument) you entered does not exist for this user!"));
                            return;
                        }

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "-----=====(" + ChatColor.AQUA + ChatColor.BOLD + identityString + " Bans - #" + page + "/" + maxPage + ChatColor.RESET + ChatColor.DARK_AQUA + ")=====-----"));

                        GSONBan gsonBan = bans.get(maxPage - page);

                        if (gsonBan.getDuration() == 0) {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Permanently Banned"));
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Temporarily Banned"));
                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Time Remaining: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getRemainingTimeFormatted()));
                        }

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Timestamp: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getTimestampFormatted()));
                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Ban Reason: " + ChatColor.RESET + ChatColor.AQUA + gsonBan.getReason()));
                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - Banned By: " + ChatColor.RESET + ChatColor.AQUA + instance.getPlayerManager().getPlayer(gsonBan.getBannerUUID()).getLastKnownName()));
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Something went wrong with this command!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /bans (playername/ipaddress) [pagenumber]"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}