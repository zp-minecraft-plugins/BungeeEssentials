package pro.zackpollard.bungeeutil.json.config;

public class GSONPermissions {

    private final GSONCommandPermissions commands;

    private final GSONChatPermissions chat;

    private final GSONOverridePermissions overrides;

    public GSONPermissions() {

        commands = new GSONCommandPermissions();
        chat = new GSONChatPermissions();
        overrides = new GSONOverridePermissions();
    }

    public GSONCommandPermissions getCommandPermissions() {

        return commands;
    }

    public GSONChatPermissions getChatPermissions() {

        return chat;
    }

    public GSONOverridePermissions getOverridePermissions() {

        return overrides;
    }
}