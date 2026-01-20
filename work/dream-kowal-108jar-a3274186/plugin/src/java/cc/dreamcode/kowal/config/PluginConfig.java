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
import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import cc.dreamcode.menu.adventure.BukkitMenuBuilder;
import eu.okaeri.configs.annotation.Header;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import eu.okaeri.configs.OkaeriConfig;
import cc.dreamcode.kowal.economy.PaymentMode;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

@Configuration(child = "config.yml")
@Header({ "## Dream-Kowal (Main-Config) ##" })
public class PluginConfig extends OkaeriConfig
{
    @Comments({ @Comment, @Comment({ "Jak ma wygladac gui kowala?" }) })
    @CustomKey("menus.kowal")
    public BukkitMenuBuilder kowalMenu;
    @CustomKey("kowal-menu")
    private BukkitMenuBuilder legacyKowalMenu;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja o zmianie trybu na kamien kowalski?" }) })
    @CustomKey("menus.modeMetal")
    public ItemStack modeMetal;
    @CustomKey("kowal-menu-mode-metal")
    private ItemStack legacyModeMetal;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja o zmianie trybu na metal?" }) })
    @CustomKey("menus.modeKamien")
    public ItemStack modeKamien;
    @CustomKey("kowal-menu-mode-kamien")
    private ItemStack legacyModeKamien;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie znajduje sie przycisk od zmiany trybu kowala?" }) })
    @CustomKey("slots.mode")
    public int modeSlot;
    @CustomKey("kowal-menu-mode-slot")
    private Integer legacyModeSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie ma sie znajdowac przycisk od anulowania ulepszenia?" }) })
    @CustomKey("slots.upgradeCancel")
    public int upgradeCancelSlot;
    @CustomKey("kowal-menu-upgrade-cancel-slot")
    private Integer legacyUpgradeCancelSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie ma sie znajdowac przedmiot do ulepszenia?" }) })
    @CustomKey("slots.upgradeItem")
    public int upgradeItemSlot;
    @CustomKey("kowal-menu-upgrade-item-slot")
    private Integer legacyUpgradeItemSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie ma sie znajdowac przycisk od potwierdzenia ulepszenia?" }) })
    @CustomKey("slots.upgradeAccept")
    public int upgradeAcceptSlot;
    @CustomKey("kowal-menu-upgrade-accept-slot")
    private Integer legacyUpgradeAcceptSlot;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja w gui, gdy przedmiot w lapce nie jest do ulepszenia?" }) })
    @CustomKey("menus.notUpgradeable")
    public ItemStack notUpgradeable;
    @CustomKey("kowal-menu-not-upgradeable")
    private ItemStack legacyNotUpgradeable;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja w lore gdy gracz posiada wymagania?" }) })
    @CustomKey("messages.status.canUpgrade")
    public String canUpgradeStatus;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja w lore gdy brakuje przedmiotow?" }) })
    @CustomKey("messages.status.missingRequirements")
    public String missingRequirementsStatus;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja gdy brakuje pieniedzy (tryb MONEY_ONLY/MONEY_AND_ITEMS)." }) })
    @CustomKey("messages.status.missingRequirementsMoneyOnly")
    public String missingRequirementsMoneyOnlyStatus;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja gdy brakuje przedmiotow (tryb ITEMS_ONLY)." }) })
    @CustomKey("messages.status.missingRequirementsItemsOnly")
    public String missingRequirementsItemsOnlyStatus;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac informacja gdy brakuje i pieniedzy i itemow." }) })
    @CustomKey("messages.status.missingRequirementsBoth")
    public String missingRequirementsBothStatus;
    @CustomKey("upgrade-status-false")
    private String legacyCannotUpgradeStatus;
    @CustomKey("upgrade-status-true")
    private String legacyCanUpgradeStatus;
    @Comments({ @Comment, @Comment({ "Format linii kosztu w GUI (placeholder {cost})." }) })
    @CustomKey("messages.requirements.costLine")
    public String costLine;
    @Comments({ @Comment, @Comment({ "Linia itemu gdy gracz ma wymagania." }) })
    @CustomKey("messages.requirements.itemLineHave")
    public String itemLineHave;
    @Comments({ @Comment, @Comment({ "Linia itemu gdy gracz nie ma wymaganej ilosci." }) })
    @CustomKey("messages.requirements.itemLineMissing")
    public String itemLineMissing;
    @Comments({ @Comment, @Comment({ "Dodatkowa linia z informacja ile jeszcze brakuje." }) })
    @CustomKey("messages.requirements.itemMissingHint")
    public String itemMissingHint;
    @Comments({ @Comment, @Comment({ "Opcjonalna linia gdy gracz ma wszystkie przedmioty. (Pusta/null = brak)" }) })
    @CustomKey("messages.requirements.itemHaveHint")
    public String itemHaveHint;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac gui z potwierdzeniem ulepszenia przedmiotu?" }) })
    @CustomKey("menus.confirm")
    public BukkitMenuBuilder confirmMenu;
    @CustomKey("kowal-confirm-menu")
    private BukkitMenuBuilder legacyConfirmMenu;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac przedmiot gdy gracz uzywa metalu?" }) })
    @CustomKey("menus.confirmMetalItem")
    public ItemStack confirmModeMetal;
    @CustomKey("kowal-confirm-menu-metal-item")
    private ItemStack legacyConfirmModeMetal;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac przedmiot gdy gracz uzywa kamienia kowalskiego?" }) })
    @CustomKey("menus.confirmKamienItem")
    public ItemStack confirmModeKamien;
    @CustomKey("kowal-config-menu-kamien-item")
    private ItemStack legacyConfirmModeKamien;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie znajduje sie przycisk od anulowania ulepszenia?" }) })
    @CustomKey("slots.confirmCancel")
    public int confirmCancelSlot;
    @CustomKey("kowal-confirm-menu-cancel-slot")
    private Integer legacyConfirmCancelSlot;
    @Comments({ @Comment, @Comment({ "Na ktorym slocie znajduje sie przycisk od ulepszenia?" }) })
    @CustomKey("slots.confirmAccept")
    public int confirmAcceptSlot;
    @CustomKey("kowal-confirm-menu-accept-slot")
    private Integer legacyConfirmAcceptSlot;
    @Comments({ @Comment, @Comment({ "Lista poziomow kowala" }) })
    @CustomKey("levels")
    public Map<Integer, Level> kowalLevels;
    @CustomKey("kowal-levels")
    private Map<Integer, Level> legacyKowalLevels;
    @Comments({ @Comment, @Comment({ "Jaka nazwe maja posiadac przedmioty po ulepszeniu? (Tylko gdy przedmiot ma podstawowa nazwe)" }) })
    @CustomKey("items.names")
    public Map<Material, String> kowalItems;
    @CustomKey("kowal-items-name")
    private Map<Material, String> legacyKowalItems;
    @Comments({ @Comment, @Comment({ "Jaki kolor ma miec poziom ulepszenia przedmiotu?" }) })
    @CustomKey("items.colors")
    public Map<Material, String> kowalColors;
    @CustomKey("kowal-items-colors")
    private Map<Material, String> legacyKowalColors;
    @Comments({ @Comment, @Comment({ "Jak ma wygladac kamien kowalski?" }) })
    @CustomKey("items.kamienKowalski")
    public ItemStack kamienKowalski;
    @CustomKey("kamien-kowalski-item")
    private ItemStack legacyKamienKowalski;
    @Comments({ @Comment, @Comment({ "Custom model data dla kamienia kowalskiego. (0 = brak)" }) })
    @CustomKey("items.kamienKowalskiCustomModelData")
    public Integer kamienKowalskiCustomModelData;
    @CustomKey("kamien-kowalski-custom-model-data")
    private Integer legacyKamienKowalskiCustomModelData;
    @Comments({ @Comment, @Comment({ "Lista efektow, ktore gracz dostaje losowo na 7 poziomie ulepszenia." }) })
    @CustomKey("effects.list")
    public Map<EffectType, Effect> effects;
    @CustomKey("effect-list")
    private Map<EffectType, Effect> legacyEffects;
    @Comments({ @Comment, @Comment({ "Jaki ma sie odtworzyc dzwiek gdy ulepszenie sie uda?" }) })
    @CustomKey("sounds.upgradeSuccess")
    public String upgradeSuccess;
    @CustomKey("upgrade-success-sound")
    private String legacyUpgradeSuccess;
    @Comments({ @Comment, @Comment({ "Jaki ma sie odtworzyc dzwiek gdy ulepszenie sie nie uda?" }) })
    @CustomKey("sounds.upgradeFailure")
    public String upgradeFailure;
    @CustomKey("upgrade-failure-sound")
    private String legacyUpgradeFailure;
    @Comments({ @Comment, @Comment({ "Lista mozliwych particlesow dla pelnego seta 6 lub 7 poziomu (losowane przy zalozeniu pelnego seta lub mieszanego 6/7)." }) })
    @CustomKey("particles.list")
    public List<Particle> particles;
    @CustomKey("particles")
    private Object legacyParticles;
    @Comments({ @Comment, @Comment({ "Ustawienia Citizens do bypassu auto-equip." }) })
    @CustomKey("integrations.citizens")
    public CitizensSettings citizens;
    @CustomKey("citizens")
    private CitizensSettings legacyCitizens;
    @Comments({ @Comment, @Comment({ "Ustawienia PacketEvents do natychmiastowego resyncu po otwarciu GUI." }) })
    @CustomKey("integrations.packetEventsSync")
    public PacketEventsSyncSettings packetEventsSync;
    @CustomKey("packetEventsSync")
    private PacketEventsSyncSettings legacyPacketEventsSync;
    @Comments({ @Comment, @Comment({ "Tryb kosztow ulepszen: MONEY_ONLY, ITEMS_ONLY, MONEY_AND_ITEMS." }) })
    @CustomKey("economy.paymentMode")
    public String paymentMode;
    private transient PaymentMode resolvedPaymentMode;
    
