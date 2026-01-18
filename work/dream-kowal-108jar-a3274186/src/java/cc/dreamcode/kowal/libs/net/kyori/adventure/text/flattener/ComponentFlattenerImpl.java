package cc.dreamcode.kowal.libs.net.kyori.adventure.text.flattener;

import java.util.function.Consumer;
import java.util.function.BiConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.SelectorComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.KeybindComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ScoreComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslatableComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.InheritanceAwareMap;

final class ComponentFlattenerImpl implements ComponentFlattener
{
    static final ComponentFlattener BASIC;
    static final ComponentFlattener TEXT_ONLY;
    private static final int MAX_DEPTH = 512;
    private final InheritanceAwareMap<Component, Handler> flatteners;
    private final Function<Component, String> unknownHandler;
    
    ComponentFlattenerImpl(final InheritanceAwareMap<Component, Handler> flatteners, @Nullable final Function<Component, String> unknownHandler) {
        this.flatteners = flatteners;
        this.unknownHandler = unknownHandler;
    }
    
    @Override
    public void flatten(@NotNull final Component input, @NotNull final FlattenerListener listener) {
        this.flatten0(input, listener, 0);
    }
    
    private void flatten0(@NotNull final Component input, @NotNull final FlattenerListener listener, final int depth) {
        Objects.requireNonNull((Object)input, "input");
        Objects.requireNonNull((Object)listener, "listener");
        if (input == Component.empty()) {
            return;
        }
        if (depth > 512) {
            throw new IllegalStateException("Exceeded maximum depth of 512 while attempting to flatten components!");
        }
        final Handler flattener = this.flattener(input);
        final Style inputStyle = input.style();
        listener.pushStyle(inputStyle);
        try {
            if (flattener != null) {
                flattener.handle(this, input, listener, depth + 1);
            }
            if (!input.children().isEmpty() && listener.shouldContinue()) {
                for (final Component child : input.children()) {
                    this.flatten0(child, listener, depth + 1);
                }
            }
        }
        finally {
            listener.popStyle(inputStyle);
        }
    }
    
    @Nullable
    private <T extends Component> Handler flattener(final T test) {
        final Handler flattener = this.flatteners.get(test.getClass());
        if (flattener == null && this.unknownHandler != null) {
            return (self, component, listener, depth) -> listener.component((String)this.unknownHandler.apply((Object)component));
        }
        return flattener;
    }
    
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this.flatteners, this.unknownHandler);
    }
    
    static {
        BASIC = new BuilderImpl().mapper(KeybindComponent.class, (java.util.function.Function<KeybindComponent, String>)(component -> component.keybind())).mapper(ScoreComponent.class, (java.util.function.Function<ScoreComponent, String>)(component -> {
            final String value = component.value();
            return (value != null) ? value : "";
        })).mapper(SelectorComponent.class, (java.util.function.Function<SelectorComponent, String>)SelectorComponent::pattern).mapper(TextComponent.class, (java.util.function.Function<TextComponent, String>)TextComponent::content).mapper(TranslatableComponent.class, (java.util.function.Function<TranslatableComponent, String>)(component -> {
            final String fallback = component.fallback();
            return (fallback != null) ? fallback : component.key();
        })).build();
        TEXT_ONLY = new BuilderImpl().mapper(TextComponent.class, (java.util.function.Function<TextComponent, String>)TextComponent::content).build();
    }
    
    static final class BuilderImpl implements Builder
    {
        private final InheritanceAwareMap.Builder<Component, Handler> flatteners;
        @Nullable
        private Function<Component, String> unknownHandler;
        
        BuilderImpl() {
            this.flatteners = InheritanceAwareMap.builder().strict(true);
        }
        
        BuilderImpl(final InheritanceAwareMap<Component, Handler> flatteners, @Nullable final Function<Component, String> unknownHandler) {
            this.flatteners = InheritanceAwareMap.builder((InheritanceAwareMap<? extends Component, ? extends Handler>)flatteners).strict(true);
            this.unknownHandler = unknownHandler;
        }
        
        @NotNull
        @Override
        public ComponentFlattener build() {
            return new ComponentFlattenerImpl(this.flatteners.build(), this.unknownHandler);
        }
        
        @Override
        public <T extends Component> Builder mapper(@NotNull final Class<T> type, @NotNull final Function<T, String> converter) {
            this.flatteners.put(type, (self, component, listener, depth) -> listener.component((String)converter.apply((Object)component)));
            return this;
        }
        
        @Override
        public <T extends Component> Builder complexMapper(@NotNull final Class<T> type, @NotNull final BiConsumer<T, Consumer<Component>> converter) {
            this.flatteners.put(type, (self, component, listener, depth) -> converter.accept((Object)component, (Object)(c -> self.flatten0(c, listener, depth))));
            return this;
        }
        
        @Override
        public Builder unknownMapper(@Nullable final Function<Component, String> converter) {
            this.unknownHandler = converter;
            return this;
        }
    }
    
    @FunctionalInterface
    interface Handler
    {
        void handle(final ComponentFlattenerImpl self, final Component input, final FlattenerListener listener, final int depth);
    }
}
