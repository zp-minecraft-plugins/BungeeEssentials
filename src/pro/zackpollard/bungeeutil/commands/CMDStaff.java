package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.util.Map;
import java.util.UUID;

/**
 * @Author zack
 * @Date 05/02/15.
 */

public class CMDStaff extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDStaff(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getStaff(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /clearchat
     * /chatclear
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(hasAccess(sender)) {

            Map<UUID, Integer> roles = instance.getConfigs().getRoles().getUsersWithRoles();

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "-----=====(" + ChatColor.AQUA + ChatColor.BOLD + "All Users With Roles" + ChatColor.RESET + ChatColor.DARK_AQUA + ")=====-----"));

            for(UUID uuid : roles.keySet()) {

                GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayer(uuid);

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.DARK_AQUA + "Name: " + ChatColor.AQUA + ChatColor.BOLD + gsonPlayer.getLastKnownName()
                        + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Role: " + instance.getConfigs().getRoles().getRoleColoredName(roles.get(uuid))));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}