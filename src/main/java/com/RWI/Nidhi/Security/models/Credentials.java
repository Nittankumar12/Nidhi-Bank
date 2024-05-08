// User.java
package com.RWI.Nidhi.Security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email"),
      @UniqueConstraint(columnNames = "phone_number")
    })

public class Credentials {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  
  private String username;

//  @NotBlank
//  @Size(max = 50)
  @Email
  private String email;
  
//  @NotBlank
//  @Size(max = 15)
  @Column(name = "phone_number")
  private String phoneNumber;

//  @NotBlank
//  @Size(max = 120)
  private String password;
  
  private String designation;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public Credentials() {
  }

  public Credentials(String username, String email, String phoneNumber, String password) {
    this.username = username;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
  }
  


public Credentials(Long id, @Size(max = 20) String username, @Size(max = 50) @Email String email,
                   @Size(max = 15) String phoneNumber, @Size(max = 120) String password, String designation) {
	super();
	this.id = id;
	this.username = username;
	this.email = email;
	this.phoneNumber = phoneNumber;
	this.password = password;
	this.designation = designation;
}

public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }


public void setDesignation(String designation) {
	this.designation = designation;
}
public String getDesignation() {
	return designation;
}

}
