package dev.apexstudios.fantasydice.core;

import dev.apexstudios.fantasydice.api.Dice;
import dev.apexstudios.fantasydice.api.FantasyDice;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterTooltipAppendersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(FantasyDice.ID)
public final class FantasyDiceEntryPoint {
    public FantasyDiceEntryPoint(IEventBus modBus) {
        FantasyDice.REGISTREE.register(modBus);

        modBus.addListener(RegisterTooltipAppendersEvent.class, event -> event.registerComponentAppenderBeforeAll(FantasyDice.COMPONENT_TYPE, (stack, context, display, player, flag, builder) -> builder.accept(Dice.getDescription(stack))));

        NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.RightClickItem.class, event -> {
            var stack = event.getItemStack();

            if(Dice.is(stack)) {
                event.setCancellationResult(Dice.roll(event.getLevel(), stack, event.getEntity()));
                event.setCanceled(true);
            }
        });
    }
}
