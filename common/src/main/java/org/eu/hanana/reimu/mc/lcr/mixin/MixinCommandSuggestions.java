package org.eu.hanana.reimu.mc.lcr.mixin;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import org.eu.hanana.reimu.mc.lcr.command.client.ClientCommandList;
import org.eu.hanana.reimu.mc.lcr.network.C2SGetSuggestionPayload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandSuggestions.class)
public class MixinCommandSuggestions {
    @Final
    @Shadow
    EditBox input;
    @Shadow @Final private Font font;

    @Inject(
            at = {@At("HEAD")},
            method = {"showSuggestions"},
            cancellable = true
    )
    public void showSuggestions(boolean narrateFirstSuggestion, CallbackInfo ci) {
        for (String commandKey : ClientCommandList.commandKeys) {
            if (input.getValue().startsWith("/"+commandKey)) {
                ci.cancel();
            }
        }
    }
    @Inject( at = {@At("HEAD")},
            method = {"keyPressed"},
            cancellable = true
    )
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        for (String commandKey : ClientCommandList.commandKeys) {
            if (input.getValue().startsWith("/"+commandKey)) {
                if (keyCode == 258) {
                    NetworkManager.sendToServer(new C2SGetSuggestionPayload(input.getValue().substring(1)));
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
    }
    @Inject(at = @At("HEAD"),method = {"render"},cancellable = true)
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        for (String commandKey : ClientCommandList.commandKeys) {
            if (input.getValue().startsWith("/"+commandKey)) {
                ci.cancel();
            }
        }
    }
}
