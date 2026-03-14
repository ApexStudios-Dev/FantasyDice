package dev.apexstudios.fantasydice.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.apexstudios.fantasydice.data.DieDataUtils;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;

public record Die(String material, int sides) implements TooltipProvider {
    public static final int[] DEFAULT_SIDES = new int[] { 4, 6, 8, 10, 12, 20 };

    public static final String[] DEFAULT_MATERIALS = new String[] {
            "wooden", "stone", "bone", "iron", "golden", "diamond", "emerald", "netherite", "copper", "ender", "frozen",
            "slime", "redstone", "paper", "amethyst", "chocolate"
    };

    public static final String DESCRIPTION_ID = FantasyDice.ID + ".description";

    public static final Die DEFAULT = new Die(DEFAULT_MATERIALS[0], DEFAULT_SIDES[0]);

    public static final MapCodec<Die> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.validate(str -> Identifier.isValidNamespace(str) ? DataResult.success(str) : DataResult.error(() -> "Invalid string for dice material: " + str)).fieldOf("material").forGetter(Die::material),
            ExtraCodecs.POSITIVE_INT.fieldOf("sides").forGetter(Die::sides)
    ).apply(builder, Die::new));

    public static final Codec<Die> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<ByteBuf, Die> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Die::material,
            ByteBufCodecs.VAR_INT, Die::sides,
            Die::new
    );

    public void throwInLevel(Level level, ItemStack stack, LivingEntity thrower) {
        var count = stack.getCount();

        var d0 = FantasyDice.DICE_ENTITY.value().getWidth() + 2.5F;
        var d1 = 1D - d0;
        var d2 = d0 / 2D;

        var x = Math.floor(thrower.getX());
        var y = thrower.getEyeY() - .3F;
        var z = Math.floor(thrower.getZ());
        var color = thrower.getTeamColor();
        var diceStack = stack.copyWithCount(1);
        var random = level.getRandom();

        for(var i = 0; i < count; i++) {
            var roll = random.nextInt(sides) + 1;
            var dice = DiceEntity.createDiceEntity(level, diceStack, thrower);

            dice.setPos(
                    x + random.nextDouble() * d1 + d2,
                    y,
                    z + random.nextDouble() * d1 + d2
            );

            dice.setCustomName(Component.literal(String.valueOf(roll)).withColor(color));
            dice.setCustomNameVisible(true);
            level.addFreshEntity(dice);
        }

        if(thrower instanceof Player player) {
            player.getCooldowns().addCooldown(stack, SharedConstants.TICKS_PER_SECOND / 2);
        }
    }

    public Die withMaterial(String material) {
        return this.material.equals(material) ? this : new Die(material, sides);
    }

    public Die withSides(int sides) {
        return this.sides == sides ? this : new Die(material, sides);
    }

    public Component getDescription() {
        return Component.translatable(DESCRIPTION_ID, Component.translatable(DieDataUtils.getMaterialKey(material)), sides);
    }

    public ItemStack asStack() {
        return new ItemStack(
                FantasyDice.DICE_ITEM,
                1,
                DataComponentPatch.builder()
                        .set(FantasyDice.DIE_COMPONENT.value(), this)
                        .build()
        );
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(getDescription());
    }

    public static Stream<Die> builtIn() {
        return Stream.of(DEFAULT_MATERIALS).flatMap(material -> IntStream.of(DEFAULT_SIDES).mapToObj(sides -> new Die(material, sides)));
    }
}
