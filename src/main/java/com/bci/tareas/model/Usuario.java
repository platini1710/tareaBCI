package com.bci.tareas.model;


import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



@Entity
@Table(name = "usuario" )
@EntityListeners(AuditingEntityListener.class)

public class Usuario implements Serializable {

    @Id
    private String id;



    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "fcreated", nullable = true)
    private String created;
    @Column(name = "lastlogin", nullable = true)
    private String lastLogin;
    @Column(name = "token", nullable = true ,length = 300)
    private String token;
    @Column(name = "isactive",columnDefinition = "boolean  default true", nullable = true )
    private boolean  isActive;

    @OneToOne(fetch=FetchType.EAGER, orphanRemoval = true, cascade = { javax.persistence.CascadeType.ALL })
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name="id",referencedColumnName = "id",nullable = true )
    private Phone phone;
    
    
    public Usuario() {

    }
    
    public Usuario( String id,String name,String password,String email,String created,String lastLogin,String token, boolean  isActive,Phone phone) {
    	this.id=id;
    	this.name=name;
    	this.password=password;
    	this.email=email;
    	this.created=created;
    	this.lastLogin=lastLogin;
    	this.token=token;
    	this.isActive=isActive;  
    	this.phone=phone;
    	
    }
    public Usuario( String id) {
    	this.id=id;
 	
    	
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public Phone getPhone() {
		return phone;
	}
	public void setPhone(Phone phone) {
		this.phone = phone;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean getActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

    
    
}