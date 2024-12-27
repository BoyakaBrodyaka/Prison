package org.example.upgrade;

import com.sun.security.auth.UnixNumericGroupPrincipal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.MainClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeCommand implements CommandExecutor, Listener {
    private final MainClass plugin;
    private final Map<Material, UpgradePath> upgradeMap = new HashMap<>();

    public UpgradeCommand(MainClass plugin) {
        this.plugin = plugin;
        UpgradePath shovelPath = new UpgradePath();
        UpgradePath pickaxePath = new UpgradePath();
        UpgradePath axePath = new UpgradePath();
        UpgradePath shearsPath = new UpgradePath();
        UpgradePath helmetPath = new UpgradePath();
        UpgradePath chestplatePath = new UpgradePath();
        UpgradePath leggingsPath = new UpgradePath();
        UpgradePath bootsPath = new UpgradePath();
        UpgradePath swordPath = new UpgradePath();
        Map<Enchantment, Integer> enchantmentsEff0 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsEff1 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsEff2 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsEff3 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsEff4 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsEff5 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsProt0 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsProt1 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsProt2 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsProt3 = new HashMap<>();
        Map<Enchantment, Integer> enchantmentsSharp0 = new HashMap<>();
        enchantmentsSharp0.put(Enchantment.DAMAGE_ALL, 0);
        Map<Enchantment, Integer> enchantmentsSharp1 = new HashMap<>();
        enchantmentsSharp1.put(Enchantment.DAMAGE_ALL, 1);
        Map<Enchantment, Integer> enchantmentsSharp2 = new HashMap<>();
        enchantmentsSharp2.put(Enchantment.DAMAGE_ALL, 2);
        Map<Enchantment, Integer> enchantmentsSharp3 = new HashMap<>();
        enchantmentsSharp3.put(Enchantment.DAMAGE_ALL, 3);
        Map<Enchantment, Integer> enchantmentsSharp4 = new HashMap<>();
        enchantmentsSharp4.put(Enchantment.DAMAGE_ALL, 4);
        enchantmentsEff0.put(Enchantment.DIG_SPEED, 0);
        enchantmentsEff1.put(Enchantment.DIG_SPEED, 1);
        enchantmentsEff2.put(Enchantment.DIG_SPEED, 2);
        enchantmentsEff3.put(Enchantment.DIG_SPEED, 3);
        enchantmentsEff4.put(Enchantment.DIG_SPEED, 4);
        enchantmentsEff5.put(Enchantment.DIG_SPEED, 5);
        enchantmentsProt0.put(Enchantment.PROTECTION_ENVIRONMENTAL, 0);
        enchantmentsProt1.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        enchantmentsProt2.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        enchantmentsProt3.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_SHOVEL, 10, 100, 1, enchantmentsEff1, true, "Сломанная Старая лопата", "Уровень 2"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_SHOVEL, 20, 300, 1, enchantmentsEff2, true, "старая лопата", "Уровень 3"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_SHOVEL, 40, 500, 1, enchantmentsEff3, true, "Улучшенная старая лопата", "Уровень 4"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SHOVEL, 80, 1000, 2, enchantmentsEff0, true, "Сломанная обычная лопата", "Уровень 5"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SHOVEL, 100, 1500, 2, enchantmentsEff1, true, "Обычная лопата", "Уровень 6"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SHOVEL, 150, 2000, 2, enchantmentsEff2, true, "Улучшенная обычная лопата", "Уровень 7"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SHOVEL, 250, 3000, 2, enchantmentsEff3, true, "Эксклюзивная обычная лопата", "Уровень 8"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SHOVEL, 350, 5000, 3, enchantmentsEff0, true, "Сломанная крутая лопата", "Уровень 9"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SHOVEL, 500, 7000, 3, enchantmentsEff1, true, "Крутая лопата", "Уровень 10"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SHOVEL, 700, 10000, 3, enchantmentsEff2, true, "Улучшенная крутая лопата", "Уровень 11"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SHOVEL, 1000, 15000, 3, enchantmentsEff3, true, "Эксклюзивная крутая лопата", "Уровень 12"));
        shovelPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SHOVEL, 1500, 25000, 3, enchantmentsEff4, true, "Профессиональная лопата", "Уровень 13"));

        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_PICKAXE, 30, 300, 2, enchantmentsEff1, true, "Сломанная Старая кирка", "Уровень 2"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_PICKAXE, 40, 500, 2, enchantmentsEff2, true, "старая кирка", "Уровень 3"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_PICKAXE, 80, 1000, 2, enchantmentsEff3, true, "Улучшенная старая кирка", "Уровень 4"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_PICKAXE, 150, 1500, 3, enchantmentsEff0, true, "Сломанная обычная кирка", "Уровень 5"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_PICKAXE, 300, 2000, 3, enchantmentsEff1, true, "Обычная кирка", "Уровень 6"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_PICKAXE, 500, 2500, 3, enchantmentsEff2, true, "Улучшенная обычная кирка", "Уровень 7"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_PICKAXE, 700, 3500, 3, enchantmentsEff3, true, "Эксклюзивная обычная кирка", "Уровень 8"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_PICKAXE, 1000, 5000, 4, enchantmentsEff0, true, "Сломанная крутая кирка", "Уровень 9"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_PICKAXE, 1500, 10000, 4, enchantmentsEff1, true, "Крутая кирка", "Уровень 10"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_PICKAXE, 2500, 15000, 4, enchantmentsEff2, true, "Улучшенная крутая кирка", "Уровень 11"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_PICKAXE, 3000, 20000, 4, enchantmentsEff3, true, "Эксклюзивная крутая кирка", "Уровень 12"));
        pickaxePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_PICKAXE, 3500, 30000, 5, enchantmentsEff4, true, "Профессиональная кирка", "Уровень 13"));

        axePath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_AXE, 50, 500, 3, enchantmentsEff1, true, "Сломанный старый топор", "Уровень 2"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_AXE, 80, 700, 3, enchantmentsEff2, true, "Старый топор", "Уровень 3"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_AXE, 120, 1000,  3, enchantmentsEff3, true, "Улучшенный старый топор", "Уровень 4"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_AXE, 180, 1500, 4, enchantmentsEff0, true, "Сломанный обычный топор", "Уровень 5"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_AXE, 250, 2000, 4, enchantmentsEff1, true, "Обычный топор", "Уровень 6"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_AXE, 500, 3000, 4, enchantmentsEff2, true, "Улучшенный обычный топор", "Уровень 7"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.STONE_AXE, 700, 5000, 4, enchantmentsEff3, true, "Эксклюзивный обычный топор", "Уровень 8"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_AXE, 1000, 7000, 5, enchantmentsEff0, true, "Сломанный крутой топор", "Уровень 9"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_AXE, 1500, 10000, 5, enchantmentsEff1, true, "Крутой топор", "Уровень 10"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_AXE, 2500, 15000, 5, enchantmentsEff2, true, "Улучшенный крутой топор", "Уровень 11"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_AXE, 3500, 20000, 5, enchantmentsEff3, true, "Эксклюзивный крутой топор", "Уровень 12"));
        axePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_AXE, 5000, 30000, 6, enchantmentsEff4, true, "Профессиональный топор", "Уровень 13"));

        shearsPath.addUpgradeLevel(new UpgradeInfo(Material.SHEARS, 100, 700, 4, enchantmentsEff1, true, "Старые ножницы", "Уровень 2"));
        shearsPath.addUpgradeLevel(new UpgradeInfo(Material.SHEARS, 100, 700, 4, enchantmentsEff2, true, "Улучшенные ножницы", "Уровень 3"));
        shearsPath.addUpgradeLevel(new UpgradeInfo(Material.SHEARS, 100, 700, 5, enchantmentsEff3, true, "Обычныен ножницы", "Уровень 4"));
        shearsPath.addUpgradeLevel(new UpgradeInfo(Material.SHEARS, 100, 700, 5, enchantmentsEff4, true, "Эксклюзивные ножницы", "Уровень 5"));
        shearsPath.addUpgradeLevel(new UpgradeInfo(Material.SHEARS, 100, 700, 6, enchantmentsEff5, true, "Профессиональные ножницы", "Уровень 6"));

        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_HELMET, 50, 500, 3, enchantmentsProt0, true, "Сломанный кожаный шлем", "Уровень 2"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_HELMET, 80, 700, 3, enchantmentsProt1, true, "Кожаный шлем", "Уровень 3"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_HELMET, 120, 1000, 3, enchantmentsProt2, true, "Улучшенный кожаный шлем", "Уровень 4"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_HELMET, 180, 1500, 4, enchantmentsProt0, true, "Сломанный кольчужный шлем", "Уровень 5"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_HELMET, 250, 2000, 4, enchantmentsProt1, true, "Кольчужный шлем", "Уровень 6"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_HELMET, 500, 3000, 4, enchantmentsProt2, true, "Улучшенный кольчужный шлем", "Уровень 7"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_HELMET, 700, 5000, 4, enchantmentsProt3, true, "Сломанный железный шлем", "Уровень 8"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_HELMET, 1000, 7000, 5, enchantmentsProt0, true, "Железный шлем", "Уровень 9"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_HELMET, 1500, 10000, 5, enchantmentsProt1, true, "Улучшенный железный шлем", "Уровень 10"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_HELMET, 2500, 15000, 5, enchantmentsProt2, true, "Эксклюзивный железный шлем", "Уровень 11"));
        helmetPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_HELMET, 3500, 20000, 5, enchantmentsProt3, true, "Профессиональный алмазный шлем", "Уровень 12"));

        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_CHESTPLATE, 50, 500, 3, enchantmentsProt0, true, "Сломанный кожаный нагрудник", "Уровень 2"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_CHESTPLATE, 80, 700, 3, enchantmentsProt1, true, "Кожаный нагрудник", "Уровень 3"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_CHESTPLATE, 120, 1000, 3, enchantmentsProt2, true, "Улучшенный кожаный нагрудник", "Уровень 4"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_CHESTPLATE, 180, 1500, 4, enchantmentsProt0, true, "Сломанный кольчужный нагрудник", "Уровень 5"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_CHESTPLATE, 250, 2000, 4, enchantmentsProt1, true, "Кольчужный нагрудник", "Уровень 6"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_CHESTPLATE, 500, 3000, 4, enchantmentsProt2, true, "Улучшенный кольчужный нагрудник", "Уровень 7"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_CHESTPLATE, 700, 5000, 4, enchantmentsProt3, true, "Сломанный железный нагрудник", "Уровень 8"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_CHESTPLATE, 1000, 7000, 5, enchantmentsProt0, true, "Железный нагрудник", "Уровень 9"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_CHESTPLATE, 1500, 10000, 5, enchantmentsProt1, true, "Улучшенный железный нагрудник", "Уровень 10"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_CHESTPLATE, 2500, 15000, 5, enchantmentsProt2, true, "Эксклюзивный железный нагрудник", "Уровень 11"));
        chestplatePath.addUpgradeLevel(new UpgradeInfo(Material.IRON_CHESTPLATE, 3500, 20000, 5, enchantmentsProt3, true, "Профессиональный алмазный нагрудник", "Уровень 12"));

        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_LEGGINGS, 50, 500, 3, enchantmentsProt0, true, "Сломанные кожаные поножи", "Уровень 2"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_LEGGINGS, 80, 700, 3, enchantmentsProt1, true, "Кожаные поножи", "Уровень 3"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_LEGGINGS, 120, 1000, 3, enchantmentsProt2, true, "Улучшенные кожаные поножи", "Уровень 4"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_LEGGINGS, 180, 1500, 4, enchantmentsProt0, true, "Сломанные кольчужные поножи", "Уровень 5"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_LEGGINGS, 250, 2000, 4, enchantmentsProt1, true, "Кольчужные поножи", "Уровень 6"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_LEGGINGS, 500, 3000, 4, enchantmentsProt2, true, "Улучшенные кольчужные поножи", "Уровень 7"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_LEGGINGS, 700, 5000, 4, enchantmentsProt3, true, "Сломанные железные поножи", "Уровень 8"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_LEGGINGS, 1000, 7000, 5, enchantmentsProt0, true, "Железные поножи", "Уровень 9"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_LEGGINGS, 1500, 10000, 5, enchantmentsProt1, true, "Улучшенные железные поножи", "Уровень 10"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_LEGGINGS, 2500, 15000, 5, enchantmentsProt2, true, "Эксклюзивные железные поножи", "Уровень 11"));
        leggingsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_LEGGINGS, 3500, 20000, 5, enchantmentsProt3, true, "Профессиональные алмазные поножи", "Уровень 12"));

        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_BOOTS, 50, 500, 3, enchantmentsProt0, true, "Сломанные кожаные ботинки", "Уровень 2"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_BOOTS, 80, 700, 3, enchantmentsProt1, true, "Кожаные ботинки", "Уровень 3"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.LEATHER_BOOTS, 120, 1000, 3, enchantmentsProt2, true, "Улучшенные кожаные ботинки", "Уровень 4"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_BOOTS, 180, 1500, 4, enchantmentsProt0, true, "Сломанные кольчужные ботинки", "Уровень 5"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_BOOTS, 250, 2000, 4, enchantmentsProt1, true, "Кольчужные ботинки", "Уровень 6"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_BOOTS, 500, 3000, 4, enchantmentsProt2, true, "Улучшенные кольчужные ботинки", "Уровень 7"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.CHAINMAIL_BOOTS, 700, 5000, 4, enchantmentsProt3, true, "Сломанные железные ботинки", "Уровень 8"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_BOOTS, 1000, 7000, 5, enchantmentsProt0, true, "Железные ботинки", "Уровень 9"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_BOOTS, 1500, 10000, 5, enchantmentsProt1, true, "Улучшенные железные ботинки", "Уровень 10"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_BOOTS, 2500, 15000, 5, enchantmentsProt2, true, "Эксклюзивные железные ботинки", "Уровень 11"));
        bootsPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_BOOTS, 3500, 20000, 5, enchantmentsProt3, true, "Профессиональные алмазные ботинки", "Уровень 12"));

        swordPath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_SWORD, 80, 700, 3, enchantmentsSharp1, true, "Деревянный меч", "Уровень 1"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.WOODEN_SWORD, 120, 1000, 3, enchantmentsSharp2, true, "Улучшенный деревянный меч", "Уровень 2"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SWORD, 180, 1500, 4, enchantmentsSharp0, true, "Сломанный каменный меч", "Уровень 3"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SWORD, 250, 2000, 4, enchantmentsSharp1, true, "Каменный меч", "Уровень 4"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SWORD, 500, 3000, 4, enchantmentsSharp2, true, "Улучшенный каменный меч", "Уровень 5"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.STONE_SWORD, 700, 5000, 4, enchantmentsSharp3, true, "Эксклюзивный каменный меч", "Уровень 6"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SWORD, 1000, 7000, 5, enchantmentsSharp0, true, "Сломанный железный меч", "Уровень 7"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SWORD, 1500, 10000, 5, enchantmentsSharp1, true, "Железный меч", "Уровень 8"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SWORD, 2500, 15000, 5, enchantmentsSharp2, true, "Улучшенный железный меч", "Уровень 9"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.IRON_SWORD, 3500, 20000, 5, enchantmentsSharp3, true, "Эксклюзивный железный меч", "Уровень 10"));
        swordPath.addUpgradeLevel(new UpgradeInfo(Material.DIAMOND_SWORD, 5000, 30000, 6, enchantmentsSharp4, true, "Профессиональный алмазный меч", "Уровень 11"));

        upgradeMap.put(Material.WOODEN_SHOVEL, shovelPath);
        upgradeMap.put(Material.STONE_SHOVEL, shovelPath);
        upgradeMap.put(Material.IRON_SHOVEL, shovelPath);

        upgradeMap.put(Material.WOODEN_PICKAXE, pickaxePath);
        upgradeMap.put(Material.STONE_PICKAXE, pickaxePath);
        upgradeMap.put(Material.IRON_PICKAXE, pickaxePath);

        upgradeMap.put(Material.WOODEN_AXE, axePath);
        upgradeMap.put(Material.STONE_AXE, axePath);
        upgradeMap.put(Material.IRON_AXE, axePath);

        upgradeMap.put(Material.SHEARS, shearsPath);

        upgradeMap.put(Material.LEATHER_HELMET, helmetPath);
        upgradeMap.put(Material.CHAINMAIL_HELMET, helmetPath);
        upgradeMap.put(Material.IRON_HELMET, helmetPath);
        upgradeMap.put(Material.DIAMOND_HELMET, helmetPath);

        upgradeMap.put(Material.LEATHER_CHESTPLATE, chestplatePath);
        upgradeMap.put(Material.CHAINMAIL_CHESTPLATE, chestplatePath);
        upgradeMap.put(Material.IRON_CHESTPLATE, chestplatePath);
        upgradeMap.put(Material.DIAMOND_CHESTPLATE, chestplatePath);

        upgradeMap.put(Material.LEATHER_LEGGINGS, leggingsPath);
        upgradeMap.put(Material.CHAINMAIL_LEGGINGS, leggingsPath);
        upgradeMap.put(Material.IRON_LEGGINGS, leggingsPath);
        upgradeMap.put(Material.DIAMOND_LEGGINGS, leggingsPath);

        upgradeMap.put(Material.LEATHER_BOOTS, bootsPath);
        upgradeMap.put(Material.CHAINMAIL_BOOTS, bootsPath);
        upgradeMap.put(Material.IRON_BOOTS, bootsPath);
        upgradeMap.put(Material.DIAMOND_BOOTS, bootsPath);

        upgradeMap.put(Material.WOODEN_SWORD, swordPath);
        upgradeMap.put(Material.STONE_SWORD, swordPath);
        upgradeMap.put(Material.IRON_SWORD, swordPath);
        upgradeMap.put(Material.DIAMOND_SWORD, swordPath);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (isUpgradeableItem(itemInHand)) {
                openUpgradeMenu(player, itemInHand);
            } else {
                player.sendMessage(ChatColor.RED + "Вы должны держать предмет, который можно улучшить!");
            }
        }
        return true;
    }

    private boolean isUpgradeableItem(ItemStack item) {
        return upgradeMap.containsKey(item.getType());
    }

    private void openUpgradeMenu(Player player, ItemStack itemInHand) {
        Inventory upgradeMenu = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Улучшение предмета");

        UpgradePath upgradePath = upgradeMap.get(itemInHand.getType());
        UpgradeInfo nextUpgrade = upgradePath.getNextUpgrade(itemInHand);

        if (nextUpgrade == null) {
            player.sendMessage(ChatColor.RED + "Этот предмет уже достиг максимального уровня.");
            return;
        }

        ItemStack upgradedItem = new ItemStack(nextUpgrade.getNextMaterial());
        ItemMeta upgradedMeta = upgradedItem.getItemMeta();
        int currentLevel = upgradePath.extractLevelFromName(itemInHand.getItemMeta().getDisplayName());
        int nextLevel = currentLevel + 1;

        upgradedMeta.setDisplayName(ChatColor.YELLOW + nextUpgrade.getCustomName() + ChatColor.RESET + " [" + ChatColor.YELLOW + nextLevel + ChatColor.RESET + "]");
        upgradedMeta.setLore(Arrays.asList(
                ChatColor.GOLD + "Требования:",
                ChatColor.YELLOW + "Деньги: " + nextUpgrade.getCost(),
                ChatColor.YELLOW + "Блоки: " + nextUpgrade.getRequiredBlocks(),
                ChatColor.YELLOW + "Уровень: " + nextUpgrade.getRequiredLevel(),
                ""
        ));
        nextUpgrade.getEnchantments().forEach((enchantment, level) -> upgradedMeta.addEnchant(enchantment, level, true));
        upgradedMeta.setUnbreakable(nextUpgrade.isUnbreakable());
        upgradedItem.setItemMeta(upgradedMeta);

        upgradeMenu.setItem(22, upgradedItem);

        player.openInventory(upgradeMenu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Улучшение предмета")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                UpgradePath upgradePath = upgradeMap.get(itemInHand.getType());
                UpgradeInfo nextUpgrade = upgradePath.getNextUpgrade(itemInHand);
                int currentLevel = upgradePath.extractLevelFromName(itemInHand.getItemMeta().getDisplayName());
                int nextLevel = currentLevel + 1;

                if (plugin.getPlayerStats().getLevel(player.getUniqueId()) >= nextUpgrade.getRequiredLevel()) {
                    if (plugin.getPlayerStats().getMoney(player.getUniqueId()) >= nextUpgrade.getCost()) {
                        if (plugin.getPlayerStats().getBlocksMined(player.getUniqueId()) >= nextUpgrade.getRequiredBlocks()) {
                            plugin.getPlayerStats().setMoney(player.getUniqueId(), plugin.getPlayerStats().getMoney(player.getUniqueId()) - nextUpgrade.getCost());
                            ItemStack newItem = new ItemStack(nextUpgrade.getNextMaterial());
                            ItemMeta newMeta = newItem.getItemMeta();
                            newMeta.setDisplayName(ChatColor.YELLOW + nextUpgrade.getCustomName() + ChatColor.RESET + " [" + ChatColor.YELLOW + nextLevel + ChatColor.RESET + "]");
                            newMeta.setLore(List.of(""
                            ));
                            nextUpgrade.getEnchantments().forEach((enchantment, level) -> newMeta.addEnchant(enchantment, level, true));
                            newMeta.setUnbreakable(nextUpgrade.isUnbreakable());
                            newItem.setItemMeta(newMeta);
                            player.getInventory().setItemInMainHand(newItem);
                            player.sendMessage(ChatColor.GREEN + "Вы успешно улучшили " + newMeta.getDisplayName() + "!");
                        } else {
                            player.sendMessage(ChatColor.RED + "Недостаточно блоков!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Недостаточно денег!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Недостаточный уровень!");
                }
                player.closeInventory();
            }
        }
    }
}
