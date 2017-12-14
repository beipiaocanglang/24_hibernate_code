package study.javaProject.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 一个学生的实体模型
 * @author canglang
 * 先手动创建数据库
 * create table student(
 * 	 id int primary key auto_increment,
 *	 name varchar(100),
 *	 gender varchar(10),
 *	 birthday date
 *	);
 */
public class Student implements Serializable {

	private Integer id;
	private String name;
	private String gender;
	private Date birthday;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", gender=" + gender + ", birthday=" + birthday + "]";
	}
}
