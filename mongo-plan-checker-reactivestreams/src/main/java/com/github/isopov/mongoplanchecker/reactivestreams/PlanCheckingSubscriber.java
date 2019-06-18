package com.github.isopov.mongoplanchecker.reactivestreams;

import static com.github.isopov.mongoplanchecker.core.PlanChecker.getViolations;

import com.github.isopov.mongoplanchecker.core.BadPlanException;
import com.github.isopov.mongoplanchecker.core.Violations;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

class PlanCheckingSubscriber<T> implements Subscriber<T> {
  private final Subscriber<?> mainSubscriber;
  private final int skip;
  private final Runnable mainAction;

  PlanCheckingSubscriber(Subscriber<?> mainSubscriber, int skip, Runnable mainAction) {
    this.mainSubscriber = mainSubscriber;
    this.skip = skip;
    this.mainAction = mainAction;
  }

  PlanCheckingSubscriber(Subscriber<?> mainSubscriber, Runnable mainAction) {
    this.mainSubscriber = mainSubscriber;
    this.skip = 0;
    this.mainAction = mainAction;
  }

  @Override
  public void onSubscribe(Subscription s) {
    s.request(1);
  }

  @Override
  public void onNext(T tResult) {
    Document plan = (Document) tResult;
    Violations violations = getViolations(plan, skip);
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
