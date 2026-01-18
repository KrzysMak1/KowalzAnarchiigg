package cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure.serializer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.minecraft.NoticeType;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure.BukkitNotice;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class BukkitNoticeSerializer implements ObjectSerializer<BukkitNotice>
{
    @Override
    public boolean supports(@NonNull final Class<? super BukkitNotice> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BukkitNotice.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final BukkitNotice object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("type", object.getNoticeType(), NoticeType.class);
        data.add("text", object.getRaw(), String.class);
        if (object.getTitleFadeIn() != 10) {
            data.add("title-fade-in", object.getTitleFadeIn());
        }
        if (object.getTitleStay() != 20) {
            data.add("title-stay", object.getTitleStay());
        }
        if (object.getTitleFadeOut() != 10) {
            data.add("title-fade-out", object.getTitleFadeOut());
        }
    }
    
    @Override
    public BukkitNotice deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final BukkitNotice minecraftNotice = new BukkitNotice(data.get("type", NoticeType.class), new String[] { data.get("text", String.class) });
        if (data.containsKey("title-fade-in")) {
            minecraftNotice.setTitleFadeIn(data.get("title-fade-in", Integer.class));
        }
        if (data.containsKey("title-stay")) {
            minecraftNotice.setTitleStay(data.get("title-stay", Integer.class));
        }
        if (data.containsKey("title-fade-out")) {
            minecraftNotice.setTitleFadeOut(data.get("title-fade-out", Integer.class));
        }
        return minecraftNotice;
    }
}
