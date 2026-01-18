package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.DataInput;

final class TrackingDataInput implements DataInput, BinaryTagScope
{
    private static final int MAX_DEPTH = 512;
    private final DataInput input;
    private final long maxLength;
    private long counter;
    private int depth;
    
    TrackingDataInput(final DataInput input, final long maxLength) {
        this.input = input;
        this.maxLength = maxLength;
    }
    
    public static BinaryTagScope enter(final DataInput input) throws IOException {
        if (input instanceof TrackingDataInput) {
            return ((TrackingDataInput)input).enter();
        }
        return NoOp.INSTANCE;
    }
    
    public static BinaryTagScope enter(final DataInput input, final long expectedSize) throws IOException {
        if (input instanceof TrackingDataInput) {
            return ((TrackingDataInput)input).enter(expectedSize);
        }
        return NoOp.INSTANCE;
    }
    
    public DataInput input() {
        return this.input;
    }
    
    public TrackingDataInput enter(final long expectedSize) throws IOException {
        if (this.depth++ > 512) {
            throw new IOException("NBT read exceeded maximum depth of 512");
        }
        this.ensureMaxLength(expectedSize);
        return this;
    }
    
    public TrackingDataInput enter() throws IOException {
        return this.enter(0L);
    }
    
    public void exit() throws IOException {
        --this.depth;
        this.ensureMaxLength(0L);
    }
    
    private void ensureMaxLength(final long expected) throws IOException {
        if (this.maxLength > 0L && this.counter + expected > this.maxLength) {
            throw new IOException("The read NBT was longer than the maximum allowed size of " + this.maxLength + " bytes!");
        }
    }
    
    public void readFully(final byte[] array) throws IOException {
        this.counter += array.length;
        this.input.readFully(array);
    }
    
    public void readFully(final byte[] array, final int off, final int len) throws IOException {
        this.counter += len;
        this.input.readFully(array, off, len);
    }
    
    public int skipBytes(final int n) throws IOException {
        return this.input.skipBytes(n);
    }
    
    public boolean readBoolean() throws IOException {
        ++this.counter;
        return this.input.readBoolean();
    }
    
    public byte readByte() throws IOException {
        ++this.counter;
        return this.input.readByte();
    }
    
    public int readUnsignedByte() throws IOException {
        ++this.counter;
        return this.input.readUnsignedByte();
    }
    
    public short readShort() throws IOException {
        this.counter += 2L;
        return this.input.readShort();
    }
    
    public int readUnsignedShort() throws IOException {
        this.counter += 2L;
        return this.input.readUnsignedShort();
    }
    
    public char readChar() throws IOException {
        this.counter += 2L;
        return this.input.readChar();
    }
    
    public int readInt() throws IOException {
        this.counter += 4L;
        return this.input.readInt();
    }
    
    public long readLong() throws IOException {
        this.counter += 8L;
        return this.input.readLong();
    }
    
    public float readFloat() throws IOException {
        this.counter += 4L;
        return this.input.readFloat();
    }
    
    public double readDouble() throws IOException {
        this.counter += 8L;
        return this.input.readDouble();
    }
    
    @Nullable
    public String readLine() throws IOException {
        final String result = this.input.readLine();
        if (result != null) {
            this.counter += result.length() + 1;
        }
        return result;
    }
    
    @NotNull
    public String readUTF() throws IOException {
        final String result = this.input.readUTF();
        this.counter += result.length() * 2L + 2L;
        return result;
    }
    
    public void close() throws IOException {
        this.exit();
    }
}
