package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.chain;

import java.util.function.Supplier;
import java.time.Duration;

class ChainTask
{
    protected final Runnable runnable;
    protected final Duration delay;
    protected final Supplier<Boolean> async;
    protected final boolean exceptionHandler;
    
    public ChainTask(final Runnable runnable, final Duration delay, final Supplier<Boolean> async, final boolean exceptionHandler) {
        this.runnable = runnable;
        this.delay = delay;
        this.async = async;
        this.exceptionHandler = exceptionHandler;
    }
    
    public Runnable getRunnable() {
        return this.runnable;
    }
    
    public Duration getDelay() {
        return this.delay;
    }
    
    public Supplier<Boolean> getAsync() {
        return this.async;
    }
    
    public boolean isExceptionHandler() {
        return this.exceptionHandler;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChainTask)) {
            return false;
        }
        final ChainTask other = (ChainTask)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isExceptionHandler() != other.isExceptionHandler()) {
            return false;
        }
        final Object this$runnable = this.getRunnable();
        final Object other$runnable = other.getRunnable();
        Label_0078: {
            if (this$runnable == null) {
                if (other$runnable == null) {
                    break Label_0078;
                }
            }
            else if (this$runnable.equals(other$runnable)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$delay = this.getDelay();
        final Object other$delay = other.getDelay();
        Label_0115: {
            if (this$delay == null) {
                if (other$delay == null) {
                    break Label_0115;
                }
            }
            else if (this$delay.equals(other$delay)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$async = this.getAsync();
        final Object other$async = other.getAsync();
        if (this$async == null) {
            if (other$async == null) {
                return true;
            }
        }
        else if (this$async.equals(other$async)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ChainTask;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isExceptionHandler() ? 79 : 97);
        final Object $runnable = this.getRunnable();
        result = result * 59 + (($runnable == null) ? 43 : $runnable.hashCode());
        final Object $delay = this.getDelay();
        result = result * 59 + (($delay == null) ? 43 : $delay.hashCode());
        final Object $async = this.getAsync();
        result = result * 59 + (($async == null) ? 43 : $async.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ChainTask(runnable=" + (Object)this.getRunnable() + ", delay=" + (Object)this.getDelay() + ", async=" + (Object)this.getAsync() + ", exceptionHandler=" + this.isExceptionHandler() + ")";
    }
    
    public void run() {
        this.getRunnable().run();
    }
}
