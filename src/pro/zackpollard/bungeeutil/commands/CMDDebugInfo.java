package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDDebugInfo extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDDebugInfo(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getDebugInfo(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /debuginfo
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (hasAccess(sender)) {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(false, ChatColor.RED + "PlayerManager Entries: " + instance.getPlayerManager().getGsonPlayerCache(false).size()));
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}
