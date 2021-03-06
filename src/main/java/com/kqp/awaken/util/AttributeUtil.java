package com.kqp.awaken.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.Random;

public class AttributeUtil {
    public static double applyAttribute(EntityAttributeInstance attributeInstance, double val) {
        attributeInstance.setBaseValue(val);
        return attributeInstance.getValue();
    }

    public static int applyAttribute(EntityAttributeInstance attributeInstance, int val, Random r) {
        attributeInstance.setBaseValue(1D);

        if (r.nextFloat() < attributeInstance.getValue() - 1D) {
            val *= 2;
        }

        return val;
    }

    public static boolean rollAttribute(EntityAttributeInstance attributeInstance, Random r) {
        attributeInstance.setBaseValue(0D);

        return r.nextDouble() < attributeInstance.getValue();
    }

    @Environment(EnvType.CLIENT)
    public static String toTooltip(EntityAttribute attrib, EntityAttributeModifier mod) {
        String translKey = attrib.getTranslationKey();

        String name = translateOr(translKey + ".tooltip.name", translKey);

        String sign = mod.getValue() > 0 ? "+" : "-";
        double value = Math.abs(mod.getValue());

        String operation = mod.getOperation() == EntityAttributeModifier.Operation.ADDITION ? "addition" : "multiply";

        String tooltip = translateOr(
                translKey + ".tooltip." + operation,
                "attribute.default.tooltip." + operation
        );

        tooltip = tooltip.replace("${sign}", sign);
        tooltip = tooltip.replace("${value}", formatDouble(value));
        tooltip = tooltip.replace("${value*100}", formatDouble(value * 100));
        tooltip = tooltip.replace("${value*10}", formatDouble(value * 10));
        tooltip = tooltip.replace("${value/20}", formatDouble(value / 20D));
        tooltip = tooltip.replace("${perc}", "%");
        tooltip = tooltip.replace("${name}", name);

        return tooltip;
    }

    @Environment(EnvType.CLIENT)
    private static String translateOr(String key, String backupKey) {
        if (I18n.hasTranslation(key)) {
            return I18n.translate(key);
        }

        return I18n.translate(backupKey);
    }

    @Environment(EnvType.CLIENT)
    private static String formatDouble(double value) {
        return String.format("%.1f", value).replace(".0", "");
    }
}
