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
     * /kick (player) (reason (optional))
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            int playerRole = instance.getConfigs().getRoles().getRole(player.getUniqueId());

            if (playerRole >= getPermissionLevel()) {

                if (args.length != 0) {

                    ProxiedPlayer proxiedPlayer = Utils.getOnlinePlayerByPartName(args[0]);

                    if (proxiedPlayer != null) {

                        if(instance.getConfigs().getRoles().getRole(proxiedPlayer.getUniqueId()) <= playerRole) {

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

                            instance.getConfigs().getRoles().sendMessageToRole(
                                    instance.getConfigs().getMessages().getPlayerKickMessage(player.getName(), reason),
                                    instance.getConfigs().getMainConfig().getPermissions().getChatPermissions().getReceiveKickAlerts());
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot kick a player of a higher rank than you!"));
                        }
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
