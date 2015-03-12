package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

/**
 * @Author zack
 * @Date 05/02/15.
 */

public class CMDMessageToggle extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDMessageToggle(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getMessageToggle(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /messagetoggle
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if(instance.getPrivateChatManager().togglePrivateChat(player)) {

                    player.sendMessage(instance.getConfigs().getMessages().getCmdMessageToggleEnabled());
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().getCmdMessageToggleDisabled());
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}