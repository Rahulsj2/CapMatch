package com.stacko.capmatch.Models;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.stacko.capmatch.Security.UserPermission;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor(access=AccessLevel.PROTECTED, force=true)
//@Table(name="SystemUser")
public class User implements UserDetails, Comparable<User> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5502144075497068553L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int userId;
	
	
	@Column(nullable=false)
	protected String firstname;
	
	@Column(nullable=false)
	protected String lastname;	
	
	/**
	 * Email must always remain unique because it is used in User compareTo method. It is also how users
	 * login
	 */
	@Column(unique=true, nullable=false)
	@Email
	protected String email;
	
	@Size(min=8)
	protected String password;
	
	@Column(nullable=true)
	protected String bio;
	
	protected AccountStatus accountStatus = AccountStatus.UNVERIFIED;							// Users are unverified by default
	
	protected Date registrationDate;
	
	
	@ManyToMany(fetch = FetchType.EAGER,
				cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "user_permission",
				joinColumns = { @JoinColumn(name = "fk_user")},
				inverseJoinColumns = { @JoinColumn(name = "fk_permission")})
	protected Set<UserPermission> permissions = new TreeSet<>();
	
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "user_interest",
			joinColumns = { @JoinColumn(name = "fk_interested_user")},
			inverseJoinColumns = { @JoinColumn(name = "fk_interest")})
	protected Set<Interest> interests = new TreeSet<>();
	
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "user_sdg",
			joinColumns = { @JoinColumn(name = "fk_user")},
			inverseJoinColumns = { @JoinColumn(name = "fk_sdg")})
	protected Set<SDG> SDGs = new TreeSet<>();
	
	
	/**
	 * This is the class constructor
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 */
	public User(String firstname, String lastname, @Email String email, @Size(min = 8) String password) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}
	
	
	
	
	
	@PrePersist
	private void setRegistrationDate() {
		this.registrationDate = new Date();
		if (email != null)
			this.email = email.toLowerCase();
	}
	
	
	
	
	// ----------------------------------- General Class Methods ---------------------------------------
	
	public String getName() { return this.firstname + " " + this.lastname;}
	
	public void grantPermission(UserPermission permission) {
		if (permission != null) {
			this.permissions.add(permission);
			permission.getUsers().add(this);
		}
	}
	
	public void revokePemission(UserPermission permission) {
		if (permission != null) {
			this.permissions.remove(permission);
			permission.getUsers().remove(this);
		}
	}
	
	public void addInterest(Interest interest) {
		if (interest != null) {
			this.interests.add(interest);
			interest.getInterestedUsers().add(this);
		}
	}
	
	public void removeInterest(Interest interest) {
		if (interest != null)
			this.interests.remove(interest);
		interest.getInterestedUsers().remove(this);
	}
	
	public void addSDG(SDG sdg) {
		if (sdg != null) {
			this.SDGs.add(sdg);
			sdg.getInterestedUsers().add(this);
		}
	}
	
	public void removeSDG(SDG sdg) {
		if (sdg != null) {
			this.SDGs.remove(sdg);
			sdg.getInterestedUsers().remove(this);
		}
	}	
	
	/**
	 * Compare to method was added to make this object compatible for use with a TreeSet
	 */
	public int compareTo(User user) {
		if (user == null)
			return -1;
		return this.email.compareToIgnoreCase(user.getEmail());
	}

	
	// ---------------------------------- End of Class Methods -----------------------------------------
	
	
	
	
	//------------------------------------- Spring Security Methods --------------------------------------------	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissions;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return !AccountStatus.EXPIRED.equals(this.accountStatus);				// Make sure account status isn't set to EXPIRED
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !AccountStatus.BLOCKED.equals(this.accountStatus);				// Make sure account status isn't set to BLOCKED
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return !AccountStatus.EXPIRED.equals(this.accountStatus);				// Make sure account status isn't set to EXPIRED
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	// ------------------------------------------- End of Security methods ------------------------------------------
	
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (accountStatus != other.accountStatus)
			return false;
		if (bio == null) {
			if (other.bio != null)
				return false;
		} else if (!bio.equals(other.bio))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		if (registrationDate == null) {
			if (other.registrationDate != null)
				return false;
		} else if (!registrationDate.equals(other.registrationDate))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountStatus == null) ? 0 : accountStatus.hashCode());
		result = prime * result + ((bio == null) ? 0 : bio.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
		result = prime * result + userId;
		return result;
	}
	


	// --------------------------------------- Define Helper Classes/Enums ---------------------------------
	
	public enum AccountStatus{
		UNVERIFIED, ACTIVE, BLOCKED, EXPIRED;
	}	
}
