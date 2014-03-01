/**
 * 
 */
package edu.uky.cepis.controller;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.component.UIRepeat;
import org.ajax4jsf.model.DataComponentState;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportUser;
import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.component.html.HtmlExtendedDataTable;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.SimpleSelection;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import edu.uky.cepis.domain.Address;
import edu.uky.cepis.domain.AddressType;
import edu.uky.cepis.domain.Advisor;
import edu.uky.cepis.domain.Artifact;
import edu.uky.cepis.domain.CohortTerm;
import edu.uky.cepis.domain.CohortType;
import edu.uky.cepis.domain.Course;
import edu.uky.cepis.domain.EmailAddress;
import edu.uky.cepis.domain.EmailAddressType;
import edu.uky.cepis.domain.FacultyAdvisor;
import edu.uky.cepis.domain.HoldLift;
import edu.uky.cepis.domain.Note;
import edu.uky.cepis.domain.Phone;
import edu.uky.cepis.domain.PhoneType;
import edu.uky.cepis.domain.Role;
import edu.uky.cepis.domain.Standard;
import edu.uky.cepis.domain.StandardSet;
import edu.uky.cepis.domain.User;
import edu.uky.cepis.domain.UserAssessmentProfile;
import edu.uky.cepis.domain.UserAssessmentStandard;
import edu.uky.cepis.domain.UserAssessmentStandardSet;
import edu.uky.cepis.domain.UserClassification;
import edu.uky.cepis.domain.UserCollegeProfile;
import edu.uky.cepis.domain.UserConfiguration;
import edu.uky.cepis.domain.UserPersonalProfile;
import edu.uky.cepis.domain.UserProgramProfile;
import edu.uky.cepis.domain.UserType;
import edu.uky.cepis.domain.WorkingSet;
import edu.uky.cepis.domain.component.Program;
import edu.uky.cepis.domain.component.ProgramDomain;
import edu.uky.cepis.domain.component.ProgramGroup;
import edu.uky.cepis.domain.component.ProgramTrack;
import edu.uky.cepis.domain.component.UKCollege;
import edu.uky.cepis.domain.component.UKDepartment;
import edu.uky.cepis.domain.component.UKMajor;
import edu.uky.cepis.domain.component.UKStatus;
import edu.uky.cepis.domain.component.UKTerm;
import edu.uky.cepis.util.global.CEPISGlobalComponentConfigurator;
import edu.uky.cepis.util.operations.security.CEPISPGPUtil;
import edu.uky.cepis.view.AccountManagementView;
import edu.uky.cepis.view.NoteView;

/**
 * @author cawalk4
 */

