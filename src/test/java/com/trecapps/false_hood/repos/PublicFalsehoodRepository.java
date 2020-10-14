package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.publicFalsehoods.Institution;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;
import com.trecapps.false_hood.publicFalsehoods.Region;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class PublicFalsehoodRepository implements PublicFalsehoodRepo
{    Set<PublicFalsehood> appeals = new TreeSet<>();

    @Override
    public List<PublicFalsehood> getFalsehoodsBetween(Date begin, Date end) {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsBefore(Date end) {
        return new LinkedList<>(appeals);
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByMinimumSeverity(byte minSeverity) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)-> f.getSeverity() >= minSeverity).collect(Collectors.toList()));

    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByMaximumSeverity(byte maxSeverity) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)-> f.getSeverity() <= maxSeverity).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodBySeverity(byte maxSeverity, byte minSeverity) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getSeverity() >= minSeverity && f.getSeverity() <= maxSeverity;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getChallengedFalsehoods() {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getStatus() == Falsehood.CHALLENGED;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getConfirmedFalsehoods() {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getStatus() == Falsehood.VERIFIED;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByPublicFigure(PublicFigure author) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getOfficial().getId().equals(author.getId());
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByCommonLie(CommonLie lie) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getCommonLie() != null && lie.getId().equals(f.getCommonLie().getId());
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByRegion(Region region) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getRegion() != null && region.getId().equals(f.getRegion().getId());
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByInstitution(Institution institution) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getInstitution() != null && institution.getId().equals(f.getInstitution().getId());
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> findAll() {
        return new ArrayList<PublicFalsehood>(appeals);
    }

    @Override
    public List<PublicFalsehood> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PublicFalsehood> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<PublicFalsehood> preRet = new ArrayList<>(appeals);

        return new TrecPage<PublicFalsehood>( preRet.subList(start, end));
    }

    @Override
    public List<PublicFalsehood> findAllById(Iterable<BigInteger> bigIntegers) {
        return null;
    }

    @Override
    public long count() {
        return appeals.size();
    }

    @Override
    public void deleteById(BigInteger bigInteger) {
        List<PublicFalsehood> preRet = new ArrayList<>(appeals);
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
    public void delete(PublicFalsehood entity) {
        appeals.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends PublicFalsehood> entities) {
        appeals.clear();
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends PublicFalsehood> S save(S entity) {
        if(entity == null)
            return null;

        boolean add = entity.getId() == null;
        if(add)
        {
            List<PublicFalsehood> l = new ArrayList<PublicFalsehood>(appeals);

            BigInteger i = new BigInteger("0");

            for(PublicFalsehood app: appeals)
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
    public <S extends PublicFalsehood> List<S> saveAll(Iterable<S> entities) {
        List<S> ret = new LinkedList<>();
        for(S s: entities)
        {
            ret.add(save(s));
        }
        return ret;
    }

    @Override
    public Optional<PublicFalsehood> findById(BigInteger bigInteger) {
        for(PublicFalsehood app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return Optional.of(app);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(BigInteger bigInteger) {
        for(PublicFalsehood app: appeals)
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
    public <S extends PublicFalsehood> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<PublicFalsehood> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PublicFalsehood getOne(BigInteger bigInteger) {
        for(PublicFalsehood app: appeals)
        {
            if(bigInteger.equals(app.getId()))
                return app;
        }

        return new PublicFalsehood();
    }

    @Override
    public <S extends PublicFalsehood> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PublicFalsehood> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PublicFalsehood> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PublicFalsehood> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PublicFalsehood> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PublicFalsehood> boolean exists(Example<S> example) {
        return false;
    }
}
