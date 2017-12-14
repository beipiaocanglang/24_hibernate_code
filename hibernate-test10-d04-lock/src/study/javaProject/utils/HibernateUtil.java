package study.javaProject.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * hibernate的工具类
 * @author canglang
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	/*
	 * 定义一个静态代码块，在类加载时就会执行，并且只执行一次
	 * 异常是存在的，只不过hibernate将所有的异常都转成了运行时异常
	 */
	static{
		//1、创建Configuration对象
		Configuration configuration = new Configuration();
		//2、加载hibernate的主配置文件
		configuration.configure();
		//创建sessionFactory工厂
		sessionFactory = configuration.buildSessionFactory();
	}
	
	//提供一个每次都获取新的Session的方法
	public static Session getSession(){
		return sessionFactory.openSession();
	}
	//从当前线程上获取session
	public static Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	public static void main(String[] args) {
		Session session = getSession();
		session.close();
	}
}
