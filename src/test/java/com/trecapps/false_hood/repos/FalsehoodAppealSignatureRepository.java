package com.trecapps.false_hood.repos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.trecapps.false_hood.appeals.FalsehoodAppeal;
import com.trecapps.false_hood.appeals.FalsehoodAppealSignature;
import com.trecapps.false_hood.appeals.FalsehoodAppealSignatureRepo;

public class FalsehoodAppealSignatureRepository implements FalsehoodAppealSignatureRepo {

	Set<FalsehoodAppealSignature> appeals = new TreeSet<>();
	
	@Override
	public List<FalsehoodAppealSignature> findAll() {
		return new LinkedList<>(appeals);
	}

	@Override
	public List<FalsehoodAppealSignature> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FalsehoodAppealSignature> findAllById(Iterable<BigInteger> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends FalsehoodAppealSignature> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public <S extends FalsehoodAppealSignature> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInBatch(Iterable<FalsehoodAppealSignature> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public FalsehoodAppealSignature getOne(BigInteger id) {
		for(FalsehoodAppealSignature sign: appeals)
		{
			if(id.equals(sign.getId()))
			{
				return sign;
			}
		}
		return new FalsehoodAppealSignature();
	}

	@Override
	public <S extends FalsehoodAppealSignature> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends FalsehoodAppealSignature> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<FalsehoodAppealSignature> findAll(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = number * size;

        int end = start + number;

        List<FalsehoodAppealSignature> preRet = new ArrayList<>(appeals);

        return new TrecPage<FalsehoodAppealSignature>( preRet.subList(start, end));
	}

	@Override
	public <S extends FalsehoodAppealSignature> S save(S entity) {
    	if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");

        boolean add = entity.getId() == null;
        if(add)
        {
            BigInteger i = new BigInteger("0");

            for(FalsehoodAppealSignature app: appeals)
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
	public Optional<FalsehoodAppealSignature> findById(BigInteger id) {
		for(FalsehoodAppealSignature sign: appeals)
		{
			if(id.equals(sign.getId()))
			{
				return Optional.of(sign);
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean existsById(BigInteger id) {
		for(FalsehoodAppealSignature sign: appeals)
		{
			if(id.equals(sign.getId()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public long count() {
		return appeals.size();
	}

	@Override
	public void deleteById(BigInteger id) {
		for(FalsehoodAppealSignature sign: appeals)
		{
			if(id.equals(sign.getId()))
			{
				appeals.remove(sign);
				return;
			}
		}

	}

	@Override
	public void delete(FalsehoodAppealSignature entity) {
		appeals.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends FalsehoodAppealSignature> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		appeals.clear();

	}

	@Override
	public <S extends FalsehoodAppealSignature> Optional<S> findOne(Example<S> example) {
		
		return null;
	}

	@Override
	public <S extends FalsehoodAppealSignature> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends FalsehoodAppealSignature> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends FalsehoodAppealSignature> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FalsehoodAppealSignature> getSignaturesByFalsehood(FalsehoodAppeal appeal) {
		
		List<FalsehoodAppealSignature> ret = new LinkedList<>();
		for(FalsehoodAppealSignature sign : appeals)
		{
			if(appeal.equals(sign.getAppeal()))
				ret.add(sign);
		}
		return ret;
	}

}
