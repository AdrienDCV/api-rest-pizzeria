package dto;

public class Users {

	private int idclient;
	private String name;
	private String password;
	private String token;
	private String address;
	private String tel;
	
	
	public Users() {
		
	}
	public Users(int id,String name,String password, String token,String address,String tel) {
		this.idclient=id;
		this.name=name;
		this.password=password;
		this.token=token;
		this.address=address;
		this.tel=tel;
	}
	public int getIdclient() {
		return idclient;
	}
	
	public void setIdclient(int idclient) {
		this.idclient = idclient;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTel() {
		return tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
