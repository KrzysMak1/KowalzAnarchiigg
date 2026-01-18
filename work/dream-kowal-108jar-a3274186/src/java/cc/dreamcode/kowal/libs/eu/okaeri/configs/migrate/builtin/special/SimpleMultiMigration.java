package cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.builtin.special;

import java.util.Arrays;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleMultiMigration implements ConfigMigration
{
    private final ConfigMigration[] migrations;
    private boolean requireAll;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        final long performed = Arrays.stream((Object[])this.migrations).filter(migration -> migration.migrate(config, view)).count();
        return this.requireAll ? (performed == this.migrations.length) : (performed > 0L);
    }
    
    @Override
    public String toString() {
        return "SimpleMultiMigration(migrations=" + Arrays.deepToString((Object[])this.migrations) + ", requireAll=" + this.requireAll + ")";
    }
    
    public SimpleMultiMigration(final ConfigMigration[] migrations, final boolean requireAll) {
        this.requireAll = false;
        this.migrations = migrations;
        this.requireAll = requireAll;
    }
    
    public SimpleMultiMigration(final ConfigMigration[] migrations) {
        this.requireAll = false;
        this.migrations = migrations;
    }
}
