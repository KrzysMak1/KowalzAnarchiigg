package cc.dreamcode.kowal;

import cc.dreamcode.platform.bukkit.hook.PluginHook;
import cc.dreamcode.kowal.hook.VaultHook;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.Generated;
import cc.dreamcode.kowal.effect.EffectSerializer;
import cc.dreamcode.kowal.level.LevelSerializer;
import cc.dreamcode.menu.adventure.serializer.MenuBuilderSerializer;
import eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.notice.serializer.BukkitNoticeSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.kowal.command.KowalCommand;
import cc.dreamcode.kowal.controller.EffectController;
import cc.dreamcode.kowal.controller.PlayerController;
import cc.dreamcode.kowal.controller.ArmorEquipController;
import cc.dreamcode.kowal.controller.DamageController;
import cc.dreamcode.kowal.citizens.CitizensBypassService;
import cc.dreamcode.kowal.listener.CitizensNpcCommandListener;
import cc.dreamcode.kowal.listener.KowalMenuCloseListener;
import cc.dreamcode.kowal.hook.PacketEventsSupport;
import cc.dreamcode.kowal.tasks.ParticleTask;
import java.util.function.Consumer;
import cc.dreamcode.platform.bukkit.hook.PluginHookManager;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.kowal.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.kowal.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.kowal.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.kowal.command.result.BukkitNoticeResolver;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.notice.bukkit.BukkitNoticeProvider;
import cc.dreamcode.menu.adventure.BukkitMenuProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import cc.dreamcode.utilities.bukkit.VersionUtil;
import lombok.NonNull;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;

public final class KowalPlugin extends DreamBukkitPlatform implements DreamBukkitConfig
{
    private static KowalPlugin instance;
    private BukkitNoticeProvider noticeProvider;
    private BukkitAudiences bukkitAudiences;
    
    @Override
    public void load(@NonNull final ComponentService componentService) {
        if (componentService == null) {
            throw new NullPointerException("componentService is marked non-null but is null");
        }
        KowalPlugin.instance = this;
    }
    
    public void enable(@NonNull final ComponentService componentService) {
        if (componentService == null) {
            throw new NullPointerException("componentService is marked non-null but is null");
        }
        if (!VersionUtil.isPaper()) {
            throw new RuntimeException("Plugin works only on paper fork.");
        }
        this.saveResource("config.yml", false);
        this.saveResource("message.yml", false);
        componentService.setDebug(false);
        this.registerInjectable(BukkitTasker.newPool((Plugin)this));
        this.registerInjectable(BukkitMenuProvider.create((Plugin)this));
        this.noticeProvider = BukkitNoticeProvider.create((Plugin)this);
        this.bukkitAudiences = this.noticeProvider.getBukkitAudiences();
        this.registerInjectable(this.noticeProvider);
        this.registerInjectable(BukkitCommandProvider.create((Plugin)this));
        componentService.registerExtension(DreamCommandExtension.class);
        componentService.registerResolver(ConfigurationResolver.class);
        componentService.registerComponent(MessageConfig.class);
        componentService.registerComponent(BukkitNoticeResolver.class);
        componentService.registerComponent(InvalidInputHandlerImpl.class);
        componentService.registerComponent(InvalidPermissionHandlerImpl.class);
        componentService.registerComponent(InvalidSenderHandlerImpl.class);
        componentService.registerComponent(InvalidUsageHandlerImpl.class);
        componentService.registerComponent(PluginConfig.class);
        componentService.registerComponent(PluginHookManager.class, (java.util.function.Consumer<PluginHookManager>)(pluginHookManager -> pluginHookManager.registerHook(VaultHook.class)));
        componentService.registerComponent(ParticleCache.class, (java.util.function.Consumer<ParticleCache>)ParticleCache::checkOnline);
        componentService.registerComponent(PacketEventsSupport.class);
        componentService.registerComponent(ParticleTask.class);
        componentService.registerComponent(CitizensBypassService.class);
        componentService.registerComponent(DamageController.class);
        componentService.registerComponent(ArmorEquipController.class);
        componentService.registerComponent(PlayerController.class);
        componentService.registerComponent(EffectController.class);
        componentService.registerComponent(KowalCommand.class);
        this.getInject(PluginConfig.class).ifPresent(pluginConfig -> {
            this.validateConfig(pluginConfig);
            this.registerCitizensListener(pluginConfig);
        });
    }
    
    public void disable() {
        this.getServer().getScheduler().cancelTasks(this);
        this.getInject(ParticleCache.class).ifPresent(ParticleCache::clear);
        if (this.bukkitAudiences != null) {
            this.bukkitAudiences.close();
            this.bukkitAudiences = null;
        }
        this.noticeProvider = null;
    }
    
    @NonNull
    public DreamVersion getDreamVersion() {
        return DreamVersion.create("Dream-Kowal", "1.0.8", "Sebt");
    }
    
    @NonNull
    @Override
    public OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerializer());
            registry.register(new MenuBuilderSerializer());
            registry.register(new LevelSerializer());
            registry.register(new EffectSerializer());
        };
    }
    
    @Generated
    public static KowalPlugin getInstance() {
        return KowalPlugin.instance;
    }

    private void validateConfig(final PluginConfig pluginConfig) {
        pluginConfig.applyLegacyFallbacks(new java.io.File(this.getDataFolder(), "config.yml"));
        pluginConfig.applyKamienKowalskiCustomModelData();
        pluginConfig.ensureRequirementMessages();
        pluginConfig.resolvePaymentMode(this.getLogger());
        if (pluginConfig.kowalItems == null || pluginConfig.kowalItems.isEmpty()) {
            this.getLogger().warning("Config: brak ustawien 'items.names'.");
        }
        if (pluginConfig.kowalLevels == null || pluginConfig.kowalLevels.isEmpty()) {
            this.getLogger().warning("Config: brak ustawien 'levels'.");
        }
        if (pluginConfig.effects == null || pluginConfig.effects.isEmpty()) {
            this.getLogger().warning("Config: brak ustawien 'effects.list'.");
        }
        if (pluginConfig.kowalLevels != null) {
            for (int level = 1; level <= 7; level++) {
                if (!pluginConfig.kowalLevels.containsKey(level)) {
                    this.getLogger().warning("Config: brak definicji poziomu kowala " + level + ".");
                }
            }
        }
    }

    private void registerCitizensListener(final PluginConfig pluginConfig) {
        final PluginConfig.CitizensSettings citizensSettings = pluginConfig.citizens;
        if (citizensSettings == null || !citizensSettings.enabled) {
            return;
        }
        if (!this.getServer().getPluginManager().isPluginEnabled("Citizens")) {
            return;
        }
        if (citizensSettings.debug) {
            this.getLogger().info("Wykryto Citizens. Integracja NPC jest aktywna.");
        }
        this.getServer().getPluginManager().registerEvents(this.createInstance(CitizensNpcCommandListener.class), this);
        this.getServer().getPluginManager().registerEvents(this.createInstance(KowalMenuCloseListener.class), this);
    }
}
