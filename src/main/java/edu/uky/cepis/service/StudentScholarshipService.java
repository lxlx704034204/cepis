package edu.uky.cepis.service;

import java.util.List;

import edu.uky.cepis.domain.StudentScholarship;
import edu.uky.cepis.domain.User;

/**
 * @author cawalk4
 */
public interface StudentScholarshipService {

	public abstract StudentScholarship findStudentScholarshipById(long id);
	
	public abstract boolean saveOrUpdateStudentScholarship(StudentScholarship studentScholarship);
	
	public abstract boolean deleteStudentScholarship(StudentScholarship studentScholarship);
	
	public abstract List<StudentScholarship> getStudentScholarshipByExample(StudentScholarship studentScholarship);
	
	public abstract List<StudentScholarship> getStudentScholarshipForUser(User user);
}
