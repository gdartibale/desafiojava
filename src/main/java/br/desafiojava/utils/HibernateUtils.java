
package br.desafiojava.utils;

import br.desafiojava.entity.Phone;
import br.desafiojava.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {

	private static SessionFactory sessionFactory;

	static {
		Configuration c = new Configuration();

		c.addAnnotatedClass(User.class);
		c.addAnnotatedClass(Phone.class);
		c.addPackage(User.class.getPackage().getName());

		c.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect");
		c.setProperty(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver");
		c.setProperty(AvailableSettings.USER, "root");
		c.setProperty(AvailableSettings.PASS, "asdasd33");
		c.setProperty(AvailableSettings.URL, "jdbc:mysql://localhost:3306/desafiojava");

		c.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");

		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(c.getProperties()).build();

		sessionFactory = c.buildSessionFactory(registry);

	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}