package dev.apexstudios.fantasydice.client;

import dev.apexstudios.fantasydice.api.FantasyDice;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceClientEntryPoint {
    public FantasyDiceClientEntryPoint(IEventBus modBus) {
        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> event.registerEntityRenderer(FantasyDice.DICE.value(), DiceEntityRenderer::new));
    }
}
