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
public class CMDBan extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDBan(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getBan(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     *
     * /ban (player) (reason (optional))
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                if(args.length != 0) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                    if(gsonPlayer != null) {

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

                        gsonPlayer.setCurrentBan(gsonBan);

                        ProxiedPlayer proxiedPlayer = instance.getProxy().getPlayer(gsonPlayer.getUUID());

                        if (proxiedPlayer != null) {

                            instance.getPlayerManager().checkPlayerBanned(proxiedPlayer, gsonPlayer);
                        }

                        player.sendMessage(instance.getConfigs().getMessages().getCmdBanPlayerSuccess(gsonPlayer.getLastKnownName(), reason));
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with that name was not found in the save system!"));
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
