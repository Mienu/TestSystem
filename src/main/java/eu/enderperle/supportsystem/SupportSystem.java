package eu.enderperle.supportsystem;

import eu.enderperle.supportsystem.commands.CommandSupport;
import eu.enderperle.supportsystem.commands.CommandSupportchat;
import eu.enderperle.supportsystem.events.EventDisconnect;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class SupportSystem extends Plugin {

    private static SupportSystem instance;

    @Override
    public void onEnable() {
        instance = this;
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandSupport("support"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandSupportchat("sc"));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new EventDisconnect());
        ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("§9SupportSystem §8>> §aDas Plugin wurde erfolgreich aktiviert!"));
    }

    @Override
    public void onDisable() {

    }

    public static SupportSystem getInstance()
    {
        return instance;
    }

}
