package untangle.accounting.server;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import untangle.accounting.server.entity.EntityBase;
import untangle.accounting.server.repository.RepositoryBase;

@Configuration
@EnableJpaRepositories(basePackageClasses = {RepositoryBase.class})
@EntityScan(basePackageClasses = EntityBase.class)
@EnableJpaAuditing
public class DatabaseConfiguration {

}
