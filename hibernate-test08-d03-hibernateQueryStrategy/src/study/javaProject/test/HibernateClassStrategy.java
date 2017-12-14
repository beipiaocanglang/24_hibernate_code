package study.javaProject.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Customer;
import study.javaProject.utils.HibernateUtil;
/**
 * 一张表的检索策略我们称之为：类级别的检索策略。
 * 		类级别检索策略解决的问题：
 * 			查询时机的问题
 *  	注意：
 *  		累级别的检索策略只存在查询时机的问题
 *  		只要是说类级别的检索策略，就一定不涉及关联对象(不涉及到多张表)。
 * 查询的时机(两种)：
 * 		立即加载：不管用不用，都马上查询出来
 * 		延迟加载：什么时候用，什么时候去查询。（懒加载，惰性加载），使用的时代理对象
 * 常用方法：
 *		get：
 *			永远都是立即加载。返回的当前实体类的对象。
 *		load  
 *  		默认情况下是延迟加载。返回的当前实体类的代理对象。
 *  		它可以改为立即加载。
 *  		在对应的映射文件中，class元素使用lazy属性，把值改为false
 *  		lazy取值:
 *  			true：延迟加载（默认值）
 *  			false：立即加载。
 *  		例如：
 *  			<class name="Customer" table="T_CUSTOMERS" lazy="false">
 * 		query的list方法:
 * 			永远都是立即加载。
 * @author canglang
 */
public class HibernateClassStrategy {

	/*
	 * 立即加载：不管用或者不用都会马上全部查询出来
	 */
	@Test
	public void test1(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//查询id=1的客户信息
		Customer customer = session.get(Customer.class, 1);
		System.out.println(customer);
		transaction.commit();
		session.close();
	}
	/*
	 * 延迟加载：什么时候使用什么时候加载(使用的是代理对象)
	 */
	@Test
	public void test2(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//查询id=1的客户信息
		Customer customer = session.load(Customer.class, 1);
		System.out.println(customer);
		transaction.commit();
		session.close();
	}
	
}
