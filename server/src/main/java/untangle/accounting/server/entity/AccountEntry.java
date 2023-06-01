package untangle.accounting.server.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AccountEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private Double debit;
	private Double credit;
	
	private LocalDateTime executedAt;
	
	private String account;
	
	private String description;
	
	private Long collerationId;
	
	private AccountEntry() {
		
	}
	
	public AccountEntry(String account, Long collerationId, LocalDateTime executedAt, Double debit, Double credit, String description) {
		this.account = account;
		this.collerationId = collerationId;
		this.executedAt = executedAt;
		this.debit = debit;
		this.credit = credit;
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Double getDebit() {
		return debit;
	}

	public Double getCredit() {
		return credit;
	}

	public LocalDateTime getExecutedAt() {
		return executedAt;
	}

	public String getAccount() {
		return account;
	}

	public String getDescription() {
		return description;
	}

	public Long getCollerationId() {
		return collerationId;
	}

	public void setCollerationId(Long collerationId) {
		this.collerationId = collerationId;
	}
	

}
