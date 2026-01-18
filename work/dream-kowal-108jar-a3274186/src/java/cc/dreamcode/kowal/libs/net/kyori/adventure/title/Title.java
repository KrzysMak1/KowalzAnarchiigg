package cc.dreamcode.kowal.libs.net.kyori.adventure.title;

import java.time.Duration;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Ticks;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

@ApiStatus.NonExtendable
public interface Title extends Examinable
{
    public static final Times DEFAULT_TIMES = Times.times(Ticks.duration(10L), Ticks.duration(70L), Ticks.duration(20L));

    @NotNull
    static Title title(@NotNull final Component title, @NotNull final Component subtitle) {
        return new TitleImpl(title, subtitle, Title.DEFAULT_TIMES);
    }

    @NotNull
    static Title title(@NotNull final Component title, @NotNull final Component subtitle, @Nullable final Times times) {
        return new TitleImpl(title, subtitle, times);
    }
    
    @NotNull
    Component title();
    
    @NotNull
    Component subtitle();
    
    @Nullable
    Times times();
    
     <T> T part(@NotNull final TitlePart<T> part);
    
    public interface Times extends Examinable
    {
        @NotNull
        static Times times(@NotNull final Duration fadeIn, @NotNull final Duration stay, @NotNull final Duration fadeOut) {
            return new TitleImpl.TimesImpl(fadeIn, stay, fadeOut);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
        @NotNull
        default Times of(@NotNull final Duration fadeIn, @NotNull final Duration stay, @NotNull final Duration fadeOut) {
            return Times.times(fadeIn, stay, fadeOut);
        }
        
        @NotNull
        Duration fadeIn();
        
        @NotNull
        Duration stay();
        
        @NotNull
        Duration fadeOut();
    }
}
