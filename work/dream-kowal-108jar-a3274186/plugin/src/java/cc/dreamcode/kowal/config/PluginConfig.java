package cc.dreamcode.kowal.config;

import cc.dreamcode.kowal.economy.PaymentMode;
import cc.dreamcode.kowal.effect.Effect;
import cc.dreamcode.kowal.effect.EffectType;
import cc.dreamcode.kowal.level.Level;
import cc.dreamcode.menu.adventure.BukkitMenuBuilder;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import eu.okaeri.configs.annotation.Header;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Configuration(child = "config.yml")
@Header({ "## Dream-Kowal (Main-Config) ##" })
public class PluginConfig extends OkaeriConfig {
    @Comments({ @Comment, @Comment({ "Jak ma wygladac gui kowala?" }) })
    public Menus menus;

    @Comments({ @Comment, @Comment({ "Ustawienia slotow w GUI." }) })
    public Slots slots;

    @Comments({ @Comment, @Comment({ "Teksty widoczne w GUI." }) })
    public Messages messages;

    @Comments({ @Comment, @Comment({ "Ustawienia ekonomii." }) })
    public Economy economy;

    @Comments({ @Comment, @Comment({ "Lista poziomow kowala" }) })
    public Map<Integer, Level> levels;

    @Comments({ @Comment, @Comment({ "Ustawienia przedmiotow." }) })
    public Items items;

    @Comments({ @Comment, @Comment({ "Efekty losowe na 7 poziomie." }) })
    public Effects effects;

    @Comments({ @Comment, @Comment({ "Dzwieki ulepszenia." }) })
    public Sounds sounds;

    @Comments({ @Comment, @Comment({ "Casteczki dla pelnego seta." }) })
    public List<Particle> particles;

    @Comments({ @Comment, @Comment({ "Integracje z innymi pluginami." }) })
    public Integrations integrations;

    private transient PaymentMode resolvedPaymentMode;

    public PluginConfig() {
        this.menus = new Menus();
        this.slots = new Slots();
        this.messages = new Messages();
        this.economy = new Economy();
        this.levels = new MapBuilder<Integer, Level>()
                .put(1, new Level(Map.of(Material.COAL, 10), "&8» &aWegiel x10", "&8» &a35$", 35.0, "&b+0.5 odpornosc na obrazenia", 0.0015, 0.0, 80))
                .put(2, new Level(Map.of(Material.IRON_INGOT, 8), "&8» &aSztabka zelaza x8", "&8» &a50$", 50.0, "&b+1.0 odpornosc na obrazenia", 0.002, 0.0, 70))
                .put(3, new Level(Map.of(Material.STRING, 16), "&8» &aNi\u0107 x16", "&8» &a75$", 75.0, "&b+1.5 odpornosc na obrazenia", 0.0025, 0.0, 60))
                .put(4, new Level(Map.of(Material.EMERALD, 2), "&8» &aEmeraldy x2", "&8» &a90$", 90.0, "&b+2.0 odpornosc na obrazenia", 0.003, 0.0, 50))
                .put(5, new Level(Map.of(Material.IRON_INGOT, 16), "&8» &aSztabka zelaza x16", "&8» &a105$", 105.0, "&b+3.0 odpornosc na obrazenia", 0.0035, 0.0, 40))
                .put(6, new Level(Map.of(Material.EMERALD, 6), "&8» &aEmeraldy x6", "&8» &a120$", 120.0, "&b+4.0 odpornosc na obrazenia", 0.004, 0.0, 30))
                .put(7, new Level(Map.of(Material.DIAMOND, 6), "&8» &aDiamenty x6", "&8» &a145$", 145.0, "&b+5.0 odpornosc na obrazenia", 0.0045, 0.0, 20))
                .build();
        this.items = new Items();
        this.items.applyKamienKowalskiCustomModelData();
        this.effects = new Effects();
        this.sounds = new Sounds();
        this.particles = List.of(Particle.HAPPY_VILLAGER);
        this.integrations = new Integrations();
    }

