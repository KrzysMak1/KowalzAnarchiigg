package cc.dreamcode.kowal.libs.net.kyori.adventure.text.format;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Index;

public enum TextDecoration implements StyleBuilderApplicable, TextFormat
{
    OBFUSCATED("obfuscated"), 
    BOLD("bold"), 
    STRIKETHROUGH("strikethrough"), 
    UNDERLINED("underlined"), 
    ITALIC("italic");
    
    public static final Index<String, TextDecoration> NAMES;
    private final String name;
    
    private TextDecoration(final String name) {
        this.name = name;
    }
    
    @Deprecated
    @NotNull
    public final TextDecorationAndState as(final boolean state) {
        return this.withState(state);
    }
    
    @Deprecated
    @NotNull
    public final TextDecorationAndState as(@NotNull final State state) {
        return this.withState(state);
    }
    
    @NotNull
    public final TextDecorationAndState withState(final boolean state) {
        return new TextDecorationAndStateImpl(this, State.byBoolean(state));
    }
    
    @NotNull
    public final TextDecorationAndState withState(@NotNull final State state) {
        return new TextDecorationAndStateImpl(this, state);
    }
    
    @NotNull
    public final TextDecorationAndState withState(@NotNull final TriState state) {
        return new TextDecorationAndStateImpl(this, State.byTriState(state));
    }
    
    public void styleApply(final Style.Builder style) {
        style.decorate(this);
    }
    
    @NotNull
    public String toString() {
        return this.name;
    }
    
    static {
        NAMES = Index.create(TextDecoration.class, (java.util.function.Function<? super TextDecoration, ? extends String>)(constant -> constant.name));
    }
    
    public enum State
    {
        NOT_SET("not_set"), 
        FALSE("false"), 
        TRUE("true");
        
        private final String name;
        
        private State(final String name) {
            this.name = name;
        }
        
        public String toString() {
            return this.name;
        }
        
        @NotNull
        public static State byBoolean(final boolean flag) {
            return flag ? State.TRUE : State.FALSE;
        }
        
        @NotNull
        public static State byBoolean(@Nullable final Boolean flag) {
            return (flag == null) ? State.NOT_SET : byBoolean((boolean)flag);
        }
        
        @NotNull
        public static State byTriState(@NotNull final TriState flag) {
            Objects.requireNonNull((Object)flag);
            switch (flag) {
                case TRUE: {
                    return State.TRUE;
                }
                case FALSE: {
                    return State.FALSE;
                }
                case NOT_SET: {
                    return State.NOT_SET;
                }
                default: {
                    throw new IllegalArgumentException("Unable to turn TriState: " + (Object)flag + " into a TextDecoration.State");
                }
            }
        }
    }
}
