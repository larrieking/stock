package com.acl.stock.repository;


import com.acl.stock.domain.request.PaginationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.javers.spring.annotation.JaversAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@SuppressWarnings({"unused", "DuplicatedCode", "unchecked"})
@Transactional
@Repository
@Slf4j
public class BaseRepository extends CustomRepository {

    public BaseRepository(EntityManager em) {
        super(em);
    }

    @JaversAuditable
    public <T> T save(T entity) {

        em.persist(entity);
        em.flush();

        return entity;
    }


    @JaversAuditable
    public <T> void update(T entity) {

        em.merge(entity);
        em.flush();

    }







    public <T, V, K> Optional<T> findOneByOptional(Class<T> type, V columnName, K param) {
        String sqlQuery = "select e from  " + type.getSimpleName() + " e where LOWER(e." + columnName
                + ") = LOWER(:param)";

        TypedQuery<T> typeQuery = em.createQuery(sqlQuery, type).setParameter("param", param);
        Optional<T> result = typeQuery.getResultList().stream().findFirst();
        result.ifPresent(t -> validate(type, t));

        return result;
    }

    public <T> Page<T> findAllBy(Class<T> type, Map<String, Object> filterTemp, PaginationRequest page) {

        Map<String, Object> filter = new HashMap<>(filterTemp);

        AtomicReference<String> sqlQuery = new AtomicReference<>();
        sqlQuery.set("select e from  " + type.getSimpleName() + " e " + (filter.isEmpty() ? "" : "where "));

        filter.entrySet().stream().filter(o -> o.getValue() == null).forEach(i -> sqlQuery.set(sqlQuery.get() + " " + i.getKey() + " IS NULL AND"));

        filter.entrySet().stream().filter(o -> (o.getValue() != null && !(o.getValue() instanceof List<?>)
                        && !(o.getValue() instanceof Map) && !(o.getValue() instanceof AtomicReference<?>) && !(o.getValue() instanceof String[])
                        && !(o.getValue() instanceof Pair)))
                .forEach(i -> sqlQuery.set(sqlQuery.get() + " " + i.getKey() + " = :" + i.getKey() + " AND"));

        filter.entrySet().stream().filter(k -> k.getValue() instanceof String[]).forEach(m ->
                sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " in :" + m.getKey() + " AND"));

