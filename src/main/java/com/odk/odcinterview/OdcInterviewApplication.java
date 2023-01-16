package com.odk.odcinterview;

import com.odk.odcinterview.Model.Erole;
import com.odk.odcinterview.Model.Etat;
import com.odk.odcinterview.Model.Role;
import com.odk.odcinterview.Model.Utilisateur;
import com.odk.odcinterview.Repository.EtatRepository;
import com.odk.odcinterview.Repository.RoleRepository;
import com.odk.odcinterview.Repository.UtilisateurRepository;
import com.odk.odcinterview.Service.AccountService;
import com.odk.odcinterview.Service.EtatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.ArrayList;

@SpringBootApplication
public class OdcInterviewApplication {
    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EtatRepository etatRepository;

    public OdcInterviewApplication(RoleRepository roleRepository,
                                   UtilisateurRepository utilisateurRepository,
                                   EtatRepository etatRepository) {
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.etatRepository = etatRepository;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(OdcInterviewApplication.class, args);
    }
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/","classpath:/image/","classpath:/temp/")
                    .setCachePeriod(0);
        }
    }
    @Bean
    CommandLineRunner start(AccountService accountService, EtatService etatService){
        return args -> {
            if (roleRepository.findByRoleName(Erole.JURY)==null){
                accountService.saveRole(new Role(null, Erole.JURY));
            }
            if(roleRepository.findByRoleName(Erole.ADMIN)==null){
                accountService.saveRole(new Role(null, Erole.ADMIN));
            }
            if (utilisateurRepository.findByEmail("Admin@gmail.com")==null)
            accountService.saveAdmin(new Utilisateur(null,null,"Admin","ODC","Admin@gmail.com","82608734","HOMME","ADMIN1234"));
            if(etatRepository.findByStatus("EN COUR")==null){
                etatService.Create(new Etat(null,"EN COUR"));
            }
            if(etatRepository.findByStatus("A VENIR")==null){
                etatService.Create(new Etat(null,"A VENIR"));
            }
            if(etatRepository.findByStatus("TERMINE")==null){
                etatService.Create(new Etat(null,"TERMINE"));
            }
        };
    };
}
