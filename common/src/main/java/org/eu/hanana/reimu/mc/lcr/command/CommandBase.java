package org.eu.hanana.reimu.mc.lcr.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.eu.hanana.reimu.mc.lcr.CommandManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public String[] getAllItems(){
        final List<String> listName = new ArrayList<>();
        HolderLookup.RegistryLookup<Item> itemRegistryLookup = CommandManager.getCommandManager().getCommandBuildContext().lookup(BuiltInRegistries.ITEM.key()).get();
        List<Holder.Reference<Item>> list = itemRegistryLookup.listElements().toList();
        for (Holder.Reference<Item> itemReference : list) {
            listName.add(itemReference.getRegisteredName());
        }
        return listName.toArray(new String[0]);
    }
    public List<? extends Entity> getEntityBySelector(String selector,CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return new EntitySelectorParser(new StringReader(selector)).parse().findEntities(commandSourceStack);
    }
    public Entity getSingleEntityBySelector(String selector,CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return new EntitySelectorParser(new StringReader(selector)).parse().findSingleEntity(commandSourceStack);
    }
    public ServerPlayer getSinglePlayerBySelector(String selector, CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return new EntitySelectorParser(new StringReader(selector)).parse().findSinglePlayer(commandSourceStack);
    }
    public List<ServerPlayer> getPlayerBySelector(String selector, CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return new EntitySelectorParser(new StringReader(selector)).parse().findPlayers(commandSourceStack);
    }
    public ItemInput getItemInputBySelector(String selector) throws CommandSyntaxException {
        return new ItemArgument(CommandManager.getCommandManager().getCommandBuildContext()).parse(new StringReader(selector));
    }
    public BlockPos getBlockPos(String pos,CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return BlockPosArgument.blockPos().parse(new StringReader(pos)).getBlockPos(commandSourceStack);
    }
    public Vec3 getVec3(String pos, CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return Vec3Argument.vec3().parse(new StringReader(pos)).getPosition(commandSourceStack);
    }
    public Vec2 getVec2(String pos, CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return Vec2Argument.vec2().parse(new StringReader(pos)).getRotation(commandSourceStack);
    }
    public String toVec3Str(String x,String y,String z){
        return to1Arg(x,y,z);
    }
    public String toVec2Str(String a,String b){
        return to1Arg(a,b);
    }
    public String to1Arg(String... str){
        var sb = new StringBuilder();
        for (String s : str) {
            sb.append(s).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
