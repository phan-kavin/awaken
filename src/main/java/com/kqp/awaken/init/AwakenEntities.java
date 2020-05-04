package com.kqp.awaken.init;

import com.kqp.awaken.entity.mob.AbominationEntity;
import com.kqp.awaken.entity.mob.DireWolfEntity;
import com.kqp.awaken.entity.mob.RaptorChickenEntity;
import com.kqp.awaken.entity.mob.SpiderSacEntity;
import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import com.kqp.awaken.entity.effect.AwakenStatusEffects;
import com.kqp.awaken.entity.mob.VoidGhostEntity;
import net.fabricmc.fabric.api.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenEntities {
    // TODO: add spawning
    // TODO: add killer bunny (https://minecraft.gamepedia.com/Rabbit#The_Killer_Bunny)

    public static final EntityType<RaptorChickenEntity> RAPTOR_CHICKEN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "raptor_chicken"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, RaptorChickenEntity::new)
                    .size(EntityDimensions.fixed(0.95F, 1.65F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<DireWolfEntity> DIRE_WOLF = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "dire_wolf"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, DireWolfEntity::new)
                    .size(EntityDimensions.fixed(1.5F, 1F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<SpiderSacEntity> SPIDER_SAC = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "spider_sac"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, SpiderSacEntity::new)
                    .size(EntityDimensions.fixed(0.8F, 0.8F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<VoidGhostEntity> VOID_GHOST = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "void_ghost"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, VoidGhostEntity::new)
                    .size(EntityDimensions.fixed(0.6F, 1.9F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<AbominationEntity> ABOMINATION = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "abomination"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, AbominationEntity::new)
                    .size(EntityDimensions.fixed(0.6F * 3F, 1.95F * 3F))
                    .trackable(72, 3)
                    .build()
    );

    public static void init() {
        Awaken.info("Initializing entities");

        // Custom Attributes
        {
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "ranged_damage"), AwakenEntityAttributes.RANGED_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "bow_damage"), AwakenEntityAttributes.BOW_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "crossbow_damage"), AwakenEntityAttributes.CROSSBOW_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "trident_damage"), AwakenEntityAttributes.TRIDENT_DAMAGE);

            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "melee_damage"), AwakenEntityAttributes.MELEE_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "sword_damage"), AwakenEntityAttributes.SWORD_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "axe_damage"), AwakenEntityAttributes.AXE_DAMAGE);
        }

        // Custom Status Effects
        {
            Registry.register(Registry.STATUS_EFFECT, new Identifier(Awaken.MOD_ID, "confusion"), AwakenStatusEffects.CONFUSION);
        }

        // Phase 2 Mobs
        {
            register(RAPTOR_CHICKEN, 0x9C0202, 0x610000, RaptorChickenEntity.createRaptorChickenAttributes());
            register(DIRE_WOLF, 0xD6E9FF, 0x97ADCC, DireWolfEntity.createDireWolfAttributes());
            register(SPIDER_SAC, 0x000000, 0xFFFFFF, SpiderSacEntity.createSpiderSacAttributes());
            register(VOID_GHOST, 0x0000000, 0xFFFFFF, VoidGhostEntity.createVoidGhostAttributes());
            register(ABOMINATION, 0xFFFFFF, 0x000000, AbominationEntity.createAbominationAttributes());
        }
    }

    private static <T extends LivingEntity> void register(EntityType<T> type, int primaryColor, int secondaryColor, DefaultAttributeContainer.Builder attributeBuilder) {
        Registry.register(Registry.ITEM, new Identifier(EntityType.getId(type).toString() + "_spawn_egg"),
                new SpawnEggItem(type, primaryColor, secondaryColor, new Item.Settings().group(ItemGroup.MISC))
        );

        FabricDefaultAttributeRegistry.register(type, attributeBuilder);
    }
}
