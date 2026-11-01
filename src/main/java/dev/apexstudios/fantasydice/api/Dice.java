package dev.apexstudios.fantasydice.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.apexstudios.apexcore.api.FakeItemEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface Dice {
    int DEFAULT_LIFETIME = 10; // seconds

    int DEFAULT_SIDES = 6;
    int[] VALID_SIDES = new int[] { 4, 6, 8, 10, 12, 20 };
    String DESCRIPTION_KEY = "item." + FantasyDice.ID + ".desc";

    Codec<Integer> CODEC = Codec.INT.validate(value -> {
        if(isValid(value)) {
            return DataResult.success(value);
        }

        return DataResult.error(() -> "Value '" + value + "' is not a valid number of sides", value);
    });

    StreamCodec<ByteBuf, Integer> STREAM_CODEC = ByteBufCodecs.VAR_INT;
    TagKey<Item> TAG = FantasyDice.REGISTREE.tag(Registries.ITEM, "dice");

    static int roll(RandomSource random, int sides) {
        return (int) (random.nextDouble() * sides) + 1;
    }

    static int get(ItemStack stack) {
        return stack.getOrDefault(FantasyDice.COMPONENT_TYPE, DEFAULT_SIDES);
    }

    static void set(ItemStack stack, int sides) {
        stack.set(FantasyDice.COMPONENT_TYPE, sides);
    }

    static boolean is(ItemStack stack) {
        return stack.is(TAG);
    }

    static boolean isValid(int sides) {
        for(var valid : VALID_SIDES) {
            if(valid == sides) {
                return true;
            }
        }

        return false;
    }

    static InteractionResult roll(Level level, ItemStack stack, Player thrower) {
        if(!level.isClientSide()) {
            var sides = get(stack);
            var random = thrower.getRandom();
            var roll = roll(random, sides);

            var dice = FakeItemEntity.drop(thrower, stack, false, true);
            dice.getItem().set(DataComponents.ITEM_NAME, Component.translatable("%s %s", roll, getDescription(stack)));
            dice.setCustomNameVisible(true);
            //dice.setMergable(false);
            dice.setLifetime(Dice.getLifeTime(level));
            level.addFreshEntity(dice);
        }

        thrower.getCooldowns().addCooldown(stack, SharedConstants.TICKS_PER_SECOND / 2);
        return InteractionResult.SUCCESS;
    }

    static Component getDescription(ItemStack stack) {
        return Component.translatable(DESCRIPTION_KEY, get(stack));
    }

    static int getLifeTime(Level level) {
        var lifetime = DEFAULT_LIFETIME;

        if(level instanceof ServerLevel sLevel) {
            lifetime = sLevel.getGameRules().get(FantasyDice.DICE_LIFETIME.value());
        }

        // gamerule is in seconds but we want ticks
        // scale up by constant to convert
        return lifetime * SharedConstants.TICKS_PER_SECOND;
    }
}
