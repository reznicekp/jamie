package com.reznicek.jamie.config;

/**
 * Uncomment all beans and annotations in case of embedded database.
 * In case of MySQL database this configuration is not necessary.
 */
// @Configuration
// @EnableJpaRepositories(basePackages = { "com.reznicek.jamie" })
// @EnableTransactionManagement
public class DatabaseConfiguration {

	// @Bean
	// public DataSource dataSource() {
	// EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
	// builder.setType(EmbeddedDatabaseType.HSQL);
	// builder.addDefaultScripts();
	// return builder.build();
	// return dataSource;
	// }
	//
	// @Bean
	// public EntityManagerFactory entityManagerFactory() {
	// HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	// vendorAdapter.setGenerateDdl(true);
	//
	// LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	// factory.setJpaVendorAdapter(vendorAdapter);
	// factory.setPackagesToScan("com.reznicek.jamie");
	// factory.setDataSource(dataSource());
	// factory.afterPropertiesSet();
	//
	// return factory.getObject();
	// }
	//
	// @Bean
	// public PlatformTransactionManager transactionManager() {
	// JpaTransactionManager txManager = new JpaTransactionManager();
	// txManager.setEntityManagerFactory(entityManagerFactory());
	// return txManager;
	// }
}