package pro.zackpollard.bungeeutil.json.config;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class GSONAdvertisementBlock {

    private final String ipBlockRegex;
    private final String domainBlockRegex;

    private transient Pattern ipBlockPattern;
    private transient Pattern domainBlockPattern;

    private final Set<String> domainBlockExceptions;
    private final Set<String> ipBlockExceptions;

    private transient boolean domainConvertedToLower;

    public GSONAdvertisementBlock() {

        ipBlockRegex = "((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,-:; ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))";
        domainBlockRegex = "[-a-zA-Z0-9@:%_\\\\+.~#?&//=]{2,256}\\\\.[a-z]{2,4}\\\\b(\\\\/[-a-zA-Z0-9@:%_\\\\+~#?&//=]*)?";

        domainBlockExceptions = new HashSet<>();
        domainBlockExceptions.add("example.com");
        domainBlockExceptions.add("youtube.com");

        ipBlockExceptions = new HashSet<>();
        ipBlockExceptions.add("127.0.0.1");
        ipBlockExceptions.add("192.168.0.1");

        domainConvertedToLower = false;
    }

    public Pattern getIPBlockRegex() {

        if (ipBlockPattern == null) {

            ipBlockPattern = Pattern.compile(ipBlockRegex);
        }

        return ipBlockPattern;
    }

    public Pattern getDomainBlockRegex() {

        if (domainBlockPattern == null) {

            domainBlockPattern = Pattern.compile(domainBlockRegex);
        }

        return domainBlockPattern;
    }

    public Set<String> getDomainBlockExceptions() {

        return domainBlockExceptions;
    }

    public Set<String> getIpBlockExceptions() {

        return ipBlockExceptions;
    }

    public boolean matchesBlocks(String message) {

        if (getDomainBlockRegex().matcher(message).matches() || getIPBlockRegex().matcher(message).matches()) {

            for (String string : new HashSet<>(domainBlockExceptions)) {

                if (!domainConvertedToLower) {

                    domainBlockExceptions.remove(string);
                    string = string.toLowerCase();
                    domainBlockExceptions.add(string);
                }

                if (message.toLowerCase().contains(string)) {

                    return false;
                }
            }

            for (String string : ipBlockExceptions) {

                if (message.contains(string)) {

                    return false;
                }
            }

            domainConvertedToLower = true;

            return true;
        }

        return false;
    }
}