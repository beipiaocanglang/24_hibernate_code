package study.javaProject.test;

import java.util.List;
import java.util.Set;

import javafx.geometry.Side;

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

/**
 * /**
 * hibernate的二级缓存
 *  1、二级缓存的存储结构
 * 		明确：
 * 			二级缓存是存放在SessionFactory中的。
 * 			它是以插件的形式存在的，需要独立配置。
 *  2、二级缓存提供事物支持
 *		非严格读写：READ UNCOMMITTED
 *		读写型：READ COMMITTED
 *		事务型：REPEATABLED READ
 *		只读型：SERIALIZABLE
 *	3、二级缓存的提供商
 *		我们选用的是EhCache
 *	4、配置二级缓存的步骤
 *		a、拷贝必要的jar包到工作空间
 *		b、拷贝ehcache.xml文件到类的根路径下。
 *			该文件存放于echcache-core-2.4.3.jar包中，名称是ehcahe-failsafe.xml
 *		c、在hibernate的主配置文件中开启二级缓存
 *			<property name="hibernate.cache.use_second_level_cache">true</property>
 *		d、配置hibernate的二级缓存提供商
 *			<property name="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory</property>
 *		e、配置要使用二级缓存的类或者是集合
 *			<class-cache usage="read-write" class="cn.itcast.domain.Customer"/>
 *			<collection-cache usage="read-write" collection="cn.itcast.domain.Customer.orders"/>
 * @author canglang
 */
public class HibernateDemo1 {
	/**
	 * 验证二级缓存是否存在
	 */
	@Test
	public void test1(){
		Session s1 = HibernateUtil.getSession();
		Transaction t1 = s1.beginTransaction();
		//去数据库中查询一个客户，将查询的数据放入一级缓存中，如果配置了二级缓存，同时也会将数据放入二级缓存中
		Customer c1 = s1.get(Customer.class, 1);
		System.out.println(c1);
		t1.commit();
		s1.close();
		
		Session s2 = HibernateUtil.getSession();
		Transaction t2 = s2.beginTransaction();
		//查询一个客户(如果二级缓存存在则使用二级缓存中的数据，而不会再去数据库中查询)
		Customer c2 = s2.get(Customer.class, 1);
		System.out.println(c2);
		t2.commit();
		s2.close();
	}
	
	/*
	 * 类缓存区
	 * 	都是针对实体类说的，不涉及类中的关联对象。
	 *  举例：
	 *    如果配置了客户，只涉及客户的信息，不会涉及客户关联的订单！
	 * 哪些方法可以操作类缓存区：
	 * 	get和load：
	 * 		他们都是可以存和取二级缓存中类缓存区的数据。
	 *  query.list()
	 *  	它只能存，不能取二级缓存的类缓存区数据
	 *  
	 * 类缓存区，存的是什么？
	 * 	一级缓存：存的是对象
	 * 	二级缓存：存的是散装数据。
	 * 		例如：Cusomert[
	 * 				{id:1,name:'testA',age:20},
	 * 				{id:2,name:'testB',age:28}
	 * 			] 
	 */
	@Test
	public void test2(){
		Session s1 = HibernateUtil.getSession();//每次都是获取一个新的Session，不能绑到当前线程上。
		Transaction tx1 = s1.beginTransaction();
		//使用query的list方法，查询所有客户
		Query query1 = s1.createQuery("from Customer");
		List<Customer> list1 = query1.list();//会去查询，同时把查询结果放入一级缓存。如果配置了二级缓存，也会放入二级缓存。
		System.out.println(list1);
		//使用get方法获取一个客户
		Customer c1 = s1.load(Customer.class,1);//不查，因为一级缓存之中一级有了
		System.out.println(c1);
		tx1.commit();
		s1.close();//session一关闭，一级缓存就消失了
		
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		//使用get方法获取一个客户
		Customer c2 = s2.load(Customer.class,1);//不查，因为二级缓存中有
		System.out.println(c2);
		Query query2 = s2.createQuery("from Customer");
		List<Customer> list2 = query2.list();//会去查询,不会从二级缓存中取数据
		System.out.println(list2);
		tx2.commit();
		s2.close();
	}
	

