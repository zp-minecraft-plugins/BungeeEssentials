package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.utils.Utils;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDWarn extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDWarn(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getWarn(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /warn (player) (reason)
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(hasAccess(sender)) {

            if(args.length != 0) {

                ProxiedPlayer proxiedPlayer = Utils.getOnlinePlayerByPartName(args[0]);

                if(proxiedPlayer != null) {

                    String reason = "";

                    if (args.length != 1) {

                        int i = 1;

                        while (i < args.length) {

                            String partReason = args[i];
                            reason += partReason + " ";
                            ++i;
                        }
                    }

                    if (reason.equals("")) {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You did not provide a message to be sent to the player as a warning!"));
                        return;
                    }

                    proxiedPlayer.sendMessage(ChatMessageType.ACTION_BAR, instance.getConfigs().getMessages().getWarningMessage(sender.getName(), reason));
                    proxiedPlayer.sendMessage(ChatMessageType.CHAT, instance.getConfigs().getMessages().getWarningMessage(sender.getName(), reason));

                    sender.sendMessage(instance.getConfigs().getMessages().getCmdWarnSuccess(proxiedPlayer.getName(), reason));
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found online!"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /warn (playername) (reason)"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}