    public PluginConfig() {
        this.kowalMenu = new BukkitMenuBuilder("&8Kowal", 3, new MapBuilder<Integer, ItemStack>().put(11, ItemBuilder.of(Material.RED_DYE).setName("&cAnuluj").toItemStack()).put(15, ItemBuilder.of(Material.LIME_DYE).setName("&2Zwieksz poziom").setLore("&7Ulepszenie: &a+{level} \u2192 &a+{new}", " ", "&7Wymagane ulepszacze:", "{items}", "{cost}", " ", "{status}").toItemStack()).build());
        this.modeMetal = ItemBuilder.of(Material.LEVER).setName("&9Ulepszanie kamieniem kowalskim").setLore("&7Gdy ulepszasz przedmiot &fKamieniem Kowalskim", "&7masz pewnosc, ze gdy ulepszenie sie", "&7nie powiedzie poziom przedmiotu nie", "&7zostanie obnizony", " ", "&aKliknij aby przelaczyc!").toItemStack();
        this.modeKamien = ItemBuilder.of(Material.REDSTONE_TORCH).setName("&5Ulepszasz metalem!").setLore("&7Ulepszasz przedmiotem specjalnym", "&7metalem, ktory zabezpiecza przed", "&7obnizeniem poziomu przedmiotu.", " ", "&dKliknij aby przelaczyc!").toItemStack();
        this.modeSlot = 22;
        this.upgradeCancelSlot = 11;
        this.upgradeItemSlot = 13;
        this.upgradeAcceptSlot = 15;
        this.notUpgradeable = ItemBuilder.of(Material.BARRIER).setName("&cNiepoprawny przedmiot!").setLore("&7Przedmiot, ktory trzymasz w rece nie posiada", "&7mozliwosci ulepszenia badz jest ulepszony juz", "&7na &fmaksymalny &7poziom", " ", "&7Dostepne przedmioty do ulepszenia:", "&8» &bDiamentowe uzbrojenie", "&8» &cNetherytowe uzbrojenie").toItemStack();
        this.canUpgradeStatus = "&eKliknij, aby ulepszyc!";
        this.missingRequirementsStatus = "&cUzbieraj wymagane przedmioty!";
        this.missingRequirementsMoneyOnlyStatus = "&cNie masz wystarczajaco pieniedzy!";
        this.missingRequirementsItemsOnlyStatus = "&cNie masz wymaganych przedmiotow!";
        this.missingRequirementsBothStatus = "&cBrakuje ci przedmiotow lub pieniedzy!";
        this.costLine = "&8» &a{cost}$";
        this.itemLineHave = "&8» &a{itemName} x{required}";
        this.itemLineMissing = "&8» &c{itemName} x{required}";
        this.itemMissingHint = "&cUzbieraj jeszcze &f{missing} &c({itemName})";
        this.itemHaveHint = "";
        this.confirmMenu = new BukkitMenuBuilder(InventoryType.HOPPER, "&8Czy chcesz kontynuowac?", new MapBuilder<Integer, ItemStack>().put(1, ItemBuilder.of(Material.RED_DYE).setName("&cNIE").setLore("&7Nie chce ryzykowac. Wole zostac", "&7przy aktualnym poziomie ulepszenia.").toItemStack()).build());
        this.confirmModeMetal = ItemBuilder.of(Material.LIME_DYE).setName("&aTAK").setLore("&7Szansa na ulepszenie: &f{chance}%", " ", "&7Jestes swiadomy, ze przy nieudanej", "&7probie &eulepszenia poziom &7przedmiotu zostanie", "&7obnizony o jeden.").toItemStack();
        this.confirmModeKamien = ItemBuilder.of(Material.LIME_DYE).setName("&aTAK").setLore("&7Szansa na ulepszenie: &f{chance}%", " ", "&7To ulepszenie jest &ezabezpieczone", "&7przed obnizeniem poziomu przedmiotu.").toItemStack();
        this.confirmCancelSlot = 1;
        this.confirmAcceptSlot = 3;
        this.kowalLevels = new MapBuilder<Integer, Level>().put(1, new Level(Map.of(Material.COAL, 10), "&8» &aWegiel x10", "&8» &a35$", 35.0, "&b+0.5 odpornosc na obrazenia", 0.0015, 0.0, 80)).put(2, new Level(Map.of(Material.IRON_INGOT, 8), "&8» &aSztabka zelaza x8", "&8» &a50$", 50.0, "&b+1.0 odpornosc na obrazenia", 0.002, 0.0, 70)).put(3, new Level(Map.of(Material.STRING, 16), "&8» &aNi\u0107 x16", "&8» &a75$", 75.0, "&b+1.5 odpornosc na obrazenia", 0.0025, 0.0, 60)).put(4, new Level(Map.of(Material.EMERALD, 2), "&8» &aEmeraldy x2", "&8» &a90$", 90.0, "&b+2.0 odpornosc na obrazenia", 0.003, 0.0, 50)).put(5, new Level(Map.of(Material.IRON_INGOT, 16), "&8» &aSztabka zelaza x16", "&8» &a105$", 105.0, "&b+3.0 odpornosc na obrazenia", 0.0035, 0.0, 40)).put(6, new Level(Map.of(Material.EMERALD, 6), "&8» &aEmeraldy x6", "&8» &a120$", 120.0, "&b+4.0 odpornosc na obrazenia", 0.004, 0.0, 30)).put(7, new Level(Map.of(Material.DIAMOND, 6), "&8» &aDiamenty x6", "&8» &a145$", 145.0, "&b+5.0 odpornosc na obrazenia", 0.0045, 0.0, 20)).build();
        this.kowalItems = new MapBuilder<Material, String>().put(Material.DIAMOND_HELMET, "&3Diamentowy helm").put(Material.DIAMOND_CHESTPLATE, "&3Diamentowa klata").put(Material.DIAMOND_LEGGINGS, "&3Diamentowe spodnie").put(Material.DIAMOND_BOOTS, "&3Diamentowe buty").put(Material.NETHERITE_HELMET, "&cNetherytowy helm").put(Material.NETHERITE_CHESTPLATE, "&cNetherytowa klata").put(Material.NETHERITE_LEGGINGS, "&cNetherytowe spodnie").put(Material.NETHERITE_BOOTS, "&cNetherytowe buty").build();
        this.kowalColors = new MapBuilder<Material, String>().put(Material.DIAMOND_HELMET, "&b+{level}").put(Material.DIAMOND_CHESTPLATE, "&b+{level}").put(Material.DIAMOND_LEGGINGS, "&b+{level}").put(Material.DIAMOND_BOOTS, "&b+{level}").put(Material.NETHERITE_HELMET, "&4+{level}").put(Material.NETHERITE_CHESTPLATE, "&4+{level}").put(Material.NETHERITE_LEGGINGS, "&4+{level}").put(Material.NETHERITE_BOOTS, "&4+{level}").build();
        this.kamienKowalski = ItemBuilder.of(Material.GRAY_DYE).setName("&cKamien kowalski").setLore("&8» &7Powoduje, ze przedmiot po ulepszeniu", "&8» &7u &fKowala &7nie cofa swojego poziomu w", "&8» &7przypadku &eniepowodzenia&7!").toItemStack();
        this.kamienKowalskiCustomModelData = 0;
        this.applyKamienKowalskiCustomModelData();
        this.effects = new MapBuilder<EffectType, Effect>().put(EffectType.ARMOR_DAMAGE, new Effect("&6{chance}% wolniejsze niszczenie seta", 12)).put(EffectType.POTION_DURATION, new Effect("&9{chance}% wydluzenia efektu wypitych mikstur", 12)).put(EffectType.DAMAGE, new Effect("&a{chance}% szansy na odbicie ciosu", 3)).put(EffectType.ARROW, new Effect("&d{chance}% szansy na odbicie strzaly", 10)).build();
        this.upgradeSuccess = "BLOCK_ANVIL_BREAK";
        this.upgradeFailure = "ENTITY_ITEM_BREAK";
        this.particles = List.of(Particle.HAPPY_VILLAGER);
        this.citizens = new CitizensSettings();
        this.packetEventsSync = new PacketEventsSyncSettings();
        this.paymentMode = PaymentMode.MONEY_AND_ITEMS.name();
    }

