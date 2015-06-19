package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

import java.util.Objects;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDTempBan extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDTempBan(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getTempBan(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /tempban (player) (time length) (time unit) (reason (optional))
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            int playerRole = instance.getConfigs().getRoles().getRole(player.getUniqueId());

            if (playerRole >= getPermissionLevel()) {

                if (args.length >= 3) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                    if (gsonPlayer != null) {

                        if(instance.getConfigs().getRoles().getRole(gsonPlayer.getUUID()) <= playerRole) {


                            GSONBan oldBan = gsonPlayer.getCurrentBan();

                            long banTime = 0;

                            int i;

                            banTimeCalc:
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
                                        break banTimeCalc;
                                }

                                Long timeArg = Long.parseLong(args[i]);

                                banTime += timeArg * time;
                            }

                            if (oldBan != null) {

                                if (oldBan.getDuration() == 0) {

                                    player.sendMessage(instance.getConfigs().getMessages().getStaffTempBanWhilePermBanned());
                                    return;
                                } else if (oldBan.getRemainingTime() > banTime) {

                                    player.sendMessage(instance.getConfigs().getMessages().getStaffTempBanWhileLongerTempBanExists());
                                    return;
                                }
                            }

                            GSONBan gsonBan = new GSONBan();
                            gsonBan.setTimestampNow();
                            gsonBan.setDuration(banTime);
                            gsonBan.setBannerUUID(player.getUniqueId());

                            String reason = "";

                            if (i < args.length) {

                                while (i < args.length) {

                                    reason += args[i] + " ";
                                    ++i;
                                }
                            }

                            if (Objects.equals(reason, "")) {

                                reason = instance.getConfigs().getMessages().getDefaultBanReason();
                            }

                            gsonBan.setReason(reason);

                            gsonPlayer.setCurrentBan(gsonBan);

                            ProxiedPlayer proxiedPlayer = instance.getProxy().getPlayer(gsonPlayer.getUUID());

                            if (proxiedPlayer != null) {

                                instance.getPlayerManager().checkPlayerBanned(proxiedPlayer, gsonPlayer);
                            }

                            player.sendMessage();

                            instance.getConfigs().getRoles().sendMessageToRole(
                                    instance.getConfigs().getMessages().getCmdTempBanPlayerSuccess(gsonPlayer.getLastKnownName(), reason, gsonBan.getRemainingTimeFormatted()),
                                    instance.getConfigs().getMainConfig().getPermissions().getChatPermissions().getReceiveTempBanAlerts());
                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot tempban a player of a higher rank than you!"));
                        }
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with this name was not found in the save system!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /tempban (playername) (time) (unit of time (s/m/h/d/w)) (reason - optional)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}
