package study.javaProject.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import study.javaProject.domain.Student;
import study.javaProject.utils.HibernateUtil;

/**
 * session中常用的方法
 * @author canglang
 */
public class HibernateDemo {
	
	/**
	 * save方法
	 * save方法和persist方法
	 * 	共同点：
	 * 		 都是把临时态对象转成持久态
	 * 	区别：
	 * 		1、提供者不一样
	 * 			save方法是hibernate提供的
	 * 			persist方法是JPA规范提供的	Java Persistence API
	 *  	2、在没有事务的支持下
	 *  		save方法：
	 *  			hibernate5：会用框架内置的事务执行保存操作。
	 *  			hibernate3：就会执行保存，但是没有明确的提交操作，在超时后，事务自动回滚。
	 * 			persist方法：
	 * 				什么都不去做。
	 */
	@Test
	public void testSave(){
		//准备数据源	
		Student student = new Student();
		student.setName("test1");
		student.setGender("male");
		student.setBirthday(new Date());
		
		//获取session对象
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(student);
		
		transaction.commit();
		session.close();
	}
	/**
	 * persist()方法
	 */
	@Test
	public void testPersist(){
		//准备数据源	
		Student student = new Student();
		student.setName("test");
		student.setGender("male");
		student.setBirthday(new Date());
		
		//获取session对象
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		session.persist(student);
		
		transaction.commit();
		session.close();
	}
	/**
	 * update方法
	 * 问题1：
	 * 		当我们使用update持久一个脱管态对象时，就算没有修改实体对象的数据，也会执行update语句。
	 * 解决办法：
	 * 		select-before-update
	 * 配置位置：映射文件的class元素上
	 *  	<class name="Student" table="student" select-before-update="true">
	 * 取值：
	 *  	true：在更新之前，执行查询
	 *  	false：在更新之前不执行查询。默认值
	 */
	@Test
	public void testUpdate(){
		Session s1 = HibernateUtil.getSession();
		Transaction tx1 = s1.beginTransaction();
		Student s = s1.get(Student.class,1);//持久态
		tx1.commit();
		s1.close();
		
		System.out.println(s);//脱管态：有OID，和Session没有关系
		
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		/**
		 * 在映射配置文件的class标签上配置了select-before-update属性后
		 * 在执行update方法之前会先去数据库中查询一遍数据进行对比，如果有不一样的数据，
		 * 则会执行update方法，反之，不会执行(查询的效率比更新的效率高，因为更新涉及到多个字段的赋值)
		 */
		s2.update(s);//持久态
		tx2.commit();
		s2.close();
	}
	/*
	 * 问题2：
	 * 	当我们持久化一个脱管态对象时，如果在session的一级缓存之中，已经包含了有相同OID的对象，则会报错。
	 * 	见图13
	 * 解决办法：
	 * 	使用使用merge方法
	 */
	@Test
	public void test4(){
		Session s1 = HibernateUtil.getSession();
		Transaction tx1 = s1.beginTransaction();
		Student studnet1 = s1.get(Student.class,1);//持久态
		tx1.commit();
		s1.close();
		
		System.out.println(studnet1);//脱管态：有OID，和Session没有关系
		
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		Student student2 = s2.get(Student.class, 1);//持久态
		/**
		 * session的一级缓存中已经存在id=1的student2的对象了
		 * 当我们执行update方法时会将id=1的student1对象放入一级缓存中
		 * 连个id等于1的对象会保存
		 */
		s2.update(studnet1);//把脱管态对象转成持久态
		tx2.commit();
		s2.close();
	}
	/*
	 * 问题3：
	 * 	 当我们持久化一个脱管态的对象时，如果该对象被别的程序删除了，则会报错
	 */
	@Test
	public void test5(){
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		Student student2 = s2.get(Student.class, 1);//持久态
		student2.setGender("female");
		s2.update(student2);//把脱管态对象转成持久态
		tx2.commit();
		s2.close();
	}
	/*
	 * merge方法：
	 * 	作用：合并两个对象。用脱管态对象的数据去替换持久态对象的数据。
	 */
	@Test
	public void test6(){
		Session s1 = HibernateUtil.getSession();
		Transaction tx1 = s1.beginTransaction();
		Student studnet1 = s1.get(Student.class,2);//持久态
		tx1.commit();
		s1.close();
		
		System.out.println(studnet1);//脱管态：有OID，和Session没有关系
		
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		Student student2 = s2.get(Student.class, 2);//持久态
		s2.merge(studnet1);//把脱管态对象转成持久态
		tx2.commit();
		s2.close();
	}
	/*
	 * saveOrUpdate
	 * 	作用：
	 * 		执行保存或者更新
	 * 	执行依据：
	 * 		依据的是对象的状态。
	 * 		如果对象是临时态，则执行save
	 * 		如果对象是脱管态，则执行update
	 */
	@Test
	public void test8(){
		Student s1 = new Student();//临时态：没有OID，和session没有关系
		s1.setName("test5");
		s1.setGender("male");
		s1.setBirthday(new Date());
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		s.saveOrUpdate(s1);//持久态：有OID，和session有关系
		tx.commit();
		s.close();
	}
	@Test
	public void test7(){
		Session s2 = HibernateUtil.getSession();
		Transaction tx2 = s2.beginTransaction();
		Student student2 = s2.get(Student.class, 2);//持久态
		student2.setGender("male");
		s2.saveOrUpdate(student2);//把脱管态对象转成持久态
		tx2.commit();
		s2.close();
	}	
	
}
