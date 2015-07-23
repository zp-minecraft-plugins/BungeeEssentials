package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @author zack
 */
public class CMDRegister extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDRegister(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getRegister(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /register (password)
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (hasAccess(sender)) {

                if(args.length == 1) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayer(player.getUniqueId());

                    if (!gsonPlayer.hasOfflineModePassword()) {

                        gsonPlayer.setOfflineModePassword(args[0]);
                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "Your password has now been set," + ChatColor.RED + " do not forget it!" + ChatColor.YELLOW + " You will now be able to login if the mojang session servers go down!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect syntax. Correct syntax is " + ChatColor.BOLD + "/register (password)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}
