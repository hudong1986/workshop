package workshop.model;

import java.math.BigDecimal;

public class SalaryReport {
	private String dept_name;
	private BigDecimal toalMoney;
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public BigDecimal getToalMoney() {
		return toalMoney;
	}
	public void setToalMoney(BigDecimal toalMoney) {
		this.toalMoney = toalMoney;
	}
}
