package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDCReload extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDCReload(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getCReload(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /creload
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(hasAccess(sender)) {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.GREEN + "Reload complete!"));
            instance.reload();
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}