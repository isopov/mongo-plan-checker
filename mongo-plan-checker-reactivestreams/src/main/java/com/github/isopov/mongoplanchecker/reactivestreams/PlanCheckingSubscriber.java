package com.github.isopov.mongoplanchecker.reactivestreams;

import com.github.isopov.mongoplanchecker.core.BadPlanException;
import com.github.isopov.mongoplanchecker.core.PlanChecker;
import com.github.isopov.mongoplanchecker.core.Violations;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

class PlanCheckingSubscriber<T> implements Subscriber<T> {
  private final Subscriber<?> mainSubscriber;
  private final int skip;
  private final PlanChecker checker;
  private final Runnable mainAction;

  PlanCheckingSubscriber(
      Subscriber<?> mainSubscriber, int skip, PlanChecker checker, Runnable mainAction) {
    this.mainSubscriber = mainSubscriber;
    this.skip = skip;
    this.checker = checker;
    this.mainAction = mainAction;
  }

  PlanCheckingSubscriber(Subscriber<?> mainSubscriber, PlanChecker checker, Runnable mainAction) {
    this.mainSubscriber = mainSubscriber;
    this.skip = 0;
    this.checker = checker;
    this.mainAction = mainAction;
  }

  @Override
  public void onSubscribe(Subscription s) {
    s.request(1);
  }

  @Override
  public void onNext(T tResult) {
    Document plan = (Document) tResult;
    Violations violations = checker.getViolations(plan, skip);
    if (violations.any()) {
      mainSubscriber.onError(new BadPlanException(plan, violations));
      mainSubscriber.onComplete();
    } else {
      mainAction.run();
    }
  }

  @Override
  public void onError(Throwable t) {
    mainSubscriber.onError(t);
    mainSubscriber.onComplete();
  }

  @Override
  public void onComplete() {
    // do nothing
  }
}
