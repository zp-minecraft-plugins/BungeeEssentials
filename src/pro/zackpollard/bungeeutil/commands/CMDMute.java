package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONMute;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.util.Objects;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDMute extends BungeeEssentialsCommand implements Listener {

    private final BungeeEssentials instance;

    public CMDMute(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getMute(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
        instance.getProxy().getPluginManager().registerListener(instance, this);
    }

    /**
     * /mute (player) (reason (optional))
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            int playerRole = instance.getConfigs().getRoles().getRole(player.getUniqueId());

            if (playerRole >= getPermissionLevel()) {

                if (args.length != 0) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                    if (gsonPlayer != null) {

                        if(instance.getConfigs().getRoles().getRole(gsonPlayer.getUUID()) <= playerRole) {

                            GSONMute gsonMute = new GSONMute();
                            gsonMute.setTimestampNow();
                            gsonMute.setMuterUUID(player.getUniqueId());
                            gsonMute.setDuration(0);

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

                                reason = instance.getConfigs().getMessages().getDefaultMuteReason();
                            }

                            gsonMute.setReason(reason);

                            gsonPlayer.setCurrentMute(gsonMute);

                            instance.getConfigs().getRoles().sendMessageToRole(
                                    instance.getConfigs().getMessages().getCmdMuteSuccess(gsonPlayer.getLastKnownName(), reason),
                                    instance.getConfigs().getMainConfig().getPermissions().getChatPermissions().getReceiveMuteAlerts());
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot mute a player of a higher rank than you!"));
                        }
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /mute (playername) (reason - optional)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {

        if (event.getSender() instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayer(player.getUniqueId());
            GSONMute gsonMute = gsonPlayer.getCurrentMute();

            if (gsonMute != null) {

                if (!event.isCommand()) {

                    if (gsonMute.getDuration() == (long) 0) {

                        player.sendMessage(instance.getConfigs().getMessages().getPlayerPermMuteMessage(
                                instance.getPlayerManager().getPlayer(gsonMute.getMuterUUID()).getLastKnownName(),
                                gsonMute.getTimestampFormatted(),
                                gsonMute.getReason()));
                    } else {

                        if (gsonMute.getRemainingTime() <= 0) {

                            gsonPlayer.setCurrentMute(null);
                        }

                        player.sendMessage(instance.getConfigs().getMessages().getPlayerTempMuteMessage(
                                instance.getPlayerManager().getPlayer(gsonMute.getMuterUUID()).getLastKnownName(),
                                gsonMute.getTimestampFormatted(),
                                gsonMute.getReason(),
                                gsonMute.getRemainingTimeFormatted()));
                    }

                    event.setCancelled(true);
                } else {

                    String command = event.getMessage().substring(1);

                    for (String string : instance.getConfigs().getMainConfig().getBlockedCommandsWhenMuted()) {

                        if (command.toLowerCase().startsWith(string)) {

                            if (gsonMute.getDuration() == (long) 0) {

                                player.sendMessage(instance.getConfigs().getMessages().getPlayerPermMuteMessage(
                                        instance.getPlayerManager().getPlayer(gsonMute.getMuterUUID()).getLastKnownName(),
                                        gsonMute.getTimestampFormatted(),
                                        gsonMute.getReason()));
                            } else {

                                if (gsonMute.getRemainingTime() <= 0) {

                                    gsonPlayer.setCurrentMute(null);
                                }

                                player.sendMessage(instance.getConfigs().getMessages().getPlayerTempMuteMessage(
                                        instance.getPlayerManager().getPlayer(gsonMute.getMuterUUID()).getLastKnownName(),
                                        gsonMute.getTimestampFormatted(),
                                        gsonMute.getReason(),
                                        gsonMute.getRemainingTimeFormatted()));
                            }

                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}