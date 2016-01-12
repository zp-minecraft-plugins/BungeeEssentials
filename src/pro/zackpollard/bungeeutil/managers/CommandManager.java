package pro.zackpollard.bungeeutil.managers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author zack
 * @Date 26/02/15.
 */
public class CommandManager implements Listener {

    private final BungeeEssentials instance;
    private final List<BungeeEssentialsCommand> commands = new ArrayList<>();

    public CommandManager(BungeeEssentials instance) {

        this.instance = instance;
        registerCommands();
        instance.getProxy().getPluginManager().registerListener(instance, this);
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {

        if (event.isCommand()) {

            if (event.getSender() instanceof ProxiedPlayer) {

                ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

                Set<Map.Entry<String, Integer>> blockedCommands = instance.getConfigs().getMainConfig().getBlockedCommands().entrySet();

                String command = event.getMessage().substring(1);

                for (Map.Entry<String, Integer> blockedCommand : blockedCommands) {

                    if (command.toLowerCase().startsWith(blockedCommand.getKey())) {

                        if (instance.getConfigs().getRoles().getRole(proxiedPlayer.getUniqueId()) < blockedCommand.getValue() || blockedCommand.getValue() < 0) {

                            event.setCancelled(true);
                            proxiedPlayer.sendMessage(instance.getConfigs().getMessages().getCommandPermissionDenied());
                        }
                    }
                }
            }
        }
    }

    private void registerCommands() {

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getAccounts() != -1) {
            commands.add(new CMDAccounts(instance, "accounts"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getAddProof() != -1) {
            commands.add(new CMDAddProof(instance, "addproof"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getAlts() != -1) {
            commands.add(new CMDAlts(instance, "alts"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getBan() != -1) {
            commands.add(new CMDBan(instance, "ban"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getBanIP() != -1) {
            commands.add(new CMDBanIP(instance, "banip"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getBanlist() != -1) {
            commands.add(new CMDBans(instance, "banlist", "bans"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getChatLock() != -1) {
            commands.add(new CMDChatLock(instance, "chatlock", "lockchat"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getChatClear() != -1) {
            commands.add(new CMDClearChat(instance, "clearchat", "chatclear"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getCReload() != -1) {
            commands.add(new CMDCReload(instance, "creload"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getCRestart() != -1) {
            commands.add(new CMDCRestart(instance, "crestart"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getDebugInfo() != -1) {
            commands.add(new CMDDebugInfo(instance, "debuginfo"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getKick() != -1) {
            commands.add(new CMDKick(instance, "kick"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getLogin() != -1) {
            commands.add(new CMDLogin(instance, "login"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getMaintenance() != -1) {
            commands.add(new CMDMaintenance(instance, "maintenance"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getMessage() != -1) {
            commands.add(new CMDMessage(instance, "msg", "message", "pm", "tell", "m", "t", "whisper"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getMessageToggle() != -1) {
            commands.add(new CMDMessageToggle(instance, "messagetoggle", "msgtoggle", "pmtoggle", "togglemessage", "togglemsg", "togglepm"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getMute() != -1) {
            commands.add(new CMDMute(instance, "mute"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getOnlineStaff() != -1) {
            commands.add(new CMDOnlineStaff(instance, "onlinestaff"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getRegister() != -1) {
            commands.add(new CMDRegister(instance, "register"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getRemoveRole() != -1) {
            commands.add(new CMDRemoveRole(instance, "removerole"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getReply() != -1) {
            commands.add(new CMDReply(instance, "reply", "r"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getReport() != -1) {
            commands.add(new CMDReport(instance, "report"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getSearch() != -1) {
            commands.add(new CMDSearch(instance, "search"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getSetRole() != -1) {
            commands.add(new CMDSetRole(instance, "setrole"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getSlowChat() != -1) {
            commands.add(new CMDSlowChat(instance, "slowchat"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getSocialSpy() != -1) {
            commands.add(new CMDSocialSpy(instance, "socialspy"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getStaff() != -1) {
            commands.add(new CMDStaff(instance, "staff", "stafflist"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getStaffChat() != -1) {
            commands.add(new CMDStaffChat(instance, "staffchat", "sc"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getStaffVanish() != -1) {
            commands.add(new CMDStaffVanish(instance, "staffvanish"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getTempBan() != -1) {
            commands.add(new CMDTempBan(instance, "tempban"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getTempBanIP() != -1) {
            commands.add(new CMDTempBanIP(instance, "tempbanip"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getTempMute() != -1) {
            commands.add(new CMDTempMute(instance, "tempmute"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getUnban() != -1) {
            commands.add(new CMDUnban(instance, "unban"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getUnbanIP() != -1) {
            commands.add(new CMDUnbanIP(instance, "unbanip"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getUnmute() != -1) {
            commands.add(new CMDUnmute(instance, "unmute"));
        }

        if (instance.getConfigs().getMainConfig().getPermissions().getCommandPermissions().getWarn() != -1) {
            commands.add(new CMDWarn(instance, "warn"));
        }
    }
}
