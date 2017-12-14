package study.javaProject.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Customer;
import study.javaProject.utils.HibernateUtil;

/**
 * 关联级别的检索策略
 * 注意：
 * 	 它永远说的是查询当前实体类时，要不要查询那个关联的对象。
 * 	涉及的是：一对多，多对一，多对多和一对一。 
 * 	举例：
 * 	  一对多和多对一
 * 		查询客户的时候要不要查询订单：一对多的情况
 * 		查询订单的时候要不要查询客户：多对一的情况
 *   多对多
 *   	查询教师的时候要不要查询学生：多对多的情况
 *   一对一
 *   	查询人员的时候要不要查询身份证号：一对一的情况
 *   
 * 解决的问题：
 * 	1、查询的时机
 * 		立即加载：不管用不用，都马上查询出来
 * 		延迟加载：什么时候用，什么时候真正去查询
 *  2、查询的方式（怎么查）
 *  	多条SQL语句
 *  	子查询
 *  	表连接
 * 关于关联级别的检索策略,有如下配置
 * 	明确：是有少的一方，根据少的一方，去检索多的一方 
 * 	举例说明：
 * 		我们有客户，根据客户获取订单。/ 我们有一个教师，根据这个教师获取学生
 * 配置涉及的属性：
 * 	lazy：加载的时机
 * 		true：延迟加载（默认值）
 * 		false：立即加载
 * 		extra：极懒加载 （用什么查什么，不用的都不查）
 * 	fetch：检索的方式
 * 		select：多条SQL语句(默认值)
 * 		subselect：子查询
 * 		join：左外连接
 * 
 * 我们现在是在映射文件的set元素上配置！
 * 
 * @author zhy
 * 
 * 第七种情况：lazy="true" fetch="join"
 * <set name="orders" table="T_ORDERS" lazy="extra" fetch="join">
 *
 */
public class HibernateDemo8 {
	
	/* 当我们使用HQL语句进行查询时，由于语句是我们自己提供的，
	 * hibernate是无权修改的，所以只会按照我们提供的语句去执行。
	 * 所以，此时fetch=join就失效了。
	 * 但是，当fetch失效了之后，lazy属性就有效了。
	 * 
	 * 
	 * 需求：
	 * 	查询所有客户，获取每个客户的所有订单
	 * 查询的时机：
	 *   由于fetch失效了，lazy就又有效了。lazy取值是true就是延迟加载
	 * 查询的方式：
	 * 	 由于使用了HQL语句，所以fetch失效了。会取默认值（多条SQL语句）
	 * 	
	 */
	@Test
	public void test2(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//查询所有客户
		Query query = s.createQuery("from Customer");
		List<Customer> cs = query.list();
		//获取每个客户的所有订单
		for(Customer c : cs){
			System.out.println(c);
			System.out.println(c.getOrders());
		}	
		tx.commit();
		s.close();
	}
	
	
	/* 当fetch属性取值是join的时候，lazy属性就失效了。
	 * 原因是：
	 * 	 由于join是左外连接，一次就查询两张表，就相当于把两张表的符合条件的数据都查询出来了。
	 *  所以就没有延迟加载的概念了。
	 * 
	 * 需求：
	 * 	查询一个客户，获取该客户的所有订单
	 * 查询的时机：
	 * 	 lazy失效了
	 * 查询的方式：
	 * 		左外连接  
	*/	
	@Test
	public void test1(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//查询一个客户
		Customer c1 = s.get(Customer.class, 1);//客户说的是类级别的检索策略
		System.out.println(c1);
		//获取当前客户的所有订单
		System.out.println(c1.getOrders());
		tx.commit();
		s.close();
	}
	 
}














