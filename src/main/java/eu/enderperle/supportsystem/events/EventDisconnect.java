package eu.enderperle.supportsystem.events;

import eu.enderperle.supportsystem.utils.SupportManager;
import eu.enderperle.supportsystem.utils.Var;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventDisconnect implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e)
    {
        ProxiedPlayer p = e.getPlayer();
        if(SupportManager.hasSession(p))
        {
            SupportManager.getSessionFromSupported(p).getSupporting().forEach(all -> {
                all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§6"+p.getName()+" §7hat die Supportsitzung §cverlassen§7."));
                all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDie Supportsitzung wurde geschlossen."));
            });
            SupportManager.removeSession(SupportManager.getSessionFromSupported(p));
        }
        if(SupportManager.getSessionFromSupporter(p) != null)
        {
            if(SupportManager.getSessionFromSupported(p).getSupporting().size() <= 1)
            {
                SupportManager.getSessionFromSupporter(p).getSupported().sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDie Supportsitzung wurde geschlossen, da kein Supporter mehr an ihr teilnimmt."));
                SupportManager.removeSession(SupportManager.getSessionFromSupporter(p));
            }else {
                SupportManager.getSessionFromSupporter(p).removeSupporter(p);
                SupportManager.getSessionFromSupporter(p).getSupporting().forEach(all -> {
                    all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§6"+p.getName()+" §7hat die Supportsitzung §cverlassen§7."));
                });
            }
        }
        if(SupportManager.getSessionFromInvited(p) != null)
        {
            SupportManager.getSessionFromInvited(p).removeInvited(p);
        }

    }

}
