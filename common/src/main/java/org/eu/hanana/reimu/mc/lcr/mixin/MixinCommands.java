package org.eu.hanana.reimu.mc.lcr.mixin;

import com.google.gson.Gson;
import com.mojang.brigadier.ParseResults;
import dev.architectury.networking.NetworkManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.eu.hanana.reimu.mc.lcr.CommandManager;
import org.eu.hanana.reimu.mc.lcr.network.S2CPayloadSendKeyCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class MixinCommands {
    @Inject(method = {"performCommand"}, at = @At("HEAD"), cancellable = true)
    public void performCommand(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci){
        CommandManager.getCommandManager().performCommand(parseResults,command,ci);
    }
    @Inject(method = {"sendCommands"}, at = @At("HEAD"))
    public void sendCommands(ServerPlayer player, CallbackInfo ci){
        String json = new Gson().toJson(CommandManager.getCommandManager().getAllKeyCommands());
        NetworkManager.sendToPlayer(player,new S2CPayloadSendKeyCommands(json));
    }
}
