package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.common.FantasyDice;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceDataEntryPoint {
    public FantasyDiceDataEntryPoint(IEventBus modBus) {
        modBus.addListener(GatherDataEvent.Client.class, event -> {
            event.createReloadableRegistryObjects(new RegistrySetBuilder()
                    .add(RecipeProvider.asBootstrap(FDRecipeProvider::new))
            );

            event.createProvider(FDLanguageProvider::new);
            event.createProvider(FDModelProvider::new);
            event.createProvider(FDEntityTypeTagsProvider::new);
            event.createProvider(output -> PackMetadataGenerator.forFeaturePack(output, Component.literal("Fantasy's Dice resources")));
        });
    }
}
