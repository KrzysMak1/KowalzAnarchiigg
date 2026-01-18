package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor.ConfigSectionWalker;
import java.util.Optional;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor.ConfigLineInfo;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor.format.YamlSectionWalker;
import java.util.function.Predicate;
import java.io.OutputStream;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor.ConfigPostprocessor;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import java.io.InputStream;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.FieldDeclaration;
import java.util.Map;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.OkaeriException;
import org.bukkit.configuration.MemorySection;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;

public class YamlBukkitConfigurer extends Configurer
{
    private YamlConfiguration config;
    private String commentPrefix;
    
    public YamlBukkitConfigurer(@NonNull final YamlConfiguration config) {
        this.commentPrefix = "# ";
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        this.config = config;
    }
    
    public YamlBukkitConfigurer() {
        this(new YamlConfiguration());
        this.config.options().pathSeparator('\u001d');
    }
    
    @Override
    public List<String> getExtensions() {
        return (List<String>)Arrays.asList((Object[])new String[] { "yml", "yaml" });
    }
    
    @Override
    public Object simplify(final Object value, final GenericsDeclaration genericType, @NonNull final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        if (value instanceof MemorySection) {
            return ((MemorySection)value).getValues(false);
        }
        return super.simplify(value, genericType, serdesContext, conservative);
    }
    
    @Override
    public <T> T resolveType(final Object object, final GenericsDeclaration genericSource, @NonNull final Class<T> targetClazz, final GenericsDeclaration genericTarget, @NonNull final SerdesContext serdesContext) {
        if (targetClazz == null) {
            throw new NullPointerException("targetClazz is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        if (object instanceof MemorySection) {
            final Map<String, Object> values = (Map<String, Object>)((MemorySection)object).getValues(false);
            return super.resolveType(values, GenericsDeclaration.of(values), targetClazz, genericTarget, serdesContext);
        }
        return super.resolveType(object, genericSource, targetClazz, genericTarget, serdesContext);
    }
    
    @Override
    public void setValue(@NonNull final String key, final Object value, final GenericsDeclaration type, final FieldDeclaration field) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        final Object simplified = this.simplify(value, type, SerdesContext.of(this, field), true);
        this.config.set(key, simplified);
    }
    
    @Override
    public void setValueUnsafe(@NonNull final String key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.config.set(key, value);
    }
    
    @Override
    public Object getValue(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.config.get(key);
    }
    
    @Override
    public Object remove(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (!this.keyExists(key)) {
            return null;
        }
        final Object old = this.config.get(key);
        this.config.set(key, (Object)null);
        return old;
    }
    
    @Override
    public boolean keyExists(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.config.getKeys(false).contains((Object)key);
    }
    
    @Override
    public List<String> getAllKeys() {
        return (List<String>)Collections.unmodifiableList((List)new ArrayList((Collection)this.config.getKeys(false)));
    }
    
    @Override
    public void load(@NonNull final InputStream inputStream, @NonNull final ConfigDeclaration declaration) throws Exception {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is marked non-null but is null");
        }
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
        this.config.loadFromString(ConfigPostprocessor.of(inputStream).getContext());
    }
    
    @Override
    public void write(@NonNull final OutputStream outputStream, @NonNull final ConfigDeclaration declaration) throws Exception {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is marked non-null but is null");
        }
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
        final String contents = this.config.saveToString();
        ConfigPostprocessor.of(contents).removeLines(line -> line.startsWith(this.commentPrefix.trim())).removeLinesUntil((Predicate<String>)(line -> line.chars().anyMatch(x -> !Character.isWhitespace(x)))).updateLinesKeys(new YamlSectionWalker() {
            @Override
            public String update(final String line, final ConfigLineInfo lineInfo, final List<ConfigLineInfo> path) {
                ConfigDeclaration currentDeclaration = declaration;
                for (int i = 0; i < path.size() - 1; ++i) {
                    final ConfigLineInfo pathElement = (ConfigLineInfo)path.get(i);
                    final Optional<FieldDeclaration> field = currentDeclaration.getField(pathElement.getName());
                    if (!field.isPresent()) {
                        return line;
                    }
                    final GenericsDeclaration fieldType = ((FieldDeclaration)field.get()).getType();
                    if (!fieldType.isConfig()) {
                        return line;
                    }
                    currentDeclaration = ConfigDeclaration.of(fieldType.getType());
                }
                final Optional<FieldDeclaration> lineDeclaration = currentDeclaration.getField(lineInfo.getName());
                if (!lineDeclaration.isPresent()) {
                    return line;
                }
                final String[] fieldComment = ((FieldDeclaration)lineDeclaration.get()).getComment();
                if (fieldComment == null) {
                    return line;
                }
                final String comment = ConfigPostprocessor.createComment(YamlBukkitConfigurer.this.commentPrefix, fieldComment);
                return ConfigPostprocessor.addIndent(comment, lineInfo.getIndent()) + line;
            }
        }).prependContextComment(this.commentPrefix, declaration.getHeader()).write(outputStream);
    }
    
    public YamlBukkitConfigurer setCommentPrefix(final String commentPrefix) {
        this.commentPrefix = commentPrefix;
        return this;
    }
}
