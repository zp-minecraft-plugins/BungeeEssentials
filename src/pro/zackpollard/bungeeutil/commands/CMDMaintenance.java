package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @author Zack Pollard
 */
public class CMDMaintenance extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDMaintenance(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getMaintenance(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /maintenance (password)
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (hasAccess(sender)) {

            if(args.length == 0) {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "Maintenance mode has been " + ChatColor.BOLD + (instance.toggleMaintenanceMode() ? "enabled." : "disabled.")));
            } else if(args.length == 2) {

                if(args[0].equalsIgnoreCase("toggle")) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[1]);

                    if(gsonPlayer != null) {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "Maintenance mode bypass has been " + ChatColor.BOLD + (gsonPlayer.toggleMaintenanceModeBypass() ? " enabled " : " disabled ") + ChatColor.RESET + ChatColor.YELLOW + " for " + ChatColor.BOLD + gsonPlayer.getLastKnownName()));
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                    }
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect syntax. Correct syntax is " + ChatColor.BOLD + "/maintenance  -or-  /maintenance toggle (playername)"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect syntax. Correct syntax is " + ChatColor.BOLD + "/maintenance  -or-  /maintenance toggle (playername)"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}