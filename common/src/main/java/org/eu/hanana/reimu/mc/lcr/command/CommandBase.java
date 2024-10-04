package org.eu.hanana.reimu.mc.lcr.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CommandBase {
    public abstract int getPermissionLevel();
    public abstract String getCommand();
    public boolean hasPermission(CommandSourceStack commandSourceStack) {
        return commandSourceStack.hasPermission(this.getPermissionLevel());
    }
    public abstract int execute(CommandSourceStack commandSourceStack, String rawCommand) throws Exception;

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
        if (command.endsWith(" "))
            result.add("");
        return result.toArray(new String[0]);
    }
    public String cycleTabSuggestion(Player player,String[] parsedStr, String[] values,boolean show){
        parsedStr = parsedStr.clone();
        var length = parsedStr.length;
        var lastWord = parsedStr[length-1];
        if (show){
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : values) {
                stringBuilder.append(value).append(" ");
            }
            player.sendSystemMessage(Component.literal(stringBuilder.toString()));
        }
        List<String> suggestions = new ArrayList<>();
        for (String value : values) {
            if (lastWord.equals(value)) {
                suggestions.addAll(List.of(values));
                break;
            }
        }
        for (String value : values) {
            if (!suggestions.isEmpty()) break;
            if (value.startsWith(lastWord)){
                suggestions.add(value);
            }
        }
        var pos = suggestions.indexOf(lastWord);
        pos++;
        if (pos>suggestions.size()-1) pos=0;
        StringBuilder stringBuilder = new StringBuilder();
        if (!suggestions.isEmpty()) {
            parsedStr[length - 1] = suggestions.get(pos);
        }
        for (String s : parsedStr) {
            stringBuilder.append(s).append(" ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}
