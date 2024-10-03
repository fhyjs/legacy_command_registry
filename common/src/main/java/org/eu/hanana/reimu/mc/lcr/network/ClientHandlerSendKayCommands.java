package org.eu.hanana.reimu.mc.lcr.network;

import com.google.gson.Gson;
import dev.architectury.networking.NetworkManager;
import org.eu.hanana.reimu.mc.lcr.command.client.ClientCommandList;

import java.util.List;

public class ClientHandlerSendKayCommands {
    public static void receive(S2CPayloadSendKeyCommands payload, NetworkManager.PacketContext context) {
        ClientCommandList.commandKeys.clear();
        ClientCommandList.commandKeys.addAll(List.of(new Gson().fromJson(payload.string(), String[].class)));
    }
}
