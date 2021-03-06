/**
 * 
 */
package edu.uky.cepis.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import edu.uky.cepis.domain.Ranking;

/**
 * @author scott
 * 
 */
public class RankingDaoImpl implements RankingDao {

	private static Logger log = Logger.getLogger(RankingDaoImpl.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public boolean createRanking(String rankingCode, String title,
			String program) {
		Ranking ranking = new Ranking(rankingCode, title, program);
		if (!this.saveRanking(ranking)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateRanking(Ranking ranking, String rankingCode,
			String title, String program) {
		if (ranking == null) {
			return false;
		}
		ranking.setRankingCode(rankingCode);
		ranking.setTitle(title);
		ranking.setProgram(program);

		this.saveRanking(ranking);
		return true;
	}

	@Override
	public boolean deleteRanking(Ranking ranking) {
		if (ranking == null) {
			return false;
		}

		this.sessionFactory.getCurrentSession().delete(ranking);
		return true;
	}

	@Override
	public Ranking findRankingByCode(String rankingCode) {
		if (rankingCode.isEmpty()) {
			return null;
		}
		List<Ranking> rankings = null;
		Ranking ranking = null;

		String hql = "select a from Ranking a where a.rankingCode ='"
				+ rankingCode + "'";
		rankings = this.sessionFactory.getCurrentSession().createQuery(hql)
				.list();
		if (rankings.size() < 1) {
			return null;
		}
		ranking = rankings.get(0);

		return ranking;
	}

	@Override
	public List<Ranking> getRankingList() {
		List<Ranking> rankings = new ArrayList<Ranking>(0);

		String hql = "select a from Ranking a order by a.title";
		rankings = this.sessionFactory.getCurrentSession().createQuery(hql).list();

		return rankings;
	}

	@Override
	public boolean saveRanking(Ranking ranking) {

		this.sessionFactory.getCurrentSession().saveOrUpdate(ranking);
		return true;
	}

}
