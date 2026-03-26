package com.A2plugin.lsreborn;

import com.A2plugin.lsreborn.util.*;
import com.A2plugin.lsreborn.util.revive.ReviveTaskManager;
import com.zetaplugins.zetacore.ZetaCorePlugin;
import com.zetaplugins.zetacore.services.bStats.Metrics;
import com.zetaplugins.zetacore.services.commands.AutoCommandRegistrar;
import com.zetaplugins.zetacore.services.events.AutoEventRegistrar;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.A2plugin.lsreborn.api.LSRebornAPI;
import com.A2plugin.lsreborn.api.LSRebornAPIImpl;
import com.A2plugin.lsreborn.caches.EliminatedPlayersCache;
import com.A2plugin.lsreborn.caches.OfflinePlayerCache;
import com.A2plugin.lsreborn.util.customblocks.ReviveBeaconEffectManager;
import com.A2plugin.lsreborn.util.customitems.recipe.RecipeManager;
import com.A2plugin.lsreborn.util.geysermc.GeyserManager;
import com.A2plugin.lsreborn.util.geysermc.GeyserPlayerFile;
import com.A2plugin.lsreborn.storage.MariaDBStorage;
import com.A2plugin.lsreborn.storage.MySQLStorage;
import com.A2plugin.lsreborn.storage.Storage;
import com.A2plugin.lsreborn.storage.SQLiteStorage;
import com.A2plugin.lsreborn.util.worldguard.WorldGuardManager;

import java.io.File;
import java.util.List;

public final class LSReborn extends ZetaCorePlugin {
    private static final String PACKAGE_PREFIX = "com.A2plugin.lsreborn";

    private VersionChecker versionChecker;
    private Storage storage;
    private WorldGuardManager worldGuardManager;
    private LanguageManager languageManager;
    private ConfigManager configManager;
    private RecipeManager recipeManager;
    private GeyserManager geyserManager;
    private GeyserPlayerFile geyserPlayerFile;
    private WebHookManager webHookManager;
    private GracePeriodManager gracePeriodManager;
    private BypassManager bypassManager;
    private EliminatedPlayersCache eliminatedPlayersCache;
    private OfflinePlayerCache offlinePlayerCache;
    private AsyncTaskManager asyncTaskManager;
    private ReviveBeaconEffectManager reviveBeaconEffectManager;
    private ReviveTaskManager reviveTaskManager;
    private final boolean hasWorldGuard = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    private final boolean hasGeyser = Bukkit.getPluginManager().getPlugin("floodgate") != null;

    @Override
    public void onLoad() {
        getLogger().info("Loading LSReborn...");

        if (Bukkit.getName().toLowerCase().contains("spigot") || Bukkit.getName().toLowerCase().contains("craftbukkit")) {
            getLogger().severe("---------------------------------------------------");
            getLogger().severe("LSReborn does not support Spigot or Bukkit!");
            getLogger().severe("Please use Paper or any fork of Paper (like Purpur). If you need further assistance, please join our Discord server:");
            getLogger().severe("https://discord.gg/pAAmuNPUrJ");
            getLogger().severe("---------------------------------------------------");
        }

        if (hasWorldGuard()) {
            getLogger().info("WorldGuard found! Enabling WorldGuard support...");
            worldGuardManager = new WorldGuardManager();
            getLogger().info("WorldGuard found! Enabled WorldGuard support!");
        }
    }

