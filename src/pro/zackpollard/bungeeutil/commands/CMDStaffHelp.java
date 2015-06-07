package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

/**
 * @Author zack
 * @Date 06/02/15.
 */
public class CMDStaffHelp extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDStaffHelp(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getStaffHelp(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /staffhelp
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                int role = instance.getConfigs().getRoles().getRole(player.getUniqueId());

                player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "-----=====(" + ChatColor.AQUA + ChatColor.BOLD + "Available Commands" + ChatColor.RESET + ChatColor.DARK_AQUA + ")=====-----"));


                player.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Ban Status: " + ChatColor.RESET + ChatColor.AQUA + "Not Banned!"));
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}