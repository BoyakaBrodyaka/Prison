package org.example;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.bosses.BossCommand;
import org.example.bosses.BossManager;
import org.example.bosses.InventoryListener;
import org.example.donate.DonateCommand;
import org.example.donate.DonateMenuManager;
import org.example.donate.boosters.BoosterManager;
import org.example.donate.boosters.DonateEvent;
import org.example.donate.boosters.disableBoostersCommand;
import org.example.donate.currency.AddCurrencyCommand;
import org.example.donate.currency.CurrencyManager;
import org.example.donate.currency.RemoveCurrencyCommand;
import org.example.menu.auction.AuctionCommand;
import org.example.menu.auction.AuctionManager;
import org.example.events.EventManager;
import org.example.events.GryaznylaEvent;
import org.example.events.SerunEvent;
import org.example.events.SoniaEvent;
import org.example.leaderboard.CustomPlaceholders;
import org.example.listeners.*;
import org.example.managers.*;
import org.example.menu.*;
import org.example.stats.*;
import org.example.upgrade.UpgradeCommand;

import java.util.*;

public class MainClass extends JavaPlugin implements Listener {
   private static MainClass instance;
   private ScoreboardManager scoreboardManager;
   private PlayerStats playerStats;
   private ZoneManager zoneManager;
   private InventoryManager inventoryManager;
   private PvPManager pvpManager;
   private PvPModeManager pvpModeManager;

   private Map<Material, Double> mine1Materials = new HashMap<>();
   private Map<Material, Double> mine2Materials = new HashMap<>();
   private Map<Material, Double> mine3Materials = new HashMap<>();
   private Map<Material, Double> mine4Materials = new HashMap<>();
   private Map<Material, Double> mine5Materials = new HashMap<>();
   private Map<Material, Double> mine6Materials = new HashMap<>();
   private Map<Material, Double> mine7Materials = new HashMap<>();
   private Map<Material, Double> mine8Materials = new HashMap<>();
   private Map<Material, Double> mine9Materials = new HashMap<>();
   private Map<Material, Double> mine10Materials = new HashMap<>();

   private ChatZoneManager chatZoneManager;
   private SpawnCommandManager spawnCommandManager;
   private LogProtectionListener logProtectionListener;
   private CraftingProtectionListener craftingProtectionListener;
   private ToolDamageListener toolDamageListener;
   private PlayerKillEventListener playerKillEventListener;
   private PlayerJoinQuitListener playerJoinQuitListener;
   private StatusManager manager;
   private FractionManager fractionManager;
   private AuctionManager auctionManager;
   private EventManager eventManager;
    private MainMenuManager mainMenuManager;
    private ShopMenuManager shopMenuManager;
    private ToolsMenuManager toolsMenuManager;
    private MinesMenuManager minesMenuManager;
    private DonateMenuManager donateMenuManager;
    private BoosterManager boosterManager;
    private CurrencyManager currencyManager;
    private AutoSellCommand autoSellCommand;
    private BossManager bossManager;

   @Override
    public void onEnable() {

           saveDefaultConfig();
       getServer().getPluginManager().registerEvents(this, this);
       playerStats = new PlayerStats(getDataFolder(), this);
      instance = this;
       manager = new StatusManager(this);
      scoreboardManager = new ScoreboardManager(this, playerStats);
      zoneManager = new ZoneManager(this);
      inventoryManager = new InventoryManager(getDataFolder());
       mainMenuManager = new MainMenuManager(this);
       toolsMenuManager = new ToolsMenuManager(this);
       shopMenuManager = new ShopMenuManager(this, toolsMenuManager);
       minesMenuManager = new MinesMenuManager(this);
      pvpManager = new PvPManager();
      pvpModeManager = new PvPModeManager(this, pvpManager);
      eventManager = new EventManager(this);
       fractionManager = new FractionManager(this, playerStats);
       currencyManager = new CurrencyManager();
       boosterManager = new BoosterManager(this);
       donateMenuManager = new DonateMenuManager(this, boosterManager, currencyManager);
       autoSellCommand = new AutoSellCommand(this);

       if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
           new CustomPlaceholders(this, playerStats).register();
           getLogger().info("Custom placeholders registered with PlaceholderAPI");
       } else {
           getLogger().warning("PlaceholderAPI not found, custom placeholders not registered");
       }

       scoreboardManager.startScoreboardUpdater();

