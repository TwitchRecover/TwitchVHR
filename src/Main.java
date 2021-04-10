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
 * Main class of the VOD retrieving program.
 */
public class Main {
    public static void main(String[] args){
        ArrayList<String> domains=getDomains();
    }

    /**
     * This function prints the help commands.
     */
    private static void help(){
        //TODO: Create the help output when I am finished designing the program.
    }

    /**
     * This function performs the create feature which
     * creates a new VOD domain list.
     */
    private static void create(){

    }

    /**
     * This function rebases an existing VOD domain list.
     */
    private static void rebase(){

    }

    /**
     * This function updates an existing VOD domain list.
     */
    private static void update(){

    }

    /**
     * This function prints the listing of all VOD domains.
     * @param domains   String arraylist of all VOD domains.
     */
    private static void list(ArrayList<String> domains){
        System.out.print("\nTwitch VOD Domains List:\n");
        for(String s: domains){
            System.out.print(s+"\n");
        }
    }

    /**
     * This retrieves all VOD domains, from both the list of
     * known domains and new ones found from current Twitch videos.
     * @return  ArrayList<String>   String arraylist containing the list of all Twitch VOD domains.
     */
    private static ArrayList<String> getDomains(){
        ArrayList<String> domains=new ArrayList<String>();
        domains.addAll(API.getDomains());
        for(String s: getTwitchDomains()){
            if(!domains.contains(s)){
                domains.add(s);
            }
        }
        return domains;
    }

    /**
     * This function retrieves and returns all of
     * the Twitch VOD domains retrieved from
     * the top Twitch videos.
     * @return ArrayList<String>    String arraylist containing a of unique Twitch VOD domain.
     */
    private static ArrayList<String> getTwitchDomains(){
        ArrayList<String> domains=new ArrayList<String>();
        for(int i=0;i<6;i++){
            for(String s: API.getVODDomains(i*100)){
                if(!domains.contains(s)){
                    domains.add(s);
                }
            }
        }
        return domains;
    }
}