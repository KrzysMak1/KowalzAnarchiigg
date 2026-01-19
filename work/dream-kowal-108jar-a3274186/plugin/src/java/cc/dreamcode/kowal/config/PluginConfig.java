package cc.dreamcode.kowal.config;

import org.bukkit.event.inventory.InventoryType;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import cc.dreamcode.utilities.builder.MapBuilder;
import org.bukkit.Particle;
import cc.dreamcode.kowal.effect.Effect;
import cc.dreamcode.kowal.effect.EffectType;
import org.bukkit.Material;
import cc.dreamcode.kowal.level.Level;
import java.util.Map;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import cc.dreamcode.menu.adventure.BukkitMenuBuilder;
import eu.okaeri.configs.annotation.Header;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import eu.okaeri.configs.OkaeriConfig;

@Configuration(child = "config.yml")
@Header({ "## Dream-Kowal (Main-Config) ##" })
public class PluginConfig extends OkaeriConfig
{
    @Comments({ @Comment, @Comment({ "Jak ma wygladac gui kowala?" }) })
    @CustomKey("kowal-menu")
    public BukkitMenuBuilder kowalMenu;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja o zmianie trybu na kamien kowalski?" }) })
    @CustomKey("kowal-menu-mode-metal")
    public ItemStack modeMetal;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja o zmianie trybu na metal?" }) })
    @CustomKey("kowal-menu-mode-kamien")
    public ItemStack modeKamien;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie znajduje sie przycisk od zmiany trybu kowala?" }) })
    @CustomKey("kowal-menu-mode-slot")
    public int modeSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie ma sie znajdowac przycisk od anulowania ulepszenia?" }) })
    @CustomKey("kowal-menu-upgrade-cancel-slot")
    public int upgradeCancelSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie ma sie znajdowac przedmiot do ulepszenia?" }) })
    @CustomKey("kowal-menu-upgrade-item-slot")
    public int upgradeItemSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie ma sie znajdowac przycisk od potwierdzenia ulepszenia?" }) })
    @CustomKey("kowal-menu-upgrade-accept-slot")
    public int upgradeAcceptSlot;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja w gui, gdy przedmiot w lapce nie jest do ulepszenia?" }) })
    @CustomKey("kowal-menu-not-upgradeable")
    public ItemStack notUpgradeable;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja w lore gdy gracz nie posiada przedmiotow do ulepszenia uzbrojenia?" }) })
    @CustomKey("upgrade-status-false")
    public String cannotUpgradeStatus;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja w lore gdy gracz posiada przedmioty do ulepszenia uzbrojenia?" }) })
    @CustomKey("upgrade-status-true")
    public String canUpgradeStatus;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac gui z potwierdzeniem ulepszenia przedmiotu?" }) })
    @CustomKey("kowal-confirm-menu")
    public BukkitMenuBuilder confirmMenu;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac przedmiot gdy gracz uzywa metalu?" }) })
    @CustomKey("kowal-confirm-menu-metal-item")
    public ItemStack confirmModeMetal;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac przedmiot gdy gracz uzywa kamienia kowalskiego?" }) })
    @CustomKey("kowal-config-menu-kamien-item")
    public ItemStack confirmModeKamien;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie znajduje sie przycisk od anulowania ulepszenia?" }) })
    @CustomKey("kowal-confirm-menu-cancel-slot")
    public int confirmCancelSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie znajduje sie przycisk od ulepszenia?" }) })
    @CustomKey("kowal-confirm-menu-accept-slot")
    public int confirmAcceptSlot;
    @Comments({ @Comment, @Comment({ "Lista poziomow kowala" }) })
    @CustomKey("kowal-levels")
    public Map<Integer, Level> kowalLevels;
    @Comments({ @Comment, @Comment({ "Jaka nazwe maja posiadac przedmioty po ulepszeniu? (Tylko gdy przedmiot ma podstawowa nazwe)" }) })
    @CustomKey("kowal-items-name")
    public Map<Material, String> kowalItems;
    @Comments({ @Comment, @Comment({ "Jaki kolor ma miec poziom ulepszenia przedmiotu?" }) })
    @CustomKey("kowal-items-colors")
    public Map<Material, String> kowalColors;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac kamien kowalski?" }) })
    @CustomKey("kamien-kowalski-item")
    public ItemStack kamienKowalski;
    @Comments({ @Comment, @Comment({ "Lista efektow, ktore gracz dostaje losowo na 7 poziomie ulepszenia." }) })
    @CustomKey("effect-list")
    public Map<EffectType, Effect> effects;
    @Comments({ @Comment, @Comment({ "Jaki ma sie odtworzyc dzwiek gdy ulepszenie sie uda?" }) })
    @CustomKey("upgrade-success-sound")
    public String upgradeSuccess;
    @Comments({ @Comment, @Comment({ "Jaki ma sie odtworzyc dzwiek gdy ulepszenie sie nie uda?" }) })
    @CustomKey("upgrade-failure-sound")
    public String upgradeFailure;
    @Comments({ @Comment, @Comment({ "Lista mozliwych particlesow dla pelnego seta 6 lub 7 poziomu (losowane przy zalozeniu pelnego seta lub mieszanego 6/7)." }) })
    @CustomKey("particles")
    public List<Particle> particles;
    @Comments({ @Comment, @Comment({ "ID FancyNPC, ktore ma otwierac GUI kowala po kliknieciu PPM (puste = wylaczone)." }) })
    @CustomKey("fancy-npc-id")
    public String fancyNpcId;
    
