package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentBuilder;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import java.util.PrimitiveIterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import java.util.List;
import java.util.Collections;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Inserting;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree.Node;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Modifying;

abstract class AbstractColorChangingTag implements Modifying, Examinable
{
    private static final ComponentFlattener LENGTH_CALCULATOR;
    private boolean visited;
    private int size;
    private int disableApplyingColorDepth;
    
    AbstractColorChangingTag() {
        this.size = 0;
        this.disableApplyingColorDepth = -1;
    }
    
    protected final int size() {
        return this.size;
    }
    
    @Override
    public final void visit(@NotNull final Node current, final int depth) {
        if (this.visited) {
            throw new IllegalStateException("Color changing tag instances cannot be re-used, return a new one for each resolve");
        }
        if (current instanceof ValueNode) {
            final String value = ((ValueNode)current).value();
            this.size += value.codePointCount(0, value.length());
        }
        else if (current instanceof TagNode) {
            final TagNode tag = (TagNode)current;
            if (tag.tag() instanceof Inserting) {
                AbstractColorChangingTag.LENGTH_CALCULATOR.flatten(((Inserting)tag.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
            }
        }
    }
    
    @Override
    public final void postVisit() {
        this.visited = true;
        this.init();
    }
    
    @Override
    public final Component apply(@NotNull final Component current, final int depth) {
        if ((this.disableApplyingColorDepth != -1 && depth > this.disableApplyingColorDepth) || current.style().color() != null) {
            if (this.disableApplyingColorDepth == -1 || depth < this.disableApplyingColorDepth) {
                this.disableApplyingColorDepth = depth;
            }
            if (current instanceof TextComponent) {
                final String content = ((TextComponent)current).content();
                for (int len = content.codePointCount(0, content.length()), i = 0; i < len; ++i) {
                    this.advanceColor();
                }
            }
            return current.children((List<? extends ComponentLike>)Collections.emptyList());
        }
        this.disableApplyingColorDepth = -1;
        if (current instanceof TextComponent && ((TextComponent)current).content().length() > 0) {
            final TextComponent textComponent = (TextComponent)current;
            final String content2 = textComponent.content();
            final TextComponent.Builder parent = Component.text();
            final int[] holder = { 0 };
            final PrimitiveIterator.OfInt it = content2.codePoints().iterator();
            while (it.hasNext()) {
                holder[0] = it.nextInt();
                final Component comp = Component.text(new String(holder, 0, 1), current.style().color(this.color()));
                this.advanceColor();
                parent.append(comp);
            }
            return ((ComponentBuilder<Component, B>)parent).build();
        }
        if (!(current instanceof TextComponent)) {
            final Component ret = current.children((List<? extends ComponentLike>)Collections.emptyList()).colorIfAbsent(this.color());
            this.advanceColor();
            return ret;
        }
        return Component.empty().mergeStyle(current);
    }
    
    protected abstract void init();
    
    protected abstract void advanceColor();
    
    protected abstract TextColor color();
    
    @NotNull
    @Override
    public abstract Stream<? extends ExaminableProperty> examinableProperties();
    
    @NotNull
    @Override
    public final String toString() {
        return Internals.toString(this);
    }
    
    @Override
    public abstract boolean equals(@Nullable final Object other);
    
    @Override
    public abstract int hashCode();
    
    static {
        LENGTH_CALCULATOR = ComponentFlattener.builder().mapper(TextComponent.class, (java.util.function.Function<TextComponent, String>)TextComponent::content).unknownMapper((Function<Component, String>)(x -> "_")).build();
    }
}
