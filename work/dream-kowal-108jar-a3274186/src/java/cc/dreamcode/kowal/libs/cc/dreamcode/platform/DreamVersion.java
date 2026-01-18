package cc.dreamcode.kowal.libs.cc.dreamcode.platform;

import lombok.Generated;

public class DreamVersion
{
    private final String name;
    private final String version;
    private final String author;
    
    @Generated
    private DreamVersion(final String name, final String version, final String author) {
        this.name = name;
        this.version = version;
        this.author = author;
    }
    
    @Generated
    public static DreamVersion create(final String name, final String version, final String author) {
        return new DreamVersion(name, version, author);
    }
    
    @Generated
    public String getName() {
        return this.name;
    }
    
    @Generated
    public String getVersion() {
        return this.version;
    }
    
    @Generated
    public String getAuthor() {
        return this.author;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DreamVersion)) {
            return false;
        }
        final DreamVersion other = (DreamVersion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$version = this.getVersion();
        final Object other$version = other.getVersion();
        Label_0102: {
            if (this$version == null) {
                if (other$version == null) {
                    break Label_0102;
                }
            }
            else if (this$version.equals(other$version)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$author = this.getAuthor();
        final Object other$author = other.getAuthor();
        if (this$author == null) {
            if (other$author == null) {
                return true;
            }
        }
        else if (this$author.equals(other$author)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof DreamVersion;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $version = this.getVersion();
        result = result * 59 + (($version == null) ? 43 : $version.hashCode());
        final Object $author = this.getAuthor();
        result = result * 59 + (($author == null) ? 43 : $author.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "DreamVersion(name=" + this.getName() + ", version=" + this.getVersion() + ", author=" + this.getAuthor() + ")";
    }
}
