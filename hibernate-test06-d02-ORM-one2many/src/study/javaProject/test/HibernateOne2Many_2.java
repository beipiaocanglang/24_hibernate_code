package study.javaProject.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Customer;
import study.javaProject.domain.Order;
import study.javaProject.utils.HibernateUtil;

/**
 * 双向一对多操作
 * 	 我们有订单的话，知道订单属于哪个客户
 * 	 我们有客户的话，知道客户有多少订单。
 * @author canglang
 *
 */
public class HibernateOne2Many_2 {
	
   /**保存操作
	* 需求：
	* 	先保存客户，再保存订单
	* 问题：
	*    当我们建立了双向关联关系之后，就算是先保存主表，再保存从表，也是会产生5条SQL语句
	*  	Hibernate: insert into T_CUSTOMERS (NAME, AGE) values (?, ?)
	*	Hibernate: insert into T_ORDERS (ORDERNUM, MONEY, CUSTOMER_ID) values (?, ?, ?)
	*	Hibernate: insert into T_ORDERS (ORDERNUM, MONEY, CUSTOMER_ID) values (?, ?, ?)
	*	
	*	Hibernate: update T_ORDERS set CUSTOMER_ID=? where id=?
	*	Hibernate: update T_ORDERS set CUSTOMER_ID=? where id=?
	* 解决办法：
	*    思路：
	*   	让主表的集合放弃维护关联关系的权利。
	*    操作方式：注释上
	*         c1.getOrders().add(o1);
	*         c1.getOrders().add(o2);
	*/
	@Test
	public void test1(){

		Order o1 = new Order();
		o1.setOrdernum("555");
		o1.setMoney(45f);
		
		Order o2 = new Order();
		o2.setOrdernum("666");
		o2.setMoney(55f);

		Customer c1 = new Customer();
		c1.setName("test4");
		c1.setAge(33);

		o1.setCustomer(c1);
		o2.setCustomer(c1);
		
		//c1.getOrders().add(o1);
		//c1.getOrders().add(o2);

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(c1);
		session.save(o1);
		session.save(o2);
		
		transaction.commit();
		session.close();
	}
	
	/*
	 * 更新操作
	 * 需求：
	 * 	先创建一个订单，然后查询出来一个客户。
	 * 	建立客户和新订单的关联关系。
	 * 	更新客户
	 * 问题：
	 * 	一个持久态对象，关联了一个临时态的对象。
	 * 解决办法：
	 * 	配置级联保存更新
	 * 	<set name="orders" table="T_ORDERS" cascade="save-update">
	 */
	@Test
	public void test2(){
		
		//创建一个新的订单
		Order o1 = new Order();//临时态
		o1.setOrdernum("A003");
		o1.setMoney(100f);
		
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//查询一个客户
		Customer c1 = s.get(Customer.class, 1);//持久态
		//建立双向关联关系
		c1.getOrders().add(o1);
		o1.setCustomer(c1);
		//更新操作
		s.update(c1);
		
		tx.commit();
		s.close();
	}
	
	/*
	 * 需求：变更关系
	 * 	把id为1的订单，从属于2号客户，改为属于1号客户
	 * 解决办法：
	 * 	使用配置的方式，来实现让有集合的一方，放弃维护的权利
	 * 	inverse：是否放弃维护的权利
	 * 	取值：true放弃  和  false 不放弃（默认值）。
	 *  inverse用于应该只出现在set元素上
	 * <set name="orders" table="T_ORDERS" cascade="save-update" inverse="true" > 
	 */
	@Test
	public void test3(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Customer c1 = s.get(Customer.class, 1);//查询出来1号客户
		Order o1 = s.get(Order.class, 1);//查询出啦1号订单
		//建立双向关联关系
		c1.getOrders().add(o1);
		o1.setCustomer(c1);
		//更新操作
		s.update(c1);
		
		tx.commit();
		s.close();
		
		//System.out.println(c1.getOrders());
	}
	/*
	 * 需求：
	 * 	删除一个订单
	 * 
	 * 可以删除成功
	 */
	@Test
	public void test4(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Order o1 = s.get(Order.class, 1);
		s.delete(o1);
		tx.commit();
		s.close();	
	}
	/*
	 * 需求：
	 * 	删除一个客户
	 * 
	 * 在直接删除客户的时候，如果客户的集合属性（set元素）上没有配置inverse=true，
	 * 会直接把客户删除掉，同时把订单中关联改该客户的id置为null
	 * 
	 * 在直接删除客户的时候，如果客户的集合属性（set元素）上配置了inverse=true，
	 * 如果有订单引用该客户的话，则不能删除成功。
	 * 
	 * 原因：
	 * 	因为客户的集合属性已经放弃维护和订单之间的关联关系，也就是说，它将不能把
	 *  订单的CUSTOMER_ID列置为null。
	 */
	@Test
	public void test5(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Customer c1 = s.get(Customer.class,4);
		s.delete(c1);
		tx.commit();
		s.close();	
	}
	/** 
	 * 解除关系
	 * 需求：
	 * 	把1订单和1号客户之间的关系解除
	 * 结果：
	 * 	会将order表中的外键修改成null
	 */
	@Test
	public void test6(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Customer c1 = s.get(Customer.class,2);
		Order o1 = s.get(Order.class, 3);
		
		//解除1号订单和1号客户之间的关系
		c1.getOrders().remove(o1);
		o1.setCustomer(null);
		
		
		tx.commit();
		s.close();	
	}
	/*
	 * 孤儿数据
	 * 	 在一对多对象关系映射中，数据具有父子关系，当子数据和父数据之间失去了关联关系。子数据被称之为孤儿数据
	 * 孤儿删除
	 * 	在hibernate中认为孤儿数据，是没有存在的意义，理应删除。
	 * 需要在配置文件中配置
	 * 	孤儿删除的配置：cascade="delete-orphan"
	 * 	<set name="orders" table="T_ORDERS" cascade="save-update,delete-orphan" inverse="true">
	 */
	@Test
	public void test7(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Customer c1 = s.get(Customer.class,2);
		Order o1 = s.get(Order.class, 4);
		
		//解除1号订单和1号客户之间的关系
		c1.getOrders().remove(o1);
		o1.setCustomer(null);
		
		//s.update(c1);
		tx.commit();//有快照机制的存在
		s.close();	
	}
	/*
	 * 级联删除：
	 * 
	 * 	<set name="orders" table="T_ORDERS" cascade="save-update,delete" inverse="true">
	 */
	@Test
	public void test8(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Customer c1 = s.get(Customer.class,1);
		s.delete(c1);
		tx.commit();
		s.close();	
	}
}
