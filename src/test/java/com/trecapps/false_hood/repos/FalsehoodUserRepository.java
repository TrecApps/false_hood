package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class FalsehoodUserRepository implements FalsehoodUserRepo {
    Set<FalsehoodUser> appeals = new TreeSet<>();

    @Override
    public List<FalsehoodUser> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<FalsehoodUser> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<FalsehoodUser> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<FalsehoodUser> preRet = new ArrayList<>(appeals);

        return new TrecPage<FalsehoodUser>( preRet.subList(start, end));
    }

    @Override
    public List<FalsehoodUser> findAllById(Iterable<Long> Longs) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(Long Long) {
        List<FalsehoodUser> preRet = new ArrayList<>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if(preRet.get(Rust).getUserId().equals(Long))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(FalsehoodUser entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends FalsehoodUser> entities) {
        for(FalsehoodUser u : entities)
        {
        	delete(u);
        }
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends FalsehoodUser> S save(S entity) {
    	if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");

        boolean add = entity.getUserId() == null;
        if(add)
        {
            Long i = Long.valueOf("0");

            for(FalsehoodUser app: appeals)
            {
                if(!i.equals(app.getUserId()))
                    entity.setUserId(i);
                i = i + 1;
            }
            
            if(entity.getUserId() == null)
            	entity.setUserId(i);
        }

        appeals.removeIf((app) -> entity.getUserId().equals(app.getUserId()));
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends FalsehoodUser> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<FalsehoodUser> findById(Long Long) {

        for(FalsehoodUser app: appeals)
        {
            if(Long.equals(app.getUserId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long Long) {
        for(FalsehoodUser app: appeals)
        {
            if(Long.equals(app.getUserId()))
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends FalsehoodUser> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<FalsehoodUser> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public FalsehoodUser getOne(Long Long) {
        for(FalsehoodUser app: appeals)
        {
            if(Long.equals(app.getUserId()))
                return app;
        }

        return new FalsehoodUser();
    }

    @Override
    public <S extends FalsehoodUser> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends FalsehoodUser> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends FalsehoodUser> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends FalsehoodUser> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends FalsehoodUser> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends FalsehoodUser> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Long getUsersAboveCredit(Integer credit) {
        long ret = 0;

        for(FalsehoodUser u: appeals)
        {
            if(u.getCredit() >= credit)
                ret++;
        }
        return ret;
    }
}
