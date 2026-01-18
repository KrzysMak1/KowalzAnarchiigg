package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;

final class MiniMessageSerializer
{
    private MiniMessageSerializer() {
    }
    
    @NotNull
    static String serialize(@NotNull final Component component, @NotNull final SerializableResolver resolver, final boolean strict) {
        final StringBuilder sb = new StringBuilder();
        final Collector emitter = new Collector(resolver, strict, sb);
        emitter.mark();
        visit(component, emitter, resolver, true);
        if (strict) {
            emitter.popAll();
        }
        else {
            emitter.completeTag();
        }
        return sb.toString();
    }
    
    private static void visit(@NotNull final Component component, final Collector emitter, final SerializableResolver resolver, final boolean lastChild) {
        resolver.handle(component, emitter);
        emitter.flushClaims(component);
        final Iterator<Component> it = (Iterator<Component>)component.children().iterator();
        while (it.hasNext()) {
            emitter.mark();
            visit((Component)it.next(), emitter, resolver, lastChild && !it.hasNext());
        }
        if (!lastChild) {
            emitter.popToMark();
        }
    }
    
    static final class Collector implements TokenEmitter, ClaimConsumer
    {
        private static final String MARK = "__<'\"\\MARK__";
        private static final char[] TEXT_ESCAPES;
        private static final char[] TAG_TOKENS;
        private static final char[] SINGLE_QUOTED_ESCAPES;
        private static final char[] DOUBLE_QUOTED_ESCAPES;
        private final SerializableResolver resolver;
        private final boolean strict;
        private final StringBuilder consumer;
        private String[] activeTags;
        private int tagLevel;
        private TagState tagState;
        @Nullable
        Emitable componentClaim;
        final Set<String> claimedStyleElements;
        
        Collector(final SerializableResolver resolver, final boolean strict, final StringBuilder consumer) {
            this.activeTags = new String[4];
            this.tagLevel = 0;
            this.tagState = TagState.TEXT;
            this.claimedStyleElements = (Set<String>)new HashSet();
            this.resolver = resolver;
            this.strict = strict;
            this.consumer = consumer;
        }
        
        private void pushActiveTag(final String tag) {
            if (this.tagLevel >= this.activeTags.length) {
                this.activeTags = (String[])Arrays.copyOf((Object[])this.activeTags, this.activeTags.length * 2);
            }
            this.activeTags[this.tagLevel++] = tag;
        }
        
        private String popTag(final boolean allowMarks) {
            if (this.tagLevel-- <= 0) {
                throw new IllegalStateException("Unbalanced tags, tried to pop below depth");
            }
            final String tag = this.activeTags[this.tagLevel];
            if (!allowMarks && tag == "__<'\"\\MARK__") {
                throw new IllegalStateException("Tried to pop past mark, tag stack: " + Arrays.toString((Object[])this.activeTags) + " @ " + this.tagLevel);
            }
            return tag;
        }
        
        void mark() {
            this.pushActiveTag("__<'\"\\MARK__");
        }
        
        void popToMark() {
            if (this.tagLevel == 0) {
                return;
            }
            String tag;
            while ((tag = this.popTag(true)) != "__<'\"\\MARK__") {
                this.emitClose(tag);
            }
        }
        
        void popAll() {
            while (this.tagLevel > 0) {
                final String[] activeTags = this.activeTags;
                final int tagLevel = this.tagLevel - 1;
                this.tagLevel = tagLevel;
                final String tag = activeTags[tagLevel];
                if (tag != "__<'\"\\MARK__") {
                    this.emitClose(tag);
                }
            }
        }
        
        void completeTag() {
            if (this.tagState.isTag) {
                this.consumer.append('>');
                this.tagState = TagState.TEXT;
            }
        }
        
        @NotNull
        @Override
        public Collector tag(@NotNull final String token) {
            this.completeTag();
            this.consumer.append('<');
            this.escapeTagContent(token, QuotingOverride.UNQUOTED);
            this.tagState = TagState.MID;
            this.pushActiveTag(token);
            return this;
        }
        
        @NotNull
        @Override
        public TokenEmitter selfClosingTag(@NotNull final String token) {
            this.completeTag();
            this.consumer.append('<');
            this.escapeTagContent(token, QuotingOverride.UNQUOTED);
            this.tagState = TagState.MID_SELF_CLOSING;
            return this;
        }
        
        @NotNull
        @Override
        public TokenEmitter argument(@NotNull final String arg) {
            if (!this.tagState.isTag) {
                throw new IllegalStateException("Not within a tag!");
            }
            this.consumer.append(':');
            this.escapeTagContent(arg, null);
            return this;
        }
        
        @NotNull
        @Override
        public TokenEmitter argument(@NotNull final String arg, @NotNull final QuotingOverride quotingPreference) {
            if (!this.tagState.isTag) {
                throw new IllegalStateException("Not within a tag!");
            }
            this.consumer.append(':');
            this.escapeTagContent(arg, (QuotingOverride)Objects.requireNonNull((Object)quotingPreference, "quotingPreference"));
            return this;
        }
        
