package cc.dreamcode.kowal.libs.net.kyori.adventure.inventory;

import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;

final class BookImpl implements Book
{
    private final Component title;
    private final Component author;
    private final List<Component> pages;
    
    BookImpl(@NotNull final Component title, @NotNull final Component author, @NotNull final List<Component> pages) {
        this.title = (Component)Objects.requireNonNull((Object)title, "title");
        this.author = (Component)Objects.requireNonNull((Object)author, "author");
        this.pages = (List<Component>)Collections.unmodifiableList((List)Objects.requireNonNull((Object)pages, "pages"));
    }
    
    @NotNull
    @Override
    public Component title() {
        return this.title;
    }
    
    @NotNull
    @Override
    public Book title(@NotNull final Component title) {
        return new BookImpl((Component)Objects.requireNonNull((Object)title, "title"), this.author, this.pages);
    }
    
    @NotNull
    @Override
    public Component author() {
        return this.author;
    }
    
    @NotNull
    @Override
    public Book author(@NotNull final Component author) {
        return new BookImpl(this.title, (Component)Objects.requireNonNull((Object)author, "author"), this.pages);
    }
    
    @NotNull
    @Override
    public List<Component> pages() {
        return this.pages;
    }
    
    @NotNull
    @Override
    public Book pages(@NotNull final List<Component> pages) {
        return new BookImpl(this.title, this.author, (List<Component>)new ArrayList((Collection)Objects.requireNonNull((Object)pages, "pages")));
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("title", this.title), ExaminableProperty.of("author", this.author), ExaminableProperty.of("pages", this.pages) });
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookImpl)) {
            return false;
        }
        final BookImpl that = (BookImpl)o;
        return this.title.equals(that.title) && this.author.equals(that.author) && this.pages.equals((Object)that.pages);
    }
    
    @Override
    public int hashCode() {
        int result = this.title.hashCode();
        result = 31 * result + this.author.hashCode();
        result = 31 * result + this.pages.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    static final class BuilderImpl implements Builder
    {
        private Component title;
        private Component author;
        private final List<Component> pages;
        
        BuilderImpl() {
            this.title = Component.empty();
            this.author = Component.empty();
            this.pages = (List<Component>)new ArrayList();
        }
        
        @NotNull
        @Override
        public Builder title(@NotNull final Component title) {
            this.title = (Component)Objects.requireNonNull((Object)title, "title");
            return this;
        }
        
        @NotNull
        @Override
        public Builder author(@NotNull final Component author) {
            this.author = (Component)Objects.requireNonNull((Object)author, "author");
            return this;
        }
        
        @NotNull
        @Override
        public Builder addPage(@NotNull final Component page) {
            this.pages.add((Object)Objects.requireNonNull((Object)page, "page"));
            return this;
        }
        
        @NotNull
        @Override
        public Builder pages(@NotNull final Collection<Component> pages) {
            this.pages.addAll((Collection)Objects.requireNonNull((Object)pages, "pages"));
            return this;
        }
        
        @NotNull
        @Override
        public Builder pages(@NotNull final Component... pages) {
            Collections.addAll((Collection)this.pages, (Object[])pages);
            return this;
        }
        
        @NotNull
        @Override
        public Book build() {
            return new BookImpl(this.title, this.author, (List<Component>)new ArrayList((Collection)this.pages));
        }
    }
}
