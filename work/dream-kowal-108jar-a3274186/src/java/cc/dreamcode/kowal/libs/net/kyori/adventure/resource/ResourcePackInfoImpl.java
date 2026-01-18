package cc.dreamcode.kowal.libs.net.kyori.adventure.resource;

import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.net.URI;
import java.util.UUID;

final class ResourcePackInfoImpl implements ResourcePackInfo
{
    private final UUID id;
    private final URI uri;
    private final String hash;
    
    ResourcePackInfoImpl(@NotNull final UUID id, @NotNull final URI uri, @NotNull final String hash) {
        this.id = (UUID)Objects.requireNonNull((Object)id, "id");
        this.uri = (URI)Objects.requireNonNull((Object)uri, "uri");
        this.hash = (String)Objects.requireNonNull((Object)hash, "hash");
    }
    
    @NotNull
    @Override
    public UUID id() {
        return this.id;
    }
    
    @NotNull
    @Override
    public URI uri() {
        return this.uri;
    }
    
    @NotNull
    @Override
    public String hash() {
        return this.hash;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("id", this.id), ExaminableProperty.of("uri", this.uri), ExaminableProperty.of("hash", this.hash) });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ResourcePackInfoImpl)) {
            return false;
        }
        final ResourcePackInfoImpl that = (ResourcePackInfoImpl)other;
        return this.id.equals((Object)that.id) && this.uri.equals((Object)that.uri) && this.hash.equals((Object)that.hash);
    }
    
    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.uri.hashCode();
        result = 31 * result + this.hash.hashCode();
        return result;
    }
    
    static CompletableFuture<String> computeHash(final URI uri, final Executor exec) {
        final CompletableFuture<String> result = (CompletableFuture<String>)new CompletableFuture();
        exec.execute(() -> {
            try {
                final URL url = uri.toURL();
                final URLConnection conn = url.openConnection();
                conn.addRequestProperty("User-Agent", "adventure/" + ResourcePackInfoImpl.class.getPackage().getSpecificationVersion() + " (pack-fetcher)");
                try (final InputStream is = conn.getInputStream()) {
                    final MessageDigest digest = MessageDigest.getInstance("SHA-1");
                    final byte[] buf = new byte[8192];
                    int read;
                    while ((read = is.read(buf)) != -1) {
                        digest.update(buf, 0, read);
                    }
                    result.complete((Object)bytesToString(digest.digest()));
                }
            }
            catch (final IOException | NoSuchAlgorithmException ex) {
                result.completeExceptionally((Throwable)ex);
            }
        });
        return result;
    }
    
    static String bytesToString(final byte[] arr) {
        final StringBuilder builder = new StringBuilder(arr.length * 2);
        final Formatter fmt = new Formatter((Appendable)builder, Locale.ROOT);
        for (int i = 0; i < arr.length; ++i) {
            fmt.format("%02x", new Object[] { arr[i] & 0xFF });
        }
        return builder.toString();
    }
    
    static final class BuilderImpl implements Builder
    {
        private UUID id;
        private URI uri;
        private String hash;
        
        @NotNull
        @Override
        public Builder id(@NotNull final UUID id) {
            this.id = (UUID)Objects.requireNonNull((Object)id, "id");
            return this;
        }
        
        @NotNull
        @Override
        public Builder uri(@NotNull final URI uri) {
            this.uri = (URI)Objects.requireNonNull((Object)uri, "uri");
            if (this.id == null) {
                this.id = UUID.nameUUIDFromBytes(uri.toString().getBytes(StandardCharsets.UTF_8));
            }
            return this;
        }
        
        @NotNull
        @Override
        public Builder hash(@NotNull final String hash) {
            this.hash = (String)Objects.requireNonNull((Object)hash, "hash");
            return this;
        }
        
        @NotNull
        @Override
        public ResourcePackInfo build() {
            return new ResourcePackInfoImpl(this.id, this.uri, this.hash);
        }
        
        @NotNull
        @Override
        public CompletableFuture<ResourcePackInfo> computeHashAndBuild(@NotNull final Executor executor) {
            return (CompletableFuture<ResourcePackInfo>)ResourcePackInfoImpl.computeHash((URI)Objects.requireNonNull((Object)this.uri, "uri"), executor).thenApply(hash -> {
                this.hash(hash);
                return this.build();
            });
        }
    }
}
