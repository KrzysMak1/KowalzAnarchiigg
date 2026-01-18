package cc.dreamcode.kowal.libs.net.kyori.adventure.permission;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;

final class PermissionCheckers
{
    static final PermissionChecker NOT_SET;
    static final PermissionChecker FALSE;
    static final PermissionChecker TRUE;
    
    private PermissionCheckers() {
    }
    
    static {
        NOT_SET = new Always(TriState.NOT_SET);
        FALSE = new Always(TriState.FALSE);
        TRUE = new Always(TriState.TRUE);
    }
    
    private static final class Always implements PermissionChecker
    {
        private final TriState value;
        
        private Always(final TriState value) {
            this.value = value;
        }
        
        @NotNull
        @Override
        public TriState value(@NotNull final String permission) {
            return this.value;
        }
        
        @Override
        public String toString() {
            return PermissionChecker.class.getSimpleName() + ".always(" + (Object)this.value + ")";
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            final Always always = (Always)other;
            return this.value == always.value;
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
    }
}