    public PaymentMode resolvePaymentMode(final Logger logger) {
        if (this.resolvedPaymentMode != null) {
            return this.resolvedPaymentMode;
        }
        if (this.economy == null) {
            this.economy = new Economy();
        }
        final PaymentMode parsed = PaymentMode.fromString(this.economy.paymentMode);
        if (parsed == null) {
            if (logger != null) {
                logger.warning("Config: niepoprawny paymentMode=" + this.economy.paymentMode + ". Ustawiam MONEY_AND_ITEMS.");
            }
            this.economy.paymentMode = PaymentMode.MONEY_AND_ITEMS.name();
            this.resolvedPaymentMode = PaymentMode.MONEY_AND_ITEMS;
            return this.resolvedPaymentMode;
        }
        this.resolvedPaymentMode = parsed;
        return parsed;
    }

    public void ensureRequirementMessages() {
        if (this.messages == null) {
            this.messages = new Messages();
            return;
        }
        if (this.messages.status == null) {
            this.messages.status = new Status();
        }
        if (this.messages.requirements == null) {
            this.messages.requirements = new Requirements();
        }
        if (this.messages.status.canUpgrade == null) {
            this.messages.status.canUpgrade = "&eKliknij, aby ulepszyc!";
        }
        if (this.messages.status.missingRequirements == null) {
            this.messages.status.missingRequirements = "&cUzbieraj wymagane przedmioty!";
        }
        if (this.messages.status.missingRequirementsMoneyOnly == null) {
            this.messages.status.missingRequirementsMoneyOnly = "&cNie masz wystarczajaco pieniedzy!";
        }
        if (this.messages.status.missingRequirementsItemsOnly == null) {
            this.messages.status.missingRequirementsItemsOnly = "&cNie masz wymaganych przedmiotow!";
        }
        if (this.messages.status.missingRequirementsBoth == null) {
            this.messages.status.missingRequirementsBoth = "&cBrakuje ci przedmiotow lub pieniedzy!";
        }
        if (this.messages.requirements.costLine == null) {
            this.messages.requirements.costLine = "&8» &a{cost}$";
        }
        if (this.messages.requirements.itemLineHave == null) {
            this.messages.requirements.itemLineHave = "&8» &a{itemName} x{required}";
        }
        if (this.messages.requirements.itemLineMissing == null) {
            this.messages.requirements.itemLineMissing = "&8» &c{itemName} x{required}";
        }
        if (this.messages.requirements.itemMissingHint == null) {
            this.messages.requirements.itemMissingHint = "&cUzbieraj jeszcze &f{missing} &c({itemName})";
        }
        if (this.messages.requirements.itemHaveHint == null) {
            this.messages.requirements.itemHaveHint = "";
        }
    }

    public static class Menus extends OkaeriConfig {
        @Comment("Jak ma wygladac gui kowala?")
        public BukkitMenuBuilder kowal;

        @Comment("Jak ma wygladac informacja o zmianie trybu na kamien kowalski?")
        public ItemStack modeMetal;

        @Comment("Jak ma wygladac informacja o zmianie trybu na metal?")
        public ItemStack modeKamien;

        @Comment("Jak ma wygladac informacja w gui, gdy przedmiot w lapce nie jest do ulepszenia?")
        public ItemStack notUpgradeable;

        @Comment("Jak ma wygladac gui z potwierdzeniem ulepszenia przedmiotu?")
        public BukkitMenuBuilder confirm;

        @Comment("Jak ma wygladac przedmiot gdy gracz uzywa metalu?")
        public ItemStack confirmMetalItem;

        @Comment("Jak ma wygladac przedmiot gdy gracz uzywa kamienia kowalskiego?")
        public ItemStack confirmKamienItem;

