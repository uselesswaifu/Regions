package ru.nukkit.regions.commands.regions;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

import java.util.Map;

@CmdDefine(command = "region", alias = "rg", subCommands = {"info|i"}, defaultPerm = Permission.DEFAULT_TRUE, permission = "regions.info", description = Message.RG_CHK_DESC)
public class CmdRgInfo extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (args.length > 1) return false;
        Map<String, Region> regions = Regions.getManager().getRegions(player.getLocation());
        if (regions.isEmpty()) return Message.RG_CHK_NOTFOUND.print(player);
        if (regions.size() == 1) {
            String id = regions.keySet().toArray(new String[1])[0];
            Region region = regions.get(id);
            Message.RG_INFO_REGNAME.print(sender, id);
            Message.RG_INFO_COORD.print(sender, region.getDimension());
            if (!region.getOwners().isEmpty())
                Message.RG_INFO_OWNERS.print(sender, StringUtil.listToString(region.getOwners()));
            if (!region.getMembers().isEmpty())
                Message.RG_INFO_MEMBERS.print(sender, StringUtil.listToString(region.getMembers()));
            if (!region.getFlags().isEmpty())
                for (Flag f : region.getFlags())
                    sender.sendMessage(TextFormat.colorize("&a" + f.toString()));
            return true;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String id : regions.keySet()) {
                Region r = regions.get(id);
                TextFormat color = TextFormat.GREEN;
                if (player != null) {
                    if (r.isOwner(player.getName())) color = TextFormat.GOLD;
                    else if (r.isMember(player.getName())) color = TextFormat.YELLOW;
                }
                if (sb.length() > 0) {
                    if (color != TextFormat.GREEN) sb.append(TextFormat.GREEN);
                    sb.append(", ");
                }
                sb.append(color).append(id);
            }
            return Message.RG_CHK_FOUND.print(player, sb.toString());
        }
    }
}
