package org.eu.hanana.reimu.mc.lcr.commands;

import dev.architectury.utils.GameInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.eu.hanana.reimu.mc.lcr.command.CommandBase;

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
    public int execute(CommandSourceStack commandSourceStack, String rawCommand) throws Exception {
        var args = this.parseCommand(rawCommand);
        commandSourceStack.sendSuccess(()-> Component.literal("Testing LCR Mod! Command input: "+rawCommand),true);
        commandSourceStack.sendSuccess(()-> Component.literal("with "+args.length+" arg(s)"),true);
        if (args.length>1){
            if (args[1].equals("fail")){
                commandSourceStack.sendFailure(Component.literal(args[2]));
            }else if (args[1].equals("success")){
                commandSourceStack.sendSuccess(()->Component.literal(args[2]),true);
            }else if (args[1].equals("execute")){
                GameInstance.getServer().getCommands().performPrefixedCommand(commandSourceStack,args[2]);
            }else if (args[1].equals("return")){
                return Integer.parseInt(args[2]);
            }
        }else {
            throw new Exception("Incomplete command!");
        }
        return 0;
    }

    @Override
    public String getSuggestion(String string, Player player) {
        var args = this.parseCommand(string);
        if (args.length==1){
            return string+" ";
        }
        if (args.length==2){
            return cycleTabSuggestion(player,args,new String[]{"fail","success","execute","return"},true);
        }
        return super.getSuggestion(string, player);
    }
}
