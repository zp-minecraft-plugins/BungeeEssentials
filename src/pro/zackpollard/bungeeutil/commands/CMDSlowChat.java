package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

/**
 * @Author zack
 * @Date 05/02/15.
 */

public class CMDSlowChat extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDSlowChat(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getSlowChat(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /slowchat [server]
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            ServerInfo server;

            switch (args.length) {
                case 0:
                    server = player.getServer().getInfo();
                    break;
                case 1:
                    server = instance.getProxy().getServerInfo(args[0]);
                    if (server == null) {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The server specified does not exist on this network!"));
                        return;
                    }

                    break;
                default:
                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "The correct syntax for this command is /slowchat [servername]"));
                    return;
            }

            if (instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if (instance.getServerChatManager().toggleSlowChat(server.getName())) {

                    BaseComponent[] message = instance.getConfigs().getMessages().getCmdSlowChatEnabled(server.getName());

                    for (ProxiedPlayer serverPlayer : server.getPlayers()) {

                        if (!serverPlayer.equals(player)) {

                            serverPlayer.sendMessage(message);
                        }
                    }

                    player.sendMessage(message);
                } else {

                    BaseComponent[] message = instance.getConfigs().getMessages().getCmdSlowChatDisabled(server.getName());

                    for (ProxiedPlayer serverPlayer : server.getPlayers()) {

                        if (!serverPlayer.equals(player)) {

                            serverPlayer.sendMessage(message);
                        }
                    }

                    player.sendMessage(message);
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}