package dev.apexstudios.fantasydice.api;

import dev.apexstudios.registree.api.Registree;
import dev.apexstudios.registree.api.holders.DeferredDataComponentType;
import dev.apexstudios.registree.api.holders.DeferredGameRule;
import dev.apexstudios.registree.api.util.CreativeModeTabRegistrations;
import dev.apexstudios.registree.api.util.DataComponentRegistrations;
import dev.apexstudios.registree.api.util.GameRuleRegistrations;
import dev.apexstudios.registree.api.util.ItemRegistrations;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;

public interface FantasyDice {
    String ID = "fantasydice";
    Registree REGISTREE = Registree.create(ID);
    DeferredDataComponentType<Integer> COMPONENT_TYPE = DataComponentRegistrations.register(REGISTREE, "dice", Dice.CODEC, Dice.STREAM_CODEC);

    DeferredItem<Item> WOODEN_DICE = dice("wooden");
    DeferredItem<Item> STONE_DICE = dice("stone");
    DeferredItem<Item> BONE_DICE = dice("bone");
    DeferredItem<Item> IRON_DICE = dice("iron");
    DeferredItem<Item> GOLDEN_DICE = dice("golden");
    DeferredItem<Item> DIAMOND_DICE = dice("diamond");
    DeferredItem<Item> EMERALD_DICE = dice("emerald");
    DeferredItem<Item> NETHERITE_DICE = dice("netherite");
    DeferredItem<Item> COPPER_DICE = dice("copper");
    DeferredItem<Item> ENDER_DICE = dice("ender");
    DeferredItem<Item> FROZEN_DICE = dice("frozen");
    DeferredItem<Item> SLIME_DICE = dice("slime");
    DeferredItem<Item> REDSTONE_DICE = dice("redstone");
    DeferredItem<Item> PAPER_DICE = dice("paper");
    DeferredItem<Item> AMETHYST_DICE = dice("amethyst");
    DeferredItem<Item> CHOCOLATE_DICE = dice("chocolate");

    ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB = CreativeModeTabRegistrations.register(REGISTREE, "items", () -> new ItemStack(WOODEN_DICE.value()), output -> {
        append(WOODEN_DICE.value(), output);
        append(STONE_DICE.value(), output);
        append(BONE_DICE.value(), output);
        append(IRON_DICE.value(), output);
        append(GOLDEN_DICE.value(), output);
        append(DIAMOND_DICE.value(), output);
        append(EMERALD_DICE.value(), output);
        append(NETHERITE_DICE.value(), output);
        append(COPPER_DICE.value(), output);
        append(ENDER_DICE.value(), output);
        append(FROZEN_DICE.value(), output);
        append(SLIME_DICE.value(), output);
        append(REDSTONE_DICE.value(), output);
        append(PAPER_DICE.value(), output);
        append(AMETHYST_DICE.value(), output);
        append(CHOCOLATE_DICE.value(), output);
    });

    // value is in seconds
    DeferredGameRule<Integer> DICE_LIFETIME = GameRuleRegistrations.registerInteger(
            REGISTREE,
            "dice_lifetime",
            properties -> properties.value(
                    Dice.DEFAULT_LIFETIME, // default 10 seconds
                    1, // min 1 second
                    (SharedConstants.TICKS_PER_MINUTE * 5) / SharedConstants.TICKS_PER_SECOND // max 5 mins
            )
    );

    private static DeferredItem<Item> dice(String material) {
        return ItemRegistrations.register(REGISTREE, material, Item::new, properties -> properties.stacksTo(8).component(COMPONENT_TYPE, Dice.DEFAULT_SIDES));
    }

    private static void append(Item item, CreativeModeTab.Output output) {
        for(var sides : Dice.VALID_SIDES) {
            var stack = new ItemStack(item);
            Dice.set(stack, sides);
            output.accept(stack);
        }
    }
}
