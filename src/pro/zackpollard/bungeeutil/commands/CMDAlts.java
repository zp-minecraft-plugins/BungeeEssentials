package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONIPAddress;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author zack
 * @Date 06/02/15.
 */
public class CMDAlts extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDAlts(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getAlts(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /alts (playername)
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(hasAccess(sender)) {

            if(args.length == 1 || args.length == 2) {

                int page = 1;

                if(args.length == 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch(NumberFormatException e) {
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The page number (second argument) you entered was not an integer!"));
                        return;
                    }
                }

                if(page <= 0) {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The page number (second argument) you entered was less than or equal to zero!"));
                    return;
                }

                GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                if (gsonPlayer != null) {

                    List<String> ips = gsonPlayer.getKnownIPs();
                    List<UUID> alts = new ArrayList<>();

                    int finish = (10 * page);
                    int start = finish - 10;

                    for(String ip : ips) {

                        GSONIPAddress ipAddress = instance.getIPManager().getIP(ip);

                        if(ipAddress != null) {

                            for(UUID uuid : ipAddress.getUUIDs()) {

                                if(!uuid.equals(gsonPlayer.getUUID())) {

                                    alts.add(uuid);
                                }
                            }
                        }
                    }

                    int maxPage = (int) Math.ceil((double) alts.size() / 10);

                    if(maxPage == 0) {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "There were no alternate accounts found for this account!"));
                        return;
                    }

                    if(page > maxPage) {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The page number (second argument) you entered does not exist for this user!"));
                        return;
                    }

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "-----=====(" + ChatColor.AQUA + ChatColor.BOLD + gsonPlayer.getLastKnownName() + " Alts - Page " + page + "/" + maxPage + ChatColor.RESET + ChatColor.DARK_AQUA + ")=====-----"));

                    int i = start;

                    while(i < finish && i < alts.size()) {

                        GSONPlayer gsonAlt = instance.getPlayerManager().getPlayer(alts.get(i));
                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + " - " + ChatColor.RESET + ChatColor.AQUA + gsonAlt.getLastKnownName()));
                        ++i;
                    }
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with that name could not be found!"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true,  ChatColor.RED + "Incorrect command syntax. Should be /alts (playername) [pagenumber]"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}
