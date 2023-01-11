package com.odk.odcinterview;

import com.odk.odcinterview.Model.Erole;
import com.odk.odcinterview.Model.Role;
import com.odk.odcinterview.Model.Utilisateur;
import com.odk.odcinterview.Repository.RoleRepository;
import com.odk.odcinterview.Repository.UtilisateurRepository;
import com.odk.odcinterview.Service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.ArrayList;

@SpringBootApplication
public class OdcInterviewApplication {
    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;

    public OdcInterviewApplication(RoleRepository roleRepository,
                                   UtilisateurRepository utilisateurRepository) {
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(OdcInterviewApplication.class, args);
    }
    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            if (roleRepository.findByRoleName(Erole.JURY)==null){
                accountService.saveRole(new Role(null, Erole.JURY));
            }
            if(roleRepository.findByRoleName(Erole.ADMIN)==null){
                accountService.saveRole(new Role(null, Erole.ADMIN));
            }
            if (utilisateurRepository.findByEmail("Admin@gmail.com")==null)
            accountService.saveAdmin(new Utilisateur(null,null,"Admin","ODC","Admin@gmail.com","82608734","HOMME","ADMIN1234"));

        };
    };
}
