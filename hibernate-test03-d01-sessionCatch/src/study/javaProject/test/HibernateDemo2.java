package study.javaProject.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Student;
import study.javaProject.utils.HibernateUtil;

/**
 *明确：
 * 	什么情况下适用于缓存，什么情况不使用。
 *  缓存多用于非敏感数据。
 *  敏感数据一般都是跟数据库实时同步。
 *  
 *  一级缓存指的是Session的缓存
 *  
 *  当session一关闭，所以一级缓存也就消失了。
 * @author canglang
 */
public class HibernateDemo2 {

	/**
	 * 证明一级缓存的存在
	 */
	@Test
	public void test1(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//执行查询，第一次会去数据库中查询(有sql语句)，并且将查询出来的结果存入session的一级缓存中
		Student student = session.get(Student.class, 2);
		System.out.println(student);
		//第二次查询同样的数据时不会去数据库中查询(没有sql语句)而是会去session的一级缓存中查询
		Student student1 = session.get(Student.class, 2);
		System.out.println(student1);
		transaction.commit();
		session.close();//Session关闭后一级缓存会消失
	}
	//修改数据
	//快照机制
	@Test
	public void test2(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Student s1 = s.get(Student.class, 2);//会去数据库查询，同时会把查询出来的数据存入一级缓存之中。
		System.out.println(s1);//gender:male
		s1.setGender("male");//改gender，其实改的是内存对象
		System.out.println(s1);//gender：female
		//s.update(s1);
		tx.commit();
		s.close();//一级缓存在此行执行之后就消失了。
		
		//在此种再接着写代码
		System.out.println(s1);//gender:female
	}
}
