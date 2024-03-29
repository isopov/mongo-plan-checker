package com.github.isopov.mongoplanchecker.reactivestreams.sample;

import static org.junit.jupiter.api.Assertions.*;

import com.github.isopov.mongoplanchecker.core.BadPlanException;
import com.github.isopov.mongoplanchecker.core.PlanChecker;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleTest {

  @Autowired private PlayerRepository repository;
  @Autowired private PlanChecker checker;

  @BeforeEach
  void setup() {
    repository
        .insert(
            IntStream.range(0, 10)
                .mapToObj(
                    house ->
                        IntStream.range(0, 10)
                            .mapToObj(
                                name -> new GameOfThronesPlayer("House" + house, "Name" + name)))
                .flatMap(Function.identity())
                .collect(Collectors.toList()))
        .blockLast();
  }

  @Test
  void testCountByName() {
    assertThrows(BadPlanException.class, () -> repository.countByName("Name3").block());
  }

  @Test
  void testCountByNameWithIgnore() {
    checker.ignoreCollscan();
    assertEquals(10, repository.countByName("Name5").block());
  }

  @Test
  void testCountByHouse() {
    assertEquals(10, repository.countByHouse("House7").block());
  }

  @AfterEach
  void testDown() {
    assertFalse(checker.anyIgnores());
    // Have you ever run your tests against production DB?
    repository.deleteAll().block();
  }
}
