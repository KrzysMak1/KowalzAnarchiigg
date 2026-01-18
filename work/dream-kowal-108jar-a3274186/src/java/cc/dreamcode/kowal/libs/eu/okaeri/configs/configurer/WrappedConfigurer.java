package cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import java.util.Map;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.OkaeriException;
import java.util.Collection;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;

public class WrappedConfigurer extends Configurer
{
    private final Configurer wrapped;
    
    public WrappedConfigurer(final Configurer wrapped) {
        this.wrapped = wrapped;
    }
    
    public Configurer getWrapped() {
        return this.wrapped;
    }
    
    @Override
    public void register(final OkaeriSerdesPack pack) {
        this.getWrapped().register(pack);
    }
    
    @Override
    public List<String> getExtensions() {
        return this.getWrapped().getExtensions();
    }
    
    @Override
    public void setValue(final String key, final Object value, final GenericsDeclaration genericType, final FieldDeclaration field) {
        this.getWrapped().setValue(key, value, genericType, field);
    }
    
    @Override
    public void setValueUnsafe(final String key, final Object value) {
        this.getWrapped().setValueUnsafe(key, value);
    }
    
    @Override
    public Object getValue(final String key) {
        return this.getWrapped().getValue(key);
    }
    
    @Override
    public Object getValueUnsafe(final String key) {
        return this.getWrapped().getValueUnsafe(key);
    }
    
    @Override
    public Object remove(final String key) {
        return this.getWrapped().remove(key);
    }
    
    @Override
    public boolean isToStringObject(final Object object, final GenericsDeclaration genericType, final SerdesContext serdesContext) {
        return this.getWrapped().isToStringObject(object, genericType, serdesContext);
    }
    
    @Deprecated
    @Override
    public boolean isToStringObject(final Object object, final GenericsDeclaration genericType) {
        return this.getWrapped().isToStringObject(object, genericType);
    }
    
    @Override
    public Object simplifyCollection(final Collection<?> value, final GenericsDeclaration genericType, final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        return this.getWrapped().simplifyCollection(value, genericType, serdesContext, conservative);
    }
    
    @Override
    public Object simplifyMap(final Map<Object, Object> value, final GenericsDeclaration genericType, final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        return this.getWrapped().simplifyMap(value, genericType, serdesContext, conservative);
    }
    
    @Override
    public Object simplify(final Object value, final GenericsDeclaration genericType, final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        return this.getWrapped().simplify(value, genericType, serdesContext, conservative);
    }
    
    @Override
    public <T> T getValue(final String key, final Class<T> clazz, final GenericsDeclaration genericType, final SerdesContext serdesContext) {
        return this.getWrapped().getValue(key, clazz, genericType, serdesContext);
    }
    
    @Override
    public <T> T resolveType(final Object object, final GenericsDeclaration genericSource, final Class<T> targetClazz, final GenericsDeclaration genericTarget, final SerdesContext serdesContext) throws OkaeriException {
        return this.getWrapped().resolveType(object, genericSource, targetClazz, genericTarget, serdesContext);
    }
    
    @Override
    public Class<?> resolveTargetBaseType(final SerdesContext serdesContext, final GenericsDeclaration target, final GenericsDeclaration source) {
        return this.getWrapped().resolveTargetBaseType(serdesContext, target, source);
    }
    
    @Override
    public Object createInstance(final Class<?> clazz) throws OkaeriException {
        return this.getWrapped().createInstance(clazz);
    }
    
    @Override
    public boolean keyExists(final String key) {
        return this.getWrapped().keyExists(key);
    }
    
    @Override
    public boolean isValid(final FieldDeclaration declaration, final Object value) {
        return this.getWrapped().isValid(declaration, value);
    }
    
    @Override
    public List<String> getAllKeys() {
        return this.getWrapped().getAllKeys();
    }
    
    @Override
    public Set<String> sort(final ConfigDeclaration declaration) {
        return this.getWrapped().sort(declaration);
    }
    
    @Override
    public void write(final OutputStream outputStream, final ConfigDeclaration declaration) throws Exception {
        this.getWrapped().write(outputStream, declaration);
    }
    
    @Override
    public void load(final InputStream inputStream, final ConfigDeclaration declaration) throws Exception {
        this.getWrapped().load(inputStream, declaration);
    }
    
    @Override
    public OkaeriConfig getParent() {
        return this.getWrapped().getParent();
    }
    
    @Override
    public void setParent(final OkaeriConfig parent) {
        this.getWrapped().setParent(parent);
    }
    
    @Override
    public void setRegistry(final SerdesRegistry registry) {
        this.getWrapped().setRegistry(registry);
    }
    
    @Override
    public SerdesRegistry getRegistry() {
        return this.getWrapped().getRegistry();
    }
}
