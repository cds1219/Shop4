package sec01.ex01;

public class MemberVO {
	private String id;
	private String pw;
	private String name;
	private String address;
	
	public MemberVO() {
		System.out.println("MemberVO 생성자 호출");
	}
	
	

	public MemberVO(String id, String pw, String name, String address) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.address = address;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
}