        @NotNull
        @Override
        public TokenEmitter argument(@NotNull final Component arg) {
            final String serialized = MiniMessageSerializer.serialize(arg, this.resolver, this.strict);
            return this.argument(serialized, QuotingOverride.QUOTED);
        }
        
        @NotNull
        @Override
        public Collector text(@NotNull final String text) {
            this.completeTag();
            appendEscaping(this.consumer, text, Collector.TEXT_ESCAPES, true);
            return this;
        }
        
        private void escapeTagContent(final String content, @Nullable final QuotingOverride preference) {
            boolean mustBeQuoted = preference == QuotingOverride.QUOTED;
            boolean hasSingleQuote = false;
            boolean hasDoubleQuote = false;
            for (int i = 0; i < content.length(); ++i) {
                final char active = content.charAt(i);
                if (active == '>' || active == ':' || active == ' ') {
                    mustBeQuoted = true;
                    if (hasSingleQuote && hasDoubleQuote) {
                        break;
                    }
                }
                else {
                    if (active == '\'') {
                        hasSingleQuote = true;
                        break;
                    }
                    if (active == '\"') {
                        hasDoubleQuote = true;
                        if (mustBeQuoted && hasSingleQuote) {
                            break;
                        }
                    }
                }
            }
            if (hasSingleQuote) {
                this.consumer.append('\"');
                appendEscaping(this.consumer, content, Collector.DOUBLE_QUOTED_ESCAPES, true);
                this.consumer.append('\"');
            }
            else if (hasDoubleQuote || mustBeQuoted) {
                this.consumer.append('\'');
                appendEscaping(this.consumer, content, Collector.SINGLE_QUOTED_ESCAPES, true);
                this.consumer.append('\'');
            }
            else {
                appendEscaping(this.consumer, content, Collector.TAG_TOKENS, false);
            }
        }
        
        static void appendEscaping(final StringBuilder builder, final String text, final char[] escapeChars, final boolean allowEscapes) {
            int startIdx = 0;
            boolean unescapedFound = false;
            for (int i = 0; i < text.length(); ++i) {
                final char test = text.charAt(i);
                boolean escaped = false;
                final int length = escapeChars.length;
                int j = 0;
                while (j < length) {
                    final char c = escapeChars[j];
                    if (test == c) {
                        if (!allowEscapes) {
                            throw new IllegalArgumentException("Invalid escapable character '" + test + "' found at index " + i + " in string '" + text + "'");
                        }
                        escaped = true;
                        break;
                    }
                    else {
                        ++j;
                    }
                }
                if (escaped) {
                    if (unescapedFound) {
                        builder.append((CharSequence)text, startIdx, i);
                    }
                    startIdx = i + 1;
                    builder.append('\\').append(test);
                }
                else {
                    unescapedFound = true;
                }
            }
            if (startIdx < text.length() && unescapedFound) {
                builder.append((CharSequence)text, startIdx, text.length());
            }
        }
        
        @NotNull
        @Override
        public Collector pop() {
            this.emitClose(this.popTag(false));
            return this;
        }
        
        private void emitClose(@NotNull final String tag) {
            if (this.tagState.isTag) {
                if (this.tagState == TagState.MID) {
                    this.consumer.append('/');
                }
                this.consumer.append('>');
                this.tagState = TagState.TEXT;
            }
            else {
                this.consumer.append('<').append('/');
                this.escapeTagContent(tag, QuotingOverride.UNQUOTED);
                this.consumer.append('>');
            }
        }
        
        @Override
        public void style(@NotNull final String claimKey, @NotNull final Emitable styleClaim) {
            if (this.claimedStyleElements.add((Object)Objects.requireNonNull((Object)claimKey, "claimKey"))) {
                styleClaim.emit(this);
            }
        }
        
        @Override
        public boolean component(@NotNull final Emitable componentClaim) {
            if (this.componentClaim != null) {
                return false;
            }
            this.componentClaim = (Emitable)Objects.requireNonNull((Object)componentClaim, "componentClaim");
            return true;
        }
        
        @Override
        public boolean componentClaimed() {
            return this.componentClaim != null;
        }
        
        @Override
        public boolean styleClaimed(@NotNull final String claimId) {
            return this.claimedStyleElements.contains((Object)claimId);
        }
        
        void flushClaims(final Component component) {
            if (this.componentClaim != null) {
                this.componentClaim.emit(this);
                this.componentClaim = null;
            }
            else {
                if (!(component instanceof TextComponent)) {
                    throw new IllegalStateException("Unclaimed component " + (Object)component);
                }
                this.text(((TextComponent)component).content());
            }
            this.claimedStyleElements.clear();
        }
        
        static {
            TEXT_ESCAPES = new char[] { '\\', '<' };
            TAG_TOKENS = new char[] { '>', ':' };
            SINGLE_QUOTED_ESCAPES = new char[] { '\\', '\'' };
            DOUBLE_QUOTED_ESCAPES = new char[] { '\\', '\"' };
        }
        
        enum TagState
        {
            TEXT(false), 
            MID(true), 
            MID_SELF_CLOSING(true);
            
            final boolean isTag;
            
            private TagState(final boolean isTag) {
                this.isTag = isTag;
            }
        }
    }
}
