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

/**
 * 丢失更新
 * 注意：
 * 	靠事务控制，是无法避免的。
 * 解决办法：
 * 	1、hibernate中的悲观锁
 * 		它不是锁，是一种处理丢失更新的态度。
 * 		hibernate认为丢失更新一定会发生。
 * 		hibernate运用的是数据库的锁机制（数据库的锁是真正的锁）
 *  2、hibernate中的乐观锁
 *  	它不是锁，也是一种处理丢失更新的态度。
 *  	hibernate认为丢失更新不一定会发生。
 *  	hibernate是通过使用版本号的方式来控制：
 *  		a、需要在实体类中添加一个字段，该字段必须是Integer，并且提供get和set方法
 *  		b、在映射文件中把版本字段映射出来
 *  			<!-- 版本字段，用于解决丢失更新（版本好的映射只能写在id后面，不能写在其他位置） -->
 *			<version name="version"></version>
 *	注意：
 *		版本字段，不需要我们自己维护，而是由hibernate维护即可。
 * @author canglang
 *
 */
public class HibernateDemo {
	//线程1
	@Test
	public void test1(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		/*
		 * 第一种结局方案：hibernate的悲观锁机制
		 * Customer c1 = s.get(Customer.class, 1,LockMode.PESSIMISTIC_WRITE);//hibernate处理丢失更新的悲观锁解决方式
		 */
		//第二种在实体对象中添加一个版本号的机制
		Customer c1 = s.get(Customer.class, 1);
		c1.setName("泰斯特");
		tx.commit();
		s.close();
	}
	
	//线程2
	@Test
	public void test2(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		/*
		 * 第一种解决方案：hibernate的悲观锁机制
		 * Customer c1 = s.get(Customer.class, 1,LockMode.PESSIMISTIC_WRITE);
		 */
		//第二种在实体对象中添加一个版本号的机制
		Customer c1 = s.get(Customer.class, 1);
		c1.setAge(20);
		tx.commit();
		s.close();
	}
}














