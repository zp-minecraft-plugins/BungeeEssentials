package pro.zackpollard.bungeeutil.json.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GSONMessages {

    private static final Pattern PATTERN_BANNER = Pattern.compile("%banner%", Pattern.LITERAL);
    private static final Pattern PATTERN_TIMESTAMP = Pattern.compile("%timestamp%", Pattern.LITERAL);
    private static final Pattern PATTERN_TIMEREMAINING = Pattern.compile("%timeremaining%", Pattern.LITERAL);
    private static final Pattern PATTERN_KICKER = Pattern.compile("%kicker%", Pattern.LITERAL);
    private static final Pattern PATTERN_MUTER = Pattern.compile("%muter%", Pattern.LITERAL);
    private static final Pattern PATTERN_PLAYERNAME = Pattern.compile("%playername%", Pattern.LITERAL);
    private static final Pattern PATTERN_ROLENAME = Pattern.compile("%rolename%", Pattern.LITERAL);
    private static final Pattern PATTERN_REASON = Pattern.compile("%reason%", Pattern.LITERAL);
    private static final Pattern PATTERN_DURATION = Pattern.compile("%duration%", Pattern.LITERAL);
    private static final Pattern PATTERN_IPADDRESS = Pattern.compile("%ipaddress%", Pattern.LITERAL);
    private static final Pattern PATTERN_SENDER = Pattern.compile("%sender%", Pattern.LITERAL);
    private static final Pattern PATTERN_MESSAGE = Pattern.compile("%message%", Pattern.LITERAL);
    private static final Pattern PATTERN_RECEIVER = Pattern.compile("%receiver%", Pattern.LITERAL);
    private static final Pattern PATTERN_SERVERNAME = Pattern.compile("%servername%", Pattern.LITERAL);
    private static final Pattern PATTERN_BLOCKEDWORD = Pattern.compile("%blockedword%", Pattern.LITERAL);
    private static final Pattern PATTERN_SIMILARITY = Pattern.compile("%similarity%", Pattern.LITERAL);
    private static final Pattern PATTERN_REPORTER = Pattern.compile("%reporter%", Pattern.LITERAL);
    private static final Pattern PATTERN_REPORTEE = Pattern.compile("%reportee%", Pattern.LITERAL);
    private static final Pattern PATTERN_URL = Pattern.compile("%url%", Pattern.LITERAL);

    private final String prefix;
    private final String timeStampFormat;
    private final String banRemainingDurationFormat;
    private final String muteRemainingDurationFormat;
    private final String totalOnlineTimeFormat;
    private final String defaultBanReason;
    private final String defaultKickReason;
    private final String defaultMuteReason;
    private final String warningMessageFormat;
    private final String playerPermBanMessage;
    private final String playerTempBanMessage;
    private final String playerPermMuteMessage;
    private final String playerTempMuteMessage;
    private final String playerKickMessage;
    private final String IPPermBanMessage;
    private final String IPTempBanMessage;
    private final String staffTempBanWhilePermBanned;
    private final String staffTempBanWhileLongerTempBanExists;
    private final String staffTempMuteWhilePermMuted;
    private final String staffTempMuteWhileLongerTempMuteExists;
    private final String commandPermissionDenied;
    private final String cmdAddProofSuccess;
    private final String cmdSetRoleSuccess;
    private final String cmdRemoveRoleSuccess;
    private final String cmdBanPlayerSuccess;
    private final String cmdBanIPSuccess;
    private final String cmdKickSuccess;
    private final String cmdMuteSuccess;
    private final String cmdWarnSuccess;
    private final String cmdReportSent;
    private final String cmdServerChatLocked;
    private final String cmdServerChatUnlocked;
    private final String cmdSlowChatEnabled;
    private final String cmdSlowChatDisabled;
    private final String cmdTempBanPlayerSuccess;
    private final String cmdTempBanIPSuccess;
    private final String cmdTempMuteSuccess;
    private final String cmdUnbanPlayerSuccess;
    private final String cmdUnbanIPSuccess;
    private final String cmdUnmuteSuccess;
    private final String cmdPlayerUnmuted;
    private final String cmdStaffChatEnabled;
    private final String cmdStaffChatDisabled;
    private final String staffChatFormat;
    private final String privateChatMessageReceivedFormat;
    private final String privateChatMessageSentFormat;
    private final String privateChatMessagingDisabled;
    private final String socialSpyMessageFormat;
    private final String cmdSocialSpyEnabled;
    private final String cmdSocialSpyDisabled;
    private final String chatLockedMessage;
    private final String cmdStaffVanishEnabled;
    private final String cmdStaffVanishDisabled;
    private final String messageBlockedFromBannedWord;
    private final String slowChatMessage;
    private final String chatSimilarityBlocked;
    private final String reportMessage;
    private final String advertisingBlocked;
    private final String cmdMessageToggleDisabled;
    private final String cmdMessageToggleEnabled;

    public GSONMessages() {

        prefix = "%3[%9BungeeUtils%3]%c - ";

        timeStampFormat = "dd/MM/yyyy - HH:mm:ss";

        banRemainingDurationFormat = "Days: %dd% - %hh%:%mm%:%ss%";

        muteRemainingDurationFormat = "Days: %dd% - %hh%:%mm%:%ss%";

        totalOnlineTimeFormat = "Weeks: %ww% - Days: %dd% - %hh%:%mm%:%ss%";

        defaultBanReason = "%cPlease go to our website to appeal your ban!";

        defaultKickReason = "%cReason not specified!";

        defaultMuteReason = "%cReason not specified";

        playerPermBanMessage = "%cYou have been permbanned by %banner%!\nDate of ban: %timestamp%\nBan reason: %reason%";

        playerTempBanMessage = "%cYou have been tempbanned by %banner%!\nDate of ban: %timestamp%\nBan time remaining: %timeremaining%\nBan reason: %reason%";

        playerPermMuteMessage = "%cYou have been permmuted by %muter%!\n" + "Date of mute: %timestamp%\n" + "Mute reason: %reason%";

        playerTempMuteMessage = "%cYou have been tempmuted by %muter%\nDate of mute: %timestamp%\nMute time remaining: %timeremaining%\nMute reason: %reason%";

        playerKickMessage = "%cYou have been kicked by %kicker%!\nKick reason: %reason%";

        IPPermBanMessage = "%cYour IP has been permbanned by %banner%!\nDate of ban: %timestamp%\nBan reason: %reason%";

        IPTempBanMessage = "%cYour IP has been tempbanned by %banner%!Date of ban: %timestamp%\nBan time remaining: %timeremaining%\nBan reason: %reason%";

        staffTempBanWhilePermBanned = "%cYou are not permitted to tempban this player as there is already a ban in place!";

        staffTempBanWhileLongerTempBanExists = "%cYou cannot tempban a player while a longer tempban already exists!";

        staffTempMuteWhilePermMuted = "%cYou are not permitted to tempmute this player as there is already a mute in place!";

        staffTempMuteWhileLongerTempMuteExists = "%cYou cannot tempmute this player while a longer tempmute already exists!";

        commandPermissionDenied = "%cYou cannot use this command as you do not have the required permissions!";

        cmdAddProofSuccess = "%aYou have added the following url to %l%playername%'s%r%a ban: %url%";

        cmdMessageToggleDisabled = "%cYou have %ldisabled private messaging and now you will not be able to be messaged by other players!";

        cmdMessageToggleEnabled = "%cYou have %lenabled private messaging and now you will be able to be messaged by other players!";

        cmdSetRoleSuccess = "%a%l%playername%%r%a was given the role %l%rolename%!";

        cmdRemoveRoleSuccess = "%a%l%playername%'s%r%a role was removed!";

        cmdBanPlayerSuccess = "%a%l%playername%%r%a was banned with reason %reason%!";

        cmdBanIPSuccess = "%a%l%ipaddress%%r%a was banned with reason %reason%!";

        cmdKickSuccess = "%a%l%playername%%r%a was kicked with reason %reason%!";

        cmdMuteSuccess = "%a%l%playername%%r%a was muted with reason %reason%!";

        cmdWarnSuccess = "%a%l%playername%%r%a was warned with reason %reason%!";

        cmdReportSent = "%aYour report has been sent and the moderators will look at it shortly!";

        cmdServerChatLocked = "%aThe chat on the server %l%servername%%r%a was %llocked!";

        cmdServerChatUnlocked = "%aThe chat on the server %l%servername%%r%a was %lunlocked!";

        cmdSlowChatEnabled = "%aThe chat on the server %l%servername%%r%a has been set to %lslow mode!";

        cmdSlowChatDisabled = "%aThe chat on the server %l%servername%%r%a has been set to %lnormal mode!";

        cmdStaffChatEnabled = "%aStaff chat has been %lenabled%r%a for you!";

        cmdStaffChatDisabled = "%aStaff chat has been %ldisabled%r%a for you!";

        cmdStaffVanishEnabled = "%aYou have been %lvanished%r%a from the /onlinestaff list!";

        cmdStaffVanishDisabled = "%aYou have been %lun-vanished%r%a from the /onlinestaff list!";

        cmdTempBanPlayerSuccess = "%a%playername% was %ltempbanned%r%a with reason %reason% for %duration%!";

        cmdTempBanIPSuccess = "%a%ipaddress% was %ltempbanned%r%a with reason %reason% for %duration%!";

        cmdTempMuteSuccess = "%a%playername% was %ltempmuted%r%a with reason %reason% for %duration%!";

        cmdUnbanPlayerSuccess = "%a%playername% was unbanned!";

        cmdUnbanIPSuccess = "%a%ipaddress% was unbanned!";

        cmdUnmuteSuccess = "%a%playername% was unmuted!";

        cmdPlayerUnmuted = "%aYou have been unmuted!";

        staffChatFormat = "%2[%3StaffChat%2]%r - %c%sender%%f - %9%message%";

        privateChatMessageReceivedFormat = "%eMessage from %sender%%c: %f%message%";

        privateChatMessageSentFormat = "%eMessage sent to %receiver%%c: %f%message%";

        socialSpyMessageFormat = "%4[%3SocialSpy%4]%e - %eFrom %l%sender%%r%e to %l%receiver%%f: %message%";

        cmdSocialSpyEnabled = "%aSocial Spy has been enabled for you!";

        cmdSocialSpyDisabled = "%aSocial Spy has been disabled for you!";

        chatLockedMessage = "%aThe chat has been locked on %servername%!";

        warningMessageFormat = "%c%l%sender% sent you a warning with reason %reason%";

        messageBlockedFromBannedWord = "%cYour message was blocked as you included the blocked word %l%blockedword%";

        slowChatMessage = "%cYour message was blocked because slow chat is active on %l%servername%! You can chat again in %l%timeremaining% seconds!";

        chatSimilarityBlocked = "%cYour message was blocked because it was to similar to your last message! Current max similarity is %similarity%%!";

        reportMessage = "%2[%3Report%2]%r - %eFrom %l%reporter% about %l%reportee%%f: %message%";

        advertisingBlocked = "%cDo not advertise on this server!";

        privateChatMessagingDisabled = "%cYou can't send a message to this player as they have private messaging disabled!";
    }

    public String getPrefix() {
        return this.prefix + ChatColor.RESET;
    }

    public String getTimeStampFormat() {
        return this.timeStampFormat;
    }

    public String getBanRemainingDurationFormat() {
        return this.banRemainingDurationFormat;
    }

    public String getDefaultBanReason() {

        return defaultBanReason;
    }

    public String getDefaultKickReason() {

        return defaultKickReason;
    }

    public String getMuteRemainingDurationFormat() {

        return muteRemainingDurationFormat;
    }

    public String getDefaultMuteReason() {

        return defaultMuteReason;
    }

    public String getTotalOnlineTimeFormat() {

        return totalOnlineTimeFormat;
    }

    public BaseComponent[] getPlayerPermBanMessage(String bannerName, String timestamp, String reason) {

        return generateMessage(false,
                PATTERN_BANNER.matcher(
                        PATTERN_TIMESTAMP.matcher(
                                PATTERN_REASON.matcher(playerPermBanMessage).replaceAll(Matcher.quoteReplacement(reason))
                        ).replaceAll(Matcher.quoteReplacement(timestamp))
                ).replaceAll(Matcher.quoteReplacement(bannerName))
        );
    }

    public BaseComponent[] getPlayerTempBanMessage(String bannerName, String timestamp, String reason, String timeRemaining) {

        return generateMessage(false,
                PATTERN_BANNER.matcher(
                        PATTERN_TIMESTAMP.matcher(
                                PATTERN_REASON.matcher(
                                        PATTERN_TIMEREMAINING.matcher(playerTempBanMessage).replaceAll(Matcher.quoteReplacement(timeRemaining))
                                ).replaceAll(Matcher.quoteReplacement(reason))
                        ).replaceAll(Matcher.quoteReplacement(timestamp))
                ).replaceAll(Matcher.quoteReplacement(bannerName))
        );
    }

    public BaseComponent[] getIPPermBanMessage(String bannerName, String timestamp, String reason) {

        return generateMessage(false,
                PATTERN_BANNER.matcher(
                        PATTERN_TIMESTAMP.matcher(
                                PATTERN_REASON.matcher(IPPermBanMessage).replaceAll(Matcher.quoteReplacement(reason))
                        ).replaceAll(Matcher.quoteReplacement(timestamp))
                ).replaceAll(Matcher.quoteReplacement(bannerName))
        );
    }

    public BaseComponent[] getIPTempBanMessage(String bannerName, String timestamp, String reason, String timeRemaining) {

        return generateMessage(false,
                PATTERN_BANNER.matcher(
                        PATTERN_TIMESTAMP.matcher(
                                PATTERN_REASON.matcher(
                                        PATTERN_TIMEREMAINING.matcher(IPTempBanMessage).replaceAll(Matcher.quoteReplacement(timeRemaining))
                                ).replaceAll(Matcher.quoteReplacement(reason))
                        ).replaceAll(Matcher.quoteReplacement(timestamp))
                ).replaceAll(Matcher.quoteReplacement(bannerName))
        );
    }

    public BaseComponent[] getStaffTempBanWhilePermBanned() {

        return generateMessage(false, staffTempBanWhilePermBanned);
    }

    public BaseComponent[] getStaffTempBanWhileLongerTempBanExists() {

        return generateMessage(false, staffTempBanWhileLongerTempBanExists);
    }

    public BaseComponent[] getCommandPermissionDenied() {

        return generateMessage(true, commandPermissionDenied);
    }

    public BaseComponent[] getPlayerKickMessage(String kickerName, String reason) {

        return generateMessage(false,
                PATTERN_KICKER.matcher(
                        PATTERN_REASON.matcher(playerKickMessage).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(kickerName))
        );
    }

    public BaseComponent[] getStaffTempMuteWhilePermMuted() {

        return generateMessage(true, staffTempMuteWhilePermMuted);
    }

    public BaseComponent[] getStaffTempMuteWhileLongerTempMuteExists() {

        return generateMessage(true, staffTempMuteWhileLongerTempMuteExists);
    }

    public BaseComponent[] getPlayerTempMuteMessage(String muterName, String timestamp, String reason, String timeRemaining) {

        return generateMessage(false,
                PATTERN_MUTER.matcher(
                        PATTERN_TIMESTAMP.matcher(
                                PATTERN_REASON.matcher(
                                        PATTERN_TIMEREMAINING.matcher(playerTempMuteMessage).replaceAll(Matcher.quoteReplacement(timeRemaining))
                                ).replaceAll(Matcher.quoteReplacement(reason))
                        ).replaceAll(Matcher.quoteReplacement(timestamp))
                ).replaceAll(Matcher.quoteReplacement(muterName))
        );
    }

    public BaseComponent[] getPlayerPermMuteMessage(String muterName, String timestamp, String reason) {

        return generateMessage(false,
                PATTERN_MUTER.matcher(
                        PATTERN_TIMESTAMP.matcher(
                                PATTERN_REASON.matcher(playerPermMuteMessage).replaceAll(Matcher.quoteReplacement(reason))
                        ).replaceAll(Matcher.quoteReplacement(timestamp))
                ).replaceAll(Matcher.quoteReplacement(muterName))
        );
    }

    public BaseComponent[] getCmdSetRoleSuccess(String playerName, String roleName) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(
                        PATTERN_ROLENAME.matcher(cmdSetRoleSuccess).replaceAll(Matcher.quoteReplacement(roleName))
                ).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdRemoveRoleSuccess(String playerName) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(cmdRemoveRoleSuccess).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdBanPlayerSuccess(String playerName, String reason) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(
                        PATTERN_REASON.matcher(cmdBanPlayerSuccess).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdBanIPSuccess(String ipAddress, String reason) {

        return generateMessage(true,
                PATTERN_IPADDRESS.matcher(
                        PATTERN_REASON.matcher(cmdBanIPSuccess).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(ipAddress))
        );
    }

    public BaseComponent[] getCmdKickSuccess(String playerName, String reason) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(
                        PATTERN_REASON.matcher(cmdKickSuccess).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdMuteSuccess(String playerName, String reason) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(
                        PATTERN_REASON.matcher(cmdMuteSuccess).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdTempBanPlayerSuccess(String playerName, String reason, String duration) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(
                        PATTERN_REASON.matcher(
                                PATTERN_DURATION.matcher(cmdTempBanPlayerSuccess).replaceAll(Matcher.quoteReplacement(duration))
                        ).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdTempBanIPSuccess(String ipAddress, String reason, String duration) {

        return generateMessage(true,
                PATTERN_IPADDRESS.matcher(
                        PATTERN_REASON.matcher(
                                PATTERN_DURATION.matcher(cmdTempBanIPSuccess)
                                        .replaceAll(Matcher.quoteReplacement(duration))
                        ).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(ipAddress))
        );
    }

    public BaseComponent[] getCmdTempMuteSuccess(String playerName, String reason, String duration) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(
                        PATTERN_REASON.matcher(
                                PATTERN_DURATION.matcher(cmdTempMuteSuccess).replaceAll(Matcher.quoteReplacement(duration))
                        ).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdUnbanPlayerSuccess(String playerName) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(cmdUnbanPlayerSuccess).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdUnbanIPSuccess(String ipAddress) {

        return generateMessage(true,
                PATTERN_IPADDRESS.matcher(cmdUnbanIPSuccess).replaceAll(Matcher.quoteReplacement(ipAddress))
        );

    }

    public BaseComponent[] getCmdUnmuteSuccess(String playerName) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(cmdUnmuteSuccess).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdPlayerUnmuted() {

        return generateMessage(true, cmdPlayerUnmuted);
    }

    public BaseComponent[] getStaffChatFormat(String sender, String message) {

        return generateMessage(false,
                PATTERN_SENDER.matcher(
                        PATTERN_MESSAGE.matcher(staffChatFormat).replaceAll(Matcher.quoteReplacement(message))
                ).replaceAll(Matcher.quoteReplacement(sender))
        );
    }

    public BaseComponent[] getCmdStaffChatEnabled() {

        return generateMessage(true, cmdStaffChatEnabled);
    }

    public BaseComponent[] getCmdStaffChatDisabled() {

        return generateMessage(true, cmdStaffChatDisabled);
    }

    public BaseComponent[] getPrivateChatMessageReceivedFormat(String sender, String message) {

        return generateMessage(false,
                PATTERN_SENDER.matcher(
                        PATTERN_MESSAGE.matcher(privateChatMessageReceivedFormat).replaceAll(Matcher.quoteReplacement(message))
                ).replaceAll(Matcher.quoteReplacement(sender))
        );
    }

    public BaseComponent[] getPrivateChatMessageSentFormat(String receiver, String message) {

        return generateMessage(false,
                PATTERN_RECEIVER.matcher(
                        PATTERN_MESSAGE.matcher(privateChatMessageSentFormat).replaceAll(Matcher.quoteReplacement(message))
                ).replaceAll(Matcher.quoteReplacement(receiver))
        );
    }

    public BaseComponent[] getSocialSpyMessageFormat(String sender, String receiver, String message) {

        return generateMessage(false,
                PATTERN_SENDER.matcher(
                        PATTERN_RECEIVER.matcher(
                                PATTERN_MESSAGE.matcher(socialSpyMessageFormat).replaceAll(Matcher.quoteReplacement(message))
                        ).replaceAll(Matcher.quoteReplacement(receiver))
                ).replaceAll(Matcher.quoteReplacement(sender))
        );
    }

    public BaseComponent[] getCmdSocialSpyEnabled() {

        return generateMessage(true, cmdSocialSpyEnabled);
    }

    public BaseComponent[] getCmdSocialSpyDisabled() {

        return generateMessage(true, cmdSocialSpyDisabled);
    }

    public BaseComponent[] getChatLockedMessage(String serverName) {

        return generateMessage(true,
                PATTERN_SERVERNAME.matcher(chatLockedMessage).replaceAll(Matcher.quoteReplacement(serverName))
        );
    }

    public BaseComponent[] getCmdServerChatLocked(String serverName) {

        return generateMessage(true,
                PATTERN_SERVERNAME.matcher(cmdServerChatLocked).replaceAll(Matcher.quoteReplacement(serverName))
        );
    }

    public BaseComponent[] getCmdServerChatUnlocked(String serverName) {

        return generateMessage(true,
                PATTERN_SERVERNAME.matcher(cmdServerChatUnlocked).replaceAll(Matcher.quoteReplacement(serverName))
        );
    }

    public BaseComponent[] getCmdStaffVanishEnabled() {

        return generateMessage(true, cmdStaffVanishEnabled);
    }

    public BaseComponent[] getCmdStaffVanishedDisabled() {

        return generateMessage(true, cmdStaffVanishDisabled);
    }

    public BaseComponent[] getWarningMessage(String sender, String reason) {

        return generateMessage(true,
                PATTERN_SENDER.matcher(
                        PATTERN_REASON.matcher(warningMessageFormat).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(sender))
        );
    }

    public BaseComponent[] getCmdWarnSuccess(String playerName, String reason) {

        return generateMessage(true,
                PATTERN_PLAYERNAME.matcher(
                        PATTERN_REASON.matcher(cmdWarnSuccess).replaceAll(Matcher.quoteReplacement(reason))
                ).replaceAll(Matcher.quoteReplacement(playerName))
        );
    }

    public BaseComponent[] getCmdSlowChatEnabled(String serverName) {

        return generateMessage(true,
                PATTERN_SERVERNAME.matcher(cmdSlowChatEnabled).replaceAll(Matcher.quoteReplacement(serverName))
        );
    }

    public BaseComponent[] getCmdSlowChatDisabled(String serverName) {

        return generateMessage(true,
                PATTERN_SERVERNAME.matcher(cmdSlowChatDisabled).replaceAll(Matcher.quoteReplacement(serverName))
        );
    }

    public BaseComponent[] getMessageBlockedFromBannedWord(String blockedWord) {

        return generateMessage(true,
                PATTERN_BLOCKEDWORD.matcher(messageBlockedFromBannedWord).replaceAll(Matcher.quoteReplacement(blockedWord))
        );
    }

    public BaseComponent[] getSlowChatMessage(String serverName, String timeRemaining) {

        return generateMessage(true,
                PATTERN_SERVERNAME.matcher(
                        PATTERN_TIMEREMAINING.matcher(slowChatMessage).replaceAll(Matcher.quoteReplacement(timeRemaining))
                ).replaceAll(Matcher.quoteReplacement(serverName))
        );
    }

    public BaseComponent[] getChatSimilarityBlocked(int maxMessageSimilarity) {

        return generateMessage(true,
                PATTERN_SIMILARITY.matcher(chatSimilarityBlocked).replaceAll(String.valueOf(maxMessageSimilarity))
        );
    }

    public BaseComponent[] getAdvertisingBlocked() {

        return generateMessage(true, advertisingBlocked);
    }

    public BaseComponent[] getCmdReportSent() {

        return generateMessage(true, cmdReportSent);
    }

    public BaseComponent[] getReportMessage(String reporter, String reportee, String message) {

        return generateMessage(false,
                PATTERN_REPORTER.matcher(
                        PATTERN_REPORTEE.matcher(
                                PATTERN_MESSAGE.matcher(reportMessage).replaceAll(Matcher.quoteReplacement(message))
                        ).replaceAll(Matcher.quoteReplacement(reportee))
                ).replaceAll(Matcher.quoteReplacement(reporter))
        );
    }

    public BaseComponent[] getCmdMessageToggleDisabled() {

        return generateMessage(false, cmdMessageToggleDisabled);
    }

    public BaseComponent[] getCmdMessageToggleEnabled() {

        return generateMessage(false, cmdMessageToggleEnabled);
    }

    public BaseComponent[] getPrivateChatMessagingDisabled(String receiver) {

        return generateMessage(true,
                PATTERN_RECEIVER.matcher(privateChatMessagingDisabled).replaceAll(Matcher.quoteReplacement(receiver))
        );
    }

    public BaseComponent[] getCmdAddProofSuccess(String url) {

        return generateMessage(true,
                PATTERN_URL.matcher(cmdAddProofSuccess).replaceAll(Matcher.quoteReplacement(url))
        );
    }

    public BaseComponent[] generateMessage(boolean prefix, String message) {

        if (prefix) {

            return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('%', this.getPrefix() + message));
        } else {

            return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('%', message));
        }
    }
}