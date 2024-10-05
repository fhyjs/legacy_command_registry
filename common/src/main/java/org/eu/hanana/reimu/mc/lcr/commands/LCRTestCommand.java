package org.eu.hanana.reimu.mc.lcr.commands;

import dev.architectury.utils.GameInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.eu.hanana.reimu.mc.lcr.command.CommandBase;

import java.util.List;

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
            }else if (args[1].equals("selector")){
                List<? extends Entity> entityBySelector = this.getEntityBySelector(args[2], commandSourceStack);
                for (Entity entity : entityBySelector) {
                    commandSourceStack.sendSuccess(entity::getName,true);
                }
                return entityBySelector.size();
            }else if (args[1].equals("item")){
                ItemInput itemInputBySelector = getItemInputBySelector(args[2]);
                commandSourceStack.sendSuccess(()-> Component.literal(itemInputBySelector.getItem().toString()),true);
                return 0;
            } else if (args[1].equals("block_pos_1_arg")){
                BlockPos blockPos = getBlockPos(args[2], commandSourceStack);
                BlockState blockState = commandSourceStack.getLevel().getBlockState(blockPos);
                commandSourceStack.sendSystemMessage(Component.literal(blockState.toString()));
                return 0;
            } else if (args[1].equals("block_pos_3_arg")){
                BlockPos blockPos =  getBlockPos(toVec3Str(args[2],args[3],args[4]), commandSourceStack);
                BlockState blockState = commandSourceStack.getLevel().getBlockState(blockPos);
                commandSourceStack.sendSystemMessage(Component.literal(blockState.toString()));
                return 0;
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
            return cycleTabSuggestion(player,args,new String[]{"fail","success","execute","return","selector","item","block_pos_1_arg","block_pos_3_arg"},true);
        }
        if (args.length==3){
            if (args[1].equals("item")){
                return cycleTabSuggestion(player,args,getAllItems(),true);
            } else if (args[1].equals("selector")){
                return cycleTabSuggestion(player,args,selectorSuggestions(),true);
            }
        }
        return super.getSuggestion(string, player);
    }
}
