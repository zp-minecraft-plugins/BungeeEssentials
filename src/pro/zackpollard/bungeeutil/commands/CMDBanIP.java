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
public class CMDBanIP extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDBanIP(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getBanIP(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /banip (ip) (reason (optional))
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if(args.length != 0) {

                    GSONIPAddress gsonIP = instance.getIPManager().getIP(args[0]);

                    if(gsonIP != null) {

                        GSONBan gsonBan = new GSONBan();
                        gsonBan.setTimestampNow();
                        gsonBan.setBannerUUID(player.getUniqueId());

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

                        player.sendMessage(instance.getConfigs().getMessages().getCmdBanIPSuccess(gsonIP.getIP(), reason));
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "This IP was not found in the save system!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /banip (playername) (reason - optional)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}
