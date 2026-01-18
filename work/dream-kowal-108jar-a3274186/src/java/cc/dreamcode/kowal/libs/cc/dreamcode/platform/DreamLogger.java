package cc.dreamcode.kowal.libs.cc.dreamcode.platform;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

public interface DreamLogger
{
    void info(final String text);
    
    void debug(final String text);
    
    void warning(final String text);
    
    void error(final String text);
    
    void error(final String text, final Throwable throwable);
    
    public static class Builder
    {
        private String type;
        private String name;
        private Long took;
        private final Map<String, Object> meta;
        private final List<String> footer;
        
        public Builder() {
            this.meta = new TreeMap<>();
            this.footer = new ArrayList<>();
        }
        
        public Builder type(final String type) {
            this.type = type;
            return this;
        }
        
        public Builder name(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder meta(final String name, final Object value) {
            this.meta.put(name, value);
            return this;
        }
        
        public Builder meta(final Map<String, Object> metas) {
            this.meta.putAll((Map)metas);
            return this;
        }
        
        public Builder took(final long took) {
            this.took = took;
            return this;
        }
        
        public String build() {
            if (this.type == null) {
                throw new IllegalArgumentException("type cannot be null");
            }
            final StringBuilder metaBuilder = new StringBuilder();
            metaBuilder.append(this.type);
            if (this.name == null) {
                throw new IllegalArgumentException("name cannot be null");
            }
            metaBuilder.append(": ");
            metaBuilder.append(this.name);
            if (!this.meta.isEmpty()) {
                metaBuilder.append(" { ");
                metaBuilder.append(this.meta.entrySet().stream().map(entry -> {
                    Object rendered = entry.getValue();
                    if (rendered instanceof String) {
                        rendered = "'" + rendered + "'";
                    }
                    return entry.getKey() + " = " + rendered;
                }).collect(Collectors.joining((CharSequence)", ")));
                metaBuilder.append(" }");
            }
            if (this.took != null) {
                metaBuilder.append(" [");
                metaBuilder.append((Object)this.took);
                metaBuilder.append(" ms]");
            }
            if (!this.footer.isEmpty()) {
                for (final String line : this.footer) {
                    metaBuilder.append("\n");
                    metaBuilder.append(line);
                }
            }
            return metaBuilder.toString();
        }
        
        public Builder footer(final String line) {
            this.footer.add(line);
            return this;
        }
    }
}
