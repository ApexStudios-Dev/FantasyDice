package dev.apexstudios.fantasydice.common.util;

import dev.apexstudios.apexcore.api.util.StringHelper;
import dev.apexstudios.fantasydice.common.DiceEntity;
import dev.apexstudios.fantasydice.common.FantasyDice;
import java.util.Objects;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jspecify.annotations.Nullable;

public interface Dice {
    int[] DEFAULT_SIDES = new int[] { 4, 6, 8, 10, 12, 20 };

    String[] DEFAULT_MATERIALS = new String[] {
            "wooden", "stone", "bone", "iron", "golden", "diamond", "emerald", "netherite", "copper", "ender", "frozen",
            "slime", "redstone", "paper", "amethyst", "chocolate"
    };

    String DESCRIPTION_ID = FantasyDice.ID + ".description";

    @Nullable
    static String getMaterial(DataComponentHolder components) {
        return components.get(DiceRegistries.MATERIAL_COMPONENT);
    }

    static void setMaterial(MutableDataComponentHolder components, String material) {
        components.set(DiceRegistries.MATERIAL_COMPONENT, material);
    }

    static String getMaterialKey(String material) {
        return FantasyDice.ID + ".material." + material;
    }

    static int getSides(DataComponentHolder components) {
        return components.getOrDefault(DiceRegistries.SIDES_COMPONENT, -1);
    }

    static void setSides(MutableDataComponentHolder components, int sides) {
        components.set(DiceRegistries.SIDES_COMPONENT, sides);
    }

    static Component getDescription(DataComponentHolder components) {
        var sides = getSides(components);
        var material = Objects.requireNonNullElse(getMaterial(components), "unknown_dice");
        var materialName = Component.translatableWithFallback(getMaterialKey(material), StringHelper.toEnglishName(material));
        return Component.translatable(Dice.DESCRIPTION_ID, materialName, sides);
    }

    static ItemStack create(int sides, String material) {
        var stack = DiceRegistries.DICE_ITEM.toStack();
        setMaterial(stack, material);
        setSides(stack, sides);
        return stack;
    }

    static boolean throwDice(Level level, ItemStack stack, LivingEntity thrower) {
        if(stack.isEmpty() || !isValid(stack)) {
            return false;
        }

        var sides = getSides(stack);

        if(level.isClientSide()) {
            return true;
        }

        var count = stack.getCount();

        var d0 = DiceRegistries.DICE_ENTITY.value().getWidth() + 2.5F;
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
            var dice = createDiceEntity(level, diceStack, thrower);

            dice.setPos(
                    x + random.nextDouble() * d1 + d2,
                    y,
                    z + random.nextDouble() * d1 + d2
            );

            dice.setCustomName(Component.literal(String.valueOf(roll)).withColor(color));
            dice.setCustomNameVisible(true);
            level.addFreshEntity(dice);
        }

        return true;
    }

    static boolean isValid(DataComponentHolder components) {
        return components.has(DiceRegistries.SIDES_COMPONENT) && components.has(DiceRegistries.MATERIAL_COMPONENT);
    }

    private static DiceEntity createDiceEntity(Level level, ItemStack stack, LivingEntity thrower) {
        var random = thrower.getRandom();

        // copied from LivingEntity#createItemStackToDrop
        var f8 = Mth.sin(thrower.getXRot() * (float) (Math.PI / 180F));
        var f2 = Mth.cos(thrower.getXRot() * (float) (Math.PI / 180F));
        var f3 = Mth.sin(thrower.getYRot() * (float) (Math.PI / 180F));
        var f4 = Mth.cos(thrower.getYRot() * (float) (Math.PI / 180F));
        var f5 = random.nextFloat();
        var f6 = .02F * random.nextFloat();

        var dice = new DiceEntity(level, stack.copyWithCount(1), thrower);

        dice.setDeltaMovement(
                -f3 * f2 * .3F + Math.cos(f5) * f6,
                -f8 * .3F + .1F + (random.nextFloat() - random.nextFloat()) * .1F,
                f4 * f2 * .3F + Math.sin(f5) * f6
        );

        return dice;
    }
}
