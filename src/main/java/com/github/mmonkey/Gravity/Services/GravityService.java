package com.github.mmonkey.Gravity.Services;

import com.flowpowered.math.vector.Vector3d;
import com.github.mmonkey.Gravity.Gravity;
import com.google.common.base.Optional;
import org.spongepowered.api.data.manipulator.mutable.entity.VelocityData;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.scheduler.SchedulerService;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GravityService {

    private Gravity plugin;
    private SchedulerService schedulerService;
    private HashMap<UUID, VelocityData> velocities = new HashMap<UUID, VelocityData>();
    private HashMap<UUID, Boolean> grounded = new HashMap<UUID, Boolean>();

    public void setGravity() {

        this.schedulerService.createTaskBuilder().interval(1).name("Gravity calculator").execute(new Runnable() {
             public void run() {
                 updateGravity();
             }
         }).submit(plugin);

    }

    private void updateGravity() {

        Collection<World> worlds = this.plugin.getGame().getServer().getWorlds();
        for (World world : worlds) {
            Collection<Entity> entities = world.getEntities();
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    //update velocity vector
                    this.updateVelocity(entity);
                }
            }
        }
    }

    private void updateVelocity(Entity entity) {

        VelocityData data = null;
        Optional<VelocityData> optional = entity.get(VelocityData.class);
        if (optional.isPresent()) {
            data = optional.get();
        }

        if (this.velocities.containsKey(entity.getUniqueId()) && this.grounded.containsKey(entity.getUniqueId()) && data != null && !isOnGround(entity)) {
            final Value<Vector3d> oldVelocityValue = this.velocities.get(entity.getUniqueId()).velocity();
            final Value<Vector3d> newVelocityValue = data.velocity();

            final double yDifference = oldVelocityValue.get().clone().sub(newVelocityValue.get()).getY();
            if (yDifference > 0.0 && newVelocityValue.get().getY() < -0.01 && newVelocityValue.get().getY() > 0.01) {
                double gravity = -1.0;
                double y = oldVelocityValue.get().getY() - yDifference * gravity;
                y = y > 4.0 ? 4.0 : y;
                y = y < -10.0 ? -10.0 : y;
                final Vector3d gravityVelocity = new Vector3d(newVelocityValue.get().getX(), y, newVelocityValue.get().getZ());
                entity.offer(optional.get().set(newVelocityValue.set(gravityVelocity)));
            }
        }

        if (data != null) {
            this.velocities.put(entity.getUniqueId(), data);
            this.grounded.put(entity.getUniqueId(), isOnGround(entity));
        }

    }

    private boolean isOnGround(Entity entity) {
        return entity.getLocation().getRelative(Direction.DOWN).getBlockType().isSolidCube();
    }

    public GravityService(Gravity plugin) {
        this.plugin = plugin;
        this.schedulerService = plugin.getGame().getScheduler();
    }
}
