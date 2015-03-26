package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONMute;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.util.Objects;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDTempMute extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDTempMute(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getTempMute(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /tempmute (player) (time length) (time unit) (reason (optional))
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if(args.length >= 3) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                    if(gsonPlayer != null) {

                        GSONMute oldMute = gsonPlayer.getCurrentMute();

                        long muteTime = 0;

                        int i;

                        muteTimeCalc:
                        for (i = 1; i + 1 < args.length; i += 2) {

                            long time;

                            switch (args[i + 1].toLowerCase()) {

                                case "s":
                                    time = 1000;
                                    break;
                                case "m":
                                    time = 60000;
                                    break;
                                case "h":
                                    time = 3600000;
                                    break;
                                case "d":
                                    time = 86400000;
                                    break;
                                case "w":
                                    time = 604800000;
                                    break;
                                default:
                                    break muteTimeCalc;
                            }

                            Long timeArg = Long.parseLong(args[i]);

                            muteTime += timeArg * time;
                        }

                        if (oldMute != null) {

                            if (oldMute.getDuration() == 0) {

                                player.sendMessage(instance.getConfigs().getMessages().getStaffTempMuteWhilePermMuted());
                                return;
                            } else if (oldMute.getRemainingTime() > muteTime) {

                                player.sendMessage(instance.getConfigs().getMessages().getStaffTempMuteWhileLongerTempMuteExists());
                                return;
                            }
                        }

                        GSONMute gsonMute = new GSONMute();
                        gsonMute.setTimestampNow();
                        gsonMute.setDuration(muteTime);
                        gsonMute.setMuterUUID(player.getUniqueId());

                        String reason = "";

                        while (i < args.length) {

                            reason += args[i] + " ";
                            ++i;
                        }

                        if (Objects.equals(reason, "")) {

                            reason = instance.getConfigs().getMessages().getDefaultMuteReason();
                        }

                        gsonMute.setReason(reason);

                        gsonPlayer.setCurrentMute(gsonMute);

                        instance.getConfigs().getRoles().sendMessageToRole(
                                instance.getConfigs().getMessages().getCmdTempMuteSuccess(gsonPlayer.getLastKnownName(), reason, gsonMute.getRemainingTimeFormatted()),
                                instance.getConfigs().getMainConfig().getPermissions().getChatPermissions().getReceiveTempMuteAlerts());
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /tempmute (playername) (time) (unit of time (s/m/h/d/w)) (reason - optional)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}
