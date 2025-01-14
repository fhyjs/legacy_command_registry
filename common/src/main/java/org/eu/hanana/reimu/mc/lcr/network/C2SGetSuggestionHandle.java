package org.eu.hanana.reimu.mc.lcr.network;

import com.google.gson.Gson;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import org.eu.hanana.reimu.mc.lcr.CommandManager;
import org.eu.hanana.reimu.mc.lcr.command.client.ClientCommandList;

import java.util.List;

public class C2SGetSuggestionHandle {
    public static void receive(C2SGetSuggestionPayload payload, NetworkManager.PacketContext context) {
        var cmd = CommandManager.getCommandManager().getCommandByCommand(payload.string());
        if (cmd!=null)
            NetworkManager.sendToPlayer(((ServerPlayer) context.getPlayer()),new S2CPayloadSendSetCommandField(cmd.getSuggestion(payload.string(),context.getPlayer())));
    }
}
