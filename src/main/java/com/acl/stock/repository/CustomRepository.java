package com.acl.stock.repository;


import com.acl.stock.util.ChecksumImplementation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;


@SuppressWarnings({"unused", "DuplicatedCode", "unchecked", "JavadocDeclaration"})
@Slf4j
@RequiredArgsConstructor
public class CustomRepository {

    protected final EntityManager em;




    protected <T> void validate(Class<T> type, T obj) {
        if (ChecksumImplementation.class.isAssignableFrom(type)) {
            try {
                ChecksumImplementation.class.getMethod("validate").invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


}