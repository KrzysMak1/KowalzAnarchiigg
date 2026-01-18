package cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor.format;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor.ConfigSectionWalker;

public abstract class YamlSectionWalker implements ConfigSectionWalker
{
    private static final Set<String> MULTILINE_START;
    
    @Override
    public boolean isKeyMultilineStart(final String line) {
        final String trimmed = line.trim().replaceAll("\\s{2,}", " ");
        if (trimmed.isEmpty()) {
            return false;
        }
        final int colon = trimmed.indexOf(":");
        final int distance = trimmed.length() - colon;
        for (final String trigger : YamlSectionWalker.MULTILINE_START) {
            if (distance > trigger.length() + 2) {
                continue;
            }
            if (!trimmed.endsWith(trigger)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isKey(final String line) {
        final String name = this.readName(line);
        return !name.isEmpty() && name.charAt(0) != '-' && name.charAt(0) != '#';
    }
    
    @Override
    public String readName(final String line) {
        return line.split(":", 2)[0].trim();
    }
    
    static {
        MULTILINE_START = (Set)new HashSet((Collection)Arrays.asList((Object[])new String[] { ">", ">-", "|", "|-" }));
    }
}
