package dev.apexstudios.fantasydice.data;

import dev.apexstudios.apexcore.api.util.ApexUtil;
import dev.apexstudios.fantasydice.common.FantasyDice;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceDataEntryPoint {
    public FantasyDiceDataEntryPoint(IEventBus modBus) {
        modBus.addListener(GatherDataEvent.Client.class, event -> {
            event.createProvider(FDLanguageProvider::new);
            event.createProvider(FDModelProvider::new);
            event.createProvider(FDEntityTypeTagsProvider::new);
            event.createProvider(output -> ApexUtil.createMetadataProvider(output, Component.literal("Fantasy's Dice resources"), PackType.SERVER_DATA));
        });

        modBus.addListener(GatherDataRegistryEntriesEvent.class, event -> event
                .gatherFor(Identifier.DEFAULT_NAMESPACE) // generates the recipes under `minecraft`
                .recipe(FDRecipeProvider::new)
        );
    }
}
