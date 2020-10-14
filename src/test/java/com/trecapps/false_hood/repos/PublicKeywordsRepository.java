package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.keywords.PublicKeyword;
import com.trecapps.false_hood.keywords.PublicKeywordRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class PublicKeywordsRepository implements PublicKeywordRepo {
    Set<PublicKeyword> appeals = new TreeSet<>();

    @Override
    public List<PublicKeyword> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<PublicKeyword> findAll(Sort sort) {
        return new LinkedList<>(appeals);
    }

    @Override
    public Page<PublicKeyword> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<PublicKeyword> preRet = new ArrayList<>(appeals);

        return new TrecPage<PublicKeyword>( preRet.subList(start, end));
    }

    @Override
    public List<PublicKeyword> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(String s) {
        List<PublicKeyword> preRet = new ArrayList<>(appeals);
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
    public void delete(PublicKeyword entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends PublicKeyword> entities) {
        appeals.clear();
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends PublicKeyword> S save(S entity) {
        for(PublicKeyword k: appeals)
        {
            if(entity.equals(k.getWord()))
            {
                k.setFalsehoods(entity.getFalsehoods());
                break;
            }
        }

        return entity;
    }

    @Override
    public <S extends PublicKeyword> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<PublicKeyword> findById(String s) {
        for(PublicKeyword app: appeals)
        {
            if(s.equals(app.getWord()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        for(PublicKeyword app: appeals)
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
    public <S extends PublicKeyword> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<PublicKeyword> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PublicKeyword getOne(String s) {
        for(PublicKeyword app: appeals)
        {
            if(s.equals(app.getWord()))
                return app;
        }

        return new PublicKeyword();
    }

    @Override
    public <S extends PublicKeyword> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PublicKeyword> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PublicKeyword> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PublicKeyword> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PublicKeyword> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PublicKeyword> boolean exists(Example<S> example) {
        return false;
    }
}