      spawnCommandManager = new SpawnCommandManager(this);
      logProtectionListener = new LogProtectionListener(this);
      craftingProtectionListener = new CraftingProtectionListener(this);
      toolDamageListener = new ToolDamageListener(this, pvpModeManager);
      playerKillEventListener = new PlayerKillEventListener(pvpModeManager);
       chatZoneManager = new ChatZoneManager(manager, fractionManager);
       playerJoinQuitListener = new PlayerJoinQuitListener(this, inventoryManager, playerStats, manager, fractionManager, boosterManager);
       auctionManager = new AuctionManager(this);
       this.bossManager = new BossManager(this);

      new BukkitRunnable() {
          @Override
          public void run() {
                  mine1Materials.put(Material.DIRT, 90.0); // 10% dirt
                  mine1Materials.put(Material.COARSE_DIRT, 10.0);// 90% coarse_dirt
                  mine2Materials.put(Material.COARSE_DIRT, 100.0);
                  mine3Materials.put(Material.STONE, 95.0);
                  mine3Materials.put(Material.COAL_ORE, 4.5);
                  mine3Materials.put(Material.COAL_BLOCK, 0.5);
                  mine4Materials.put(Material.STONE, 95.0);
                  mine4Materials.put(Material.COAL_ORE, 5.0);
                  mine4Materials.put(Material.IRON_ORE, 2.0);
                  mine4Materials.put(Material.GOLD_ORE, 0.8);
                  mine4Materials.put(Material.DIAMOND_ORE, 0.3);
                  mine5Materials.put(Material.STONE, 90.0);
                  mine5Materials.put(Material.COAL_ORE, 10.0);
                  mine5Materials.put(Material.IRON_ORE, 4.0);
                  mine5Materials.put(Material.GOLD_ORE, 1.6);
                  mine5Materials.put(Material.DIAMOND_ORE, 0.6);
                  mine6Materials.put(Material.OAK_PLANKS, 90.0);
                  mine6Materials.put(Material.OAK_WOOD, 10.0);
                  mine7Materials.put(Material.OAK_WOOD, 100.0);
                  mine8Materials.put(Material.WHITE_WOOL, 90.0);
                  mine8Materials.put(Material.PINK_WOOL, 10.0);
                  mine9Materials.put(Material.QUARTZ_BLOCK, 90.0);
                  mine9Materials.put(Material.QUARTZ_BRICKS, 1.0);
                  mine9Materials.put(Material.CHISELED_QUARTZ_BLOCK, 0.1);
                  mine10Materials.put(Material.SPRUCE_PLANKS, 90.0);
                  mine10Materials.put(Material.SPRUCE_WOOD, 10.0);

                  pvpManager.addSafeZone(new Location(Bukkit.getWorld("PrisonServer"), -24,114,-27), new Location(Bukkit.getWorld("PrisonServer"), 36,54,31));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), -24,114,-27), new Location(Bukkit.getWorld("PrisonServer"), 36,54,31));
//1shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), -215,62,-72), new Location(Bukkit.getWorld("PrisonServer"), -243,46,-100), mine1Materials, 5);
                  pvpManager.addPvPZone(new Location(Bukkit.getWorld("PrisonServer"), -184,99,-27), new Location(Bukkit.getWorld("PrisonServer"), -282,23,-140));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), -184,99,-27), new Location(Bukkit.getWorld("PrisonServer"), -282,23,-140));
//2shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), -134,62,-60), new Location(Bukkit.getWorld("PrisonServer"), -152,46,-78), mine2Materials, 5);
                  pvpManager.addSafeZone(new Location(Bukkit.getWorld("PrisonServer"), -106,94,-24), new Location(Bukkit.getWorld("PrisonServer"), -174,25,-105));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), -106,94,-24), new Location(Bukkit.getWorld("PrisonServer"), -174,25,-105));
//3shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"),-50,63,424), new Location(Bukkit.getWorld("PrisonServer"), -68,47,442), mine3Materials, 5);
                  pvpManager.addSafeZone(new Location(Bukkit.getWorld("PrisonServer"), -6,171,489), new Location(Bukkit.getWorld("PrisonServer"), -110,20,394));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), -6,171,489), new Location(Bukkit.getWorld("PrisonServer"), -110,20,394));
//4shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 9,61,-55), new Location(Bukkit.getWorld("PrisonServer"), 29,45,-75), mine4Materials, 5);
                  pvpManager.addSafeZone(new Location(Bukkit.getWorld("PrisonServer"), -30,108,-34), new Location(Bukkit.getWorld("PrisonServer"), 49,27,-102));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), -30,108,-34), new Location(Bukkit.getWorld("PrisonServer"), 49,27,-102));
