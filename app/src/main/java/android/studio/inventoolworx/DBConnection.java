package android.studio.inventoolworx;

/**
 * Created by IT-PC2 on 23/02/2018.
 */

public class DBConnection {
    //URL to our login.php file, url bisa diganti sesuai dengan alamat server kita
//    public static final String LOGIN_URL = "http://192.168.42.64//inventoworx//login.php";
    public static final String LOGIN_URL = "http://invento.html-5.me//login.php";
//    public static final String INVENTORY_URL = "http://192.168.42.64//inventoworx//inventory.php";
    public static final String INVENTORY_URL = "http://invento.html-5.me//inventory.php";
    public static String COOKIE = "";
    public static void setCOOKIE(String token){
        COOKIE = "__test=" + token + ";";
    }
    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";


}
