package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.common.FantasyDice;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.Tags;

final class FDEntityTypeTagsProvider extends EntityTypeTagsProvider {
    FDEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, FantasyDice.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED).add(FantasyDice.DICE_ENTITY.getKey());
    }
}
