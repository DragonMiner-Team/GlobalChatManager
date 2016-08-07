import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionGroup;

import static ru.tehkode.permissions.bukkit.PermissionsEx.getUser;

public class Main extends JavaPlugin implements Listener
{
    Main                    plugin      = this;
    FileConfiguration       config      = plugin.getConfig();
    HashMap<Player, String> playerchats = new HashMap();
    List<Player>            censorlist  = new ArrayList();
    Util                    util;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("gcm"))
        {
            if(args.length == 0)
            {
                return false;
            }

            if(args[0].equalsIgnoreCase("list"))
            {
                sender.sendMessage(ChatColor.YELLOW + "List of all chat types:");

                for(final String chat : config.getKeys(false))
                {
                    if(!chat.equals("resetconfig"))
                    {
                        sender.sendMessage(ChatColor.GREEN + " - " + util.capitalize(chat));
                    }
                }

                return true;
            }

            if(args[0].equalsIgnoreCase("censor"))
            {
                if(args.length != 1)
                {
                    return false;
                }

                if(censorlist.contains((Player) sender))
                {
                    sender.sendMessage(util.colorize(config.getString("censor.message")
                                                           .replace("%bool%", ChatColor.RED + "No Longer")));
                    censorlist.remove((Player) sender);

                    return true;
                }
                else
                {
                    sender.sendMessage(util.colorize(config.getString("censor.message")
                                                           .replace("%bool%", ChatColor.GREEN + "Now")));
                    sender.sendMessage(util.colorize(config.getString("censor.disclaimer")));
                    censorlist.add((Player) sender);

                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("me"))
            {
                if(args.length != 1)
                {
                    return false;
                }

                String[] lines = { "Name: " + sender.getName(), "Custom Name: " + ((Player) sender).getCustomName(),
                                   "Display Name: " + ((Player) sender).getDisplayName(),
                                   "Player List: " + ((Player) sender).getPlayerListName(),
                                   "As String: " + sender.toString() };

                sender.sendMessage(lines);

                return true;
            }

            if(args[0].equalsIgnoreCase("players"))
            {
                if(!config.getBoolean("consoleonly") &&!((Player) sender).hasPermission("gcm.players"))
                {
                    ((Player) sender).sendMessage(ChatColor.RED + "You do not have permission to use this!");

                    return true;
                }

                if(config.getBoolean("consoleonly") && (sender instanceof Player))
                {
                    ((Player) sender).sendMessage(ChatColor.RED
                                                  + "This command may only be executed from the console.");

                    return true;
                }

                if(args[1].equalsIgnoreCase("hasPerms"))
                {
                    if(args.length != 4)
                    {
                        return false;
                    }

                    sender.sendMessage(String.valueOf(util.isAllowed(Bukkit.getPlayer(args[2]), args[3])));

                    return true;
                }

                if(args[1].equalsIgnoreCase("add"))
                {
                    if(args.length < 4)
                    {
                        return false;
                    }

                    if(!config.getBoolean("consoleonly") &&!((Player) sender).hasPermission("gcm.players.add"))
                    {
                        ((Player) sender).sendMessage(ChatColor.RED + "You do not have permission to use this!");

                        return true;
                    }

                    if(!config.contains(args[3].toLowerCase()))
                    {
                        sender.sendMessage(ChatColor.RED + "Could not find chat \""
                                           + util.capitalize(args[3].toLowerCase()) + "\"");

                        return true;
                    }

                    List<String> players = config.getStringList(args[3].toLowerCase() + ".allowedplayers");

                    players.add(args[2]);
                    config.set(args[3].toLowerCase() + ".allowedplayers", players);
                    saveConfig();
                    sender.sendMessage(ChatColor.GREEN + args[2] + " is now allowed to talk in the "
                                       + util.capitalize(args[3].toLowerCase()) + " Chat.");

                    return true;
                }

                if(args[1].equalsIgnoreCase("remove"))
                {
                    if(args.length < 4)
                    {
                        return false;
                    }

                    if(!((Player) sender).hasPermission("gcm.players.remove"))
                    {
                        ((Player) sender).sendMessage(ChatColor.RED + "You do not have permission to use this!");

                        return true;
                    }

                    if(!config.contains(args[3].toLowerCase()))
                    {
                        sender.sendMessage(ChatColor.RED + "Could not find chat \""
                                           + util.capitalize(args[3].toLowerCase()) + "\"");

                        return true;
                    }

                    List<String> players = config.getStringList(args[3].toLowerCase() + ".allowedplayers");

                    if(!players.contains(args[2]))
                    {
                        sender.sendMessage(ChatColor.RED + "The player \"" + args[2]
                                           + "\" is not allowed to talk in chat \""
                                           + util.capitalize(args[3].toLowerCase()) + "\"");

                        return true;
                    }

                    players.remove(args[2]);
                    config.set(args[3].toLowerCase() + ".allowedplayers", players);
                    saveConfig();
                    sender.sendMessage(ChatColor.GREEN + args[2] + " is no longer allowed to talk in the "
                                       + util.capitalize(args[3].toLowerCase()) + " Chat.");

                    return true;
                }
            }

            if(config.contains(args[0].toLowerCase()))
            {
                if(!util.isAllowed((Player) sender, args[0].toLowerCase()))
                {
                    ((Player) sender).sendMessage(ChatColor.RED + "You do not have permission to use this!");

                    return true;
                }

                if(args.length == 1)
                {
                    if(playerchats.containsKey((Player) sender))
                    {
                        ((Player) sender).sendMessage(ChatColor.GOLD
                                                      + util.capitalize(playerchats.get((Player) sender)) + " Chat: "
                                                      + ChatColor.RED + "Disabled!");
                        playerchats.remove((Player) sender);

                        return true;
                    }
                    else
                    {
                        playerchats.put((Player) sender, args[0].toLowerCase());
                        ((Player) sender).sendMessage(ChatColor.GOLD + util.capitalize(args[0]) + " Chat: "
                                                      + ChatColor.GREEN + "Enabled!");

                        return true;
                    }
                }
                else
                {
                    util.chatCastMessage((Player) sender,
                                         StringUtils.join(ArrayUtils.subarray(args, 1, args.length)),
                                         args[0]);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onDisable()
    {
        playerchats.clear();
    }

    @Override
    public void onEnable()
    {
        plugin.util = new Util(plugin);
        playerchats.clear();
        getServer().getPluginManager().registerEvents(this, this);

        if(!config.contains("resetconfig") || config.getBoolean("resetconfig"))
        {
            config.set("resetconfig", false);

            //
            config.set("censor.message", "&bChat Censoring is %bool% &bactive.");
            config.set("censor.disclaimer",
                       "&7Note: Chat Censoring is not perfect and we cannot guarantee all words will be correctly censored.");
            config.set("censor.words", Arrays.asList("fuck", "shit", "hell", "..."));

            //
            config.set("chat.formatting", "&f<%prefix% &b%nickname% &f%suffix%&f> %message%");

            //
            config.set("builder.allowedplayers", new ArrayList());
            config.set("builder.formatting", "&1[&9B %player%&1] &f%message%");

            //
            config.set("developer.allowedplayers", new ArrayList());
            config.set("developer.formatting", "&6[&eD %player%&6] &8<&c%message%&8>");
            saveConfig();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();

        if(playerchats.containsKey(player))
        {
            if(util.isAllowed(player, playerchats.get(player)))
            {
                util.chatCastMessage(player, e.getMessage(), playerchats.get(player));
                e.setCancelled(true);

                return;
            }
        }

        if(e.getMessage().startsWith("/"))
        {
            getServer().dispatchCommand((CommandSender) player, e.getMessage());
            e.setCancelled(true);

            return;
        }

        String groupPrefix = null;
        String groupSuffix = null;

        for(PermissionGroup g : getUser(player).getGroups())
        {
            groupPrefix = g.getPrefix();
            groupSuffix = g.getSuffix();
        }

        String formatting = config.getString("chat.formatting")
                                  .replace("%prefix%", groupPrefix)
                                  .replace("%player%", player.getName())
                                  .replace("%nickname%", player.getDisplayName())
                                  .replace("%suffix%", groupSuffix)
                                  .replace("%message%", e.getMessage());

        util.gCastMessage(util.colorize(formatting));
        e.setCancelled(true);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