    public PluginConfig() {
        this.kowalMenu = new BukkitMenuBuilder("&8Kowal", 3, new MapBuilder<Integer, ItemStack>().put(11, ItemBuilder.of(Material.RED_DYE).setName("&cAnuluj").toItemStack()).put(15, ItemBuilder.of(Material.LIME_DYE).setName("&2Zwieksz poziom").setLore("&7Ulepszenie: &a+{level} \u2192 &a+{new}", " ", "&7Wymagane ulepszacze:", "{items}", "{cost}", " ", "{status}").toItemStack()).build());
        this.modeMetal = ItemBuilder.of(Material.LEVER).setName("&9Ulepszanie kamieniem kowalskim").setLore("&7Gdy ulepszasz przedmiot &fKamieniem Kowalskim", "&7masz pewnosc, ze gdy ulepszenie sie", "&7nie powiedzie poziom przedmiotu nie", "&7zostanie obnizony", " ", "&aKliknij aby przelaczyc!").toItemStack();
        this.modeKamien = ItemBuilder.of(Material.REDSTONE_TORCH).setName("&5Ulepszasz metalem!").setLore("&7Ulepszasz przedmiotem specjalnym", "&7metalem, ktory zabezpiecza przed", "&7obnizeniem poziomu przedmiotu.", " ", "&dKliknij aby przelaczyc!").toItemStack();
        this.modeSlot = 22;
        this.upgradeCancelSlot = 11;
        this.upgradeItemSlot = 13;
        this.upgradeAcceptSlot = 15;
        this.notUpgradeable = ItemBuilder.of(Material.BARRIER).setName("&cNiepoprawny przedmiot!").setLore("&7Przedmiot, ktory trzymasz w rece nie posiada", "&7mozliwosci ulepszenia badz jest ulepszony juz", "&7na &fmaksymalny &7poziom", " ", "&7Dostepne przedmioty do ulepszenia:", "&8» &bDiamentowe uzbrojenie", "&8» &cNetherytowe uzbrojenie").toItemStack();
        this.cannotUpgradeStatus = "&cUzbieraj wymagane przedmioty!";
        this.canUpgradeStatus = "&eKliknij, aby ulepszyc!";
        this.confirmMenu = new BukkitMenuBuilder(InventoryType.HOPPER, "&8Czy chcesz kontynuowac?", new MapBuilder<Integer, ItemStack>().put(1, ItemBuilder.of(Material.RED_DYE).setName("&cNIE").setLore("&7Nie chce ryzykowac. Wole zostac", "&7przy aktualnym poziomie ulepszenia.").toItemStack()).build());
        this.confirmModeMetal = ItemBuilder.of(Material.LIME_DYE).setName("&aTAK").setLore("&7Szansa na ulepszenie: &f{chance}%", " ", "&7Jestes swiadomy, ze przy nieudanej", "&7probie &eulepszenia poziom &7przedmiotu zostanie", "&7obnizony o jeden.").toItemStack();
        this.confirmModeKamien = ItemBuilder.of(Material.LIME_DYE).setName("&aTAK").setLore("&7Szansa na ulepszenie: &f{chance}%", " ", "&7To ulepszenie jest &ezabezpieczone", "&7przed obnizeniem poziomu przedmiotu.").toItemStack();
        this.confirmCancelSlot = 1;
        this.confirmAcceptSlot = 3;
        this.kowalLevels = new MapBuilder<Integer, Level>().put(1, new Level(Map.of(Material.COAL, 10), "&8» &aWegiel x10", "&8» &a35$", 35.0, "&b+0.5 odpornosc na obrazenia", 0.0015, 0.0, 80)).put(2, new Level(Map.of(Material.IRON_INGOT, 8), "&8» &aSztabka zelaza x8", "&8» &a50$", 50.0, "&b+1.0 odpornosc na obrazenia", 0.002, 0.0, 70)).put(3, new Level(Map.of(Material.STRING, 16), "&8» &aNi\u0107 x16", "&8» &a75$", 75.0, "&b+1.5 odpornosc na obrazenia", 0.0025, 0.0, 60)).put(4, new Level(Map.of(Material.EMERALD, 2), "&8» &aEmeraldy x2", "&8» &a90$", 90.0, "&b+2.0 odpornosc na obrazenia", 0.003, 0.0, 50)).put(5, new Level(Map.of(Material.IRON_INGOT, 16), "&8» &aSztabka zelaza x16", "&8» &a105$", 105.0, "&b+3.0 odpornosc na obrazenia", 0.0035, 0.0, 40)).put(6, new Level(Map.of(Material.EMERALD, 6), "&8» &aEmeraldy x6", "&8» &a120$", 120.0, "&b+4.0 odpornosc na obrazenia", 0.004, 0.0, 30)).put(7, new Level(Map.of(Material.DIAMOND, 6), "&8» &aDiamenty x6", "&8» &a145$", 145.0, "&b+5.0 odpornosc na obrazenia", 0.0045, 0.0, 20)).build();
        this.kowalItems = new MapBuilder<Material, String>().put(Material.DIAMOND_HELMET, "&3Diamentowy helm").put(Material.DIAMOND_CHESTPLATE, "&3Diamentowa klata").put(Material.DIAMOND_LEGGINGS, "&3Diamentowe spodnie").put(Material.DIAMOND_BOOTS, "&3Diamentowe buty").put(Material.NETHERITE_HELMET, "&cNetherytowy helm").put(Material.NETHERITE_CHESTPLATE, "&cNetherytowa klata").put(Material.NETHERITE_LEGGINGS, "&cNetherytowe spodnie").put(Material.NETHERITE_BOOTS, "&cNetherytowe buty").build();
        this.kowalColors = new MapBuilder<Material, String>().put(Material.DIAMOND_HELMET, "&b+{level}").put(Material.DIAMOND_CHESTPLATE, "&b+{level}").put(Material.DIAMOND_LEGGINGS, "&b+{level}").put(Material.DIAMOND_BOOTS, "&b+{level}").put(Material.NETHERITE_HELMET, "&4+{level}").put(Material.NETHERITE_CHESTPLATE, "&4+{level}").put(Material.NETHERITE_LEGGINGS, "&4+{level}").put(Material.NETHERITE_BOOTS, "&4+{level}").build();
        this.kamienKowalski = ItemBuilder.of(Material.GRAY_DYE).setName("&cKamien kowalski").setLore("&8» &7Powoduje, ze przedmiot po ulepszeniu", "&8» &7u &fKowala &7nie cofa swojego poziomu w", "&8» &7przypadku &eniepowodzenia&7!").toItemStack();
        this.effects = new MapBuilder<EffectType, Effect>().put(EffectType.ARMOR_DAMAGE, new Effect("&6{chance}% wolniejsze niszczenie seta", 12)).put(EffectType.POTION_DURATION, new Effect("&9{chance}% wydluzenia efektu wypitych mikstur", 12)).put(EffectType.DAMAGE, new Effect("&a{chance}% szansy na odbicie ciosu", 3)).put(EffectType.ARROW, new Effect("&d{chance}% szansy na odbicie strzaly", 10)).build();
        this.upgradeSuccess = "BLOCK_ANVIL_BREAK";
        this.upgradeFailure = "ENTITY_ITEM_BREAK";
        this.particles = List.of(Particle.HAPPY_VILLAGER);
        this.fancyNpcId = "";
    }
}
