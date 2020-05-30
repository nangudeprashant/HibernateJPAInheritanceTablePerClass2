package com.javalive.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import com.javalive.entity.Account;
import com.javalive.entity.CreditAccount;
import com.javalive.entity.DebitAccount;

/**
 * In a Table per class inheritance strategy, each concrete subclass has its own
 * table containing both the subclass and the base class properties. If you
 * don’t need polymorphic queries or relationships, the table per class strategy
 * is most likely the best fit. It allows you to use constraints to ensure data
 * consistency and provides an option of polymorphic queries. But keep in mind,
 * that polymorphic queries are very complex for this table structure and that
 * you should avoid them. 
 * Example:Use @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
 */
public class HibernateUtil {

	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

				Map<String, Object> settings = new HashMap<>();
				settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
				settings.put(Environment.URL, "jdbc:mysql://localhost:3306/test1?useSSL=false");
				settings.put(Environment.USER, "root");
				settings.put(Environment.PASS, "root");
				settings.put(Environment.HBM2DDL_AUTO, "create");
				settings.put(Environment.SHOW_SQL, "true");

				registryBuilder.applySettings(settings);
				registry = registryBuilder.build();

				MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Account.class)
						.addAnnotatedClass(CreditAccount.class).addAnnotatedClass(DebitAccount.class);

				Metadata metadata = sources.getMetadataBuilder().build();

				// To apply logging Interceptor using session factory
				sessionFactory = metadata.getSessionFactoryBuilder()
						// .applyInterceptor(new LoggingInterceptor())
						.build();
			} catch (Exception e) {
				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
				}
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}
