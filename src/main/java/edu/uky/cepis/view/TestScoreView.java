package edu.uky.cepis.view;

import java.util.Date;
import java.util.List;

import edu.uky.cepis.domain.GreTestScore;
import edu.uky.cepis.domain.PraxisIITest;
import edu.uky.cepis.domain.PraxisIITestScore;
import edu.uky.cepis.domain.PraxisITest;
import edu.uky.cepis.domain.PraxisITestScore;
import edu.uky.cepis.domain.User;

public interface TestScoreView{
	
	/***************************GRE***************************/	
	
	public GreTestScore findById(long id);
	
	public boolean addGreTestScoreToUser(User user, GreTestScore greTestScore);
	
	public boolean removeGreTestScoreFromUser(User user, GreTestScore greTestScore);
	
	public GreTestScore updateGreTestScore(User user, GreTestScore greTestScore);
	
	public boolean saveOrUpdateGreTestScore(GreTestScore greTestScore);
	
	public boolean deleteGreTestScore(GreTestScore greTestScore);

	public List<GreTestScore> findGreTestScoreByExample(GreTestScore greTestScore);
	
	/***************************PraxisII***************************/	

	public List<PraxisIITestScore> getPraxisIITestScoreList(User user);

	public List<PraxisIITestScore> getArchivedPraxisIITestScoresForUser(User user);

	public boolean addPraxisIITestScoreToUser(User user, Date testDate,	PraxisIITest praxisIITest, 
			String candidateId, double score,int primary,
			int nonStandardAdministration, int revisedScoreIndicator, int excellenceIndicator);

	public boolean removePraxisIITestScoreFromUser(User user,PraxisIITestScore praxisIITestScore);

	public PraxisIITestScore updatePraxisTestScore(User user,PraxisIITestScore praxisIITestScore, 
			Date receivedDate, Date testDate, PraxisIITest praxisIITest, String candidateId, double score,
			int primary, int nonStandardAdministration,
			int revisedScoreIndicator, int excellenceIndicator);

	public boolean addPraxisIITestScoreToUser(User user,PraxisIITestScore praxisIITestScore);
	
	public abstract PraxisIITestScore findPraxisIITestScoreById(long id);
	
	public abstract List<PraxisIITest> getPraxisIITestByCode(String testCode);
	
	public abstract List<PraxisIITest> getPraxisIITests();
	
	/***************************PraxisI***************************/	
	
	public abstract List<PraxisITest> getPraxisITests();
	
	public abstract List<PraxisITest> getPraxisITestByCode(String testCode);
	
	public abstract PraxisITest findPraxisITestById(long id);
	
	public abstract boolean addPraxisITestScoreToUser(User user,
			PraxisITestScore praxisTestScore);

	public abstract boolean addPraxisITestScoreToUser(User user, Date testDate,
			PraxisITest praxisTest, double score,int primary);

	public abstract PraxisITestScore findPraxisITestScoreById(long id);

	public abstract List<PraxisITestScore> getPraxisITestScores(User user);

	public boolean removePraxisITestScoreFromUser(User user, PraxisITestScore praxisTestScore);

	public abstract PraxisITestScore updatePraxisITestScore(User user,
			PraxisITestScore praxisTestScore, Date testDate,
			PraxisITest praxisTest,String candidateId, double score, int primary);
		
}