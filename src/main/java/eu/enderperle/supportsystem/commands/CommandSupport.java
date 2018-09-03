package eu.enderperle.supportsystem.commands;

import eu.enderperle.supportsystem.utils.SupportManager;
import eu.enderperle.supportsystem.utils.Var;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class CommandSupport extends Command {


    public CommandSupport(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if (args.length == 0) {
                if (p.hasPermission(Var.CALL_SUPPORT)) {
                    if (ProxyServer.getInstance().getPlayers().stream().anyMatch(all -> all.hasPermission(Var.SEE_SUPPORT))) {
                        if (SupportManager.addSupportSession(p)) {
                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Es wurde eine §aSupportsitzung §7für dich gestartet."));
                            p.sendMessage(TextComponent.fromLegacyText("§7Ein §cTeammitglied §7wird sich in Kürze um dich kümmern."));
                            ProxyServer.getInstance().getPlayers().stream().filter(all -> all.hasPermission(Var.SEE_SUPPORT)).forEach(all -> {
                                all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§6" + p.getName() + " §7benötigt §cHilfe§7."));
                                TextComponent comp = new TextComponent();
                                comp.addExtra("§7Klicke ");
                                TextComponent hier = new TextComponent();
                                hier.setText("§9§nhier");
                                hier.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptsupport " + p.getName()));
                                comp.addExtra(hier);
                                comp.addExtra(" §7um diesen Spieler anzunehmen.");
                                all.sendMessage(comp);
                            });
                        } else {
                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§cDu bist bereits in einer Supportsitzung!"));
                        }
                    } else {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§cEs ist zurzeit kein Supporter erreichbar."));
                        p.sendMessage(TextComponent.fromLegacyText("§7Melde dich in unserem §e§lTS3-Supportchannel §r§7oder nutze den §8#support §7Channel auf unserem §2§lDiscord§r§7."));
                    }
                } else {
                    p.sendMessage(TextComponent.fromLegacyText(Var.SYSTEM_PREFIX + "§cDu kannst diesen Befehl nicht nutzen!"));
                }
            } else if (args.length == 1) {
                if(args[0].equalsIgnoreCase("leave"))
                {
                    if(SupportManager.hasSession(p))
                    {
                        SupportManager.getSessionFromSupported(p).getSupporting().forEach(all -> {
                            all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§6"+p.getName()+" §7hat die Supportsitzung §cverlassen§7."));
                            all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDie Supportsitzung wurde geschlossen."));
                        });
                        SupportManager.removeSession(SupportManager.getSessionFromSupported(p));
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§7Du hast die Supportsitzung verlassen."));
                    }
                    else if(SupportManager.getSessionFromSupporter(p) != null)
                    {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§7Du hast die Supportsitzung verlassen."));
                        if(SupportManager.getSessionFromSupported(p).getSupporting().size() <= 1)
                        {
                            SupportManager.getSessionFromSupporter(p).getSupported().sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDie Supportsitzung wurde geschlossen, da kein Supporter mehr an ihr teilnimmt."));
                            SupportManager.removeSession(SupportManager.getSessionFromSupporter(p));
                        }else {
                            SupportManager.getSessionFromSupporter(p).removeSupporter(p);
                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§7Du hast die Supportsitzung verlassen."));
                            SupportManager.getSessionFromSupporter(p).getSupporting().forEach(all -> {
                                all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§6"+p.getName()+" §7hat die Supportsitzung §cverlassen§7."));
                            });
                        }
                    }else {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDu bist in keiner Supportsitzung"));
                    }
                }else if(args[0].equalsIgnoreCase("info"))
                {
                    if(SupportManager.hasSession(p))
                    {
                        String supporter = "";
                        for(ProxiedPlayer all : SupportManager.getSessionFromSupported(p).getSupporting()) {
                            supporter = "§7"+all.getName()+"§8, ";
                        }
                        supporter = supporter.substring(0, supporter.length()-4);
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§7Sitzungsmitglieder§8:"));
                        p.sendMessage(TextComponent.fromLegacyText("§6Erstellt von§8:"));
                        p.sendMessage(TextComponent.fromLegacyText("§8» §7"+p.getName()));
                        p.sendMessage(TextComponent.fromLegacyText("§cSupporter§8:"));
                        p.sendMessage(TextComponent.fromLegacyText("§8» §7"+supporter));
                    }
                    else if(SupportManager.getSessionFromSupporter(p) != null)
                    {
                        String supporter = "";
                        for(ProxiedPlayer all : SupportManager.getSessionFromSupporter(p).getSupporting()) {
                            supporter = "§7"+all.getName()+"§8, ";
                        }
                        supporter = supporter.substring(0, supporter.length()-4);
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§7Sitzungsmitglieder§8:"));
                        p.sendMessage(TextComponent.fromLegacyText("§6Erstellt von§8:"));
                        p.sendMessage(TextComponent.fromLegacyText("§8» §7"+SupportManager.getSessionFromSupporter(p).getSupported().getName()));
                        p.sendMessage(TextComponent.fromLegacyText("§cSupporter§8:"));
                        p.sendMessage(TextComponent.fromLegacyText("§8» §7"+supporter));
                    }else {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDu bist in keiner Supportsitzung"));
                    }
                }else {
                    if (p.hasPermission(Var.ADD_SUPPORT) || p.hasPermission(Var.SEE_SUPPORT)) {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Nutze §8» §c/support [<add|accept|leave> [<Spieler>]]"));
                    } else if (p.hasPermission(Var.CALL_SUPPORT)) {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Nutze §8» §c/support"));
                    } else {
                        p.sendMessage(TextComponent.fromLegacyText(Var.SYSTEM_PREFIX + "§cDu kannst diesen Befehl nicht nutzen!"));
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("accept")) {
                    if(p.hasPermission(Var.SEE_SUPPORT))
                    {
                        if(SupportManager.getSessionFromSupporter(p) != null)
                        {
                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDu bist bereits in einer Supportsitzung."));
                            return;
                        }
                        if (SupportManager.getSessionFromSupported(p) != null)
                        {
                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDu bist bereits in einer Supportsitzung."));
                            return;
                        }
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                        if (target != null) {
                            if(target != p)
                            {
                                if (SupportManager.getSessionFromSupported(target).getInvited().contains(p)) {
                                    if (SupportManager.hasSession(target)) {
                                        if (SupportManager.getSessionFromSupported(target).addSupporter(p)) {
                                            SupportManager.getSessionFromSupported(target).getInvited().remove(p);
                                            SupportManager.getSessionFromSupported(target).getSupporting().stream().filter(all -> all != p).forEach(all -> {
                                                all.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§a" + p.getName() + " §7ist der Sitzung §abeigetreten§7."));
                                            });
                                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Du bist der Sitzung von §6" + target.getName() + " §abeigetreten§7."));
                                        }
                                    } else {
                                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§cDieser Spieler braucht keine Hilfe."));
                                    }
                                } else {
                                    p.sendMessage(TextComponent.fromLegacyText(Var.SYSTEM_PREFIX + "§cDu kannst dieser Sitzung nicht beitreten."));
                                }
                            }else {
                                p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX+"§cDu kannst nicht mit dir selbst interagieren."));
                            }
                        } else {
                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§cDieser Spieler existiert nicht oder ist nicht online."));
                        }
                    }else {
                        p.sendMessage(TextComponent.fromLegacyText(Var.SYSTEM_PREFIX + "§cDu kannst diesen Befehl nicht nutzen!"));
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (p.hasPermission(Var.ADD_SUPPORT)) {
                        if (SupportManager.getSessionFromSupporter(p) != null) {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                            if (target != null) {
                                target.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Du wurdest gebeten, einer laufenden Supportsitzung von §6" + SupportManager.getSessionFromSupporter(p).getSupported().getName() + " §7beizutreten."));
                                TextComponent comp = new TextComponent();
                                comp.addExtra("§7Klicke ");
                                TextComponent hier = new TextComponent();
                                hier.setText("§9§nhier");
                                hier.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptsupport " + SupportManager.getSessionFromSupporter(p).getSupported().getName()));
                                comp.addExtra(hier);
                                comp.addExtra(" §7um der Sitzung beizutreten.");
                                target.sendMessage(comp);
                            } else {
                                p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§cDieser Spieler existiert nicht oder ist nicht online."));
                            }
                        } else {
                            p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§cDu bist in keiner Supportsession."));
                        }
                    } else {
                        p.sendMessage(TextComponent.fromLegacyText(Var.SYSTEM_PREFIX + "§cDu kannst diesen Befehl nicht nutzen!"));
                    }
                } else {
                    if (p.hasPermission(Var.ADD_SUPPORT) || p.hasPermission(Var.SEE_SUPPORT)) {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Nutze §8» §c/support [<add|accept|leave> [<Spieler>]]"));
                    } else if (p.hasPermission(Var.CALL_SUPPORT)) {
                        p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Nutze §8» §c/support"));
                    } else {
                        p.sendMessage(TextComponent.fromLegacyText(Var.SYSTEM_PREFIX + "§cDu kannst diesen Befehl nicht nutzen!"));
                    }
                }
            } else {
                if (p.hasPermission(Var.ADD_SUPPORT) || p.hasPermission(Var.SEE_SUPPORT)) {
                    p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Nutze §8» §c/support [<add|accept|leave> [<Spieler>]]"));
                } else if (p.hasPermission(Var.CALL_SUPPORT)) {
                    p.sendMessage(TextComponent.fromLegacyText(Var.PREFIX + "§7Nutze §8» §c/support"));
                } else {
                    p.sendMessage(TextComponent.fromLegacyText(Var.SYSTEM_PREFIX + "§cDu kannst diesen Befehl nicht nutzen!"));
                }
            }
        } else {
            commandSender.sendMessage(TextComponent.fromLegacyText("§9SupportSystem §8>> §cNur für Spieler!"));
        }
    }
}
