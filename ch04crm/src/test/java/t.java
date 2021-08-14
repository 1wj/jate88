import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.junit.Test;

public class t {
    @Test
    public void t1(){
        UserService service= (UserService) ServiceFactory.getService(new UserServiceImpl());
        System.out.println(service);
    }
    @Test
    public  void t2(){
        UserDao dao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        System.out.println(dao);
    }
}
