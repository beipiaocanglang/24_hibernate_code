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
 * 		extra：极懒加载
 * 	fetch：检索的方式
 * 		select：多条SQL语句(默认值)
 * 		subselect：子查询
 * 		join：左外连接
 * 
 * 我们现在是在映射文件的set元素上配置！
 * 
 * @author zhy
 * 
 * 第一种情况：都取默认值
 * <set name="orders" table="T_ORDERS" lazy="true" fetch="select">
 *
 */
public class HibernateDemo2 {
	
	/*
	 * 需求：
	 * 	查询所有客户，获取每个客户的所有订单
	 * 查询的时机：
	 * 	  延迟加载
	 * 查询的方式：
	 * 	 多条SQL语句。
	 	 多条SQL语句的一个问题：n+1问题。我们有4个客户，查询客户的订单，有4条SQL语句就够了，但是实际上是执行了5条。
	 		select * from T_CUSTOMERS ;
			select * from T_ORDERS  where CUSTOMER_ID=1;
			select * from T_ORDERS  where CUSTOMER_ID=2;
			select * from T_ORDERS  where CUSTOMER_ID=3;
			select * from T_ORDERS  where CUSTOMER_ID=4;
		解决办法：
			在set元素上配置一个检索深度的属性：batch-size。其实就是在参数中对应的问号个数。
			例如：
				<set name="orders" table="T_ORDERS" lazy="true" fetch="select" batch-size="4">
			特殊说明：
				batch-size的使用一般不建议太大。因为有分页的存在。建议取值是3~10之间。
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
	
	
	/*
	 * 需求：
	 * 	查询一个客户，获取该客户的所有订单
	 * 查询的时机：
	 * 		延迟加载
	 * 查询的方式：
	 * 		多条SQL语句。
	 * 		一条查询客户。
	 * 		一条查询订单。
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














