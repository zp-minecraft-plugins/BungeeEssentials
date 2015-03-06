package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.utils.Utils;

import java.util.Objects;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDKick extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDKick(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getKick(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /kick (player) (reason (optional))
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if(args.length != 0) {

                    ProxiedPlayer proxiedPlayer = Utils.getOnlinePlayerByPartName(args[0]);

                    if(proxiedPlayer != null) {

                        String reason = "";

                        if (args.length != 1) {

                            int i = 1;

                            while (i < args.length) {

                                String partReason = args[i];
                                reason += partReason + " ";
                                ++i;
                            }
                        }

                        if (Objects.equals(reason, "")) {

                            reason = instance.getConfigs().getMessages().getDefaultKickReason();
                        }

                        proxiedPlayer.disconnect(instance.getConfigs().getMessages().getPlayerKickMessage(player.getName(), reason));

                        player.sendMessage(instance.getConfigs().getMessages().getCmdKickSuccess(proxiedPlayer.getName(), reason));
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found online!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /ban (playername) (reason - optional)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}
