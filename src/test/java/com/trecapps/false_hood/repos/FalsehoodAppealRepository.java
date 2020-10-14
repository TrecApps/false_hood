package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.appeals.FalsehoodAppeal;
import com.trecapps.false_hood.appeals.FalsehoodAppealRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.*;

public class FalsehoodAppealRepository implements FalsehoodAppealRepo
{
    Set<FalsehoodAppeal> appeals = new TreeSet<>();

    @Override
    public List<FalsehoodAppeal> getAppeals() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<FalsehoodAppeal> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<FalsehoodAppeal> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<FalsehoodAppeal> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<FalsehoodAppeal> preRet = new ArrayList<>(appeals);

        return new TrecPage<FalsehoodAppeal>( preRet.subList(start, end));
    }

    @Override
    public List<FalsehoodAppeal> findAllById(Iterable<BigInteger> bigIntegers) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(BigInteger bigInteger) {
        List<FalsehoodAppeal> preRet = new ArrayList<>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if(preRet.get(Rust).getId().equals(bigInteger))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(FalsehoodAppeal entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends FalsehoodAppeal> entities) {
        appeals.clear();
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends FalsehoodAppeal> S save(S entity) {

        if(entity == null)
            return null;

        boolean add = entity.getId() == null;
        if(add)
        {
            List<FalsehoodAppeal> l = new ArrayList<FalsehoodAppeal>(appeals);

            BigInteger i = new BigInteger("0");

            for(FalsehoodAppeal app: appeals)
            {
                if(!i.equals(app.getId()))
                    entity.setId(i);
                i = i.add(BigInteger.ONE);
            }
        }

        appeals.removeIf((app) -> entity.getId().equals(app.getId()));
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends FalsehoodAppeal> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<FalsehoodAppeal> findById(BigInteger bigInteger) {

        for(FalsehoodAppeal app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(BigInteger bigInteger) {
        for(FalsehoodAppeal app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends FalsehoodAppeal> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<FalsehoodAppeal> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public FalsehoodAppeal getOne(BigInteger bigInteger) {
        for(FalsehoodAppeal app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return app;
        }

        return new FalsehoodAppeal();
    }

    @Override
    public <S extends FalsehoodAppeal> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends FalsehoodAppeal> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends FalsehoodAppeal> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends FalsehoodAppeal> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends FalsehoodAppeal> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends FalsehoodAppeal> boolean exists(Example<S> example) {
        return false;
    }
}