        public Menus() {
            this.kowal = new BukkitMenuBuilder(
                    "&8Kowal",
                    3,
                    new MapBuilder<Integer, ItemStack>()
                            .put(11, ItemBuilder.of(Material.RED_DYE).setName("&cAnuluj").toItemStack())
                            .put(15, ItemBuilder.of(Material.LIME_DYE).setName("&2Zwieksz poziom")
                                    .setLore("&7Ulepszenie: &a+{level} \u2192 &a+{new}", " ", "&7Wymagane ulepszacze:", "{items}", "{cost}", " ", "{status}")
                                    .toItemStack())
                            .build()
            );
            this.modeMetal = ItemBuilder.of(Material.LEVER)
                    .setName("&9Ulepszanie kamieniem kowalskim")
                    .setLore("&7Gdy ulepszasz przedmiot &fKamieniem Kowalskim", "&7masz pewnosc, ze gdy ulepszenie sie", "&7nie powiedzie poziom przedmiotu nie", "&7zostanie obnizony", " ", "&aKliknij aby przelaczyc!")
                    .toItemStack();
            this.modeKamien = ItemBuilder.of(Material.REDSTONE_TORCH)
                    .setName("&5Ulepszasz metalem!")
                    .setLore("&7Ulepszasz przedmiotem specjalnym", "&7metalem, ktory zabezpiecza przed", "&7obnizeniem poziomu przedmiotu.", " ", "&dKliknij aby przelaczyc!")
                    .toItemStack();
            this.notUpgradeable = ItemBuilder.of(Material.BARRIER)
                    .setName("&cNiepoprawny przedmiot!")
                    .setLore("&7Przedmiot, ktory trzymasz w rece nie posiada", "&7mozliwosci ulepszenia badz jest ulepszony juz", "&7na &fmaksymalny &7poziom", " ", "&7Dostepne przedmioty do ulepszenia:", "&8» &bDiamentowe uzbrojenie", "&8» &cNetherytowe uzbrojenie")
                    .toItemStack();
            this.confirm = new BukkitMenuBuilder(
                    InventoryType.HOPPER,
                    "&8Czy chcesz kontynuowac?",
                    new MapBuilder<Integer, ItemStack>()
                            .put(1, ItemBuilder.of(Material.RED_DYE).setName("&cNIE")
                                    .setLore("&7Nie chce ryzykowac. Wole zostac", "&7przy aktualnym poziomie ulepszenia.")
                                    .toItemStack())
                            .build()
            );
            this.confirmMetalItem = ItemBuilder.of(Material.LIME_DYE)
                    .setName("&aTAK")
                    .setLore("&7Szansa na ulepszenie: &f{chance}%", " ", "&7Jestes swiadomy, ze przy nieudanej", "&7probie &eulepszenia poziom &7przedmiotu zostanie", "&7obnizony o jeden.")
                    .toItemStack();
            this.confirmKamienItem = ItemBuilder.of(Material.LIME_DYE)
                    .setName("&aTAK")
                    .setLore("&7Szansa na ulepszenie: &f{chance}%", " ", "&7To ulepszenie jest &ezabezpieczone", "&7przed obnizeniem poziomu przedmiotu.")
                    .toItemStack();
        }
    }

    public static class Slots extends OkaeriConfig {
        @Comment("Na ktorym slocie znajduje sie przycisk od zmiany trybu kowala?")
        public int mode;

        @Comment("Na ktorym slocie ma sie znajdowac przycisk od anulowania ulepszenia?")
        public int upgradeCancel;

        @Comment("Na ktorym slocie ma sie znajdowac przedmiot do ulepszenia?")
        public int upgradeItem;

        @Comment("Na ktorym slocie ma sie znajdowac przycisk od potwierdzenia ulepszenia?")
        public int upgradeAccept;

        @Comment("Na ktorym slocie znajduje sie przycisk od anulowania ulepszenia?")
        public int confirmCancel;

        @Comment("Na ktorym slocie znajduje sie przycisk od ulepszenia?")
        public int confirmAccept;

        public Slots() {
            this.mode = 22;
            this.upgradeCancel = 11;
            this.upgradeItem = 13;
            this.upgradeAccept = 15;
            this.confirmCancel = 1;
            this.confirmAccept = 3;
        }
    }

