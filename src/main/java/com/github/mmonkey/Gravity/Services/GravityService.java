package com.github.mmonkey.Gravity.Services;

import com.github.mmonkey.Gravity.Gravity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.scheduler.SchedulerService;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.world.World;

import java.util.Collection;

public class GravityService {

    private Gravity plugin;
    private SchedulerService schedulerService;

    public void setGravity() {
        this.schedulerService.createTaskBuilder().async().interval(1).name("Gravity calculator").execute(new Runnable() {
                 public void run() {
                     updateGravity();
                 }
             }
        );
    }

    private void updateGravity() {

        Collection<World> worlds = this.plugin.getGame().getServer().getWorlds();
        for (World world : worlds) {

            Collection<Entity> entities = world.getEntities();
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    ((Player) entity).sendMessage(Texts.of("updating gravity"));
                }
            }
        }
    }

    public GravityService(Gravity plugin) {
        this.plugin = plugin;
        this.schedulerService = plugin.getGame().getScheduler();
    }
}
