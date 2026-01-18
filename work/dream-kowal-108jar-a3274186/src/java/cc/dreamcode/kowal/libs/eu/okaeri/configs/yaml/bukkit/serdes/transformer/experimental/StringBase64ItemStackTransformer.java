package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer.experimental;

import java.io.InputStream;
import org.bukkit.util.io.BukkitObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.io.OutputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.ByteArrayOutputStream;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class StringBase64ItemStackTransformer extends BidirectionalTransformer<ItemStack, String>
{
    @Override
    public GenericsPair<ItemStack, String> getPair() {
        return this.genericsPair(ItemStack.class, String.class);
    }
    
    @Override
    public String leftToRight(@NonNull final ItemStack data, @NonNull final SerdesContext serdesContext) {
        try {
            if (data == null) {
                throw new NullPointerException("data is marked non-null but is null");
            }
            if (serdesContext == null) {
                throw new NullPointerException("serdesContext is marked non-null but is null");
            }
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeObject((Object)data);
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray()).trim();
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public ItemStack rightToLeft(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        try {
            if (data == null) {
                throw new NullPointerException("data is marked non-null but is null");
            }
            if (serdesContext == null) {
                throw new NullPointerException("serdesContext is marked non-null but is null");
            }
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            final ItemStack item = (ItemStack)dataInput.readObject();
            dataInput.close();
            return item;
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
}
