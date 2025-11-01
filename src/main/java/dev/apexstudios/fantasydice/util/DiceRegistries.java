package dev.apexstudios.fantasydice.util;

import dev.apexstudios.fantasydice.DiceEntity;
import dev.apexstudios.fantasydice.FantasyDice;
import dev.apexstudios.registree.api.Registree;
import dev.apexstudios.registree.api.holder.DeferredDataComponent;
import dev.apexstudios.registree.api.holder.DeferredEntity;
import dev.apexstudios.registree.api.holder.DeferredItem;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;

public interface DiceRegistries {
    Registree REGISTREE = Registree.create(FantasyDice.ID);

    DeferredDataComponent<String> MATERIAL_COMPONENT = REGISTREE.registerDataComponent("material", DiceCodecs.MATERIAL_CODEC);
    DeferredDataComponent<Integer> SIDES_COMPONENT = REGISTREE.registerDataComponent("sides", DiceCodecs.SIDES_CODEC);
    DeferredItem<Item> DICE_ITEM = REGISTREE.registerItem("dice", Item::new, properties -> properties.stacksTo(8));

    DeferredEntity<DiceEntity> DICE_ENTITY = REGISTREE.registerEntity("dice", DiceEntity::new, MobCategory.MISC, properties -> properties
            .noLootTable()
            .sized(.25F, .25F)
            .eyeHeight(ItemEntity.EYE_HEIGHT)
            .clientTrackingRange(6)
            .updateInterval(SharedConstants.TICKS_PER_SECOND)
    );

    ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB = REGISTREE.registerCreativeModeTab("dice", () -> Dice.create(Dice.DEFAULT_SIDES[0], Dice.DEFAULT_MATERIALS[0]), (parameters, output) -> {
        for(var material : Dice.DEFAULT_MATERIALS) {
            for(var side : Dice.DEFAULT_SIDES) {
                output.accept(Dice.create(side, material));
            }
        }
    });

    GameRules.Key<GameRules.IntegerValue> RULE_DICE_LIFETIME = REGISTREE.registerIntegerGameRule("diceLifetime", GameRules.Category.MISC, DiceEntity.DEFAULT_LIFETIME);
}
