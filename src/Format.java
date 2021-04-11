/*
 * Copyright (c) 2020, 2021 Daylam Tayari <daylam@tayari.gg>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License version 3as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * @author Daylam Tayari daylam@tayari.gg https://github.com/daylamtayari
 * @version 1.0
 * Github project home page: https://github.com/TwitchRecover
 * Github repository: https://github.com/TwitchRecover/VOD-Domain-Retriever
 */

import java.util.ArrayList;

/**
 * The format class which handles the formatting
 * of the files into their desired output formats.
 */
public class Format {
    private static String format;   //Variable which represents the user's file format.

    /**
     * Mutator for the format variable.
     * @param fm    String value representing a file format.
     */
    protected static void setFormat(String fm){
        format=fm;
    }

    /**
     * This function formats an entire file according to the specifications
     * of a desired given file format.
     * @param domains               String arraylist representing all of the Twitch VOD domains.
     * @return ArrayList<String>    String arraylist containing the output contents, with each entry representing a line.
     */
    protected static ArrayList<String> format(ArrayList<String> domains){
        ArrayList<String> output=new ArrayList<String>();
        if(format.equals("csv")){
            output.add("Domains");
            output.addAll(formatAppend(domains));
        }
        else if(format.equals("json")){
            String json="{\"domains\":["+formatAppend(domains).get(0)+"]}";
        }
        else if(format.equals("txt")){
            output.add("-------------------------------------------------------------------------------------------------------------------------");
            output.add("# Twitch VOD Domain List:");
            output.add("# Generated using Twitch-VDR - https://github.com/TwitchRecover/Twitch-VDR");
            output.add("# Copyright Daylam Tayari 2021. Licensed under GPL-3.");
            output.add("# You can help support me financially by donating to the following:");
            output.add("# paypal.me/daylamtayari $daylamtayari BTC: 15KcKrsqW6DQdyZPrgRXXmsKkyyZzHAQVX");
            output.add("-------------------------------------------------------------------------------------------------------------------------");
            output.addAll(formatAppend(domains));
        }
        return output;
    }

    /**
     * This function formats the domains to add into the correct file format,
     * with the intention for use to be appended to an existing file/list.
     * @param domains               String arraylist representing all of the Twitch VOD domains.
     * @return ArrayList<String>    String arraylist containing the output contents, with each entry representing a line.
     */
    protected static ArrayList<String> formatAppend(ArrayList<String> domains){
        ArrayList<String> output=new ArrayList<String>();
        if(format.equals("csv") || format.equals("txt")){
            output.addAll(domains);
        }
        else if(format.equals("json")){
            String jsonAddition="";
            for(String s: domains){
                jsonAddition+=", \""+s+"\"";
            }
            output.add(jsonAddition);
        }
        return output;
    }
}