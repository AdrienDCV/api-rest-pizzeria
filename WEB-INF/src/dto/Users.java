package dto;

public class Users {

	private int idclient;
	private String login;
	private String password;
	private String email;
	private String address;
	private String tel;
	
	
	public Users() {
		
	}
	public Users(int id,String login,String password, String email,String address,String tel) {
		this.idclient=id;
		this.login=login;
		this.password=password;
		this.email=email;
		this.address=address;
		this.tel=tel;
	}
	public int getIdclient() {
		return idclient;
	}
	
	public void setIdclient(int idclient) {
		this.idclient = idclient;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
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
