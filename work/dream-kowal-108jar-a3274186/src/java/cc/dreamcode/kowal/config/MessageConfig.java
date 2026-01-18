package cc.dreamcode.kowal.config;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure.BukkitNotice;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Header;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Headers;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;

@Configuration(child = "message.yml")
@Headers({ @Header({ "## Dream-Kowal (Message-Config) ##" }), @Header({ "Dostepne type: (DO_NOT_SEND, CHAT, ACTION_BAR, SUBTITLE, TITLE, TITLE_SUBTITLE)" }) })
public class MessageConfig extends OkaeriConfig
{
    @CustomKey("command-usage")
    public BukkitNotice usage;
    @CustomKey("command-usage-help")
    public BukkitNotice usagePath;
    @CustomKey("command-usage-not-found")
    public BukkitNotice usageNotFound;
    @CustomKey("command-path-not-found")
    public BukkitNotice pathNotFound;
    @CustomKey("command-no-permission")
    public BukkitNotice noPermission;
    @CustomKey("command-not-player")
    public BukkitNotice notPlayer;
    @CustomKey("command-not-console")
    public BukkitNotice notConsole;
    @CustomKey("command-invalid-format")
    public BukkitNotice invalidFormat;
    @CustomKey("player-not-found")
    public BukkitNotice playerNotFound;
    @CustomKey("world-not-found")
    public BukkitNotice worldNotFound;
    @CustomKey("config-reloaded")
    public BukkitNotice reloaded;
    @CustomKey("config-reload-error")
    public BukkitNotice reloadError;
    @CustomKey("upgrade-success")
    public BukkitNotice upgradeSuccess;
    @CustomKey("upgrade-failure")
    public BukkitNotice upgradeFailure;
    @CustomKey("kamien-kowalski-required")
    public BukkitNotice kamienRequired;
    @CustomKey("upgrade-cannot-afford")
    public BukkitNotice cannotAfford;
    @CustomKey("command-set-kamien-air")
    public BukkitNotice kamienAir;
    @CustomKey("command-set-kamien-success")
    public BukkitNotice kamienSet;
    @CustomKey("command-kamien-give-all-player")
    public BukkitNotice giveAllPlayer;
    @CustomKey("command-kamien-give-all")
    public BukkitNotice giveAll;
    @CustomKey("command-kamien-give-player")
    public BukkitNotice givePlayer;
    @CustomKey("command-kamien-give")
    public BukkitNotice give;
    @CustomKey("command-upgrade-air")
    public BukkitNotice commandUpgradeAir;
    @CustomKey("command-upgrade-not-armor")
    public BukkitNotice commandUpgradeNotArmor;
    @CustomKey("command-upgrade-level-not-exists")
    public BukkitNotice commandUpgradeLevelError;
    @CustomKey("command-upgrade-success")
    public BukkitNotice commandUpgradeSuccess;
    @CustomKey("effect-arrow-use")
    public BukkitNotice arrowUse;
    @CustomKey("effect-damage-use-player")
    public BukkitNotice damageUsePlayer;
    @CustomKey("effect-damage-use-damger")
    public BukkitNotice damageUseDamager;
    
    public MessageConfig() {
        this.usage = BukkitNotice.chat("&7Przyklady uzycia komendy: &c{label}");
        this.usagePath = BukkitNotice.chat("&f{usage} &8- &7{description}");
        this.usageNotFound = BukkitNotice.chat("&cNie znaleziono pasujacych do kryteriow komendy.");
        this.pathNotFound = BukkitNotice.chat("&cTa komenda jest pusta lub nie posiadasz dostepu do niej.");
        this.noPermission = BukkitNotice.chat("&cNie posiadasz uprawnien.");
        this.notPlayer = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
        this.notConsole = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu konsoli.");
        this.invalidFormat = BukkitNotice.chat("&cPodano nieprawidlowy format argumentu komendy. ({input})");
        this.playerNotFound = BukkitNotice.chat("&cPodanego gracza nie znaleziono.");
        this.worldNotFound = BukkitNotice.chat("&cPodanego swiata nie znaleziono.");
        this.reloaded = BukkitNotice.chat("&aPrzeladowano! &7({time})");
        this.reloadError = BukkitNotice.chat("&cZnaleziono problem w konfiguracji: &6{error}");
        this.upgradeSuccess = BukkitNotice.titleSubtitle("&aUlepszono!", "&7Udalo Ci sie ulepszyc przedmiot!");
        this.upgradeFailure = BukkitNotice.titleSubtitle("&cNie ulepszono!", "&7Tym razem nie udalo Ci sie ulepszyc przedmiotu!");
        this.kamienRequired = BukkitNotice.titleSubtitle("&cNie posiadasz magicznego kamienia!", "&7Aby ulepszyc przedmiot, musisz posiadac magiczny kamien!");
        this.cannotAfford = BukkitNotice.chat("&cNie stac cie na to ulepszenie!");
        this.kamienAir = BukkitNotice.chat("&cMusisz trzymac przedmiot w rece.");
        this.kamienSet = BukkitNotice.chat("&aUstawiono przedmiot kamienia kowalskiego.");
        this.giveAllPlayer = BukkitNotice.chat("&aOtrzymales kamien kowalski w ilosci &2x{amount}&a.");
        this.giveAll = BukkitNotice.chat("&aNadales kamien kowalski wszystkim graczom w ilosci &2{amount}&a.");
        this.givePlayer = BukkitNotice.chat("&aOtrzymales kamien kowalski w ilosci &2x{amount}&a.");
        this.give = BukkitNotice.chat("&aNadales kamien kowalski graczowi &2{player} &aw ilosci &2x{amount}&a.");
        this.commandUpgradeAir = BukkitNotice.chat("&cMusisz trzymac przedmiot w rece.");
        this.commandUpgradeNotArmor = BukkitNotice.chat("&cTego przedmiotu nie da sie ulepszyc.");
        this.commandUpgradeLevelError = BukkitNotice.chat("&cPoziom ulepszenia nie moze byc wyzszy niz 7.");
        this.commandUpgradeSuccess = BukkitNotice.chat("&aUlepszono przedmiot w rece na poziom &2{level}&a.");
        this.arrowUse = BukkitNotice.chat("&aStrzala zostala odbita!");
        this.damageUsePlayer = BukkitNotice.chat("&aZadane obrazenia zostaly odbite!");
        this.damageUseDamager = BukkitNotice.chat("&cGracz odbil zadane obrazenia!");
    }
}
