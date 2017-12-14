package study.javaProject.domain;

import java.io.Serializable;

public class Customer implements Serializable {

	private Integer id;
	private String fristName;
	private String lastName;
	
	//定义订单总金额字段
	private float totalPrice;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return fristName+"."+lastName;
	}
	public void setName(String name) {
		this.fristName = name.split("\\.")[0];
		this.lastName = name.split("\\.")[1];
	}
	
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", fristName=" + fristName
				+ ", lastName=" + lastName + ", totalPrice=" + totalPrice + "]";
	}
}
