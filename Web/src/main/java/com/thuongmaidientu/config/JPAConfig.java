package com.thuongmaidientu.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(basePackages = {"com.thuongmaidientu.repository"})
@EnableTransactionManagement// tu dong quan ly transaction entity
public class JPAConfig {
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		//em.setPersistenceUnitName("persistence-data");
		
		em.setPackagesToScan("com.thuongmaidientu.entity");
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		

		return em;
	}

	
	@Bean
	JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory((jakarta.persistence.EntityManagerFactory) entityManagerFactory);
		return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/quanlibanhang");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		return dataSource;
	}
	
	Properties additionalProperties() {
		Properties properties = new Properties();
		//properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		//properties.setProperty("hibernate.hbm2ddl.auto", "create");
		
		//properties.setProperty("hibernate.hbm2ddl.auto", "update");
	
		properties.setProperty("hibernate.hbm2ddl.auto", "none");
		properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
		
		 
//	    // Bật logging SQL
//	    properties.setProperty("hibernate.show_sql", "true");
//	    
//	    // Format SQL để dễ đọc
//	    properties.setProperty("hibernate.format_sql", "true");
//	    
//	    // Bật log parameter bindings (để xem giá trị các tham số trong câu truy vấn)
//	    properties.setProperty("hibernate.type.descriptor.sql.BasicBinder", "trace");
		
		return properties;
	}
}