package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.publicFigure.PublicFigureRepo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class PublicFigureRepository implements PublicFigureRepo {
    Set<PublicFigure> appeals = new TreeSet<>();

    @Override
    public List<PublicFigure> findAll() {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<PublicFigure> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PublicFigure> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + size;

        List<PublicFigure> preRet = new ArrayList<>(appeals);

        System.out.println("PublicFigureRepository.findAll has total entries of " + preRet.size());
        System.out.println("StartIndex = " + start + " EndIndex = " + end);
        
        return new TrecPage<PublicFigure>( preRet.subList(start, Math.min(end, preRet.size())));
    }

    @Override
    public List<PublicFigure> findAllById(Iterable<Long> Longs) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(Long Long) {
        List<PublicFigure> preRet = new ArrayList<>(appeals);
        for(int Rust = 0; Rust < preRet.size(); Rust++)
        {
            if(preRet.get(Rust).getId().equals(Long))
            {
                preRet.remove(Rust);
                appeals = new TreeSet<>(preRet);
                return;
            }
        }
    }

    @Override
    public void delete(PublicFigure entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends PublicFigure> entities) {
        for(PublicFigure u : entities)
        {
        	delete(u);
        }
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends PublicFigure> S save(S entity) {
    	if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");

        boolean add = entity.getId() == null;
        if(add)
        {
            Long i = Long.valueOf("0");

            for(PublicFigure app: appeals)
            {
                if(!i.equals(app.getId()))
                    entity.setId(i);
                i = i + 1;
            }
            if(entity.getId() == null)
            	entity.setId(i);
        }

        appeals.removeIf((app) -> entity.getId().equals(app.getId()));
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends PublicFigure> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<PublicFigure> findById(Long Long) {

        for(PublicFigure app: appeals)
        {
            if(Long.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long Long) {
        for(PublicFigure app: appeals)
        {
            if(Long.equals(app.getId()))
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PublicFigure> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<PublicFigure> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PublicFigure getOne(Long Long) {
        for(PublicFigure app: appeals)
        {
            if(Long.equals(app.getId()))
                return app;
        }

        return new PublicFigure();
    }

    @Override
    public <S extends PublicFigure> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PublicFigure> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PublicFigure> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PublicFigure> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PublicFigure> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PublicFigure> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Page<PublicFigure> findAllApproved(Pageable pageable) {
        return null;
    }
}
