package toast.client.command.commands;

import com.google.gson.JsonParser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import org.apache.commons.io.IOUtils;
import toast.client.command.Command;
import toast.client.utils.ToastLogger;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

public class CmdEntityStats extends Command {

    @Override
    public String getAlias() {
        return "estats";
    }

    @Override
    public String getDescription() {
        return "Gets stats of the entity you are riding";
    }

    @Override
    public String getSyntax() {
        return "estats";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (mc.player == null || !mc.player.hasVehicle()) return;

        if (mc.player.getVehicle() instanceof HorseBaseEntity) {
            HorseBaseEntity horse = (HorseBaseEntity) mc.player.getVehicle();
            float maxHealth = horse.getMaximumHealth();
            double speed = round(43.17 * horse.getMovementSpeed(), 2);
            double jump = round(-0.1817584952 * Math.pow(horse.getJumpStrength(), 3) + 3.689713992 * Math.pow(horse.getJumpStrength(), 2) + 2.128599134 * horse.getJumpStrength() - 0.343930367, 4);
            String builder;

            // TODO: find out what speed is in blocks per second
            try {
                String ownerId = horse.getOwnerUuid() == null ? "Not tamed." : horse.getOwnerUuid().toString();
                builder = "§6Entity (" + mc.player.getVehicle().getName().asString() + ") Statistics:" +
                        "\n§cMax Health: " + maxHealth +
                        "\n§cSpeed: " + speed +
                        "\n§cJump: " + jump + "blocks" +
                        "\n§cOwner: " + getNameFromUUID(ownerId).replace("\"", "");
            } catch (Throwable t) {
                builder = "§6Entity (" + mc.player.getVehicle().getName().asString() + ") Statistics:" +
                        "\n§cMax Health: " + maxHealth +
                        "\n§cSpeed: " + speed +
                        "\n§cJump: " + jump + "blocks";
            }
            ToastLogger.infoMessage(builder);
        } else if (mc.player.getVehicle() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) mc.player.getVehicle();
            ToastLogger.infoMessage("&6Entity (" + mc.player.getVehicle().getName().asString() + ") Stats:" +
                    "\n&cMax Health: &b" + entity.getMaximumHealth() + " &2HP" +
                    "\n&cSpeed: &b" + round(43.17 * entity.getMovementSpeed(), 2) + " &2m/s");
        } else {
            ToastLogger.errorMessage("&4&lError: &cNot riding a compatible entity.");
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getNameFromUUID(String uuid) {
        try {
            String jsonUrl = IOUtils.toString(new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names"));

            JsonParser parser = new JsonParser();

            return parser.parse(jsonUrl).getAsJsonArray().get(parser.parse(jsonUrl).getAsJsonArray().size() - 1).getAsJsonObject().get("name").toString();
        } catch (IOException ignored) { }

        return null;
    }

}
