package toast.client.mixin;

import toast.client.BleachHack;
import toast.client.command.CommandManager;
import toast.client.event.events.EventReadPacket;
import toast.client.event.events.EventSendPacket;
import toast.client.module.ModuleManager;
import toast.client.module.mods.AntiChunkBan;
import toast.client.utils.BleachLogger;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketEncoderException;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    @Shadow
    private Channel channel;

    @Shadow
    private void sendImmediately(Packet<?> packet_1, GenericFutureListener<? extends Future<? super Void>> genericFutureListener_1) {}

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext_1, Packet<?> packet_1, CallbackInfo callback) {
        if (this.channel.isOpen() && packet_1 != null) {
        	try {
                EventReadPacket event = new EventReadPacket(packet_1);
                BleachHack.eventBus.post(event);
                if (event.isCancelled()) callback.cancel();
            } catch (Exception exception) {}
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    public void send(Packet<?> packet_1, GenericFutureListener<? extends Future<? super Void>> genericFutureListener_1, CallbackInfo callback) {
    	if(packet_1 instanceof ChatMessageC2SPacket) {
			ChatMessageC2SPacket pack = (ChatMessageC2SPacket) packet_1;
			if(pack.getChatMessage().startsWith(CommandManager.prefix)) {
	    		CommandManager.callCommand(pack.getChatMessage().substring(CommandManager.prefix.length()));
	    		callback.cancel();
			}
		}
    	
    	EventSendPacket event = new EventSendPacket(packet_1);
        BleachHack.eventBus.post(event);

        if (event.isCancelled()) callback.cancel();
    }
    
    // Packet kick blocc 
    @Inject(method = "exceptionCaught(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Throwable;)V", at = @At("HEAD"), cancellable = true)
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext_1, Throwable throwable_1, CallbackInfo callback) {
    	if(!ModuleManager.getModule(AntiChunkBan.class).isToggled()) return;
    	
    	if(!(throwable_1 instanceof PacketEncoderException)) {
    		BleachLogger.warningMessage("Canceled Defect Packet: " + throwable_1);
        	callback.cancel();
    	}
    }
}
