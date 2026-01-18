package cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor;

import java.util.function.Predicate;
import java.util.Collections;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Arrays;
import lombok.NonNull;
import java.io.InputStream;

public class ConfigPostprocessor
{
    private String context;
    
    public static ConfigPostprocessor of(@NonNull final InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is marked non-null but is null");
        }
        final ConfigPostprocessor postprocessor = new ConfigPostprocessor();
        postprocessor.setContext(readInput(inputStream));
        return postprocessor;
    }
    
    public static ConfigPostprocessor of(@NonNull final String context) {
        if (context == null) {
            throw new NullPointerException("context is marked non-null but is null");
        }
        final ConfigPostprocessor postprocessor = new ConfigPostprocessor();
        postprocessor.setContext(context);
        return postprocessor;
    }
    
    public static int countIndent(@NonNull final String line) {
        if (line == null) {
            throw new NullPointerException("line is marked non-null but is null");
        }
        int whitespaces = 0;
        for (final char c : line.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                return whitespaces;
            }
            ++whitespaces;
        }
        return whitespaces;
    }
    
    public static String addIndent(@NonNull final String line, final int size) {
        if (line == null) {
            throw new NullPointerException("line is marked non-null but is null");
        }
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            buf.append(" ");
        }
        final String indent = buf.toString();
        return (String)Arrays.stream((Object[])line.split("\n")).map(part -> indent + part).collect(Collectors.joining((CharSequence)"\n")) + "\n";
    }
    
    public static String createCommentOrEmpty(final String commentPrefix, final String[] strings) {
        return (strings == null) ? "" : createComment(commentPrefix, strings);
    }
    
    public static String createComment(String commentPrefix, final String[] strings) {
        if (strings == null) {
            return null;
        }
        if (commentPrefix == null) {
            commentPrefix = "";
        }
        final List<String> lines = (List<String>)new ArrayList();
        for (final String line : strings) {
            final String[] parts = line.split("\n");
            final String prefix = line.startsWith(commentPrefix.trim()) ? "" : commentPrefix;
            lines.add((Object)((line.isEmpty() ? "" : prefix) + line));
        }
        return String.join((CharSequence)"\n", (Iterable)lines) + "\n";
    }
    
    private static String readInput(final InputStream inputStream) {
        return (String)new BufferedReader((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining((CharSequence)"\n"));
    }
    
    private static void writeOutput(final OutputStream outputStream, final String text) {
        try {
            final PrintStream out = new PrintStream(outputStream, true, StandardCharsets.UTF_8.name());
            try {
                out.print(text);
            }
            finally {
                if (Collections.singletonList((Object)out).get(0) != null) {
                    out.close();
                }
            }
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    public ConfigPostprocessor write(@NonNull final OutputStream outputStream) {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is marked non-null but is null");
        }
        writeOutput(outputStream, this.context);
        return this;
    }
    
    public ConfigPostprocessor removeLines(@NonNull final ConfigLineFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        final String[] lines = this.context.split("\n");
        final StringBuilder buf = new StringBuilder();
        for (final String line : lines) {
            if (!filter.remove(line)) {
                buf.append(line).append("\n");
            }
        }
        this.context = buf.toString();
        return this;
    }
    
    public ConfigPostprocessor removeLinesUntil(@NonNull final Predicate<String> shouldStop) {
        if (shouldStop == null) {
            throw new NullPointerException("shouldStop is marked non-null but is null");
        }
        final String[] lines = this.context.split("\n");
        for (int i = 0; i < lines.length; ++i) {
            final String line = lines[i];
            if (shouldStop.test((Object)line)) {
                final String[] remaining = (String[])Arrays.copyOfRange((Object[])lines, i, lines.length);
                this.context = String.join((CharSequence)"\n", (CharSequence[])remaining);
                break;
            }
        }
        return this;
    }
    
    public ConfigPostprocessor updateLines(@NonNull final ConfigContextManipulator manipulator) {
        if (manipulator == null) {
            throw new NullPointerException("manipulator is marked non-null but is null");
        }
        final String[] lines = this.context.split("\n");
        final StringBuilder buf = new StringBuilder();
        for (final String line : lines) {
            buf.append(manipulator.convert(line)).append("\n");
        }
        this.context = buf.toString();
        return this;
    }
    
    public ConfigPostprocessor updateLinesKeys(@NonNull final ConfigSectionWalker walker) {
        if (walker == null) {
            throw new NullPointerException("walker is marked non-null but is null");
        }
        try {
            return this.updateLinesKeys0(walker);
        }
        catch (final Exception exception) {
            throw new RuntimeException("failed to #updateLinesKeys for context:\n" + this.context, (Throwable)exception);
        }
    }
    
    private ConfigPostprocessor updateLinesKeys0(@NonNull final ConfigSectionWalker walker) {
        if (walker == null) {
            throw new NullPointerException("walker is marked non-null but is null");
        }
        final String[] lines = this.context.split("\n");
        List<ConfigLineInfo> currentPath = (List<ConfigLineInfo>)new ArrayList();
        int lastIndent = 0;
        int level = 0;
        final StringBuilder newContext = new StringBuilder();
        boolean multilineSkip = false;
        for (final String line : lines) {
            final int indent = countIndent(line);
            final int change = indent - lastIndent;
            final String key = walker.readName(line);
            if (!walker.isKey(line)) {
                newContext.append(line).append("\n");
                multilineSkip = false;
            }
            else {
                if (currentPath.isEmpty()) {
                    currentPath.add((Object)ConfigLineInfo.of(indent, change, key));
                }
                if (change > 0) {
                    if (!multilineSkip) {
                        ++level;
                        currentPath.add((Object)ConfigLineInfo.of(indent, change, key));
                    }
                }
                else {
                    if (change != 0) {
                        final ConfigLineInfo lastLineInfo = (ConfigLineInfo)currentPath.get(currentPath.size() - 1);
                        final int step = lastLineInfo.getIndent() / level;
                        level -= change * -1 / step;
                        currentPath = (List<ConfigLineInfo>)currentPath.subList(0, level + 1);
                        multilineSkip = false;
                    }
                    if (!multilineSkip) {
                        currentPath.set(currentPath.size() - 1, (Object)ConfigLineInfo.of(indent, change, key));
                    }
                }
                if (multilineSkip) {
                    newContext.append(line).append("\n");
                }
                else {
                    if (walker.isKeyMultilineStart(line)) {
                        multilineSkip = true;
                    }
                    lastIndent = indent;
                    final String updatedLine = walker.update(line, (ConfigLineInfo)currentPath.get(currentPath.size() - 1), currentPath);
                    newContext.append(updatedLine).append("\n");
                }
            }
        }
        this.context = newContext.toString();
        return this;
    }
    
    public ConfigPostprocessor updateContext(@NonNull final ConfigContextManipulator manipulator) {
        if (manipulator == null) {
            throw new NullPointerException("manipulator is marked non-null but is null");
        }
        this.context = manipulator.convert(this.context);
        return this;
    }
    
    public ConfigPostprocessor prependContextComment(final String prefix, final String[] strings) {
        if (strings != null) {
            this.context = createComment(prefix, strings) + this.context;
        }
        return this;
    }
    
    public ConfigPostprocessor appendContextComment(final String prefix, final String[] strings) {
        return this.appendContextComment(prefix, "", strings);
    }
    
    public ConfigPostprocessor appendContextComment(final String prefix, final String separator, final String[] strings) {
        if (strings != null) {
            this.context = this.context + separator + createComment(prefix, strings);
        }
        return this;
    }
    
    public String getContext() {
        return this.context;
    }
    
    public void setContext(final String context) {
        this.context = context;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConfigPostprocessor)) {
            return false;
        }
        final ConfigPostprocessor other = (ConfigPostprocessor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$context = this.getContext();
        final Object other$context = other.getContext();
        if (this$context == null) {
            if (other$context == null) {
                return true;
            }
        }
        else if (this$context.equals(other$context)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigPostprocessor;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $context = this.getContext();
        result = result * 59 + (($context == null) ? 43 : $context.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ConfigPostprocessor(context=" + this.getContext() + ")";
    }
}
