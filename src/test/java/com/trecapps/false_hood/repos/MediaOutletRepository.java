package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.falsehoods.MediaOutletRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.*;

public class MediaOutletRepository implements MediaOutletRepo
{
    Set<MediaOutlet> appeals = new TreeSet<>();

    @Override
    public MediaOutlet getOutletByName(String name) {
        for(MediaOutlet mo: appeals)
        {
            if(name.equals(mo.getName()))
                return mo;
        }
        return null;
    }

    @Override
    public List<MediaOutlet> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<MediaOutlet> findAll(Sort sort) {
        return new LinkedList<>(appeals);
    }

    @Override
    public Page<MediaOutlet> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<MediaOutlet> preRet = new ArrayList<>(appeals);

        return new TrecPage<MediaOutlet>( preRet.subList(start, end));
    }

    @Override
    public List<MediaOutlet> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(Integer integer) {
        List<MediaOutlet> preRet = new ArrayList<>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if(preRet.get(Rust).getOutletId().equals(integer))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(MediaOutlet entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends MediaOutlet> entities) {
        appeals.clear();
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends MediaOutlet> S save(S entity) {
        if(entity == null)
            return null;

        boolean add = entity.getOutletId() == null;
        if(add)
        {
            List<MediaOutlet> l = new ArrayList<MediaOutlet>(appeals);

            Integer i = Integer.valueOf("0");

            for(MediaOutlet app: appeals)
            {
                if(!i.equals(app.getOutletId()))
                    entity.setOutletId(i);
                i = i + 1;
            }
        }

        appeals.removeIf((app) -> entity.getOutletId().equals(app.getOutletId()));
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends MediaOutlet> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<MediaOutlet> findById(Integer integer) {
        for(MediaOutlet app: appeals)
        {
            if(integer.equals(app.getOutletId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        for(MediaOutlet app: appeals)
        {
            if(integer.equals(app.getOutletId()))
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends MediaOutlet> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<MediaOutlet> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public MediaOutlet getOne(Integer integer) {
        for(MediaOutlet app: appeals)
        {
            if(integer.equals(app.getOutletId()))
                return app;
        }

        return new MediaOutlet();
    }

    @Override
    public <S extends MediaOutlet> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends MediaOutlet> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends MediaOutlet> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends MediaOutlet> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends MediaOutlet> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends MediaOutlet> boolean exists(Example<S> example) {
        return false;
    }
}
