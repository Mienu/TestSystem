package eu.enderperle.supportsystem.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SupportSession {

    private final ProxiedPlayer supported;
    private final Set<ProxiedPlayer> supporting = new HashSet<>();
    private final Set<ProxiedPlayer> invited = new HashSet<>();

    public SupportSession(final ProxiedPlayer supported) {
        this.supported = supported;
        ProxyServer.getInstance().getScheduler().schedule(SupportSystem.getInstance(), () -> {
            if (supported != null && supporting.size() == 0) {
                if (SupportManager.removeSession(this)) {
                    this.supported.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§cDer Supportchat wurde automatisch geschlossen, da ihn kein Supporter angenommen hat."));
                }
            }
        }, 15, TimeUnit.MINUTES);
    }

    public boolean addSupporter(ProxiedPlayer supporter) {
        if(supporter == null) return false;
        if (!this.supporting.contains(supporter)) {
            this.supporting.add(supporter);
            return true;
        }
        return false;
    }

    public boolean removeSupporter(final ProxiedPlayer supporter) {
        if(supporter == null) return false;
        if (this.supporting.contains(supporter)) {
            this.supporting.remove(supporter);
            return true;
        }
        return false;
    }

    public boolean addInvited(final ProxiedPlayer invited) {
        if(invited == null) return false;
        if (!this.invited.contains(invited)) {
            this.invited.remove(invited);
            return true;
        }
        return false;
    }

    public boolean removeInvited(final ProxiedPlayer invited) {
        if(invited == null) return false;
        if (this.invited.contains(invited)) {
            this.invited.remove(invited);
            return true;
        }
        return false;
    }

    public ProxiedPlayer getSupported() {
        return this.supported;
    }

    public Set<ProxiedPlayer> getSupporting() {
        return this.supporting;
    }

    public Set<ProxiedPlayer> getInvited() {
        return this.invited;
    }
}
