package asia.ubb.ubbwhitelistcleaner;

import asia.ubb.ubbwhitelistcleaner.task.CleanTask;
import asia.ubb.ubbwhitelistcleaner.utils.TicksUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class UBBWhitelistCleanerPlugin extends JavaPlugin {

    private final File logFile = new File(getDataFolder(), getConfig().getString("logging.file.filename", "log.txt"));

    private void saveLog() {
        try {
            if (!logFile.createNewFile())
                getLogger().warning("Could not save " + logFile.getName() + " because " + logFile.getName() + " already exists.");
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save " + logFile.getName() + ".", e);
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveLog();

        String cleanIntervalStr = getConfig().getString("clean-interval");
        String offlineBeforeCleanStr = getConfig().getString("offline-before-clean");
        if (cleanIntervalStr != null && offlineBeforeCleanStr != null) {
            long cleanInterval = TicksUtils.parseTicks(cleanIntervalStr);
            long offlineBeforeClean = TicksUtils.parseTicks(offlineBeforeCleanStr);
            getLogger().info("Clean Interval (Seconds):         " + cleanInterval / 20);
            getLogger().info("Offline Before Clean (Seconds):   " + offlineBeforeClean / 20);

            getServer().getScheduler().scheduleSyncRepeatingTask(
                    this, new CleanTask(this, offlineBeforeClean, logFile),
                    0L, cleanInterval);
        } else
            getLogger().log(Level.SEVERE, "Could not find option clean-interval or offline-before-clean in config.");
    }

}
