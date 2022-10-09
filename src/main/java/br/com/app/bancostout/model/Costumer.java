package br.com.app.bancostout.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.Exclude;

public class Costumer implements UserDetails{
	
	@Exclude
	private static final boolean DEFAULT_IS_ACCOUNT_NON_EXPIRED = true;	
	@Exclude
	private static final boolean DEFAULT_IS_ACCOUNT_NON_LOCKED = true;	
	@Exclude
	private static final boolean DEFAULT_IS_CREDENTIALS_NON_EXPIRED = true;	
	@Exclude
	private static final boolean DEFAULT_IS_ENABLED = true;
	
	@DocumentId
	private DocumentReference id;
	private String name;
	private String username;
	private String password;	
	
	private boolean isAccountNonExpired;	
	private boolean isAccountNonLocked;	
	private boolean isCredentialsNonExpired;	
	private boolean isEnabled;

	public Costumer() {}

	public Costumer(String name, String username, String password) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.isAccountNonExpired = DEFAULT_IS_ACCOUNT_NON_EXPIRED;
		this.isAccountNonLocked = DEFAULT_IS_ACCOUNT_NON_LOCKED;
		this.isCredentialsNonExpired = DEFAULT_IS_CREDENTIALS_NON_EXPIRED;
		this.isEnabled = DEFAULT_IS_ENABLED;
	}
		
	public Costumer(DocumentReference id, String name, String username, String password, boolean isAccountNonExpired,
			boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
		this.isEnabled = isEnabled;
	}

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonLocked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}
	
	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}
	
	public DocumentReference getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	
	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	
	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}


}


