package pro.zackpollard.bungeeutil.json.config;

/**
 * @Author zack
 * @Date 27/02/15.
 */
public class GSONReplaceWords {

    private final String blocked;
    private final String replacement;
    private final int bypassRole;
    private boolean blockMessage;

    public GSONReplaceWords() {

        blocked = "";
        replacement = "";
        bypassRole = 0;
        blockMessage = false;
    }

    public GSONReplaceWords(String original, String replacement, int bypassRole) {

        this.blocked = original;
        this.replacement = replacement;
        this.bypassRole = bypassRole;
    }

    public String getBlocked() {

        return blocked;
    }

    public String getReplacement() {

        return replacement;
    }

    public int getBypassRole() {

        return bypassRole;
    }

    public boolean isBlockMessage() {

        return blockMessage;
    }
}
