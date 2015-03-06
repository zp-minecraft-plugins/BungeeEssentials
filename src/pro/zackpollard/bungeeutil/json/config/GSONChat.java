package pro.zackpollard.bungeeutil.json.config;

import java.util.HashSet;
import java.util.Set;

public class GSONChat {

    private final GSONSlowChat slowChat;
    private final Set<GSONReplaceWords> replaceWords;
    private final GSONAdvertisementBlock advertisingBlocking;

    public GSONChat() {

        slowChat = new GSONSlowChat();

        advertisingBlocking = new GSONAdvertisementBlock();

        replaceWords = new HashSet<>();
        replaceWords.add(new GSONReplaceWords("fuck", "****", 4));
        replaceWords.add(new GSONReplaceWords("crap", "****", 4));
        replaceWords.add(new GSONReplaceWords("shit", "****", 4));
        replaceWords.add(new GSONReplaceWords("dick", "****", 4));
        replaceWords.add(new GSONReplaceWords("asshole", "*******", 4));
    }

    public Set<GSONReplaceWords> getReplaceWords() {

        return replaceWords;
    }

    public GSONSlowChat getSlowChat() {

        return slowChat;
    }

    public GSONAdvertisementBlock getAdvertisingBlocking() {

        return advertisingBlocking;
    }
}