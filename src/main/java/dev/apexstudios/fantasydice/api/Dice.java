package dev.apexstudios.fantasydice.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.apexstudios.fantasydice.core.DiceEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Math;

public interface Dice {
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
        var sides = get(stack);
        var random = thrower.getRandom();
        var roll = roll(random, sides);

        var thrown = stack.copy();
        thrown.set(DataComponents.ITEM_NAME, Component.translatable("%s %s", roll, getDescription(thrown)));
        var yHandPos = thrower.getEyeY() - .3F;

        var dice = new DiceEntity(level, thrower.getX(), yHandPos, thrower.getZ(), thrown.copy());
        dice.setCustomNameVisible(true);

        var pow = .3F;
        var rotX = thrower.getXRot() * (Math.PI / 180F);
        var rotY = thrower.getYRot() * (Math.PI / 180F);
        var sinX = Mth.sin(rotX);
        var cosX = Mth.cos(rotX);
        var sinY = Mth.sin(rotY);
        var cosY = Mth.cos(rotY);
        var dir = random.nextFloat() * Math.PI_TIMES_2_f;
        var pow2 = .02F * random.nextFloat();

        dice.setDeltaMovement(
                -sinY * cosX * pow + Math.cos(dir) * pow2,
                -sinX * pow + .1F + (random.nextFloat() - random.nextFloat()) * .1F,
                cosY * cosX * pow + Math.sin(dir) * pow2
        );

        level.addFreshEntity(dice);

        thrower.getCooldowns().addCooldown(stack, SharedConstants.TICKS_PER_SECOND / 2);
        return InteractionResult.SUCCESS;
    }

    static Component getDescription(ItemStack stack) {
        return Component.translatable(DESCRIPTION_KEY, get(stack));
    }
}
