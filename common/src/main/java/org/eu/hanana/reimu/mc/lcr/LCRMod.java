package org.eu.hanana.reimu.mc.lcr;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.ArchitecturyConstants;
import dev.architectury.utils.Env;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.eu.hanana.reimu.mc.lcr.commands.LCRProxyCommand;
import org.eu.hanana.reimu.mc.lcr.commands.LCRTestCommand;
import org.eu.hanana.reimu.mc.lcr.events.LegacyCommandRegistrationEvent;
import org.eu.hanana.reimu.mc.lcr.network.*;

public final class LCRMod {
    public static final String MOD_ID = "legacy_command_registry";
    public static void init() {
        // Write common init code here.
        CommandRegistrationEvent.EVENT.register(new CommandRegistrationEvent() {
            @Override
            public void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
                new CommandManager(commandBuildContext).init();
                LCRProxyCommand.register(commandDispatcher);
            }
        });
        LegacyCommandRegistrationEvent.EVENT.register(new LegacyCommandRegistrationEvent() {
            @Override
            public void register(CommandManager commandManager) {
                commandManager.register(new LCRTestCommand());
            }
        });
        PlayerEvent.PLAYER_JOIN.register(new PlayerEvent.PlayerJoin() {
            @Override
            public void join(ServerPlayer player) {
                String json = new Gson().toJson(CommandManager.getCommandManager().getAllKeyCommands());
                NetworkManager.sendToPlayer(player,new S2CPayloadSendKeyCommands(json));
            }
        });
        NetworkManager.registerReceiver(NetworkManager.c2s(), C2SGetSuggestionPayload.TYPE,C2SGetSuggestionPayload.STREAM_CODEC, C2SGetSuggestionHandle::receive);
        if (Platform.getEnvironment()== Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.s2c(), S2CPayloadSendKeyCommands.TYPE, S2CPayloadSendKeyCommands.STREAM_CODEC, ClientHandlerSendKayCommands::receive);
            NetworkManager.registerReceiver(NetworkManager.s2c(), S2CPayloadSendSetCommandField.TYPE, S2CPayloadSendSetCommandField.STREAM_CODEC, S2CHandlerSetCommandField::receive);
        }else {
            NetworkManager.registerS2CPayloadType(S2CPayloadSendKeyCommands.TYPE, S2CPayloadSendKeyCommands.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(S2CPayloadSendSetCommandField.TYPE, S2CPayloadSendSetCommandField.STREAM_CODEC);
        }
    }
}
