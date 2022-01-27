package xyz.apex.forge.fantasytable.init;

import net.minecraft.block.Blocks;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.Tags;

import xyz.apex.forge.fantasytable.FantasyTable;
import xyz.apex.forge.fantasytable.item.DiceItem;
import xyz.apex.forge.fantasytable.item.DyeableDiceItem;
import xyz.apex.forge.fantasytable.util.DiceHelper;
import xyz.apex.forge.utility.registrator.provider.RegistrateLangExtProvider;
import xyz.apex.repack.com.tterrag.registrate.providers.RegistrateRecipeProvider;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public final class FTElements
{
	private static final FTRegistry REGISTRY = FTRegistry.getRegistry();

	// region: Wooden
	public static final DiceType<FTRegistry, DiceItem> DICE_WOODEN = DiceType
			.builder("wooden", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, Color.fromRgb(0xFF8A5A27)))
				.withRecipe((sides, ctx, recipe) -> recipes(sides, recipe, ItemTags.WOODEN_BUTTONS))
				.withSimpleDie(6)
				.withSimpleDie(20)
			.build();
	// endregion

	// region: Stone
	public static final DiceType<FTRegistry, DiceItem> DICE_STONE = DiceType
			.builder("stone", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextFormatting.GRAY))
				.withRecipe((sides, ctx, recipe) -> recipes(sides, recipe, Blocks.STONE_BUTTON))
				.withSimpleDie(6)
				.withSimpleDie(20)
			.build();
	// endregion

	// region: Paper
	public static final DiceType<FTRegistry, DyeableDiceItem> DICE_PAPER = DiceType
			.builder("paper", REGISTRY, DyeableDiceItem::new)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextFormatting.WHITE))
				.withRecipe((sides, ctx, recipe) -> recipes(sides, recipe, Items.PAPER))

				.withDie(6)
					.color(() -> () -> (stack, tintIndex) -> ((IDyeableArmorItem) stack.getItem()).getColor(stack))
				.build()

				.withDie(20)
					.color(() -> () -> (stack, tintIndex) -> ((IDyeableArmorItem) stack.getItem()).getColor(stack))
				.build()
			.build();
	// endregion

	// region: Bone
	public static final DiceType<FTRegistry, DiceItem> DICE_BONE = DiceType
			.builder("bone", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextFormatting.WHITE))
				.withRecipe((sides, ctx, recipe) -> recipes(sides, recipe, Tags.Items.BONES))
				.withSimpleDie(6)
				.withSimpleDie(20)
			.build();
	// endregion

	// region: Iron
	public static final DiceType<FTRegistry, DiceItem> DICE_IRON = DiceType
			.builder("iron", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextFormatting.GRAY))
				.withSimpleDie(6)
				.withSimpleDie(20)
			.build();
	// endregion

	// region: Golden
	public static final DiceType<FTRegistry, DiceItem> DICE_GOLD = DiceType
			.builder("golden", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextFormatting.YELLOW))
				.withSimpleDie(6)
				.withSimpleDie(20)
			.build();
	// endregion

	// region: Diamond
	public static final DiceType<FTRegistry, DiceItem> DICE_DIAMOND = DiceType
			.builder("diamond", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextFormatting.AQUA))
				.withSimpleDie(6)
				.withSimpleDie(20)
			.build();
	// endregion

	// region: Emerald
	public static final DiceType<FTRegistry, DiceItem> DICE_EMERALD = DiceType
			.builder("emerald", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, TextFormatting.GREEN))
				.withSimpleDie(6)
				.withSimpleDie(20)
			.build();
	// endregion

	// region: Fantasy
	public static final DiceType<FTRegistry, DiceItem> DICE_FANTASY = DiceType
			.builder("fantasy", REGISTRY)
				.withStyle((stack, style) -> colorOrDyed(stack, style, Color.fromRgb(0xFFF39F9F)))
				.usesFoil()
				.onRoll((player, hand, stack, min, sides, rolls) -> {
					UUID playerID = player.getGameProfile().getId();
					Random rng = player.getRandom();

					int newMin;
					int newMax;
					boolean loaded = false; // TODO

					if(playerID.equals(FantasyTable.FANTASY_UUID))
					{
						newMin = sides / 2;
						newMax = sides;
					}
					else
					{
						newMin = min;
						newMax = sides / 2;
					}

					return IntStream.range(0, stack.getCount()).map(i -> DiceHelper.roll(rng, newMin, newMax, loaded)).toArray();
				})

				.withDie(6)
					.lang("Fantasy's Lucky 6-Sided Die")
					.lang(RegistrateLangExtProvider.EN_GB, "Fantasy's Lucky 6-Sided Die")
				.build()

				.withDie(20)
					.lang("Fantasy's Lucky 20-Sided Die")
					.lang(RegistrateLangExtProvider.EN_GB, "Fantasy's Lucky 20-Sided Die")
				.build()
			.build();
	// endregion

	static void bootstrap()
	{
	}

	// region: Styles
	private static Style colorOrDyed(ItemStack stack, Style style, Color color)
	{
		Item item = stack.getItem();

		if(!stack.isEmpty() && item instanceof IDyeableArmorItem)
		{
			int dyedColor = ((IDyeableArmorItem) item).getColor(stack);
			return style.withColor(Color.fromRgb(dyedColor));
		}

		return style.withColor(color);
	}

	private static Style colorOrDyed(ItemStack stack, Style style, TextFormatting formatting)
	{
		Item item = stack.getItem();

		if(!stack.isEmpty() && item instanceof IDyeableArmorItem)
		{
			int dyedColor = ((IDyeableArmorItem) item).getColor(stack);
			return style.withColor(Color.fromRgb(dyedColor));
		}

		return style.withColor(formatting);
	}
	// endregion

	// region: Recipes
	@Nullable
	private static ShapedRecipeBuilder recipes(int sides, ShapedRecipeBuilder recipe, ITag.INamedTag<Item> ingredient)
	{
		switch(sides)
		{
			case 6: return recipeSixSided(recipe, ingredient);
			case 20: return recipeTwentySided(recipe, ingredient);
			default: return null;
		}
	}

	private static ShapedRecipeBuilder recipeSixSided(ShapedRecipeBuilder recipe, ITag.INamedTag<Item> ingredient)
	{
		return recipe.define('I', ingredient)
		             .pattern("II")
		             .pattern("II")
		             .unlockedBy("has_item", RegistrateRecipeProvider.hasItem(ingredient));
	}

	private static ShapedRecipeBuilder recipeTwentySided(ShapedRecipeBuilder recipe, ITag.INamedTag<Item> ingredient)
	{
		return recipe.define('I', ingredient)
		             .pattern(" I ")
		             .pattern("III")
		             .pattern(" I ")
		             .unlockedBy("has_item", RegistrateRecipeProvider.hasItem(ingredient));
	}

	@Nullable
	private static ShapedRecipeBuilder recipes(int sides, ShapedRecipeBuilder recipe, IItemProvider ingredient)
	{
		switch(sides)
		{
			case 6: return recipeSixSided(recipe, ingredient);
			case 20: return recipeTwentySided(recipe, ingredient);
			default: return null;
		}
	}

	private static ShapedRecipeBuilder recipeSixSided(ShapedRecipeBuilder recipe, IItemProvider ingredient)
	{
		return recipe.define('I', ingredient)
		             .pattern("II")
		             .pattern("II")
		             .unlockedBy("has_item", RegistrateRecipeProvider.hasItem(ingredient));
	}

	private static ShapedRecipeBuilder recipeTwentySided(ShapedRecipeBuilder recipe, IItemProvider ingredient)
	{
		return recipe.define('I', ingredient)
		             .pattern(" I ")
		             .pattern("III")
		             .pattern(" I ")
		             .unlockedBy("has_item", RegistrateRecipeProvider.hasItem(ingredient));
	}
	// endregion
}
