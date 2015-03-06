package pro.zackpollard.bungeeutil.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;
import pro.zackpollard.bungeeutil.BungeeEssentials;

public abstract class BungeeEssentialsCommand extends Command {

    private final int permissionLevel;
    protected final BungeeEssentials instance;

    public BungeeEssentialsCommand(BungeeEssentials instance, String name, int permissionLevel, String... aliases) {

        super(name, null, aliases);
        this.instance = instance;
        this.permissionLevel = permissionLevel;
    }

    public int getPermissionLevel() {

        return permissionLevel;
    }

    public boolean hasAccess(CommandSender sender) {

        boolean access = false;

        if(sender instanceof ConsoleCommandSender) {

            access = true;
        } else if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(instance.getConfigs().getRoles().getRole(player.getUniqueId()) >= getPermissionLevel()) {

                access = true;
            }
        }

        return access;
    }
}