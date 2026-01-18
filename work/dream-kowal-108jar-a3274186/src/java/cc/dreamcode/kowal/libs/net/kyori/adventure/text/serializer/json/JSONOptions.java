package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import cc.dreamcode.kowal.libs.net.kyori.option.Option;

public final class JSONOptions
{
    private static final int VERSION_INITIAL = 0;
    private static final int VERSION_1_16 = 2526;
    private static final int VERSION_1_20_3 = 3679;
    private static final int VERSION_1_20_5 = 3819;
    public static final Option<Boolean> EMIT_RGB;
    public static final Option<HoverEventValueMode> EMIT_HOVER_EVENT_TYPE;
    public static final Option<Boolean> EMIT_COMPACT_TEXT_COMPONENT;
    public static final Option<Boolean> EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY;
    public static final Option<Boolean> VALIDATE_STRICT_EVENTS;
    public static final Option<Boolean> EMIT_DEFAULT_ITEM_HOVER_QUANTITY;
    public static final Option<ShowItemHoverDataMode> SHOW_ITEM_HOVER_DATA_MODE;
    private static final OptionState.Versioned BY_DATA_VERSION;
    private static final OptionState MOST_COMPATIBLE;
    
    private JSONOptions() {
    }
    
    private static String key(final String value) {
        return "adventure:json/" + value;
    }
    
    public static OptionState.Versioned byDataVersion() {
        return JSONOptions.BY_DATA_VERSION;
    }
    
    @NotNull
    public static OptionState compatibility() {
        return JSONOptions.MOST_COMPATIBLE;
    }
    
    static {
        EMIT_RGB = Option.booleanOption(key("emit/rgb"), true);
        EMIT_HOVER_EVENT_TYPE = Option.enumOption(key("emit/hover_value_mode"), HoverEventValueMode.class, HoverEventValueMode.MODERN_ONLY);
        EMIT_COMPACT_TEXT_COMPONENT = Option.booleanOption(key("emit/compact_text_component"), true);
        EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY = Option.booleanOption(key("emit/hover_show_entity_id_as_int_array"), true);
        VALIDATE_STRICT_EVENTS = Option.booleanOption(key("validate/strict_events"), true);
        EMIT_DEFAULT_ITEM_HOVER_QUANTITY = Option.booleanOption(key("emit/default_item_hover_quantity"), true);
        SHOW_ITEM_HOVER_DATA_MODE = Option.enumOption(key("emit/show_item_hover_data"), ShowItemHoverDataMode.class, ShowItemHoverDataMode.EMIT_EITHER);
        BY_DATA_VERSION = OptionState.versionedOptionState().version(0, (Consumer<OptionState.Builder>)(b -> b.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.LEGACY_ONLY).value(JSONOptions.EMIT_RGB, false).value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false).value(JSONOptions.VALIDATE_STRICT_EVENTS, false).value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY, false).value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_LEGACY_NBT))).version(2526, (Consumer<OptionState.Builder>)(b -> b.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.MODERN_ONLY).value(JSONOptions.EMIT_RGB, true))).version(3679, (Consumer<OptionState.Builder>)(b -> b.value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT, true).value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, true).value(JSONOptions.VALIDATE_STRICT_EVENTS, true))).version(3819, (Consumer<OptionState.Builder>)(b -> b.value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY, true).value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_DATA_COMPONENTS))).build();
        MOST_COMPATIBLE = OptionState.optionState().value(JSONOptions.EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.BOTH).value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false).value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT, false).value(JSONOptions.VALIDATE_STRICT_EVENTS, false).value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_EITHER).build();
    }
    
    public enum HoverEventValueMode
    {
        MODERN_ONLY, 
        LEGACY_ONLY, 
        BOTH;
    }
    
    public enum ShowItemHoverDataMode
    {
        EMIT_LEGACY_NBT, 
        EMIT_DATA_COMPONENTS, 
        EMIT_EITHER;
    }
}
