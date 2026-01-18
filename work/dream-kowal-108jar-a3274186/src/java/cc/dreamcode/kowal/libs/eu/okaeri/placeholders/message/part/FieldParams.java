package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part;

import java.util.Arrays;
import java.math.BigDecimal;
import lombok.NonNull;

public class FieldParams
{
    private final String field;
    private final String[] params;
    
    public static FieldParams of(final String field, @NonNull final String[] params) {
        if (params == null) {
            throw new NullPointerException("params is marked non-null but is null");
        }
        return new FieldParams(field, params);
    }
    
    public static FieldParams empty(final String field) {
        return new FieldParams(field, new String[0]);
    }
    
    public String[] strArr() {
        return this.params;
    }
    
    public int[] intArr() {
        final int[] arr = new int[this.params.length];
        for (int i = 0; i < this.params.length; ++i) {
            arr[i] = this.intAt(i, 0);
        }
        return arr;
    }
    
    public double[] doubleArr() {
        final double[] arr = new double[this.params.length];
        for (int i = 0; i < this.params.length; ++i) {
            arr[i] = this.doubleAt(i, 0.0);
        }
        return arr;
    }
    
    public String strAt(final int pos) {
        return this.strAt(pos, "");
    }
    
    public String strAt(final int pos, final String def) {
        if (pos >= this.params.length) {
            return def;
        }
        return this.params[pos];
    }
    
    public double doubleAt(final int pos) {
        return this.doubleAt(pos, 0.0);
    }
    
    public double doubleAt(final int pos, final double def) {
        final String str = this.strAt(pos, String.valueOf(def));
        try {
            return new BigDecimal(str).doubleValue();
        }
        catch (final NumberFormatException exception) {
            return def;
        }
    }
    
    public int intAt(final int pos) {
        return this.intAt(pos, 0);
    }
    
    public int intAt(final int pos, final int def) {
        final String str = this.strAt(pos, String.valueOf(def));
        try {
            return new BigDecimal(str).intValue();
        }
        catch (final NumberFormatException exception) {
            return def;
        }
    }
    
    public String getField() {
        return this.field;
    }
    
    public String[] getParams() {
        return this.params;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldParams)) {
            return false;
        }
        final FieldParams other = (FieldParams)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$field = this.getField();
        final Object other$field = other.getField();
        if (this$field == null) {
            if (other$field == null) {
                return Arrays.deepEquals((Object[])this.getParams(), (Object[])other.getParams());
            }
        }
        else if (this$field.equals(other$field)) {
            return Arrays.deepEquals((Object[])this.getParams(), (Object[])other.getParams());
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FieldParams;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $field = this.getField();
        result = result * 59 + (($field == null) ? 43 : $field.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getParams());
        return result;
    }
    
    @Override
    public String toString() {
        return "FieldParams(field=" + this.getField() + ", params=" + Arrays.deepToString((Object[])this.getParams()) + ")";
    }
    
    private FieldParams(final String field, final String[] params) {
        this.field = field;
        this.params = params;
    }
}
