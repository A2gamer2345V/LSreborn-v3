package com.A2plugin.lsreborn.events.death;

import com.A2plugin.lsreborn.events.ZPlayerDeathEventBase;
import com.A2plugin.lsreborn.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ZPlayerHeartGainCooldownEvent extends ZPlayerDeathEventBase {
    @Getter
    private final Player killer;
    
    @Getter
    private final long timeLeftOnCooldown;
    
    @Getter @Setter
    private boolean shouldDropHeartsInstead;
    
    @Getter @Setter
    private Component cooldownMessage;

    public ZPlayerHeartGainCooldownEvent(PlayerDeathEvent originalEvent, Player killer, long timeLeft) {
        super(originalEvent);
        this.killer = killer;
        this.timeLeftOnCooldown = timeLeft;
        this.shouldDropHeartsInstead = false;
        this.cooldownMessage = MessageUtils.getAndFormatMsg(
                false,
                "heartGainCooldown",
                "You have to wait before gaining another heart!",
                new MessageUtils.Replaceable("%time%", MessageUtils.formatTime(timeLeft))
        );
    }
}
