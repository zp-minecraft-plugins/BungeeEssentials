package pro.zackpollard.bungeeutil.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import pro.zackpollard.bungeeutil.BungeeEssentials;

import java.util.regex.Pattern;

/**
 * @Author zack
 * @Date 06/02/15.
 */
public class Utils {

    private static final Pattern PATTERN_WEEK = Pattern.compile("%ww%", Pattern.LITERAL);
    private static final Pattern PATTERN_DAY = Pattern.compile("%dd%", Pattern.LITERAL);
    private static final Pattern PATTERN_HOUR = Pattern.compile("%hh%", Pattern.LITERAL);
    private static final Pattern PATTERN_MIN = Pattern.compile("%mm%", Pattern.LITERAL);
    private static final Pattern PATTERN_SEC = Pattern.compile("%ss%", Pattern.LITERAL);

    public static ProxiedPlayer getOnlinePlayerByPartName(String partName) {

        partName = partName.toLowerCase();

        for (ProxiedPlayer player : BungeeEssentials.getInstance().getProxy().getPlayers()) {

            if (player.getName().toLowerCase().startsWith(partName)) {

                return player;
            }
        }

        return null;
    }

    public static String parseStringTimeInfo(String message, long amountTimeSeconds) {

        amountTimeSeconds = amountTimeSeconds / 1000;

        long weeks = -1;
        long days = -1;
        long hours = -1;
        long minutes = -1;
        long seconds = -1;

        if (message.contains("%ww%")) {

            weeks = amountTimeSeconds / 604800;
            amountTimeSeconds = amountTimeSeconds % 604800;
        }
        if (message.contains("%dd%")) {

            days = amountTimeSeconds / 86400;
            amountTimeSeconds = amountTimeSeconds % 86400;
        }
        if (message.contains("%hh%")) {

            hours = amountTimeSeconds / 3600;
            amountTimeSeconds = amountTimeSeconds % 3600;
        }
        if (message.contains("%mm%")) {

            minutes = amountTimeSeconds / 60;
            amountTimeSeconds = amountTimeSeconds % 60;
        }
        if (message.contains("%ss%")) {

            seconds = amountTimeSeconds;
        }

        String weeksstr = String.valueOf(weeks);
        String daysstr = String.valueOf(days);
        String hoursstr = ((hours < 10) ? "0" : "") + hours;
        String minutesstr = ((minutes < 10) ? "0" : "") + minutes;
        String secondsstr = ((seconds < 10) ? "0" : "") + seconds;

        message = PATTERN_WEEK.matcher(PATTERN_DAY.matcher(PATTERN_HOUR.matcher(PATTERN_MIN.matcher(PATTERN_SEC.matcher(message)
                        .replaceAll(secondsstr))
                        .replaceAll(minutesstr))
                        .replaceAll(hoursstr))
                        .replaceAll(daysstr))
                        .replaceAll(weeksstr);
        return message;
    }


    public static double getStringSimilarity(String s1, String s2) {

        String longer = s1, shorter = s2;

        if (s1.length() < s2.length()) { // longer should always have greater length

            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();

        if (longerLength == 0) {

            return 1.0;
        }

        return (((longerLength - editDistance(longer, shorter)) / (double) longerLength)) * 100;
    }

    private static int editDistance(String s1, String s2) {

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {

            int lastValue = i;

            for (int j = 0; j <= s2.length(); j++) {

                if (i == 0) {

                    costs[j] = j;
                } else {

                    if (j > 0) {

                        int newValue = costs[j - 1];

                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {

                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }

                        costs[j - 1] = lastValue;

                        lastValue = newValue;
                    }
                }
            }

            if (i > 0) {

                costs[s2.length()] = lastValue;
            }
        }

        return costs[s2.length()];
    }
}
