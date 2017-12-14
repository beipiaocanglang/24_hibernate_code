package study.javaProject.test;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import study.javaProject.domain.Customer;
import study.javaProject.domain.Order;
import study.javaProject.utils.HibernateUtil;

public class HibernateDemo {
	
	
	/*
	 * 对象导航查询
	 * 	
	 * 明确：
	 * 	思想的转变。我们在需要某些数据时，是否还要单独去写一个方法来实现。
	 * 
	 * 对象导航的思想
	 */
	@Test 
	public void test17(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Order o1 = s.get(Order.class, 1);
		Customer c = o1.getCustomer();//对象导航的方式
		System.out.println(c);
		tx.commit();
		s.close();
	}
	
	@Test
	public void test15(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Customer c1 = s.get(Customer.class, 1);
		Set<Order> orders = c1.getOrders();//对象导航的方式
		System.out.println(orders);
		tx.commit();
		s.close();
	}
	/*原来JDBC时，留下的思想
	@Test
	public void test16(){
		List<Order> orders = findOrderByCustomerid(1);
		System.out.println(orders);
	}
	
	public List<Order> findOrderByCustomerid(int customerid){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//select * from t_orders where customer_id = ?;
		Query query = s.createQuery("from Order o where o.customer.id = ?");
		query.setInteger(0, customerid);
		List<Order> orders = query.list();
		tx.commit();
		s.close();
		return orders;	
	}
	*/
	
	/*
	 * 原生SQL查询
	 * 	使用的是session的createSQLQuery方法
	 * 	它的执行list结果是一个由Object数组组成的集合。
	 * 
	 * 在实际开发中，我们一般不会使用Object数组来操作。
	 * 而是在执行list()方法之前，调用addEntity方法，给sqlquery添加一个实体，
	 * 从而得到的就是实体对象的集合
	
	select 
	sum(
		case when score between 0 and 59 then 1 else 0 end
	)  as E,
	sum(
		case when score between 60 and 69 then 1 else 0 end
	) as D,
	sum(
		case when score between 70 and 79 then 1 else 0 end
	) as C,
	sum(
		case when score between 80 and 89 then 1 else 0 end
	) as B,
	sum(
		case when score between 90 and 99 then 1 else 0 end
	) as A	
 from t_students
	
	 */
	@Test
	public void test14(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		SQLQuery sqlquery = s.createSQLQuery("select * from t_customers");//参数就是SQL语句
		//给SQLQuery添加一个实体
		sqlquery.addEntity(Customer.class);
		List list = sqlquery.list();
		tx.commit();
		s.close();
		for(Object o : list){
			System.out.println(o);
		}
		
	}
	
	/**
	 * 投影查询
	 * 		就想实现
	 * 			查询客户时，只有客户的id和name
	 * 			查询订单时，只有订单id和money
	 * 投影查询就是：
	 * 		在实际开发中，我们只需要一个实体类中的某个或者某些字段，当查询完成后还想使用该实体对象来操作。
	 * 		需要把查询的某个或某些字段投射到实体类上，这种查询方式叫投影查询。
	 * 在使用投影查询时的要求：
	 * 		实体类必须提供对应参数列表的构造函数。
	 *  	在写HQL语句时，需要使用new关键字
	 * 
	 * 注意事项：
	 * 		在使用投影查询时，如果直接写类名，需要确定该类在工程中唯一。
	 * 		如果不唯一的话，需要些类全名。包名.类名
	 */
	@Test
	public void test13(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Query query = s.createQuery("select new study.javaProject.domain.Order(o.id,o.money) from Order o");
		List list = query.list();
		tx.commit();
		s.close();
		for(Object o : list){
			System.out.println(o);
		}
	}
	//无重复类的查询
	@Test
	public void test11(){
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("select new Customer(c.id,c.name) from Customer c");
		List<Object> list = query.list();
		
		transaction.commit();
		session.close();
		for (Object order : list) {
			System.out.println(order);
		}
	}
	/**
	 * 迫切左外连接查询
	 * 		返回的是一个集合，集合中的每个元素都是左表实体对象。
	 * 		有可能重复。重复的原因是看迫切左外连接查询的条件。
	 */
	@Test
	public void test10(){
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("from Customer c left outer join fetch c.orders");
		List<Object> list = query.list();
		
		transaction.commit();
		session.close();
		for (Object order : list) {
			System.out.println(order);
		}
	}
	/**
	 * 左外连接查询
	 * 
	 * 		返回值是一个集合，集合的每个元素是Object数组。
	 * 		数组中包含2个Object对象。
	 * 		其中一个左表对象和一个右表对象。
	 * 		左表对象有可能重复，右表对象不会重复
	 */
	@Test
	public void test9(){
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("from Customer c left outer join c.orders");
		List<Object[]> list = query.list();
		
		transaction.commit();
		session.close();
		for (Object[] order1 : list) {
			for (Object object : order1) {
				System.out.println(object);
			}
		}
	}
	
	/**
	 * 分页查询
	 * 		mysql中的分页查询：
	 * 			使用的关键字：limit
	 * 			关键字中的必要条件：firstResult（开始记录索引），maxResults（最大的结果集数）
	 *  	sql语句：
	 * 			select * from t_orders limit firstResult,maxResults;
	 * 
	 * HQL语句：from Order 
	 * 
	 * 在hibernate中分页只有
	 * 		setFirstResult
	 * 		setMaxResults
	 * 不管使用什么数据库都是这两个方法
	 */
	@Test
	public void test8(){
		//调用HQLA查询
		//List<Order> findAllOrders = findAllOrders(0, 2);
		//调用QBC查询
		List<Order> findAllOrders = findAllOrder(0, 2);
		
		for (Order order : findAllOrders) {
			System.out.println(order);
		}
	}
	
