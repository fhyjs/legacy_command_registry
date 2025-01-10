package org.eu.hanana.reimu.mc.lcr.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.eu.hanana.reimu.mc.lcr.LCRMod;

public record S2CPayloadSendSetCommandField(String string) implements CustomPacketPayload {

    public static final Type<S2CPayloadSendSetCommandField> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LCRMod.MOD_ID, "s2c_send_set_commands"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, S2CPayloadSendSetCommandField> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            S2CPayloadSendSetCommandField::string,
            S2CPayloadSendSetCommandField::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
