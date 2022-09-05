package club.lavaer.moltenwar.functions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Loc30 implements Serializable {
    public final int x, y, z;
    public final String world;

    public Loc30(String world, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loc30 loc3D = (Loc30) o;

        if (x != loc3D.x) return false;
        if (y != loc3D.y) return false;
        if (z != loc3D.z) return false;
        return Objects.equals(world, loc3D.world);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (world != null ? world.hashCode() : 0);
        return result;
    }

    public Location toLocation() {
        World world = this.world == null ? null : Bukkit.getWorld(this.world);
        return new Location(world, x, y, z);
    }

    public Location toUsableLocation() {
        Location loc = toLocation();
        if (loc.getWorld() == null) {
            throw new NoSuchElementException("No world `" + world + "` found.");
        }
        return loc;
    }

    public static @NotNull Loc30 from(@NotNull Location location) {
        World world = location.getWorld();
        String worldName = world == null ? null : world.getName();
        return new Loc30(worldName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}