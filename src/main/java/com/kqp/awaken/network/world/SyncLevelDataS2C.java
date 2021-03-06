package com.kqp.awaken.network.world;

import com.kqp.awaken.client.AwakenClientLevelData;
import com.kqp.awaken.network.AwakenPacketS2C;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public class SyncLevelDataS2C extends AwakenPacketS2C {
    public SyncLevelDataS2C() {
        super("sync_level_data_s2c");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        CompoundTag awakenLevelDataTag = data.readCompoundTag();

        context.getTaskQueue().execute(() -> {
            AwakenClientLevelData.INSTANCE.setAwakenServerLevelData(new AwakenLevelData(awakenLevelDataTag));
        });
    }
}
