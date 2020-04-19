package toast.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public class EntityUtils {

	private static MinecraftClient mc = MinecraftClient.getInstance();

	public static boolean notSelf(Entity e) {
		return e != mc.player && e != mc.cameraEntity;
	}

	public static boolean isAnimal(Entity e) {
		return e instanceof AnimalEntity ||
				e instanceof AmbientEntity ||
				e instanceof WaterCreatureEntity ||
				(e instanceof GolemEntity && !((GolemEntity) e).isHandSwinging) ||
				e instanceof VillagerEntity;
	}

	public static boolean isLiving(Entity e) {
		return e instanceof LivingEntity;
	}

	public static boolean isHostile(Entity e) {
		return e instanceof HostileEntity ||
				(e instanceof ZombiePigmanEntity && ((ZombiePigmanEntity) e).isAngryAt(mc.player)) ||
				(e instanceof WolfEntity && ((WolfEntity) e).isAngry() && ((WolfEntity) e).getOwnerUuid() != mc.player.getUuid()) ||
				(e instanceof EndermanEntity && ((EndermanEntity) e).isAngry()) ||
				(e instanceof GolemEntity && ((GolemEntity) e).isHandSwinging);
	}

	public static boolean isNeutral(Entity e) {
		return (e instanceof ZombiePigmanEntity && !((ZombiePigmanEntity) e).isAngryAt(mc.player)) ||
				(e instanceof WolfEntity && (!((WolfEntity) e).isAngry() || ((WolfEntity) e).getOwnerUuid() == mc.player.getUuid())) ||
				(e instanceof EndermanEntity && !((EndermanEntity) e).isAngry()) ||
				(e instanceof GolemEntity && !((GolemEntity) e).isHandSwinging);
	}

	public static void setGlowing(Entity entity, Formatting color, String teamName) {
		Team team = mc.world.getScoreboard().getTeamNames().contains(teamName) ?
				mc.world.getScoreboard().getTeam(teamName) :
				mc.world.getScoreboard().addTeam(teamName);
        
		mc.world.getScoreboard().addPlayerToTeam(
				entity instanceof PlayerEntity ? entity.getEntityName() : entity.getUuidAsString(), team);
		mc.world.getScoreboard().getTeam(teamName).setColor(color);
		
		entity.setGlowing(true);
	}
	
	public static void facePos(double x, double y, double z) {
		double diffX = x - mc.player.getX();
		double diffY = y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
		double diffZ = z - mc.player.getZ();
			
		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
			
		float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
			
		mc.player.yaw += MathHelper.wrapDegrees(yaw - mc.player.yaw);
		mc.player.pitch += MathHelper.wrapDegrees(pitch - mc.player.pitch);
	}
}
