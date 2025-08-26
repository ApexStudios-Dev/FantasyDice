package dev.apexstudios.fantasydice;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

public class DiceEntity extends Entity implements TraceableEntity {
    public static final String NBT_AGE = "Age";
    public static final String NBT_THROWER = "Thrower";
    public static final String NBT_ITEM = "Item";

    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(DiceEntity.class, EntityDataSerializers.ITEM_STACK);
    public static final int DEFAULT_LIFETIME = SharedConstants.TICKS_PER_SECOND * 10;

    private int age = 0;
    @Nullable private EntityReference<Entity> thrower;
    public final float bobOffs = random.nextFloat() * (float) Math.PI * 2F;

    public DiceEntity(EntityType<? extends DiceEntity> entityType, Level level) {
        super(entityType, level);

        setYRot(getRandom().nextFloat() * 360F);
    }

    public DiceEntity(Level level, ItemStack stack, @Nullable Entity thrower) {
        this(FantasyDice.DICE_ENTITY.value(), level);

        setItem(stack);
        setThrower(thrower);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        age = input.getIntOr(NBT_AGE, 0);
        thrower = EntityReference.read(input, NBT_THROWER);
        setItem(input.read(NBT_ITEM, ItemStack.CODEC).orElse(ItemStack.EMPTY));

        if(getItem().isEmpty())
            discard();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt(NBT_AGE, age);
        EntityReference.store(thrower, output, NBT_THROWER);

        if(!getItem().isEmpty())
            output.store(NBT_ITEM, ItemStack.CODEC, getItem());
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return EntityReference.getEntity(thrower, level());
    }

    @Override
    public void restoreFrom(Entity entity) {
        super.restoreFrom(entity);

        if(entity instanceof DiceEntity dice)
            thrower = dice.thrower;
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    protected double getDefaultGravity() {
        return .04D;
    }

    @Override
    public void tick() {
        if(getItem().isEmpty()) {
            discard();
            return;
        }

        super.tick();

        xo = getX();
        yo = getY();
        zo = getZ();

        var delta = getDeltaMovement();

        if(isInWater() && getFluidHeight(FluidTags.WATER) > ItemEntity.FLOAT_HEIGHT)
            setFluidMovement(.99F);
        else if(isInLava() && getFluidHeight(FluidTags.LAVA) > ItemEntity.FLOAT_HEIGHT)
            setFluidMovement(.95F);
        else
            applyGravity();

        if(level().isClientSide())
            noPhysics = false;
        else {
            var boundingBox = getBoundingBox();
            noPhysics = !level().noCollision(this, boundingBox.deflate(Shapes.EPSILON));

            if(noPhysics)
                moveTowardsClosestSpace(getX(), (boundingBox.minY + boundingBox.maxY) / 2D, getZ());
        }

        if(!onGround() || getDeltaMovement().horizontalDistanceSqr() > Mth.EPSILON || (tickCount + getId()) % 4 == 0) {
            move(MoverType.SELF, getDeltaMovement());
            applyEffectsFromBlocks();

            var f = .98F;

            if(onGround()) {
                var groundPos = getBlockPosBelowThatAffectsMyMovement();
                f = level().getBlockState(groundPos).getFriction(level(), groundPos, this) * f;
            }

            setDeltaMovement(getDeltaMovement().multiply(f, f, f));

            if(onGround()) {
                var delta1 = getDeltaMovement();

                if(delta1.y < 0D)
                    setDeltaMovement(delta1.multiply(1D, -.5D, 1D));
            }
        }

        age++;
        hasImpulse = hasImpulse | updateInWaterStateAndDoFluidPushing();

        if(!level().isClientSide()) {
            var d0 = getDeltaMovement().subtract(delta).lengthSqr();

            if(d0 > .01D)
                hasImpulse = true;
        }

        var item = getItem();

        if(!level().isClientSide() && age >= getLifeTime())
            discard();
        if(item.isEmpty() && !isRemoved())
            discard();
    }

    @Override
    public BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return getOnPos((float) DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0);
    }

    private void setFluidMovement(double multiplier) {
        var delta = getDeltaMovement();
        setDeltaMovement(delta.x * multiplier, delta.y + (delta.y < .06F ? MoveControl.MIN_SPEED : 0F), delta.z * multiplier);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public Component getName() {
        var customName = getCustomName();
        return customName == null ? getItem().getItemName() : customName;
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if(DATA_ITEM.equals(key))
            getItem().setEntityRepresentation(this);
    }

    public void setThrower(@Nullable Entity thrower) {
        this.thrower = EntityReference.of(thrower);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 180F - ItemEntity.getSpin(age + .5F, bobOffs) / (float) (Math.PI * 2F) * 360F;
    }

    @Override
    public SlotAccess getSlot(int slot) {
        return slot == 0 ? SlotAccess.of(this::getItem, this::setItem) : super.getSlot(slot);
    }

    public int getLifeTime() {
        return level() instanceof ServerLevel sLevel ? sLevel.getGameRules().getInt(FantasyDice.RULE_DICE_LIFETIME) : DEFAULT_LIFETIME;
    }
}
