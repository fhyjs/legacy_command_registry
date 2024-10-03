package org.eu.hanana.reimu.mc.lcr;

import com.mojang.brigadier.ParseResults;
import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import org.eu.hanana.reimu.mc.lcr.command.CommandBase;
import org.eu.hanana.reimu.mc.lcr.events.LegacyCommandRegistrationEvent;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.executeCommandInContext;

public class CommandManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static CommandManager commandManager;
    private final List<CommandBase> commands = new ArrayList<>();
    public CommandManager(){
        commandManager=this;
    }
    public static CommandManager getCommandManager() {
        return commandManager;
    }
    public void init(){
        LegacyCommandRegistrationEvent.EVENT.invoker().register(this);
    }
    public void register(CommandBase commandBase){
        commands.add(commandBase);
    }
    public boolean hasRootCommand(String rootCommand){
        for (CommandBase command : commands) {
            if (command.getCommand().equals(rootCommand)){
                return true;
            }
        }
        return false;
    }
    public List<String> getAllKeyCommands(){
        List<String> strings = new ArrayList<>();
        for (CommandBase command : commands) {
            strings.add(command.getCommand());
        }
        return strings;
    }
    public boolean hasCommand(String commandStr){
        for (CommandBase command : commands) {
            if (commandStr.startsWith(command.getCommand())){
                return true;
            }
        }
        return false;
    }
    public CommandBase getCommandByCommand(String commandStr){
        for (CommandBase command : commands) {
            if (commandStr.startsWith(command.getCommand())){
                return command;
            }
        }
        return null;
    }
    public void performCommand(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci) {
        if (hasCommand(command)){
            ci.cancel();
            CommandSourceStack commandSourceStack = parseResults.getContext().getSource();
            commandSourceStack.getServer().getProfiler().push(() -> "/" + command);
            var commandBase = getCommandByCommand(command);
            try {
                if (commandBase != null) {
                    executeCommandInContext(commandSourceStack, (executionContext) -> {
                        commandBase.execute(commandSourceStack,command);
                    });
                }
            } catch (Exception var12) {
                Exception exception = var12;
                MutableComponent mutableComponent = Component.literal(exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.error("Command exception: /{}", command, exception);
                    StackTraceElement[] stackTraceElements = exception.getStackTrace();

                    for(int i = 0; i < Math.min(stackTraceElements.length, 3); ++i) {
                        mutableComponent.append("\n\n").append(stackTraceElements[i].getMethodName()).append("\n ").append(stackTraceElements[i].getFileName()).append(":").append(String.valueOf(stackTraceElements[i].getLineNumber()));
                    }
                }

                commandSourceStack.sendFailure(Component.translatable("command.failed").withStyle((style) -> {
                    return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, mutableComponent));
                }));
                if (SharedConstants.IS_RUNNING_IN_IDE) {
                    commandSourceStack.sendFailure(Component.literal(Util.describeError(exception)));
                    LOGGER.error("'/{}' threw an exception", command, exception);
                }
            } finally {
                commandSourceStack.getServer().getProfiler().pop();
            }
        }
    }
}