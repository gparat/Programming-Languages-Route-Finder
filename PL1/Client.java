import java.util.*;

public class Client {
    public static void main(String args[]) throws Exception{
        RouteFinder rf=new RouteFinder();
        //String t=rf.getURLText();
        //System.out.println(t);
        Scanner scan=new Scanner(System.in);
        boolean done=false;
        while(!done){
            System.out.println("Please enter a letter that your destination starts with");
            char destchar=scan.nextLine().toUpperCase().charAt(0);
            Map <String, Map<String, String>> routemap=rf.getBusRoutesUrls(destchar);
            Map <String,String> routeurlholder;
            //Iterator <String> it=routemap.keySet().iterator();
            //System.out.println(routemap);
            if(!routemap.isEmpty()){
                //System.out.println("ok");
                
                for (Map.Entry<String, Map<String, String>> pair : routemap.entrySet()) {
                    System.out.println("Destination: "+pair.getKey()); 
                    routeurlholder=pair.getValue();
                    for (Map.Entry<String, String> pair2 : routeurlholder.entrySet()) {
                        System.out.println("Bus Number: "+pair2.getKey()); 
                    }
                    System.out.println("+++++++++++++++++++++++++++++++++++");
                }

                System.out.println("Please enter your destination: ");
                String dest=scan.nextLine().strip();
                System.out.println("Please enter a route id: ");
                String rnum=scan.nextLine().strip();
                
                boolean valid=false;
                boolean valid2=false;

                for (Map.Entry<String, Map<String, String>> pair : routemap.entrySet()) {//check that requested destination is the map
                    if(dest.equals(pair.getKey())){
                        valid=true;
                        routeurlholder=pair.getValue();
                        for (Map.Entry<String, String> pair2 : routeurlholder.entrySet()) {//check that requested id is in the map
                            if(rnum.equals(pair2.getKey())){
                                valid2=true;
                            }
                        }
                    }
                }

                /*
                for(int i=0;i<rnum.length();i++){//check that id is a number
                    if(!Character.isDigit(rnum.charAt(i))){
                        valid=false;
                    }
                }
                */
                //System.out.println(valid);
                //System.out.println(valid2);
                if(valid&&valid2){
                    //System.out.println("e");
                    Map <String, LinkedHashMap<String, String>> routestops=rf.getRouteStops("https://www.communitytransit.org/busservice/"+routemap.get(dest).get(rnum));
                    //System.out.println(routestops);
                    for (Map.Entry<String, LinkedHashMap<String, String>> pair : routestops.entrySet()){
                        System.out.println("Destination: "+pair.getKey());
                        Map <String,String> temp=pair.getValue();
                        for (Map.Entry<String, String> pair2 : temp.entrySet()) {
                            System.out.println("Stop number: "+pair2.getKey()+" is "+pair2.getValue()); 
                        }
                    }
                }
                else{
                    throw new RuntimeException("Route ID or destination input was invalid or not found.");
                }
            }
            else{
                throw new RuntimeException("Route initial invalid or not found.");
            }
            System.out.println("Do you want to check different destination? Please type Y to continue or press any other key to exit");
            String input=scan.nextLine().strip();
            if(!input.equalsIgnoreCase("y")){
                done=true;
            }
        }
        scan.close();
    }
}
