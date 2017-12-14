package study.javaProject.test;

import org.hibernate.Session;
import org.junit.Test;

import study.javaProject.utils.HibernateUtil;

public class SessionDemo {

	//测试从当前线程上获取session
	//如果从当前线程上获取session对象，则不需要手动关闭session了，由hibernate为我们关闭
	@Test
	public void test(){
		Session session = HibernateUtil.getCurrentSession();
		Session session2 = HibernateUtil.getCurrentSession();
		System.out.println(session == session2);//true:同一个线程上获取的数据
		
		//session.close();
		//session2.close();
	}
}
