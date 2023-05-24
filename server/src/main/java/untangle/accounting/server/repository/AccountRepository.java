package untangle.accounting.server.repository;

import org.springframework.data.repository.CrudRepository;

import untangle.accounting.server.entity.Account;

public interface AccountRepository extends CrudRepository<Account,Long> {

}
