package untangle.accounting.server.repository;

import org.springframework.data.repository.CrudRepository;

import untangle.accounting.server.entity.Journal;

public interface JournalRepository extends CrudRepository<Journal, Long> {

}
