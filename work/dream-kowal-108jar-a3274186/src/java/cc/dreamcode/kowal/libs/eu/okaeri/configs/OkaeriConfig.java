package cc.dreamcode.kowal.libs.eu.okaeri.configs;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.builtin.NamedMigration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Variable;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.ConfigMigration;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.ValidationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.OkaeriException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.io.File;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.InitializationException;
import java.util.stream.Stream;
import java.util.Objects;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;
import java.nio.file.Path;
import java.util.logging.Logger;

public abstract class OkaeriConfig
{
    private Logger logger;
    private Path bindFile;
    private Configurer configurer;
    private ConfigDeclaration declaration;
    private boolean removeOrphans;
    
    public OkaeriConfig() {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
        this.removeOrphans = false;
        this.updateDeclaration();
    }
    
    public void setConfigurer(@NonNull final Configurer configurer) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        (this.configurer = configurer).setParent(this);
    }
    
    public OkaeriConfig withConfigurer(@NonNull final Configurer configurer) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        if (this.getConfigurer() != null) {
            configurer.setRegistry(this.getConfigurer().getRegistry());
        }
        this.setConfigurer(configurer);
        return this;
    }
    
    public OkaeriConfig withConfigurer(@NonNull final Configurer configurer, @NonNull final OkaeriSerdesPack... serdesPack) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        if (serdesPack == null) {
            throw new NullPointerException("serdesPack is marked non-null but is null");
        }
        if (this.getConfigurer() != null) {
            configurer.setRegistry(this.getConfigurer().getRegistry());
        }
        this.setConfigurer(configurer);
        final Stream<OkaeriSerdesPack> stream = Arrays.stream(serdesPack);
        final Configurer configurer2 = this.getConfigurer();
        Objects.requireNonNull(configurer2);
        stream.forEach(configurer2::register);
        return this;
    }
    
    public OkaeriConfig withSerdesPack(@NonNull final OkaeriSerdesPack serdesPack) {
        if (serdesPack == null) {
            throw new NullPointerException("serdesPack is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        this.getConfigurer().register(serdesPack);
        return this;
    }
    
    public OkaeriConfig withBindFile(@NonNull final File bindFile) {
        if (bindFile == null) {
            throw new NullPointerException("bindFile is marked non-null but is null");
        }
        this.setBindFile(bindFile.toPath());
        return this;
    }
    
    public OkaeriConfig withBindFile(@NonNull final Path path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.setBindFile(path);
        return this;
    }
    
    public OkaeriConfig withBindFile(@NonNull final String pathname) {
        if (pathname == null) {
            throw new NullPointerException("pathname is marked non-null but is null");
        }
        this.setBindFile(Paths.get(pathname, new String[0]));
        return this;
    }
    
    public OkaeriConfig withLogger(@NonNull final Logger logger) {
        if (logger == null) {
            throw new NullPointerException("logger is marked non-null but is null");
        }
        this.setLogger(logger);
        return this;
    }
    
    public OkaeriConfig withRemoveOrphans(final boolean removeOrphans) {
        this.setRemoveOrphans(removeOrphans);
        return this;
    }
    
    public OkaeriConfig saveDefaults() throws OkaeriException {
        if (this.getBindFile() == null) {
            throw new InitializationException("bindFile cannot be null");
        }
        if (Files.exists(this.getBindFile(), new LinkOption[0])) {
            return this;
        }
        return this.save();
    }
    
    public void set(@NonNull final String key, Object value) throws OkaeriException {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        final FieldDeclaration field = this.getDeclaration().getField(key).orElse(null);
        if (field != null) {
            value = this.getConfigurer().resolveType(value, GenericsDeclaration.of(value), field.getType().getType(), field.getType(), SerdesContext.of(this.configurer, field));
            field.updateValue(value);
        }
        final GenericsDeclaration fieldGenerics = (field == null) ? null : field.getType();
        this.getConfigurer().setValue(key, value, fieldGenerics, field);
    }
    
    public Object get(@NonNull final String key) throws OkaeriException {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        final FieldDeclaration field = this.getDeclaration().getField(key).orElse(null);
        if (field != null) {
            return field.getValue();
        }
        return this.getConfigurer().getValue(key);
    }
    
    public <T> T get(@NonNull final String key, @NonNull final Class<T> clazz) throws OkaeriException {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        return this.get(key, GenericsDeclaration.of(clazz));
    }
    
    public <T> T get(@NonNull final String key, @NonNull final GenericsDeclaration generics) throws OkaeriException {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        final FieldDeclaration field = this.getDeclaration().getField(key).orElse(null);
        if (field != null) {
            return (T)this.getConfigurer().resolveType(field.getValue(), field.getType(), generics.getType(), generics, SerdesContext.of(this.configurer, field));
        }
        return (T)this.getConfigurer().getValue(key, generics.getType(), null, SerdesContext.of(this.configurer));
    }
    
    public OkaeriConfig save() throws OkaeriException {
        return this.save(this.getBindFile());
    }
    
    public OkaeriConfig save(@NonNull final File file) throws OkaeriException {
        if (file == null) {
            throw new NullPointerException("file is marked non-null but is null");
        }
        try {
            final File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            return this.save((OutputStream)new PrintStream((OutputStream)new FileOutputStream(file, false), true, StandardCharsets.UTF_8.name()));
        }
        catch (final FileNotFoundException | UnsupportedEncodingException exception) {
            throw new OkaeriException("failed #save using file " + (Object)file, (Throwable)exception);
        }
    }
    
    public OkaeriConfig save(@NonNull final Path path) throws OkaeriException {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.save(path.toFile());
    }
    
    public String saveToString() throws OkaeriException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.save((OutputStream)outputStream);
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
    
    public OkaeriConfig save(@NonNull final OutputStream outputStream) throws OkaeriException {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        for (final FieldDeclaration field : this.getDeclaration().getFields()) {
            if (!this.getConfigurer().isValid(field, field.getValue())) {
                throw new ValidationException((Object)this.getConfigurer().getClass() + " marked " + field.getName() + " as invalid without throwing an exception");
            }
            try {
                this.getConfigurer().setValue(field.getName(), field.getValue(), field.getType(), field);
            }
            catch (final Exception exception) {
                throw new OkaeriException("failed to #setValue for " + field.getName(), (Throwable)exception);
            }
        }
        try {
            final Set<String> orphans = this.getConfigurer().sort(this.getDeclaration());
            if (!orphans.isEmpty() && this.isRemoveOrphans()) {
                this.logger.warning("Removed orphaned (undeclared) keys: " + (Object)orphans);
                orphans.forEach(orphan -> this.getConfigurer().remove(orphan));
            }
            this.getConfigurer().write(outputStream, this.getDeclaration());
        }
        catch (final Exception exception2) {
            throw new OkaeriException("failed #write", (Throwable)exception2);
        }
        return this;
    }
    
    public Map<String, Object> asMap(@NonNull final Configurer configurer, final boolean conservative) throws OkaeriException {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        final Map<String, Object> map = (Map<String, Object>)new LinkedHashMap();
        for (final FieldDeclaration field : this.getDeclaration().getFields()) {
            final Object simplified = configurer.simplify(field.getValue(), field.getType(), SerdesContext.of(configurer, field), conservative);
            map.put((Object)field.getName(), simplified);
        }
        if (this.getConfigurer() == null) {
            return map;
        }
        for (final String keyName : this.getConfigurer().getAllKeys()) {
            if (map.containsKey((Object)keyName)) {
                continue;
            }
            final Object value = this.getConfigurer().getValue(keyName);
            final Object simplified2 = configurer.simplify(value, GenericsDeclaration.of(value), SerdesContext.of(configurer, null), conservative);
            map.put((Object)keyName, simplified2);
        }
        return map;
    }
    
    public OkaeriConfig load(final boolean update) throws OkaeriException {
        this.load();
        if (update) {
            this.save();
        }
        return this;
    }
    
    public OkaeriConfig load(@NonNull final String data) throws OkaeriException {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        return this.load((InputStream)inputStream);
    }
    
    public OkaeriConfig load(@NonNull final InputStream inputStream) throws OkaeriException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        try {
            this.getConfigurer().load(inputStream, this.getDeclaration());
        }
        catch (final Exception exception) {
            throw new OkaeriException("failed #load", (Throwable)exception);
        }
        return this.update();
    }
    
    public OkaeriConfig load() throws OkaeriException {
        return this.load(this.getBindFile());
    }
    
    public OkaeriConfig load(@NonNull final File file) throws OkaeriException {
        if (file == null) {
            throw new NullPointerException("file is marked non-null but is null");
        }
        try {
            return this.load((InputStream)new FileInputStream(file));
        }
        catch (final FileNotFoundException exception) {
            throw new OkaeriException("failed #load using file " + (Object)file, (Throwable)exception);
        }
    }
    
    public OkaeriConfig load(@NonNull final Path path) throws OkaeriException {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.load(path.toFile());
    }
    
    public OkaeriConfig load(@NonNull final Map<String, Object> map) throws OkaeriException {
        if (map == null) {
            throw new NullPointerException("map is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        map.forEach(this::set);
        return this;
    }
    
    public OkaeriConfig load(@NonNull final OkaeriConfig otherConfig) throws OkaeriException {
        if (otherConfig == null) {
            throw new NullPointerException("otherConfig is marked non-null but is null");
        }
        if (this.getConfigurer() == null) {
            throw new InitializationException("configurer cannot be null");
        }
        return this.load(otherConfig.asMap(this.getConfigurer(), true));
    }
    
    public OkaeriConfig migrate(@NonNull final ConfigMigration... migrations) throws OkaeriException {
        if (migrations == null) {
            throw new NullPointerException("migrations is marked non-null but is null");
        }
        return this.migrate((Consumer<Long>)(performed -> {
            try {
                this.load(this.saveToString());
            }
            catch (final OkaeriException exception) {
                throw new OkaeriException("failed #migrate due to load error after migrations (not saving)", (Throwable)exception);
            }
            this.save();
        }), migrations);
    }
    
    public OkaeriConfig migrate(@NonNull final Consumer<Long> callback, @NonNull final ConfigMigration... migrations) throws OkaeriException {
        if (callback == null) {
            throw new NullPointerException("callback is marked non-null but is null");
        }
        if (migrations == null) {
            throw new NullPointerException("migrations is marked non-null but is null");
        }
        final RawConfigView view = new RawConfigView(this);
        final long performed = Arrays.stream((Object[])migrations).filter(migration -> {
            try {
                return migration.migrate(this, view);
            }
            catch (final Exception exception) {
                throw new OkaeriException("migrate failure in " + migration.getClass().getName(), (Throwable)exception);
            }
        }).peek(migration -> {
            if (migration instanceof NamedMigration) {
                final String name = migration.getClass().getSimpleName();
                final String description = ((NamedMigration)migration).getDescription();
                this.logger.info(name + ": " + description);
            }
        }).count();
        if (performed > 0L) {
            callback.accept((Object)performed);
        }
        return this;
    }
    
    public OkaeriConfig update() throws OkaeriException {
        if (this.getDeclaration() == null) {
            throw new InitializationException("declaration cannot be null: config not initialized");
        }
        for (final FieldDeclaration field : this.getDeclaration().getFields()) {
            final String fieldName = field.getName();
            final GenericsDeclaration genericType = field.getType();
            final Class<?> type = field.getType().getType();
            final Variable variable = field.getVariable();
            boolean updateValue = true;
            if (variable != null) {
                final String rawProperty = System.getProperty(variable.value());
                final String property = (rawProperty == null) ? System.getenv(variable.value()) : rawProperty;
                if (property != null) {
                    Object value;
                    try {
                        value = this.getConfigurer().resolveType(property, GenericsDeclaration.of(property), genericType.getType(), genericType, SerdesContext.of(this.configurer, field));
                    }
                    catch (final Exception exception) {
                        throw new OkaeriException("failed to #resolveType for @Variable { " + variable.value() + " }", (Throwable)exception);
                    }
                    if (!this.getConfigurer().isValid(field, value)) {
                        throw new ValidationException((Object)this.getConfigurer().getClass() + " marked " + field.getName() + " as invalid without throwing an exception");
                    }
                    field.updateValue(value);
                    field.setVariableHide(true);
                    updateValue = false;
                }
            }
            if (!this.getConfigurer().keyExists(fieldName)) {
                continue;
            }
            Object value2;
            try {
                value2 = this.getConfigurer().getValue(fieldName, type, genericType, SerdesContext.of(this.configurer, field));
            }
            catch (final Exception exception2) {
                throw new OkaeriException("failed to #getValue for " + fieldName, (Throwable)exception2);
            }
            if (updateValue) {
                if (!this.getConfigurer().isValid(field, value2)) {
                    throw new ValidationException((Object)this.getConfigurer().getClass() + " marked " + field.getName() + " as invalid without throwing an exception");
                }
                field.updateValue(value2);
            }
            field.setStartingValue(value2);
        }
        return this;
    }
    
    public OkaeriConfig updateDeclaration() {
        this.setDeclaration(ConfigDeclaration.of(this));
        return this;
    }
    
    public void setLogger(final Logger logger) {
        this.logger = logger;
    }
    
    public Path getBindFile() {
        return this.bindFile;
    }
    
    protected void setBindFile(final Path bindFile) {
        this.bindFile = bindFile;
    }
    
    public Configurer getConfigurer() {
        return this.configurer;
    }
    
    public ConfigDeclaration getDeclaration() {
        return this.declaration;
    }
    
    protected void setDeclaration(final ConfigDeclaration declaration) {
        this.declaration = declaration;
    }
    
    public boolean isRemoveOrphans() {
        return this.removeOrphans;
    }
    
    protected void setRemoveOrphans(final boolean removeOrphans) {
        this.removeOrphans = removeOrphans;
    }
}
