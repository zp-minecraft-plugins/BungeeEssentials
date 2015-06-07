package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

/**
 * @Author zack
 * @Date 05/02/15.
 */

public class CMDClearChat extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDClearChat(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getChatClear(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /clearchat
     * /chatclear
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                BaseComponent[] message = TextComponent.fromLegacyText("");

                for (ProxiedPlayer allPlayers : instance.getProxy().getPlayers()) {

                    for (int i = 0; i < 100; ++i) {

                        allPlayers.sendMessage(message);
                    }
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}