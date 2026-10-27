package dev.apexstudios.fantasydice.data;

import dev.apexstudios.apexcore.api.data.LanguageProvider;
import dev.apexstudios.fantasydice.api.Dice;
import dev.apexstudios.fantasydice.api.FantasyDice;
import dev.apexstudios.registree.api.util.CreativeModeTabRegistrations;
import net.minecraft.data.PackOutput;

final class LangProvider extends LanguageProvider {
    LangProvider(PackOutput output) {
        super(output, FantasyDice.ID);
    }

    @Override
    protected void addTranslations() {
        add(Dice.DESCRIPTION_KEY, "d%s");
        add(Dice.TAG, "Dice");
        add(CreativeModeTabRegistrations.defaultTranslationKey(FantasyDice.CREATIVE_MODE_TAB), "Fantasy's Dice");

        addTranslatableComponent(FantasyDiceDataEntryPoint.PACK_DESC, "Fantasy's Dice resources");
        addGameRule(FantasyDice.DICE_LIFETIME, "Dice Lifetime", "How long thrown dice may persist in world (in seconds)");

        addItem(FantasyDice.WOODEN_DICE, "Wooden Dice");
        addItem(FantasyDice.STONE_DICE, "Stone Dice");
        addItem(FantasyDice.BONE_DICE, "Bone Dice");
        addItem(FantasyDice.IRON_DICE, "Iron Dice");
        addItem(FantasyDice.GOLDEN_DICE, "Golden Dice");
        addItem(FantasyDice.DIAMOND_DICE, "Diamond Dice");
        addItem(FantasyDice.EMERALD_DICE, "Emerald Dice");
        addItem(FantasyDice.NETHERITE_DICE, "Netherite Dice");
        addItem(FantasyDice.COPPER_DICE, "Copper Dice");
        addItem(FantasyDice.ENDER_DICE, "Ender Dice");
        addItem(FantasyDice.FROZEN_DICE, "Frozen Dice");
        addItem(FantasyDice.SLIME_DICE, "Slime Dice");
        addItem(FantasyDice.REDSTONE_DICE, "Redstone Dice");
        addItem(FantasyDice.PAPER_DICE, "Paper Dice");
        addItem(FantasyDice.AMETHYST_DICE, "Amethyst Dice");
        addItem(FantasyDice.CHOCOLATE_DICE, "Chocolate Dice");
    }
}
