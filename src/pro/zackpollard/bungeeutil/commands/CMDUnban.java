package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDUnban extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDUnban(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getUnban(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /unban (player) (reason (optional))
     * bungeeutils.unban
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (hasAccess(sender)) {

            if (args.length != 0) {

                GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                if (gsonPlayer != null) {

                    if (gsonPlayer.getCurrentBan() != null) {

                        gsonPlayer.setCurrentBan(null);

                        instance.getConfigs().getRoles().sendMessageToRole(
                                instance.getConfigs().getMessages().getCmdUnbanPlayerSuccess(gsonPlayer.getLastKnownName()),
                                instance.getConfigs().getMainConfig().getPermissions().getChatPermissions().getReceiveUnBanAlerts());
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A ban was not in place for that player!"));
                    }
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /unban (playername)"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}