	/*
	 * 集合缓存区
	 * 要想使用集合缓存区(缺一不可)：
	 * 	  1、必须在主配置文件中配置开启集合缓存区。
	 *	  	<collection-cache usage="read-write" collection="study.javaProject.domain.Customer.orders"/>
	 * 	  2、必须同时配置上集合元素的类缓存区。
	 *    	<class-cache usage="read-write" class="study.javaProject.domain.Order"/>
	 *	集合缓存区，存入的是什么？
	 *		是只有OID的一个集合。
	 *		举例：
	 *			{id:1,id:2,id:3,id:4....}
	 *	执行方式：
	 *		在使用集合缓存区时，会先从集合缓存区去匹配OID，把匹配上的OID全部取出，到对应的类缓存区去取数据，再生成对象
	 */
	@Test
	public void test3(){
		Session s1 = HibernateUtil.getSession();//每次都是获取一个新的Session，不能绑到当前线程上。
		Transaction tx1 = s1.beginTransaction();
		//使用get方法获取一个客户
		Customer c1 = s1.load(Customer.class,1);
		//输出客户的订单
		System.out.println(c1.getOrders());//由于有延迟加载的存在，此时才会去查询。并且把查询结果存入一级缓存之中。同时也会存入二级缓存。
		tx1.commit();
		s1.close();//session一关闭，一级缓存就消失了
		
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		//使用get方法获取一个客户
		Customer c2 = s2.load(Customer.class,1);
		//获取该客户的订单
		System.out.println(c2.getOrders());
		tx2.commit();
		s2.close();
	}
	
	/*
	 * 更新时间戳
	 * 
	 * 时间戳原理：
	 * 	 	当一级缓存和二级缓存在创建时，都会有两个时间点。
	 * 		
	 * 		其一：创建时间
	 * 		其二：最后修改时间
	 * 当执行update后，由于一级缓存已经发生变化了，
	 * 这时hibernate会用一级缓存的最后修改时就和二级缓存的最后修改时间进行比较，
	 * 用离当前时间近的去修改离当前时间远的。
	 */
	@Test
	public void test4(){
		Session s1 = HibernateUtil.getSession();//每次都是获取一个新的Session，不能绑到当前线程上。
		Transaction tx1 = s1.beginTransaction();
		//使用get方法获取一个客户
		Customer c1 = s1.get(Customer.class,1);//会查询，同时存入一级缓存和二级缓存
		c1.setName("泰斯特");
		tx1.commit();//由于快照机制，此行会执行更新，同时更新一级缓存。也会更新二级缓存。
		s1.close();//session一关闭，一级缓存就消失了
		
		Session s2 = HibernateUtil.getSession();//每次都是获取一个新的Session，不能绑到当前线程上。
		Transaction tx2 = s2.beginTransaction();
		//使用get方法获取一个客户
		Customer c2 = s2.get(Customer.class,1);//不查。二级缓存中有
		System.out.println(c2);//输出的【泰斯特】还是【testA】？
		tx2.commit();
		s2.close();
	}
	
	/*
	 * 为什么query的list方法对二级缓存是能存不能取？
	 * 原因：
	 * 		 因为HQL语句是不定的，hibernate没法确定每次查询的HQL语句都是一样。
	 * 
	 * 解决不能取的思路：
	 * 	 	Map<String hql,Object result> queryCache;
	 * 思路：
	 * 		创建一个新的区域，区域可能是一个map。
	 * 		map的key是查询的HQL语句。
	 * 		map的value是查询的结果集。
	 * 
	 * hibernate的查询缓存区：
	 * 		1、即是开启了二级缓存，hibernate也不会开启查询缓存区。查询缓存区必须独立开启，且必须是在二级缓存已经开启的基础之上。
	 * 			<!-- 开启hibernate的查询缓存区：有些地方（书籍或者公司）可能会把查询缓存区叫成hibernate的三级缓存 -->
	 *			<property name="hibernate.cache.use_query_cache">true</property>
	 *
	 *		2、在执行查询的时候，需要设置使用查询缓存区
	 *			query1.setCacheable(true);//明确使用查询缓存区
	 */
	@Test
	public void test5(){
		Session s1 = HibernateUtil.getSession();//每次都是获取一个新的Session，不能绑到当前线程上。
		Transaction tx1 = s1.beginTransaction();
		Query query1 = s1.createQuery("from Customer");
		query1.setCacheable(true);//明确使用查询缓存区
		List list1 = query1.list();
		System.out.println(list1);
		tx1.commit();
		s1.close();//session一关闭，一级缓存就消失了
		
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		Query query2 = s2.createQuery("from Customer");
		query2.setCacheable(true);
		List list2 = query2.list();
		System.out.println(list2);
		tx2.commit();
		s2.close();
	}
}














