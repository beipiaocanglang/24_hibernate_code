package study.javaProject.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Customer;
import study.javaProject.utils.HibernateUtil;

/**
 * 一张表的检索策略我们称之为：
 * 	类级别的检索策略。
 *  注意：只要是说类级别的检索策略，就一定不涉及关联对象。
 * 
 * 类级别检索策略解决的问题：
 * 1、查询的时机：
 * 		分为两种情况
 * 			立即加载：不管用不用，都马上查询出来
 * 			延迟加载：什么时候用，什么时候去查询。（懒加载，惰性加载）

 *  
 *常用方法：
 *	get：
 *		永远都是立即加载。返回的当前实体类的对象。
 *	load  
 *  	默认情况下是延迟加载。返回的当前实体类的代理对象。
 *  	它可以改为立即加载。
 *  	在对应的映射文件中，class元素使用lazy属性，把值改为false
 *  	lazy取值:true 延迟加载（默认值）。false是立即加载。
 *  	例如：
 *  	<class name="Customer" table="T_CUSTOMERS" lazy="false">
 * query的list方法:
 * 		永远都是立即加载。
 * 
 * @author zhy
 *
 */
public class HibernateDemo1 {
	
	/*
	 * 不管用不用，都立即查询出一个客户的所有字段数据
	 */
	@Test
	public void test1(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//查询id为1的客户
		Customer c1 = s.get(Customer.class, 1);
		System.out.println(c1);
		tx.commit();
		s.close();
		
	}	
	/*
	 * 什么时候用什么时候真正去查询
	 */
	@Test
	public void test2(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//查询id为1的客户
		Customer c1 = s.load(Customer.class, 1);
		System.out.println(c1);
		tx.commit();
		s.close();

	}
}














