package org.eu.hanana.reimu.mc.lcr.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CommandBase {
    public abstract int getPermissionLevel();
    public abstract String getCommand();
    public boolean hasPermission(CommandSourceStack commandSourceStack) {
        return commandSourceStack.hasPermission(this.getPermissionLevel());
    }
    public abstract void execute(CommandSourceStack commandSourceStack, String rawCommand);

    public String getSuggestion(String string, Player player) {
        player.sendSystemMessage(Component.literal("No Suggestions!"));
        return string;
    }
    public String[] parseCommand(String command){
        // 正则表达式：匹配成对的双引号内容，或没有引号的部分，或者不配对的引号部分
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\"[^\"]*$)|(\\S+)");
        Matcher matcher = pattern.matcher(command);

        ArrayList<String> result = new ArrayList<>();

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // 如果是成对的引号，去掉引号，保留内容
                result.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // 如果是不配对的引号，保留整个内容，包括引号
                result.add(matcher.group(2));
            } else if (matcher.group(3) != null) {
                // 普通文本（不带引号）
                result.add(matcher.group(3));
            }
        }

        return result.toArray(new String[0]);
    }
}
