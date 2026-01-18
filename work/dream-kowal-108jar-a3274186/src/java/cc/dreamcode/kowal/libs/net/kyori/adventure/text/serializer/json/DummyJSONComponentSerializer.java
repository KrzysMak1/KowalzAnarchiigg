package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json;

import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

final class DummyJSONComponentSerializer implements JSONComponentSerializer
{
    static final JSONComponentSerializer INSTANCE;
    private static final String UNSUPPORTED_MESSAGE = "No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?";
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String input) {
        throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
    }
    
    @NotNull
    @Override
    public String serialize(@NotNull final Component component) {
        throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
    }
    
    static {
        INSTANCE = new DummyJSONComponentSerializer();
    }
    
    static final class BuilderImpl implements Builder
    {
        @NotNull
        @Override
        public Builder options(@NotNull final OptionState flags) {
            return this;
        }
        
        @NotNull
        @Override
        public Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor) {
            return this;
        }
        
        @Deprecated
        @NotNull
        @Override
        public Builder downsampleColors() {
            return this;
        }
        
        @NotNull
        @Override
        public Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer) {
            return this;
        }
        
        @Deprecated
        @NotNull
        @Override
        public Builder emitLegacyHoverEvent() {
            return this;
        }
        
        @NotNull
        @Override
        public JSONComponentSerializer build() {
            return DummyJSONComponentSerializer.INSTANCE;
        }
    }
}