public class AccountManagementController extends AbstractController implements
		PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger
			.getLogger(AccountManagementController.class);
	private AccountManagementView accountManagementBean;
	private String address;
	private int badgeHolder;
	private List<Address> addressList = new ArrayList<Address>(0);
	private boolean allowReportModuleAccess;
	private CEPISGlobalComponentConfigurator cepisGlobalComponentConfigurator;
	
	private String city;
	private String country;
	private Date createdOn;
	private Integer degreeAwarded;

	private Date dOB;
	private String email;
	private List<EmailAddress> emailAddressList = new ArrayList<EmailAddress>(0);
	private String ext;
	private String fName;
	private String fullName;
	private String gender;

	private boolean hiddenToStudent;
	private HtmlDataTable htmlDataTable;
	private HtmlSelectOneMenu htmlSelectOneMenu;
	private String lName;
	private String lName1;
	private String lName2;
	private String lName3;
	private String lName4;
	private String preferredName;
	private Locale locale;
	private String maidenName;
	private String mName;
	private String finalGPA;
	private boolean recommendCertification;
	private List<Phone> phoneList = new ArrayList<Phone>(0);	
	private String phoneNo;
	private boolean selectCustomRoleWorkingSet;
	private Address selectedAddress;
	private AddressType selectedAddressType;
	private Advisor selectedAdvisor;
	private FacultyAdvisor selectedFacultyAdvisor;
	private CohortTerm selectedCohortTerm;
	private CohortType selectedCohortType;
	private EmailAddress selectedEmailAddress;
	private EmailAddressType selectedEmailAddressType;
	
	private Phone selectedPhone;
	private Phone primaryPhone;
	private EmailAddress primaryEmailAddress;
	private PhoneType selectedPhoneType;
	private Program selectedProgram;
	private ProgramDomain selectedProgramDomain;
	private ProgramGroup selectedProgramGroup;
	private ProgramTrack selectedProgramTrack;
	private Role selectedRole;
	private List<Role> selectedRoles = new ArrayList<Role>(0);	
	
	private UKCollege selectedUkCollege;
	private UKTerm selectedUKAnticipatedCompleteTerm;
	private UKTerm selectedUKCompleteTerm;
	private UKDepartment selectedUkDepartment;
	private UKMajor selectedUKMajor;
	private UserClassification selectedUserClassification;
	private UserCollegeProfile selectedUserCollegeProfile;
	
	private UserProgramProfile selectedUserProgramProfile;
	private UserType selectedUserType;
	private WorkingSet selectedWorkingSet;
	private String shortDesc;
	private boolean showAddress;
	private boolean showEmail;
	private boolean showPersonalProfile;
	private boolean showPhone;
	private boolean showProgramProfile;
	private boolean showReport;
	private boolean showRole;
	private boolean showCollegeProfileEditModalWindow;
	private boolean showCollegeProfileAddModalWindow;
	private boolean showProgramProfileEditModalWindow;
	private boolean showProgramProfileAddModalWindow;
	private boolean showPersonalProfileEditModalWindow;
	private boolean showPersonalProfileAddModalWindow;
	private boolean enableNonUKUserOptions;
	private boolean loadAddress = true;		
	private boolean loadArchivedPraxisTestScore = true;
	
	private boolean loadEmail = true;
	private boolean loadPersonalProfile = true;
	private boolean loadPhone = true;
	private boolean loadProgramProfile = true;
	private boolean loadCollegeProfile = true;
	private boolean loadRole = true;
	private boolean loadCourses = true;
	private boolean loadHoldLifts = true;
	private boolean showProgramProfileCompleteOptions;
	private boolean bypassOldPasswordCheck = false;	
	private String ssn;
	private String state;
	private String street1;
	private String street2;
	private String suffix;
	private TimeZone timeZone;
	private String title;
	private String uid;
	private String ukID;
	private UIExtendedDataTable dataTable;
	private Object tableState;
	private SimpleSelection selection = new SimpleSelection();
	private UIExtendedDataTable holdLiftDataTable;
	private Object holdLiftTableState;
	private SimpleSelection holdLiftSelection = new SimpleSelection();
	private List<Course> courses = new ArrayList<Course>(0);
	private List<HoldLift> holdLifts = new ArrayList<HoldLift>(0);
	private ExtendedTableDataModel<Course> courseDataModel;
	private ExtendedTableDataModel<HoldLift> holdLiftDataModel;
	private List<Course> selectedCourses = new ArrayList<Course>();
	private List<UserCollegeProfile> userCollegeProfileList = new ArrayList<UserCollegeProfile>(0);
	private String username;
	private String password;
	private String verifyPassword;
	private String oldPassword;
	private int enabled;
	
	private int assessmentType;
	// List of new assessment profiles
	private List<UserAssessmentProfile> userAssessmentProfiles = new ArrayList<UserAssessmentProfile>(
			0);
	private Set<Integer> stdSetKeys = new HashSet<Integer>(0);
	private Set<Integer> assessmentKeys = new HashSet<Integer>(0);
	private Set<Integer> standardKeys = new HashSet<Integer>(0);
	private HtmlSelectOneMenu stdScoreComponent;
	private HtmlSelectOneMenu finalScoreComponent;
	private int displayMode;
	private int userAction;
	private int activeAssessmentProfileKey;
	private int activeStdSetKey;
	private int activeStandardKey;
	private boolean showAssessmentDataEntryPanel;
	private List<User> selectedUsers = new ArrayList<User>(0);
	private UserAssessmentProfile selectedUserAssessmentProfile;
	private UKTerm selectedUKTerm;
	private Map<String, Object> showArtifactFlags = new HashMap<String, Object>();
	private UIRepeat repeater;
	private UIRepeat stdRepeater;
	private DataComponentState repeaterState = null;
	private List<Artifact> assessmentArtifacts = new ArrayList<Artifact>(0);

	private HtmlOutputText artifactText;

	// Assessment profiles of a selected user
	private List<UserAssessmentProfile> userAssessmentProfileList = new ArrayList<UserAssessmentProfile>(
			0);
	private boolean loadUserAssessmentProfileList = true;

	
	
	private UserPersonalProfile userPersonalProfile = new UserPersonalProfile();
	
	//Used when adding a new cepis user
	private UserPersonalProfile addUserPersonalProfile = new UserPersonalProfile();
	
	private List<UserProgramProfile> userProgramProfileList = new ArrayList<UserProgramProfile>(0);
	private List<UserProgramProfile> studentUserProgramProfiles = new ArrayList<UserProgramProfile>(0);
	private List<Role> userRoles = new ArrayList<Role>(0);
	private String zip;			
	private String cpShortDesc;
	private String ppShortDesc;
	private UKStatus cpSelectedUKStatus;
	private UKStatus ppSelectedUKStatus;
	private UKTerm cpSelectedUKStartTerm;
	private UKTerm cpSelectedUKEndTerm;
	private UKTerm ppSelectedUKStartTerm;
	private String currentAccountSection;
	
	/*********************************************NOTES**********************************************/	
	@Resource(name="noteBean")
	private NoteView noteBean;
	
	private List<Note> notes;
	private Note selectedNote;
		
	private HtmlExtendedDataTable noteTable;
	private SimpleSelection noteSelection = new SimpleSelection();
	private ExtendedTableDataModel<Note> noteDataModel;
	private Object noteTableState;
	
	/**************************GPA**************************/	
	private float cumulativeGpa;	
	private int totalQualityHours;	
	private int totalQualityPoints;
	
	
	private static String USER_PERSONAL_PROFILE_DELETE_SUCCESS = "User Personal Profile Sucessfully Deleted for user:";
	private static String USER_PERSONAL_PROFILE_DELETE_FAIL = "User Personal Profile Failed To Delete. PLEASE CONTACT AN ADMINISTRATOR.";
	private static String USER_PERSONAL_PROFILE_ERROR = "Selected User does not have a Personal Profile. On: ";
	

	
	
	public AccountManagementController() {

	}
	
	
	/*********************************************NOTES**********************************************/	
	public String getInitNotes() {
		//Load notes for current user
		List<Note> currentUsersNotes = getNoteBean().getNotesForCurrentUser(this.getConfigurationManager().getSelectedUser());	
		setNotes(new ArrayList<Note>());
 		for (Note tempNote : currentUsersNotes){
 			if(tempNote.getNoteHidden() == false){
 				getNotes().add(tempNote);
 			}
 			if(tempNote.getNoteHidden() == true){
 				if(this.getUserManager().getUser().getFullName().equals(tempNote.getEnteredBy())){
 					getNotes().add(tempNote);
 				}
 			}
 		}
		return " ";
	}
	
	public ExtendedTableDataModel<Note> getSelectedNotesDataModel() {
		log.debug("Calling AccountManagementController.getSelectedNotesDataModel()");
					
		this.setSelection(new SimpleSelection());
		this.setNoteDataModel(new ExtendedTableDataModel<Note>(
				new DataProvider<Note>() {

					private static final long serialVersionUID = 5054087821033164847L;

					public Note getItemByKey(Object key) {
						for (Note a : getNotes()) {
							if (key.equals(getKey(a))) {
								return a;
							}
						}
						return null;
					}

					public List<Note> getItemsByRange(int firstRow, int endRow) {
						if(getNotes() == null){
							setNotes(new ArrayList<Note>());
						}
							log.debug("Note: " + getNotes().size());
							Iterator<Note> iter = getNotes().iterator();
							while (iter.hasNext()) {
								log.debug(iter.next());
							}
							return getNotes().subList(firstRow, endRow);						
					}

					public Object getKey(Note item) {
						return item.getId();
					}

					public int getRowCount() {
						if(getNotes() != null){
							return getNotes().size();
						}else{
							return 0;
						}
					}
				}));
				getNoteDataModel().setRowIndex(-1);
		return this.getNoteDataModel();
	}
	
	public void takeNoteSelection() {
		Object key = null;
		if(this.getNoteTable() != null){
			key = this.getNoteTable().getActiveRowKey();
		}					
		Note note = findNoteByID((long)key);
		if (note != null) {
			setSelectedNote(note);
		}		
	}
	
	private Note findNoteByID(long key) {
		for (Note item : notes) {
			if (key == item.getId()) {
				return item;
			}
		}
		return null;
	}
		
	/*********************************************PERSONAL PROFILE**********************************************/	
	public void removeUserPersonalProfileFromUser() {		
		log.debug("Calling AccountManagementController.removeUserPersonalProfileFromUser()");
		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		
		UserPersonalProfile profileToDelete = getUserPersonalProfile();
		
		//Step 1: Check if the user has a personal profile
		if(getUserPersonalProfile().equals(new UserPersonalProfile())){
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary(USER_PERSONAL_PROFILE_ERROR + new Date() );
			context.addMessage("msg", message);
		}else{			
			if(getAccountManagementBean().deleteUserPersonalProfile(profileToDelete)){
				setLoadPersonalProfile(true);
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary( USER_PERSONAL_PROFILE_DELETE_SUCCESS
				+ getConfigurationManager().getSelectedUser().getFullName()
				+ ". On: "+ new Date() );
				context.addMessage("msg", message);
			}else{
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary(USER_PERSONAL_PROFILE_DELETE_FAIL);
				context.addMessage("msg", message);
			}
		}	
	}
	
	
	public void editUserPersonalProfile() {
		log.debug("Calling AccountManagementController.editUserPersonalProfile()");
		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
				
		UserPersonalProfile newUserPersonalProfile = getUserPersonalProfile();
		newUserPersonalProfile.setUser(getConfigurationManager().getSelectedUser());
				
		if (this.getAccountManagementBean().saveOrUpdateUserPersonalProfile(newUserPersonalProfile)) {
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User personal profile updated successfully.");
			context.addMessage("msg", message);
			this.setLoadPersonalProfile(true);
			
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot edit User personal profile information.");
			context.addMessage("msg", message);
		}
	}
	
	/*********************************************ADD NEW CEPIS USER**********************************************/
	public String addCEPISUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		User newuser = null;
		UserCollegeProfile userCollegeProfile = null;
		UserProgramProfile userProgramProfile = null;
		UserPersonalProfile userPersonalProfile = null;
		Phone phone = null;
		Address address = null;
		EmailAddress emailAddress = null;
		List<Role> roles = new ArrayList<Role>(0);
		ReportUser reportUser = null;
		log.debug("Adding ... " + username + " - " + lName + "," + mName + ","
				+ fName);
		if (this.getAccountManagementBean().checkDuplicacy(this.username,
				this.ukID)) {
			log.debug("Duplicate user found");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date() + "] Duplicate Entry: User "
					+ this.lName + ", " + this.mName
					+ " already exists in our portal. ");
			context.addMessage("msg", message);
			this.clearAllValues();
			return null;
		}
		log.debug("Creating User Object");
		if (preferredName == null || preferredName.isEmpty()) {
			preferredName = lName;
		}
		newuser = new User(ukID, ssn, username, lName, lName1, lName2, lName3,
				lName4, preferredName, mName, fName, maidenName, suffix,
				gender, title, dOB, badgeHolder);

		if (this.isEnableNonUKUserOptions()) {
			if (password == null || password.isEmpty()
					|| verifyPassword == null || verifyPassword.isEmpty()) {
				log.debug("Password invalid or null");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Either the password field is empty or you have entered an invalid password. ");
				context.addMessage("msg", message);
				this.setPassword(null);
				this.setVerifyPassword(null);
				return null;
			}
			if (!this.verifyPasswords()) {
				log.debug("Passwords does not match");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Password and Verify Password fields doesn't match. Please try again. ");
				context.addMessage("msg", message);
				this.setPassword(null);
				this.setVerifyPassword(null);
				return null;
			}
			log.debug("Generating MD5 hash value of the passowrd ...");
			String md5Password = CEPISPGPUtil.getMD5Hash(password);
			newuser.setPassword((md5Password != null) ? md5Password.trim()
					: null);
			// Add ROLE_NONUK
			Role nonUKRole = this.getAccountManagementBean().findRoleByName(
					"ROLE_NONUK");
			if (nonUKRole == null) {
				log.debug("Cannot find nonuk role");
			}
			roles.add(nonUKRole);

		}
		newuser.setEnabled(1);
		log.debug("Creating UserCollegeProfile Object");
		userCollegeProfile = new UserCollegeProfile(
				this.getSelectedUserType(),
				this.getSelectedUserClassification(),
				this.getSelectedUkCollege(), 
				this.getSelectedUkDepartment(),
				this.getCpSelectedUKStartTerm(),
				this.getCpSelectedUKEndTerm(), 
				this.getCpSelectedUKStatus(),
				this.getSelectedCohortType(), 
				this.getSelectedCohortTerm(),
				this.getCpShortDesc());
		if (this.isShowProgramProfile()) {
			log.debug("Creating UserProgramProfile Object");
			userProgramProfile = new UserProgramProfile(
					RandomStringUtils.randomNumeric(32),
					this.getSelectedProgram(), 
					this.getSelectedProgramDomain(),
					this.getSelectedProgramTrack(),
					this.getSelectedProgramGroup(), 
					this.getSelectedAdvisor(),
					this.getSelectedFacultyAdvisor(),
					this.getSelectedUKMajor(), 
					this.getPpSelectedUKStatus(),
					this.getPpSelectedUKStartTerm(),
					this.getSelectedUKAnticipatedCompleteTerm(),
					this.getSelectedUKCompleteTerm(), finalGPA, degreeAwarded,
					hiddenToStudent, recommendCertification,
					this.getPpShortDesc());
		}
		
		if (this.isShowPersonalProfile()) {
			log.debug("Creating UserPersonalProfile Object");			
			userPersonalProfile = getAddUserPersonalProfile();
			userPersonalProfile.setUser(newuser);
		}
		if (this.isShowAddress()) {
			log.debug("Creating Address Object");
			address = new Address(this.getStreet1(), this.getStreet2(),
					this.getCity(), this.getState(), this.getZip(),
					this.getCountry(), this.getSelectedAddressType());
		}
		if (this.isShowEmail()) {
			log.debug("Creating Email Object");
			emailAddress = new EmailAddress(this.getAddress(),
					this.getSelectedEmailAddressType());
		}
		if (this.isShowPhone()) {
			log.debug("Creating Phone Object");
			phone = new Phone(this.getPhoneNo(), this.getExt(),
					this.getShortDesc(), this.getSelectedPhoneType());
		}
		if (this.isShowRole()) {
			log.debug("Creating Roles Object");
			roles.addAll(this.getSelectedRoles());
		}
		if (this.isShowReport()) {
			log.debug("Creating ReportUser Object");
			reportUser = new ReportUser();
			reportUser.setName(username);
			reportUser.setEmail(this.getAddress());
			reportUser.setPdfExportType(0);
		}
		log.debug("Calling Add CEPIS User DAO");

		newuser = this.getAccountManagementBean().addCEPISUser(newuser,
				userCollegeProfile, userProgramProfile, userPersonalProfile,
				phone, address, emailAddress, roles, reportUser);

		if (newuser != null) {
			log.debug("User object created Successfully");
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] CEPIS User Created Successfully And Added To Working Set.");
			
			context.addMessage("msg", message);
			log.debug("Adding user to current working set");
			List<User> newUsers = new ArrayList<User>(0);
			
			//ADD User Just Created to the working set
			newUsers.add(newuser);
			this.getAccountManagementBean().addUsersToWorkingSet(
					this.getConfigurationManager().getSelectedWorkingSet(),
					newUsers);
			log.debug("Clearing junk values");
			this.clearAllValues();
		} else {
			log.debug("Cannot create user account.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Unknown Error: Cannot add user information.");
			context.addMessage("msg", message);
		}
		return null;
	}

	/*********************************************CLEAR METHODS**********************************************/
	public String clearIdentityValues() {
		log.debug("Calling clear Identity");
		this.setUsername(null);
		this.setPassword(null);
		this.setVerifyPassword(null);
		this.setEnableNonUKUserOptions(false);
		this.setUkID(null);
		this.setSsn(null);
		this.setlName(null);
		this.setlName1(null);
		this.setlName2(null);
		this.setlName3(null);
		this.setlName4(null);
		this.setPreferredName(null);
		this.setfName(null);
		this.setmName(null);
		this.setMaidenName(null);
		this.setSuffix(null);
		this.setTitle(null);
		this.setGender(null);
		this.setdOB(null);
		this.setBadgeHolder(0);
		return "";
	}
	
	public String clearPersonalProfileValues() {
		log.debug("Calling clear personal profile");
		this.setShowPersonalProfile(false);
		setAddUserPersonalProfile(new UserPersonalProfile());
		setUserPersonalProfile(new UserPersonalProfile());
		return "";
	}
	
	public String clearCollegeProfileValues() {
		log.debug("Calling clear college profile");
		this.setSelectedUserType(null);
		this.setSelectedUserClassification(null);
		this.setSelectedUkCollege(null);
		this.setSelectedUkDepartment(null);
		this.setCpSelectedUKStatus(null);
		this.setCpSelectedUKStartTerm(null);
		this.setCpSelectedUKEndTerm(null);
		this.setSelectedCohortType(null);
		this.setSelectedCohortTerm(null);
		this.setCpShortDesc(null);
		return "";
	}

	public String clearProgramProfileValues() {
		log.debug("Calling clear program profile");
		this.setShowProgramProfile(false);
		this.setSelectedProgram(null);
		this.setSelectedProgramDomain(null);
		this.setSelectedProgramTrack(null);
		this.setSelectedProgramGroup(null);
		this.setSelectedUKMajor(null);
		this.setPpSelectedUKStatus(null);
		this.setPpSelectedUKStartTerm(null);
		this.setSelectedUKCompleteTerm(null);
		this.setFinalGPA(null);
		this.setRecommendCertification(false);
		this.setSelectedUKAnticipatedCompleteTerm(null);
		this.setSelectedAdvisor(null);
		this.setDegreeAwarded(null);
		this.setPpShortDesc(null);
		return "";
	}

	public String clearEmailAddressValues() {
		log.debug("Calling clear Email");
		this.setShowEmail(false);
		this.setAddress(null);
		this.setSelectedEmailAddressType(null);
		return "";
	}

	public String clearPhoneValues() {
		log.debug("Calling clear Phone");
		this.setShowPhone(false);
		this.setPhoneNo(null);
		this.setExt(null);
		this.setSelectedPhoneType(null);
		return "";
	}

	public String clearReportValues() {
		log.debug("Calling clear Report");
		this.setShowReport(false);
		this.setAllowReportModuleAccess(false);
		return "";
	}

	public String clearRoleValues() {
		log.debug("Calling clear Roles");
		this.setShowRole(false);
		this.setSelectedRoles(new ArrayList<Role>(0));
		this.setSelectCustomRoleWorkingSet(false);
		this.setSelectedWorkingSet(null);
		return "";
	}

	public String clearAddressValues() {
		log.debug("Calling clear Address");
		this.setShowAddress(false);
		this.setStreet1(null);
		this.setStreet2(null);
		this.setCity(null);
		this.setState(null);
		this.setZip(null);
		this.setCountry(null);
		this.setSelectedAddressType(null);
		return "";
	}

	public String getClearStatus() {
		clearAllValues();
		return "";
	}

	public String clearAllValues() {
		log.debug("Calling clear ALL");
		clearIdentityValues();
		clearCollegeProfileValues();
		clearProgramProfileValues();
		clearPersonalProfileValues();
		clearEmailAddressValues();
		clearPhoneValues();
		clearReportValues();
		clearRoleValues();
		clearAddressValues();
		return null;
	}
	
	/********************************************* **********************************************/
	public String getEnableBypassOldPasswordCheck() {
		this.bypassOldPasswordCheck = true;
		return "";
	}

	public String getDisableBypassOldPasswordCheck() {
		this.bypassOldPasswordCheck = false;
		return "";
	}

	public String addAddressToUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (!this.getAccountManagementBean().addAddressToUserAccount(
					this.getConfigurationManager().getSelectedUser(),
					this.getStreet1(), this.getStreet2(), this.getCity(),
					this.getState(), this.getZip(), this.getCountry(),
					this.getSelectedAddressType())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot add Address information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Address information added successfully.");
				context.addMessage("msg", message);
				this.setLoadAddress(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	
	public String changePassword() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		String md5Password = null;
		if (oldPassword == null || oldPassword.isEmpty()) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Old Password cannot be empty.");
			context.addMessage("msg", message);
		}
		if ((!bypassOldPasswordCheck)
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			md5Password = CEPISPGPUtil.getMD5Hash(oldPassword);
			if (!md5Password.equals(this.getConfigurationManager()
					.getSelectedUser().getPassword())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Old Password is invalid. Please try again.");
				context.addMessage("msg", message);
				return null;
			}
		}
		if (password == null || password.isEmpty()) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] New Password cannot be empty.");
			context.addMessage("msg", message);
			return null;
		}
		if (verifyPassword == null || verifyPassword.isEmpty()) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Verfiy Password cannot be empty.");
			context.addMessage("msg", message);
			return null;
		}

		if (!this.verifyPasswords()) {
			log.debug("Passwords does not match");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Password and Verify Password fields doesn't match. Please try again. ");
			context.addMessage("msg", message);
			this.setPassword(null);
			this.setVerifyPassword(null);
			return null;
		}
		User user = this.getConfigurationManager().getSelectedUser();
		if (user == null || user.getUid() == null || user.getUid().isEmpty()) {
			log.debug("Invalid User object ..");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot access the user data. Please try again later. ");
			context.addMessage("msg", message);
			this.setPassword(null);
			this.setVerifyPassword(null);
			return null;
		}
		md5Password = CEPISPGPUtil.getMD5Hash(password);
		this.getAccountManagementBean().updatePassword(user, md5Password);
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		message.setSummary("[" + new Date()
				+ "] Password updated successfully. ");
		context.addMessage("msg", message);
		this.setOldPassword(null);
		this.setPassword(null);
		this.setVerifyPassword(null);
		return null;
	}

	public String addEmailAddressToUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (!this.getAccountManagementBean().addEmailAddressToUserAccount(
					this.getConfigurationManager().getSelectedUser(),
					this.getAddress(), this.getSelectedEmailAddressType())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot add Email Address information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Email Address added successfully.");
				context.addMessage("msg", message);
				this.setLoadEmail(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	public String addPhoneToUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (!this.getAccountManagementBean().addPhoneToUserAccount(
					this.getConfigurationManager().getSelectedUser(),
					this.getPhoneNo(), this.getExt(), this.getShortDesc(),
					this.getSelectedPhoneType())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot add Phone information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Phone information added successfully.");
				context.addMessage("msg", message);
				this.setLoadPhone(true);
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	public String addReportUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		log.debug("Calling Add Report User");
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getAccountManagementBean().createReportUser(
					this.getConfigurationManager().getSelectedUser(), address,
					0) == null) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot add Report user information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Report user information added successfully.");
				context.addMessage("msg", message);
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;

	}

	public String getUserData() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			this.setUid(this.getConfigurationManager().getSelectedUser()
					.getUid());
			this.setUkID(this.getConfigurationManager().getSelectedUser()
					.getUkID());
			this.setSsn(this.getConfigurationManager().getSelectedUser()
					.getSsn());
			this.setUsername(this.getConfigurationManager().getSelectedUser()
					.getUsername());
			this.setlName(this.getConfigurationManager().getSelectedUser()
					.getlName());
			this.setlName1(this.getConfigurationManager().getSelectedUser()
					.getlName1());
			this.setlName2(this.getConfigurationManager().getSelectedUser()
					.getlName2());
			this.setlName3(this.getConfigurationManager().getSelectedUser()
					.getlName3());
			this.setlName4(this.getConfigurationManager().getSelectedUser()
					.getlName4());
			this.setPreferredName(this.getConfigurationManager()
					.getSelectedUser().getPreferredName());
			this.setmName(this.getConfigurationManager().getSelectedUser()
					.getmName());
			this.setfName(this.getConfigurationManager().getSelectedUser()
					.getfName());
			this.setMaidenName(this.getConfigurationManager().getSelectedUser()
					.getMaidenName());
			this.setFullName(this.getConfigurationManager().getSelectedUser()
					.getFullName());
			this.setSuffix(this.getConfigurationManager().getSelectedUser()
					.getSuffix());
			this.setGender(this.getConfigurationManager().getSelectedUser()
					.getGender());
			this.setTitle(this.getConfigurationManager().getSelectedUser()
					.getTitle());
			this.setdOB(this.getConfigurationManager().getSelectedUser()
					.getdOB());
			this.setBadgeHolder(this.getConfigurationManager()
					.getSelectedUser().getBadgeHolder());

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return "";
	}

	public String addRogueUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getAccountManagementBean().checkDuplicacy(this.username,
				this.ukID)) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date() + "] Duplicate Entry: User "
					+ this.lName + ", " + this.mName
					+ " already exists in our portal. ");
			context.addMessage("msg", message);
			return null;
		}
		User user = this.getAccountManagementBean().addUser(uid, ukID, ssn,
				username, lName, lName1, lName2, lName3, lName4, preferredName,
				mName, fName, maidenName, fullName, suffix, gender, title, dOB,
				badgeHolder);
		

		if (user == null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Unknown error: Cannot add user information.");
			context.addMessage("msg", message);
		} else {
			user.setEnabled(1);
			List<User> newUser = new ArrayList<User>(0);
			newUser.add(user);
			this.getAccountManagementBean().addUsersToWorkingSet(
					this.getConfigurationManager().getSelectedWorkingSet(),
					newUser);
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date() + "] User " + lName + ", "
					+ fName + " " + mName + " added successfully.");
			context.addMessage("msg", message);
		}
		return null;
	}

	public String editUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {

			this.getConfigurationManager().setSelectedUser(
					this.getAccountManagementBean().updateUser(
							this.getConfigurationManager().getSelectedUser(),
							uid, ukID, ssn, username, lName, lName1, lName2,
							lName3, lName4, preferredName, mName, fName,
							maidenName, fullName, suffix, gender, title, dOB,
							badgeHolder));

			if (this.getConfigurationManager().getSelectedUser() == null) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot update user information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date() + "] User " + lName + ", "
						+ fName + " " + mName + " updated successfully.");
				context.addMessage("msg", message);
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}

		return null;
	}

	public String deactivateUserAccount() {
		log.debug("Calling deactivateUserAccount function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getConfigurationManager().getSelectedUser()
					.equals(this.getUserManager().getUser())) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("["
						+ new Date()
						+ "] For security reason, you cannot deactivate your own account. You can ask your colleague with"
						+ " sufficient permission to deactivate your account.");
				context.addMessage("msg", message);
				return null;
			}
			if (this.getAccountManagementBean().disableUser(
					this.getConfigurationManager().getSelectedUser())) {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("["
						+ new Date()
						+ "] User ("
						+ this.getConfigurationManager().getSelectedUser()
								.getFullName()
						+ ") Account deactivated successfully.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot deactivate the selected user. Please try again later..");
				context.addMessage("msg", message);
				this.getConfigurationManager().setSelectedUser(
						this.getUserManager().getUser());
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No User selected.");
			context.addMessage("msg", message);

		}
		return null;
	}

	public String activateUserAccount() {
		log.debug("Calling activateUserAccount function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getConfigurationManager().getSelectedUser()
					.equals(this.getUserManager().getUser())) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("["
						+ new Date()
						+ "] For security reason, you cannot activate your own account. You can ask your colleague with"
						+ " sufficient permission to activate your account.");
				context.addMessage("msg", message);
				return null;
			}
			if (this.getAccountManagementBean().enableUser(
					this.getConfigurationManager().getSelectedUser())) {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("["
						+ new Date()
						+ "] User ("
						+ this.getConfigurationManager().getSelectedUser()
								.getFullName()
						+ ") Account activated successfully.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot activate the selected user. Please try again later..");
				context.addMessage("msg", message);
				this.getConfigurationManager().setSelectedUser(
						this.getUserManager().getUser());
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No User selected.");
			context.addMessage("msg", message);

		}
		return null;
	}

	public String removeUserAccount() {

		log.debug("Calling Remove User Account function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getConfigurationManager().getSelectedUser()
					.equals(this.getUserManager().getUser())) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("["
						+ new Date()
						+ "] For security reason, you cannot delete your own account. You can ask your colleague with"
						+ " sufficient permission to remove your account.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean().removeUser(
					this.getConfigurationManager().getSelectedUser())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot remove this account.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] User Account removed successfully.");
				context.addMessage("msg", message);
				this.getConfigurationManager().setSelectedUser(
						this.getUserManager().getUser());
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No User selected.");
			context.addMessage("msg", message);
			return null;
		}

		return null;
	}

	public String addRolesToUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getSelectedRoles().size() < 1) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No roles selected.");
			context.addMessage("msg", message);
			return null;
		}
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {

			boolean result = false;
			if (!this.isSelectCustomRoleWorkingSet()) {
				result = this.getAccountManagementBean().addRolesToUserAccount(
						this.getConfigurationManager().getSelectedUser(),
						getSelectedRoles());
			} else {
				if (this.getSelectedWorkingSet() == null) {
					message.setSeverity(FacesMessage.SEVERITY_WARN);
					message.setSummary("[" + new Date()
							+ "] No workingset selected.");
					context.addMessage("msg", message);
					return null;
				}
				
				result = this.getAccountManagementBean().addRolesToUserAccount(
					this.getConfigurationManager().getSelectedUser(),
					getSelectedRoles(), this.getSelectedWorkingSet());
			}

			if (!result) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot add role information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Role(s) added successfully.");
				context.addMessage("msg", message);
				this.setLoadRole(true);
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		this.setSelectCustomRoleWorkingSet(false);
		return null;
	}

	public String addUserCollegeProfileToUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		log.debug("Calling User College Profile Create function");

		if (this.getAccountManagementBean().addUserCollegeProfileToUserAccount(
				this.getConfigurationManager().getSelectedUser(),
				this.getSelectedUserType(),
				this.getSelectedUserClassification(),
				this.getSelectedUkCollege(), this.getSelectedUkDepartment(),
				this.getCpSelectedUKStartTerm(),
				this.getCpSelectedUKEndTerm(), this.getCpSelectedUKStatus(),
				this.getSelectedCohortType(), this.getSelectedCohortTerm(),
				this.getCpShortDesc())) {
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User college profile added successfully.");
			context.addMessage("msg", message);
			this.setLoadCollegeProfile(true);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Unknown error: Cannot add User college profile.");
			context.addMessage("msg", message);
		}

		return null;
	}

	

	public String addUserProgramProfileToUser() {

		log.debug("Calling User Program Profile Create function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getAccountManagementBean().addUserProgramProfileToUserAccount(
				this.getConfigurationManager().getSelectedUser(),
				this.getSelectedProgram(), 
				this.getSelectedProgramDomain(),
				this.getSelectedProgramTrack(), 
				this.getSelectedProgramGroup(),
				this.getSelectedAdvisor(), 
				this.getSelectedFacultyAdvisor(),
				this.getSelectedUKMajor(),
				this.getPpSelectedUKStatus(), 
				this.getPpSelectedUKStartTerm(),
				this.getSelectedUKAnticipatedCompleteTerm(),
				this.getSelectedUKCompleteTerm(), 
				this.getFinalGPA(),
				this.getDegreeAwarded(), 
				this.isHiddenToStudent(),
				this.isRecommendCertification(), this.getPpShortDesc())) {
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User program profile added successfully.");
			context.addMessage("msg", message);
			this.setLoadProgramProfile(true);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Unknown error: Cannot add User program profile.");
			context.addMessage("msg", message);

		}

		return null;
	}

	public String editAddressDetail() {
		log.debug("Calling Edit Address Information"
				+ this.getSelectedAddressType().getDesc());
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedAddress() == null
					|| this.getSelectedAddress().getId() < 1) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No Address information selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (this.getSelectedAddressType() == null
					|| this.getSelectedAddressType().getCode() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No Addresstype selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean().updateAddressDetails(
					this.getConfigurationManager().getSelectedUser(),
					this.getSelectedAddress(), this.getStreet1(),
					this.getStreet2(), this.getCity(), this.getState(),
					this.getZip(), this.getCountry(),
					this.getSelectedAddressType())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot update Address information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Address information updated successfully.");
				context.addMessage("msg", message);
				this.setLoadAddress(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	
	public String editEmailAddressDetail() {
		log.debug("Calling Edit Email Address Information"
				+ this.getSelectedEmailAddress());
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedEmailAddress() == null
					|| this.getSelectedEmailAddress().getId() < 1) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No Email Address selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (this.getSelectedEmailAddressType() == null
					|| this.getSelectedEmailAddressType().getCode() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No Email Address Type selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean().updateEmailAddressDetails(
					this.getConfigurationManager().getSelectedUser(),
					this.getSelectedEmailAddress(), this.getAddress(),
					this.getSelectedEmailAddressType())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot edit Email Address information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Email Address updated successfully.");
				context.addMessage("msg", message);
				this.setLoadEmail(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	public String editPhoneDetail() {
		log.debug("Calling Edit Phone Information" + this.getSelectedPhoneType().getDesc());
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
		&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedPhone() == null
			|| this.getSelectedPhone().getId() < 1) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()	+ "] No Phone information selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (this.getSelectedPhoneType() == null
			|| this.getSelectedPhoneType().getCode() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()	+ "] No phone type selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean().updatePhoneDetails(
			this.getConfigurationManager().getSelectedUser(),
			this.getSelectedPhone(), this.getPhoneNo(), this.getExt(),
			this.getShortDesc(), this.getSelectedPhoneType())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()	+ "] Unknown error: Cannot edit Phone information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()	+ "] Phone information updated successfully.");
				context.addMessage("msg", message);
				this.setLoadPhone(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	public String editUserCollegeProfile() {

		log.debug("Calling User College Profile edit function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getSelectedUserCollegeProfile() == null
				|| this.getSelectedUserCollegeProfile()
						.getUsercollegeprofileid() < 1) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Error: Invalid College Profile ID. Cannot edit User college profile information.");
			context.addMessage("msg", message);
		}
		if (this.getAccountManagementBean().updateUserCollegeProfile(
				this.getConfigurationManager().getSelectedUser(),
				this.getSelectedUserCollegeProfile(),
				this.getSelectedUserType(),
				this.getSelectedUserClassification(),
				this.getSelectedUkCollege(), this.getSelectedUkDepartment(),
				this.getCpSelectedUKStartTerm(),
				this.getCpSelectedUKEndTerm(), this.getCpSelectedUKStatus(),
				this.getSelectedCohortType(), this.getSelectedCohortTerm(),
				this.getCpShortDesc()) == null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot edit User college profile information.");
			context.addMessage("msg", message);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User college profile updated successfully.");
			context.addMessage("msg", message);
			this.setLoadCollegeProfile(true);
		}

		return null;
	}

	

	public String updateUserConfiguration() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		log.debug("Calling User configuration edit function");
		UserConfiguration userConfiguration = this.getAccountManagementBean()
				.updateUserConfiguration(
						this.getConfigurationManager().getSelectedUser(),
						this.primaryEmailAddress, this.primaryPhone);
		if (userConfiguration == null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot save user primary contact information.");
			context.addMessage("msg", message);
		} else {
			this.setSelectedEmailAddress(userConfiguration.getPrimaryEmail());
			this.setSelectedPhone(userConfiguration.getPrimaryPhone());
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] Primary contact information saved successfully.");
			context.addMessage("msg", message);
		}
		return null;
	}
	
	public String getPrepProgramProfileTab(){
		this.setSelectedUserProgramProfile(null);
		this.setSelectedProgram(null);
		this.setSelectedProgramDomain(null);
		this.setSelectedProgramTrack(null);
		this.setSelectedProgramGroup(null);
		this.setSelectedAdvisor(null);
		this.setSelectedFacultyAdvisor(null);
		this.setSelectedUKMajor(null);
		this.setPpSelectedUKStartTerm(null);
		this.setPpSelectedUKStatus(null);
		this.setSelectedUKAnticipatedCompleteTerm(null);
		this.setSelectedUKCompleteTerm(null);
		this.setFinalGPA(null);
		this.setDegreeAwarded(null);
		this.setHiddenToStudent(false);
		this.setRecommendCertification(false);
		this.setPpShortDesc(null);
		return "";
	}

	public String editUserProgramProfile() {

		log.debug("Calling User Program Profile edit function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		
		if(getPpSelectedUKStatus().getStatusCode().equals("ACTIVE")){
			this.setShowProgramProfileCompleteOptions(false);
			this.setSelectedUKCompleteTerm(null);
		}
		
		if (this.getAccountManagementBean().updateUserProgramProfile(
				this.getConfigurationManager().getSelectedUser(),
				this.getSelectedUserProgramProfile(),
				this.getSelectedProgram(), 
				this.getSelectedProgramDomain(),
				this.getSelectedProgramTrack(), 
				this.getSelectedProgramGroup(),
				this.getSelectedAdvisor(), 
				this.getSelectedFacultyAdvisor(),
				this.getSelectedUKMajor(),
				this.getPpSelectedUKStatus(), 
				this.getPpSelectedUKStartTerm(),
				this.getSelectedUKAnticipatedCompleteTerm(),
				this.getSelectedUKCompleteTerm(), 
				this.getFinalGPA(),
				this.getDegreeAwarded(), 
				this.isHiddenToStudent(),
				this.isRecommendCertification(), 
				this.getPpShortDesc()) == null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot edit User program profile information.");
			context.addMessage("msg", message);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User program profile updated successfully.");
			context.addMessage("msg", message);
			this.setLoadProgramProfile(true);
		}

		return null;
	}
	
	public String removeUserProgramProfileFromUser() {
		System.out
				.println("Calling Remove ProgramProfile information function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedUserProgramProfile() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No User program profile selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean()
					.removeUserProgramProfileFromUserAccount(
							this.getConfigurationManager().getSelectedUser(),
							this.getSelectedUserProgramProfile())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot remove User program profile information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] User program profile removed successfully.");
				context.addMessage("msg", message);
				this.setLoadProgramProfile(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date()
					+ "] No User program profile selected.");
			context.addMessage("msg", message);
		}
		return null;
	}
	
	public String removeAddressFromUser() {
		log.debug("Calling Remove address information function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedAddress() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No Address information selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean().removeAddressFromUserAccount(
					this.getConfigurationManager().getSelectedUser(),
					this.getSelectedAddress())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot remove Address information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Address information removed successfully.");
				context.addMessage("msg", message);
				this.setLoadAddress(true);
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	
	public String removeEmailAddressFromUser() {
		log.debug("Calling Remove email address information function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedEmailAddress() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No Email Address information selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean()
					.removeEmailAddressFromUserAccount(
							this.getConfigurationManager().getSelectedUser(),
							this.getSelectedEmailAddress())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot remove Email Address information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Email Address removed successfully.");
				context.addMessage("msg", message);
				this.setLoadEmail(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}

	public String removePhoneFromUser() {
		log.debug("Calling Remove phone information function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedPhone() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No Phone information selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean().removePhoneFromUserAccount(
					this.getConfigurationManager().getSelectedUser(),
					this.getSelectedPhone())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot remove Phone information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Phone information removed successfully.");
				context.addMessage("msg", message);
				this.setLoadPhone(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user information.");
			context.addMessage("msg", message);
		}
		return null;
	}

	public String removeUserCollegeProfileFromUser() {
		System.out
				.println("Calling Remove CollegeProfile information function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (this.getSelectedUserCollegeProfile() == null) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] No User college profile selected.");
				context.addMessage("msg", message);
				return null;
			}
			if (!this.getAccountManagementBean()
					.removeUserCollegeProfileFromUserAccount(
							this.getConfigurationManager().getSelectedUser(),
							this.getSelectedUserCollegeProfile())) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("["
						+ new Date()
						+ "] Unknown error: Cannot remove User college profile information.");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] User college profile removed successfully.");
				context.addMessage("msg", message);
				this.setLoadCollegeProfile(true);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No User selected.");
			context.addMessage("msg", message);
		}
		return null;
	}


	public String revokeRolesFromUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getSelectedRoles().size() < 1) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No Role(s) selected.");
			context.addMessage("msg", message);
			return null;
		}
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			if (!this.getAccountManagementBean().revokeRolesFromUserAccount(
					this.getConfigurationManager().getSelectedUser(),
					this.getSelectedRoles())) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("[" + new Date()
						+ "] Unknown error: Cannot revoke Role(s).");
				context.addMessage("msg", message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("[" + new Date()
						+ "] Role(s) revoked successfully.");
				context.addMessage("msg", message);
				setUserRoles(this.getAccountManagementBean().getUserRoleList(
						this.getConfigurationManager().getSelectedUser()));
				this.getSelectedRoles().clear();
				this.setLoadRole(true);
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
		}
		return null;
	}
	
	public void updateSelectedUser(ActionEvent event) {
		log.debug("Calling update selected user");
		
		selectedCourses.clear();
		clearAllValues();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void valueChangeAccessLDAPInfo(ActionEvent event)
			throws NamingException {
		log.debug("Calling valueChangeAccessLDAPInfo ...");
		String temp = null;
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();

		if (this.getUsername() != null || !this.getUsername().equals("")) {
			log.debug("Fetching LDAP info for " + this.getUsername());
			// Fetch information from uky ldap server
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://128.163.16.252:389/o=uky");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, "ou=users,o=uky");
			DirContext ctx = null;
			NamingEnumeration results = null;
			SearchResult searchResult = null;
			Attributes attrs = null;
			Attribute attr = null;
			try {
				ctx = new InitialDirContext(env);
				SearchControls controls = new SearchControls();
				controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				results = ctx
						.search("", "(cn=" + this.username + ")", controls);
				this.setShowProgramProfile(false);
				if (results.hasMore()) {
					log.debug("Fetching information");
					searchResult = (SearchResult) results.next();
					attrs = searchResult.getAttributes();
					// UKID

					attr = attrs.get("workforceID");
					if (attr != null) {
						temp = attr.get(0).toString();
					}
					if (temp == null || temp.equals("")) {
						log.debug("UKID is null");
						return;
					} else {
						log.debug("UKID: " + temp + "username:" + this.username);
					}

					if (this.getAccountManagementBean().checkDuplicacy(
							this.username, temp)) {
						System.out
								.println("Duplicate username" + this.username);
						message.setSeverity(FacesMessage.SEVERITY_ERROR);
						message.setSummary("["
								+ new Date()
								+ "] Duplicate Entry: User with LinkBlue username "
								+ this.username + " and UK ID " + temp
								+ " already exists in our portal. ");
						context.addMessage("msg", message);
						this.clearAllValues();
						return;
					} else {
						this.clearAllValues();
						this.setUkID(temp);
					}
					log.debug("Proceeding other info");

					// First Name
					attr = attrs.get("givenname");
					if (attr != null) {
						this.setfName(attr.get(0).toString());
						this.setPreferredName(attr.get(0).toString());
					}
					// Middle Name
					attr = attrs.get("initials");
					if (attr != null) {
						this.setmName(attr.get(0).toString());
					}
					// Last name
					attr = attrs.get("sn");
					if (attr != null) {
						this.setlName(attr.get(0).toString());
					}
					// User Type
					attr = attrs.get("generationalQualifier");
					if (attr != null) {
						String gq = attr.get(0).toString();
						String code = "";
						if (gq.equalsIgnoreCase("S")) {
							code = "STU";
							this.setShowProgramProfile(true);
						} else if (gq.equalsIgnoreCase("ST")) {
							code = "STA";
						} else if (gq.equalsIgnoreCase("F")) {
							code = "FAC";
						}

						UserType userType = this
								.getCepisGlobalComponentConfigurator()
								.findUserTypeByCode(code);
						if (userType != null) {
							this.setSelectedUserType(userType);
						}
					}
					// Username
					attr = attrs.get("userid");
					if (attr != null) {
						this.setUsername(attr.get(0).toString());
					}

					// Primary email
					attr = attrs.get("mail");
					if (attr != null) {
						this.setAddress(attr.get(0).toString());
					}

					// Telephone No
					attr = attrs.get("telephonenumber");
					if (attr != null) {
						this.setPhoneNo(attr.get(0).toString());
					}
				}
			} finally {
				if (ctx != null) {
					ctx.close();
				}
			}
		}
	}

	public void valueChangeCheckDuplicateUsername(ActionEvent event) {
		log.debug("Calling valueChangeCheckDuplicateUsername function ...");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getAccountManagementBean().checkDuplicacy(this.username)) {
			log.debug("Duplicate username" + this.username);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Duplicate Entry: User with same username "
					+ this.username + " already exists in our portal. ");
			context.addMessage("msg", message);
		}
	}

	private boolean verifyPasswords() {
		return (password.equals(verifyPassword) ? true : false);
	}

	public void valueChangeVerifyPassword(ActionEvent event) {
		log.debug("Calling valueChangeVerifyPassword function ...");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();

		if (password == null || password.isEmpty() || verifyPassword == null
				|| verifyPassword.isEmpty()) {
			log.debug("Password invalid or null");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Either the password field is empty or you have entered an invalid password. ");
			context.addMessage("msg", message);
			this.setPassword(null);
			this.setVerifyPassword(null);
		}
		if (!this.verifyPasswords()) {
			log.debug("Passwords does not match");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Password and Verify Password fields doesn't match. Please try again. ");
			context.addMessage("msg", message);
			this.setPassword(null);
			this.setVerifyPassword(null);
		}

	}

	public void valueChangeNonUKUserOptions(ActionEvent event) {
		log.debug("Calling valueChangeNonUKUserOptions function ...");
		if (this.isEnableNonUKUserOptions()) {
			this.setUkID("00000000");
			this.setUsername(null);
			this.setPassword(null);
			this.setVerifyPassword(null);
			this.setShowRole(true);
			this.setSelectedUserType(this.getAccountManagementBean()
					.findUserTypeByCode("NUK"));
			this.setSelectedUserClassification(this.getAccountManagementBean()
					.findUserClassificationByCode("OTHER"));
			this.setSelectedUkCollege(this.getAccountManagementBean()
					.findUKCollegeByCode("NC"));
			this.setSelectedUkDepartment(this.getAccountManagementBean()
					.findUKDepartmenteByCode("NAD"));
		} else {
			this.setUkID(null);
			this.setPassword(null);
			this.setVerifyPassword(null);
			this.setShowRole(false);
			this.setSelectedUserType(null);
			this.setSelectedUserClassification(null);
			this.setSelectedUkCollege(null);
			this.setSelectedUkDepartment(null);
		}

	}

	public void valueChangeAddProgramProfileView(ActionEvent event) {
		log.debug("Calling valueChangeAddProgramProfileView");
		this.showProgramProfile = false;
		if (this.getSelectedUserType() != null
				&& this.getSelectedUserType().getShortDesc()
						.equalsIgnoreCase("student")) {
			this.showProgramProfile = true;
		}
		if (this.getCpSelectedUKStatus() != null
				&& this.getCpSelectedUKStatus().getStatusCode()
						.equals("POTENTIAL")) {
			this.showProgramProfile = false;
		}

	}

	public void valueChangeAddRole(ActionEvent event) {
		log.debug("Calling valueChangeAddRole");
		this.setSelectCustomRoleWorkingSet(false);
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			Iterator<Role> iter = this.getSelectedRoles().iterator();
			Role customRole = null;
			while (iter.hasNext()) {
				customRole = iter.next();
				if (customRole != null && customRole.getRoleid() > 0) {
					if (customRole.getRolename().startsWith("ROLE_CUSTOM")) {
						this.setSelectCustomRoleWorkingSet(true);
					}
				}
			}
		}
	}

	public void valueChangefName(ActionEvent event) {
		this.setPreferredName(this.getfName());
	}

	public void valueChangeEditAddress(ActionEvent event) {
		log.debug("Calling valueChangeEditAddress");
		if (this.selectedAddress != null
				&& this.getSelectedAddress().getId() >= 0) {
			this.setStreet1(this.selectedAddress.getStreet1());
			this.setStreet2(this.selectedAddress.getStreet2());
			this.setCity(this.selectedAddress.getCity());
			this.setState(this.selectedAddress.getState());
			this.setZip(this.selectedAddress.getZip());
			this.setCountry(this.selectedAddress.getCountry());
			this.setSelectedAddressType(this.selectedAddress.getAddressType());
		}
	}

	public void valueChangeProgramDomain(ActionEvent event) {
		if (this.selectedProgramDomain != null) {
			this.getCepisGlobalComponentConfigurator().setActiveProgramDomain(
					selectedProgramDomain);
		}
	}

	public void valueChangeEditEmailAddress(ActionEvent event) {
		log.debug("Calling valueChangeEditEmailAddress");
		if (this.selectedEmailAddress != null
				&& this.getSelectedEmailAddress().getId() >= 0) {
			this.setAddress(this.getSelectedEmailAddress().getAddress());
			this.setSelectedEmailAddressType(this.selectedEmailAddress
					.getType());
			log.debug("Successful");
		}
	}

	public void valueChangeEditPhone(ActionEvent event) {
		log.debug("Calling valueChangeEditPhone");
		if (this.selectedPhone != null && this.getSelectedPhone().getId() >= 0) {
			this.setPhoneNo(this.selectedPhone.getPhoneNo());
			this.setExt(this.selectedPhone.getExt());
			this.setSelectedPhoneType(this.selectedPhone.getPhoneType());
			this.setShortDesc(this.selectedPhone.getMemo());
		}
	}

	public String valueChangeEditUserCollegeProfile() {
		
		log.debug("Calling valueChangeEditUserCollegeProfile");
		
		if (this.selectedUserCollegeProfile != null
		&& !(this.getSelectedUserCollegeProfile().getUsercollegeprofileid() < 0)) {
			
			this.setSelectedUserType(
					this.getSelectedUserCollegeProfile().getUserType());			
			this.setSelectedUserClassification(
					this.getSelectedUserCollegeProfile().getUserClassification());			
			this.setSelectedUkCollege(
					this.getSelectedUserCollegeProfile().getUkCollege());			
			this.setSelectedUkDepartment(
					this.getSelectedUserCollegeProfile().getUkDepartment());			
			this.setCpSelectedUKStatus(
					this.getSelectedUserCollegeProfile().getStatus());			
			this.setCpSelectedUKStartTerm(
					this.getSelectedUserCollegeProfile().getStartTerm());			
			this.setCpSelectedUKEndTerm(
					this.getSelectedUserCollegeProfile().getEndTerm());			
			this.setSelectedCohortTerm(
					this.getSelectedUserCollegeProfile().getCohortTerm());			
			this.setSelectedCohortType(
					this.getSelectedUserCollegeProfile().getCohortType());			
			this.setCpShortDesc(
					this.getSelectedUserCollegeProfile().getShortDesc());
		}
		return null;
	}

	public String valueChangeEditUserProgramProfile() {
		
		log.debug("Calling valueChangeEditUserProgramProfile");
		
		if (this.selectedUserProgramProfile != null
		&& this.selectedUserProgramProfile.getUserprogramprofileid() != null) {
			
			this.setSelectedProgram(
					this.getSelectedUserProgramProfile().getProgram());			
			this.setSelectedProgramDomain(
					this.getSelectedUserProgramProfile().getProgramDomain());			
			this.setSelectedProgramTrack(
					this.getSelectedUserProgramProfile().getProgramTrack());			
			this.setSelectedProgramGroup(
					this.getSelectedUserProgramProfile().getProgramGroup());			
			this.setSelectedUKMajor(
					this.getSelectedUserProgramProfile().getUkMajor());			
			this.setPpSelectedUKStatus(
					this.getSelectedUserProgramProfile().getUkStatus());			
			this.setSelectedUKCompleteTerm(
					this.getSelectedUserProgramProfile().getUkCompleteTerm());			
			this.setFinalGPA(
					this.getSelectedUserProgramProfile().getFinalGPA());			
			this.setPpSelectedUKStartTerm(
					this.getSelectedUserProgramProfile().getUkStartTerm());				
			this.setSelectedUKAnticipatedCompleteTerm(
					this.getSelectedUserProgramProfile().getUkAnticipatedCompleteTerm());			
			this.setSelectedAdvisor(
					this.getSelectedUserProgramProfile().getAdvisor());		
			this.setSelectedFacultyAdvisor(
					this.getSelectedUserProgramProfile().getFacultyAdvisor());
			this.setRecommendCertification(
					this.getSelectedUserProgramProfile().isRecommendCertification());			
			this.setDegreeAwarded(
					this.getSelectedUserProgramProfile().getDegreeAwarded());			
			this.setHiddenToStudent(
					this.getSelectedUserProgramProfile().isHiddenToStudent());
			this.setPpShortDesc(
					this.getSelectedUserProgramProfile().getShortDesc());
			
			this.setShowProgramProfileCompleteOptions(false);			

			if(this.getPpSelectedUKStatus() != null){
				if ( ! this.getPpSelectedUKStatus().getStatusCode().equals("ACTIVE")) {
					this.setShowProgramProfileCompleteOptions(true);
				}
			}
		}
		return null;
	}

	public void valueChangeUserProgramProfileStatus(ActionEvent event) {
		log.debug("Calling valueChangeUserProgramProfileStatus");
		this.setShowProgramProfileCompleteOptions(false);
		if (this.getPpSelectedUKStatus() != null
				&& !this.getPpSelectedUKStatus().getStatusCode()
						.equals("ACTIVE")) {
			log.debug("Show complete fields");
			this.setShowProgramProfileCompleteOptions(true);
		}
	}


	public String getAddress() {
		return address;
	}
	
	public List<Address> getAddressList() {
		System.out.print("Calling Get Address List: ");
		if (loadAddress
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setAddressList(this.getAccountManagementBean().getAddressList(
					this.getConfigurationManager().getSelectedUser()));
			loadAddress = false;
		} else {
			log.debug("skipped");
		}
		return this.addressList;
	}
	
	public void downloadPDF() throws IOException {
		int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
		// Prepare.
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext
				.getResponse();
		;
		// File file = new File(
		// "C:\\Workspace\\cepis\\src\\main\\webapp\\pages\\account\\academic-profile\\transcripsst.pdf");
		URL myUKTranscriptLink = null;
		URLConnection myUKTranscriptConnection = null;
		try {
			myUKTranscriptLink = new URL(
					"https://myuk.uky.edu/irj/servlet/prt/portal/prtroot/cmSA_Advising2.pdf?o=10633403");
		} catch (MalformedURLException mue) {
			System.err.println(mue);
		}
		// log.debug(myUKTranscriptLink.getFile());
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		Document document = null;

		try {
			myUKTranscriptConnection = myUKTranscriptLink.openConnection();
			input = new BufferedInputStream(
					myUKTranscriptConnection.getInputStream());
			output = new BufferedOutputStream(response.getOutputStream(),
					DEFAULT_BUFFER_SIZE);
			BufferedReader readerPDF = new BufferedReader(
					new InputStreamReader(
							myUKTranscriptConnection.getInputStream()));

			// Init servlet response.
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Length", String
					.valueOf(myUKTranscriptConnection.getContentLengthLong()));
			response.setHeader("Content-Disposition",
					"inline; filename=\"transcript.pdf\"");
//			if (false) {
//				// Open file.
//
//				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
//				int length;
//
//				// read from the file; write to the ServletOutputStream
//				while ((length = input.read(buffer)) > 0) {
//					output.write(buffer, 0, length);
//					log.debug(buffer);
//				}
//			} else {
				StringBuilder stringBuilder = new StringBuilder();

				String line = null;
				while ((line = readerPDF.readLine()) != null) {
					stringBuilder.append(line + "\n");
				}
				log.debug(stringBuilder);
				document = new Document();
				try {
					PdfReader reader = new PdfReader(myUKTranscriptLink);
					log.debug(reader.getNumberOfPages());
					PdfWriter writer = PdfWriter.getInstance(document, output);

					document.open();
					PdfImportedPage page = writer.getImportedPage(reader, 1);
					Image instance = Image.getInstance(page);
					document.add(instance);
					document.add(new Paragraph("Copyrighted material."));
					document.add(new Paragraph("Generated on "
							+ new Date().toString()));

				} catch (DocumentException e) {
					System.err.println(e);
				}

//			}
			// Finalize task.
			output.flush();
		} finally {
			// Gently close streams.
			close(output);
			close(input);
			if (document != null)
				document.close();
		}

		// Inform JSF that it doesn't need to handle response.
		// This is very important, otherwise you will get the following
		// exception in the logs:
		// java.lang.IllegalStateException: Cannot forward after response has
		// been committed.
		facesContext.responseComplete();
	}

	// Helpers (can be refactored to public utility class)
	// ----------------------------------------

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				// Do your thing with the exception. Print it, log it or mail
				// it. It may be useful to
				// know that this will generally only be thrown when the client
				// aborted the download.
				e.printStackTrace();
			}
		}
	}

	public String getMyUKTranscriptLink() {
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUkID() != null
				&& this.getConfigurationManager().getSelectedUser().getUkID().trim() != "") {
			return "https://myuk.uky.edu/irj/servlet/prt/portal/prtroot/cmSA_Advising2.pdf?o="
					+ this.getConfigurationManager().getSelectedUser()
							.getUkID();
		}
		return "/cepis/pages/account/academic-profile/no-transcript.html";
	}

	

	private Course findCourseByID(String key) {
		for (Course item : courses) {
			if (key.equals(item.getStuMasterNo() + item.getSourceID()
					+ item.getSourceCD() + item.getCdpMask() + item.getNo()
					+ item.getSection() + item.getTerm())) {
				return item;
			}
		}
		return null;
	}

	public void takeSelection() {
		selectedCourses.clear();
		Iterator<Object> iterator = getSelection().getKeys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Course course = findCourseByID(key);
			if (course != null) {
				selectedCourses.add(course);
			}

		}

	}

	public ExtendedTableDataModel<Course> getCoursesDataModel() {
		System.out.print("Calling GetCourseDataModel: ");
		if ((this.isLoadCourses()
				&& this.getConfigurationManager().getSelectedUser() != null && this
				.getConfigurationManager().getSelectedUser().getUid() != null)
				|| courseDataModel == null) {
			dataTable.setGroupingColumn("col_2");
			Collection<Object> sortPriority = new ArrayList<Object>(1);
			sortPriority.add("col_2");
			dataTable.setSortPriority(sortPriority);
			courses = this.getAccountManagementBean().getAcademicProfile(
					this.getConfigurationManager().getSelectedUser());
			courseDataModel = new ExtendedTableDataModel<Course>(
					new DataProvider<Course>() {
						private static final long serialVersionUID = 5054087821033164847L;

						public Course getItemByKey(Object key) {
							for (Course c : courses) {
								if (key.equals(getKey(c))) {
									return c;
								}
							}
							return null;
						}

						public List<Course> getItemsByRange(int firstRow,
								int endRow) {
							return courses.subList(firstRow, endRow);
						}

						public Object getKey(Course item) {
							return item.getStuMasterNo() + item.getSourceID()
									+ item.getSourceCD() + item.getCdpMask()
									+ item.getNo() + item.getSection()
									+ item.getTerm();
						}

						public int getRowCount() {
							return courses.size();
						}
					});
			courseDataModel.setRowIndex(-1);
			this.setLoadCourses(false);
		} else {
			log.debug("skipped");
		}
		return courseDataModel;

	}

	public ExtendedTableDataModel<HoldLift> getHoldLiftsDataModel() {
		System.out.print("Calling GetHoldLiftDataModel: ");
		if ((this.isLoadHoldLifts()
				&& this.getConfigurationManager().getSelectedUser() != null && this
				.getConfigurationManager().getSelectedUser().getUid() != null)
				|| holdLiftDataModel == null) {
			log.debug("loaded");
			holdLiftDataTable.setGroupingColumn("hold_col2");
			Collection<Object> sortPriority = new ArrayList<Object>(1);
			sortPriority.add("hold_col2");
			holdLiftDataTable.setSortPriority(sortPriority);
			holdLifts = this.getAccountManagementBean().getHoldLiftsByStudent(
					this.getConfigurationManager().getSelectedUser());
			holdLiftDataModel = new ExtendedTableDataModel<HoldLift>(
					new DataProvider<HoldLift>() {
						private static final long serialVersionUID = 5054087821033164847L;

						public HoldLift getItemByKey(Object key) {
							for (HoldLift c : holdLifts) {
								if (key.equals(getKey(c))) {
									return c;
								}
							}
							return null;
						}

						public List<HoldLift> getItemsByRange(int firstRow,
								int endRow) {
							return holdLifts.subList(firstRow, endRow);
						}

						public Object getKey(HoldLift item) {
							return item.getId() + "";
						}

						public int getRowCount() {
							return holdLifts.size();
						}
					});

			holdLiftDataModel.setRowIndex(-1);
			this.setLoadHoldLifts(false);
		} else {
			log.debug("skipped");
		}
		return holdLiftDataModel;

	}
	
	public void valueChangeStandardScoreMetrics(ActionEvent event) {
		log.debug("Calling valueChangeStandardSetFinalScoreMetrics");
		FacesContext context = FacesContext.getCurrentInstance();

		if (this.getUserAssessmentProfiles().size() > 0) {
			Map<String, String> reqMap = context.getExternalContext()
					.getRequestParameterMap();
			Object value = reqMap.get("assessmentKey");
			if (value != null) {
				activeAssessmentProfileKey = Integer.parseInt(value + "");
			}
			value = reqMap.get("stdSetKey");
			if (value != null) {
				activeStdSetKey = Integer.parseInt(value + "");
			}
			value = reqMap.get("stdKey");
			if (value != null) {
				activeStandardKey = Integer.parseInt(value + "");
			}

			HashSet<Integer> keys = new HashSet<Integer>();
			log.debug("Standard Rows: " + activeStandardKey);
			keys.add(activeStandardKey);
			this.setStandardKeys(keys);

			keys = new HashSet<Integer>();
			log.debug("StdSet Rows: " + activeStdSetKey);
			keys.add(activeStdSetKey);
			this.setStdSetKeys(keys);
			keys = new HashSet<Integer>();
			System.out
					.println("Assessment Rows: " + activeAssessmentProfileKey);
			keys.add(activeAssessmentProfileKey);
			this.setAssessmentKeys(keys);
			UserAssessmentProfile assessmentProfile = this
					.getUserAssessmentProfiles()
					.get(activeAssessmentProfileKey);
			if (assessmentProfile != null) {
				UserAssessmentStandardSet userStdSet = assessmentProfile
						.getStandardSets().get(activeStdSetKey);

				if (userStdSet != null) {
					log.debug(userStdSet + " (CScore:"
							+ userStdSet.getComputedScore() + "-"
							+ userStdSet.getFinalScore() + ")");
					userStdSet.setFinalScore((int) userStdSet
							.getComputedScore());
				} else {
					System.err.println("UserAssessmentStandardSet is null");
				}
			} else {
				System.err.println("UserAssessmentProfile is null");
			}

		}
	}

	public void valueChangeStandardSetFinalScoreMetrics(ActionEvent event) {
		log.debug("Calling valueChangeStandardSetFinalScoreMetrics");
		FacesContext context = FacesContext.getCurrentInstance();

		if (this.getUserAssessmentProfiles().size() > 0) {
			Map<String, String> reqMap = context.getExternalContext()
					.getRequestParameterMap();
			Object value = reqMap.get("assessmentKey");
			if (value != null) {
				activeAssessmentProfileKey = Integer.parseInt(value + "");
			}
			value = reqMap.get("stdSetKey");
			if (value != null) {
				activeStdSetKey = Integer.parseInt(value + "");
			}

			HashSet<Integer> keys = new HashSet<Integer>();
			log.debug("StdSet Rows: " + activeStdSetKey);
			keys.add(activeStdSetKey);
			this.setStdSetKeys(keys);
			keys = new HashSet<Integer>();
			System.out
					.println("Assessment Rows: " + activeAssessmentProfileKey);
			keys.add(activeAssessmentProfileKey);
			this.setAssessmentKeys(keys);
		}
	}

	/*
	 * public String ChangeStandardSetFinalScoreMetrics() {
	 * log.debug(" Calling ChangeStandardSetFinalScoreMetrics"); HashSet keys =
	 * new HashSet<Integer>(); int rowKey = 0; //
	 * this.getStdSetDataTable().getRowIndex();// //
	 * getStdSetRepeater().getRowIndex(); log.debug("Rows: " + rowKey);
	 * keys.add(rowKey); this.setStdSetKeys(keys); if
	 * (this.getUserAssessmentProfiles().size() > 0) { UserAssessmentProfile
	 * assessmentProfile = this.getUserAssessmentProfiles() .get(0);
	 * UserAssessmentStandardSet userStdSet = null;
	 * List<UserAssessmentStandardSet> userStdSets = assessmentProfile
	 * .getStandardSets();
	 * 
	 * if (rowKey < userStdSets.size()) { userStdSet = userStdSets.get(rowKey);
	 * log.debug(userStdSet + " (CScore:" + userStdSet.getComputedScore() + "-"
	 * + userStdSet.getFinalScore() + ")"); userStdSet.setFinalScore((int)
	 * userStdSet.getComputedScore()); }
	 * 
	 * } stdScoreComponent.processValidators(FacesContext.getCurrentInstance());
	 * stdScoreComponent.processUpdates(FacesContext.getCurrentInstance());
	 * return null; }
	 * 
	 * public String ChangeComputedScoreMetric() {
	 * 
	 * log.debug(" Calling ChangeComputedScoreMetric"); HashSet keys = new
	 * HashSet<Integer>(); int rowKey = 0;//
	 * getAssessmentRepeater().getRowIndex(); log.debug("Rows: " + rowKey);
	 * keys.add(rowKey); this.setAssessmentKeys(keys); int totalProfiles =
	 * this.getUserAssessmentProfiles().size(); int score = 0; int count = 0; if
	 * (totalProfiles > 0 && rowKey < totalProfiles) { UserAssessmentProfile
	 * assessmentProfile = this.getUserAssessmentProfiles() .get(rowKey);
	 * UserAssessmentStandardSet userStdSet = null;
	 * Iterator<UserAssessmentStandardSet> iter = assessmentProfile
	 * .getStandardSets().iterator();
	 * 
	 * while (iter.hasNext()) { userStdSet = iter.next(); if
	 * (userStdSet.getFinalScore() != 0) { count++; score +=
	 * userStdSet.getFinalScore(); } } if (count < 1) {
	 * assessmentProfile.setScore(0); } else { assessmentProfile.setScore((score
	 * / count)); }
	 * 
	 * } finalScoreComponent
	 * .processValidators(FacesContext.getCurrentInstance());
	 * finalScoreComponent.processUpdates(FacesContext.getCurrentInstance());
	 * return null; }
	 */

	public String addAllUserAssessmentProfiles() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		log.debug("Calling All User Assessment Profile Create function");
		log.debug(this.getUserAssessmentProfiles().size());
		if (this.getUserAssessmentProfiles().size() > 0) {
			if (this.getAccountManagementBean().addUserAssessmentProfiles(
					getUserAssessmentProfiles(),
					this.getSelectedProgramDomain(), this.getSelectedUKTerm(),
					this.getAssessmentType()) > 0) {
				this.getUserAssessmentProfiles().clear();
				if (this.getSelectedUsers().size() > 1) {
					this.setSelectedUsers(new ArrayList<User>(0));
				}
				this.setSelectedProgramDomain(null);
				this.setSelectedUKTerm(null);
				this.setAssessmentType(0);
				this.setShowAssessmentDataEntryPanel(false);
				this.setLoadUserAssessmentProfileList(true);
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("["
						+ new Date()
						+ "] All the User Assessment profiles were added successfully.");
				context.addMessage("msg", message);
			}
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot add one or more User Assessment profile(s).");
			context.addMessage("msg", message);
		}

		return null;
	}

	public String updateSelectedUserAssessmentProfile() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		log.debug("Calling update selected User Assessment Profile function");
		log.debug(this.getUserAssessmentProfiles().size());
		if (this.getUserAssessmentProfiles().size() > 0) {
			if (this.getAccountManagementBean().updateUserAssessmentProfile(
					getUserAssessmentProfiles().get(0))) {
				this.getUserAssessmentProfiles().clear();
				if (this.getSelectedUsers().size() > 1) {
					this.setSelectedUsers(new ArrayList<User>(0));
				}
				this.setSelectedProgramDomain(null);
				this.setSelectedUKTerm(null);
				this.setAssessmentType(0);
				this.setShowAssessmentDataEntryPanel(false);
				this.setLoadUserAssessmentProfileList(true);

				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("["
						+ new Date()
						+ "] All the User Assessment profiles were updated successfully.");
				context.addMessage("msg", message);
			}

		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot update one or more User Assessment profile(s). Please try again later.");
			context.addMessage("msg", message);
		}

		return null;
	}

	public String addThisUserAssessmentProfile() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		System.out
				.println("Calling this User Assessment Profile Create function");
		log.debug(this.getUserAssessmentProfiles().size());
		if (this.getUserAssessmentProfiles().size() > 0) {

			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User Assessment profile added successfully.");
			context.addMessage("msg", message);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot add this User Assessment profile.");
			context.addMessage("msg", message);
		}

		return null;
	}

	public String discardAllUserAssessmentProfiles() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		log.debug("Calling All User Assessment Profile discard function");
		log.debug(this.getUserAssessmentProfiles().size());
		if (this.getUserAssessmentProfiles().size() > 0) {
			this.getUserAssessmentProfiles().clear();
			this.setSelectedProgramDomain(null);
			this.setSelectedUKTerm(null);
			this.setAssessmentType(0);

			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User Assessment profiles discarded successfully.");
			context.addMessage("msg", message);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date()
					+ "] Error: No User Assessment profiles available.");
			context.addMessage("msg", message);
		}

		return null;
	}

	public String deleteSelectedUserAssessmentProfile() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		log.debug("Calling delete selected User Assessment Profile function");
		if (this.getSelectedUserAssessmentProfile() == null) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("["
					+ new Date()
					+ "] No User Assessment profile selected. Please try again.");
			context.addMessage("msg", message);
		}
		if (this.getAccountManagementBean().deleteUserAssessmentProfile(
				this.getSelectedUserAssessmentProfile())) {
			this.setLoadUserAssessmentProfileList(true);
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date() + "] "
					+ " User Assessment profile deleted successfully.");
			context.addMessage("msg", message);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot delete the selected User Assessment profile.");
			context.addMessage("msg", message);
		}

		return null;
	}

	public String removeThisUserAssessmentProfile() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		System.out
				.println("Calling this User Assessment Profile remove function");
		log.debug(this.getSelectedUserAssessmentProfile().getUser());
		if (this.getSelectedUserAssessmentProfile() != null
				&& this.getUserAssessmentProfiles().size() > 0) {
			log.debug("Total Size: " + this.getUserAssessmentProfiles().size());
			Iterator<UserAssessmentProfile> iter = this
					.getUserAssessmentProfiles().iterator();
			while (iter.hasNext()) {
				log.debug(iter.next().getUser());
			}
			this.getUserAssessmentProfiles().remove(
					this.getSelectedUserAssessmentProfile());
			log.debug("Total Size: " + this.getUserAssessmentProfiles().size());
			iter = this.getUserAssessmentProfiles().iterator();
			while (iter.hasNext()) {
				log.debug(iter.next().getUser());
			}
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("[" + new Date()
					+ "] User Assessment profile removed successfully.");
			context.addMessage("msg", message);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("["
					+ new Date()
					+ "] Unknown error: Cannot remove this User Assessment profile.");
			context.addMessage("msg", message);
		}

		return null;
	}


	public String addButtonInvoked() {
		log.debug("Calling addButtonInvoked function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getSelectedUsers().size() < 1) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date() + "] No user selected.");
			context.addMessage("msg", message);
			return null;
		}
		this.setShowAssessmentDataEntryPanel(true);
		// Add action
		this.setUserAction(0);
		this.setSelectedProgramDomain(null);
		this.setSelectedUKTerm(null);
		this.setAssessmentType(0);
		this.getUserAssessmentProfiles().clear();
		this.setSelectedUserAssessmentProfile(null);
		return null;
	}

	public String updateButtonInvoked() {
		log.debug("Calling updateButtonInvoked function");
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.getSelectedUserAssessmentProfile() == null) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("[" + new Date()
					+ "] No User Assessment profile selected.");
			context.addMessage("msg", message);
			return null;
		}
		this.setShowAssessmentDataEntryPanel(true);
		// Update action
		this.setUserAction(1);
		this.setSelectedProgramDomain(this.getSelectedUserAssessmentProfile()
				.getProgramDomain());
		this.setSelectedUKTerm(this.getSelectedUserAssessmentProfile()
				.getUkTerm());
		this.setAssessmentType(this.getSelectedUserAssessmentProfile()
				.getAssessmentType());

		// Add the selectedUserAssessmentProfile to the userAssessmentProfiles
		// list object to user same data entry for both add and update
		// operation.
		this.getUserAssessmentProfiles().clear();
		this.getUserAssessmentProfiles().add(
				this.getSelectedUserAssessmentProfile());
		return null;
	}

	public String addArtifacts() {
		log.debug("Calling addArtifacts function");

		if (this.getUserAssessmentProfiles().size() > 0) {
			this.getSelectedUserAssessmentProfile().getStandardSets().get(0)
					.getStandards().get(0).setScore(0);
			this.getUserAssessmentProfiles().clear();
			this.getUserAssessmentProfiles().add(
					this.getSelectedUserAssessmentProfile());
		}
		return null;
	}

	public List<SelectItem> getAllCompletedArtifacts() {
		List<SelectItem> allCompletedArtifacts = new ArrayList<SelectItem>(0);
		selectedUserAssessmentProfile = (UserAssessmentProfile) artifactText
				.getAttributes().get("currentAssessment");
		log.debug(selectedUserAssessmentProfile);
		if (selectedUserAssessmentProfile == null) {
			log.debug("Current Assessment is null");
		} else {
			List<Artifact> completedArtifacts = this.getAccountManagementBean()
					.getArtifactList(selectedUserAssessmentProfile.getUser());
			Iterator<Artifact> iter = completedArtifacts.iterator();
			Artifact artifact = null;
			while (iter.hasNext()) {
				artifact = iter.next();
				SelectItem sItem = new SelectItem(artifact,
						artifact.getAssignment() + " (" + artifact.getDate()
								+ ")");
				allCompletedArtifacts.add(sItem);
			}
		}
		return allCompletedArtifacts;
	}

	public List<Artifact> getAssessmentArtifacts() {
		selectedUserAssessmentProfile = (UserAssessmentProfile) artifactText
				.getAttributes().get("currentAssessment");
		log.debug(selectedUserAssessmentProfile);
		if (selectedUserAssessmentProfile == null || selectedUserAssessmentProfile.getId()<1) {
			log.debug("Current Assessment is null/not intialized");
		} else {
			return this.getAccountManagementBean().getArtifactList(
					selectedUserAssessmentProfile);
		}
		return assessmentArtifacts;
	}

	public void valueChangeSelectedUsers(ActionEvent event) {
		log.debug(" Calling valueChangeSelectedUsers");
		// Clear the new assessment profiles
		this.getUserAssessmentProfiles().clear();
		// Reset to defaull values
		this.setSelectedProgramDomain(null);
		this.setSelectedUKTerm(null);
		this.setAssessmentType(0);
		// Hide the Data Entry Section
		this.setShowAssessmentDataEntryPanel(false);
		// Set default action to Add
		this.setUserAction(0);
		this.setLoadUserAssessmentProfileList(true);
	}

	public void valueChangePrimaryAssessmentComponent(ActionEvent event) {
		log.debug("Calling valueChangePrimaryAssessmentComponent");
		long startTime = System.currentTimeMillis();
		if (this.selectedProgramDomain != null
				&& this.getSelectedUsers() != null
				&& this.getSelectedUsers().size() > 0) {
			// TODO: Need an improvement (takes approx: 60 - 300 ms for avg no.
			// of users): add/remove recent changes. Do not
			// start from scrach. Saves a couple of 10 ms.
			this.getUserAssessmentProfiles().clear();
			// Generate the User Assessment Profiles
			List<StandardSet> stdSets = this.getAccountManagementBean()
					.getStandardSets(this.getSelectedProgramDomain());
			UserAssessmentProfile assessmentProfile = null;
			User user = null;
			Iterator<StandardSet> iterStdSet = null;
			Iterator<Standard> iterStd = null;
			StandardSet stdSet = null;
			Standard std = null;
			UserAssessmentStandardSet userStandardSet = null;
			UserAssessmentStandard userStandard = null;
			log.debug(this.getSelectedUKTerm());
			if (stdSets.size() > 0) {
				Iterator<User> iterUser = this.getSelectedUsers().iterator();
				while (iterUser.hasNext()) {
					user = iterUser.next();
					assessmentProfile = new UserAssessmentProfile(
							this.getSelectedProgramDomain(),
							this.getSelectedUKTerm(), this.getAssessmentType(),
							0, 0, null, user);
					iterStdSet = stdSets.iterator();
					while (iterStdSet.hasNext()) {
						stdSet = iterStdSet.next();
						userStandardSet = new UserAssessmentStandardSet(stdSet,
								0);
						iterStd = stdSet.getStandards().iterator();
						while (iterStd.hasNext()) {
							std = iterStd.next();
							userStandard = new UserAssessmentStandard(std, 0);
							userStandardSet.getStandards().add(userStandard);
						}
						assessmentProfile.getStandardSets()
								.add(userStandardSet);
					}
					this.getUserAssessmentProfiles().add(assessmentProfile);
				}
			} else {
				this.getUserAssessmentProfiles().clear();
				log.warn("StdSet is empty for this progdomain.");
			}
		} else {
			this.getUserAssessmentProfiles().clear();
			log.warn("Either SelectedProgramDomain or SelectedUsers field is null/empty");
		}
		log.debug(System.currentTimeMillis() - startTime);

	}

	public String copyThisUserAssessmentProfile() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		System.out
				.println("Calling this User Assessment Profile copy function");

		this.setAssessmentKeys(new HashSet<Integer>(0));
		this.setStdSetKeys(new HashSet<Integer>(0));
		this.setStandardKeys(new HashSet<Integer>(0));

		if (this.getSelectedUserAssessmentProfile() != null
				&& this.getUserAssessmentProfiles().size() > 1) {

			UserAssessmentStandardSet userStandardSetFrom = null;
			UserAssessmentStandard userStandardFrom = null;
			UserAssessmentProfile assessmentProfile = null;
			List<UserAssessmentStandardSet> stdSets = this
					.getSelectedUserAssessmentProfile().getStandardSets();
			List<UserAssessmentStandard> standards = null;
			for (int i = 0; i < this.getUserAssessmentProfiles().size(); i++) {
				assessmentProfile = this.getUserAssessmentProfiles().get(i);
				assessmentProfile.setScore(this
						.getSelectedUserAssessmentProfile().getScore());
				assessmentProfile.setShortDesc(this
						.getSelectedUserAssessmentProfile().getShortDesc());
				this.assessmentKeys.add(new Integer(i));
				// deepCopy the standardsets' data
				for (int j = 0; j < stdSets.size(); j++) {
					userStandardSetFrom = stdSets.get(j);
					assessmentProfile.getStandardSets().get(j)
							.setFinalScore(userStandardSetFrom.getFinalScore());
					standards = userStandardSetFrom.getStandards();
					for (int k = 0; k < standards.size(); k++) {
						userStandardFrom = standards.get(k);
						assessmentProfile.getStandardSets().get(j)
								.getStandards().get(k)
								.setScore(userStandardFrom.getScore());
						this.standardKeys.add(new Integer(k));
					}
					this.stdSetKeys.add(new Integer(j));
				}

			}
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary("["
					+ new Date()
					+ "] User Assessment data copied to all other profiles successfully.");
			context.addMessage("msg", message);
		} else {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("[" + new Date()
					+ "] Unknown error: Cannot copy User Assessment data.");
			context.addMessage("msg", message);
		}

		return null;
	}

	public CEPISGlobalComponentConfigurator getCepisGlobalComponentConfigurator() {
		return cepisGlobalComponentConfigurator;
	}

	public String getCity() {
		return city;
	}
	
	public String getCountry() {
		return country;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public Date getIdentityCreatedOn() {
		return this.getConfigurationManager().getSelectedUser().getCreatedOn();
	}

	public Integer getDegreeAwarded() {
		return degreeAwarded;
	}

	public Date getdOB() {
		return this.dOB;
	}

	public Date getIdentityDOB() {
		return this.getConfigurationManager().getSelectedUser().getdOB();
	}

	public UKStatus getCpSelectedUKStatus() {
		return cpSelectedUKStatus;
	}

	public void setCpSelectedUKStatus(UKStatus cpSelectedUKStatus) {
		this.cpSelectedUKStatus = cpSelectedUKStatus;
	}
	
	public UKStatus getPpSelectedUKStatus() {
		return ppSelectedUKStatus;
	}
	
	public void setPpSelectedUKStatus(UKStatus ppSelectedUKStatus) {
		this.ppSelectedUKStatus = ppSelectedUKStatus;
	}

	public UKTerm getCpSelectedUKStartTerm() {
		return cpSelectedUKStartTerm;
	}

	public void setCpSelectedUKStartTerm(UKTerm cpSelectedUKStartTerm) {
		this.cpSelectedUKStartTerm = cpSelectedUKStartTerm;
	}

	public UKTerm getCpSelectedUKEndTerm() {
		return cpSelectedUKEndTerm;
	}
	
	public void setCpSelectedUKEndTerm(UKTerm cpSelectedUKEndTerm) {
		this.cpSelectedUKEndTerm = cpSelectedUKEndTerm;
	}

	public UKTerm getPpSelectedUKStartTerm() {
		return ppSelectedUKStartTerm;
	}

	public void setPpSelectedUKStartTerm(UKTerm ppSelectedUKStartTerm) {
		this.ppSelectedUKStartTerm = ppSelectedUKStartTerm;
	}

	public String getEmail() {
		return email;
	}

	public List<EmailAddress> getEmailAddressList() {
		System.out.print("Calling Get Email Address List: ");
		if (this.isLoadEmail()
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setEmailAddressList(this.getAccountManagementBean()
					.getEmailAddressList(
							this.getConfigurationManager().getSelectedUser()));
			this.setLoadEmail(false);
		} else {
			log.debug("skipped");
		}
		return this.emailAddressList;
	}

	public String getExt() {
		return ext;
	}

	public String getfName() {
		return this.fName;
	}

	public String getIdentityfName() {
		return this.getConfigurationManager().getSelectedUser().getfName();
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getIdentityFullName() {
		return this.getConfigurationManager().getSelectedUser().getFullName();
	}

	public String getGender() {
		return this.gender;
	}

	public String getIdentityGender() {
		return this.getConfigurationManager().getSelectedUser().getGender();

	}

	public HtmlDataTable getHtmlDataTable() {
		return htmlDataTable;
	}

	public HtmlSelectOneMenu getHtmlSelectOneMenu() {
		return htmlSelectOneMenu;
	}

	public String getlName() {
		return this.lName;
	}

	public String getlName1() {
		return lName1;
	}

	public void setlName1(String lName1) {
		this.lName1 = lName1;
	}

	public String getlName2() {
		return lName2;
	}

	public void setlName2(String lName2) {
		this.lName2 = lName2;
	}

	public String getlName3() {
		return lName3;
	}

	public void setlName3(String lName3) {
		this.lName3 = lName3;
	}

	public String getlName4() {
		return lName4;
	}

	public void setlName4(String lName4) {
		this.lName4 = lName4;
	}

	public String getIdentitylName() {
		return this.getConfigurationManager().getSelectedUser().getlName();
	}

	public String getIdentitylName1() {
		return this.getConfigurationManager().getSelectedUser().getlName1();
	}

	public String getIdentitylName2() {
		return this.getConfigurationManager().getSelectedUser().getlName2();
	}

	public String getIdentitylName3() {
		return this.getConfigurationManager().getSelectedUser().getlName3();
	}

	public String getIdentitylName4() {
		return this.getConfigurationManager().getSelectedUser().getlName4();
	}

	public String getIdentityPreferredName() {
		return this.getConfigurationManager().getSelectedUser()
				.getPreferredName();
	}

	public String getLocale() {
		return this.locale.getDisplayName();
	}

	public String getIdentityLocale() {
		return this.getConfigurationManager().getSelectedUser().getLocale()
				.getDisplayName();
	}

	public String getMaidenName() {
		return this.maidenName;
	}

	public String getIdentityMaidenName() {
		return this.getConfigurationManager().getSelectedUser().getMaidenName();
	}

	public String getmName() {
		return this.mName;
	}

	public String getIdentitymName() {
		return this.getConfigurationManager().getSelectedUser().getmName();
	}

	public List<Phone> getPhoneList() {
		System.out.print("Calling Get Phone List: ");
		if (this.isLoadPhone()
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setPhoneList(this.getAccountManagementBean().getPhoneList(
					this.getConfigurationManager().getSelectedUser()));
			this.setLoadPhone(false);
		} else {
			log.debug("skipped");
		}
		return this.phoneList;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public List<Role> getRoleList() {
		return this.getAccountManagementBean().getRoleList();
	}

	public Address getSelectedAddress() {
		return selectedAddress;
	}

	public AddressType getSelectedAddressType() {
		return selectedAddressType;
	}

	public Advisor getSelectedAdvisor() {
		return selectedAdvisor;
	}

	public CohortTerm getSelectedCohortTerm() {
		return selectedCohortTerm;
	}

	public CohortType getSelectedCohortType() {
		return selectedCohortType;
	}

	public EmailAddress getSelectedEmailAddress() {
		return selectedEmailAddress;
	}

	public EmailAddressType getSelectedEmailAddressType() {
		return selectedEmailAddressType;
	}

	public Phone getSelectedPhone() {
		return selectedPhone;
	}

	public PhoneType getSelectedPhoneType() {
		return selectedPhoneType;
	}

	public Program getSelectedProgram() {
		return selectedProgram;
	}

	public ProgramDomain getSelectedProgramDomain() {
		return selectedProgramDomain;
	}

	public ProgramGroup getSelectedProgramGroup() {
		return selectedProgramGroup;
	}

	public ProgramTrack getSelectedProgramTrack() {
		return selectedProgramTrack;
	}

	public Role getSelectedRole() {
		return selectedRole;
	}

	public List<Role> getSelectedRoles() {
		return selectedRoles;
	}

	public UKCollege getSelectedUkCollege() {
		return selectedUkCollege;
	}

	public UKTerm getSelectedUKCompleteTerm() {
		return selectedUKCompleteTerm;
	}

	public UKTerm getSelectedUKAnticipatedCompleteTerm() {
		return selectedUKAnticipatedCompleteTerm;
	}

	public void setSelectedUKAnticipatedCompleteTerm(
			UKTerm selectedUKAnticipatedCompleteTerm) {
		this.selectedUKAnticipatedCompleteTerm = selectedUKAnticipatedCompleteTerm;
	}

	public UKDepartment getSelectedUkDepartment() {
		return selectedUkDepartment;
	}

	public UKMajor getSelectedUKMajor() {
		return selectedUKMajor;
	}

	public UserClassification getSelectedUserClassification() {
		return selectedUserClassification;
	}

	public UserCollegeProfile getSelectedUserCollegeProfile() {
		return selectedUserCollegeProfile;
	}

	public UserProgramProfile getSelectedUserProgramProfile() {
		return selectedUserProgramProfile;
	}
	
	public UserType getSelectedUserType() {
		return selectedUserType;
	}

	public WorkingSet getSelectedWorkingSet() {
		return selectedWorkingSet;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getSsn() {
		return this.ssn;
	}

	public String getCpShortDesc() {
		return cpShortDesc;
	}

	public String getPpShortDesc() {
		return ppShortDesc;
	}

	public void setCpShortDesc(String cpShortDesc) {
		this.cpShortDesc = cpShortDesc;
	}

	public void setPpShortDesc(String ppShortDesc) {
		this.ppShortDesc = ppShortDesc;
	}

	public String getIdentitySsn() {
		String temp = this.getConfigurationManager().getSelectedUser().getSsn();
		if (temp == null || temp.length() < 1) {
			return "Not Available";
		}
		int len = temp.length();
		if (len > 4)
			return "XXX-XX-" + temp.substring(len - 4);
		else
			return "Not Available";

	}

	public String getState() {
		return state;
	}

	public String getStreet1() {
		return street1;
	}

	public String getStreet2() {
		return street2;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public String getIdentitySuffix() {
		return this.getConfigurationManager().getSelectedUser().getSuffix();
	}

	public String getTimeZone() {
		return this.timeZone.getDisplayName();
	}

	public String getIdentityTimeZone() {
		return this.getConfigurationManager().getSelectedUser().getTimeZone()
				.getDisplayName();
	}

	public String getTitle() {
		return this.title;
	}

	public String getIdentityTitle() {
		return this.getConfigurationManager().getSelectedUser().getTitle();
	}

	public String getUid() {
		return this.uid;
	}

	public String getIdentityUid() {
		return this.getConfigurationManager().getSelectedUser().getUid();
	}

	public String getUkID() {
		return this.ukID;
	}

	public String getIdentityUkID() {
		return this.getConfigurationManager().getSelectedUser().getUkID();
	}

	public List<UserCollegeProfile> getUserCollegeProfileList() {
		System.out.print("Calling Get College Profile List: ");
		if (this.isLoadCollegeProfile()
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setUserCollegeProfileList(this.getAccountManagementBean()
					.getUserCollegeProfiles(
							this.getConfigurationManager().getSelectedUser()));
			this.setLoadCollegeProfile(false);
		} else {
			log.debug("skipped");
		}
		return this.userCollegeProfileList;
	}

	public String getUsername() {
		return this.username;
	}

	public String getIdentityUsername() {
		return this.getConfigurationManager().getSelectedUser().getUsername();
	}

	public UserPersonalProfile getUserPersonalProfile() {
		System.out.print("Calling Get Personal Profile List: ");
		if (this.isLoadPersonalProfile()
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setUserPersonalProfile(this.getAccountManagementBean()
					.getUserPersonalProfile(
							this.getConfigurationManager().getSelectedUser()));
			this.setLoadPersonalProfile(false);
		} else {
			log.debug("skipped");
		}
		return this.userPersonalProfile;
	}

	public List<UserProgramProfile> getUserProgramProfileList() {
		System.out.print("Calling Get Program Profile List: ");
		if (this.isLoadProgramProfile()
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setUserProgramProfileList(this.getAccountManagementBean()
					.getUserProgramProfiles(
							this.getConfigurationManager().getSelectedUser()));
			this.setLoadProgramProfile(false);
		} else {
			log.debug("skipped");
		}
		return this.userProgramProfileList;
	}

	public List<UserProgramProfile> getStudentUserProgramProfileList() {
		System.out.print("Calling Get Student Program Profile List: ");

		if (this.isLoadProgramProfile()
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setStudentUserProgramProfiles(this.getAccountManagementBean()
					.getStudentUserProgramProfiles(
							this.getConfigurationManager().getSelectedUser()));
			this.setLoadProgramProfile(false);
		} else {
			log.debug("skipped");
		}
		return studentUserProgramProfiles;
	}

	/**
	 * @return the userRoles
	 * @uml.property name="userRoles"
	 */
	public List<Role> getUserRoles() {
		System.out.print("Calling Get User Roles: ");
		if (this.isLoadRole()
				&& this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			log.debug("loaded");
			this.setUserRoles(this.getAccountManagementBean().getUserRoleList(
					this.getConfigurationManager().getSelectedUser()));
			this.setLoadRole(false);
		} else {
			log.debug("skipped");
		}
		return this.userRoles;
	}

	/**
	 * @return the zip
	 * @uml.property name="zip"
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @return the showCollegeProfileEditModalWindow
	 * @uml.property name="showCollegeProfileEditModalWindow"
	 */
	public boolean isShowCollegeProfileEditModalWindow() {
		return showCollegeProfileEditModalWindow;
	}

	/**
	 * @return the showCollegeProfileAddModalWindow
	 * @uml.property name="showCollegeProfileAddModalWindow"
	 */
	public boolean isShowCollegeProfileAddModalWindow() {
		return showCollegeProfileAddModalWindow;
	}

	/**
	 * @return the showProgramProfileEditModalWindow
	 * @uml.property name="showProgramProfileEditModalWindow"
	 */
	public boolean isShowProgramProfileEditModalWindow() {
		return showProgramProfileEditModalWindow;
	}

	/**
	 * @return the showProgramProfileAddModalWindow
	 * @uml.property name="showProgramProfileAddModalWindow"
	 */
	public boolean isShowProgramProfileAddModalWindow() {
		return showProgramProfileAddModalWindow;
	}

	
	/**
	 * @return the showPersonalProfileEditModalWindow
	 * @uml.property name="showPersonalProfileEditModalWindow"
	 */
	public boolean isShowPersonalProfileEditModalWindow() {
		return showPersonalProfileEditModalWindow;
	}

	/**
	 * @return the showPersonalProfileAddModalWindow
	 * @uml.property name="showPersonalProfileAddModalWindow"
	 */
	public boolean isShowPersonalProfileAddModalWindow() {
		return showPersonalProfileAddModalWindow;
	}

	/**
	 * @param showCollegeProfileEditModalWindow
	 *            the showCollegeProfileEditModalWindow to set
	 * @uml.property name="showCollegeProfileEditModalWindow"
	 */
	public void setShowCollegeProfileEditModalWindow(
			boolean showCollegeProfileEditModalWindow) {
		log.debug("Updating showCollegeProfileEditModalWindow to "
				+ showCollegeProfileEditModalWindow);
		this.showCollegeProfileEditModalWindow = showCollegeProfileEditModalWindow;
	}

	/**
	 * @param showCollegeProfileAddModalWindow
	 *            the showCollegeProfileAddModalWindow to set
	 * @uml.property name="showCollegeProfileAddModalWindow"
	 */
	public void setShowCollegeProfileAddModalWindow(
			boolean showCollegeProfileAddModalWindow) {
		log.debug("Updating showCollegeProfileAddModalWindow to "
				+ showCollegeProfileAddModalWindow);
		this.showCollegeProfileAddModalWindow = showCollegeProfileAddModalWindow;
	}

	/**
	 * @return the bypassOldPasswordCheck
	 */
	public boolean isBypassOldPasswordCheck() {
		return bypassOldPasswordCheck;
	}

	/**
	 * @param bypassOldPasswordCheck
	 *            the bypassOldPasswordCheck to set
	 */
	public void setBypassOldPasswordCheck(boolean bypassOldPasswordCheck) {
		this.bypassOldPasswordCheck = bypassOldPasswordCheck;
	}

	/**
	 * @param showProgramProfileEditModalWindow
	 *            the showProgramProfileEditModalWindow to set
	 * @uml.property name="showProgramProfileEditModalWindow"
	 */
	public void setShowProgramProfileEditModalWindow(
			boolean showProgramProfileEditModalWindow) {
		this.showProgramProfileEditModalWindow = showProgramProfileEditModalWindow;
	}

	/**
	 * @param showProgramProfileAddModalWindow
	 *            the showProgramProfileAddModalWindow to set
	 * @uml.property name="showProgramProfileAddModalWindow"
	 */
	public void setShowProgramProfileAddModalWindow(
			boolean showProgramProfileAddModalWindow) {
		this.showProgramProfileAddModalWindow = showProgramProfileAddModalWindow;
	}

	/**
	 * @param showPersonalProfileEditModalWindow
	 *            the showPersonalProfileEditModalWindow to set
	 * @uml.property name="showPersonalProfileEditModalWindow"
	 */
	public void setShowPersonalProfileEditModalWindow(
			boolean showPersonalProfileEditModalWindow) {
		this.showPersonalProfileEditModalWindow = showPersonalProfileEditModalWindow;
	}

	/**
	 * @param showPersonalProfileAddModalWindow
	 *            the showPersonalProfileAddModalWindow to set
	 * @uml.property name="showPersonalProfileAddModalWindow"
	 */
	public void setShowPersonalProfileAddModalWindow(
			boolean showPersonalProfileAddModalWindow) {
		this.showPersonalProfileAddModalWindow = showPersonalProfileAddModalWindow;
	}

	public void initAccountManagement(PhaseEvent event) {
		this.setSelectedWorkingSet(null);
		if (this.getConfigurationManager().getSelectedUser() == null) {
			this.getConfigurationManager().setSelectedUser(
					this.getUserManager().getUser());
		}

		if (!this.getConfigurationManager().hasPropertyChangeListener(
				"selectedUser")) {
			System.out
					.println("Registering PropertyChangeListener for selectedUser property in ConfigurationManager");
			this.getConfigurationManager().addPropertyChangeListener(this);
		}
	}

	public String getOnEMSPageLoaded() {
		log.debug("getOnEMSPageLoaded called");
		showCollegeProfileEditModalWindow = false;
		showCollegeProfileAddModalWindow = false;
		showProgramProfileEditModalWindow = false;
		showProgramProfileAddModalWindow = false;
		showPersonalProfileEditModalWindow = false;
		showPersonalProfileAddModalWindow = false;
		return null;
	}

	/**
	 * @return the primaryPhone
	 * @uml.property name="primaryPhone"
	 */
	public Phone getPrimaryPhone() {
		if (this.getConfigurationManager().getSelectedUser() == null)
			return null;
		return this.getAccountManagementBean().getPrimaryPhone(
				this.getConfigurationManager().getSelectedUser());
	}

	/**
	 * @return the primaryEmailAddress
	 * @uml.property name="primaryEmailAddress"
	 */
	public EmailAddress getPrimaryEmailAddress() {
		if (this.getConfigurationManager().getSelectedUser() == null)
			return null;
		return this.getAccountManagementBean().getPrimaryEmailAddress(
				this.getConfigurationManager().getSelectedUser());
	}

	/**
	 * @param primaryPhone
	 *            the primaryPhone to set
	 * @uml.property name="primaryPhone"
	 */
	public void setPrimaryPhone(Phone primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	/**
	 * @param primaryEmailAddress
	 *            the primaryEmailAddress to set
	 * @uml.property name="primaryEmailAddress"
	 */
	public void setPrimaryEmailAddress(EmailAddress primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}

	/**
	 * @return the allowReportModuleAccess
	 * @uml.property name="allowReportModuleAccess"
	 */
	public boolean isAllowReportModuleAccess() {
		return allowReportModuleAccess;
	}

	/**
	 * @return the selectCustomRoleWorkingSet
	 * @uml.property name="selectCustomRoleWorkingSet"
	 */
	public boolean isSelectCustomRoleWorkingSet() {
		return selectCustomRoleWorkingSet;
	}

	/**
	 * @return the showAddress
	 * @uml.property name="showAddress"
	 */
	public boolean isShowAddress() {
		return showAddress;
	}

	public boolean isShowEmail() {
		return showEmail;
	}

	public boolean isShowPersonalProfile() {
		return showPersonalProfile;
	}

	public boolean isEnableNonUKUserOptions() {
		return enableNonUKUserOptions;
	}

	public void setEnableNonUKUserOptions(boolean enableNonUKUserOptions) {
		this.enableNonUKUserOptions = enableNonUKUserOptions;
	}

	public boolean isShowPhone() {
		return showPhone;
	}

	public boolean isShowProgramProfile() {
		return showProgramProfile;
	}

	public boolean isShowReport() {
		return showReport;
	}

	public boolean isShowRole() {
		return showRole;
	}

	public AccountManagementView getAccountManagementBean() {
		return accountManagementBean;
	}
	
	public void setAccountManagementBean(
			AccountManagementView accountManagementBean) {
		this.accountManagementBean = accountManagementBean;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public void setAllowReportModuleAccess(boolean allowReportModuleAccess) {
		this.allowReportModuleAccess = allowReportModuleAccess;
	}

	public void setCepisGlobalComponentConfigurator(
			CEPISGlobalComponentConfigurator cepisGlobalComponentConfigurator) {
		this.cepisGlobalComponentConfigurator = cepisGlobalComponentConfigurator;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public void setDegreeAwarded(Integer degreeAwarded) {
		this.degreeAwarded = degreeAwarded;
	}

	public void setdOB(Date dOB) {
		this.dOB = dOB;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmailAddressList(List<EmailAddress> set) {
		this.emailAddressList = set;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setHtmlDataTable(HtmlDataTable htmlDataTable) {
		this.htmlDataTable = htmlDataTable;
	}

	public void setHtmlSelectOneMenu(HtmlSelectOneMenu htmlSelectOneMenu) {
		this.htmlSelectOneMenu = htmlSelectOneMenu;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	/**
	 * @param phoneList
	 *            the phoneList to set
	 * @uml.property name="phoneList"
	 */
	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	/**
	 * @param phoneNo
	 *            the phoneNo to set
	 * @uml.property name="phoneNo"
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @param selectCustomRoleWorkingSet
	 *            the selectCustomRoleWorkingSet to set
	 * @uml.property name="selectCustomRoleWorkingSet"
	 */
	public void setSelectCustomRoleWorkingSet(boolean selectCustomRoleWorkingSet) {
		this.selectCustomRoleWorkingSet = selectCustomRoleWorkingSet;
	}

	/**
	 * @param selectedAddress
	 *            the selectedAddress to set
	 * @uml.property name="selectedAddress"
	 */
	public void setSelectedAddress(Address selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	/**
	 * @param selectedAddressType
	 *            the selectedAddressType to set
	 * @uml.property name="selectedAddressType"
	 */
	public void setSelectedAddressType(AddressType selectedAddressType) {
		this.selectedAddressType = selectedAddressType;
	}

	/**
	 * @param selectedAdvisor
	 *            the selectedAdvisor to set
	 * @uml.property name="selectedAdvisor"
	 */
	public void setSelectedAdvisor(Advisor selectedAdvisor) {
		this.selectedAdvisor = selectedAdvisor;
	}

	/**
	 * @param selectedCohortTerm
	 *            the selectedCohortTerm to set
	 * @uml.property name="selectedCohortTerm"
	 */
	public void setSelectedCohortTerm(CohortTerm selectedCohortTerm) {
		this.selectedCohortTerm = selectedCohortTerm;
	}

	/**
	 * @param selectedCohortType
	 *            the selectedCohortType to set
	 * @uml.property name="selectedCohortType"
	 */
	public void setSelectedCohortType(CohortType selectedCohortType) {
		this.selectedCohortType = selectedCohortType;
	}

	/**
	 * @param selectedEmailAddress
	 *            the selectedEmailAddress to set
	 * @uml.property name="selectedEmailAddress"
	 */
	public void setSelectedEmailAddress(EmailAddress selectedEmailAddress) {
		this.selectedEmailAddress = selectedEmailAddress;
	}

	/**
	 * @param selectedEmailAddressType
	 *            the selectedEmailAddressType to set
	 * @uml.property name="selectedEmailAddressType"
	 */
	public void setSelectedEmailAddressType(
			EmailAddressType selectedEmailAddressType) {
		this.selectedEmailAddressType = selectedEmailAddressType;
	}

	public void setSelectedPhone(Phone selectedPhone) {
		this.selectedPhone = selectedPhone;
	}

	/**
	 * @param selectedPhoneType
	 *            the selectedPhoneType to set
	 * @uml.property name="selectedPhoneType"
	 */
	public void setSelectedPhoneType(PhoneType selectedPhoneType) {
		this.selectedPhoneType = selectedPhoneType;
	}

	/**
	 * @param selectedProgram
	 *            the selectedProgram to set
	 * @uml.property name="selectedProgram"
	 */
	public void setSelectedProgram(Program selectedProgram) {
		this.selectedProgram = selectedProgram;
	}

	/**
	 * @param selectedProgramDomain
	 *            the selectedProgramDomain to set
	 * @uml.property name="selectedProgramDomain"
	 */
	public void setSelectedProgramDomain(ProgramDomain selectedProgramDomain) {
		this.selectedProgramDomain = selectedProgramDomain;
		this.valueChangeProgramDomain(null);
	}

	public void setSelectedProgramGroup(ProgramGroup selectedProgramGroup) {
		this.selectedProgramGroup = selectedProgramGroup;
	}

	public void setSelectedProgramTrack(ProgramTrack selectedProgramTrack) {
		this.selectedProgramTrack = selectedProgramTrack;
	}

	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}

	public void setSelectedRoles(List<Role> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public void setSelectedUkCollege(UKCollege selectedUkCollege) {
		this.selectedUkCollege = selectedUkCollege;
	}

	public void setSelectedUKCompleteTerm(UKTerm selectedUKCompleteTerm) {
		this.selectedUKCompleteTerm = selectedUKCompleteTerm;
	}

	public void setSelectedUkDepartment(UKDepartment selectedUkDepartment) {
		this.selectedUkDepartment = selectedUkDepartment;
	}

	public void setSelectedUKMajor(UKMajor selectedUKMajor) {
		this.selectedUKMajor = selectedUKMajor;
	}

	public void setSelectedUserClassification(
			UserClassification selectedUserClassification) {
		this.selectedUserClassification = selectedUserClassification;
	}

	public void setSelectedUserCollegeProfile(
			UserCollegeProfile selectedUserCollegeProfile) {
		this.selectedUserCollegeProfile = selectedUserCollegeProfile;
	}

	public void setSelectedUserProgramProfile(
			UserProgramProfile selectedUserProgramProfile) {
		this.selectedUserProgramProfile = selectedUserProgramProfile;
	}

	public void setSelectedUserType(UserType selectedUserType) {
		this.selectedUserType = selectedUserType;
	}

	public void setSelectedWorkingSet(WorkingSet selectedWorkingSet) {
		this.selectedWorkingSet = selectedWorkingSet;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public void setShowAddress(boolean showAddress) {
		this.showAddress = showAddress;
	}

	public void setShowEmail(boolean showEmail) {
		this.showEmail = showEmail;
	}

	public void setShowPersonalProfile(boolean showPersonalProfile) {
		this.showPersonalProfile = showPersonalProfile;
	}

	public void setShowPhone(boolean showPhone) {
		this.showPhone = showPhone;
	}

	public void setShowProgramProfile(boolean showProgramProfile) {
		this.showProgramProfile = showProgramProfile;
	}

	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}

	public void setShowRole(boolean showRole) {
		this.showRole = showRole;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setUkID(String ukID) {
		this.ukID = ukID;
	}

	public void setUserCollegeProfileList(
			List<UserCollegeProfile> userCollegeProfileList) {
		this.userCollegeProfileList = userCollegeProfileList;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public void setUserPersonalProfile(UserPersonalProfile userPersonalProfile) {
		this.userPersonalProfile = userPersonalProfile;
	}

	public void setUserProgramProfileList(
			List<UserProgramProfile> userProgramProfileList) {
		this.userProgramProfileList = userProgramProfileList;
	}

	public void setUserRoles(List<Role> userRoles) {
		this.userRoles = userRoles;
	}

	public boolean isCollegeProfileListEmpty() {
		if (this.getUserCollegeProfileList().size() < 1) {
			return true;
		}
		return false;
	}

	public boolean isProgramProfileListEmpty() {
		if (this.getUserProgramProfileList().size() < 1) {
			return true;
		}
		return false;
	}

	public boolean isPersonalProfileListEmpty() {
		if (getUserPersonalProfile() == null) {
			return true;
		}
		return false;
	}

	public boolean isShowProgramProfileCompleteOptions() {
		return showProgramProfileCompleteOptions;
	}

	public void setShowProgramProfileCompleteOptions(
			boolean showProgramProfileCompleteOptions) {
		this.showProgramProfileCompleteOptions = showProgramProfileCompleteOptions;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void setBadgeHolder(int badgeHolder) {
		this.badgeHolder = badgeHolder;
	}

	public int getBadgeHolder() {
		return badgeHolder;
	}
	
	
	public void getProfilePicture(OutputStream output, Object data)
			throws IOException {
		log.debug("Calling getProfilePicture function ...");
		File file = null;
		if (this.getConfigurationManager().getSelectedUser() != null
				&& this.getConfigurationManager().getSelectedUser().getUid() != null) {
			String filename = this.getCepisGlobalComponentConfigurator()
					.getProfileBaseLocation()
					+ "/"
					+ this.getConfigurationManager().getSelectedUser()
							.getUid() + ".jpg";
			log.debug("Filename: "+filename);
			try {
				
				file = new File(filename);
				if (!file.exists()) {
					log.debug("Profile image not found at " + filename);
					file = new File(this.getCepisGlobalComponentConfigurator()
							.getProfileBaseLocation() + "anonymous.jpg");
				}
				BufferedImage image = ImageIO.read(file);
				ImageIO.write(image, "jpg", output);
			} catch (IOException ie) {
				log.debug("Error:" + ie.getMessage());
			}
		}
		/*
		 * FileInputStream inputStream = new FileInputStream(file);
		 * ByteArrayOutputStream bos = new ByteArrayOutputStream(); byte[] buf =
		 * new byte[1024]; try { for (int readNum; (readNum =
		 * inputStream.read(buf)) != -1;) { bos.write(buf, 0, readNum); //no
		 * doubt here is 0 Writes len bytes from the specified byte array
		 * starting at offset off to this byte array output stream.
		 * log.debug("read " + readNum + " bytes,"); } } catch (IOException ex)
		 * { log.debug("IO Exception"); } byte[] bytes = bos.toByteArray();
		 * output.write(bytes, 0, bytes.length);
		 */
	}

	
	public Object getTableState() {
		return tableState;
	}

	public void setTableState(Object tableState) {
		this.tableState = tableState;
	}

	public SimpleSelection getSelection() {
		return selection;
	}

	public void setSelection(SimpleSelection selection) {
		this.selection = selection;
	}

	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}

	/**
	 * @return the selectedCourses
	 */
	public List<Course> getSelectedCourses() {
		return selectedCourses;
	}

	/**
	 * @param courses
	 *            the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	/**
	 * @param selectedCourses
	 *            the selectedCourses to set
	 */
	public void setSelectedCourses(List<Course> selectedCourses) {
		this.selectedCourses = selectedCourses;
	}

	/**
	 * @param dataTable
	 *            the dataTable to set
	 */
	public void setDataTable(UIExtendedDataTable dataTable) {
		this.dataTable = dataTable;
	}

	/**
	 * @return the dataTable
	 */
	public UIExtendedDataTable getDataTable() {
		return dataTable;
	}

	/**
	 * @param hiddenToStudent
	 *            the hiddenToStudent to set
	 */
	public void setHiddenToStudent(boolean hiddenToStudent) {
		this.hiddenToStudent = hiddenToStudent;
	}

	/**
	 * @return the hiddenToStudent
	 */
	public boolean isHiddenToStudent() {
		return hiddenToStudent;
	}

	/**
	 * @param holdLiftSelection
	 *            the holdLiftSelection to set
	 */
	public void setHoldLiftSelection(SimpleSelection holdLiftSelection) {
		this.holdLiftSelection = holdLiftSelection;
	}

	/**
	 * @return the holdLiftSelection
	 */
	public SimpleSelection getHoldLiftSelection() {
		return holdLiftSelection;
	}

	/**
	 * @param holdLiftTableState
	 *            the holdLiftTableState to set
	 */
	public void setHoldLiftTableState(Object holdLiftTableState) {
		this.holdLiftTableState = holdLiftTableState;
	}

	/**
	 * @return the holdLiftTableState
	 */
	public Object getHoldLiftTableState() {
		return holdLiftTableState;
	}

	/**
	 * @param holdLiftDataTable
	 *            the holdLiftDataTable to set
	 */
	public void setHoldLiftDataTable(UIExtendedDataTable holdLiftDataTable) {
		this.holdLiftDataTable = holdLiftDataTable;
	}

	/**
	 * @return the finalGPA
	 */
	public String getFinalGPA() {
		return finalGPA;
	}

	/**
	 * @param finalGPA
	 *            the finalGPA to set
	 */
	public void setFinalGPA(String finalGPA) {
		this.finalGPA = finalGPA;
	}

	/**
	 * @return the recommendCertification
	 */
	public boolean isRecommendCertification() {
		return recommendCertification;
	}
		
	/**
	 * @param recommendCertification
	 *            the recommendCertification to set
	 */
	public void setRecommendCertification(boolean recommendCertification) {
		this.recommendCertification = recommendCertification;
	}

	/**
	 * @return the holdLiftDataTable
	 */
	public UIExtendedDataTable getHoldLiftDataTable() {
		return holdLiftDataTable;
	}

	public String changeAccountSection() {
		FacesContext context = FacesContext.getCurrentInstance();
		currentAccountSection = (String) context.getExternalContext()
				.getRequestParameterMap().get("currentAccountSection");
		return null;
	}

	/**
	 * @return the currentAccountSection
	 */
	public String getCurrentAccountSection() {
		if (currentAccountSection == null
				|| currentAccountSection.trim().length() < 1) {
			return "General";
		}
		return currentAccountSection;
	}

	/**
	 * @param currentAccountSection
	 *            the currentAccountSection to set
	 */
	public void setCurrentAccountSection(String currentAccountSection) {
		this.currentAccountSection = currentAccountSection;
	}

	private JobLauncher jobLauncher;
	private Job job;

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	/**
	 * @return the job
	 */
	public Job getJob() {
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(Job job) {
		this.job = job;
	}

	public String runETSPraxisScoreLinkBatchJob() {
		try {
			Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
			parameters.put("date", new JobParameter(new Date()));

			jobLauncher.run(job, new JobParameters(parameters));

		} catch (JobInstanceAlreadyCompleteException e) {
			System.err.println("This job has been completed already !\n" + e);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * @return the userAssessmentProfiles
	 */
	public synchronized List<UserAssessmentProfile> getUserAssessmentProfiles() {
		return userAssessmentProfiles;
	}

	/**
	 * @return the repeaterState
	 */
	public DataComponentState getRepeaterState() {
		return repeaterState;
	}

	/**
	 * @return the artifactText
	 */
	public HtmlOutputText getArtifactText() {
		return artifactText;
	}

	/**
	 * @param artifactText
	 *            the artifactText to set
	 */
	public void setArtifactText(HtmlOutputText artifactText) {
		this.artifactText = artifactText;
	}

	/**
	 * @param repeaterState
	 *            the repeaterState to set
	 */
	public void setRepeaterState(DataComponentState repeaterState) {
		this.repeaterState = repeaterState;
	}

	/**
	 * @return the repeater
	 */
	public UIRepeat getRepeater() {
		return repeater;
	}

	/**
	 * @param repeater
	 *            the repeater to set
	 */
	public void setRepeater(UIRepeat repeater) {
		this.repeater = repeater;
	}

	/**
	 * @return the stdRepeater
	 */
	public UIRepeat getStdRepeater() {
		return stdRepeater;
	}

	/**
	 * @param stdRepeater
	 *            the stdRepeater to set
	 */
	public void setStdRepeater(UIRepeat stdRepeater) {
		this.stdRepeater = stdRepeater;
	}

	/**
	 * @return the showArtifactFlags
	 */
	public Map<String, Object> getShowArtifactFlags() {
		return showArtifactFlags;
	}

	/**
	 * @param showArtifactFlags
	 *            the showArtifactFlags to set
	 */
	public void setShowArtifactFlags(Map<String, Object> showArtifactFlags) {
		this.showArtifactFlags = showArtifactFlags;
	}

	/**
	 * @param userAssessmentProfiles
	 *            the userAssessmentProfiles to set
	 */
	public void setUserAssessmentProfiles(
			List<UserAssessmentProfile> userAssessmentProfiles) {
		this.userAssessmentProfiles = userAssessmentProfiles;
	}

	public boolean isUserSelected() {
		if (this.getSelectedUsers().size() >= 1) {
			return true;
		}
		return false;
	}

	public boolean isSingleUserSelected() {
		if (this.getSelectedUsers().size() == 1) {
			return true;
		}
		return false;
	}

	public boolean isMultipleUserSelected() {
		if (this.getSelectedUsers().size() > 1) {
			return true;
		}
		return false;
	}

	public boolean isUserAssessmentProfileAvailable() {
		if (this.getUserAssessmentProfiles().size() > 0) {
			return true;
		}
		return false;
	}

	public boolean isMutipleUserAssessmentProfileAvailable() {
		if (this.getUserAssessmentProfiles().size() > 1) {
			return true;
		}
		return false;
	}


	/**
	 * @param assessmentType
	 *            the assessmentType to set
	 */
	public void setAssessmentType(int assessmentType) {
		this.assessmentType = assessmentType;
	}

	/**
	 * @return the assessmentType
	 */
	public int getAssessmentType() {
		return assessmentType;
	}

	/**
	 * @param stdScoreComponent
	 *            the stdScoreComponent to set
	 */
	public void setStdScoreComponent(HtmlSelectOneMenu stdScoreComponent) {
		this.stdScoreComponent = stdScoreComponent;
	}

	/**
	 * @return the stdScoreComponent
	 */
	public HtmlSelectOneMenu getStdScoreComponent() {
		return stdScoreComponent;
	}

	/**
	 * @return the stdSetKeys
	 */
	public Set<Integer> getStdSetKeys() {
		return stdSetKeys;
	}

	/**
	 * @param stdSetKeys
	 *            the stdSetKeys to set
	 */
	public void setStdSetKeys(Set<Integer> stdSetKeys) {
		this.stdSetKeys = stdSetKeys;
	}

	/**
	 * @return the assessmentKeys
	 */
	public Set<Integer> getAssessmentKeys() {
		return assessmentKeys;
	}

	/**
	 * @return the finalScoreComponent
	 */
	public HtmlSelectOneMenu getFinalScoreComponent() {
		return finalScoreComponent;
	}

	/**
	 * @param assessmentKeys
	 *            the assessmentKeys to set
	 */
	public void setAssessmentKeys(Set<Integer> assessmentKeys) {
		this.assessmentKeys = assessmentKeys;
	}

	/**
	 * @param finalScoreComponent
	 *            the finalScoreComponent to set
	 */
	public void setFinalScoreComponent(HtmlSelectOneMenu finalScoreComponent) {
		this.finalScoreComponent = finalScoreComponent;
	}

	/**
	 * @param selectedUsers
	 *            the selectedUsers to set
	 */
	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	/**
	 * @return the selectedUsers
	 */
	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	/**
	 * @param activeStandardKey
	 *            the activeStandardKey to set
	 */
	public void setActiveStandardKey(int activeStandardKey) {
		this.activeStandardKey = activeStandardKey;
	}

	/**
	 * @return the activeStandardKey
	 */
	public int getActiveStandardKey() {
		return activeStandardKey;
	}

	/**
	 * @param selectedUserAssessmentProfile
	 *            the selectedUserAssessmentProfile to set
	 */
	public void setSelectedUserAssessmentProfile(
			UserAssessmentProfile selectedUserAssessmentProfile) {
		this.selectedUserAssessmentProfile = selectedUserAssessmentProfile;
	}

	/**
	 * @return the selectedUserAssessmentProfile
	 */
	public UserAssessmentProfile getSelectedUserAssessmentProfile() {
		return selectedUserAssessmentProfile;
	}

	/**
	 * @param standardKeys
	 *            the standardKeys to set
	 */
	public void setStandardKeys(Set<Integer> standardKeys) {
		this.standardKeys = standardKeys;
	}

	/**
	 * @return the standardKeys
	 */
	public Set<Integer> getStandardKeys() {
		return standardKeys;
	}

	/**
	 * @return the activeAssessmentProfileKey
	 */
	public int getActiveAssessmentProfileKey() {
		return activeAssessmentProfileKey;
	}

	/**
	 * @return the activeStdSetKey
	 */
	public int getActiveStdSetKey() {
		return activeStdSetKey;
	}

	/**
	 * @param activeAssessmentProfileKey
	 *            the activeAssessmentProfileKey to set
	 */
	public void setActiveAssessmentProfileKey(int activeAssessmentProfileKey) {
		this.activeAssessmentProfileKey = activeAssessmentProfileKey;
	}

	/**
	 * @param activeStdSetKey
	 *            the activeStdSetKey to set
	 */
	public void setActiveStdSetKey(int activeStdSetKey) {
		this.activeStdSetKey = activeStdSetKey;
	}

	/**
	 * @param selectedUKTerm
	 *            the selectedUKTerm to set
	 */
	public void setSelectedUKTerm(UKTerm selectedUKTerm) {
		this.selectedUKTerm = selectedUKTerm;
	}

	/**
	 * @return the selectedUKTerm
	 */
	public UKTerm getSelectedUKTerm() {
		return selectedUKTerm;
	}

	/**
	 * @param showAssessmentDataEntryPanel
	 *            the showAssessmentDataEntryPanel to set
	 */
	public void setShowAssessmentDataEntryPanel(
			boolean showAssessmentDataEntryPanel) {
		this.showAssessmentDataEntryPanel = showAssessmentDataEntryPanel;
	}

	/**
	 * @return the showAssessmentDataEntryPanel
	 */
	public boolean isShowAssessmentDataEntryPanel() {
		if (this.getSelectedUsers().size() > 1) {
			return true;
		}
		return showAssessmentDataEntryPanel;
	}

	/**
	 * @param displayMode
	 *            the displayMode to set
	 */
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * @return the displayMode
	 */
	public int getDisplayMode() {
		return displayMode;
	}

	/**
	 * @return the userAssessmentProfileList
	 */
	public List<UserAssessmentProfile> getUserAssessmentProfileList() {
		if (this.isLoadUserAssessmentProfileList()) {
			if (this.getSelectedUsers() != null
					&& this.getSelectedUsers().size() == 1) {
				this.userAssessmentProfileList = this
						.getAccountManagementBean().getUserAssessmentProfiles(
								this.getSelectedUsers().get(0));
			} else {
				log.warn("No user/multiple user selected");
			}
			this.setLoadUserAssessmentProfileList(false);
		}
		return userAssessmentProfileList;
	}

	/**
	 * @return the loadUserAssessmentProfileList
	 */
	public boolean isLoadUserAssessmentProfileList() {
		return loadUserAssessmentProfileList;
	}

	/**
	 * @param loadUserAssessmentProfileList
	 *            the loadUserAssessmentProfileList to set
	 */
	public void setLoadUserAssessmentProfileList(
			boolean loadUserAssessmentProfileList) {
		this.loadUserAssessmentProfileList = loadUserAssessmentProfileList;
	}

	/**
	 * @param userAction
	 *            the userAction to set
	 */
	public void setUserAction(int userAction) {
		this.userAction = userAction;
	}

	/**
	 * @return the userAction
	 */
	public int getUserAction() {
		return userAction;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("selectedUser")) {
			log.debug(evt.getPropertyName() + " Property changed from "
					+ evt.getOldValue() + "to" + evt.getNewValue());
			this.setLoadAddress(true);
			this.setLoadCollegeProfile(true);
			this.setLoadCourses(true);
			this.setLoadEmail(true);
			this.setLoadHoldLifts(true);
			this.setLoadPersonalProfile(true);
			this.setLoadPhone(true);
			this.setLoadProgramProfile(true);
			this.setLoadRole(true);
			// Praxis Test Score
			this.setLoadArchivedPraxisTestScore(true);			
		}
	}

	public boolean isLoadAddress() {
		return loadAddress;
	}

	public void setLoadAddress(boolean loadAddress) {
		this.loadAddress = loadAddress;
	}

	/**
	 * @return the loadArchivedPraxisTestScore
	 */
	public boolean isLoadArchivedPraxisTestScore() {
		return loadArchivedPraxisTestScore;
	}

	/**
	 * @param loadArchivedPraxisTestScore
	 *            the loadArchivedPraxisTestScore to set
	 */
	public void setLoadArchivedPraxisTestScore(
			boolean loadArchivedPraxisTestScore) {
		this.loadArchivedPraxisTestScore = loadArchivedPraxisTestScore;
	}
		
	/**
	 * @return the loadEmail
	 */
	public boolean isLoadEmail() {
		return loadEmail;
	}

	/**
	 * @param loadEmail
	 *            the loadEmail to set
	 */
	public void setLoadEmail(boolean loadEmail) {
		this.loadEmail = loadEmail;
	}

	/**
	 * @return the loadPersonalProfile
	 */
	public boolean isLoadPersonalProfile() {
		return loadPersonalProfile;
	}

	/**
	 * @param loadPersonalProfile
	 *            the loadPersonalProfile to set
	 */
	public void setLoadPersonalProfile(boolean loadPersonalProfile) {
		this.loadPersonalProfile = loadPersonalProfile;
	}

	/**
	 * @return the loadPhone
	 */
	public boolean isLoadPhone() {
		return loadPhone;
	}

	/**
	 * @param loadPhone
	 *            the loadPhone to set
	 */
	public void setLoadPhone(boolean loadPhone) {
		this.loadPhone = loadPhone;
	}

	/**
	 * @return the loadProgramProfile
	 */
	public boolean isLoadProgramProfile() {
		return loadProgramProfile;
	}

	/**
	 * @param loadProgramProfile
	 *            the loadProgramProfile to set
	 */
	public void setLoadProgramProfile(boolean loadProgramProfile) {
		this.loadProgramProfile = loadProgramProfile;
	}

	/**
	 * @return the loadCollegeProfile
	 */
	public boolean isLoadCollegeProfile() {
		return loadCollegeProfile;
	}

	/**
	 * @param loadCollegeProfile
	 *            the loadCollegeProfile to set
	 */
	public void setLoadCollegeProfile(boolean loadCollegeProfile) {
		this.loadCollegeProfile = loadCollegeProfile;
	}

	/**
	 * @return the loadRole
	 */
	public boolean isLoadRole() {
		return loadRole;
	}

	/**
	 * @param loadRole
	 *            the loadRole to set
	 */
	public void setLoadRole(boolean loadRole) {
		this.loadRole = loadRole;
	}

	/**
	 * @return the loadCourses
	 */
	public boolean isLoadCourses() {
		return loadCourses;
	}

	/**
	 * @param loadCourses
	 *            the loadCourses to set
	 */
	public void setLoadCourses(boolean loadCourses) {
		this.loadCourses = loadCourses;
	}

	/**
	 * @return the loadHoldLifts
	 */
	public boolean isLoadHoldLifts() {
		return loadHoldLifts;
	}

	/**
	 * @param loadHoldLifts
	 *            the loadHoldLifts to set
	 */
	public void setLoadHoldLifts(boolean loadHoldLifts) {
		this.loadHoldLifts = loadHoldLifts;
	}

	/**
	 * @return the studentUserProgramProfiles
	 */
	public List<UserProgramProfile> getStudentUserProgramProfiles() {
		return studentUserProgramProfiles;
	}

	/**
	 * @param studentUserProgramProfiles
	 *            the studentUserProgramProfiles to set
	 */
	public void setStudentUserProgramProfiles(
			List<UserProgramProfile> studentUserProgramProfiles) {
		this.studentUserProgramProfiles = studentUserProgramProfiles;
	}
	
	public NoteView getNoteBean() {
		return noteBean;
	}

	public void setNoteBean(NoteView noteBean) {
		this.noteBean = noteBean;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Note getSelectedNote() {
		if(selectedNote == null){
			selectedNote = new Note();
		}
		return selectedNote;
	}

	public void setSelectedNote(Note selectedNote) {
		this.selectedNote = selectedNote;
	}

	public HtmlExtendedDataTable getNoteTable() {
		return noteTable;
	}

	public void setNoteTable(HtmlExtendedDataTable noteTable) {
		this.noteTable = noteTable;
	}

	public SimpleSelection getNoteSelection() {
		return noteSelection;
	}

	public void setNoteSelection(SimpleSelection noteSelection) {
		this.noteSelection = noteSelection;
	}

	public ExtendedTableDataModel<Note> getNoteDataModel() {
		return noteDataModel;
	}

	public void setNoteDataModel(ExtendedTableDataModel<Note> noteDataModel) {
		this.noteDataModel = noteDataModel;
	}

	public Object getNoteTableState() {
		return noteTableState;
	}

	public void setNoteTableState(Object noteTableState) {
		this.noteTableState = noteTableState;
	}

	public float getCumulativeGpa() {
		
		List<Course> courses = new ArrayList<Course>();
		
		float totalQualityPoints = 0;
		float totalQualityHours = 0;
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(3);
		df.setMaximumFractionDigits(3);
		
		if (getAccountManagementBean().getAcademicProfile(
			this.getConfigurationManager().getSelectedUser()) != null){
			
			courses = this.getAccountManagementBean().getAcademicProfile(
			this.getConfigurationManager().getSelectedUser());
			
			for(Course course: courses){
				if(course.getGpa().contains("T")
				|| course.getGpa().contains("R")
				|| course.getrTitle() != null){
					totalQualityHours = totalQualityHours + 0;
					totalQualityPoints = totalQualityPoints + 0;
				} else if(course.getGpa().contains("A") 
				|| course.getGpa().contains("B")
				|| course.getGpa().contains("C")
				|| course.getGpa().contains("D")
				|| course.getGpa().contains("E")){
						totalQualityPoints = totalQualityPoints + course.getGpaPoints();
						totalQualityHours = totalQualityHours + course.getGpaHours();
				}
			}
			
			setTotalQualityHours((int)totalQualityHours);
			setTotalQualityPoints((int)totalQualityPoints);
			if(getTotalQualityHours() != 0){
				String decimalFormatted = df.format(totalQualityPoints / totalQualityHours);
				setCumulativeGpa(new Float(decimalFormatted));	
			}else{
				setCumulativeGpa(0);
			}
		}
		
		
		return cumulativeGpa;
	}

	public void setCumulativeGpa(float cumulativeGpa) {
		this.cumulativeGpa = cumulativeGpa;
	}

	public int getTotalQualityHours() {
		return totalQualityHours;
	}

	public void setTotalQualityHours(int totalQualityHours) {
		this.totalQualityHours = totalQualityHours;
	}

	public int getTotalQualityPoints() {
		return totalQualityPoints;
	}

	public void setTotalQualityPoints(int totalQualityPoints) {
		this.totalQualityPoints = totalQualityPoints;
	}


	public UserPersonalProfile getAddUserPersonalProfile() {
		return addUserPersonalProfile;
	}


	public void setAddUserPersonalProfile(UserPersonalProfile addUserPersonalProfile) {
		this.addUserPersonalProfile = addUserPersonalProfile;
	}


	public FacultyAdvisor getSelectedFacultyAdvisor() {
		return selectedFacultyAdvisor;
	}


	public void setSelectedFacultyAdvisor(FacultyAdvisor selectedFacultyAdvisor) {
		this.selectedFacultyAdvisor = selectedFacultyAdvisor;
	}	
}