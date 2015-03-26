package pro.zackpollard.bungeeutil.json.config;

public class GSONChatPermissions {

    private final int receiveReports;

    private final int receiveBanAlerts;
    private final int receiveTempBanAlerts;
    private final int receiveUnBanAlerts;
    private final int receiveIPBanAlerts;
    private final int receiveIPTempBanAlerts;
    private final int receiveIPUnBanAlerts;
    private final int receiveKickAlerts;
    private final int receiveMuteAlerts;
    private final int receiveTempMuteAlerts;
    private final int receiveUnMuteAlerts;
    private final int receiveWarnAlerts;

    public GSONChatPermissions() {

        receiveReports = 1;

        receiveBanAlerts = 1;
        receiveTempBanAlerts = 1;
        receiveUnBanAlerts = 1;
        receiveIPBanAlerts = 1;
        receiveIPTempBanAlerts = 1;
        receiveIPUnBanAlerts = 1;
        receiveKickAlerts = 1;
        receiveMuteAlerts = 1;
        receiveTempMuteAlerts = 1;
        receiveUnMuteAlerts = 1;
        receiveWarnAlerts = 1;
    }

    public int getReceiveReports() {
        return receiveReports;
    }

    public int getReceiveBanAlerts() {
        return receiveBanAlerts;
    }

    public int getReceiveKickAlerts() {
        return receiveKickAlerts;
    }

    public int getReceiveMuteAlerts() {
        return receiveMuteAlerts;
    }

    public int getReceiveIPBanAlerts() {
        return receiveIPBanAlerts;
    }

    public int getReceiveTempBanAlerts() {
        return receiveTempBanAlerts;
    }

    public int getReceiveTempMuteAlerts() {
        return receiveTempMuteAlerts;
    }

    public int getReceiveIPTempBanAlerts() {
        return receiveIPTempBanAlerts;
    }

    public int getReceiveUnBanAlerts() {
        return receiveUnBanAlerts;
    }

    public int getReceiveIPUnBanAlerts() {
        return receiveIPUnBanAlerts;
    }

    public int getReceiveUnMuteAlerts() {
        return receiveUnMuteAlerts;
    }

    public int getReceiveWarnAlerts() {
        return receiveWarnAlerts;
    }
}
