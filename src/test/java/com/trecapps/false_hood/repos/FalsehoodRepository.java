package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodRepo;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class FalsehoodRepository implements FalsehoodRepo
{
    Set<Falsehood> appeals = new TreeSet<>();
    
    @Override
    public List<Falsehood> getFalsehoodsBetween(Date begin, Date end) {
        return new LinkedList<>(appeals.stream().filter((f)-> (begin == null) ? true: (f.getDateMade().getTime() >= begin.getTime()))
        		.filter((f)-> (end == null) ? true : (f.getDateMade().getTime() <= end.getTime())).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getFalsehoodsBefore(Date end) {
        return new LinkedList<>(appeals.stream().filter((f)-> (end == null) ? true : (f.getDateMade().getTime() <= end.getTime())).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getFalsehoodsByMinimumSeverity(byte minSeverity) {
        return new ArrayList<Falsehood>(appeals.stream().filter((f)-> f.getSeverity() <= minSeverity).collect(Collectors.toList()));

    }

    @Override
    public List<Falsehood> getFalsehoodsByMaximumSeverity(byte maxSeverity) {
        return new ArrayList<Falsehood>(appeals.stream().filter((f)-> f.getSeverity() >= maxSeverity).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getFalshoodBySeverity(byte maxSeverity, byte minSeverity) {
        return new ArrayList<Falsehood>(appeals.stream().filter((f)->{
            return f.getSeverity() <= minSeverity && f.getSeverity() >= maxSeverity;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getFalsehoodsByOutlet(MediaOutlet outletId) {
        return new ArrayList<Falsehood>(appeals.stream().filter((f)->{
            return f.getOutlet().getOutletId() == outletId.getOutletId();
        }).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getFalsehoodsByMediaType(int mType) {
        return new ArrayList<Falsehood>(appeals.stream().filter((f)->{
            return f.getMediaType() == mType;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getChallengedFalsehoods() {
        return new ArrayList<Falsehood>(appeals.stream().filter((f)->{
            return f.getStatus() == FalsehoodStatus.CHALLENGED.GetValue();
        }).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getConfirmedFalsehoods() {
        return new ArrayList<Falsehood>(appeals.stream().filter((f)->{
            return f.getStatus() == FalsehoodStatus.VERIFIED.GetValue();
        }).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> getFalsehoodsByPublicFigure(PublicFigure author) {
    	
        return new ArrayList<Falsehood>(appeals.stream().filter((f)->{
            return ((f.getAuthor1() == null)? false : f.getAuthor1().getId().equals(author.getId())) ||
                    ((f.getAuthor2() == null) ? false: f.getAuthor2().getId().equals(author.getId()));
        }).collect(Collectors.toList()));
    }

    @Override
    public List<Falsehood> findAll() {
        return new ArrayList<Falsehood>(appeals);
    }

    @Override
    public List<Falsehood> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Falsehood> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<Falsehood> preRet = new ArrayList<>(appeals);

        return new TrecPage<Falsehood>( preRet.subList(start, end));
    }

    @Override
    public List<Falsehood> findAllById(Iterable<BigInteger> bigIntegers) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(BigInteger bigInteger) {
        List<Falsehood> preRet = new ArrayList<>(appeals);
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
    public void delete(Falsehood entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Falsehood> entities) {
        for(Falsehood f: entities)
        {
        	delete(f);
        }
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends Falsehood> S save(S entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");

        boolean add = entity.getId() == null;
        if(add)
        {
            List<Falsehood> l = new ArrayList<Falsehood>(appeals);

            BigInteger i = new BigInteger("0");

            for(Falsehood app: appeals)
            {
                if(!i.equals(app.getId()))
                    entity.setId(i);
                i = i.add(BigInteger.ONE);
            }
            
            if(entity.getId() == null)
            	entity.setId(i);
        }

        appeals.removeIf((app) -> entity.getId().equals(app.getId()));
        appeals.add(entity);

        return entity;
    }

    @Override
    public <S extends Falsehood> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<Falsehood> findById(BigInteger bigInteger) {
        for(Falsehood app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(BigInteger bigInteger) {
        for(Falsehood app: appeals)
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
    public <S extends Falsehood> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Falsehood> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Falsehood getOne(BigInteger bigInteger) {
        for(Falsehood app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return app;
        }

        return new Falsehood();
    }

    @Override
    public <S extends Falsehood> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Falsehood> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Falsehood> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Falsehood> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Falsehood> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Falsehood> boolean exists(Example<S> example) {
        return false;
    }
}
