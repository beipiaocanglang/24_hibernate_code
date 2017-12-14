package study.javaProject.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import study.javaProject.domain.Student;
import study.javaProject.domain.Teacher;
import study.javaProject.utils.HibernateUtil;

public class Many2Many {

	/*
	 * 保存操作
	 * 需求：
	 * 	创建：1号学生  2号学生 3号学生
	 * 		1号教师  2号教师
	 * 	建立关联关系
	 * 		1号教师教过  1号学生和2号学生
	 * 		2号教师教过  2号学生和3号学生
	 *	执行保存
	 *
	 * 多对多关系映射，在执行保存操作时，需要有一方放弃维护的权利。
	 * 任意一方
	 */
	@Test
	public void test1(){
		//准备数据
		Teacher t1 = new Teacher();
		t1.setName("hqy");
		t1.setSalary(500f);
		
		Teacher t2 = new Teacher();
		t2.setName("lx");
		t2.setSalary(1000f);
		
		Student s1 = new Student();
		s1.setName("王占青");
		s1.setGender("male");
		
		Student s2 = new Student();
		s2.setName("秦鹏飞");
		s2.setGender("male");
		
		Student s3 = new Student();
		s3.setName("郑恒明");
		s3.setGender("male");
		
		//建立关联关系
		t1.getStudents().add(s1);
		t1.getStudents().add(s2);
		s1.getTeachers().add(t1);
		s2.getTeachers().add(t1);
		
		t2.getStudents().add(s2);
		t2.getStudents().add(s3);
		s2.getTeachers().add(t2);
		s3.getTeachers().add(t2);
		
		
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		
		//保存操作 
		s.save(t1);
		
		tx.commit();
		s.close();
	}
	
	/*
	 * 删除操作：
	 * 	删除id为1的教师
	 * 注意事项：
	 * 	在多对多的删除操作时，不要配置级联删除。
	 */
	@Test
	public void test2(){
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Teacher t1 = s.get(Teacher.class, 1);
		s.delete(t1);
		tx.commit();
		s.close();
	}
}
