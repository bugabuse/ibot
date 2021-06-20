package com.farm.scripts.combat;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.Equipment.Slot;
import com.farm.ibot.api.world.area.PolygonArea;
import com.farm.ibot.api.world.area.RadiusArea;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

public class Settings {
    public static final Item[] RUNES = new Item[]{new Item(ItemDefinition.forName("Mind rune").getUnnotedId(), 2000), new Item(ItemDefinition.forName("Water rune").getUnnotedId(), 400), new Item(ItemDefinition.forName("Earth rune").getUnnotedId(), 700), new Item(ItemDefinition.forName("Fire rune").getUnnotedId(), 6000)};
    public static int FOOD_ID = 329;
    public static AttackSpot SPOT_GOBLINS;
    public static AttackSpot SPOT_CHICKENS;
    public static AttackSpot SPOT_AL_KHARID;
    public static AttackSpot SPOT_GIANT_FROGS;
    public static AttackSpot SPOT_COW;
    public static AttackSpot SPOT_HOBGOBLIN;
    private static EquipmentItem[] equipmentItemsMagic;
    private static EquipmentItem[] equipmentItemsMelee;

    static {
        equipmentItemsMagic = new EquipmentItem[]{(new EquipmentItem("Iron kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 1), (new EquipmentItem("Steel kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 5), (new EquipmentItem("Mithril kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 20), (new EquipmentItem("Adamant kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 30), (new EquipmentItem("Rune kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 40), new EquipmentItem("Blue wizard hat", Slot.HELM), new EquipmentItem("Blue wizard robe", Slot.CHEST), new EquipmentItem("Blue skirt", Slot.LEGS), new EquipmentItem("Staff of air", Slot.SWORD), new EquipmentItem("Amulet of magic", Slot.AMULET), new EquipmentItem("Red cape", Slot.CAPE)};
        equipmentItemsMelee = new EquipmentItem[]{(new EquipmentItem("Iron full helm", Slot.HELM)).level(Skill.DEFENSE, 1), (new EquipmentItem("Steel full helm", Slot.HELM)).level(Skill.DEFENSE, 5), (new EquipmentItem("Mithril full helm", Slot.HELM)).level(Skill.DEFENSE, 20), (new EquipmentItem("Adamant full helm", Slot.HELM)).level(Skill.DEFENSE, 30), (new EquipmentItem("Rune full helm", Slot.HELM)).level(Skill.DEFENSE, 40), (new EquipmentItem("Iron platebody", Slot.CHEST)).level(Skill.DEFENSE, 1), (new EquipmentItem("Steel platebody", Slot.CHEST)).level(Skill.DEFENSE, 5), (new EquipmentItem("Mithril platebody", Slot.CHEST)).level(Skill.DEFENSE, 20), (new EquipmentItem("Adamant platebody", Slot.CHEST)).level(Skill.DEFENSE, 30), (new EquipmentItem("Iron platelegs", Slot.LEGS)).level(Skill.DEFENSE, 1), (new EquipmentItem("Steel platelegs", Slot.LEGS)).level(Skill.DEFENSE, 5), (new EquipmentItem("Mithril platelegs", Slot.LEGS)).level(Skill.DEFENSE, 20), (new EquipmentItem("Adamant platelegs", Slot.LEGS)).level(Skill.DEFENSE, 30), (new EquipmentItem("Rune platelegs", Slot.LEGS)).level(Skill.DEFENSE, 40), (new EquipmentItem("Iron kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 1), (new EquipmentItem("Steel kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 5), (new EquipmentItem("Mithril kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 20), (new EquipmentItem("Adamant kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 30), (new EquipmentItem("Rune kiteshield", Slot.SHIELD)).level(Skill.DEFENSE, 40), (new EquipmentItem("Iron scimitar", Slot.SWORD)).level(Skill.ATTACK, 1), (new EquipmentItem("Steel scimitar", Slot.SWORD)).level(Skill.ATTACK, 5), (new EquipmentItem("Mithril scimitar", Slot.SWORD)).level(Skill.ATTACK, 20), (new EquipmentItem("Adamant scimitar", Slot.SWORD)).level(Skill.ATTACK, 30), (new EquipmentItem("Rune scimitar", Slot.SWORD)).level(Skill.ATTACK, 40), new EquipmentItem("Amulet of strength", Slot.AMULET), new EquipmentItem("Red cape", Slot.CAPE)};
        SPOT_GOBLINS = (new AttackSpot()).area(new RadiusArea(new Tile(3248, 3237, 0), 20)).npcs("Goblin");
        SPOT_CHICKENS = (new AttackSpot()).area(new RadiusArea(new Tile(3231, 3297, 0), 10)).npcs("Chicken");
        SPOT_AL_KHARID = (new AttackSpot()).area(new RadiusArea(new Tile(3293, 3174, 0), 20)).npcs("Al-Kharid warrior").walkToUnreachable(true).multiCombat(true);
        SPOT_GIANT_FROGS = (new AttackSpot()).area(new RadiusArea(new Tile(3199, 3187, 0), 25)).npcs("Giant frog", "Big frog").walkToUnreachable(true);
        SPOT_COW = (new AttackSpot()).area(new PolygonArea(new Tile[]{new Tile(3178, 3317, 0), new Tile(3183, 3314, 0), new Tile(3192, 3308, 0), new Tile(3199, 3309, 0), new Tile(3202, 3322, 0), new Tile(3199, 3333, 0), new Tile(3187, 3339, 0), new Tile(3176, 3341, 0), new Tile(3160, 3346, 0), new Tile(3156, 3345, 0), new Tile(3153, 3335, 0), new Tile(3152, 3324, 0), new Tile(3157, 3315, 0), new Tile(3169, 3318, 0), new Tile(3176, 3316, 0)})).npcs("Cow", "Cow calf");
        SPOT_HOBGOBLIN = (new AttackSpot()).area(new PolygonArea(new Tile[]{new Tile(2922, 3269, 0), new Tile(2910, 3293, 0), new Tile(2907, 3298, 0), new Tile(2905, 3299, 0), new Tile(2905, 3299, 0), new Tile(2904, 3297, 0), new Tile(2904, 3293, 0), new Tile(2904, 3290, 0), new Tile(2908, 3281, 0), new Tile(2915, 3269, 0), new Tile(2921, 3265, 0), new Tile(2918, 3257, 0), new Tile(2920, 3255, 0), new Tile(2924, 3259, 0)})).npcs("Hobgoblin");
    }

    public static AttackSpot getSpot() {
        return Skill.DEFENSE.getRealLevel() > 20 ? SPOT_HOBGOBLIN : SPOT_COW;
    }

    public static EquipmentItem[] getEquipmentItems() {
        return usingMagic() ? equipmentItemsMagic : equipmentItemsMelee;
    }

    public static boolean usingMagic() {
        return Combat.get().getStartArguments().contains("magic");
    }
}
