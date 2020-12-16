package com.trecapps.false_hood.repos;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.publicFalsehoods.Institution;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;
import com.trecapps.false_hood.publicFalsehoods.Region;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.users.FalsehoodUser;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class PublicFalsehoodRepository implements PublicFalsehoodRepo
{
	Set<PublicFalsehood> appeals = new TreeSet<>();
	
    private static List<PublicFalsehood> cullList(Pageable p, List<PublicFalsehood> f)
    {
        int number = p.getPageNumber();
        int size = p.getPageSize();

        int start = number * size;

        int end = start + size;
        return end < f.size() ? f.subList(start, end): f;
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsBetween(Date begin, Date end) {
    	return new LinkedList<>(appeals.stream().filter((f)-> (begin == null) ? true: (f.getDateMade().getTime() >= begin.getTime()))
        		.filter((f)-> (end == null) ? true : (f.getDateMade().getTime() <= end.getTime())).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsBefore(Date end) {
    	return new LinkedList<>(appeals.stream().filter((f)-> (end == null) ? true : (f.getDateMade().getTime() <= end.getTime())).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByMinimumSeverity(byte minSeverity) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)-> f.getSeverity() <= minSeverity).collect(Collectors.toList()));

    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByMaximumSeverity(byte maxSeverity) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)-> f.getSeverity() >= maxSeverity).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodBySeverity(byte maxSeverity, byte minSeverity) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getSeverity() <= minSeverity && f.getSeverity() >= maxSeverity;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getChallengedFalsehoods() {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getStatus() == FalsehoodStatus.CHALLENGED.GetValue();
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getConfirmedFalsehoods() {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getStatus() == FalsehoodStatus.VERIFIED.GetValue();
        }).collect(Collectors.toList()));
    }

    @Override
    public List<PublicFalsehood> getFalsehoodsByPublicFigure(PublicFigure author) {
        return new ArrayList<PublicFalsehood>(appeals.stream().filter((f)->{
            return f.getOfficial() == null ? false : f.getOfficial().getId().equals(author.getId());
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
        for(PublicFalsehood u : entities)
        {
        	delete(u);
        }
    }

    @Override
    public void deleteAll() {
        appeals.clear();
    }

    @Override
    public <S extends PublicFalsehood> S save(S entity) {
    	if(entity == null)
            throw new IllegalArgumentException("Entity passed to Save Method must not be null");

        boolean add = entity.getId() == null;
        if(add)
        {
            BigInteger i = new BigInteger("0");

            for(PublicFalsehood app: appeals)
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

	@Override
	public List<PublicFalsehood> getSubmittedFalsehoods(Pageable p) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status == 0)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoods(Pageable p) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetween(Pageable p, Date begin, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
			System.out.println("Ststus is " + status + " Date of Public Falsehood is " + d + " with begin " + begin + " and end " + end);
		}
		System.out.println("Before Public Cull, size is " + ret.size());
		ret = cullList(p, ret);

		System.out.println("After Public Cull, size is " + ret.size());
		return ret;
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByRegion(Pageable p, Date begin, Date end, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByInstitution(Pageable p, Date begin, Date end,
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByRegionAndInst(Pageable p, Date begin, Date end,
			Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficial(Pageable p, Date begin, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficialRegion(Pageable p, Date begin, Date end,
			PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficialInstitution(Pageable p, Date begin, Date end,
			PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficialRegionAndInst(Pageable p, Date begin,
			Date end, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverity(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityRegion(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityInstitution(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityRegionAndInst(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficial(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegion(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficialInstitution(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverity(Pageable p, Date begin, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityRegion(Pageable p, Date begin, Date end,
			byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityInstitution(Pageable p, Date begin,
			Date end, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityRegionAndInst(Pageable p, Date begin,
			Date end, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(Pageable p, Date begin, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegion(Pageable p, Date begin,
			Date end, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(Pageable p,
			Date begin, Date end, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(Pageable p,
			Date begin, Date end, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverity(Pageable p, Date begin, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityRegion(Pageable p, Date begin, Date end,
			byte maxSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityInstitution(Pageable p, Date begin,
			Date end, byte maxSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(Pageable p, Date begin,
			Date end, byte maxSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(Pageable p, Date begin, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(Pageable p, Date begin,
			Date end, byte maxSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(Pageable p,
			Date begin, Date end, byte maxSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(Pageable p,
			Date begin, Date end, byte maxSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}
	
	
	
	//// Now do Before
	
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBefore(Pageable p, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByRegion(Pageable p, Date end, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5	&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByInstitution(Pageable p, Date end,
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByRegionAndInst(Pageable p, Date end,
			Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByOfficial(Pageable p, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5	&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByOfficialRegion(Pageable p, Date end,
			PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByOfficialInstitution(Pageable p, Date end,
			PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByOfficialRegionAndInst(Pageable p,
			Date end, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverity(Pageable p, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityRegion(Pageable p, Date end,
			byte maxSeverity, byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityInstitution(Pageable p, Date end,
			byte maxSeverity, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityRegionAndInst(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityOfficial(Pageable p, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegion(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityOfficialInstitution(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverity(Pageable p, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityRegion(Pageable p, Date end,
			byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityInstitution(Pageable p,
			Date end, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityRegionAndInst(Pageable p,
			Date end, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(Pageable p, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegion(Pageable p,
			Date end, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity && d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(Pageable p,
			 Date end, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(Pageable p,
			 Date end, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverity(Pageable p, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityRegion(Pageable p, Date end,
			byte maxSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityInstitution(Pageable p,
			Date end, byte maxSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(Pageable p,
			Date end, byte maxSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(Pageable p, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(Pageable p,
			Date end, byte maxSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(Pageable p,
			 Date end, byte maxSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(Pageable p,
			 Date end, byte maxSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}
	
	
	//// Now do No Dates
	
	
	
	
	
	
	
	
	

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByRegion(Pageable p,   Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByInstitution(Pageable p,  
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByRegionAndInst(Pageable p,  
			Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByOfficial(Pageable p,  
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByOfficialRegion(Pageable p,  
			PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByOfficialInstitution(Pageable p,  
			PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByOfficialRegionAndInst(Pageable p, 
			 PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverity(Pageable p,  
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityRegion(Pageable p,  
			byte maxSeverity, byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityInstitution(Pageable p,  
			byte maxSeverity, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityRegionAndInst(Pageable p, 
			 byte maxSeverity, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityOfficial(Pageable p,  
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityOfficialRegion(Pageable p, 
			 byte maxSeverity, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityOfficialInstitution(Pageable p, 
			 byte maxSeverity, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsBySeverityOfficialRegionAndInst(Pageable p, 
			 byte maxSeverity, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverity(Pageable p,  
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityRegion(Pageable p,  
			byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityInstitution(Pageable p, 
			 byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityRegionAndInst(Pageable p, 
			 byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityOfficial(Pageable p,  
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityOfficialRegion(Pageable p, 
			 byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityOfficialInstitution(Pageable p,
			  byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMinSeverityOfficialRegionAndInst(Pageable p,
			  byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev <= minSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverity(Pageable p,  
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityRegion(Pageable p,  
			byte maxSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 1 && status < 5 && sev >= maxSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityInstitution(Pageable p, 
			 byte maxSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityRegionAndInst(Pageable p, 
			 byte maxSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityOfficial(Pageable p,  
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityOfficialRegion(Pageable p, 
			 byte maxSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityOfficialInstitution(Pageable p,
			  byte maxSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getConfirmedFalsehoodsByMaxSeverityOfficialRegionAndInst(Pageable p,
			  byte maxSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 1 && status < 5 && sev >= maxSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}
	
	
	/////////// Rejected Falsehoods ///////////
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getRejectedFalsehoods(Pageable p) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 4)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetween(Pageable p, Date begin, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
			System.out.println("With status " + status + " and date " + d + " before= " +end+ " after= " + begin);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByRegion(Pageable p, Date begin, Date end, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByInstitution(Pageable p, Date begin, Date end,
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByRegionAndInst(Pageable p, Date begin, Date end,
			Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByOfficial(Pageable p, Date begin, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByOfficialRegion(Pageable p, Date begin, Date end,
			PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByOfficialInstitution(Pageable p, Date begin, Date end,
			PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByOfficialRegionAndInst(Pageable p, Date begin,
			Date end, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverity(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityRegion(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityInstitution(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityRegionAndInst(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityOfficial(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityOfficialRegion(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityOfficialInstitution(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(Pageable p, Date begin,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverity(Pageable p, Date begin, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityRegion(Pageable p, Date begin, Date end,
			byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityInstitution(Pageable p, Date begin,
			Date end, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityRegionAndInst(Pageable p, Date begin,
			Date end, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(Pageable p, Date begin, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegion(Pageable p, Date begin,
			Date end, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(Pageable p,
			Date begin, Date end, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(Pageable p,
			Date begin, Date end, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverity(Pageable p, Date begin, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityRegion(Pageable p, Date begin, Date end,
			byte maxSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityInstitution(Pageable p, Date begin,
			Date end, byte maxSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(Pageable p, Date begin,
			Date end, byte maxSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(Pageable p, Date begin, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(Pageable p, Date begin,
			Date end, byte maxSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(Pageable p,
			Date begin, Date end, byte maxSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(Pageable p,
			Date begin, Date end, byte maxSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() >= begin.getTime()
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}
	
	
	
	//// Now do Before
	
	
	
	
	
	
	
	
	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBefore(Pageable p, Date end) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByRegion(Pageable p, Date end, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4	&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByInstitution(Pageable p, Date end,
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByRegionAndInst(Pageable p, Date end,
			Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByOfficial(Pageable p, Date end,
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4	&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByOfficialRegion(Pageable p, Date end,
			PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByOfficialInstitution(Pageable p, Date end,
			PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByOfficialRegionAndInst(Pageable p,
			Date end, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverity(Pageable p, Date end,
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityRegion(Pageable p, Date end,
			byte maxSeverity, byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityInstitution(Pageable p, Date end,
			byte maxSeverity, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityRegionAndInst(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityOfficial(Pageable p, Date end,
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityOfficialRegion(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityOfficialInstitution(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(Pageable p,
			Date end, byte maxSeverity, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverity(Pageable p, Date end,
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityRegion(Pageable p, Date end,
			byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityInstitution(Pageable p,
			Date end, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityRegionAndInst(Pageable p,
			Date end, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(Pageable p, Date end,
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegion(Pageable p,
			Date end, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity && d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(Pageable p,
			 Date end, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(Pageable p,
			 Date end, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev <= minSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverity(Pageable p, Date end,
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityRegion(Pageable p, Date end,
			byte maxSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityInstitution(Pageable p,
			Date end, byte maxSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(Pageable p,
			Date end, byte maxSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(Pageable p, Date end,
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(Pageable p,
			Date end, byte maxSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(Pageable p,
			 Date end, byte maxSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(Pageable p,
			 Date end, byte maxSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity
					&& d.getTime() <= end.getTime() && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}
	
	
	//// Now do No Dates
	
	
	
	
	
	
	
	
	

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByRegion(Pageable p,   Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByInstitution(Pageable p,  
			Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 4 && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByRegionAndInst(Pageable p,  
			Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 4 && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByOfficial(Pageable p,  
			PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 4 && official.equals(pf.getOfficial()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByOfficialRegion(Pageable p,  
			PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 4 && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByOfficialInstitution(Pageable p,  
			PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 4 && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByOfficialRegionAndInst(Pageable p, 
			 PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte status = pf.getStatus();
			
			if(status > 4 && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverity(Pageable p,  
			byte maxSeverity, byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityRegion(Pageable p,  
			byte maxSeverity, byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityInstitution(Pageable p,  
			byte maxSeverity, byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityRegionAndInst(Pageable p, 
			 byte maxSeverity, byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityOfficial(Pageable p,  
			byte maxSeverity, byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityOfficialRegion(Pageable p, 
			 byte maxSeverity, byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityOfficialInstitution(Pageable p, 
			 byte maxSeverity, byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsBySeverityOfficialRegionAndInst(Pageable p, 
			 byte maxSeverity, byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverity(Pageable p,  
			byte minSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityRegion(Pageable p,  
			byte minSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityInstitution(Pageable p, 
			 byte minSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityRegionAndInst(Pageable p, 
			 byte minSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityOfficial(Pageable p,  
			byte minSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityOfficialRegion(Pageable p, 
			 byte minSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityOfficialInstitution(Pageable p,
			  byte minSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMinSeverityOfficialRegionAndInst(Pageable p,
			  byte minSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev <= minSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverity(Pageable p,  
			byte maxSeverity) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityRegion(Pageable p,  
			byte maxSeverity, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			Date d = pf.getDateMade();
			if(status > 4 && sev >= maxSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityInstitution(Pageable p, 
			 byte maxSeverity, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityRegionAndInst(Pageable p, 
			 byte maxSeverity, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityOfficial(Pageable p,  
			byte maxSeverity, PublicFigure official) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity)
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityOfficialRegion(Pageable p, 
			 byte maxSeverity, PublicFigure official, Region r) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && r.equals(pf.getRegion()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityOfficialInstitution(Pageable p,
			  byte maxSeverity, PublicFigure official, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<PublicFalsehood> getRejectedFalsehoodsByMaxSeverityOfficialRegionAndInst(Pageable p,
			  byte maxSeverity, PublicFigure official, Region r, Institution i) {
		List<PublicFalsehood> ret = new LinkedList<>();
		
		for(PublicFalsehood pf: appeals)
		{
			byte sev = pf.getSeverity();
			byte status = pf.getStatus();
			
			if(status > 4 && sev >= maxSeverity && r.equals(pf.getRegion()) && i.equals(pf.getInstitution()))
				ret.add(pf);
		}
		
		return cullList(p, ret);
	}
	
}
