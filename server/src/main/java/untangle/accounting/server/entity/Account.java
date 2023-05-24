package untangle.accounting.server.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import untangle.accounting.data.AccountType;

@Entity
@Table(name="account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountType type;
	
	@Column(name="name",nullable = false)
	private String name;
	
	@Column(name="number", unique = true, nullable = false)
	private String number;
	
	private Account() {
		
	}
	
	public Account(AccountType type, String number, String name) {
		this.type = type;
		this.number = number;
		this.name = name;
	}
	
	public AccountType getType() {
		return type;
	}
	
	public String getName() {
		return  name;
	}
	
	public String getNumber() {
		return number;
	}

	public Long getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(type,number,name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Account other) {
			return Objects.equals(type, other.type) && 
				   Objects.equals(number, other.number) && 
				   Objects.equals(name, other.name);
		} else {
			return false;
		}
	}
}
