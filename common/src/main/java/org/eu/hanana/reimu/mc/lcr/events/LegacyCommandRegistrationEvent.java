package org.eu.hanana.reimu.mc.lcr.events;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.eu.hanana.reimu.mc.lcr.CommandManager;

public interface LegacyCommandRegistrationEvent {
    Event<LegacyCommandRegistrationEvent> EVENT = EventFactory.createLoop(new LegacyCommandRegistrationEvent[0]);

    void register(CommandManager commandManager);
}
