package pro.zackpollard.bungeeutil.json.config;

import java.util.Arrays;
import java.util.List;

/**
 * @Author zack
 * @Date 27/02/15.
 */
public class GSONReplaceWords {

    private String blocked;
    private final String replacement;
    private final int bypassRole;
    private boolean blockMessage;

    private transient boolean converted;

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
        this.blockMessage = false;
        this.converted = false;
    }

    public String getBlocked() {

        if(!converted) {

            blocked = blocked.toLowerCase();
            converted = true;
        }

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

    public String doReplacement(String string) {

        List<String> words = Arrays.asList(string.split(" "));

        for (int i = 0; i < words.size(); i++) {

            if (words.get(i).toLowerCase().equals(this.getBlocked())) {

                words.set(i, this.getReplacement());
            }
        }

        String replacementString = "";

        for (String word : words) {

            replacementString += word + " ";
        }

        return replacementString;
    }
}