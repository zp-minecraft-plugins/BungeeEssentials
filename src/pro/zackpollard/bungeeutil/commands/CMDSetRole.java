package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDSetRole extends BungeeEssentialsCommand {

    public CMDSetRole(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getSetRole(), alias);
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /setrole (player)
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(hasAccess(sender)) {

            if(args.length == 2) {

                GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                if(gsonPlayer != null) {

                    String role = args[1];

                    if(instance.getConfigs().getRoles().setRole(gsonPlayer.getUUID(), role)) {

                        sender.sendMessage(instance.getConfigs().getMessages().getCmdSetRoleSuccess(gsonPlayer.getLastKnownName(), instance.getConfigs().getRoles().getRoleColoredName(instance.getConfigs().getRoles().getRole(gsonPlayer.getUUID()))));
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "This player already had this role or this role alias did not exist!"));
                    }
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /setrole (playername) role"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}