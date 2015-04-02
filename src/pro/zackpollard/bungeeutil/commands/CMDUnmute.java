package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDUnmute extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDUnmute(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getUnmute(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /unmute (player) (reason (optional))
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(hasAccess(sender)) {

            if(args.length != 0) {

                GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                if(gsonPlayer != null) {

                    if(gsonPlayer.getCurrentMute() != null) {

                        ProxiedPlayer mutee = instance.getProxy().getPlayer(gsonPlayer.getUUID());
                        if(mutee != null) {

                            mutee.sendMessage(instance.getConfigs().getMessages().getCmdPlayerUnmuted());
                        }
                        gsonPlayer.setCurrentMute(null);

                        instance.getConfigs().getRoles().sendMessageToRole(
                                instance.getConfigs().getMessages().getCmdUnmuteSuccess(gsonPlayer.getLastKnownName()),
                                instance.getConfigs().getMainConfig().getPermissions().getChatPermissions().getReceiveUnMuteAlerts());
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "There was no mute in place for that player."));
                    }
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /mute (playername) (reason - optional)"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}