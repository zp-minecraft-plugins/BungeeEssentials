package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONBan;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class CMDAddProof extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDAddProof(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getAddProof(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /addproof (player) (url)
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            int playerRole = instance.getConfigs().getRoles().getRole(player.getUniqueId());

            if (playerRole >= getPermissionLevel()) {

                if (args.length == 2) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayerBestGuess(args[0]);

                    if (gsonPlayer != null) {

                        GSONBan gsonBan = gsonPlayer.getCurrentBan();

                        if(gsonBan != null) {

                            String url = args[1];

                            if (!url.equals("")) {

                                gsonBan.addProof(url);
                                player.sendMessage(instance.getConfigs().getMessages().getCmdAddProofSuccess(gsonPlayer.getLastKnownName(), url));
                            } else {

                                player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You didn't type a proof URL"));
                            }


                        } else {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "This player is not currently banned and therefore proof cannot be added to their ban."));
                        }
                    } else {

                        player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "A player with that name was not found in the save system!"));
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect command syntax. Should be /addproof (playername) (url)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}
