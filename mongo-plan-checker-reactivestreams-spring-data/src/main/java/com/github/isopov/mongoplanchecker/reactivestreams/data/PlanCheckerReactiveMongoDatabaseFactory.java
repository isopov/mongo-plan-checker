package com.github.isopov.mongoplanchecker.reactivestreams.data;

import com.github.isopov.mongoplanchecker.reactivestreams.PlanCheckerMongoDatabase;
import com.mongodb.ClientSessionOptions;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import reactor.core.publisher.Mono;

public class PlanCheckerReactiveMongoDatabaseFactory implements ReactiveMongoDatabaseFactory {
  private final ReactiveMongoDatabaseFactory f;

  public PlanCheckerReactiveMongoDatabaseFactory(ReactiveMongoDatabaseFactory f) {
    this.f = f;
  }

  @Override
  public MongoDatabase getMongoDatabase() throws DataAccessException {
    return new PlanCheckerMongoDatabase(f.getMongoDatabase());
  }

  @Override
  public MongoDatabase getMongoDatabase(String dbName) throws DataAccessException {
    return new PlanCheckerMongoDatabase(f.getMongoDatabase(dbName));
  }

  @Override
  public PersistenceExceptionTranslator getExceptionTranslator() {
    return f.getExceptionTranslator();
  }

  @Override
  public CodecRegistry getCodecRegistry() {
    return f.getCodecRegistry();
  }

  @Override
  public Mono<ClientSession> getSession(ClientSessionOptions options) {
    return f.getSession(options);
  }

  @Override
  public PlanCheckerReactiveMongoDatabaseFactory withSession(ClientSession session) {
    return new PlanCheckerReactiveMongoDatabaseFactory(f.withSession(session));
  }
}
