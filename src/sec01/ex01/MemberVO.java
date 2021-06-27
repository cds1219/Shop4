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
		setId(id);
		setPw(pw);
		setName(name);
		setAddress(address);
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(id!=null) {
			this.id = id;
		}else {
			System.out.println("id를 입력해주세요.");
		}
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		if(id!=null) {
			this.pw = pw;
		}else {
			System.out.println("pw를 입력해주세요.");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(id!=null) {
			this.name = name;
		}else {
			System.out.println("name를 입력해주세요.");
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if(id!=null) {
			this.address = address;
		}else {
			System.out.println("address를 입력해주세요.");
		}
	}

	
}
