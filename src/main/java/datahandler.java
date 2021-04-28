public class datahandler {
    public  static String[] dataprocess(String origin ,int num){
        String data[]=new String[num];
        for(int i=0;i<num;i++){
            data[i]=origin.substring(origin.indexOf("{"),origin.indexOf("}")+1);
            origin=origin.substring(origin.indexOf("}")+1,origin.length());
            //System.out.println(data[i]);
        }





        return data;
    }
}
