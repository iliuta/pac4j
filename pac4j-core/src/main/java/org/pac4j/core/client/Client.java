/*
  Copyright 2012 - 2014 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.core.client;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Authenticator;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.profile.ProfileCreator;
import org.pac4j.core.profile.UserProfile;

/**
 * <p>This interface represents a client. It is an authentication client in charge of an authentication process.</p>
 * <p>It is responsible for starting the authentication process (by redirecting the user to the identity provider) and
 * for retrieving the credentials and the user profile after a successful authentication. It may implement a protocol like OAuth, CAS or SAML
 * or rely on additional objects: {@link Authenticator} for credentials validation or {@link ProfileCreator} for user profile creation.</p>
 * <p>A client has a type accessible by the {@link #getName()} method.</p>
 * <p>A client supports the authentication process and user profile retrieval through :</p>
 * <ul>
 * <li>the {@link #redirect(WebContext, boolean, boolean)} method to get the redirection to perform for the user to start the authentication (at
 * the provider)</li>
 * <li>the {@link #getCredentials(WebContext)} method to get the credentials (in the application) after the user has been successfully
 * authenticated at the provider</li>
 * <li>the {@link #getUserProfile(Credentials, WebContext)} method to get the user profile from the credentials.</li>
 * </ul>
 * 
 * @author Jerome Leleu
 * @since 1.4.0
 */
public interface Client<C extends Credentials, U extends UserProfile> {

    /**
     * Get the name of the client.
     * 
     * @return the name of the client
     */
    public String getName();

    /**
     * <p>Redirect to the authentication provider by updating the WebContext accordingly.</p>
     * <p>Though, if this client requires an indirect redirection, it will return a redirection to the callback url (with an additionnal parameter requesting a
     * redirection). Whatever the kind of client's redirection, the <code>protectedTarget</code> parameter set to <code>true</code> enforces
     * a direct redirection.
     * <p>If an authentication has already been tried for this client and has failed (previous <code>null</code> credentials) and if the target
     * is protected (<code>protectedTarget</code> set to <code>true</code>), a forbidden response (403 HTTP status code) is returned.</p>
     * <p>If the request is an AJAX one (<code>ajaxRequest</code> parameter set to <code>true</code>), an authorized response (401 HTTP status
     * code) is returned instead of a redirection.</p>
     * 
     * @param context
     * @param protectedTarget
     * @param ajaxRequest
     * @throws RequiresHttpAction
     */
    public void redirect(WebContext context, boolean protectedTarget, boolean ajaxRequest) throws RequiresHttpAction;

    /**
     * <p>Get the credentials from the web context. In some cases, a {@link RequiresHttpAction} may be thrown instead:</p>
     * <ul>
     * <li>if this client requires an indirect redirection, the redirection will be actually performed by these method and not by the
     * {@link #redirect(WebContext, boolean, boolean)} one (302 HTTP status code)</li>
     * <li>if the <code>CasClient</code> receives a logout request, it returns a 200 HTTP status code</li>
     * <li>for the <code>BasicAuthClient</code>, if no credentials are sent to the callback url, an unauthorized response (401 HTTP status
     * code) is returned to request credentials through a popup.</li>
     * </ul>
     * 
     * @param context web context
     * @return the credentials
     * @throws RequiresHttpAction requires an additionnal HTTP action
     */
    public C getCredentials(WebContext context) throws RequiresHttpAction;

    /**
     * Get the user profile from the credentials.
     * 
     * @param credentials credentials
     * @param context web context 
     * @return the user profile
     */
    public U getUserProfile(C credentials, WebContext context);
}
