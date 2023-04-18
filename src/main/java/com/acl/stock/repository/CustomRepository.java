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


    public boolean isValidId(String tableName, long id) {

        String sqlQuery = "select e from " + tableName + " e where e.id = " + id;
        return !em.createQuery(sqlQuery).getResultList().isEmpty();
    }

    public boolean isUnique(String tableName, String columnName, Object value) {
        String sqlQuery = "select e from  " + tableName + " e where LOWER(e." + columnName + ") = LOWER(:value)";
        return em.createQuery(sqlQuery)
                .setParameter("value", value)
                .getResultList()
                .isEmpty();
    }

    public boolean isUnique(String tableName, String columnName, Object value, String businessCode, boolean businessContext) {

        if (!businessContext) {
            return this.isUnique(tableName, columnName, value);
        }

        String sqlQuery = "select e from  " + tableName + " e where LOWER(e." + columnName + ") = LOWER(:value) AND e.businessCode  = " + businessCode;
        return em.createQuery(sqlQuery)
                .setParameter("value", value)
                .getResultList()
                .isEmpty();
    }

    public boolean isExist(String tableName, String columnName, Object value) {
        String sqlQuery = MessageFormat.format("select e from  {0} e where LOWER(e.{1}) = LOWER(:value)", tableName, columnName);
        return !em.createQuery(sqlQuery)
                .setParameter("value", value)
                .getResultList()
                .isEmpty();
    }

    protected <T> void validate(Class<T> type, T obj) {
        if (ChecksumImplementation.class.isAssignableFrom(type)) {
            try {
                ChecksumImplementation.class.getMethod("validate").invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


    public <T> Optional<T> findOneOptional(Class<T> type, Long id) {
        T obj = em.find(type, id);

        if (!ObjectUtils.isEmpty(obj)) {
            validate(type, obj);
            em.detach(obj);
            return Optional.of(obj);
        }
        return Optional.empty();
    }

}