package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import java.util.AbstractMap;
import java.util.Map;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

final class BinaryTagReaderImpl implements BinaryTagIO.Reader
{
    private final long maxBytes;
    static final BinaryTagIO.Reader UNLIMITED;
    static final BinaryTagIO.Reader DEFAULT_LIMIT;
    
    BinaryTagReaderImpl(final long maxBytes) {
        this.maxBytes = maxBytes;
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag read(@NotNull final Path path, final BinaryTagIO.Compression compression) throws IOException {
        try (final InputStream is = Files.newInputStream(path, new OpenOption[0])) {
            return this.read(is, compression);
        }
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag read(@NotNull final InputStream input, final BinaryTagIO.Compression compression) throws IOException {
        try (final DataInputStream dis = new DataInputStream((InputStream)new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(input))))) {
            return this.read((DataInput)dis);
        }
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag read(@NotNull final DataInput input) throws IOException {
        return this.read(input, true);
    }
    
    @NotNull
    private CompoundBinaryTag read(@NotNull DataInput input, final boolean named) throws IOException {
        if (!(input instanceof TrackingDataInput)) {
            input = (DataInput)new TrackingDataInput(input, this.maxBytes);
        }
        final BinaryTagType<? extends BinaryTag> type = BinaryTagType.binaryTagType(input.readByte());
        requireCompound(type);
        if (named) {
            input.skipBytes(input.readUnsignedShort());
        }
        return BinaryTagTypes.COMPOUND.read(input);
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag readNameless(@NotNull final Path path, final BinaryTagIO.Compression compression) throws IOException {
        try (final InputStream is = Files.newInputStream(path, new OpenOption[0])) {
            return this.readNameless(is, compression);
        }
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag readNameless(@NotNull final InputStream input, final BinaryTagIO.Compression compression) throws IOException {
        try (final DataInputStream dis = new DataInputStream((InputStream)new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(input))))) {
            return this.readNameless((DataInput)dis);
        }
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag readNameless(@NotNull final DataInput input) throws IOException {
        return this.read(input, false);
    }
    
    @Override
    public Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final Path path, final BinaryTagIO.Compression compression) throws IOException {
        try (final InputStream is = Files.newInputStream(path, new OpenOption[0])) {
            return this.readNamed(is, compression);
        }
    }
    
    @Override
    public Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final InputStream input, final BinaryTagIO.Compression compression) throws IOException {
        try (final DataInputStream dis = new DataInputStream((InputStream)new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(input))))) {
            return this.readNamed((DataInput)dis);
        }
    }
    
    @Override
    public Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull final DataInput input) throws IOException {
        final BinaryTagType<? extends BinaryTag> type = BinaryTagType.binaryTagType(input.readByte());
        requireCompound(type);
        final String name = input.readUTF();
        return (Map.Entry<String, CompoundBinaryTag>)new AbstractMap.SimpleImmutableEntry((Object)name, (Object)BinaryTagTypes.COMPOUND.read(input));
    }
    
    private static void requireCompound(final BinaryTagType<? extends BinaryTag> type) throws IOException {
        if (type != BinaryTagTypes.COMPOUND) {
            throw new IOException(String.format("Expected root tag to be a %s, was %s", new Object[] { BinaryTagTypes.COMPOUND, type }));
        }
    }
    
    static {
        UNLIMITED = new BinaryTagReaderImpl(-1L);
        DEFAULT_LIMIT = new BinaryTagReaderImpl(131082L);
    }
}
