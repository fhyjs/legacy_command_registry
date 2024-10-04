package org.eu.hanana.reimu.mc.lcr.mixin;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.commands.CommandSourceStack;
import org.eu.hanana.reimu.mc.lcr.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandDispatcher.class)
public class MixinCommandDispatcher {
    //@Inject(at = @At("HEAD"),cancellable = true,method = {"execute*"})
    public void execute(String input, Object source, CallbackInfoReturnable<Integer> cir) throws Exception {
        CommandManager commandManager = CommandManager.getCommandManager();
        if (commandManager!=null){
            if (commandManager.hasCommand(input)&&source instanceof CommandSourceStack commandSourceStack) {
                cir.setReturnValue(commandManager.getCommandByCommand(input).execute(commandSourceStack,input));
                cir.cancel();
            }
        }
    }
}
