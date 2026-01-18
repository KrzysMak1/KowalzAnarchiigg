package cc.dreamcode.kowal;

import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.hook.PluginHook;
import cc.dreamcode.kowal.hook.VaultHook;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.Generated;
import cc.dreamcode.kowal.effect.EffectSerializer;
import cc.dreamcode.kowal.level.LevelSerializer;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.serializer.MenuBuilderSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure.serializer.BukkitNoticeSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.kowal.command.KowalCommand;
import cc.dreamcode.kowal.controller.EffectController;
import cc.dreamcode.kowal.controller.PlayerController;
import cc.dreamcode.kowal.controller.ArmorEquipController;
import cc.dreamcode.kowal.controller.DamageController;
import cc.dreamcode.kowal.tasks.ParticleTask;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.hook.PluginHookManager;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.kowal.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.kowal.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.kowal.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.kowal.command.result.BukkitNoticeResolver;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure.BukkitNoticeProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.BukkitMenuProvider;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.bukkit.BukkitTasker;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.VersionUtil;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.DreamBukkitPlatform;

public final class KowalPlugin extends DreamBukkitPlatform implements DreamBukkitConfig
{
    private static KowalPlugin instance;
    
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
        this.registerInjectable(BukkitNoticeProvider.create((Plugin)this));
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
        componentService.registerComponent(ParticleTask.class);
        componentService.registerComponent(DamageController.class);
        componentService.registerComponent(ArmorEquipController.class);
        componentService.registerComponent(PlayerController.class);
        componentService.registerComponent(EffectController.class);
        componentService.registerComponent(KowalCommand.class);
        this.getInject(PluginConfig.class).ifPresent(this::validateConfig);
    }
    
    public void disable() {
        this.getServer().getScheduler().cancelTasks(this);
        this.getInject(ParticleCache.class).ifPresent(ParticleCache::clear);
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
        if (pluginConfig.kowalItems == null || pluginConfig.kowalItems.isEmpty()) {
            this.getLogger().warning("Config: brak ustawien 'kowal-items-name'.");
        }
        if (pluginConfig.kowalLevels == null || pluginConfig.kowalLevels.isEmpty()) {
            this.getLogger().warning("Config: brak ustawien 'kowal-levels'.");
        }
        if (pluginConfig.effects == null || pluginConfig.effects.isEmpty()) {
            this.getLogger().warning("Config: brak ustawien 'effect-list'.");
        }
        if (pluginConfig.kowalLevels != null) {
            for (int level = 1; level <= 7; level++) {
                if (!pluginConfig.kowalLevels.containsKey(level)) {
                    this.getLogger().warning("Config: brak definicji poziomu kowala " + level + ".");
                }
            }
        }
    }
}
