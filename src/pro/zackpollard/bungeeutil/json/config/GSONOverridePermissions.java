package pro.zackpollard.bungeeutil.json.config;

public class GSONOverridePermissions {

    private final int overrideDisabledPrivateMessaging;
    private final int bypassMaintenanceMode;

    public GSONOverridePermissions() {

        overrideDisabledPrivateMessaging = 1;
        bypassMaintenanceMode = 2;
    }

    public int getOverrideDisabledPrivateMessaging() {

        return overrideDisabledPrivateMessaging;
    }

    public int getBypassMaintenanceMode() {

        return bypassMaintenanceMode;
    }
}
