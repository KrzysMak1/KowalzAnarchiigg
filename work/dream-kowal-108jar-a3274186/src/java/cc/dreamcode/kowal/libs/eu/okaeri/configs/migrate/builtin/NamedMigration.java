package cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.builtin;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.ConfigMigration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.builtin.special.SimpleMultiMigration;

public class NamedMigration extends SimpleMultiMigration
{
    private final String description;
    
    public NamedMigration(final String description, final ConfigMigration... migrations) {
        super(migrations);
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "NamedMigration(description=" + this.getDescription() + ")";
    }
    
    public String getDescription() {
        return this.description;
    }
}