	//QBC查询，方法调用时需要传入两个参数
	public List<Order> findAllOrder(int firstRestut, int maxResult){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Order.class);
		criteria.setFirstResult(firstRestut);//开始记录的索引
		criteria.setMaxResults(maxResult);//每页要查询的条数
		
		List<Order> list = criteria.list();
		
		transaction.commit();
		session.close();
		return list;
	}
	
	//HQL查询，方法调用时需要传入两个参数
	public List<Order> findAllOrders(int firstRestut, int maxResult){
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("from Order");
		query.setFirstResult(firstRestut);
		query.setMaxResults(maxResult);
		
		List<Order> list = query.list();
		
		transaction.commit();
		session.close();
		
		return list;
	}
	
	/*
	 * 命名查询
	 * 	把sql或者HQL在映射文件中配置起来(查询哪张表就放在哪个配置文件中)。提供一个名称。
	 * 	名称和语句之间是键值对的对应关系。
	 *  在程序使用根据名称获取语句的方法，叫做命名查询
	 */
	@Test
	public void test7(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Query  query = s.getNamedQuery("findCustomerByCondition");
		//给条件赋值
		query.setString("name", "test1");
		query.setInteger("age", 24);
		List list = query.list();
		tx.commit();
		s.close();
		for(Object o : list){
			System.out.println(o);
		}
	}
	/*
	 * 具名查询
	 * 		具名其实就是给查询的参数占位符提供一个名称，使我们在查询时，使用名称而不是索引来给参数赋值
	 * 书写规范：
	 * 		必须用 	:名称
	 */
	@Test
	public void test6(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		/**
		 * 基本写法：
		 * 		Query query = s.createQuery("from Customer where age >= ? and name=?");
		 *		query.setString(1, "test1");
		 *		query.setInteger(0, 24);
		 * 存在的问题：
		 * 		当HQL语句的条件参数发生位置上的变化时，设置参数的0和1也要相对应的修改
		 */
		Query query = s.createQuery("from Customer where age > :age and name=:name");
		//给条件赋值
		query.setString("name", "testD");
		query.setInteger("age", 28);
		List list = query.list();
		tx.commit();
		s.close();
		for(Object o : list){
			System.out.println(o);
		}
	}
	/**
	 * 多态查询：了解一下
	 * 作用：
	 * 		查询所有配置文件中映射的实体类，中的所有数据
	 */
	@Test
	public void test5(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Query query = s.createQuery("from java.io.Serializable ");
		//Query query = s.createQuery("from java.long.Object ");
		List list = query.list();
		tx.commit();
		s.close();
		for(Object o : list){
			System.out.println(o);
		}
	}
	
	/*
	 * 排序查询
	 * 	关键字和SQL是一样的。
	 * 	order by 
	 * 	asc 
	 *  desc
	 */
	@Test
	public void test4(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Query  query = s.createQuery("from Customer order by age desc ");
		List list = query.list();
		tx.commit();
		s.close();
		for(Object o : list){
			System.out.println(o);
		}
	}
	/**
	 * QBC特例查询
	 * 	特例：
	 * 		用实体对象创建一个例子，查询语句的条件就会根据实体对象中提供的例子进行条件的拼装。
	 *	注意：
	 *		1、条件只会用等于
	 *		2、在拼装条件时，它只涉及不为null的字段，同时不会把OID作为条件。
	 */
	@Test
	public void test3(){
		//我的例子
		Customer c = new Customer();
		c.setAge(24);
		
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//创建查询对象。参数是要查询的实体
		Criteria criteria = s.createCriteria(Customer.class);
		//添加条件
		criteria.add(Example.create(c));//select * from t_customes where age = 28
		
		List list = criteria.list();
		tx.commit();
		s.close();
		for(Object o : list){
			System.out.println(o);
		}
	}
	/**
	 * QBC带条件查询
	 */
	@Test
	public void tes2(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		//创建查询对象。参数是要查询的实体
		Criteria criteria = session.createCriteria(Customer.class);
		//添加条件
		criteria.add(Restrictions.gt("age", 24));
		List list = criteria.list();
		
		for (Object object : list) {
			System.out.println(object);
		}
		transaction.commit();
		session.close();
	}
	
	/**
	 * QBC查询
	 * 		见word文档中的附件
	 * 		QBC:query by criteria(通过标准查询)
	 *  	QBC能实现的HQL都能实现。反之亦然。
	 * 注意：
	 * 		它更加面向对象
	 * 		QBC的查询没有条件，查出来的是全部
	 */
	@Test
	public void tes1(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		//创建查询对象。参数是要查询的实体
		Criteria criteria = session.createCriteria(Customer.class);
		List list = criteria.list();
		
		for (Object object : list) {
			System.out.println(object);
		}
		transaction.commit();
		session.close();
	}
	
	/**
	 * HQL带条件查询
	 * 		见word文档中的附件
	 * 		hiberante中HQL使用条件，也是where关键字。
	 * 	 	条件的参数占位符也是用的问号
	 * 注意事项：
	 * 		hibernate中查询语句的参数占位符索引是从0开始的。
	 */
	@Test
	public void test(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("from Customer where age>=?");
		//给条件赋值
		query.setInteger(0, 24);
		List list = query.list();
		
		for (Object object : list) {
			System.out.println(object);
		}
		transaction.commit();
		session.close();
	}
}














