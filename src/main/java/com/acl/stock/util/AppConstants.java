package com.acl.stock.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;



@SuppressWarnings("NullableProblems")
@Data
@Component
@Slf4j
public class AppConstants implements ApplicationContextAware {

    private static ApplicationContext context;
    public static final String APP_CONTEXT = "/";

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        AppConstants.context = context;
    }

    public interface ApiResponseMessage {
        String SUCCESSFUL = "Successfully processed";
        String PENDING = "Pending approval";
        String FAILED = "Failed request";
        String UPDATE = "Successfully updated";
        String GET = "Successfully fetched records";
    }


}