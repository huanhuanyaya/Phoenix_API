package api.auto.utils;

public class StringUtil {
    public static boolean isEmpty(String string){
//        if(string != null && string.trim().length()>0){
//            return false;
//        }else{
//            return true;
//        }
        return (string==null)||(string.trim().length()==0);
    }
}
