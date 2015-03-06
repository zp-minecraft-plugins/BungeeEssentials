package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDRemoveRole extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDRemoveRole(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getRemoveRole(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /removerole (player)
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (hasAccess(sender)) {

            if (args.length != 0) {

                GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                if (gsonPlayer != null) {

                    if (instance.getConfigs().getRoles().removeRole(gsonPlayer.getUUID())) {

                        sender.sendMessage(instance.getConfigs().getMessages().getCmdRemoveRoleSuccess(gsonPlayer.getLastKnownName()));
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "This player did note have a role!"));
                    }
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /removerole (playername)"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}