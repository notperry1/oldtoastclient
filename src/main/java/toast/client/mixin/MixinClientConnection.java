package toast.client.mixin;

import toast.client.ToastClient;
import toast.client.command.CommandManager;
import toast.client.event.events.EventReadPacket;
import toast.client.event.events.EventSendPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
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
                ToastClient.eventBus.post(event);
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
        ToastClient.eventBus.post(event);

        if (event.isCancelled()) callback.cancel();
    }
}
