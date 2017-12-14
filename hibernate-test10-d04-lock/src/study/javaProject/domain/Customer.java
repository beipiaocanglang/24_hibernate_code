package study.javaProject.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/**
 * 客户的实体模型
 * @author canglang
 *
 * 一个客户有多个订单
 * 多个订单可以同属于一个客户
 * 
 * 所以客户预订单的关系是一对多
 */
public class Customer implements Serializable {

	private Integer id;
	private String name;
	private Integer age;
	private Integer version;
	/**
	 * 一对多关系映射：一个客户可以有多个订单
	 * 
	 * 不实例化的情况下每一次创建都是null，所以要实例化
	 * 集合底层都是数组，
	 * 而数组的创建每一次都会默认有10个占位符
	 * 所以要给初始化成0，比较好不占用多余的空间
	 */
	private Set<Order> orders = new HashSet<Order>(0);

	
	
	public Customer(){}
	
	public Customer(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
