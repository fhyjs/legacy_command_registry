package org.eu.hanana.reimu.mc.lcr.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;

public class S2CHandlerSetCommandField {
    public static void receive(S2CPayloadSendSetCommandField payload, NetworkManager.PacketContext context) {
        EditBox editBox = null;
        if (Minecraft.getInstance().screen != null&&Minecraft.getInstance().screen instanceof ChatScreen) {
            for (GuiEventListener child : Minecraft.getInstance().screen.children()) {
                if (child instanceof EditBox){
                    editBox= (EditBox) child;
                }
            }
        }
        if (editBox!=null){
            if (editBox.getValue().startsWith("/"))
                editBox.setValue("/"+payload.string());
            else
                editBox.setValue(payload.string());
        }
    }
}
