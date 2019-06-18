package com.github.isopov.mongoplanchecker.sync.sample;

import com.github.isopov.mongoplanchecker.sync.data.PlanCheckerMongoDbFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

@Component
public class MongoDbFactoryPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof MongoDbFactory) {
      return new PlanCheckerMongoDbFactory((MongoDbFactory) bean);
    }
    return bean;
  }
}
