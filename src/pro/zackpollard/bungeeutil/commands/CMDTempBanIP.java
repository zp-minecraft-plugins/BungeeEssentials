package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONIPAddress;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDTempBanIP extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDTempBanIP(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getTempBanIP(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /tempbanip (ip) (time length) (time unit) (reason (optional))
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if(args.length >= 3) {

                    GSONIPAddress gsonIP = instance.getIPManager().getIP(args[0]);

                    if(gsonIP != null) {

                        GSONBan oldBan = gsonIP.getCurrentBan();

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

                        while (i < args.length) {

                            reason += args[i] + " ";
                            ++ i;
                        }

                        if (Objects.equals(reason, "")) {

                            reason = instance.getConfigs().getMessages().getDefaultBanReason();
                        }

                        gsonBan.setReason(reason);

                        gsonIP.setCurrentBan(gsonBan);

                        List<UUID> uuids = gsonIP.getUUIDs();

                        for(ProxiedPlayer proxiedPlayer : instance.getProxy().getPlayers()) {

                            if(uuids.contains(proxiedPlayer.getUniqueId())) {

                                instance.getIPManager().checkIPBanned(proxiedPlayer, gsonIP);
                            }
                        }

                        player.sendMessage(instance.getConfigs().getMessages().getCmdTempBanIPSuccess(gsonIP.getIP(), reason, gsonBan.getRemainingTimeFormatted()));
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "This IP was not found in the save system!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /tempbanip (playername) (time) (unit of time (s/m/h/d/w)) (reason - optional)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}
