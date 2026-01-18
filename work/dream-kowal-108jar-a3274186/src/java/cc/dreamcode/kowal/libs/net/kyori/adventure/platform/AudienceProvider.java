package cc.dreamcode.kowal.libs.net.kyori.adventure.platform;

import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointered;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer.ComponentRenderer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;

public interface AudienceProvider extends AutoCloseable
{
    @NotNull
    Audience all();
    
    @NotNull
    Audience console();
    
    @NotNull
    Audience players();
    
    @NotNull
    Audience player(@NotNull final UUID playerId);
    
    @NotNull
    default Audience permission(@NotNull final Key permission) {
        return this.permission(permission.namespace() + '.' + permission.value());
    }
    
    @NotNull
    Audience permission(@NotNull final String permission);
    
    @NotNull
    Audience world(@NotNull final Key world);
    
    @NotNull
    Audience server(@NotNull final String serverName);
    
    @NotNull
    ComponentFlattener flattener();
    
    void close();
    
    public interface Builder<P extends AudienceProvider, B extends Builder<P, B>>
    {
        @NotNull
        B componentRenderer(@NotNull final ComponentRenderer<Pointered> componentRenderer);
        
        @NotNull
        B partition(@NotNull final Function<Pointered, ?> partitionFunction);
        
        @NotNull
        default <T> B componentRenderer(@NotNull final Function<Pointered, T> partition, @NotNull final ComponentRenderer<T> componentRenderer) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1         /* partition */
            //     2: invokeinterface cc/dreamcode/kowal/libs/net/kyori/adventure/platform/AudienceProvider$Builder.partition:(Ljava/util/function/Function;)Lcc/dreamcode/kowal/libs/net/kyori/adventure/platform/AudienceProvider$Builder;
            //     7: aload_2         /* componentRenderer */
            //     8: aload_1         /* partition */
            //     9: invokeinterface cc/dreamcode/kowal/libs/net/kyori/adventure/text/renderer/ComponentRenderer.mapContext:(Ljava/util/function/Function;)Lcc/dreamcode/kowal/libs/net/kyori/adventure/text/renderer/ComponentRenderer;
            //    14: invokeinterface cc/dreamcode/kowal/libs/net/kyori/adventure/platform/AudienceProvider$Builder.componentRenderer:(Lcc/dreamcode/kowal/libs/net/kyori/adventure/text/renderer/ComponentRenderer;)Lcc/dreamcode/kowal/libs/net/kyori/adventure/platform/AudienceProvider$Builder;
            //    19: areturn        
            //    Signature:
            //  <T:Ljava/lang/Object;>(Ljava/util/function/Function<Lcc/dreamcode/kowal/libs/net/kyori/adventure/pointer/Pointered;TT;>;Lcc/dreamcode/kowal/libs/net/kyori/adventure/text/renderer/ComponentRenderer<TT;>;)TB;
            //    RuntimeInvisibleTypeAnnotations: 00 03 14 00 00 0D 00 00 16 00 00 00 0D 00 00 16 01 00 00 0D 00 00
            //    MethodParameters:
            //  Name               Flags  
            //  -----------------  -----
            //  partition          FINAL
            //  componentRenderer  FINAL
            // 
            // The error that occurred was:
            // 
            // java.lang.UnsupportedOperationException: The requested operation is not supported.
            //     at i6.a.b(SourceFile:5)
            //     at m5.v1.Q(SourceFile:24)
            //     at m5.v1.Q(SourceFile:13)
            //     at m5.v1.i0(SourceFile:35)
            //     at m5.x1.x(SourceFile:51)
            //     at m5.x1.f(SourceFile:3)
            //     at m5.m1.A(SourceFile:1)
            //     at m5.x1.o(SourceFile:8)
            //     at m5.x1.v(SourceFile:42)
            //     at w5.b0.s(SourceFile:643)
            //     at w5.b0.k(SourceFile:313)
            //     at w5.b0.y(SourceFile:35)
            //     at w5.b0.x(SourceFile:2)
            //     at w5.b0.k(SourceFile:121)
            //     at w5.b0.y(SourceFile:35)
            //     at w5.b0.M(SourceFile:166)
            //     at w5.b0.L(SourceFile:2)
            //     at w5.b0.K(SourceFile:76)
            //     at w5.b0.J(SourceFile:72)
            //     at w5.f.r(SourceFile:846)
            //     at w5.f.q(SourceFile:3)
            //     at a6.j.j(SourceFile:32)
            //     at a6.j.i(SourceFile:28)
            //     at a6.i.n(SourceFile:7)
            //     at a6.i.m(SourceFile:174)
            //     at a6.i.c(SourceFile:67)
            //     at a6.i.r(SourceFile:328)
            //     at a6.i.s(SourceFile:17)
            //     at a6.i.c(SourceFile:162)
            //     at a6.i.r(SourceFile:328)
            //     at a6.i.s(SourceFile:17)
            //     at a6.i.q(SourceFile:29)
            //     at a6.i.b(SourceFile:33)
            //     at y5.d.e(SourceFile:6)
            //     at y5.d.b(SourceFile:1)
            //     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithProcyon(SourceFile:306)
            //     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:131)
            //     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
            //     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
            //     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
            //     at e7.a.run(SourceFile:1)
            //     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
            //     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
            //     at java.lang.Thread.run(Thread.java:1563)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @NotNull
        P build();
    }
}
