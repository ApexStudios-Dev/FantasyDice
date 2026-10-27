package dev.apexstudios.fantasydice.core;

import dev.apexstudios.fantasydice.api.FantasyDice;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoveSimulationType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.joml.Math;
import org.jspecify.annotations.Nullable;

public final class DiceEntity extends Entity {
    public static final int DEFAULT_LIFETIME = 10; // seconds

    public static final String TAG_AGE = "age";
    public static final String TAG_ITEM = "item";

    public static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(DiceEntity.class, EntityDataSerializers.ITEM_STACK);

    private int age = /* ItemEntity.DEFAULT_AGE */ 0;
    public final float bobOffset = random.nextFloat() * Math.PI_TIMES_2_f;

    public DiceEntity(EntityType<?> type, Level level) {
        super(type, level);

        setYRot(random.nextFloat() * 360F);
    }

    public DiceEntity(Level level, double x, double y, double z, ItemStack stack) {
        this(FantasyDice.DICE.value(), level);

        setPos(x, y, z);
        setItem(stack);
        setDeltaMovement(random.nextDouble() * .2D - .1D, .2D, random.nextDouble() * .2D - .1D);
    }

    @Override
    public boolean dampensVibrations() {
        return getItem().is(ItemTags.DAMPENS_VIBRATIONS);
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    protected double getDefaultGravity() {
        return .04F;
    }

    @Override
    public void tick() {
        /*if(getItem().onEntityItemUpdate(this)) {
            return;
        }*/

        if(getItem().isEmpty()) {
            discard();
            return;
        }

        super.tick();

        xo = getX();
        yo = getY();
        zo = getZ();

        var oldMovement = getDeltaMovement();
        var fluidType = getMaxHeightFluidType();

        if(!fluidType.isAir() && !fluidType.isVanilla() && getFluidTypeHeight(fluidType) > /*ItemEntity.FLOAT_HEIGHT*/ .1F) {
            // fluidType.setItemMovement(this);
        } else if(isInWater() && getFluidHeight(FluidTags.WATER) > /*ItemEntity.FLOAT_HEIGHT*/ .1F) {
            setUnderwaterMovement();
        } else if(isInLava() && getFluidHeight(FluidTags.LAVA) > /*ItemEntity.FLOAT_HEIGHT*/ .1F) {
            setUnderLavaMovement();
        } else {
            applyGravity();
        }

        if(level().isClientSide()) {
            noPhysics = false;
        } else {
            noPhysics = !level().noCollision(this, getBoundingBox().deflate(/* AABB.EPSILON */ 1.0E-7));

            if(noPhysics) {
                moveTowardsClosestSpace(getX(), (getBoundingBox().minY - getBoundingBox().maxY) / 2D, getZ());
            }
        }

        if(onGround() && !(getDeltaMovement().horizontalDistanceSqr() > Mth.EPSILON) && ((tickCount + getId()) & 4) != 0) {
            applyEffectsFromBlocksForLastMovements();
        } else {
            move(MoverType.SELF, getDeltaMovement());
            applyEffectsFromBlocks();

            var airDrag = getAirDrag();
            var groundFriction = airDrag;

            if(onGround()) {
                var groundPos = getBlockPosBelowThatAffectsMyMovement();
                groundFriction *= level().getBlockState(groundPos).getFriction(level(), groundPos, this);
            }

            setDeltaMovement(getDeltaMovement().multiply(groundFriction, airDrag, groundFriction));

            if(onGround()) {
                var movement = getDeltaMovement();

                if(movement.y() < 0D) {
                    setDeltaMovement(movement.multiply(1D, -.5D, 1D));
                }
            }
        }

        age++;
        needsSync = needsSync | updateFluidInteraction();

        if(!level().isClientSide()) {
            var value = getDeltaMovement().subtract(oldMovement).lengthSqr();

            if(value > .01D) {
                needsSync = true;
            }
        }

        if(!level().isClientSide() && age >= getLifeTime()) {
            discard();
        }

        if(getItem().isEmpty() && !isRemoved()) {
            discard();
        }
    }

    @Override
    public BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return getOnPos(.999999F);
    }

    private void setUnderwaterMovement() {
        setFluidMovement(.99F);
    }

    private void setUnderLavaMovement() {
        setFluidMovement(.95F);
    }

    public void setFluidMovement(double multiplier) {
        var movement = getDeltaMovement();
        setDeltaMovement(movement.x() * multiplier, movement.y() + (movement.y() < .06F ? 5.0E-4F : 0F), movement.z() * multiplier);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected boolean shouldPlayLavaHurtSound() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt(TAG_AGE, age);

        if(!getItem().isEmpty()) {
            output.store(TAG_ITEM, ItemStack.CODEC, getItem());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        age = input.getIntOr(TAG_AGE, /* ItemEntity.DEFAULT_AGE */ 0);
        setItem(input.read(TAG_ITEM, ItemStack.CODEC).orElse(ItemStack.EMPTY));

        if(getItem().isEmpty()) {
            discard();
        }
    }

    @Override
    public Component getName() {
        var name = getCustomName();
        return name == null ? getItem().getItemName() : name;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public ItemStack getItem() {
        return getEntityData().get(DATA_ITEM);
    }

    public void setItem(ItemStack stack) {
        getEntityData().set(DATA_ITEM, stack);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 180F - ItemEntity.getSpin(age + .5F, bobOffset) / Math.PI_TIMES_2_f * 360F;
    }

    @Override
    public @Nullable SlotAccess getSlot(int slot) {
        return slot == 0 ? SlotAccess.of(this::getItem, this::setItem) : super.getSlot(slot);
    }

    @Override
    public MoveSimulationType getMoveSimulationType() {
        return MoveSimulationType.SERVER_AND_CLIENT;
    }

    public int getLifeTime() {
        var lifetime = DEFAULT_LIFETIME;

        if(level() instanceof ServerLevel level) {
            lifetime = level.getGameRules().get(FantasyDice.DICE_LIFETIME.value());
        }

        // gamerule is in seconds but we want ticks
        // scale up by constant to convert
        return lifetime * SharedConstants.TICKS_PER_SECOND;
    }
}
