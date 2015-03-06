package pro.zackpollard.bungeeutil.json.config;

public class GSONPermissions {

    private final GSONCommandPermissions commands;

    private final GSONChatPermissions chat;

    public GSONPermissions() {

        commands = new GSONCommandPermissions();
        chat = new GSONChatPermissions();
    }

    public GSONCommandPermissions getCommandPermissions() {

        return commands;
    }

    public GSONChatPermissions getChatPermissions() {

        return chat;
    }
}