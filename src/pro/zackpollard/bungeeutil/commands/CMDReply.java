package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

import java.util.UUID;

/**
 * @Author zack
 * @Date 05/02/15.
 */

public class CMDReply extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDReply(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getReply(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /reply
     * /r
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if(args.length >= 1) {

                    ProxiedPlayer receiver = null;
                    UUID receiverUUID = instance.getPrivateChatManager().getLastRecipient(player.getUniqueId());

                    if(receiverUUID == null) {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You have not messaged or been messaged by anyone on the server!"));
                        return;
                    }

                    for(ProxiedPlayer playerCheck : instance.getProxy().getPlayers()) {

                        if(playerCheck.getUniqueId().equals(receiverUUID)) {

                            if(!instance.getConfigs().getRoles().getVanishedUsers().contains(playerCheck.getUniqueId())) {

                                receiver = playerCheck;
                            }

                            break;
                        }
                    }

                    if(receiver != null) {

                        String message = "";

                        for (int i = 0; i < args.length; ++i) {

                            message += args[i] + " ";
                        }

                        instance.getPrivateChatManager().sendMessage(player, receiver, message);
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The player you last messaged is now offline!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /r (message)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}