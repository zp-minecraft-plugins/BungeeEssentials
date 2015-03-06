package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

import java.util.Map;
import java.util.UUID;

/**
 * @Author zack
 * @Date 05/02/15.
 */

public class CMDOnlineStaff extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDOnlineStaff(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getOnlineStaff(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /onlinestaff
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                Map<UUID, Integer> roles = instance.getConfigs().getRoles().getUsersOnlineWithRoles();

                if(!roles.isEmpty()) {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "-----=====(" + ChatColor.AQUA + ChatColor.BOLD + "All Users With Roles Online!" + ChatColor.RESET + ChatColor.DARK_AQUA + ")=====-----"));

                    for (UUID uuid : roles.keySet()) {

                        ProxiedPlayer proxiedPlayer = instance.getProxy().getPlayer(uuid);

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Name: " + ChatColor.AQUA + ChatColor.BOLD + proxiedPlayer.getName()
                                + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Role: " + instance.getConfigs().getRoles().getRoleColoredName(roles.get(proxiedPlayer.getUniqueId()))
                                + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Server: " + proxiedPlayer.getServer().getInfo().getName()));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, "There are no staff currently online!"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}