package dev.apexstudios.fantasydice;

import dev.apexstudios.apexcore.lib.util.CustomCooldownGroup;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;

public final class DiceItem extends Item implements CustomCooldownGroup {
    public static final String ROLL_KEY = "item." + FantasyDice.ID + ".dice.roll";
    public static final String RESULT_KEY = "item." + FantasyDice.ID + ".dice.result";

    public DiceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(stack, 20);

        if(!level.isClientSide)
            return InteractionResult.SUCCESS_SERVER;

        var count = stack.getCount();
        var sides = getSides(stack);
        var rolls = IntStream.rangeClosed(1, count).map(i -> level.random.nextInt(sides) + 1).toArray();

        player.displayClientMessage(buildComponent(player, stack, sides, rolls), false);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(FMLEnvironment.production || !tooltipFlag.isAdvanced())
            return;

        var sides = getSides(stack);
        var material = getMaterial(stack);

        tooltipComponents.add(Component.literal("Sides: " + sides));
        tooltipComponents.add(Component.literal("Material: " + material));
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

    public static Component buildComponent(Player player, ItemStack stack, int sides, int[] rolls) {
        var total = IntStream.of(rolls).sum();

        return Component.translatable(ROLL_KEY, player.getDisplayName(), buildComponent(total, stack.getCount(), sides))
                .withStyle(style -> style.withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        buildComponent(stack.getStyledHoverName(), rolls)
                )));
    }

    private static Component buildComponent(int roll, int count, int sides) {
        return Component.translatable(RESULT_KEY, roll, count, sides).withStyle(style -> style);
    }

    public static Component buildComponent(Component itemName, int[] rolls) {
        var rollsComponent = Component.empty();

        for(var i = 0; i < rolls.length; i++) {
            rollsComponent.append(Component.literal(String.valueOf(rolls[i])).withStyle(style -> style.withItalic(true)));

            if(i + 1 < rolls.length)
                rollsComponent.append(", ");
        }

        return Component.empty().append(itemName).append(CommonComponents.space()).append(ComponentUtils.wrapInSquareBrackets(rollsComponent));
    }

    public static ItemStack create(int sides, String material) {
        var stack = FantasyDice.DICE_ITEM.toStack();
        setSides(stack, sides);
        setMaterial(stack, material);
        return stack;
    }
}