    public static class Messages extends OkaeriConfig {
        public Status status;
        public Requirements requirements;

        public Messages() {
            this.status = new Status();
            this.requirements = new Requirements();
        }
    }

    public static class Status extends OkaeriConfig {
        @Comment("Jak ma wygladac informacja w lore gdy gracz posiada wymagania?")
        public String canUpgrade;

        @Comment("Jak ma wygladac informacja w lore gdy brakuje przedmiotow?")
        public String missingRequirements;

        @Comment("Jak ma wygladac informacja gdy brakuje pieniedzy (tryb MONEY_ONLY/MONEY_AND_ITEMS).")
        public String missingRequirementsMoneyOnly;

        @Comment("Jak ma wygladac informacja gdy brakuje przedmiotow (tryb ITEMS_ONLY).")
        public String missingRequirementsItemsOnly;

        @Comment("Jak ma wygladac informacja gdy brakuje i pieniedzy i itemow.")
        public String missingRequirementsBoth;

        public Status() {
            this.canUpgrade = "&eKliknij, aby ulepszyc!";
            this.missingRequirements = "&cUzbieraj wymagane przedmioty!";
            this.missingRequirementsMoneyOnly = "&cNie masz wystarczajaco pieniedzy!";
            this.missingRequirementsItemsOnly = "&cNie masz wymaganych przedmiotow!";
            this.missingRequirementsBoth = "&cBrakuje ci przedmiotow lub pieniedzy!";
        }
    }

    public static class Requirements extends OkaeriConfig {
        @Comment("Format linii kosztu w GUI (placeholder {cost}).")
        public String costLine;

        @Comment("Linia itemu gdy gracz ma wymagania.")
        public String itemLineHave;

        @Comment("Linia itemu gdy gracz nie ma wymaganej ilosci.")
        public String itemLineMissing;

        @Comment("Dodatkowa linia z informacja ile jeszcze brakuje.")
        public String itemMissingHint;

        @Comment("Opcjonalna linia gdy gracz ma wszystkie przedmioty. (Pusta/null = brak)")
        public String itemHaveHint;

        public Requirements() {
            this.costLine = "&8» &a{cost}$";
            this.itemLineHave = "&8» &a{itemName} x{required}";
            this.itemLineMissing = "&8» &c{itemName} x{required}";
            this.itemMissingHint = "&cUzbieraj jeszcze &f{missing} &c({itemName})";
            this.itemHaveHint = "";
        }
    }

    public static class Economy extends OkaeriConfig {
        @Comment("Tryb kosztow ulepszen: MONEY_ONLY, ITEMS_ONLY, MONEY_AND_ITEMS.")
        public String paymentMode;

        public Economy() {
            this.paymentMode = PaymentMode.MONEY_AND_ITEMS.name();
        }
    }

    public static class Items extends OkaeriConfig {
        @Comment("Jaka nazwe maja posiadac przedmioty po ulepszeniu? (Tylko gdy przedmiot ma podstawowa nazwe)")
        public Map<Material, String> names;

        @Comment("Jaki kolor ma miec poziom ulepszenia przedmiotu?")
        public Map<Material, String> colors;

        @Comment("Jak ma wygladac kamien kowalski?")
        public ItemStack kamienKowalski;

        @Comment("Custom model data dla kamienia kowalskiego. (0 = brak)")
        public Integer kamienKowalskiCustomModelData;

