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
    
    private static List<Falsehood> cullList(Pageable p, List<Falsehood> f)
    {
        int number = p.getPageNumber();
        int size = p.getPageSize();

        int start = number * size;

        int end = start + size;
        return end < f.size() ? f.subList(start, end) : f;
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
        return  new TrecPage<Falsehood>( cullList(pageable, new ArrayList<>(appeals)));
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

	@Override
	public List<Falsehood> getSubmittedFalsehood(Pageable p) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			if(f.getStatus() == (byte)0)
				ret.add(f);
		}
		
       
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoods(Pageable p) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			if(stat > 1 && stat < 5)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficial(Pageable p, PublicFigure author) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			if(stat > 1 && stat < 5 && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2())))
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBySeverity(Pageable p, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			byte sev = f.getSeverity();
			if(stat > 1 && stat < 5 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodByMinSeverity(Pageable p, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			byte sev = f.getSeverity();
			if(stat > 1 && stat < 5 && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodByMaxSeverity(Pageable p, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			byte sev = f.getSeverity();
			if(stat > 1 && stat < 5 & sev >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoods(Pageable p) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			if(f.getStatus() > 4)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficial(Pageable p, PublicFigure author) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			if(f.getStatus() > 4 && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2())))
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBySeverity(Pageable p, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			byte sev = f.getSeverity();
			if(f.getStatus() > 4 && sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodByMinSeverity(Pageable p, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			byte sev = f.getSeverity();
			if(f.getStatus() > 4 && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodByMaxSeverity(Pageable p, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			byte sev = f.getSeverity();
			if(f.getStatus() > 4 && sev >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	
	
	
	
	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetween(Pageable p, Date begin, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			if(stat > 1 && stat < 5 && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		ret = cullList(p,ret);
		return ret;
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBefore(Pageable p, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			if(stat > 1 && stat < 5 && f.getDateMade().getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByOutlet(Pageable p, MediaOutlet outletId) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId))
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByOutletAndSeverity(Pageable p, MediaOutlet outletId, byte maxSeverity,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			byte sev = f.getSeverity();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByOutletAndMinSeverity(Pageable p, MediaOutlet outletId,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && f.getSeverity() <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByOutletAndMaxSeverity(Pageable p, MediaOutlet outletId,
			byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && f.getSeverity() >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByOutlet(Pageable p, MediaOutlet outletId, Date begin,
			Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			Date d = f.getDateMade();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByOutlet(Pageable p, MediaOutlet outletId, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && f.getDateMade().getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficialAndOutlet(Pageable p, PublicFigure author,
			MediaOutlet outletId) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2())))
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetween(Pageable p, Date begin, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			if(stat > 4 && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBefore(Pageable p, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			if(stat > 4 && f.getDateMade().getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByOutlet(Pageable p, MediaOutlet outletId) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && mo.equals(outletId))
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByOutletAndSeverity(Pageable p, MediaOutlet outletId, byte maxSeverity,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			byte sev = f.getSeverity();
			if(stat > 4 && mo != null && mo.equals(outletId) && sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByOutletAndMinSeverity(Pageable p, MediaOutlet outletId,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && mo.equals(outletId) && f.getSeverity() <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByOutletAndMaxSeverity(Pageable p, MediaOutlet outletId,
			byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && mo.equals(outletId) && f.getSeverity() >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByOutlet(Pageable p, MediaOutlet outletId, Date begin,
			Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			Date d = f.getDateMade();
			if(stat > 4 && mo != null && mo.equals(outletId) && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByOutlet(Pageable p, MediaOutlet outletId, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && mo.equals(outletId) && f.getDateMade().getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficialAndOutlet(Pageable p, PublicFigure author,
			MediaOutlet outletId) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2())))
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficialAndSeverity(Pageable p, PublicFigure author,
			byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			byte sev = f.getSeverity();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
				&& sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficialAndMinSeverity(Pageable p, PublicFigure author,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
				&& f.getSeverity() <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficialAndMaxSeverity(Pageable p, PublicFigure author,
			byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
				&& f.getSeverity() >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByPublicOfficial(Pageable p, PublicFigure author, Date begin,
			Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			Date d = f.getDateMade();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByPublicOfficial(Pageable p, PublicFigure author, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			Date d = f.getDateMade();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficialOutletAndSeverity(Pageable p, PublicFigure author,
			MediaOutlet outletId, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			byte sev = f.getSeverity();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2())) 
					&& sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficialOutletAndMinSeverity(Pageable p, PublicFigure author,
			MediaOutlet outletId, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsByPublicOfficialOutletAndMaxSeverity(Pageable p, PublicFigure author,
			MediaOutlet outletId, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficialAndSeverity(Pageable p, PublicFigure author,
			byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			byte sev = f.getSeverity();
			if(stat > 4 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
				&& sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficialAndMinSeverity(Pageable p, PublicFigure author,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
				&& f.getSeverity() <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficialAndMaxSeverity(Pageable p, PublicFigure author,
			byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
				&& f.getSeverity() >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByPublicOfficial(Pageable p, PublicFigure author, Date begin,
			Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			Date d = f.getDateMade();
			if(stat > 4 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByPublicOfficial(Pageable p, PublicFigure author, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getDateMade().getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficialOutletAndSeverity(Pageable p, PublicFigure author,
			MediaOutlet outletId, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			byte sev = f.getSeverity();
			if(stat > 4 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2())) 
					&& sev >= maxSeverity && sev <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficialOutletAndMinSeverity(Pageable p, PublicFigure author,
			MediaOutlet outletId, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsByPublicOfficialOutletAndMaxSeverity(Pageable p, PublicFigure author,
			MediaOutlet outletId, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			
			MediaOutlet mo = f.getOutlet();
			if(stat > 4 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity)
				ret.add(f);
		}
		
		return cullList(p, ret);
	}
	
	
	
	////////////////////////////////////////
	
	

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndBySeverity(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity
					&& d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMinSeverity(Pageable p, Date begin, Date end,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null&& f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverity(Pageable p, Date begin, Date end,
			byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && f.getSeverity() >= maxSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndBySeverity(Pageable p, Date end, byte maxSeverity,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMinSeverity(Pageable p, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverity(Pageable p, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && f.getSeverity() >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndBySeverityPublicFigure(Pageable p, PublicFigure author,
			Date begin, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityPublicFigure(Pageable p, PublicFigure author,
			Date begin, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityPublicFigure(Pageable p, PublicFigure author,
			Date begin, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null  &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndBySeverityPublicFigure(Pageable p, PublicFigure author,
			Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityPublicFigure(Pageable p, PublicFigure author,
			Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityPublicFigure(Pageable p, PublicFigure author,
			Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndBySeverityOutlet(Pageable p, MediaOutlet outletId,
			Date begin, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId)
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date begin, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date begin, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) 
					&& f.getSeverity() >= maxSeverity  && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndBySeverityOutlet(Pageable p, MediaOutlet outletId, Date end,
			byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) 
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) && f.getSeverity() >= maxSeverity  && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndBySeverityOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date begin, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date begin, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date begin, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndBySeverityOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMinSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByMaxSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBetweenAndByOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date begin, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getConfirmedFalsehoodsBeforeAndByOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 1 && stat < 5 && mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}
	
	
	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndBySeverity(Pageable p, Date begin, Date end,
			byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity
					&& d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMinSeverity(Pageable p, Date begin, Date end,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null&& f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMaxSeverity(Pageable p, Date begin, Date end,
			byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && f.getSeverity() >= maxSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndBySeverity(Pageable p, Date end, byte maxSeverity,
			byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMinSeverity(Pageable p, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMaxSeverity(Pageable p, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && f.getSeverity() >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndBySeverityPublicFigure(Pageable p, PublicFigure author,
			Date begin, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMinSeverityPublicFigure(Pageable p, PublicFigure author,
			Date begin, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityPublicFigure(Pageable p, PublicFigure author,
			Date begin, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null  &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndBySeverityPublicFigure(Pageable p, PublicFigure author,
			Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMinSeverityPublicFigure(Pageable p, PublicFigure author,
			Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityPublicFigure(Pageable p, PublicFigure author,
			Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && (author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndBySeverityOutlet(Pageable p, MediaOutlet outletId,
			Date begin, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId)
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date begin, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date begin, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) 
					&& f.getSeverity() >= maxSeverity  && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndBySeverityOutlet(Pageable p, MediaOutlet outletId, Date end,
			byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) 
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOutlet(Pageable p, MediaOutlet outletId,
			Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) && f.getSeverity() >= maxSeverity  && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndBySeverityOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date begin, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMinSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date begin, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByMaxSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date begin, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndBySeverityOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date end, byte maxSeverity, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMinSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date end, byte minSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() <= minSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByMaxSeverityOutletPublicFigure(Pageable p,
			PublicFigure author, MediaOutlet outletId, Date end, byte maxSeverity) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& f.getSeverity() >= maxSeverity && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBetweenAndByOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date begin, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& d.getTime() >= begin.getTime() && d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}

	@Override
	public List<Falsehood> getRejectedFalsehoodsBeforeAndByOutletPublicFigure(Pageable p, PublicFigure author,
			MediaOutlet outletId, Date end) {
		List<Falsehood> ret = new LinkedList<>();
		
		for(Falsehood f: appeals)
		{
			int stat = f.getStatus();
			Date d = f.getDateMade();
			MediaOutlet mo = f.getOutlet();
			if(stat > 4&& mo != null && mo.equals(outletId) &&(author.equals(f.getAuthor1()) || author.equals(f.getAuthor2()))
					&& d.getTime() <= end.getTime())
				ret.add(f);
		}
		
		return cullList(p, ret);
	}
}
