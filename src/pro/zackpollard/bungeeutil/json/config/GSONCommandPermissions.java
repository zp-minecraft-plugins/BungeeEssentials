package pro.zackpollard.bungeeutil.json.config;

public class GSONCommandPermissions {

    private final int accounts;
    private final int addProof;
    private final int alts;
    private final int ban;
    private final int banIP;
    private final int bans;
    private final int chatLock;
    private final int chatClear;
    private final int creload;
    private final int crestart;
    private final int kick;
    private final int login;
    private final int message;
    private final int messageToggle;
    private final int mute;
    private final int onlineStaff;
    private final int register;
    private final int removeRole;
    private final int reply;
    private final int report;
    private final int search;
    private final int setRole;
    private final int slowChat;
    private final int socialSpy;
    private final int staff;
    private final int staffChat;
    private final int staffHelp;
    private final int staffVanish;
    private final int tempBan;
    private final int tempBanIP;
    private final int tempMute;
    private final int unban;
    private final int unbanIP;
    private final int unmute;
    private final int warn;
    private final int debugInfo;


    public GSONCommandPermissions() {

        accounts = 3;
        addProof = 1;
        alts = 3;
        ban = 2;
        banIP = 3;
        bans = 2;
        chatLock = 2;
        chatClear = 2;
        creload = 4;
        crestart = 4;
        debugInfo = 4;
        kick = 1;
        login = 0;
        mute = 1;
        onlineStaff = 0;
        register = 0;
        removeRole = 4;
        reply = 0;
        report = 0;
        message = 0;
        messageToggle = 0;
        search = 2;
        setRole = 4;
        slowChat = 1;
        socialSpy = 2;
        staff = 1;
        staffChat = 1;
        staffHelp = 1;
        staffVanish = 1;
        tempBan = 2;
        tempBanIP = 3;
        tempMute = 1;
        unban = 4;
        unbanIP = 4;
        unmute = 1;
        warn = 1;
    }

    public int getAlts() {
        return this.alts;
    }

    public int getBan() {
        return this.ban;
    }

    public int getBanIP() {
        return this.banIP;
    }

    public int getKick() {
        return this.kick;
    }

    public int getMute() {
        return this.mute;
    }

    public int getRemoveRole() {
        return this.removeRole;
    }

    public int getSearch() {
        return this.search;
    }

    public int getStaffChat() {
        return this.staffChat;
    }

    public int getSetRole() {
        return this.setRole;
    }

    public int getTempBan() {
        return this.tempBan;
    }

    public int getTempBanIP() {
        return this.tempBanIP;
    }

    public int getTempMute() {
        return this.tempMute;
    }

    public int getUnban() {
        return this.unban;
    }

    public int getUnbanIP() {
        return this.unbanIP;
    }

    public int getUnmute() {
        return this.unmute;
    }

    public int getMessage() {
        return message;
    }

    public int getSocialSpy() {

        return socialSpy;
    }

    public int getChatLock() {

        return chatLock;
    }

    public int getCReload() {

        return creload;
    }

    public int getReply() {

        return reply;
    }

    public int getChatClear() {

        return chatClear;
    }

    public int getStaff() {

        return staff;
    }

    public int getOnlineStaff() {

        return onlineStaff;
    }

    public int getStaffVanish() {
        return staffVanish;
    }

    public int getAccounts() {

        return accounts;
    }

    public int getCRestart() {

        return crestart;
    }

    public int getWarn() {

        return warn;
    }

    public int getStaffHelp() {

        return staffHelp;
    }

    public int getBanlist() {

        return bans;
    }

    public int getSlowChat() {
        return slowChat;
    }

    public int getReport() {
        return report;
    }

    public int getMessageToggle() {
        return messageToggle;
    }

    public int getDebugInfo() {
        return debugInfo;
    }

    public int getAddProof() {
        return addProof;
    }

    public int getRegister() {
        return register;
    }

    public int getLogin() {
        return login;
    }
}