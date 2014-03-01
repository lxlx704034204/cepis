package edu.uky.cepis.dao;

import java.util.List;

import edu.uky.cepis.domain.CharacterAndFitness;
import edu.uky.cepis.domain.User;

/**
 * @author cawalk4
 *
 */
public interface CharacterAndFitnessDao{
	
	public abstract CharacterAndFitness findCharacterAndFitnessById(long id);
	
	public abstract List<CharacterAndFitness> findCharacterAndFitnessByExample(CharacterAndFitness characterAndFitness);
	
	public abstract boolean saveOrUpdateCharacterAndFitness(CharacterAndFitness characterAndFitness);
	
	public abstract boolean deleteCharacterAndFitness(CharacterAndFitness characterAndFitness);
	
	public abstract List<CharacterAndFitness> getCharacterAndFitnessForUser(User user);
}