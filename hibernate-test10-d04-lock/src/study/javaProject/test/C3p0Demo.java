package study.javaProject.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Test;

import study.javaProject.utils.HibernateUtil;

public class C3p0Demo {

	//验证数据源是否配置成功
	@Test
	public void test(){
		
		Session session = HibernateUtil.getSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String name = connection.getClass().getName();
				System.out.println(name);//com.mchange.v2.c3p0.impl.NewProxyConnection
				connection.close();//此时的关闭是放回连接池中而非真正的关闭
			}
		});
	}
}
