package com.github.isopov.mongoplanchecker.core;

import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

public class PlanChecker {

  public static Bson explainModifier() {
    return new Document("$explain", true);
  }

  public static Violations getViolations(Document plan) {
    return getViolations(plan, 0);
  }

  public static Violations getViolations(Document plan, int skip) {
    Document queryPlanner = (Document) plan.get("queryPlanner");
    if (queryPlanner == null) {
      throw new NotExplainException(plan);
    }
    Violations.Builder resultBuilder = new Violations.Builder();
    checkExcessRead((Document) plan.get("executionStats"), skip, resultBuilder);
    traverseStage((Document) queryPlanner.get("winningPlan"), resultBuilder);

    return resultBuilder.build();
  }

  private static void checkExcessRead(
      Document executionStats, int skip, Violations.Builder violationsBuilder) {
    Integer nReturned = (Integer) executionStats.get("nReturned");
    Integer totalDocsExamined = (Integer) executionStats.get("totalDocsExamined");
    Integer totalKeysExamined = (Integer) executionStats.get("totalKeysExamined");
    int needExamine = nReturned + skip + 1; // to work around zero returning
    if (needExamine < totalDocsExamined / 4 || needExamine < totalKeysExamined / 8) {
      violationsBuilder.excessRead = true;
    }
  }

  private static void traverseStage(Document inputStage, Violations.Builder violationsBuilder) {
    List<Document> shards = (List<Document>) inputStage.get("shards");
    if (shards != null) {
      if (shards.size() > 1) {
        violationsBuilder.broadcast = true;
      }
      traverseStage((Document) shards.get(0).get("winningPlan"), violationsBuilder);
      return;
    }

    if ("COLLSCAN".equals(inputStage.get("stage"))) {
      violationsBuilder.collscans++;
    }
    if ("SORT".equals(inputStage.get("stage"))) {
      violationsBuilder.sorts++;
    }

    inputStage = (Document) inputStage.get("inputStage");
    if (inputStage != null) {
      traverseStage(inputStage, violationsBuilder);
    }
  }
}
