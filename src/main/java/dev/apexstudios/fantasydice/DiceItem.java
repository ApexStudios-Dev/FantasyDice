package dev.apexstudios.fantasydice;

import dev.apexstudios.apexcore.lib.util.CustomCooldownGroup;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;

public final class DiceItem extends Item implements CustomCooldownGroup {
    public DiceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide()) {
            var stack = player.getItemInHand(hand);
            var count = stack.getCount();
            var sides = getSides(stack);

            var d0 = FantasyDice.DICE_ENTITY.value().getWidth() + 2.5F;
            var d1 = 1D - d0;
            var d2 = d0 / 2D;

            var x = Math.floor(player.getX());
            var y = player.getEyeY() - .3F;
            var z = Math.floor(player.getZ());
            var color = player.getTeamColor();

            for(var i = 0; i < count; i++) {
                var roll = level.random.nextInt(sides) + 1;
                var dice = createDiceEntity(level, stack.copyWithCount(1), player);

                dice.setPos(
                        x + level.random.nextDouble() * d1 + d2,
                        y,
                        z + level.random.nextDouble() * d1 + d2
                );

                dice.setCustomName(Component.literal(String.valueOf(roll)).withColor(color));
                dice.setCustomNameVisible(true);
                level.addFreshEntity(dice);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> adder, TooltipFlag tooltipFlag) {
        if(FMLEnvironment.production || !tooltipFlag.isAdvanced())
            return;

        var sides = getSides(stack);
        var material = getMaterial(stack);

        adder.accept(Component.literal("Sides: " + sides));
        adder.accept(Component.literal("Material: " + material));
    }

    @Override
    public Component getName(ItemStack stack) {
        var sides = getSides(stack);
        var material = getMaterial(stack);

        return Component.translatable(getDiceKey(sides, material));
    }

    @Override
    public ResourceLocation computeCooldownGroup(ItemStack stack) {
        var sides = getSides(stack);
        var material = getMaterial(stack);

        return FantasyDice.identifier(material + '_' + sides + "_side_dice");
    }

    public static void setSides(ItemStack stack, int sides) {
        stack.set(FantasyDice.SIDES_COMPONENT, sides);
    }

    public static void setMaterial(ItemStack stack, String material) {
        stack.set(FantasyDice.MATERIAL_COMPONENT, material);
    }

    public static int getSides(ItemStack stack) {
        return stack.getOrDefault(FantasyDice.SIDES_COMPONENT, FantasyDice.BASE_SIDES);
    }

    public static String getMaterial(ItemStack stack) {
        return stack.getOrDefault(FantasyDice.MATERIAL_COMPONENT, FantasyDice.BASE_MATERIAL);
    }

    public static String getDiceKey(int sides, String material) {
        var key = FantasyDice.DICE_ITEM.value().getDescriptionId();
        return key + '.' + material + '.' + sides + "_sided";
    }

    public static ItemStack create(int sides, String material) {
        var stack = FantasyDice.DICE_ITEM.toStack();
        setSides(stack, sides);
        setMaterial(stack, material);
        return stack;
    }

    public static DiceEntity createDiceEntity(Level level, ItemStack stack, LivingEntity thrower) {
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
