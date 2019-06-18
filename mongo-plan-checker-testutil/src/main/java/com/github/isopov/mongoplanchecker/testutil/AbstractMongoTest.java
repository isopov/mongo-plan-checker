package com.github.isopov.mongoplanchecker.testutil;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractMongoTest {
  protected static final int MONGO_PORT = 27017;

  @Container
  protected static final GenericContainer MONGO =
      new GenericContainer("mongo:4.1").withExposedPorts(MONGO_PORT).withCommand("--replSet rs0");

  @BeforeAll
  static void setupMongoContainer() throws IOException, InterruptedException {
    MONGO.execInContainer("/bin/bash", "-c", "mongo --eval 'printjson(rs.initiate())' --quiet");
  }
}
