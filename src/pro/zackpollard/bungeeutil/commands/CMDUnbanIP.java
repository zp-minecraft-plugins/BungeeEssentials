package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONIPAddress;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDUnbanIP extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDUnbanIP(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getUnbanIP(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /unbanip (player) (reason (optional))
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(hasAccess(sender)) {

            if(args.length != 0) {

                GSONIPAddress gsonIP = instance.getIPManager().getIP(args[0]);

                if(gsonIP != null) {

                    if(gsonIP.getCurrentBan() != null) {

                        gsonIP.setCurrentBan(null);
                        sender.sendMessage(instance.getConfigs().getMessages().getCmdUnbanIPSuccess(gsonIP.getIP()));
                    } else {

                        sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A ban was not in place for that IP!"));
                    }
                } else {

                    sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "That IP was not found in the save system!"));
                }
            } else {

                sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /unban (playername)"));
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
        }
    }
}
