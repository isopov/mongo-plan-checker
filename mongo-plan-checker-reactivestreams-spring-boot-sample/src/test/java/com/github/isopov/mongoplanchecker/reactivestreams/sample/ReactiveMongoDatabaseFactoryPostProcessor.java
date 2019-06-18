package com.github.isopov.mongoplanchecker.reactivestreams.sample;

import com.github.isopov.mongoplanchecker.reactivestreams.data.PlanCheckerReactiveMongoDatabaseFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.stereotype.Component;

@Component
public class ReactiveMongoDatabaseFactoryPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof ReactiveMongoDatabaseFactory) {
      return new PlanCheckerReactiveMongoDatabaseFactory((ReactiveMongoDatabaseFactory) bean);
    }
    return bean;
  }
}
