package com.github.isopov.mongoplanchecker.sync.data;

import com.github.isopov.mongoplanchecker.sync.PlanCheckerMongoDatabase;
import com.mongodb.ClientSessionOptions;
import com.mongodb.DB;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;

public class PlanCheckerMongoDbFactory implements MongoDbFactory {
  private final MongoDbFactory f;

  public PlanCheckerMongoDbFactory(MongoDbFactory f) {
    this.f = f;
  }

  @Override
  public MongoDatabase getDb() throws DataAccessException {
    return new PlanCheckerMongoDatabase(f.getDb());
  }

  @Override
  public MongoDatabase getDb(String dbName) throws DataAccessException {
    return new PlanCheckerMongoDatabase(f.getDb(dbName));
  }

  @Override
  public PersistenceExceptionTranslator getExceptionTranslator() {
    return f.getExceptionTranslator();
  }

  @Override
  @SuppressWarnings("deprecation")
  public DB getLegacyDb() {
    return f.getLegacyDb();
  }

  @Override
  public ClientSession getSession(ClientSessionOptions options) {
    return f.getSession(options);
  }

  @Override
  public MongoDbFactory withSession(ClientSession session) {
    return new PlanCheckerMongoDbFactory(f.withSession(session));
  }
}
