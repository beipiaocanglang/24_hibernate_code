package study.javaProject.domain.many2many;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/**
 * 一个老师的实体模型
 * @author canglang
 *	一个学生可以被多个老师教过
 *	一个老师可以教多个学生
 *	老师和学生的关系是多对多
 */
public class Teacher implements Serializable {

	private Integer id;
	private String name;
	private Float salary;
	
	/*
	 * 多对多关系映射
	 * 
	 * 不实例化的情况下每一次创建都是null，所以要实例化
	 * 集合底层都是数组，
	 * 而数组的创建每一次都会默认有10个占位符
	 * 所以要给初始化成0，比较好不占用多余的空间
	 */
	private Set<Student> students = new HashSet<Student>(0);

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
	public Float getSalary() {
		return salary;
	}
	public void setSalary(Float salary) {
		this.salary = salary;
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	@Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", salary=" + salary
				+ "]";
	}
}
