package com.yj0524.TabCom;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class RouletteTabCom implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] arg) {

        List<String> result = new ArrayList<>();

        // 명령어 뒤에는 아무것도 띄우지 않음
        if (arg.length >= 0) {
            result.clear();
            return result;
        }

        return arguments;
    }
}