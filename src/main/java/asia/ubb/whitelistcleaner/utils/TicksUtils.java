package asia.ubb.whitelistcleaner.utils;

public class TicksUtils {

    private TicksUtils() {
    }

    public static long parseTicks(String from) {
        StringBuilder tmp = new StringBuilder();
        long ticks = 0;

        for (char c : from.toCharArray()) {
            if (Character.isDigit(c)) {
                tmp.append(c);
            } else {
                long charInLong = Long.parseLong(tmp.toString().trim());
                switch (c) {
                    case 'y':
                        ticks += charInLong * 31556952 * 20;
                        break;

                    case 'M':
                        ticks += charInLong * 2629746 * 20;
                        break;

                    case 'd':
                        ticks += charInLong * 86400 * 20;
                        break;

                    case 'h':
                        ticks += charInLong * 3600 * 20;
                        break;

                    case 'm':
                        ticks += charInLong * 60 * 20;
                        break;

                    case 's':
                        ticks += charInLong * 20;
                        break;

                    default:
                        break;
                }
                tmp.setLength(0);
            }
        }

        return ticks;
    }

}
