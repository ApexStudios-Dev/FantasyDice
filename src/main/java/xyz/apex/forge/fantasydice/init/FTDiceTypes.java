package xyz.apex.forge.fantasydice.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import xyz.apex.forge.commonality.tags.ItemTags;
import xyz.apex.forge.fantasydice.FantasyDice;
import xyz.apex.forge.fantasydice.item.DiceItem;
import xyz.apex.forge.fantasydice.item.DyeableDiceItem;
import xyz.apex.forge.fantasydice.util.DiceHelper;

import java.util.stream.IntStream;

public final class FTDiceTypes
{
	// region: Regular
	// region: Wooden
	public static final DiceType<FTRegistry, DiceItem> DICE_WOODEN = DiceType
			.builder("wooden", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF8A5A27)))
				.withDiceQuality(FantasyDice.CONFIG.diceWoodenQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Vanilla.PLANKS))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Vanilla.PLANKS))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Vanilla.PLANKS))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Vanilla.PLANKS))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Vanilla.PLANKS))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Vanilla.PLANKS))
				.build()
			.build();
	// endregion

	// region: Stone
	public static final DiceType<FTRegistry, DiceItem> DICE_STONE = DiceType
			.builder("stone", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, ChatFormatting.GRAY))
				.withDiceQuality(FantasyDice.CONFIG.diceStoneQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.COBBLESTONE))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.COBBLESTONE))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.COBBLESTONE))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.COBBLESTONE))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.COBBLESTONE))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.COBBLESTONE))
				.build()
			.build();
	// endregion

	// region: Bone
	public static final DiceType<FTRegistry, DiceItem> DICE_BONE = DiceType
			.builder("bone", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFEDDABC)))
				.withDiceQuality(FantasyDice.CONFIG.diceBoneQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.BONES))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.BONES))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.BONES))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.BONES))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.BONES))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.BONES))
				.build()
			.build();
	// endregion

	// region: Iron
	public static final DiceType<FTRegistry, DiceItem> DICE_IRON = DiceType
			.builder("iron", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFE0E0E0)))
				.withDiceQuality(FantasyDice.CONFIG.diceIronQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_IRON))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_IRON))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_IRON))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_IRON))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_IRON))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_IRON))
				.build()
			.build();
	// endregion

	// region: Golden
	public static final DiceType<FTRegistry, DiceItem> DICE_GOLD = DiceType
			.builder("golden", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFE8C037)))
				.withDiceQuality(FantasyDice.CONFIG.diceGoldenQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_GOLD))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_GOLD))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_GOLD))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_GOLD))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_GOLD))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_GOLD))
				.build()
			.build();
	// endregion

	// region: Diamond
	public static final DiceType<FTRegistry, DiceItem> DICE_DIAMOND = DiceType
			.builder("diamond", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, ChatFormatting.AQUA))
				.withDiceQuality(FantasyDice.CONFIG.diceDiamondQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_DIAMOND))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_DIAMOND))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_DIAMOND))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_DIAMOND))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_DIAMOND))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_DIAMOND))
				.build()
			.build();
	// endregion

	// region: Emerald
	public static final DiceType<FTRegistry, DiceItem> DICE_EMERALD = DiceType
			.builder("emerald", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF1F8B20)))
				.withDiceQuality(FantasyDice.CONFIG.diceEmeraldQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_EMERALD))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_EMERALD))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_EMERALD))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_EMERALD))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_EMERALD))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.GEMS_EMERALD))
				.build()
			.build();
	// endregion

	// region: Netherite
	public static final DiceType<FTRegistry, DiceItem> DICE_NETHERITE = DiceType
			.builder("netherite", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF423030)))
				.withDiceQuality(FantasyDice.CONFIG.diceNetheriteQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_NETHERITE))
					.properties(properties -> properties.fireResistant())
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_NETHERITE))
					.properties(properties -> properties.fireResistant())
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_NETHERITE))
					.properties(properties -> properties.fireResistant())
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_NETHERITE))
					.properties(properties -> properties.fireResistant())
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_NETHERITE))
					.properties(properties -> properties.fireResistant())
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.INGOTS_NETHERITE))
					.properties(properties -> properties.fireResistant())
				.build()
			.build();
	// endregion

	// region: Copper
	public static final DiceType<FTRegistry, DiceItem> DICE_COPPER = DiceType
			.builder("copper", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFD4784D)))
				.withDiceQuality(FantasyDice.CONFIG.diceCopperQuality::get)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COPPER_INGOT))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COPPER_INGOT))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COPPER_INGOT))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COPPER_INGOT))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COPPER_INGOT))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COPPER_INGOT))
				.build()
			.build();
	// endregion
	// endregion

	// region: Cosmetic
	// region: Ender
	public static final DiceType<FTRegistry, DiceItem> DICE_ENDER = DiceType
			.builder("ender", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF0F5959)))
				.withType(DiceType.Type.COSMETIC)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.ENDER_PEARL))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.ENDER_PEARL))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.ENDER_PEARL))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.ENDER_PEARL))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.ENDER_PEARL))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.ENDER_PEARL))
				.build()
			.build();
	// endregion

	// region: Frozen
	public static final DiceType<FTRegistry, DiceItem> DICE_FROZEN = DiceType
			.builder("frozen", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFBADED8)))
				.withType(DiceType.Type.COSMETIC)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.ICE))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.ICE))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.ICE))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.ICE))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.ICE))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Blocks.ICE))
				.build()
			.build();
	// endregion

	// region: Slime
	public static final DiceType<FTRegistry, DiceItem> DICE_SLIME = DiceType
			.builder("slime", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF8ACC83)))
				.withType(DiceType.Type.COSMETIC)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.SLIMEBALLS))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.SLIMEBALLS))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.SLIMEBALLS))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.SLIMEBALLS))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.SLIMEBALLS))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.SLIMEBALLS))
				.build()
			.build();
	// endregion

	// region: Redstone
	public static final DiceType<FTRegistry, DiceItem> DICE_REDSTONE = DiceType
			.builder("redstone", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF931515)))
				.withType(DiceType.Type.COSMETIC)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.DUSTS_REDSTONE))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.DUSTS_REDSTONE))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.DUSTS_REDSTONE))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.DUSTS_REDSTONE))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.DUSTS_REDSTONE))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, ItemTags.Forge.DUSTS_REDSTONE))
				.build()
			.build();
	// endregion

	// region: Paper
	public static final DiceType<FTRegistry, DyeableDiceItem> DICE_PAPER = DiceType
			.builder("paper", FTRegistry.INSTANCE, DyeableDiceItem::new)
				.withStyle((stack, style) -> colorOrDyed(stack, style, ChatFormatting.WHITE))
				.withType(DiceType.Type.COSMETIC)

				.withDie(4)
					.color(() -> () -> (stack, tintIndex) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack))
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.PAPER))
				.build()

				.withDie(6)
					.color(() -> () -> (stack, tintIndex) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack))
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.PAPER))
				.build()

				.withDie(8)
					.color(() -> () -> (stack, tintIndex) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack))
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.PAPER))
				.build()

				.withDie(10)
					.color(() -> () -> (stack, tintIndex) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack))
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.PAPER))
				.build()

				.withDie(12)
					.color(() -> () -> (stack, tintIndex) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack))
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.PAPER))
				.build()

				.withDie(20)
					.color(() -> () -> (stack, tintIndex) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack))
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.PAPER))
				.build()
			.build();
	// endregion

	// region: Amethyst
	public static final DiceType<FTRegistry, DiceItem> DICE_AMETHYST = DiceType
			.builder("amethyst", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFC991EB)))
				.withType(DiceType.Type.COSMETIC)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.AMETHYST_SHARD))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.AMETHYST_SHARD))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.AMETHYST_SHARD))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.AMETHYST_SHARD))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.AMETHYST_SHARD))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.AMETHYST_SHARD))
				.build()
			.build();
	// endregion
	// endregion

	// region: Speciality
	// region: Fantasy
	public static final DiceType<FTRegistry, DiceItem> DICE_FANTASY = DiceType
			.builder("fantasy", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFF39F9F)))
				.withType(DiceType.Type.SPECIALITY)
				.onRoll((player, hand, stack, min, sides, roll, dieQuality) -> {
					var rng = player.getRandom();

					int newMin;
					int newMax;

					if(DiceHelper.isLuckyRoller(player))
					{
						newMin = sides / 2;
						newMax = sides;
					}
					else
					{
						newMin = min;
						newMax = sides / 2;
					}

					return DiceHelper.roll(rng, newMin, newMax, dieQuality, false);
				})

				.withDie(6)
					.lang("Fantasy's Lucky 6-Sided Die")
				.build()

				.withDie(20)
					.lang("Fantasy's Lucky 20-Sided Die")
				.build()
			.build();
	// endregion

	// region: Tobi
	public static final DiceType<FTRegistry, DiceItem> DICE_TOBI = DiceType
			.builder("tobi", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF5B20A2)))
				.withType(DiceType.Type.SPECIALITY)
				.onRoll((player, hand, stack, min, sides, roll, dieQuality) -> {
					var rng = player.getRandom();
					return IntStream.range(0, 3).map($ -> DiceHelper.roll(rng, min, sides, dieQuality, false)).max().orElse(roll);
				})

				.withDie(6)
					.lang("Tobi's Thrice 6-Sided Die")
					.properties(properties -> properties.stacksTo(1))
				.build()

				.withDie(20)
					.lang("Tobi's Thrice 20-Sided Die")
					.properties(properties -> properties.stacksTo(1))
				.build()
			.build();
	// endregion

	// region: Apex
	public static final DiceType<FTRegistry, DiceItem> DICE_APEX = DiceType
			.builder("apex", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, ChatFormatting.DARK_PURPLE))
				.withType(DiceType.Type.SPECIALITY)
				.onRoll((player, hand, stack, min, sides, roll, dieQuality) -> roll * -1)

				.withDie(6)
					.lang("Apex's NULL 6-Sided Die")
				.build()

				.withDie(20)
					.lang("Apex's NULL 20-Sided Die")
				.build()
			.build();
	// endregion

	// region: Symacon
	public static final DiceType<FTRegistry, DiceItem> DICE_SYMACON = DiceType
			.builder("symacon", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFFFF681F)))
				.withType(DiceType.Type.SPECIALITY)
				.onRoll((player, hand, stack, min, sides, roll, dieQuality) -> {
					var half = player.getRandom().nextBoolean();
					return half ? roll / 2 : roll * 2;
				})

				.withDie(6)
					.lang("Symacon's Gambling 6-Sided Die")
				.build()

				.withDie(20)
					.lang("Symacon's Gambling 20-Sided Die")
				.build()
			.build();
	// endregion

	// region: Chocolate
	public static final DiceType<FTRegistry, DiceItem> DICE_CHOCOLATE = DiceType
			.builder("chocolate", FTRegistry.INSTANCE)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextColor.fromRgb(0xFF673B27)))
				.withType(DiceType.Type.SPECIALITY)

				.withDie(4)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COCOA_BEANS))
				.build()

				.withDie(6)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COCOA_BEANS))
				.build()

				.withDie(8)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COCOA_BEANS))
				.build()

				.withDie(10)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COCOA_BEANS))
				.build()

				.withDie(12)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COCOA_BEANS))
				.build()

				.withDie(20)
					.recipe((ctx, provider) -> diceRecipe(ctx, provider, Items.COCOA_BEANS))
				.build()
			.build();
	// endregion
	// endregion

	static void bootstrap()
	{
	}

	// region: Styles
	private static Style colorOrDyed(ItemStack stack, Style style, TextColor color)
	{
		var item = stack.getItem();

		if(!stack.isEmpty() && item instanceof DyeableLeatherItem dyedItem)
		{
			var dyedColor = dyedItem.getColor(stack);
			return style.withColor(TextColor.fromRgb(dyedColor));
		}

		return style.withColor(color);
	}

	private static Style colorOrDyed(ItemStack stack, Style style, ChatFormatting formatting)
	{
		var item = stack.getItem();

		if(!stack.isEmpty() && item instanceof DyeableLeatherItem dyedItem)
		{
			var dyedColor = dyedItem.getColor(stack);
			return style.withColor(TextColor.fromRgb(dyedColor));
		}

		return style.withColor(formatting);
	}
	// endregion

	// region: Recipes
	private static <D extends DiceItem> void diceRecipe(DataGenContext<Item, D> ctx, RegistrateRecipeProvider provider, TagKey<Item> ingredient)
	{
		FTRecipes.diceStation(DataIngredient.tag(ingredient), ctx::get, 1)
		         .unlockedBy("has_item", RegistrateRecipeProvider.has(ingredient))
				 // .group(String.format("dice/%d_sided", ctx.getEntry().getSides()))
		         .save(provider, ctx.getId());
	}

	private static <D extends DiceItem> void diceRecipe(DataGenContext<Item, D> ctx, RegistrateRecipeProvider provider, ItemLike ingredient)
	{
		FTRecipes.diceStation(DataIngredient.items(ingredient.asItem()), ctx::get, 1)
		         .unlockedBy("has_item", RegistrateRecipeProvider.has(ingredient))
		         // .group(String.format("dice/%d_sided", ctx.getEntry().getSides()))
		         .save(provider, ctx.getId());
	}
	// endregion
}