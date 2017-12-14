package study.javaProject.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Customer;
import study.javaProject.domain.Order;
import study.javaProject.utils.HibernateUtil;

/**
 * 单向多对一操作
 * 	 我们有订单的话，知道订单属于哪个客户
 * 	我们有客户的话，不知道客户有多少订单。
 * @author canglang
 *
 */
public class HibernateOne2Many_1 {

	/**
	 * 需求：
	 * 		保存两个订单和一个客户
	 * 问题：
	 * 		当先保存订单再保存客户时会执行五条sql语句(三条插入，两条更新)
	 * 
	 * 见图14
	 * 
	 * 原因：
	 * 		订单表的外键应用的是客户表的主键，先更新订单表时客户表的主键还没有生成
	 * 		当客户表的数据保存进去后，订单表会再此进行更新
	 * 解决方案：
	 * 		先保存客户表，再保存订单表
	 */
	@Test
	public void test1(){

		Order o1 = new Order();
		o1.setOrdernum("3333");
		o1.setMoney(45f);
		
		Order o2 = new Order();
		o2.setOrdernum("4444");
		o2.setMoney(55f);

		Customer c1 = new Customer();
		c1.setName("test2");
		c1.setAge(33);

		o1.setCustomer(c1);
		o2.setCustomer(c1);

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(c1);
		session.save(o1);
		session.save(o2);
		
		transaction.commit();
		session.close();
	}
	/*
	 * 级联操作
	 * 
	 * 更新操作
	 * 需求：
	 * 	创建一个新的客户，查询出来一个订单。把新客户和查询的订单建立关联关系。
	 * 	然后更新订单
	 * 问题：
	 * 	一个持久态对象，关联了一个临时态对象。会报错。
	 *
	 * 见图15
	 * 
	 * 解决办法：
	 * 	思路：在更新之前，先把临时态对象，转成持久态。（先执行保存，再执行更新）
	 *  执行级联保存更新。
	 *  在配置文件中配置：要想级联谁，就在对应的映射属性上配置
	 *  	cascade属性：就是用于配置级联操作的
	 *  <many-to-one name="customer" class="Customer" column="CUSTOMER_ID" cascade="save-update">
	 */
	@Test
	public void test2(){

		Customer c1 = new Customer();//临时态
		c1.setName("test2");
		c1.setAge(33);

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//查询id为1的订单
		Order order = session.get(Order.class, 1);
		//建立订单和客户的单向多对一关联关系
		order.setCustomer(c1);//持久态
		//更新订单
		session.update(order);
		transaction.commit();
		session.close();
	}
}
