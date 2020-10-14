package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.publicFalsehoods.Region;
import com.trecapps.false_hood.publicFalsehoods.RegionRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class RegionRepository implements RegionRepo 
{    Set<Region> appeals = new TreeSet<>();

    @Override
    public List<Region> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<Region> findAll(Sort sort) {
        return new LinkedList<>(appeals);
    }

    @Override
    public Page<Region> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<Region> preRet = new ArrayList<>(appeals);

        return new TrecPage<Region>( preRet.subList(start, end));
    }

    @Override
    public List<Region> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(Long aLong) {
        List<Region> preRet = new ArrayList<>(appeals);
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
    public void delete(Region entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Region> entities) {
        appeals.clear();
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends Region> S save(S entity) {
        if(entity == null)
            return null;

        boolean add = entity.getId() == null;
        if(add)
        {
            List<Region> l = new ArrayList<Region>(appeals);

            Long i = Long.valueOf("0");

            for(Region app: appeals)
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
    public <S extends Region> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<Region> findById(Long aLong) {
        for(Region app: appeals)
        {
            if(aLong.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for(Region app: appeals)
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
    public <S extends Region> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Region> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Region getOne(Long aLong) {
        for(Region app: appeals)
        {
            if(aLong.equals(app.getId()))
                return app;
        }

        return new Region();
    }

    @Override
    public <S extends Region> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Region> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Region> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Region> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Region> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Region> boolean exists(Example<S> example) {
        return false;
    }
}