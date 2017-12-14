package study.javaProject.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Order;
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
 * 明确： 有多的一方要不要查询少的一方
 * 举例说明：
 * 		有订单，要不要查询客户/有人员，要不要查询身份证号。
 * 解决的问题：
 * 	1、查询的时机
 * 		立即加载：不管用不用，都马上查询出来
 * 		延迟加载：什么时候用，什么时候真正去查询
 *  2、查询的方式（怎么查）
 *  	多条SQL语句
 *  	表连接
 * 关于关联级别的检索策略,有如下配置
 * 配置涉及的属性：
 * 	lazy：加载的时机
 * 		false：立即加载。不管对方类级别的检索策略
 * 		proxy：不确定。原因是要看对方类级别的检索策略。
 * 			      如果对方类级别检索策略是延迟加载，则proxy就是延迟加载。
 * 			      如果对方类级别检索策略是立即记载，则proxy就是立即加载。
 * 	fetch：检索的方式
 * 		select：多条SQL语句
 * 		join：左外连接
 * 
 * 我们以多对一为例：配置都是在many-to-one上!
 * @author zhy
 * 
 * 第二种情况：lazy=false fetch=select
 * <many-to-one name="customer" class="Customer" column="CUSTOMER_ID" lazy="false" fetch="select"/>
 * 
 */
public class HibernateDemo10 {
	/* 
	 * 需求：
	 * 	查询一个订单，根据订单获取该订单的客户
	 * 查询的时机：
	 * 	立即加载
	 * 查询的方式：
	 * 	多条SQL语句。
	 */	
	@Test
	public void test1(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		//查询订单
		Order o1 = s.get(Order.class, 1);
		System.out.println(o1);
		//根据订单，获取客户信息
		System.out.println(o1.getCustomer());
		tx.commit();
		s.close();
	}
	 
}














