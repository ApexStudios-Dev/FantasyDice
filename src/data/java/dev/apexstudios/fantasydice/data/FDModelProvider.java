package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.common.FantasyDice;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;

final class FDModelProvider extends ModelProvider {
    FDModelProvider(PackOutput output) {
        super(output, FantasyDice.ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        DieDataUtils.createDiceModel(FantasyDice.DICE_ITEM, itemModels);
    }
}
