package study.javaProject.test;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import study.javaProject.domain.Student;
import study.javaProject.utils.HibernateUtil;

/**
 * hibernate的CRUD
 * CRUD:增、删、改、查
 * @author canglang
 */
public class HibernateDemo1 {
	/**
	 * 添加数据
	 */
	@Test
	public void testAdd(){
		//首先准备数据源
		Student student = new Student();
		student.setName("苍狼3");
		student.setGender("女");
		student.setBirthday(new Date());
		
		//1、使用session工厂生产一个session
		Session session = HibernateUtil.getSession();
		//2、开启事物
		Transaction transaction = session.beginTransaction();
		//3、保存一个学生类到数据库
		session.save(student);
		//4、提交事物
		transaction.commit();
		//5、释放资源
		session.close();
		//sessionFactory就不用再关闭了，否则每次还需要再创建一个
		//factory.close();
	}
	/**
	 * 删除用户
	 */
	@Test
	public void testDelete(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//1、首先要根据id查询一个指定的用户
		Student student = session.get(Student.class, 1);
		//2、删除用户
		session.delete(student);
		
		transaction.commit();
		session.close();
	}
	/**
	 * 修改用户信息
	 */
	@Test
	public void testUpdata(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//1、首先要根据id查询一个指定的用户
		Student student = session.get(Student.class, 2);
		//2、删除用户
		student.setBirthday(new Date());
		student.setGender("男");
		//3、调用updata方法更新用户
		session.update(student);
		
		transaction.commit();
		session.close();
	}
	/**
	 * 查询一个用户信息
	 */
	@Test
	public void testFindOne(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//1、首先要根据id查询一个指定的用户
		Student student = session.get(Student.class, 2);
		System.out.println(student);
		
		transaction.commit();
		session.close();
	}
	/**
	 * 查询所有用户信息
	 */
	@Test
	public void testFindAll(){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		//1.需要使用Session获取到一个查询对象
		/*
		 * createQuery中的参数是一个字符串。
		 * 它是Hibernate的查询语句。HQL。不是SQL语句。
		 * HQL : hibernate query language
		 * 写法都是和类名以及类中的属性名称（set/get方法）
		 */
		Query query = session.createQuery("from Student");
		List<Student> list = query.list();
		System.out.println(list);
		
		transaction.commit();
		session.close();
	}
}
