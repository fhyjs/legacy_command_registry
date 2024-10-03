package org.eu.hanana.reimu.mc.lcr.mixin;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.eu.hanana.reimu.mc.lcr.CommandManager;
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
}
