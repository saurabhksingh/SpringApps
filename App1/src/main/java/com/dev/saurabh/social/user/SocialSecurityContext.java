package com.dev.saurabh.social.user;

import com.dev.saurabh.blog.domain.UserAccount;

public final class SocialSecurityContext {

	private static final ThreadLocal<UserAccount> ourCurrentSignedinUserStore = new ThreadLocal<UserAccount>();

	public static UserAccount getCurrentUser() {
		UserAccount user = ourCurrentSignedinUserStore.get();
		if (user == null) {
			throw new IllegalStateException("No Active User");
		}
		return user;
	}

	public static void setCurrentUser(UserAccount user) {
		ourCurrentSignedinUserStore.set(user);
	}

	public static boolean userSignedIn() {
		return ourCurrentSignedinUserStore.get() != null;
	}

	public static void remove() {
		ourCurrentSignedinUserStore.remove();
	}

}
