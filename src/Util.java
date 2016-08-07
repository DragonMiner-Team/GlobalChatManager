import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPlayer;

import net.md_5.bungee.api.ChatColor;

public class Util
{
    Main plugin;

    Util(Main thisplugin)
    {
        plugin = thisplugin;
    }

    public String capitalize(String string)
    {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public String capitalize(String[] strings)
    {
        List<String> outs = new ArrayList();

        for(final String string : strings)
        {
            outs.add(string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());
        }

        return StringUtils.join(outs, " ");
    }

    public String censor(String string)
    {
        String out = "";

        for(int i = 0; i < string.length(); i++)
        {
            String oldout = out;

            out = oldout + "*";
        }

        return out;
    }

    public void chatCastMessage(Player sender, String message, String chatName)
    {
        for(final Player p : plugin.getServer().getOnlinePlayers())
        {
            if(isAllowed(p, chatName))
            {
                String msg = message;

                if(plugin.censorlist.contains(p))
                {
                    for(String word : plugin.config.getStringList("censor.words"))
                    {
                        for(final String thisword : message.split(" "))
                        {
                            if(thisword.equalsIgnoreCase(word))
                            {
                                msg = msg.replace(thisword, censor(word));
                            }
                        }
                    }
                }

                p.sendMessage(colorize(plugin.config.getString(chatName + ".formatting")
                                                    .replace("%message%", msg)
                                                    .replace("%player%", sender.getName())));
            }
        }
    }

    public String colorize(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void gCastMessage(String string)
    {
        for(final Player p : plugin.getServer().getOnlinePlayers())
        {
            String   msg   = string;
            String[] words = string.split(" ");

            if(plugin.censorlist.contains(p))
            {
                for(String word : plugin.config.getStringList("censor.words"))
                {
                    for(final String thisword : words)
                    {
                        if(thisword.equalsIgnoreCase(word))
                        {
                            msg = msg.replace(thisword, censor(word));
                        }
                    }
                }

                p.sendMessage(colorize(msg));
            }
            else
            {
                p.sendMessage(colorize(string));
            }
        }
    }

    public boolean isAllowed(Player player, String chatName)
    {
        return plugin.config.getStringList(chatName + ".allowedplayers").contains(player.getName());
    }

    public List<Player> getAllowedPlayers(String chatName)
    {
        List<Player> players = new ArrayList();

        for(final String pname : plugin.config.getStringList(chatName + ".allowedplayers"))
        {
            players.add(getPlayer(pname));
        }

        return players;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