//5shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 85,60,-73), new Location(Bukkit.getWorld("PrisonServer"), 67,43,-55), mine5Materials, 5);
                  pvpManager.addPvPZone(new Location(Bukkit.getWorld("PrisonServer"), 50,27,-101), new Location(Bukkit.getWorld("PrisonServer"), 104, 121, -25));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 50,27,-101), new Location(Bukkit.getWorld("PrisonServer"), 104, 121, -25));
//6shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 142,59,-68), new Location(Bukkit.getWorld("PrisonServer"), 126,43,-54), mine6Materials, 5);
                  pvpManager.addPvPZone(new Location(Bukkit.getWorld("PrisonServer"), 107,-5,-29), new Location(Bukkit.getWorld("PrisonServer"), 162,115,-92));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 107,-5,-29), new Location(Bukkit.getWorld("PrisonServer"), 162,115,-92));
//7shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 200,59,-52), new Location(Bukkit.getWorld("PrisonServer"), 186,44,-66), mine7Materials, 5);
                  pvpManager.addPvPZone(new Location(Bukkit.getWorld("PrisonServer"), 227,21,-18), new Location(Bukkit.getWorld("PrisonServer"), 165,122,-94));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 227,21,-18), new Location(Bukkit.getWorld("PrisonServer"), 165,122,-94));
//8shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 282,58,-563), new Location(Bukkit.getWorld("PrisonServer"), 268,35,-577), mine8Materials, 5);
                  pvpManager.addPvPZone(new Location(Bukkit.getWorld("PrisonServer"), 328,130,-621), new Location(Bukkit.getWorld("PrisonServer"), 217,-17,-517));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 328,130,-621), new Location(Bukkit.getWorld("PrisonServer"), 217,-17,-517));
//9shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 367,59,-66), new Location(Bukkit.getWorld("PrisonServer"), 353,36,-80), mine9Materials, 5);
                  pvpManager.addPvPZone(new Location(Bukkit.getWorld("PrisonServer"), 313,149,-140), new Location(Bukkit.getWorld("PrisonServer"), 403,12,-13));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 313,149,-140), new Location(Bukkit.getWorld("PrisonServer"), 403,12,-13));
