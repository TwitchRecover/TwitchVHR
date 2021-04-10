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

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles all of the API and web requests.
 */
public class API {
    //Constants:
    private static String CI;
    private static final int HTTP_OK=200;

    /**
     * Mutator for the WEB_CI variable which represents
     * the client ID to use for Twitch API requests.
     * @param Ci    String value representing the client ID to use.
     */
    protected static void setCI(String Ci){
        CI=Ci;
    }

    /**
     * Web requests:
     */

    /**
     * This function returns the values of a simple
     * get call to a given URL.
     * @param url                   String value representing the URL to query.
     * @return ArrayList<String>    String arraylist which contains the entire response
     */
    private static ArrayList<String> webReq(String url){
        ArrayList<String> response=new ArrayList<String>();
        try{
            CloseableHttpClient httpClient=HttpClients.createDefault();
            HttpGet httpGet=new HttpGet(url);
            CloseableHttpResponse httpResponse=httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode()==HTTP_OK){
                BufferedReader br=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String line;
                while((line=br.readLine())!=null){
                    response.add(line);
                }
                br.close();
            }
            httpResponse.close();
            httpClient.close();
        }
        catch(Exception ignored){}
        return response;
    }

    /**
     * This function retrieves the client ID of Twitch
     * from a file in the Twitch Recover repository which tracks them.
     */
    private static void retrieveCI(){
        ArrayList<String> response=webReq("https://raw.githubusercontent.com/TwitchRecover/TwitchRecover/master/WEB_CI.txt");
        for(String s: response){
            if(!s.startsWith("#")){
                CI=s;
                return;
            }
        }
        CI="kimne78kx3ncx6brgo4mv6wki5h1ko";
    }

    /**
     * Retrieves the known domains from the Twitch Recover main repository.
     * @return ArrayList<String>        String arraylist containing anll of the known domains.
     */
    protected static ArrayList<String> getDomains(){
        ArrayList<String> domains=new ArrayList<String>();
        ArrayList<String> response=webReq("https://raw.githubusercontent.com/TwitchRecover/TwitchRecover/master/domains.txt");
        if(response.isEmpty()){
            //TODO: Add error handling.
        }
        else{
            domains.addAll(response);
        }
        return domains;
    }

    /**
     * API Requests:
     */

    /**
     * This function gets the list of the top 100 VODs from a
     * given offset value and each VOD's API response.
     * @param offset    Integer value representing the offset of the top videos to get from, in the bounds of [0,500].
     * @return String   String value containing the entire API JSON response.
     */
    private static String getVods(int offset){
        ArrayList<String> response=new ArrayList<String>();
        try{
            CloseableHttpClient httpClient=HttpClients.createDefault();
            HttpGet httpGet=new HttpGet("https://api.twitch.tv/kraken/videos/top?period=week&limit=100&offset="+offset);
            httpGet.addHeader("Accept","application/vnd.twitchtv.v5+json");
            httpGet.addHeader("Client-ID",CI);
            CloseableHttpResponse httpResponse=httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode()==HTTP_OK){
                BufferedReader br=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String line;
                while((line=br.readLine())!=null){
                    response.add(line);
                }
                br.close();
            }
            else{
                //TODO: Error handling.
            }
            httpResponse.close();
            httpClient.close();
        }
        catch(Exception e){
            //TODO: Error handling.
        }
        return String.join(", ",response);
    }

    /**
     * This function calls the getVods function and
     * then parses the JSON and returns the
     * domain list of retrieved VOD domains.
     * @param offset                Integer value representing the offset of the top videos to get from, in the bounds of [0,500].
     * @return ArrayList<String>    String arraylist containing all of the domains retrieved from that particular query.
     */
    protected static ArrayList<String> getVODDomains(int offset){
        ArrayList<String> domains=new ArrayList<String>();
        JSONObject jO=new JSONObject(getVods(offset));
        JSONArray vods=jO.getJSONArray("vods");
        for(int i=0;i<vods.length();i++){
            JSONObject vod=vods.getJSONObject(i);
            domains.add(singleRegex("(https:\\/\\/[a-z0-9]*.cloudfront.net)\\/[a-z0-9_]*\\/storyboards\\/[0-9]*-info.json",vod.getString("seek_previews_url").toLowerCase()));
        }
        return domains;
    }

    /**
     * This method computes the regex of a
     * given value and returns the value of
     * the first group, or if the pattern
     * did not match the given value, it
     * returns null.
     * @param pattern   String value representing the regex pattern to compile.
     * @param value     String value representing the value to apply the regex pattern to.
     * @return String   String value representing the first regex group or null if the regex did not compile.
     */
    private static String singleRegex(String pattern, String value){
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(value);
        if(m.find()){
            return m.group(1);
        }
        return null;
    }
}