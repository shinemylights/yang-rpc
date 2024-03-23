import com.lxy.common.model.User;
import com.lxy.common.service.UserService;
import com.lxy.yangrpc.proxy.ServiceProxyFactory;


/**
 * @author lxy
 * @date 2024/3/23 23:38
 */
public class test {
    public static void main(String[] args) {
        //获取代理
        UserService userService = ServiceProxyFactory. getProxy (UserService. class) ;
        User user = new User() ;
        user.setName ("hi wonyoung");
        //调用
        User newUser = userService. getUser (user) ;
        if (newUser != null) {
            System. out. println (newUser. getName());
        } else {
            System. out. println("user == nu1l") ;
        }
        long number = userService. getNumber () ;
        System. out. println (number) ;


    }
}