//10shaft
                  zoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 444,61,-87), new Location(Bukkit.getWorld("PrisonServer"), 462,44,-69), mine10Materials, 5);
                  pvpManager.addPvPZone(new Location(Bukkit.getWorld("PrisonServer"), 493,160,-38), new Location(Bukkit.getWorld("PrisonServer"), 413,59,-118));
                  chatZoneManager.addZone(new Location(Bukkit.getWorld("PrisonServer"), 493,160,-38), new Location(Bukkit.getWorld("PrisonServer"), 413,59,-118));
                  updateHolograms();
          }
      }.runTaskLater(this, 20L);

      new BukkitRunnable() {
          @Override
          public void run() {
              playerJoinQuitListener.updateAllPlayersTabNames();
          }
      }.runTaskTimer(this, 0L, 20L);

      getServer().getPluginManager().registerEvents(new PlayerMoveEventListener(this), this);
      getServer().getPluginManager().registerEvents(new BlockBreakEventListener(playerStats, zoneManager, boosterManager), this);
      getServer().getPluginManager().registerEvents(new BlockPlaceEventListener(playerStats, zoneManager), this);
      getServer().getPluginManager().registerEvents(new PlayerEntityDamageEvent(pvpManager, fractionManager, pvpModeManager), this);
      getServer().getPluginManager().registerEvents(new PlayerDropEventListener(), this);
      getServer().getPluginManager().registerEvents(new giveMenuManager(), this);
      getServer().getPluginManager().registerEvents(new UpgradeCommand(this), this);
      getServer().getPluginManager().registerEvents(new ZoneManager(this), this);
      getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this, inventoryManager, playerStats, manager, fractionManager, boosterManager), this);
      getServer().getPluginManager().registerEvents(new PlayerKillDeathEvent(playerStats, this, inventoryManager, boosterManager), this);
      getServer().getPluginManager().registerEvents(new FractionCommand(playerStats, fractionManager), this);
      getServer().getPluginManager().registerEvents(new DonateEvent(this, boosterManager, currencyManager), this);
       getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
      giveMenuManager.startDisplayingMessages(this);
      
      fractionManager.loadFractions();
       eventManager.addEvent(new GryaznylaEvent(this));
       eventManager.addEvent(new SerunEvent(this));
       eventManager.addEvent(new SoniaEvent(this));

      getServer().getPluginCommand("stats").setExecutor(new StatsCommand(playerStats));
      getServer().getPluginCommand("sell").setExecutor(new SellCommand(playerStats, boosterManager));
      getServer().getPluginCommand("upgrade").setExecutor(new UpgradeCommand(this));
      getServer().getPluginCommand("level").setExecutor(new LevelCommand(this));
      getServer().getPluginCommand("spawn").setExecutor(new SpawnCommandManager(this));
      getServer().getPluginCommand("fraction").setExecutor(new FractionCommand(playerStats, fractionManager));
      getServer().getPluginCommand("trash").setExecutor(new TrashCommand(this));
      getServer().getPluginCommand("pay").setExecutor(new PayCommand(playerStats, fractionManager));
      getServer().getPluginCommand("ah").setExecutor(new AuctionCommand(auctionManager, playerStats));
      getServer().getPluginCommand("shop").setExecutor(new ShopCommand(this, shopMenuManager));
      getServer().getPluginCommand("mines").setExecutor(new MinesCommand(this, minesMenuManager));
      getServer().getPluginCommand("donate").setExecutor(new DonateCommand(donateMenuManager));
      getServer().getPluginCommand("addcurrency").setExecutor(new AddCurrencyCommand(currencyManager));
      getServer().getPluginCommand("removecurrency").setExecutor(new RemoveCurrencyCommand(currencyManager));
      getServer().getPluginCommand("autosell").setExecutor(new AutoSellCommand(this));
      getServer().getPluginCommand("bosses").setExecutor(new BossCommand(this));
      this.getCommand("disableboosters").setExecutor(new disableBoostersCommand(boosterManager));


       eventManager.startEvents();
      boosterManager.loadBoosters();
      currencyManager.loadCurrency();
       bossManager.startBossSpawnTask();
   }

   public ScoreboardManager getScoreboardManager() {
       return scoreboardManager;
   }

    public static MainClass getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
       if(playerStats != null) {
           playerStats.saveStats();
       }
        for(Player player : Bukkit.getOnlinePlayers()) {
            inventoryManager.saveInventory(player);
        }
            fractionManager.saveFractionsToFile();

        saveConfig();
        if(boosterManager != null) {
            boosterManager.saveBoosters();
        }
        if(currencyManager != null) {
            currencyManager.saveCurrency();
        }
        autoSellCommand.saveAutoSellData();
    }

    public ZoneManager getZoneManager() {
        return zoneManager;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public InventoryManager getInventoryManager() {
       return inventoryManager;
    }

    public MinesMenuManager getMinesMenuManager() {
        return minesMenuManager;
    }

    public PvPManager getPvPManager() {
       return pvpManager;
    }

    public PvPModeManager getPvPModeManager() {
       return pvpModeManager;
    }

    public ChatZoneManager getChatZone() {
        return chatZoneManager;
    }

    public SpawnCommandManager getSpawnCommandManager() {
       return spawnCommandManager;
    }

    public FractionManager getFractionManager() {
       return fractionManager;
    }

    public BossManager getBossManager() {
       return bossManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (!playerStats.hasPlayer(playerUUID)) {
            playerStats.initializePlayer(playerUUID);
        } else {
            playerStats.loadStatsForPlayer(playerUUID);
        }
        scoreboardManager.createScoreboard(player);

        // Дополнительные функции
        scoreboardManager.updateScoreboard(player);
        mainMenuManager.giveMenuItem(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.getInventoryManager().saveInventory(event.getPlayer());
    }

    private void updateHolograms() {
        // Обновление голограммы денег
        updateHologram("money_leaderboard", new Location(Bukkit.getWorld("PrisonServer"), -4,69,21), "&6&lТоп по деньгам", "money");

        // Обновление голограммы уровней
        updateHologram("levels_leaderboard", new Location(Bukkit.getWorld("PrisonServer"), -9,69,18), "&6&lТоп по уровню", "level");

        // Обновление голограммы блоков
        updateHologram("blocks_leaderboard", new Location(Bukkit.getWorld("PrisonServer"), 2,69,18), "&6&lТоп по блокам", "blocks");
    }

    private void updateHologram(String hologramName, Location location, String title, String type) {
        // Удаляем существующую голограмму, если она есть
        Hologram existingHologram = DHAPI.getHologram(hologramName);
        if (existingHologram != null) {
            existingHologram.delete();
        }

        // Создаем новую голограмму
        Hologram hologram = DHAPI.createHologram(hologramName, location);
        DHAPI.addHologramLine(hologram, title);
        DHAPI.addHologramLine(hologram, "");

        for (int i = 1; i <= 10; i++) {
            DHAPI.addHologramLine(hologram, "&7#" + i + " %prisonplugin_" + type + "_top_" + i + "%");
        }
    }
}