package com.stacko.capmatch.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.stacko.capmatch.Configuration.ClientRoutesConfig;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Security.Signup.AccountConfirmation;
import com.stacko.capmatch.Security.Signup.AccountConfirmationRepository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private Configuration config;				// Imported fromFreeMarkerTemplate
	
	@Autowired
	private ClientRoutesConfig clientRoutes;
	
	@Autowired
	private AccountConfirmationRepository accountConfirmationRepo;
	
//	ObjectMapper jacksonObjectMapper = new ObjectMapper();
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public boolean sendAccountConfirmationEmail(User user) {
		if (user == null || user.getEmail() == null) return false;
		
		// Get Account Confirmation Details
		AccountConfirmation confirmationDetails = accountConfirmationRepo.findByUser(user);
		
		if (confirmationDetails == null) {
			log.error("Could not find confirmation details for user '" + user.getEmail() 
					+ "while trying to send account confirmation email");
			return false;			
		}
		
		Map<String, Object> mailMap = new HashMap<>();
		mailMap.put("confirmCode", confirmationDetails.getConfirmCode());
		mailMap.put("clientBasePath", this.clientRoutes.getBasePath());
		
		String mailSubject = "Confirm Your Account Registration";
		String templateName = "verify-email-inline.ftl";
		
		return sendMail(user, mailSubject, templateName, mailMap);	
	}
	
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public boolean sendPasswordResetEmail(User user) {
		if (user == null || user.getEmail() == null) return false;
		
		// Get Account Confirmation Details
		AccountConfirmation confirmationDetails = accountConfirmationRepo.findByUser(user);		
		if (confirmationDetails == null) {
			log.error("Could not find confirmation details for user '" + user.getEmail() 
					+ "while trying to send account confirmation email");
			return false;			
		}
		
		Map<String, Object> mailMap = new HashMap<>();
		mailMap.put("confirmCode", confirmationDetails.getConfirmCode());
		mailMap.put("passwordResetPage", this.clientRoutes.getResetPassword());
		
		String mailSubject = "Reset Your Password";
		String templateName = "password_reset_email_template.ftl";
		
		return sendMail(user, mailSubject, templateName, mailMap);
	}
	
	
	
	
	/**
	 * 
	 * @param user
	 * @param mailSubject
	 * @param templateName
	 * @param mailArgumentMap
	 * @return
	 */
	private boolean sendMail(User user, String mailSubject, String templateName, Map<String, Object> mailArgumentMap) {
		if (user == null || user.getEmail() == null) return false;
		
		MimeMessage email = sender.createMimeMessage();
		try {
			// Set MediaType
			MimeMessageHelper helper = new MimeMessageHelper(email, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());	
			
//			// This is how to simply add an attachment to any mail
//			helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
			
			Template mailTemplate = config.getTemplate(templateName);
			
			addUserDetailsToMailMap(user, mailArgumentMap);			
			
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, mailArgumentMap);
	
			helper.setTo(user.getEmail());
			helper.setText(html, true);
			helper.setSubject(mailSubject);
			helper.setFrom("capmatch@ashesi.edu.gh");
			
			sender.send(email);
	
		} catch (TemplateException te) {
			log.error("There was a templating error when sending email with subject '" + mailSubject + "' to '" + user.getEmail()
					+ "' \t ErrorTrace: ", te.toString());
			return false;
		} catch(IOException ioe) {
			log.error("Could not find or load template '" + templateName + "'  when sending email with subject '" + mailSubject +
					"' to '" + user.getEmail()	+ "' \t ErrorTrace: ", ioe.getMessage());
			return false;
		} catch (MessagingException me) {
			log.error("There was a Messaging Exception when sending email with subject '" + mailSubject + "' to '" + user.getEmail()
			+ "' \t ErrorTrace: ", me.toString());
			return false;
		}
		
		log.info("Email with subject '" + mailSubject + "' sent to '" + user.getEmail() + "'... ");
		
		return true;			
	}



	private void addUserDetailsToMailMap(User user, Map<String, Object> mailMap) {
		mailMap.put("firstname", user.getFirstname());
		mailMap.put("lastname", user.getLastname());
		mailMap.put("email", user.getEmail());
	}


}
