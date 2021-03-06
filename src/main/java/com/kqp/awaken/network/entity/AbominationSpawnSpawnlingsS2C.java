package com.kqp.awaken.network.entity;

import com.kqp.awaken.entity.mob.AbominationEntity;
import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class AbominationSpawnSpawnlingsS2C extends AwakenPacketS2C {
    public AbominationSpawnSpawnlingsS2C() {
        super("spawn_abomination_spawn_spawnlings_s2c");
    }

    public void send(AbominationEntity abomination) {
        for (ServerPlayerEntity player : abomination.bossBar.getPlayers()) {
            this.sendToPlayer(player, (buf) -> {
                buf.writeInt(abomination.getEntityId());
            });
        }
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        int id = data.readInt();

        context.getTaskQueue().execute(() -> {
            spawnSpawnlings(id);
        });
    }

    @Environment(EnvType.CLIENT)
    private static void spawnSpawnlings(int id) {
        World world = MinecraftClient.getInstance().world;

        Entity entity = world.getEntityById(id);
        world.addParticle(ParticleTypes.POOF, entity.getX(), entity.getY() - entity.getHeight() / 2F, entity.getZ(), 1.0D, 0.0D, 0.0D);
        world.playSound(
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ENTITY_HOGLIN_ANGRY, SoundCategory.HOSTILE,
                4.0F, 0.14f, false);
    }
}
