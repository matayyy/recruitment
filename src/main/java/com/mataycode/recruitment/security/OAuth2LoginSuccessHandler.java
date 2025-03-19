package com.mataycode.recruitment.security;

import com.mataycode.recruitment.domain.Customer;
import com.mataycode.recruitment.domain.Gender;
import com.mataycode.recruitment.domain.Role;
import com.mataycode.recruitment.repository.CustomerRepository;
import com.mataycode.recruitment.services.GMailer;
import com.mataycode.recruitment.services.GoogleUserService;
import com.mataycode.recruitment.services.OAuth2UserService;
import com.mataycode.recruitment.util.GoogleUserInfoParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleUserService googleUserDataService;
    private final OAuth2UserService oauth2UserService;

    public OAuth2LoginSuccessHandler(CustomerRepository customerRepository, GoogleUserService googleUserDataService, PasswordEncoder passwordEncoder, OAuth2UserService oauth2UserService) {
        this.customerRepository = customerRepository;
        this.googleUserDataService = googleUserDataService;
        this.passwordEncoder = passwordEncoder;
        this.oauth2UserService = oauth2UserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //Represent the logged-in user. (provider, user and his roles)
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        //Contains only user data (email, name, picture...)
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        //stores the OAuth2 session and access token.
        OAuth2AuthorizedClient client = oauth2UserService.getAuthorizedClient(oauthToken);

        //GET SOME USER INFO FROM oAUTH2User
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        String profileImage = oAuth2User.getAttribute("picture");
        String provider = client.getClientRegistration().getRegistrationId();

        //GET ADDITIONAL USER INFO FROM Google People API
        Map<String, Object> userInfo = googleUserDataService.getUserInfo(client.getAccessToken().getTokenValue());

        //GET GENDER
        Gender gender = Gender.valueOf(GoogleUserInfoParser.extractGender(userInfo));
        //GET BIRTHDATE
        LocalDate birthdate = GoogleUserInfoParser.extractBirthdate(userInfo);

        //CREATE RANDOM PASSWORD
        String hashedPassword = passwordEncoder.encode(UUID.randomUUID().toString());

        customerRepository.findCustomerByEmail(email).orElseGet(() -> {
            Customer newCustomer = new Customer();
            newCustomer.setEmail(email);
            newCustomer.setName(name);
            newCustomer.setProfileImageId(profileImage);
            newCustomer.setProvider(provider);
            newCustomer.setGender(gender);
            newCustomer.setRoles(List.of(Role.USER));
            newCustomer.setBirthDate(birthdate);
            newCustomer.setPassword(hashedPassword);
            newCustomer.setEnabled(true);
            Customer savedCustomer = customerRepository.save(newCustomer);

            //SEND USER EMAIL WITHOUT VERIFICATION LINK.
            try {
                new GMailer().sendMail(email, "Registration message", """
                    Hola!
                    Successfully registered at mataycode-website!
                    
                    Now yo are part of our familia amigo.
                    Saludos.
                    """);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return savedCustomer;
        });

        response.sendRedirect("/");
    }
}
