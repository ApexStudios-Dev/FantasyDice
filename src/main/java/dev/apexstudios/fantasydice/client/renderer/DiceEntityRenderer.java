package dev.apexstudios.fantasydice.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.apexstudios.fantasydice.DiceEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class DiceEntityRenderer extends EntityRenderer<DiceEntity, ItemEntityRenderState> {
    private final ItemModelResolver modelResolver;
    private final RandomSource random = RandomSource.create();

    public DiceEntityRenderer(EntityRendererProvider.Context context) {
        super(context);

        modelResolver = context.getItemModelResolver();
        shadowRadius = .15F;
        shadowStrength = .75F;
    }

    @Override
    public ItemEntityRenderState createRenderState() {
        return new ItemEntityRenderState();
    }

    @Override
    public void extractRenderState(DiceEntity entity, ItemEntityRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);

        reusedState.bobOffset = entity.bobOffs;
        reusedState.shouldBob = IClientItemExtensions.of(entity.getItem()).shouldBobAsEntity(entity.getItem());
        reusedState.shouldSpread = IClientItemExtensions.of(entity.getItem()).shouldSpreadAsEntity(entity.getItem());
        reusedState.extractItemGroupRenderState(entity, entity.getItem(), modelResolver);
    }

    @Override
    public void submit(ItemEntityRenderState renderState, PoseStack pose, SubmitNodeCollector nodes, CameraRenderState camera) {
        if(renderState.item.isEmpty())
            return;

        pose.pushPose();
        var aabb = renderState.item.getModelBoundingBox();
        var f = -((float) aabb.minY) + ItemEntityRenderer.ITEM_MIN_HOVER_HEIGHT;
        var f1 = renderState.shouldBob ? Mth.sin(renderState.ageInTicks / 10F + renderState.bobOffset) * .1F + .1F : 0F;
        pose.translate(0F, f1 + f, 0F);
        var f2 = ItemEntity.getSpin(renderState.ageInTicks, renderState.bobOffset);
        pose.mulPose(Axis.YP.rotation(f2));
        ItemEntityRenderer.submitMultipleFromCount(pose, nodes, renderState.lightCoords, renderState, random, aabb);
        pose.popPose();

        super.submit(renderState, pose, nodes, camera);
    }
}
