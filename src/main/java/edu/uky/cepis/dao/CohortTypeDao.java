/**
 * 
 */
package edu.uky.cepis.dao;

import java.util.List;

import edu.uky.cepis.domain.CohortType;

/**
 * @author keerthi
 * 
 */
public interface CohortTypeDao {
	public CohortType createCohortType(String typeCode, String shortDesc,
			String desc, String status);

	public CohortType updateCohortType(CohortType cohortType, String typeCode,
			String shortDesc, String desc, String status);

	public boolean deleteCohortType(CohortType cohortType);

	public CohortType findCohortTypeByCode(String typeCode);

	public List<CohortType> getCohortTypeList();

	public boolean saveCohortType(CohortType cohortType);
}
