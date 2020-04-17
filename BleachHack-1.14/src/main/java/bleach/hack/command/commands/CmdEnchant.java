package bleach.hack.command.commands;

import bleach.hack.command.Command;
import bleach.hack.utils.BleachLogger;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.registry.Registry;

public class CmdEnchant extends Command {

	@Override
	public String getAlias() {
		return "enchant";
	}

	@Override
	public String getDescription() {
		return "Enchants an item";
	}

	@Override
	public String getSyntax() {
		return ".enchant [enchant] [level] / .enchant all [level] / .enchant list";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args[0].equalsIgnoreCase("list")) {
			BleachLogger.infoMessage("�d[Aqua_Affinity/Aqua] �5[Arthropods] �d[Blast/Blast_Prot] "
					+ "�5[Channeling] �d[Curse_Binding/Binding] �5[Curse_Vanish/Vanish] �d[Depth_Strider/Strider] "
					+ "�5[Efficiency/Eff] �d[Feather_Falling/Fall] �5[Fire_Aspect] �d[Fire_Prot] "
					+ "�5[Flame] �d[Fortune] �5[Frost_Walker/Frost] �d[Impaling] �5[Infinity] �d[Knockback/Knock] "
					+ "�5[Looting/Loot] �d[Loyalty] �5[Luck_Of_The_Sea/Luck] �d[Lure] �5[Mending/Mend] �d[Multishot] "
					+ "�5[Piercing] �d[Power] �5[Projectile_Prot/Proj_Prot] �d[Protection/Prot] "
					+ "�5[Punch] �d[Quick_Charge/Charge] �5[Respiration/Resp] �d[Riptide] "
					+ "�5[Sharpness/Sharp] �d[Silk_Touch/Silk] �5[Smite] �d[Sweeping_Edge/Sweep] �5[Thorns] �d[Unbreaking]");
			return;
		}
		
		if(!mc.player.abilities.isCreativeMode) {
			BleachLogger.errorMessage("Not In Creative Mode!");
			return;
		}
		
		int level = Integer.parseInt(args[1]);
		ItemStack item = mc.player.inventory.getCurrentItem();
		
		if(args[0].equalsIgnoreCase("all")) {
			for(Enchantment e: Registry.ENCHANTMENT) {
				enchant(item, e, level);
			}
		}
		
		if(equals(args[0], "Aqua_Affinity", "Aqua")) enchant(item, Enchantments.AQUA_AFFINITY, level);
		if(equals(args[0], "Arthropods")) enchant(item, Enchantments.BANE_OF_ARTHROPODS, level);
		if(equals(args[0], "Blast", "Blast_Prot")) enchant(item, Enchantments.BLAST_PROTECTION, level);
		if(equals(args[0], "Channeling")) enchant(item, Enchantments.CHANNELING, level);
		if(equals(args[0], "Curse_Binding", "Binding")) enchant(item, Enchantments.BINDING_CURSE, level);
		if(equals(args[0], "Curse_Vanish", "Vanish")) enchant(item, Enchantments.VANISHING_CURSE, level);
		if(equals(args[0], "Depth_Strider", "Strider")) enchant(item, Enchantments.DEPTH_STRIDER, level);
		if(equals(args[0], "Efficiency", "Eff")) enchant(item, Enchantments.EFFICIENCY, level);
		if(equals(args[0], "Feather_Falling", "Fall")) enchant(item, Enchantments.FEATHER_FALLING, level);
		if(equals(args[0], "Fire_Aspect")) enchant(item, Enchantments.FIRE_ASPECT, level);
		if(equals(args[0], "Fire_Prot")) enchant(item, Enchantments.FIRE_PROTECTION, level);
		if(equals(args[0], "Flame")) enchant(item, Enchantments.FLAME, level);
		if(equals(args[0], "Fortune")) enchant(item, Enchantments.FORTUNE, level);
		if(equals(args[0], "Frost_Walker", "Frost")) enchant(item, Enchantments.FROST_WALKER, level);
		if(equals(args[0], "Impaling")) enchant(item, Enchantments.IMPALING, level);
		if(equals(args[0], "Infinity")) enchant(item, Enchantments.INFINITY, level);
		if(equals(args[0], "Knockback", "Knock")) enchant(item, Enchantments.KNOCKBACK, level);
		if(equals(args[0], "Looting", "Loot")) enchant(item, Enchantments.LOOTING, level);
		if(equals(args[0], "Loyalty")) enchant(item, Enchantments.LOYALTY, level);
		if(equals(args[0], "Luck_Of_The_Sea", "Luck")) enchant(item, Enchantments.LUCK_OF_THE_SEA, level);
		if(equals(args[0], "Lure")) enchant(item, Enchantments.LURE, level);
		if(equals(args[0], "Mending", "Mend")) enchant(item, Enchantments.MENDING, level);
		if(equals(args[0], "Multishot")) enchant(item, Enchantments.MULTISHOT, level);
		if(equals(args[0], "Piercing")) enchant(item, Enchantments.PIERCING, level);
		if(equals(args[0], "Power")) enchant(item, Enchantments.POWER, level);
		if(equals(args[0], "Projectile_Prot", "Proj_Prot")) enchant(item, Enchantments.PROJECTILE_PROTECTION, level);
		if(equals(args[0], "Protection", "Prot")) enchant(item, Enchantments.PROTECTION, level);
		if(equals(args[0], "Punch")) enchant(item, Enchantments.PUNCH, level);
		if(equals(args[0], "Quick_Charge", "Charge")) enchant(item, Enchantments.QUICK_CHARGE, level);
		if(equals(args[0], "Respiration", "Resp")) enchant(item, Enchantments.RESPIRATION, level);
		if(equals(args[0], "Riptide")) enchant(item, Enchantments.RIPTIDE, level);
		if(equals(args[0], "Sharpness", "Sharp")) enchant(item, Enchantments.SHARPNESS, level);
		if(equals(args[0], "Silk_Touch", "Silk")) enchant(item, Enchantments.SILK_TOUCH, level);
		if(equals(args[0], "Smite")) enchant(item, Enchantments.SMITE, level);
		if(equals(args[0], "Sweeping_Edge", "Sweep")) enchant(item, Enchantments.SWEEPING, level);
		if(equals(args[0], "Thorns")) enchant(item, Enchantments.THORNS, level);
		if(equals(args[0], "Unbreaking")) enchant(item, Enchantments.UNBREAKING, level);
	}
	
	public boolean equals(String a, String... b) {
		for(int i = 0; i < b.length; i++) {
			if(a.equalsIgnoreCase(b[i])) return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public void enchant(ItemStack item, Enchantment e, int level) {
		if (item.getTag() == null) item.setTag(new CompoundNBT());
		if (!item.getTag().contains("Enchantments", 9)) {
			item.getTag().put("Enchantments", new ListNBT());
	    }

	    ListNBT listnbt = item.getTag().getList("Enchantments", 10);
	    CompoundNBT compoundnbt = new CompoundNBT();
	    compoundnbt.putString("id", String.valueOf(Registry.ENCHANTMENT.getKey(e)));
	    compoundnbt.putInt("lvl", level);
	    listnbt.add(compoundnbt);
	}

}
