package cc.dreamcode.kowal.hook;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import cc.dreamcode.kowal.KowalPlugin;
import net.milkbowl.vault.economy.Economy;

public class VaultHookService
{
    private final Economy economy;
    
    public VaultHookService(final KowalPlugin kowalPlugin) {
        final ServicesManager servicesManager = kowalPlugin.getServer().getServicesManager();
        final RegisteredServiceProvider<Economy> rsp = (RegisteredServiceProvider<Economy>)servicesManager.getRegistration((Class)Economy.class);
        if (rsp == null) {
            throw new RuntimeException("Cannot hook into Economy from Vault.");
        }
        this.economy = (Economy)rsp.getProvider();
    }
    
    public double getMoney(@NonNull final OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            throw new NullPointerException("offlinePlayer is marked non-null but is null");
        }
        return this.economy.getBalance(offlinePlayer);
    }
    
    public boolean withdraw(@NonNull final OfflinePlayer offlinePlayer, final double amount) {
        if (offlinePlayer == null) {
            throw new NullPointerException("offlinePlayer is marked non-null but is null");
        }
        return this.economy.withdrawPlayer(offlinePlayer, amount).transactionSuccess();
    }
}
