package org.eu.hanana.reimu.mc.lcr.commands;

import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.eu.hanana.reimu.mc.lcr.command.CommandBase;
import org.eu.hanana.reimu.mc.lcr.network.S2CPayloadSendKeyCommands;

public class LCRTestCommand extends CommandBase {
    @Override
    public int getPermissionLevel() {
        return 1;
    }

    @Override
    public String getCommand() {
        return "test_lcr";
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String rawCommand) {
        var args = this.parseCommand(rawCommand);
        commandSourceStack.sendSuccess(()-> Component.literal("Testing LCR Mod! Command input: "+rawCommand),true);
        commandSourceStack.sendSuccess(()-> Component.literal("with"+args.length+"args"),true);
        if (args.length>1){
            if (args[1].equals("fail")){
                commandSourceStack.sendFailure(Component.literal(args[2]));
            }
        }
    }

    @Override
    public String getSuggestion(String string, Player player) {
        return super.getSuggestion(string, player);
    }
}
