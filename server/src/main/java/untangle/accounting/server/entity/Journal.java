package untangle.accounting.server.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="journal")
public class Journal {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@OneToOne
	private Account debit_account;
	@OneToOne
	private Account credit_account;
	private String description;
	
}
