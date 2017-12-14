package study.javaProject.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import study.javaProject.domain.Customer;
import study.javaProject.utils.HibernateUtil;

/**
 * hibernate的添加属性逻辑
 * @author canglang
 */
public class HibernateDemo {
	/**
	 * 保存用户
	 */
	@Test
	public void test1(){
	
		Customer customer = new Customer();
		customer.setName("夜鹰.苍狼");
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(customer);
		
		transaction.commit();
		session.close();
	}
	/**
	 * 查询用户信息
	 */
	@Test
	public void testFindOne(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Customer customer = session.get(Customer.class, 1);
		System.out.println(customer);
		
		transaction.commit();
		session.close();
	}
}
