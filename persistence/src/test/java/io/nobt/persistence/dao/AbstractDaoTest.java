package io.nobt.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.HibernateException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

public abstract class AbstractDaoTest extends Assert {

	private static EntityManagerFactory entityManagerFactory;
	protected static EntityManager entityManager;

	@BeforeClass
	public static void initEntityManager() throws HibernateException {
		entityManagerFactory = Persistence.createEntityManagerFactory("persistence-test");
		entityManager = entityManagerFactory.createEntityManager();
	}

	@AfterClass
	public static void closeEntityManager() {
		entityManager.close();
		entityManagerFactory.close();
	}

}
