package dev.apexstudios.fantasydice.client;

import dev.apexstudios.fantasydice.common.FantasyDice;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceClientEntryPoint {
    public FantasyDiceClientEntryPoint(IEventBus modBus) {
        modBus.addListener(RegisterSelectItemModelPropertyEvent.class, event -> {
            event.register(FantasyDice.identifier("sides"), DiceSidesSelectModelProperty.TYPE);
            event.register(FantasyDice.identifier("material"), DiceMaterialSelectModelProperty.TYPE);
        });
    }
}
