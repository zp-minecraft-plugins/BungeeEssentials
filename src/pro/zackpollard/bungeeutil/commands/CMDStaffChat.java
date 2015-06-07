package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDStaffChat extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDStaffChat(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getStaffChat(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /staffchat
     * /sc
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if (args.length >= 1) {

                    String message = "";

                    for (String arg : args) {

                        message += " " + arg;
                    }

                    instance.getStaffChatManager().sendMessageToStaff(player, ChatColor.translateAlternateColorCodes('&', message));
                } else if (args.length == 0) {

                    boolean status = instance.getStaffChatManager().toggleChat(player.getUniqueId());

                    if (status) {

                        player.sendMessage(instance.getConfigs().getMessages().getCmdStaffChatEnabled());
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().getCmdStaffChatDisabled());
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /staffchat -or- /staffchat (message)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}