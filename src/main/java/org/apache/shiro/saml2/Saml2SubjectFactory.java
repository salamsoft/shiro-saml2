/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.shiro.saml2;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 
 * @author AritraChatterjee
 * 
 */
public class Saml2SubjectFactory extends DefaultWebSubjectFactory {

	@Override
	public Subject createSubject(SubjectContext context) {

		/*
		 * The authenticated flag is only set by the SecurityManager after a
		 * successful authentication attempt
		 */
		boolean authenticated = context.isAuthenticated();

		/*
		 * Although the SecurityManager 'sees' the submission as a successful
		 * authentication, in reality, the login might have been just a IdP
		 * rememberMe login. If so, set the authenticated flag appropriately
		 */
		if (authenticated) {
			AuthenticationToken token = context.getAuthenticationToken();

			if (token != null && token instanceof Saml2Token) {
				Saml2Token saml2Token = (Saml2Token) token;
				/*
				 * Set the authenticated flag of the context to true only if the
				 * SAML2 subject is not in a remember me mode
				 */
				if (saml2Token.isRememberMe()) {
					context.setAuthenticated(false);
				}
			}
		}

		return super.createSubject(context);
	}
}