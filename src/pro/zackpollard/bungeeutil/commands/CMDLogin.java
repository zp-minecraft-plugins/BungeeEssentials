package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @author zack
 */
public class CMDLogin extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDLogin(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getLogin(), alias);
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

                    if(!gsonPlayer.isAuthenticated()) {

                        if (gsonPlayer.hasOfflineModePassword()) {

                            if(gsonPlayer.compareOfflinePassword(args[0])) {

                                gsonPlayer.setAuthenticated(true);
                                player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "You have now been authenticated and can play as normal!"));
                            } else {

                                player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "That password was incorrect, please try again."));
                            }
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You never set a password."));
                        }
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "You are already authenticated, have fun!"));
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
