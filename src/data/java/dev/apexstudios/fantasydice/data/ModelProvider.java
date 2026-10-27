package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.api.Dice;
import dev.apexstudios.fantasydice.api.FantasyDice;
import java.util.stream.IntStream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.properties.select.ComponentContents;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

final class ModelProvider extends dev.apexstudios.apexcore.api.data.ModelProvider {
    ModelProvider(PackOutput output) {
        super(output, FantasyDice.REGISTREE);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        dice(itemModels, FantasyDice.WOODEN_DICE.value());
        dice(itemModels, FantasyDice.STONE_DICE.value());
        dice(itemModels, FantasyDice.BONE_DICE.value());
        dice(itemModels, FantasyDice.IRON_DICE.value());
        dice(itemModels, FantasyDice.GOLDEN_DICE.value());
        dice(itemModels, FantasyDice.DIAMOND_DICE.value());
        dice(itemModels, FantasyDice.EMERALD_DICE.value());
        dice(itemModels, FantasyDice.NETHERITE_DICE.value());
        dice(itemModels, FantasyDice.COPPER_DICE.value());
        dice(itemModels, FantasyDice.ENDER_DICE.value());
        dice(itemModels, FantasyDice.FROZEN_DICE.value());
        dice(itemModels, FantasyDice.SLIME_DICE.value());
        dice(itemModels, FantasyDice.REDSTONE_DICE.value());
        dice(itemModels, FantasyDice.PAPER_DICE.value());
        dice(itemModels, FantasyDice.AMETHYST_DICE.value());
        dice(itemModels, FantasyDice.CHOCOLATE_DICE.value());
    }

    private void dice(ItemModelGenerators itemModels, Item item) {
        itemModels.itemModelOutput.accept(item, ItemModelUtils.select(
                new ComponentContents<>(FantasyDice.COMPONENT_TYPE.value()),
                IntStream.of(Dice.VALID_SIDES)
                        .mapToObj(sides -> ItemModelUtils.when(
                                sides,
                                ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_" + sides + "_sided", ModelTemplates.FLAT_ITEM))
                        ))
                        .toList()
        ));
    }
}
