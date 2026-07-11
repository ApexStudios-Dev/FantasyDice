package dev.apexstudios.fantasydice.common;

import dev.apexstudios.fantasydice.client.renderer.DiceEntityRenderer;
import dev.apexstudios.registree.Registree;
import dev.apexstudios.registree.holder.DeferredDataComponent;
import dev.apexstudios.registree.holder.DeferredEntity;
import dev.apexstudios.registree.holder.DeferredGameRule;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@Mod(FantasyDice.ID)
public final class FantasyDice {
    public static final String ID = "fantasydice";
    public static final Registree REGISTREE = Registree.create(ID);

    public static final DeferredDataComponent<Die> DIE_COMPONENT = REGISTREE.dataComponent("die", properties -> properties
            .persistent(Die.CODEC)
            .networkSynchronized(Die.STREAM_CODEC)
    );
    public static final DeferredItem<Item> DICE_ITEM = REGISTREE.item("dice", Item::new)
            .properties(properties -> properties.stacksTo(8))
            .register();

    public static final DeferredEntity<DiceEntity> DICE_ENTITY = REGISTREE.<DiceEntity>entity("dice", DiceEntity::new, MobCategory.MISC)
            .properties(properties -> properties
                    .noLootTable()
                    .sized(.25F, .25F)
                    .eyeHeight(ItemEntity.EYE_HEIGHT)
                    .clientTrackingRange(6)
                    .updateInterval(SharedConstants.TICKS_PER_SECOND)
            )
            .renderer(() -> () -> DiceEntityRenderer::new)
            .register();

    public static final ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB = REGISTREE.creativeModeTab("dice", properties -> properties
            .icon(Die.DEFAULT::asStack)
            .displayItems((parameters, output) -> Die
                    .builtIn()
                    .map(Die::asStack)
                    .forEach(output::accept)
            )
    );

    public static final DeferredGameRule<Integer> RULE_DICE_LIFETIME = REGISTREE.integerGameRule("dice_lifetime", 0, Integer.MAX_VALUE, DiceEntity.DEFAULT_LIFETIME).register();

    public FantasyDice(IEventBus modBus) {
        REGISTREE.register(modBus);

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
    }
}
