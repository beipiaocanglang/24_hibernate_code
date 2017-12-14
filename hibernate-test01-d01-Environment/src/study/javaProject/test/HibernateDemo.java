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
 * hibernate的第一个入门案例
 * @author canglang
 */
public class HibernateDemo {
	/**
	 * 步骤分析：
	 * 	1、加载hibernate.cfg.xml主配置文件
	 * 	2、根据配置文件构建sessionFactory工厂
	 * 	3、使用session工厂生产一个session
	 * 	4、开启事物
	 * 	5、保存一个学生类到数据库
	 * 	6、提交事物
	 * 	7、释放资源
	 */
	@Test
	public void test1(){
		//首先准备数据源
		Student student = new Student();
		student.setName("苍狼");
		student.setGender("男");
		student.setBirthday(new Date());
		
		//1、加载hibernate.cfg.xml主配置文件
		Configuration configuration = new Configuration();
		configuration.configure();
	   /*
		*手动指定实体
		*	方法一：直接在hibernate的主配置文件(hibernate.cfg.xml)中配置(推荐使用)
		*		例如：<mapping resource="study/javaProject/domain/Student.hbm.xml"/>
		*	方法二：在java代码中使用(指定实体类)
		*		例如：configuration.addClass(Student.class);
		*	方法三：在java代码中使用(指定实体类的字节码文件)
		*		例如：configuration.addResource("study/javaProject/domain/Student.hbm.xml");
		*/
		//configuration.addClass(Student.class);
		//configuration.addResource("study/javaProject/domain/Student.hbm.xml");
		//2、根据配置文件构建sessionFactory工厂
		SessionFactory factory = configuration.buildSessionFactory();
		//3、使用session工厂生产一个session
		Session session = factory.openSession();
		//4、开启事物
		Transaction transaction = session.beginTransaction();
		//5、保存一个学生类到数据库
		session.save(student);
		//6、提交事物
		transaction.commit();
		//7、释放资源
		session.close();
		factory.close();
	}
	
	/**
	 * Configuration：
	 * 		作用：就是用于加载配置文件。它默认只会加载类的根路径下的配置文件。
	 * 		常用方法：
	 * 			默认构造函数：它默认只会加载类的根路径下的hibernate.properties配置文件。不会加载xml文件。
	 * 			configure：该方法会加载类的根路径下的hibernate.cfg.xml配置文件。
	 * 			addClass：手动指定实体类的字节码。目的也是指定映射文件。要求，映射文件的命名必须符合规范。实体类名称.hbm.xml
	 * 			addResource：手动指定映射文件的位置。
	 * 		总结：
	 * 			建议使用配置文件来指定映射文件的位置。
	 * 
	 * SessionFactory：
	 * 		作用：就是用于生产Session对象的。工厂的创建，是由Configuration对象来创建的。
	 * 		常用的方法：
	 * 			openSession()：每次打开一个新的Session对象。
	 * 		说明：
	 * 			1、该对象维护着应用中所有的映射文件信息(配置文件中字段与实体类中的字段或数据库中的字段的对应)
	 * 			2、它还维护类一些预定义的SQL语句。(只包含每个实体都通用的：全字段插入，全字段更新，根据ID查询，查询所有)
	 * 			3、维护着连接数据库的信息，hibernate的基本配置
	 * 			4、还维护着hibernate的二级缓存（关于二级缓存的内容，第四天再讲）
	 * 			5、所有由该工厂生产的Session，共享工厂中的数据。结论：SessionFactory是线程安全的,意味着它的一个实例可以被应用的多个线程共享。
	 * 		SessionFactory的使用原则：
	 * 			SessionFactorty是一个重量级对象，里面维护了很多信息。
	 * 			不应该随意的创建和销毁。一个应用最好只有一个SessionFactory。
	 * 			最好是应用启动时就完成初始化。
	 * 			在web工程中，应该是随着应用的加载，而创建。随着应用的卸载而销毁。
	 * 
	 * Session：
	 * 		作用：对数据库进行增删改查的操作。开启事务等等。它是我们使用频率最高的一个对象。
	 * 		常用方法：
	 * 			a lot of 
	 * 			save：保存一个实体到数据库中
	 * 		说明：
	 * 			1、Session是一个轻量级的对象。绝大多数信息都是由SessionFactory维护的。
	 * 			2、它的创建和销毁不会消耗太多资源。
	 * 			3、Session对象不是线程安全的。*****
	 * 			4、Session维护着hibernate的一级缓存。
	 * 		Session的使用原则：
	 * 			保证一个线程只有一个Session。
	 * Transaction:
	 * 		作用：
	 * 			针对事务的控制，Hibernate应用通过它声明事务边界，有助于在不同的环境或容器中进行移植(例如支付时的第三方接口)
	 * 		常用方法：
	 * 			commit();提交事务
	 * 			rollback();回滚事务
	 * 			
	 */
	/**
	 * 升级版
	 */
	@Test
	public void test2(){
		//首先准备数据源
		Student student = new Student();
		student.setName("苍狼1");
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
	 * 升级版
	 */
	@Test
	public void test3(){
		//首先准备数据源
		Student student = new Student();
		student.setName("苍狼2");
		student.setGender("不详");
		student.setBirthday(new Date());
		
		//1、使用session工厂生产一个session
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			//2、开启事物
			transaction = session.beginTransaction();
			//3、保存一个学生类到数据库
			session.save(student);
			//4、提交事物
			transaction.commit();
		} catch (Exception e) {
			//出现异常时回滚事物
			transaction.rollback();
			throw new RuntimeException(e);
		}finally{
			//5、释放资源
			session.close();
		}
	}
}
