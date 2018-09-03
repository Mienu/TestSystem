package eu.enderperle.supportsystem.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class SupportManager {

    private static final List<SupportSession> SESSIONS = new ArrayList<>();

    public static boolean addSupportSession(final ProxiedPlayer supported)
    {
        if(supported == null) return false;
        if(!hasSession(supported))
        {
            SupportSession session = new SupportSession(supported);
            SESSIONS.add(session);
            return true;
        }
        return false;
    }

    public static boolean hasSession(final ProxiedPlayer supported)
    {
        return SESSIONS.stream().anyMatch(s -> s.getSupported() == supported);
    }

    public static SupportSession getSessionFromSupporter(final ProxiedPlayer supporter)
    {
        return SESSIONS.stream().filter(s -> s.getSupporting().contains(supporter)).findAny().orElse(null);
    }

    public static SupportSession getSessionFromSupported(final ProxiedPlayer supported)
    {
        return SESSIONS.stream().filter(s -> s.getSupported() == supported).findAny().orElse(null);
    }

    public static SupportSession getSessionFromInvited(final ProxiedPlayer invited)
    {
        return SESSIONS.stream().filter(s -> s.getInvited() == invited).findAny().orElse(null);
    }

    public static boolean removeSession(SupportSession session)
    {
        if(session == null) return false;
        if(SESSIONS.contains(session))
        {
            SESSIONS.remove(session);
            session = null;
            return true;
        }
        return false;
    }

}
