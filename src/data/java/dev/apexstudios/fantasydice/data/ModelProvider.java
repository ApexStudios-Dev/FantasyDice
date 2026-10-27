package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.api.Dice;
import dev.apexstudios.fantasydice.api.FantasyDice;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.properties.select.ComponentContents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

final class ModelProvider extends net.minecraft.client.data.models.ModelProvider {
    ModelProvider(PackOutput output) {
        super(output, FantasyDice.ID);
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

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return FantasyDice.REGISTREE.registrar(Registries.ITEM).streamHolders();
    }
}
