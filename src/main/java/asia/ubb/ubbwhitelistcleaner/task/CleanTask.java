package asia.ubb.ubbwhitelistcleaner.task;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class CleanTask implements Runnable {

    private final Plugin plugin;
    private final long offlineBeforeClean;
    private final File logFile;

    public CleanTask(Plugin plugin, long offlineBeforeClean, File logFile) {
        this.plugin = plugin;
        this.offlineBeforeClean = offlineBeforeClean;
        this.logFile = logFile;
    }

    @Override
    public void run() {
        // check if whitelist cleaner is enabled
        if (plugin.getConfig().getBoolean("cleaner.enable", false)) {
            try {
                // open log file
                FileWriter logFileWriter = new FileWriter(logFile, true);

                // get current date and time
                Date now = new Date();
                // loop through all whitelisted players
                for (OfflinePlayer player : plugin.getServer().getWhitelistedPlayers()) {
                    // get last played time
                    long lastPlayed = player.getLastPlayed();
                    // skip the player if the player has never played before
                    if (plugin.getConfig().getBoolean("cleaner.remove-never-played", false))
                        if (lastPlayed == 0)
                            continue;
                    // calculate player offline time
                    long offlineTime = (now.getTime() - lastPlayed) / 1000 * 20;

                    // check if player offline time is longer than allowed and if player is not online
                    if (offlineTime >= offlineBeforeClean && !player.isOnline()) {
                        // remove player from whitelist
                        player.setWhitelisted(false);
                        // reload whitelist
                        plugin.getServer().reloadWhitelist();

                        // check if file logging is enabled
                        if (plugin.getConfig().getBoolean("logging.file.enable", false)) {
                            // convert current time to string
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            logFileWriter.write("[" + sdf.format(now) + "] " +
                                    player.getName() + " (" + player.getUniqueId() + ")" + System.lineSeparator());
                        }
                    }
                }

                // close log file
                logFileWriter.close();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not open or write to " + logFile.getName() + ".", e);
            }
        }
    }

}
