package pro.zackpollard.bungeeutil.json.config;

public class GSONSlowChat {

    private final int secondsBetweenMessage;
    private final int maxMessageSimilarity;

    public GSONSlowChat() {

        secondsBetweenMessage = 6;
        maxMessageSimilarity = 80;
    }

    public int getSecondsBetweenMessage() {

        return secondsBetweenMessage;
    }

    public int getMaxMessageSimilarity() {

        return maxMessageSimilarity;
    }
}
