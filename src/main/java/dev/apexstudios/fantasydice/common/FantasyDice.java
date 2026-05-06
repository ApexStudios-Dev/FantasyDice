package dev.apexstudios.fantasydice.common;

import dev.apexstudios.registree.api.Registree;
import dev.apexstudios.registree.api.holder.DeferredDataComponent;
import dev.apexstudios.registree.api.holder.DeferredEntity;
import dev.apexstudios.registree.api.holder.DeferredGameRule;
import dev.apexstudios.registree.api.holder.DeferredItem;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(FantasyDice.ID)
public final class FantasyDice {
    public static final String ID = "fantasydice";
    public static final Registree REGISTREE = Registree.create(ID);

    public static final DeferredDataComponent<Die> DIE_COMPONENT = REGISTREE.registerDataComponent("die", Die.MAP_CODEC.codec(), Die.STREAM_CODEC);
    public static final DeferredItem<Item> DICE_ITEM = REGISTREE.registerItem("dice", Item::new, properties -> properties.stacksTo(8));

    public static final DeferredEntity<DiceEntity> DICE_ENTITY = REGISTREE.registerEntity("dice", DiceEntity::new, MobCategory.MISC, properties -> properties
            .noLootTable()
            .sized(.25F, .25F)
            .eyeHeight(ItemEntity.EYE_HEIGHT)
            .clientTrackingRange(6)
            .updateInterval(SharedConstants.TICKS_PER_SECOND)
    );

    public static final ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB = REGISTREE.registerCreativeModeTab("dice", Die.DEFAULT::asStack, (parameters, output) -> Die
            .builtIn()
            .map(Die::asStack)
            .forEach(output::accept)
    );

    public static final DeferredGameRule<Integer> RULE_DICE_LIFETIME = REGISTREE.registerIntegerGameRule("dice_lifetime", GameRuleCategory.MISC, DiceEntity.DEFAULT_LIFETIME, 0);

    public FantasyDice(IEventBus modBus) {
        REGISTREE.registerEvents(modBus);

        NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.RightClickItem.class, event -> {
            var level = event.getLevel();
            var player = event.getEntity();
            var hand = event.getHand();

            var stack = player.getItemInHand(hand);
            var die = stack.get(DIE_COMPONENT);

            if(die == null) {
                return;
            }

            var result = InteractionResult.SUCCESS;

            if(!level.isClientSide()) {
                die.throwInLevel(level, stack, player);
                result = InteractionResult.SUCCESS_SERVER;
            }

            event.setCancellationResult(result);
            event.setCanceled(true);
        });

        NeoForge.EVENT_BUS.addListener(ItemTooltipEvent.class, event -> {
            var stack = event.getItemStack();
            var display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
            stack.addToTooltip(DIE_COMPONENT, event.getContext(), display, event.getToolTip()::add, event.getFlags());
        });
    }
}