        public Items() {
            this.names = new MapBuilder<Material, String>()
                    .put(Material.DIAMOND_HELMET, "&3Diamentowy helm")
                    .put(Material.DIAMOND_CHESTPLATE, "&3Diamentowa klata")
                    .put(Material.DIAMOND_LEGGINGS, "&3Diamentowe spodnie")
                    .put(Material.DIAMOND_BOOTS, "&3Diamentowe buty")
                    .put(Material.NETHERITE_HELMET, "&cNetherytowy helm")
                    .put(Material.NETHERITE_CHESTPLATE, "&cNetherytowa klata")
                    .put(Material.NETHERITE_LEGGINGS, "&cNetherytowe spodnie")
                    .put(Material.NETHERITE_BOOTS, "&cNetherytowe buty")
                    .build();
            this.colors = new MapBuilder<Material, String>()
                    .put(Material.DIAMOND_HELMET, "&b+{level}")
                    .put(Material.DIAMOND_CHESTPLATE, "&b+{level}")
                    .put(Material.DIAMOND_LEGGINGS, "&b+{level}")
                    .put(Material.DIAMOND_BOOTS, "&b+{level}")
                    .put(Material.NETHERITE_HELMET, "&4+{level}")
                    .put(Material.NETHERITE_CHESTPLATE, "&4+{level}")
                    .put(Material.NETHERITE_LEGGINGS, "&4+{level}")
                    .put(Material.NETHERITE_BOOTS, "&4+{level}")
                    .build();
            this.kamienKowalski = ItemBuilder.of(Material.GRAY_DYE)
                    .setName("&cKamien kowalski")
                    .setLore("&8» &7Powoduje, ze przedmiot po ulepszeniu", "&8» &7u &fKowala &7nie cofa swojego poziomu w", "&8» &7przypadku &eniepowodzenia&7!")
                    .toItemStack();
            this.kamienKowalskiCustomModelData = 0;
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
    }

    public static class Effects extends OkaeriConfig {
        @Comment("Lista efektow, ktore gracz dostaje losowo na 7 poziomie ulepszenia.")
        public Map<EffectType, Effect> list;

        public Effects() {
            this.list = new MapBuilder<EffectType, Effect>()
                    .put(EffectType.ARMOR_DAMAGE, new Effect("&6{chance}% wolniejsze niszczenie seta", 12))
                    .put(EffectType.POTION_DURATION, new Effect("&9{chance}% wydluzenia efektu wypitych mikstur", 12))
                    .put(EffectType.DAMAGE, new Effect("&a{chance}% szansy na odbicie ciosu", 3))
                    .put(EffectType.ARROW, new Effect("&d{chance}% szansy na odbicie strzaly", 10))
                    .build();
        }
    }

    public static class Sounds extends OkaeriConfig {
        @Comment("Jaki ma sie odtworzyc dzwiek gdy ulepszenie sie uda?")
        public String upgradeSuccess;

        @Comment("Jaki ma sie odtworzyc dzwiek gdy ulepszenie sie nie uda?")
        public String upgradeFailure;

        public Sounds() {
            this.upgradeSuccess = "BLOCK_ANVIL_BREAK";
            this.upgradeFailure = "ENTITY_ITEM_BREAK";
        }
    }

    public static class Integrations extends OkaeriConfig {
        @Comment("Ustawienia Citizens do bypassu auto-equip.")
        public CitizensSettings citizens;

        @Comment("Ustawienia PacketEvents do natychmiastowego resyncu po otwarciu GUI.")
        public PacketEventsSyncSettings packetEventsSync;

        public Integrations() {
            this.citizens = new CitizensSettings();
            this.packetEventsSync = new PacketEventsSyncSettings();
        }
    }

    public static class CitizensSettings extends OkaeriConfig {
        @Comment("Czy integracja Citizens jest aktywna.")
        public boolean enabled;

        @Comment("Lista Citizens NPC IDs, dla ktorych bypassujemy auto-equip.")
        public List<Integer> bypassSetNpcIds;

        @Comment("Czy wlaczyc logi debug integracji Citizens.")
        public boolean debug;

        public CitizensSettings() {
            this.enabled = true;
            this.bypassSetNpcIds = new ArrayList<>();
            this.debug = false;
        }
    }

    public static class PacketEventsSyncSettings extends OkaeriConfig {
        @Comment("Czy wlaczyc natychmiastowy resync slotow PacketEvents po otwarciu GUI.")
        public boolean enabled;

        @Comment("Czy wlaczyc logi debug dla PacketEvents resync.")
        public boolean debug;

        public PacketEventsSyncSettings() {
            this.enabled = true;
            this.debug = false;
        }
    }
}
