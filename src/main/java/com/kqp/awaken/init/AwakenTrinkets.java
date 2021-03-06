package com.kqp.awaken.init;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.item.trinket.AwakenTrinketItem;
import com.kqp.awaken.item.trinket.FlightTrinketItem;
import com.kqp.awaken.trinket.AwakenSlots;
import com.kqp.awaken.util.DataUtil;
import com.kqp.awaken.util.JsonUtil;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import io.github.cottonmc.staticdata.StaticData;
import io.github.cottonmc.staticdata.StaticDataItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AwakenTrinkets {
    public static void init() {
        addSlots();
        loadTrinkets();
    }

    private static void addSlots() {
        TrinketSlots.addSlot(SlotGroups.HEAD, AwakenSlots.HAT, Awaken.id("textures/item/empty_trinket_slot_hat.png"));
        TrinketSlots.addSlot(SlotGroups.HEAD, Slots.MASK, new Identifier("trinkets", "textures/item/empty_trinket_slot_mask.png"));
        TrinketSlots.addSlot(SlotGroups.HEAD, Slots.NECKLACE, new Identifier("trinkets", "textures/item/empty_trinket_slot_necklace.png"));
        TrinketSlots.addSlot(SlotGroups.CHEST, Slots.BELT, new Identifier("trinkets", "textures/item/empty_trinket_slot_belt.png"));
        TrinketSlots.addSlot(SlotGroups.LEGS, Slots.CHARM, new Identifier("trinkets", "textures/item/empty_trinket_slot_charm.png"));
        TrinketSlots.addSlot(SlotGroups.FEET, Slots.AGLET, new Identifier("trinkets", "textures/item/empty_trinket_slot_aglet.png"));
        TrinketSlots.addSlot(SlotGroups.HAND, Slots.RING, new Identifier("trinkets", "textures/item/empty_trinket_slot_ring.png"));
        TrinketSlots.addSlot(SlotGroups.HAND, Slots.GLOVES, new Identifier("trinkets", "textures/item/empty_trinket_slot_gloves.png"));
        TrinketSlots.addSlot(SlotGroups.OFFHAND, Slots.RING, new Identifier("trinkets", "textures/item/empty_trinket_slot_ring.png"));
    }

    private static void loadTrinkets() {
        ImmutableSet<StaticDataItem> trinketStaticData = StaticData.getAllInDirectory("trinkets");

        trinketStaticData.forEach(staticDataItem -> {
            try {
                Identifier trinketItemId = DataUtil.getStrippedIdentifier(staticDataItem.getIdentifier());
                JsonObject trinketJsonObject = DataUtil.getJsonObject(staticDataItem);

                String trinketType = "normal";
                if (trinketJsonObject.has("type")) {
                    trinketType = trinketJsonObject.get("type").getAsString();
                }

                String trinketGroup = trinketJsonObject.get("group").getAsString();
                String trinketSlot = trinketJsonObject.get("slot").getAsString();

                JsonObject efgJsonObject = trinketJsonObject.get("entity_feature_group").getAsJsonObject();
                EntityFeatureGroup efg = EntityFeatureGroup.fromJsonObject(trinketItemId.getPath(),
                        efgJsonObject);

                String rendererId = "";
                if (trinketJsonObject.has("renderer")) {
                    rendererId = trinketJsonObject.get("renderer").getAsString();
                }

                AwakenTrinketItem trinketItem;

                if (trinketType.equals("normal")) {
                    trinketItem = new AwakenTrinketItem(trinketGroup, trinketSlot, efg, rendererId);
                } else if (trinketType.equals("flight")) {
                    double maxFlySpeed = trinketJsonObject.get("max_fly_speed").getAsDouble();
                    double flySpeed = trinketJsonObject.get("fly_speed").getAsDouble();
                    int flyTime = trinketJsonObject.get("fly_time").getAsInt();
                    boolean canFloat = trinketJsonObject.get("can_float").getAsBoolean();
                    AtomicReference<Identifier> flightParticleId = new AtomicReference(null);
                    AtomicReference<Identifier> floatParticleId = new AtomicReference(null);

                    JsonUtil.optionalElement(trinketJsonObject, "flight_particle_id", (particleJson -> flightParticleId.set(new Identifier(particleJson.getAsString()))));
                    JsonUtil.optionalElement(trinketJsonObject, "float_particle_id", (particleJson -> floatParticleId.set(new Identifier(particleJson.getAsString()))));

                    trinketItem = new FlightTrinketItem(trinketGroup, trinketSlot, efg, maxFlySpeed, flySpeed, flyTime, canFloat, rendererId, flightParticleId.get(), floatParticleId.get());
                } else {
                    throw new RuntimeException(String.format(
                            "Unknown trinket type \"%s\" for ID %s",
                            trinketType,
                            trinketItemId.toString()
                    ));
                }

                register(trinketItemId, trinketItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Item register(Identifier id, Item item) {
        Registry.register(Registry.ITEM, id, item);

        return item;
    }
}
