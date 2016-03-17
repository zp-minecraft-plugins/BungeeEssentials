package pro.zackpollard.bungeeutil.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.json.storage.GSONPlayer;

/**
 * @author zack
 */
public class CMDWebsite extends BungeeEssentialsCommand {

    private final BungeeEssentials instance;

    public CMDWebsite(BungeeEssentials instance, String name, String... alias) {

        super(instance, name, instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getWebsite(), alias);
        this.instance = instance;
        instance.getProxy().getPluginManager().registerCommand(instance, this);
    }

    /**
     * /register (password)
     */

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (hasAccess(sender)) {

                if(args.length == 3) {

                    GSONPlayer gsonPlayer = instance.getPlayerManager().getPlayer(player.getUniqueId());

                    switch(args[0].toLowerCase()) {

                        case "register": {

                            HttpResponse<String> response = null;

                            try {
                                response = Unirest.get("http://104.128.51.29/api.php")
                                        .queryString("action", "register")
                                        .queryString("hash", "iA2cirDtdpKQoz-6Mzl6E3sM8NWAnbuOnCYN8NRe11gU_k0D")
                                        .queryString("username", gsonPlayer.getLastKnownName())
                                        .queryString("password", args[2])
                                        .queryString("email", args[1])
                                        .queryString("group", "Member")
                                        .asString();
                            } catch (UnirestException e) {
                                e.printStackTrace();
                            }

                            if(response != null) {

                                System.out.println("test");
                                System.out.println(response.getBody());

                                JSONObject jsonResponse = new JSONObject(response.getBody());

                                if(response.getStatus() == 200 && !jsonResponse.has("error")) {

                                    gsonPlayer.setForumUserID(jsonResponse.getInt("user_id"));
                                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.YELLOW + "Your account was created, please login at http://carbon-mc.net/login with your email (" + args[2] + ") and password (" + args[2] + ")"));
                                } else {

                                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "There was an error whilst creating your account. The error was: " + ChatColor.BOLD + jsonResponse.getString("message")));
                                }
                            }

                            break;
                        }

                        default: {

                            player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect syntax. Correct syntax is " + ChatColor.BOLD + "/website register (email) (password)"));
                        }
                    }
                } else {

                    player.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "Incorrect syntax. Correct syntax is " + ChatColor.BOLD + "/website register (email) (password)"));
                }
            } else {

                player.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
            }
        } else {

            sender.sendMessage(instance.getConfigs().getMessages().generateMessage(true, ChatColor.RED + "You cannot execute these commands from console!"));
        }
    }
}