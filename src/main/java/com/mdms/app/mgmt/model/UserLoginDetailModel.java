package com.mdms.app.mgmt.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity()
@Table(name="user_login_detail", schema="mdms_app_mgmt")
public class UserLoginDetailModel {
	
	
	@Id
	@Column(name="user_id")
	private String user_id;
	
	
	@Column(name="email")
	private String email;

	@Column(name="emp_password")
	private String emp_password;
	
	@Column(name="role_type")
	private String role_type;
	
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:ss")
	@Column(name="txn_timestamp")
	private Timestamp txn_timestamp;
	
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:Ss")
	@Column(name="last_password_change_txn_timestamp")
	private Timestamp last_password_change_txn_timestamp;
	
	
	@Column(name="user_register_approval")
	private String user_register_approval;
	
	
	


	public String getUser_register_approval() {
		return user_register_approval;
	}


	public void setUser_register_approval(String user_register_approval) {
		this.user_register_approval = user_register_approval;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEmp_password() {
		return emp_password;
	}


	public void setEmp_password(String emp_password) {
		this.emp_password = emp_password;
	}


	public String getRole_type() {
		return role_type;
	}


	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}


	public Timestamp getTxn_timestamp() {
		return txn_timestamp;
	}


	public void setTxn_timestamp(Timestamp txn_timestamp) {
		this.txn_timestamp = txn_timestamp;
	}


	public Timestamp getLast_password_change_txn_timestamp() {
		return last_password_change_txn_timestamp;
	}


	public void setLast_password_change_txn_timestamp(Timestamp last_password_change_txn_timestamp) {
		this.last_password_change_txn_timestamp = last_password_change_txn_timestamp;
	}


	@Override
	public String toString() {
		return "UserLoginDetailModel [user_id=" + user_id + ", email=" + email + ", emp_password=" + emp_password
				+ ", role_type=" + role_type + ", txn_timestamp=" + txn_timestamp
				+ ", last_password_change_txn_timestamp=" + last_password_change_txn_timestamp
				+ ", user_register_approval=" + user_register_approval + "]";
	}



	
	
	
	

}