        filter.entrySet().stream().filter(k -> k.getValue() instanceof Pair).forEach(m -> sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " BETWEEN :" + m.getKey() + "Start AND :" + m.getKey() + "End AND"));

        filter.entrySet().stream().filter(k -> k.getValue() instanceof AtomicReference).forEach(m ->
        {
            if (Objects.equals(m.getValue(), "null")) {
                sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " is not :" + m.getKey() + " AND");

            } else {
                sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " != :" + m.getKey() + " AND");
            }
        });

        filter.keySet().stream().filter(o -> (filter.get(o) instanceof Map))
                .forEach(i -> ((Map<String, String>) filter.get(i)).keySet().forEach(k -> {
                    String value = ((Map<String, String>) filter.get(i)).get(k);
                    sqlQuery.set(sqlQuery.get() + " " + k + " LIKE :" + k + " OR");
                }));
        if (filter.keySet().stream().anyMatch(o -> (filter.get(o) instanceof Map))) {
            sqlQuery.set(sqlQuery.get().substring(0, sqlQuery.get().length() - 2));
            sqlQuery.set(sqlQuery.get() + "AND");
        }


        filter.keySet().stream().filter(s -> (filter.get(s) != null && filter.get(s) instanceof List<?>)).forEach(s -> buildQueryString(filter, sqlQuery, s));

        sqlQuery.set(filter.isEmpty() ? sqlQuery.get() : sqlQuery.get().substring(0, sqlQuery.get().length() - 4));

        TypedQuery<Long> countQuery = em.createQuery(sqlQuery.get().replace("select e from", "select count(e) from"),
                Long.class);
        TypedQuery<T> typeQuery = em.createQuery(sqlQuery.get() + " ORDER BY createdDate DESC", type);

        filter.entrySet().stream().filter(i -> i.getValue() != null && !(i.getValue() instanceof List<?>) && !(i.getValue() instanceof Map)
                && !(i.getValue() instanceof AtomicReference<?>) && !(i.getValue() instanceof String[]) && !(i.getValue() instanceof Pair)).forEach(i -> {
            typeQuery.setParameter(i.getKey(), i.getValue());
            countQuery.setParameter(i.getKey(), i.getValue());
        });

        filter.entrySet().stream().filter(k -> k.getValue() instanceof String[]).forEach(m -> {
            List<String> value = Arrays.asList((String[]) m.getValue());
            typeQuery.setParameter(m.getKey(), value);
            countQuery.setParameter(m.getKey(), value);
        });

        filter.entrySet().stream().filter(k -> k.getValue() instanceof Pair).forEach(m -> {

            Pair<Date, Date> value = (Pair<Date, Date>) m.getValue();
            typeQuery.setParameter(m.getKey() + "Start", value.getKey());
            typeQuery.setParameter(m.getKey() + "End", value.getValue());
            countQuery.setParameter(m.getKey() + "Start", value.getKey());
            countQuery.setParameter(m.getKey() + "End", value.getValue());
        });

        filter.entrySet().stream().filter(i -> i.getValue() != null && (i.getValue() instanceof AtomicReference<?>)).forEach(h -> {
            typeQuery.setParameter(h.getKey(), ((AtomicReference<?>) h.getValue()).get());
            countQuery.setParameter(h.getKey(), ((AtomicReference<?>) h.getValue()).get());
        });

        filter.keySet().stream().filter(o -> filter.get(o) != null && (filter.get(o) instanceof List<?>)).forEach(i -> {
            List<?> values = (List<?>) filter.get(i);

            IntStream.range(0, values.size()).forEach(index -> {
                typeQuery.setParameter(i + "" + index, values.get(index));
                countQuery.setParameter(i + "" + index, values.get(index));
            });
        });

        filter.keySet().stream().filter(o -> filter.get(o) != null && (filter.get(o) instanceof Map))
                .forEach(i -> ((Map<String, String>) filter.get(i)).keySet().forEach(k -> {
                    String value = ((Map<String, String>) filter.get(i)).get(k);

                    typeQuery.setParameter(k, value);
                    countQuery.setParameter(k, value);

                }));

        Long contentSize = countQuery.getSingleResult();
        page.setPage(page.getPage() <= 1 ? 1 : page.getPage());
        page.setSize(page.getSize() == 0 ? (contentSize.intValue() == 0 ? 1 : contentSize.intValue()) : page.getSize());

        typeQuery.setFirstResult((page.getPage() - 1) * page.getSize()).setMaxResults(page.getSize());

        return new PageImpl<>(typeQuery.getResultList(), PageRequest.of(page.getPage() - 1, page.getSize()),
                contentSize);
    }

    private void buildQueryString(Map<String, Object> filter, AtomicReference<String> sqlQuery, String s) {
        sqlQuery.set(sqlQuery.get() + "(");
        IntStream.range(0, ((List<?>) filter.get(s)).size()).forEach(index -> sqlQuery.set(sqlQuery.get() + " " + s + " = :" + s + "" + index + " OR"));
        sqlQuery.set(sqlQuery.get().substring(0, sqlQuery.get().length() - 3) + ") AND");
    }


    //The filter map object takes care of both OR, AND & LIKE;
    //If the value is of type List means it represents OR;
    //If the value is of type Map means it represents LIKE;
    //If the value is of type String[] means it represents in;
    //If the value is of type Pair<Date,Date> means it represents in;

    //By default, all value are chained with AND
    //If value is of type AtomicReference means it represents !=

    private void buildQueryStringWithLike(Map<String, Object> filter, AtomicReference<String> sqlQuery) {
        filter.keySet().stream().filter(o -> (filter.get(o) instanceof Map))
                .forEach(i -> ((Map<String, String>) filter.get(i)).keySet().forEach(k -> {
                    String value = ((Map<String, String>) filter.get(i)).get(k);
                    sqlQuery.set(sqlQuery.get() + " " + k + " LIKE :" + k + " AND");
                }));


        for (String s : filter.keySet()) {
            if ((filter.get(s) != null && filter.get(s) instanceof List<?>)) {
                buildQueryString(filter, sqlQuery, s);
            }
        }

        sqlQuery.set(filter.isEmpty() ? sqlQuery.get() : sqlQuery.get().substring(0, sqlQuery.get().length() - 4));
    }




}