    public void applyKamienKowalskiCustomModelData() {
        if (this.kamienKowalski == null) {
            return;
        }
        final ItemMeta meta = this.kamienKowalski.getItemMeta();
        if (meta == null) {
            return;
        }
        if (this.kamienKowalskiCustomModelData != null && this.kamienKowalskiCustomModelData > 0) {
            meta.setCustomModelData(this.kamienKowalskiCustomModelData);
        }
        else {
            meta.setCustomModelData(null);
        }
        this.kamienKowalski.setItemMeta(meta);
    }

    public PaymentMode resolvePaymentMode(final Logger logger) {
        if (this.resolvedPaymentMode != null) {
            return this.resolvedPaymentMode;
        }
        final PaymentMode parsed = PaymentMode.fromString(this.paymentMode);
        if (parsed == null) {
            if (logger != null) {
                logger.warning("Config: niepoprawny paymentMode=" + this.paymentMode + ". Ustawiam MONEY_AND_ITEMS.");
            }
            this.paymentMode = PaymentMode.MONEY_AND_ITEMS.name();
            this.resolvedPaymentMode = PaymentMode.MONEY_AND_ITEMS;
            return this.resolvedPaymentMode;
        }
        this.resolvedPaymentMode = parsed;
        return parsed;
    }

    public void applyLegacyFallbacks(final File configFile) {
        final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);
        if (!yaml.contains("menus.kowal") && this.legacyKowalMenu != null) {
            this.kowalMenu = this.legacyKowalMenu;
        }
        if (!yaml.contains("menus.modeMetal") && this.legacyModeMetal != null) {
            this.modeMetal = this.legacyModeMetal;
        }
        if (!yaml.contains("menus.modeKamien") && this.legacyModeKamien != null) {
            this.modeKamien = this.legacyModeKamien;
        }
        if (!yaml.contains("slots.mode") && this.legacyModeSlot != null) {
            this.modeSlot = this.legacyModeSlot;
        }
        if (!yaml.contains("slots.upgradeCancel") && this.legacyUpgradeCancelSlot != null) {
            this.upgradeCancelSlot = this.legacyUpgradeCancelSlot;
        }
        if (!yaml.contains("slots.upgradeItem") && this.legacyUpgradeItemSlot != null) {
            this.upgradeItemSlot = this.legacyUpgradeItemSlot;
        }
        if (!yaml.contains("slots.upgradeAccept") && this.legacyUpgradeAcceptSlot != null) {
            this.upgradeAcceptSlot = this.legacyUpgradeAcceptSlot;
        }
        if (!yaml.contains("menus.notUpgradeable") && this.legacyNotUpgradeable != null) {
            this.notUpgradeable = this.legacyNotUpgradeable;
        }
        if (!yaml.contains("messages.status.canUpgrade") && this.legacyCanUpgradeStatus != null) {
            this.canUpgradeStatus = this.legacyCanUpgradeStatus;
        }
        if (!yaml.contains("messages.status.missingRequirements") && this.legacyCannotUpgradeStatus != null) {
            this.missingRequirementsStatus = this.legacyCannotUpgradeStatus;
        }
        if (!yaml.contains("menus.confirm") && this.legacyConfirmMenu != null) {
            this.confirmMenu = this.legacyConfirmMenu;
        }
        if (!yaml.contains("menus.confirmMetalItem") && this.legacyConfirmModeMetal != null) {
            this.confirmModeMetal = this.legacyConfirmModeMetal;
        }
        if (!yaml.contains("menus.confirmKamienItem") && this.legacyConfirmModeKamien != null) {
            this.confirmModeKamien = this.legacyConfirmModeKamien;
        }
        if (!yaml.contains("slots.confirmCancel") && this.legacyConfirmCancelSlot != null) {
            this.confirmCancelSlot = this.legacyConfirmCancelSlot;
        }
        if (!yaml.contains("slots.confirmAccept") && this.legacyConfirmAcceptSlot != null) {
            this.confirmAcceptSlot = this.legacyConfirmAcceptSlot;
        }
        if (!yaml.contains("levels") && this.legacyKowalLevels != null) {
            this.kowalLevels = this.legacyKowalLevels;
        }
        if (!yaml.contains("items.names") && this.legacyKowalItems != null) {
            this.kowalItems = this.legacyKowalItems;
        }
        if (!yaml.contains("items.colors") && this.legacyKowalColors != null) {
            this.kowalColors = this.legacyKowalColors;
        }
        if (!yaml.contains("items.kamienKowalski") && this.legacyKamienKowalski != null) {
            this.kamienKowalski = this.legacyKamienKowalski;
        }
        if (!yaml.contains("items.kamienKowalskiCustomModelData") && this.legacyKamienKowalskiCustomModelData != null) {
            this.kamienKowalskiCustomModelData = this.legacyKamienKowalskiCustomModelData;
        }
        if (!yaml.contains("effects.list") && this.legacyEffects != null) {
            this.effects = this.legacyEffects;
        }
        if (!yaml.contains("sounds.upgradeSuccess") && this.legacyUpgradeSuccess != null) {
            this.upgradeSuccess = this.legacyUpgradeSuccess;
        }
        if (!yaml.contains("sounds.upgradeFailure") && this.legacyUpgradeFailure != null) {
            this.upgradeFailure = this.legacyUpgradeFailure;
        }
        if (!yaml.contains("particles.list") && this.legacyParticles != null) {
            this.particles = this.resolveLegacyParticles(this.legacyParticles);
        }
        if (!yaml.contains("integrations.citizens") && this.legacyCitizens != null) {
            this.citizens = this.legacyCitizens;
        }
        if (!yaml.contains("integrations.packetEventsSync") && this.legacyPacketEventsSync != null) {
            this.packetEventsSync = this.legacyPacketEventsSync;
        }
        this.resolvedPaymentMode = null;
    }

    public void ensureRequirementMessages() {
        if (this.canUpgradeStatus == null) {
            this.canUpgradeStatus = "&eKliknij, aby ulepszyc!";
        }
        if (this.missingRequirementsStatus == null) {
            this.missingRequirementsStatus = "&cUzbieraj wymagane przedmioty!";
        }
        if (this.missingRequirementsMoneyOnlyStatus == null) {
            this.missingRequirementsMoneyOnlyStatus = "&cNie masz wystarczajaco pieniedzy!";
        }
        if (this.missingRequirementsItemsOnlyStatus == null) {
            this.missingRequirementsItemsOnlyStatus = "&cNie masz wymaganych przedmiotow!";
        }
        if (this.missingRequirementsBothStatus == null) {
            this.missingRequirementsBothStatus = "&cBrakuje ci przedmiotow lub pieniedzy!";
        }
        if (this.costLine == null) {
            this.costLine = "&8» &a{cost}$";
        }
        if (this.itemLineHave == null) {
            this.itemLineHave = "&8» &a{itemName} x{required}";
        }
        if (this.itemLineMissing == null) {
            this.itemLineMissing = "&8» &c{itemName} x{required}";
        }
        if (this.itemMissingHint == null) {
            this.itemMissingHint = "&cUzbieraj jeszcze &f{missing} &c({itemName})";
        }
        if (this.itemHaveHint == null) {
            this.itemHaveHint = "";
        }
    }

    private List<Particle> resolveLegacyParticles(Object legacyParticlesValue) {
        if (legacyParticlesValue instanceof List<?> legacyList) {
            return this.mapParticlesFromList(legacyList);
        }
        if (legacyParticlesValue instanceof Map<?, ?> legacyMap) {
            Object listValue = legacyMap.get("list");
            if (listValue instanceof List<?> legacyList) {
                return this.mapParticlesFromList(legacyList);
            }
        }
        return this.particles;
    }

    private List<Particle> mapParticlesFromList(List<?> legacyList) {
        return legacyList.stream()
                .map(entry -> {
                    if (entry instanceof Particle particle) {
                        return particle;
                    }
                    return Particle.valueOf(String.valueOf(entry));
                })
                .toList();
    }

    public static class CitizensSettings extends OkaeriConfig {
        @Comment("Czy integracja Citizens jest aktywna.")
        @CustomKey("enabled")
        public boolean enabled;

        @Comment("Lista Citizens NPC IDs, dla ktorych bypassujemy auto-equip.")
        @CustomKey("bypassSetNpcIds")
        public List<Integer> bypassSetNpcIds;

        @Comment("Czy wlaczyc logi debug integracji Citizens.")
        @CustomKey("debug")
        public boolean debug;

        public CitizensSettings() {
            this.enabled = true;
            this.bypassSetNpcIds = new ArrayList<>();
            this.debug = false;
        }
    }

    public static class PacketEventsSyncSettings extends OkaeriConfig {
        @Comment("Czy wlaczyc natychmiastowy resync slotow PacketEvents po otwarciu GUI.")
        @CustomKey("enabled")
        public boolean enabled;

        @Comment("Czy wlaczyc logi debug dla PacketEvents resync.")
        @CustomKey("debug")
        public boolean debug;

        public PacketEventsSyncSettings() {
            this.enabled = true;
            this.debug = false;
        }
    }
}
