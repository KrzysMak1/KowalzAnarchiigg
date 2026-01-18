package cc.dreamcode.kowal.hook;

import lombok.Generated;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import org.bukkit.OfflinePlayer;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.hook.annotation.Hook;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.hook.PluginHook;

@Hook(name = "vault")
public class VaultHook implements PluginHook
{
    private final KowalPlugin kowalPlugin;
    private VaultHookService vaultHookService;
    
    @Override
    public void onInit() {
        this.vaultHookService = new VaultHookService(this.kowalPlugin);
    }
    
    public Optional<Double> getMoney(@NonNull final Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (this.vaultHookService == null) {
            return (Optional<Double>)Optional.empty();
        }
        return (Optional<Double>)Optional.of((Object)this.vaultHookService.getMoney((OfflinePlayer)player));
    }
    
    public boolean withdraw(@NonNull final Player player, final double money) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        return this.vaultHookService != null && this.vaultHookService.withdraw((OfflinePlayer)player, money);
    }
    
    @Inject
    @Generated
    public VaultHook(final KowalPlugin kowalPlugin) {
        this.kowalPlugin = kowalPlugin;
    }
}
