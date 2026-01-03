package dev.apexstudios.fantasydice.client;

import dev.apexstudios.fantasydice.client.renderer.DiceEntityRenderer;
import dev.apexstudios.fantasydice.common.FantasyDice;
import dev.apexstudios.fantasydice.common.util.DiceRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceClientEntryPoint {
    public FantasyDiceClientEntryPoint(IEventBus modBus) {
        modBus.addListener(RegisterSelectItemModelPropertyEvent.class, event -> {
            event.register(FantasyDice.identifier("sides"), DiceSidesSelectModelProperty.TYPE);
            event.register(FantasyDice.identifier("material"), DiceMaterialSelectModelProperty.TYPE);
        });

        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> event.registerEntityRenderer(DiceRegistries.DICE_ENTITY.value(), DiceEntityRenderer::new));
    }
}
