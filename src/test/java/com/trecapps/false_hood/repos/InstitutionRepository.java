package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.publicFalsehoods.Institution;
import com.trecapps.false_hood.publicFalsehoods.InstitutionRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class InstitutionRepository implements InstitutionRepo
{
    Set<Institution> appeals = new TreeSet<>();

    @Override
    public List<Institution> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<Institution> findAll(Sort sort) {
        return new LinkedList<>(appeals);
    }

    @Override
    public Page<Institution> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<Institution> preRet = new ArrayList<>(appeals);

        return new TrecPage<Institution>( preRet.subList(start, end));
    }

    @Override
    public List<Institution> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(Long aLong) {
        List<Institution> preRet = new ArrayList<>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if(preRet.get(Rust).getId().equals(aLong))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(Institution entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Institution> entities) {
        appeals.clear();
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends Institution> S save(S entity) {
        if(entity == null)
            return null;

        boolean add = entity.getId() == null;
        if(add)
        {
            List<Institution> l = new ArrayList<Institution>(appeals);

            Long i = Long.valueOf("0");

            for(Institution app: appeals)
            {
                if(!i.equals(app.getId()))
                    entity.setId(i);
                i = i + 1;
            }
        }

        appeals.removeIf((app) -> entity.getId().equals(app.getId()));
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends Institution> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<Institution> findById(Long aLong) {
        for(Institution app: appeals)
        {
            if(aLong.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for(Institution app: appeals)
        {
            if(aLong.equals(app.getId()))
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Institution> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Institution> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Institution getOne(Long aLong) {
        for(Institution app: appeals)
        {
            if(aLong.equals(app.getId()))
                return app;
        }

        return new Institution();
    }

    @Override
    public <S extends Institution> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Institution> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Institution> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Institution> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Institution> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Institution> boolean exists(Example<S> example) {
        return false;
    }
}
