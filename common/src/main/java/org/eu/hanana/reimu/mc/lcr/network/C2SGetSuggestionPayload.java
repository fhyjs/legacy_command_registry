package org.eu.hanana.reimu.mc.lcr.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.eu.hanana.reimu.mc.lcr.LCRMod;

public record C2SGetSuggestionPayload(String string) implements CustomPacketPayload {

    public static final Type<C2SGetSuggestionPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LCRMod.MOD_ID, "c2s_get_suggestion"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, C2SGetSuggestionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            C2SGetSuggestionPayload::string,
            C2SGetSuggestionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
