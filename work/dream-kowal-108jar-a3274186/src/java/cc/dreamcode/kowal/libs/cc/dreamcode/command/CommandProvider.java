package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidInputHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidUsageHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidSenderHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidPermissionHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.result.ResultResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import java.util.List;
import lombok.NonNull;

public interface CommandProvider
{
    List<String> getSuggestion(@NonNull final DreamSender<?> sender, @NonNull final String input);
    
    List<String> getSuggestion(@NonNull final DreamSender<?> sender, @NonNull final CommandInput commandInput);
    
    CommandProviderImpl call(@NonNull final DreamSender<?> sender, @NonNull final String input);
    
    CommandProviderImpl call(@NonNull final DreamSender<?> sender, @NonNull final CommandInput commandInput);
    
    CommandProviderImpl register(@NonNull final CommandBase commandBase);
    
    CommandProviderImpl register(@NonNull final CommandContext commandContext, @NonNull final CommandBase commandBase);
    
    CommandProviderImpl register(@NonNull final CommandContext commandContext, @NonNull final CommandBase commandBase, @NonNull final Object instance);
    
    CommandProviderImpl unregister(@NonNull final CommandContext commandContext);
    
    CommandProviderImpl registerExtension(@NonNull final CommandExtension commandExtension);
    
    CommandProviderImpl registerTransformer(@NonNull final ObjectTransformer<?> objectTransformer);
    
    CommandProviderImpl registerTransformer(@NonNull final ArrayTransformer<?> arrayTransformer);
    
    CommandProviderImpl unregisterTransformer(@NonNull final Class<?> classTransformer, final boolean array);
    
    CommandProviderImpl registerBind(@NonNull final BindResolver<?> bindResolver);
    
    CommandProviderImpl unregisterBind(@NonNull final Class<?> bindClass);
    
    CommandProviderImpl registerResult(@NonNull final ResultResolver resultResolver);
    
    CommandProviderImpl unregisterResult(@NonNull final Class<?> resultClass);
    
    CommandProviderImpl registerSuggestion(@NonNull final String key, @NonNull final SuggestionSupplier suggestionSupplier);
    
    CommandProviderImpl unregisterSuggestion(@NonNull final String key);
    
    CommandProviderImpl registerSuggestionFilter(@NonNull final String key, @NonNull final SuggestionFilter suggestionFilter);
    
    CommandProviderImpl unregisterSuggestionFilter(@NonNull final String key);
    
    InvalidPermissionHandler getInvalidPermissionHandler();
    
    CommandProviderImpl setInvalidPermissionHandler(@NonNull final InvalidPermissionHandler invalidPermissionHandler);
    
    InvalidSenderHandler getInvalidSenderHandler();
    
    CommandProviderImpl setInvalidSenderHandler(@NonNull final InvalidSenderHandler invalidSenderHandler);
    
    InvalidUsageHandler getInvalidUsageHandler();
    
    CommandProviderImpl setInvalidUsageHandler(@NonNull final InvalidUsageHandler invalidUsageHandler);
    
    InvalidInputHandler getInvalidInputHandler();
    
    CommandProviderImpl setInvalidInputHandler(@NonNull final InvalidInputHandler invalidInputHandler);
    
    CommandRegistry getCommandRegistry();
    
    CommandProviderImpl setCommandRegistry(@NonNull final CommandRegistry commandRegistry);
    
    CommandProviderImpl registerAssignableClass(@NonNull final Class<?> from, @NonNull final Class<?> to);
    
    CommandProviderImpl unregisterAssignableClass(@NonNull final Class<?> from, @NonNull final Class<?> to);
    
    CommandScheduler getCommandScheduler();
    
    CommandProviderImpl setCommandScheduler(@NonNull final CommandScheduler commandScheduler);
}
