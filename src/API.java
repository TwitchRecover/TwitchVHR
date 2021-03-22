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

/**
 * This class handles all of the API requests.
 */
public class API {
    //Constants:
    private static final String TWITCH_ACCEPT="application/vnd.twitchtv.v5+json";
    private static String WEB_CI;
    private static final int HTTP_OK=200;
    //Header constants:
    private static final String ACCEPT="Accept";
    private static final String CI="Client-ID";
    //Domain query constants:
    private static final String D="https://api.twitch.tv/top?";
    //Query constants:
    private static final String LIM="limit=100";
    private static final String OFF="offset=";
    private static final String PER="period=week";
    //Query constants:
    private static final String D_MAIN=D+LIM+"&"+PER;
    //Query parameters:
    private static String offset;
    private static String query;

    /**
     * Mutator for the offset query parameter.
     * @param offIn Integer value representing the offset value to set.
     */
    private static void setOffset(int offIn){
        offset=OFF+offIn;
    }

    /**
     * Sets the final query URL.
     */
    private static void setQuery(){
        query=D_MAIN+"&"+offset;
    }
}