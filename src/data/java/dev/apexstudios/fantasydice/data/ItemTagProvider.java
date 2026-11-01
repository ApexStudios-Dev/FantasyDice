package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.api.Dice;
import dev.apexstudios.fantasydice.api.FantasyDice;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;

final class ItemTagProvider extends TagsProvider<Item> {
    ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ITEM, lookupProvider, FantasyDice.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(Dice.TAG).add(
            FantasyDice.WOODEN_DICE.key(),
            FantasyDice.STONE_DICE.key(),
            FantasyDice.BONE_DICE.key(),
            FantasyDice.IRON_DICE.key(),
            FantasyDice.GOLDEN_DICE.key(),
            FantasyDice.DIAMOND_DICE.key(),
            FantasyDice.EMERALD_DICE.key(),
            FantasyDice.NETHERITE_DICE.key(),
            FantasyDice.COPPER_DICE.key(),
            FantasyDice.ENDER_DICE.key(),
            FantasyDice.FROZEN_DICE.key(),
            FantasyDice.SLIME_DICE.key(),
            FantasyDice.REDSTONE_DICE.key(),
            FantasyDice.PAPER_DICE.key(),
            FantasyDice.AMETHYST_DICE.key(),
            FantasyDice.CHOCOLATE_DICE.key()
        );
    }
}
