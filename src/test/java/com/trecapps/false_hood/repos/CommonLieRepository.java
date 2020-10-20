package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.appeals.FalsehoodAppeal;
import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.commonLie.CommonLieRepo;
import com.trecapps.false_hood.users.FalsehoodUser;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.*;

public class CommonLieRepository implements CommonLieRepo
{
    Set<CommonLie> appeals = new TreeSet<>();
    @Override
    public List<CommonLie> getCommonLieByTitleStart(String title) {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<CommonLie> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<CommonLie> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CommonLie> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<CommonLie> preRet = new ArrayList<>(appeals);

        return new TrecPage<CommonLie>( preRet.subList(start, end));
    }

    @Override
    public List<CommonLie> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(Long aLong) {

        List<CommonLie> preRet = new ArrayList<CommonLie>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if( aLong.equals(preRet.get(Rust).getId()))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(CommonLie entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends CommonLie> entities) {
        for(CommonLie u : entities)
        {
        	delete(u);
        }
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends CommonLie> S save(S entity) {
    	if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");

        boolean add = entity.getId() == null;
        if(add)
        {
            List<CommonLie> l = new ArrayList<CommonLie>(appeals);

            Long i = Long.valueOf("0");

            for(CommonLie app: appeals)
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
    public <S extends CommonLie> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<CommonLie> findById(Long aLong) {
        for(CommonLie app: appeals)
        {
            if(aLong.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for(CommonLie app: appeals)
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
    public <S extends CommonLie> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<CommonLie> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CommonLie getOne(Long aLong) {
        for(CommonLie app: appeals)
        {
            if(aLong.equals(app.getId()))
                return app;
        }

        return new CommonLie();
    }

    @Override
    public <S extends CommonLie> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CommonLie> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CommonLie> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CommonLie> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CommonLie> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CommonLie> boolean exists(Example<S> example) {
        return false;
    }
}
