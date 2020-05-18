package com.stacko.capmatch.Services;



import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stacko.capmatch.Models.Department;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.SDG;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.StudentRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MatchService {	
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	FacultyRepository facultyRepo;

	public void MatchDepartment(Department department) {
		
		Deque<Student> unmatchedStudents = new LinkedList<Student> (getStudentSet(department));		
		Map<Faculty, Matches> facultyMatchMap = getFacultyMatchesMap(department);					// get all faculty within department
		
		Map<Student, PreferenceList> studentPreferences = getStudentPreferenceLists(unmatchedStudents.iterator(), department);
		Map<Faculty, Map<Student, Double>> facultyStudentSimilarityMap = new HashMap<>();
		Map<Faculty, PreferenceList> facultyPreferences = getFacultyPreferenceLists(facultyMatchMap.keySet().iterator(), facultyStudentSimilarityMap, department);	
		
		
		Student currentStudent;
		PreferenceList studentPreferenceList;
		Preference nextPreferredFaculty;
		while (!unmatchedStudents.isEmpty()) {
			currentStudent = unmatchedStudents.removeFirst();
			studentPreferenceList = studentPreferences.get(currentStudent);
			if(studentPreferenceList == null) {
				log.error("Student with email '" + currentStudent.getEmail() + "' could not be matched becuase a preference list was not found for this student");
				continue;
			}
			if (studentPreferenceList.preferenceIterator.hasNext()) {
				nextPreferredFaculty = studentPreferenceList.preferenceIterator.next();			// get next student preference
				Matches facultyMatches = facultyMatchMap.get((Faculty) nextPreferredFaculty.getUser());
				if (facultyMatches == null) {
					log.error("Could not find matches object for faculty with email '" + nextPreferredFaculty.getUser().getEmail() + "'" );
					unmatchedStudents.addLast(currentStudent);
					continue;								// Skip  this faculty if a matches object could not be found
				}
				
				Preference facultyPreferenceOfStudent = new Preference(currentStudent, facultyStudentSimilarityMap
																							.get((Faculty) nextPreferredFaculty.getUser())
																							.get(currentStudent));
				
				if (!facultyMatches.isFull()) {
					facultyMatches.addMatch(facultyPreferenceOfStudent);
					System.out.println("Student '" + facultyPreferenceOfStudent.getUser().getName() + "' matched with faculty '" 
								+ nextPreferredFaculty.getUser().getName() + "'.");
				}else {			// Student's next preferred faculty is full
					if (facultyPreferenceOfStudent.getSimilarityIndex() > facultyMatches.getLeastPrefered().getSimilarityIndex()) {			// New student more preferred
						Student removedStudent = facultyMatches.removeLeastPreferred();
						facultyMatches.addMatch(facultyPreferenceOfStudent);
						unmatchedStudents.add(removedStudent);
						System.out.println("Student " + removedStudent.getName() + "'s match with Faculty " + nextPreferredFaculty.getUser().getName()
									+ " was reversed in favour of student " + facultyPreferenceOfStudent.getUser().getName());
					}else {
						unmatchedStudents.add(currentStudent);			// add back to unmatched if proposal is rejected
						System.out.println("Student " + currentStudent.getName() + "'s proposal was rejected by faculty " + nextPreferredFaculty.getUser().getName());
					}
				}
			}
		}		
		saveMatches(facultyMatchMap);
	}
	
	
	private void saveMatches(Map<Faculty, Matches> facultyMatchMap) {
		System.err.println("FacultyMatchMap contains " + facultyMatchMap.keySet().size() + " faculty");
		for (Faculty faculty : facultyMatchMap.keySet()) {
			System.err.println("\n\n About to save matches to faculty " + faculty.getName() + "\n\n");
			// Make sure faculty is valid
			if (facultyRepo.findById(faculty.getUserId()).get() != null) {
				for (Student student : facultyMatchMap.get(faculty).matchedStudents) {
					student.bidirectionalSetSupervisor(faculty);
					studentRepo.save(student);
					System.err.println("Match Service saved match between student " + student.getName() + "and Faculty" + faculty.getName());
				}
			}else {
				log.error("Students matched to faculty with email " + faculty.getEmail() + " could not be saved because this faculty"
						+ "could not be found in the faculty repository");
			}
		}
	}
	
	
	
	private Set<Student> getStudentSet(Department department) {
		List<Set<Student>> studentsInEachDepartment = department.getMajors()
				.stream()
					.map(major -> major.getStudents())
					.collect(Collectors.toList());											// Get all students within each department
		
		Set<Student> returnSet = new HashSet<>();
		for (Set<Student> departnmentStudents: studentsInEachDepartment)
			returnSet.addAll(departnmentStudents);
		return returnSet;
	}
	
	
	private Map<Faculty, Matches> getFacultyMatchesMap(Department department){
		Map<Faculty, Matches> returnMap = new HashMap<>();
		for (Faculty faculty : department.getFaculty()) {
			returnMap.put(faculty, new Matches(faculty.getMenteeLimit()));			// create a matches object for each faculty
			
			System.err.println("\nAdded " + faculty.getName() + " to FacultyMatchMap.\t Map size: " + returnMap.size() + "\n");
		}
		return returnMap;
	}
	
	
	
	private Map<Student, PreferenceList> getStudentPreferenceLists(Iterator<Student> students, Department department){
		Map<Student, PreferenceList> studentPreferenceMap = new HashMap<>();
		
		if (students != null && department != null) {
			Student currentStudent;
			while (students.hasNext()) {
				currentStudent = students.next();
				Iterable<Faculty> allFaculty = department.getFaculty();
				PreferenceList preferenceList = new PreferenceList();
				
				for (Faculty faculty: allFaculty) {
					// For each faculty, compute similarityIndex
					double similarityIndex = getSimilarityIndex(currentStudent, faculty);
					preferenceList.addPreference(new Preference(faculty, similarityIndex));					
				}
				// For student (who are the proposers), initialize a preference iterator for use by algorithm
				preferenceList.initializeIterator();
				studentPreferenceMap.put(currentStudent, preferenceList);
			}
		}
		
		return studentPreferenceMap;
	}
	
	
	/**
	 * 
	 * NOTE:::: REWORK THIS METHOD TO REMOVE UNNCECESSARY OVERHEAD
	 * 
	 */
	private Map<Faculty, PreferenceList> getFacultyPreferenceLists(Iterator<Faculty> faculty, Map<Faculty,
											Map<Student, Double>> facultyStudentSimilarityMapping, Department department){
		Map<Faculty, PreferenceList> facultyPreferenceMap = new HashMap<>();
		
		if (faculty != null && department != null) {
			Faculty currentFaculty;
			while (faculty.hasNext()) {
				currentFaculty = faculty.next();
				Map<Student, Double> studentSimilarityMapping = new HashMap<>();				// Map to help obtain student preference for Faculty given just the student
				Iterable<Student> allStudentsInDepartment = this.getStudentSet(department);
				PreferenceList preferenceList = new PreferenceList();
				
				for (Student student: allStudentsInDepartment) {
					// For each student, compute similarityIndex
					double similarityIndex = getSimilarityIndex(currentFaculty, student);
					studentSimilarityMapping.put(student, similarityIndex);
					preferenceList.addPreference(new Preference(student, similarityIndex));					
				}
				facultyStudentSimilarityMapping.put(currentFaculty, studentSimilarityMapping);
				// For faculty (who are proposed to by students), initialize a preference list for use by algorithm
				preferenceList.initializeList();
				facultyPreferenceMap.put(currentFaculty, preferenceList);
			}
		}
		
		return facultyPreferenceMap;
	}
	
	
	
	
	
	
	/**
	 * This method is the heart of the matching process.
	 * @param user1
	 * @param user2
	 * @return Given two users, it returns a numerical value to represent how similar they are. The higher the more similar
	 */
	public double getSimilarityIndex(User user1, User user2) {
		Student student = null;
		Faculty faculty = null;
		
		if (user1 instanceof Student && user2 instanceof Faculty) {
			student = (Student) user1;
			faculty = (Faculty) user2;
		} else if (user1 instanceof Faculty && user2 instanceof Student) {
			student = (Student) user2;
			faculty = (Faculty) user1;
		}
		
		if (student == null || faculty == null) {
			throw new IllegalArgumentException("The arguments to getSimilarityIndex should consist one Student and One Faculty");
		}
		
		double basicIndex = similarityIndexHelper(student, faculty);
				

		// Adjust index for user favorites
		if (user1 instanceof Student) {
			if (((Student) user1).getFavouriteSupervisors()
									.contains((Faculty) user2)) {			// This faculty is a student favorite
				if (((Faculty) user2).getFavouriteStudents().contains((Student) user1))			// If both faculty and student have each other as favorites
					return Long.MAX_VALUE;				// Return max long value to ensure faculty is on top of student preference list
				else
					return (basicIndex + 2) * 4;				// Boost similarity index to increase chance of match
			}
		}
		
		if (user1 instanceof Faculty){
			if (((Faculty) user1).getFavouriteStudents()					
									.contains((Student) user2)) {				// Faculty has this student as a favorite
				if (((Student) user2).getFavouriteSupervisors()
										.contains((Faculty) user1))
					return Long.MAX_VALUE;
				else
					return (basicIndex + 2) * 4;				// Boost similarity index to increase chance of match
				
			}
		}
		return basicIndex;
	}
	
	
	
	/**
	 * 
	 * @param user1
	 * @param user2
	 * @return
	 */
	private double similarityIndexHelper(User user1, User user2) {		
		if (user1 == null || user2 == null)
			return 0;
		
		
		double interestsScore = 1;				// Set to one to ensure multiplication doesn't result in a zero
		double SDGScore = 1;
		
		// Go check common interest count
		for (Interest interest : user1.getInterests()) {
			if (user2.getInterests().contains(interest)) {
				System.out.println(user1.getFirstname() + " " + user1.getLastname() + " and " + user2.getFirstname() + " " + user2.getLastname() + ""
						+ " have the interest " + interest.getName() + " in common");
				interestsScore += 2.4;
			}
		}
		
		// Check for common SDGs
		for (SDG sdg : user1.getSDGs()) {
			if (user2.getSDGs().contains(sdg)) {
				System.out.println(user1.getFirstname() + " " + user1.getLastname() + " and " + user2.getFirstname() + " " + user2.getLastname() + ""
						+ " have the SDG " + sdg.getName() + " in common");
				SDGScore += 1.8;
			}
		}		
		return interestsScore * SDGScore;		
	}
		
	
	
	
	
	
	// ------------------------------------------- HELPER CLASSES -------------------------------------------------
	/**
	 * 
	 * @author Owusu-Banehene Osei
	 *
	 */
	private class Matches{
		@Getter
		private int matchLimit;
		
		private Preference[] leastPreferred ;
		
		@Getter
		private Set<Student> matchedStudents = new HashSet<>();
		
		
		// Constructor		
		public Matches(int matchLimit) throws IllegalArgumentException {
			if (matchLimit < 0) throw new IllegalArgumentException("Match limit cannot be negative");
			
			this.matchLimit = matchLimit;
			this.leastPreferred = new Preference[matchLimit];
		}
		
		
		public boolean isFull() {
			return matchedStudents.size() >= this.matchLimit;
		}
		
		
		public boolean isEmpty() { 
			return this.matchedStudents.isEmpty();
		}
		
		
		public Preference getLeastPrefered() {
			if (isEmpty())
				return null;		// return null value as sentinel
			return this.leastPreferred[this.matchedStudents.size() - 1];
		}
		
		public int size() {
			return this.matchedStudents.size();
		}
		
		
		/**
		 *  Note: The preferences of Faculty (i.e.. students are added here). If the preference of a student is used, this method will run into a TypeCastException
		 * @param preference
		 * @throws IllegalStateException
		 */
		public void addMatch(Preference preference) throws IllegalStateException {
			if (isFull())
				throw new IllegalStateException("Cannot add student match when match limit is reached");
			if (preference != null && preference.getUser() != null && !matchedStudents.contains(preference.getUser())) {		//ignore if addition is duplicate
				
				// Keep track of who is least preferred at every point
				if (this.matchedStudents.isEmpty())
					this.leastPreferred[0] = preference;				
				else if (preference.getSimilarityIndex() <= this.getLeastPrefered().getSimilarityIndex())
					this.leastPreferred[this.matchedStudents.size()] = preference;					// if the new addition is equal or less preferred, make it least preferred. First in priority
				else
					this.leastPreferred[this.matchedStudents.size()] = this.getLeastPrefered();
				
				// Add new match
				this.matchedStudents.add( (Student) preference.getUser());		// Forced type cast as students are expected to be added not faculty				
			}
		}
		
		
		public Student removeLeastPreferred() {
			if (!isEmpty()) {
				Student temp = (Student) getLeastPrefered().getUser();
				matchedStudents.remove(temp);
				this.leastPreferred[size()] = null;
				return temp;
			}
			return null;
		}		
	}
	
	
	
	/**
	 * 
	 * @author Owusu-Banahene Osei
	 *
	 */
	@AllArgsConstructor
	private class Preference{
		@Getter
		private User user;
		@Getter
		private Double similarityIndex;
		
		@Override
		public int hashCode() {
			return this.user.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			return this.user.equals(obj);
		}		
	}
	
	
	
	/**
	 * 
	 * @author Owusu-Banahene Osei
	 *
	 */
	private class PreferenceList{
		
		private Iterator<Preference> preferenceIterator;
		
		private List<Preference> list;
		
		private SortedSet<Preference> preferences = new TreeSet<>(
				(preference1, preference2) -> preference1.getSimilarityIndex().compareTo(preference2.getSimilarityIndex()) * -1);				// *-1 to invert sort order to non-increasing
		
		public void addPreference(Preference preference) {
			if (preference != null && preference.getUser() != null)
				this.preferences.add(preference);
		}
		
		
		public void initializeIterator() {
			this.preferenceIterator = preferences.iterator();
		}
		
		public void initializeList() {
			this.list = new ArrayList<>(this.preferences);
		}
				
	}
	
}
