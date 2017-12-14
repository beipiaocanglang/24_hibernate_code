package study.javaProject.domain;

import java.io.Serializable;
/**
 * 
 * @author canglang
 * 一个客户有多个订单
 * 多个订单可以同属于一个客户
 * 
 * 所以客户预订单的关系是一对多
 */
public class Order implements Serializable {
	private Integer id;
	private String ordernum;
	private Float money;
	
	//多对一关系映射：多个订单属于一个客户。
	private Customer customer;

	
	public Order(){}
	
	public Order(Integer id, Float money) {
		super();
		this.id = id;
		this.money = money;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", ordernum=" + ordernum + ", money="
				+ money + "]";
	}
}
