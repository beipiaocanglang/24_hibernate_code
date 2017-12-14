package study.javaProject.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Student;
import study.javaProject.utils.HibernateUtil;

/**
 *	hibernate的一级缓存常用的方法
 *	clear方法
 * 	evict方法
 * 	refresh方法
 * 	flush方法
 * @author canglang
 */
public class HibernateDemo3 {

	/*
	 * clear方法
	 * 	作用：清空一级缓存
	 * 和close有区别：
	 * 	close执行之后，一级缓存也没有了(Session也会消失)。
	 * 	clear执行完成之后，session是还可以用的。
	 */
	@Test
	public void test1(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Student s1 = s.get(Student.class, 2);//会去数据库查询，同时会把查询出来的数据存入一级缓存之中。
		System.out.println(s1);
		
		s.clear();
		
		Student s2 = s.get(Student.class, 2);
		System.out.println(s2);
		tx.commit();
		s.close();
	}
	

	/*
	 * evict方法
	 * 	作用清除的是指定实体的缓存
	 */
	@Test
	public void test2(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Student s2 = s.get(Student.class, 2);//会去数据库查询，同时会把查询出来的数据存入一级缓存之中。
		Student s3 = s.get(Student.class, 3);//会去数据库查询，同时会把查询出来的数据存入一级缓存之中。
		
		s.evict(s2);
		
		Student s4 = s.get(Student.class, 2);//会去数据库查询，同时会把查询出来的数据存入一级缓存之中。
		Student s5 = s.get(Student.class, 3);//不会去查询，因为一级缓存之中有数据
		tx.commit();
		s.close();
	}
	
	/*
	 * refresh方法
	 * 	作用：那数据库的数据，来同步一级缓存。
	 */
	@Test
	public void test3(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Student s1 = s.get(Student.class, 4);//会去数据库查询，同时会把查询出来的数据存入一级缓存之中。
		System.out.println(s1);//gender :male
		s1.setGender("female");//修改gender:female
		System.out.println(s1);
		s.refresh(s1);//会去数据库中查询一遍，发现不一样时会拿数据库中数据更新缓存中的数据
		System.out.println(s1);
		tx.commit();
		s.close();
	}
	
	/*
	 * flush方法
	 * 	作用：手动刷新缓存。把数据从session中刷新出去。
	 */
	@Test
	public void test5(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Student s1 = s.get(Student.class, 6);//会去数据库查询，同时会把查询出来的数据存入一级缓存之中。
		System.out.println(s1);//gender :male
		s1.setGender("male");//修改gender:female
		System.out.println(s1);
		
		s.flush();//当有此行存在时，此时就会看到update语句(但是数据库中的数据还没有更新，在执行了下面的commit方法后才会执行)
		
		tx.commit();//默认此行刷出缓存中的数据。去拿缓存修改数据库的数据
		s.close();
	}
}
