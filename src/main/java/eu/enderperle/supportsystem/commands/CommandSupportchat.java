package eu.enderperle.supportsystem.commands;

import eu.enderperle.supportsystem.utils.SupportManager;
import eu.enderperle.supportsystem.utils.Var;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandSupportchat extends Command {

    public CommandSupportchat(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(args.length > 0)
            {
                String msg = "";
                for(int i = 1; i < args.length; i++)
                {
                    msg = msg+args[i]+" ";
                }
                if(SupportManager.getSessionFromSupporter(p) != null)
                {
                    SupportManager.getSessionFromSupporter(p).getSupported().sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§a"+p.getName()+"§8: §7"+msg));
                    for (ProxiedPlayer all : SupportManager.getSessionFromSupporter(p).getSupporting()) {
                        all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§a"+p.getName()+"§8: §7"+msg));
                    }
                }
                else if (SupportManager.getSessionFromSupported(p) != null)
                {
                    SupportManager.getSessionFromSupported(p).getSupported().sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§a"+p.getName()+"§8: §7"+msg));
                    for (ProxiedPlayer all : SupportManager.getSessionFromSupported(p).getSupporting()) {
                        all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§a"+p.getName()+"§8: §7"+msg));
                    }
                }else {
                    p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDu bist in keiner Supportsitzung."));
                }
            }else {
                p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Nutze §8» §c/sc <Nachricht>"));
            }
        }
    }
}
