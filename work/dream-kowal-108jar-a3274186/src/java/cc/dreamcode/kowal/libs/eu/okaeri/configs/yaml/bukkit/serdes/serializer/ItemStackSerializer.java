package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer;

import org.bukkit.Material;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackSpecData;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackFormat;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer.experimental.StringBase64ItemStackTransformer;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class ItemStackSerializer implements ObjectSerializer<ItemStack>
{
    private static final ItemMetaSerializer ITEM_META_SERIALIZER;
    private static final StringBase64ItemStackTransformer ITEM_STACK_TRANSFORMER;
    private boolean failsafe;
    
    @Override
    public boolean supports(@NonNull final Class<? super ItemStack> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ItemStack.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final ItemStack itemStack, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("material", itemStack.getType());
        if (itemStack.getAmount() != 1) {
            data.add("amount", itemStack.getAmount());
        }
        if (itemStack.getDurability() != 0) {
            data.add("durability", itemStack.getDurability());
        }
        final ItemStackFormat format = (ItemStackFormat)data.getContext().getAttachment(ItemStackSpecData.class).map(ItemStackSpecData::getFormat).orElse((Object)ItemStackFormat.NATURAL);
        if (!itemStack.hasItemMeta()) {
            return;
        }
        switch (format) {
            case NATURAL: {
                data.add("meta", itemStack.getItemMeta(), ItemMeta.class);
                break;
            }
            case COMPACT: {
                ItemStackSerializer.ITEM_META_SERIALIZER.serialize(itemStack.getItemMeta(), data, generics);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown format: " + (Object)format);
            }
        }
        if (!this.failsafe) {
            return;
        }
        final DeserializationData deserializationData = new DeserializationData(data.asMap(), data.getConfigurer(), data.getContext());
        final ItemStack deserializedStack = this.deserialize(deserializationData, generics);
        if (deserializedStack.equals((Object)itemStack)) {
            return;
        }
        data.clear();
        final String base64Stack = ItemStackSerializer.ITEM_STACK_TRANSFORMER.leftToRight(itemStack, data.getContext());
        data.add("base64", base64Stack);
    }
    
    @Override
    public ItemStack deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (data.containsKey("base64")) {
            final String base64Stack = data.get("base64", String.class);
            return ItemStackSerializer.ITEM_STACK_TRANSFORMER.rightToLeft(base64Stack, data.getContext());
        }
        final String materialName = data.get("material", String.class);
        final Material material = Material.valueOf(materialName);
        final int amount = data.containsKey("amount") ? data.get("amount", Integer.class) : 1;
        final short durability = (short)(data.containsKey("durability") ? ((short)data.get("durability", Short.class)) : 0);
        final ItemStackFormat format = (ItemStackFormat)data.getContext().getAttachment(ItemStackSpecData.class).map(ItemStackSpecData::getFormat).orElse((Object)ItemStackFormat.NATURAL);
        ItemMeta itemMeta = null;
        switch (format) {
            case NATURAL: {
                if (data.containsKey("display") || data.containsKey("display-name")) {
                    itemMeta = ItemStackSerializer.ITEM_META_SERIALIZER.deserialize(data, generics);
                    break;
                }
                itemMeta = (data.containsKey("meta") ? data.get("meta", ItemMeta.class) : data.get("item-meta", ItemMeta.class));
                break;
            }
            case COMPACT: {
                if (data.containsKey("meta")) {
                    itemMeta = data.get("meta", ItemMeta.class);
                    break;
                }
                if (data.containsKey("item-meta")) {
                    itemMeta = data.get("item-meta", ItemMeta.class);
                    break;
                }
                itemMeta = ItemStackSerializer.ITEM_META_SERIALIZER.deserialize(data, generics);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown format: " + (Object)format);
            }
        }
        final ItemStack itemStack = new ItemStack(material, amount);
        itemStack.setItemMeta(itemMeta);
        itemStack.setDurability(durability);
        return itemStack;
    }
    
    public ItemStackSerializer() {
        this.failsafe = false;
    }
    
    public ItemStackSerializer(final boolean failsafe) {
        this.failsafe = false;
        this.failsafe = failsafe;
    }
    
    static {
        ITEM_META_SERIALIZER = new ItemMetaSerializer();
        ITEM_STACK_TRANSFORMER = new StringBase64ItemStackTransformer();
    }
}
