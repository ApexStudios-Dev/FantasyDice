package dev.apexstudios.fantasydice.common.util;

import dev.apexstudios.fantasydice.client.renderer.DiceEntityRenderer;
import dev.apexstudios.fantasydice.common.DiceEntity;
import dev.apexstudios.fantasydice.common.FantasyDice;
import dev.apexstudios.registree.Registree;
import dev.apexstudios.registree.holder.DeferredDataComponentType;
import dev.apexstudios.registree.holder.DeferredEntityType;
import dev.apexstudios.registree.holder.DeferredGameRule;
import dev.apexstudios.registree.registrar.CreativeModeTabRegistrar;
import dev.apexstudios.registree.registrar.DataComponentTypeRegistrar;
import dev.apexstudios.registree.registrar.EntityTypeRegistrar;
import dev.apexstudios.registree.registrar.GameRuleRegistrar;
import dev.apexstudios.registree.registrar.ItemRegistrar;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.neoforged.neoforge.registries.DeferredItem;

public interface DiceRegistries {
    Registree REGISTREE = Registree.create(FantasyDice.ID);
    ItemRegistrar ITEMS = REGISTREE.items();
    EntityTypeRegistrar ENTITY_TYPES = REGISTREE.entityTypes();
    GameRuleRegistrar GAME_RULES = REGISTREE.gameRules();
    DataComponentTypeRegistrar DATA_COMPONENT_TYPES = REGISTREE.dataComponentTypes();
    CreativeModeTabRegistrar CREATIVE_MODE_TABS = REGISTREE.creativeModeTabs();

    DeferredDataComponentType<String> MATERIAL_COMPONENT = DATA_COMPONENT_TYPES.register("material", DiceCodecs.MATERIAL_CODEC);
    DeferredDataComponentType<Integer> SIDES_COMPONENT = DATA_COMPONENT_TYPES.register("sides", DiceCodecs.SIDES_CODEC);

    DeferredItem<Item> DICE_ITEM = ITEMS.builder("dice", Item::new)
            .properties(properties -> properties.stacksTo(8))
            .register();

    DeferredEntityType<DiceEntity> DICE_ENTITY = ENTITY_TYPES.<DiceEntity>builder("dice", DiceEntity::new, MobCategory.MISC)
            .properties(properties -> properties
                    .noLootTable()
                    .sized(.25F, .25F)
                    .eyeHeight(ItemEntity.EYE_HEIGHT)
                    .clientTrackingRange(6)
                    .updateInterval(SharedConstants.TICKS_PER_SECOND)
            )
            .renderer(() -> () -> DiceEntityRenderer::new)
            .register();

    ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.register("dice", () -> Dice.create(Dice.DEFAULT_SIDES[0], Dice.DEFAULT_MATERIALS[0]), (parameters, output) -> {
        for(var material : Dice.DEFAULT_MATERIALS) {
            for(var side : Dice.DEFAULT_SIDES) {
                output.accept(Dice.create(side, material).create());
            }
        }
    });

    DeferredGameRule<Integer> RULE_DICE_LIFETIME = GAME_RULES.registerInteger("dice_lifetime", GameRuleCategory.MISC, DiceEntity.DEFAULT_LIFETIME);
}
