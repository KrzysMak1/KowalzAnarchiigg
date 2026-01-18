package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import lombok.Generated;
import lombok.NonNull;

public class CommandInput
{
    private final String label;
    private final String[] arguments;
    private final boolean spaceAtTheEnd;
    
    public CommandInput(@NonNull final String input) {
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        final String[] params = input.replace((CharSequence)"/", (CharSequence)"").split(" ");
        if (params.length == 0) {
            throw new RuntimeException("Parse params cannot be empty (input)");
        }
        this.label = params[0];
        System.arraycopy((Object)params, 1, (Object)(this.arguments = new String[params.length - 1]), 0, this.arguments.length);
        this.spaceAtTheEnd = input.endsWith(" ");
    }
    
    public String[] getParams() {
        final String[] params = new String[this.arguments.length + 1];
        params[0] = this.label;
        System.arraycopy((Object)this.arguments, 0, (Object)params, 1, this.arguments.length);
        return params;
    }
    
    @Generated
    public String getLabel() {
        return this.label;
    }
    
    @Generated
    public String[] getArguments() {
        return this.arguments;
    }
    
    @Generated
    public boolean isSpaceAtTheEnd() {
        return this.spaceAtTheEnd;
    }
    
    @Generated
    public CommandInput(final String label, final String[] arguments, final boolean spaceAtTheEnd) {
        this.label = label;
        this.arguments = arguments;
        this.spaceAtTheEnd = spaceAtTheEnd;
    }
}
