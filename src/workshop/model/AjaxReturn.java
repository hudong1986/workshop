package workshop.model;

public class AjaxReturn {

	private int code=0;
	private String retMsg;
	private int retInt1;
	private int retInt2;
	private String retStr1;
	private String retStr2;
	private Object object1;
	private Object object2;
	 
	public AjaxReturn(){
		
		
	}
	public AjaxReturn(int code,String msg){
		this.code = code;
		this.retMsg = msg;
	}
	
	public AjaxReturn(int code,String msg,int i1,int i2,String st1,String str2,Object ob1,Object ob2){
		this.code = code;
		this.retMsg = msg;
		this.setRetInt1(i1);
		this.setRetInt2(i2);
		this.setRetStr1(st1);
		this.setRetStr2(str2);
		this.object1=ob1;
		this.object2 = ob2;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Object getObject1() {
		return object1;
	}

	public void setObject1(Object object1) {
		this.object1 = object1;
	}

	public Object getObject2() {
		return object2;
	}

	public void setObject2(Object object2) {
		this.object2 = object2;
	}
	public int getRetInt1() {
		return retInt1;
	}
	public void setRetInt1(int retInt1) {
		this.retInt1 = retInt1;
	}
	public int getRetInt2() {
		return retInt2;
	}
	public void setRetInt2(int retInt2) {
		this.retInt2 = retInt2;
	}
	public String getRetStr1() {
		return retStr1;
	}
	public void setRetStr1(String retStr1) {
		this.retStr1 = retStr1;
	}
	public String getRetStr2() {
		return retStr2;
	}
	public void setRetStr2(String retStr2) {
		this.retStr2 = retStr2;
	}
}
