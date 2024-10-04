package org.eu.hanana.reimu.mc.lcr.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import org.eu.hanana.reimu.mc.lcr.CommandManager;
import org.eu.hanana.reimu.mc.lcr.command.CommandBase;

import java.util.concurrent.atomic.AtomicInteger;

public class LCRProxyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> base;
        base = Commands.literal("lcr").requires(commandSourceStack -> true);
        base.then(Commands.literal("proxy").then(Commands.argument("command", StringArgumentType.greedyString()).executes(new Command<CommandSourceStack>() {
            @Override
            public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandManager commandManager = CommandManager.getCommandManager();
                if (commandManager==null) return 0;
                var cmd= context.getArgument("command", String.class);
                if (commandManager.hasCommand(cmd)){
                    try {
                        CommandBase commandByCommand = commandManager.getCommandByCommand(cmd);
                        if (context.getSource().hasPermission(commandByCommand.getPermissionLevel())) {
                            return commandByCommand.execute(context.getSource(), cmd);
                        }else {
                            context.getSource().sendFailure(Component.literal("No permission to execute command "+ cmd));
                            return 0;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                context.getSource().sendFailure(Component.literal("No such as legacy command like "+ cmd));
                return 0;
            }
        })));
        dispatcher.register(base);
    }
}
