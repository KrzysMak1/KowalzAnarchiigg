package cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor;

import java.util.List;

public interface ConfigSectionWalker
{
    boolean isKey(final String line);
    
    boolean isKeyMultilineStart(final String line);
    
    String readName(final String line);
    
    String update(final String line, final ConfigLineInfo lineInfo, final List<ConfigLineInfo> path);
}
