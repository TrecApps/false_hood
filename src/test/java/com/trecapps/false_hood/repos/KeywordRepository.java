package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.keywords.Keyword;
import com.trecapps.false_hood.keywords.KeywordRepo;
import com.trecapps.false_hood.users.FalsehoodUser;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.*;

public class KeywordRepository implements KeywordRepo
{
    Set<Keyword> appeals = new TreeSet<>();
    
    @Override
    public List<Keyword> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<Keyword> findAll(Sort sort) {
        return new LinkedList<>(appeals);
    }

    @Override
    public Page<Keyword> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<Keyword> preRet = new ArrayList<>(appeals);

        return new TrecPage<Keyword>( preRet.subList(start, end));
    }

    @Override
    public List<Keyword> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(String s) {
        List<Keyword> preRet = new ArrayList<>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if(preRet.get(Rust).getWord().equals(s))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(Keyword entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Keyword> entities) {
        for(Keyword u : entities)
        {
        	delete(u);
        }
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends Keyword> S save(S entity) {
    	if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");
    	if(entity.getWord() == null)
            throw new IllegalArgumentException("Entity passed to Save Method must have a valid word!");
    	
        for(Keyword k: appeals)
        {
            if(entity.getWord().equals(k.getWord()))
            {
                k.setFalsehoods(entity.getFalsehoods());
                return entity;
            }
        }
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends Keyword> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<Keyword> findById(String s) {
        for(Keyword app: appeals)
        {
            if(s.equals(app.getWord()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        for(Keyword app: appeals)
        {
            if(s.equals(app.getWord()))
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Keyword> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Keyword> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Keyword getOne(String s) {
        for(Keyword app: appeals)
        {
            if(s.equals(app.getWord()))
                return app;
        }

        return new Keyword();
    }

    @Override
    public <S extends Keyword> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Keyword> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Keyword> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Keyword> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Keyword> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Keyword> boolean exists(Example<S> example) {
        return false;
    }
}
