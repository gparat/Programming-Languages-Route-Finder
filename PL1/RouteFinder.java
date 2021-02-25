import java.util.*;
import java.net.*;
import java.io.*;   
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteFinder implements IRouteFinder{
    static String getURLText()throws Exception{
        return getURLText(TRANSIT_WEB_URL);
    }
    //method to read a url and return the htmll of the website into a string
    static String getURLText(String s)throws Exception{
        URLConnection url = new URL(s).openConnection();
        url.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.getInputStream()));
        String inputLine = "";
        String text = "";
        while((inputLine = in.readLine()) != null){
            text += inputLine + "\n";
        }
        in.close();
        //System.out.println(text);
        return text;
    }
    //reads a stringof html to find destinations with the given letter as an initial
    public Map<String, Map<String, String>> getBusRoutesUrls(final char destInitial) throws Exception{
        Map<String, Map<String, String>> routeurls=new TreeMap<String, Map<String, String>>();
        String htmldoc=getURLText();
        //String pattern ="<hr id=\""+(destInitial+"").toLowerCase()+".*?\" />(.*?)(<div>.*?</div>)+";
        String pattern ="<hr id=\""+(destInitial+"").toLowerCase()+".*?\" />(.*?)<hr";
        String namepattern ="<h3>("+(destInitial+"").toUpperCase()+".*)</h3>";
        String urlpattern ="\"(/schedules.*?)\".*?>(.*)</a>";
        String name="";
        int start=0;
        //String pattern ="<hr.*<hr";

        Pattern regPattern=Pattern.compile(pattern, Pattern.DOTALL);
        Matcher regMatcher=regPattern.matcher(htmldoc);

        while(regMatcher.find(start)){//find a block of html that contains the destination name, number, and url
            //System.out.println("Group 0: " + regMatcher.group());
            Pattern blockPattern=Pattern.compile(namepattern);
            String block=regMatcher.group();
            start=regMatcher.end()-3;
            //System.out.println(block);
            //System.out.println("_________________");
            Matcher blockMatcher=blockPattern.matcher(block);
            while(blockMatcher.find()){
                //System.out.println("name: "+blockMatcher.group(1));
                name=blockMatcher.group(1);
            }
            Pattern urlPattern=Pattern.compile(urlpattern);
            String urlout=regMatcher.group();
            Matcher urlMatcher=urlPattern.matcher(urlout);
            Map<String, String> holder=new TreeMap<String, String>();
            
            while(urlMatcher.find()){//search the block for the number and url
                //System.out.println("all: "+urlMatcher.group());
                //System.out.println("url: "+urlMa  tcher.group(1));
                String num=urlMatcher.group(1);
                //System.out.println("num: "+urlMatcher.group(2));
                String url=urlMatcher.group(2);
                
                holder.put(url, num);
            }
            routeurls.put(name, holder);
            //System.out.println();
        }
        //System.out.println(routeurls);
        return routeurls;
    }

    //method to read a route url and return the destination and stops
    public Map<String, LinkedHashMap<String, String>> getRouteStops(final String url) throws Exception{
        Map<String, LinkedHashMap<String, String>> routestops=new LinkedHashMap<String, LinkedHashMap<String, String>>();
        String htmldoc=getURLText(url);

        String pattern ="<h2>Weekday<small>(To .*?)</small>(.*?)</thead>";
        Pattern regPattern=Pattern.compile(pattern, Pattern.DOTALL);
        Matcher regMatcher=regPattern.matcher(htmldoc);
        //System.out.println("eee");
        //String stoppattern="<strong class=\"fa fa-stack-1x\">(.*?)</strong>";
        String stoppattern="<strong class=\"fa fa-stack-1x\">(.*?)</strong>.*?<p>(.*?)</p>";

        while(regMatcher.find()){// find a block of html that contains the stop info
            LinkedHashMap<String, String> holder=new LinkedHashMap<String, String>();
            String block=regMatcher.group();
            //System.out.println("block"+block);
            String destination=regMatcher.group(1);

            Pattern stopPattern=Pattern.compile(stoppattern,Pattern.DOTALL);
            Matcher stopMatcher=stopPattern.matcher(block);

            while(stopMatcher.find()){//search the blcok for stop info
                //System.out.println("test");
                String num=stopMatcher.group(1);
                String name=stopMatcher.group(2);
                //System.out.println("num: "+num);
                //System.out.println("name: "+name);
                holder.put(num, name);
            }
            //System.out.println(destination);
            //System.out.println(holder);
            routestops.put(destination,holder);
            //System.out.println(routestops);
        }
        //System.out.println(routestops);
        return routestops;
    }
    
}
