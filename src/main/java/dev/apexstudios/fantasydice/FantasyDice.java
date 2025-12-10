package dev.apexstudios.fantasydice;

import dev.apexstudios.fantasydice.util.Dice;
import dev.apexstudios.fantasydice.util.DiceRegistries;
import net.minecraft.SharedConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(FantasyDice.ID)
public final class FantasyDice {
    public static final String ID = "fantasydice";

    public FantasyDice(IEventBus modBus) {
        DiceRegistries.REGISTREE.registerEvents(modBus);

        NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.RightClickItem.class, event -> {
            var level = event.getLevel();
            var player = event.getEntity();
            var hand = event.getHand();

            var stack = player.getItemInHand(hand);

            if(Dice.throwDice(level, stack, player)) {
                player.getCooldowns().addCooldown(stack, SharedConstants.TICKS_PER_SECOND / 2);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        });

        NeoForge.EVENT_BUS.addListener(ItemTooltipEvent.class, event -> {
            var stack = event.getItemStack();

            if(Dice.isValid(stack)) {
                event.getToolTip().add(Dice.getDescription(stack));
            }
        });
    }

    public static Identifier identifier(String identifier) {
        return DiceRegistries.REGISTREE.registryName(identifier);
    }
}
