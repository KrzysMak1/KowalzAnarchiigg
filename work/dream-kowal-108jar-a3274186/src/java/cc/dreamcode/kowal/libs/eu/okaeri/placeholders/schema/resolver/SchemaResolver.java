package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.resolver;

import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;

public interface SchemaResolver
{
    boolean supports(final Class<?> type);
    
    String resolve(final Object object, final MessageField field);
    
    String resolve(final Object object);
}