    @Override
    public void onEnable() {
        if (hasGeyser()) {
            getLogger().info("Geyser found, enabling Bedrock player support.");
            geyserPlayerFile = new GeyserPlayerFile();
            geyserManager = new GeyserManager();
        }

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        asyncTaskManager = new AsyncTaskManager();
        reviveBeaconEffectManager = new ReviveBeaconEffectManager(this);
        reviveTaskManager = new ReviveTaskManager();

        languageManager = new LanguageManager(this);
        configManager = new ConfigManager(this);

        storage = createPlayerDataStorage();
        storage.init();

        recipeManager = new RecipeManager(this);
        recipeManager.registerRecipes();

        versionChecker = new VersionChecker(this, "fjE4HIKE");
        gracePeriodManager = new GracePeriodManager(this);
        bypassManager = new BypassManager(this);
        webHookManager = new WebHookManager(this);

        eliminatedPlayersCache = new EliminatedPlayersCache(this);
        offlinePlayerCache = new OfflinePlayerCache(this);

        List<String> registeredCommands = new AutoCommandRegistrar(this, PACKAGE_PREFIX).registerAllCommands();
        getLogger().info("Registered " + registeredCommands.size() + " commands");

        List<String> registeredEvents = new AutoEventRegistrar(this, PACKAGE_PREFIX).registerAllListeners();
        getLogger().info("Registered " + registeredEvents.size() + " event listeners");

        initializeBStats();

        if (hasPlaceholderApi()) {
            PapiExpansion papiExpansion = new PapiExpansion(this);
            if (papiExpansion.canRegister()) {
                papiExpansion.register();
                getLogger().info("PlaceholderAPI found! Enabled PlaceholderAPI support!");
            }
        }

        getLogger().info("LSReborn enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Canceling all running tasks...");
        asyncTaskManager.cancelAllTasks();
        reviveBeaconEffectManager.clearAllEffects();
        getLogger().info("LSReborn disabled!");
    }

    public static LSReborn getInstance() {
        return JavaPlugin.getPlugin(LSReborn.class);
    }

    public static LSRebornAPI getAPI() {
        return new LSRebornAPIImpl(getInstance());
    }

    public AsyncTaskManager getAsyncTaskManager() {
        return asyncTaskManager;
    }

    public ReviveBeaconEffectManager getReviveBeaconEffectManager() {
        return reviveBeaconEffectManager;
    }

    public ReviveTaskManager getReviveTaskManager() {
        return reviveTaskManager;
    }

    public VersionChecker getVersionChecker() {
        return versionChecker;
    }

    public Storage getStorage() {
        return storage;
    }

    public EliminatedPlayersCache getEliminatedPlayersCache() {
        return eliminatedPlayersCache;
    }

    public OfflinePlayerCache getOfflinePlayerCache() {
        return offlinePlayerCache;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public GracePeriodManager getGracePeriodManager() {
        return gracePeriodManager;
    }

    public BypassManager getBypassManager() {
        return bypassManager;
    }

    public GeyserManager getGeyserManager() {
        return geyserManager;
    }

    public GeyserPlayerFile getGeyserPlayerFile() {
        return geyserPlayerFile;
    }

    public boolean hasWorldGuard() {
        return hasWorldGuard;
    }

    public boolean hasPlaceholderApi() {
        return hasPlaceholderApi;
    }

    public boolean hasGeyser() {
        return hasGeyser;
    }

    public WebHookManager getWebHookManager() {
        return webHookManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    private Storage createPlayerDataStorage() {
        switch (getConfigManager().getStorageConfig().getString("type").toLowerCase()) {
            case "mysql":
                getLogger().info("Using MySQL storage");
                return new MySQLStorage(this);
            case "sqlite":
                getLogger().info("Using SQLite storage");
                return new SQLiteStorage(this);
            case "mariadb":
                getLogger().info("Using MariaDB storage");
                return new MariaDBStorage(this);
            default:
                getLogger().warning("Invalid storage type in config.yml! Using SQLite storage as fallback.");
                return new SQLiteStorage(this);
        }
    }

    public static void setMaxHealth(Player player, double maxHealth) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(maxHealth);
        }
    }

    private void initializeBStats() {
        int pluginId = 18735;
        Metrics metrics = createBStatsMetrics(pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("storage_type", () -> getConfigManager().getStorageConfig().getString("type")));
        metrics.addCustomChart(new Metrics.SimplePie("language", () -> getConfig().getString("lang")));
    }

    public File getPluginFile() {
        return this.getFile();
    }
}
