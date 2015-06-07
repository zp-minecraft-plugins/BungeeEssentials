package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

/**
 * @Author zack
 * @Date 05/02/15.
 */

public class CMDReport extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDReport(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getReport(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /report (playername) (reason)
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if (args.length >= 2) {

                    if (instance.getConfigs().getRoles().getUsersOnlineWithRoles().size() != 0) {

                        ProxiedPlayer receiver = null;

                        for (ProxiedPlayer playerCheck : instance.getProxy().getPlayers()) {

                            if (playerCheck.getName().toLowerCase().startsWith(args[0].toLowerCase())) {

                                if (!instance.getConfigs().getRoles().getVanishedUsers().contains(playerCheck.getUniqueId())) {

                                    receiver = playerCheck;
                                }

                                break;
                            }
                        }

                        if (receiver != null) {

                            String message = "";

                            for (int i = 1; i < args.length; ++i) {

                                message += args[i] + " ";
                            }

                            player.sendMessage(instance.getConfigs().getMessages().getCmdReportSent());

                            instance.getConfigs().getRoles().sendMessageToRole(instance.getConfigs().getMessages().getReportMessage(player.getName(), receiver.getName(), message), instance.getConfigs().getMainConfig().getPermissions().getChatPermissions().getReceiveReports());
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with that name could not be found on the server!"));
                        }
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "There are no staff members online currently!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /report (playername) (message)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}