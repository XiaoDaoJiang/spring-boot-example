package com.xiaodao.jta.config;

import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.autoconfigure.transaction.jta.JtaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.io.File;
import java.util.Properties;

/**
 * @see org.springframework.boot.autoconfigure.transaction.jta.AtomikosJtaConfiguration
 */
@Configuration
@EnableConfigurationProperties({AtomikosProperties.class, JtaProperties.class})
public class JtaAtomikosConfig {


    @Bean(initMethod = "init", destroyMethod = "shutdownWait")
    @ConditionalOnMissingBean(UserTransactionService.class)
    public UserTransactionServiceImp userTransactionService(AtomikosProperties atomikosProperties,
                                                            JtaProperties jtaProperties) {
        Properties properties = new Properties();
        if (StringUtils.hasText(jtaProperties.getTransactionManagerId())) {
            properties.setProperty("com.atomikos.icatch.tm_unique_name", jtaProperties.getTransactionManagerId());
        }
        properties.setProperty("com.atomikos.icatch.log_base_dir", getLogBaseDir(jtaProperties));
        properties.putAll(atomikosProperties.asProperties());
        return new UserTransactionServiceImp(properties);
    }

    private String getLogBaseDir(JtaProperties jtaProperties) {
        if (StringUtils.hasLength(jtaProperties.getLogDir())) {
            return jtaProperties.getLogDir();
        }
        File home = new ApplicationHome().getDir();
        return new File(home, "transaction-logs").getAbsolutePath();
    }


    @Bean(initMethod = "init", destroyMethod = "close")
    @ConditionalOnMissingBean(TransactionManager.class)
    public UserTransactionManager atomikosTransactionManager(UserTransactionService userTransactionService) throws Exception {
        UserTransactionManager manager = new UserTransactionManager();
        manager.setStartupTransactionService(false);
        manager.setForceShutdown(true);
        return manager;
    }

    @Bean(name = "jtaTransactionManager")
    public JtaTransactionManager transactionManager(UserTransaction userTransaction, TransactionManager transactionManager,
                                                    ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, transactionManager);
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(jtaTransactionManager));
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }


}
