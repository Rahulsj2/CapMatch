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

import com.fasterxml.jackson.databind.ObjectMapper;
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
	private AccountConfirmationRepository accountConfirmationRepo;
	
	ObjectMapper jacksonObjectMapper = new ObjectMapper();
//	
//	
//	@Primary
//	@Bean 
//	public FreeMarkerConfigurationFactoryBean factoryBean() {
//		FreeMarkerConfigurationFactoryBean bean=new FreeMarkerConfigurationFactoryBean();
//		bean.setTemplateLoaderPath("classpath:/templates/mail");
//		return bean;
//	}
	
	
//	public MailResponse sendEmail(MailRequest request, Map<String, Object> model) {
//		MailResponse response = new MailResponse();
//		MimeMessage message = sender.createMimeMessage();
//		try {
//			// set mediaType
//			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//					StandardCharsets.UTF_8.name());
//			// add attachment
//			helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
//
//			Template t = config.getTemplate("email-template.ftl");
//			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
//
//			helper.setTo(request.getTo());
//			helper.setText(html, true);
//			helper.setSubject(request.getSubject());
//			helper.setFrom(request.getFrom());
//			sender.send(message);
//
//			response.setMessage("mail send to : " + request.getTo());
//			response.setStatus(Boolean.TRUE);
//
//		} catch (MessagingException | IOException | TemplateException e) {
//			response.setMessage("Mail Sending failure : "+e.getMessage());
//			response.setStatus(Boolean.FALSE);
//		}
//
//		return response;
//	}
	
	
	public boolean sendAccountConfirmationEmail(User user) {
		if (user == null || user.getEmail() == null) return false;
		
		// Get Account Confirmation Details
		AccountConfirmation confirmationDetails = accountConfirmationRepo.findByUser(user);
		
		if (confirmationDetails == null) {
			log.error("Could not find confirmation details for user '" + user.getEmail() 
					+ "while trying to send account confirmation email");
			return false;			
		}
		
		MimeMessage email = sender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(email, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());	
			Template mailTemplate = config.getTemplate("account-confirmation-email-template.ftl");
			
			//Create User Map
//			Map<String, Object> userMap = this.jacksonObjectMapper.convertValue(user,  Map.class);			Resulted In infinite recursion. Investigate
			Map<String, Object> userMap = new HashMap<>();
			userMap.put("firstname", user.getFirstname());
			userMap.put("lastname", user.getLastname());
			userMap.put("confirmCode", confirmationDetails.getConfirmCode());
			
			
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, userMap);
	
			helper.setTo(user.getEmail());
			helper.setText(html, true);
			helper.setSubject("Confirm Your Account Registration");
//			helper.setFrom("ca4036e29f3869");			// Configure the bit from APP CONFIGFILE
			
			sender.send(email);
	
		} catch (TemplateException te) {
			log.error("There was am templating error when sending account confirmation email to '" + user.getEmail()
					+ "' \t ErrorTrace: ", te.toString());
			return false;
		} catch(IOException ioe) {
			log.error("Could not find or load account confirmation email template while sending account confirmation"
					+ " email to '" + user.getEmail()
					+ "' \t ErrorTrace: ", ioe.getMessage());
			return false;
		} catch (MessagingException me) {
			log.error("There was a Messaging Exception when sending account confirmation email to '" + user.getEmail()
			+ "' \t ErrorTrace: ", me.toString());
			return false;
		}
		
		log.info("Confirmation email sent to '" + user.getEmail() + "'... ");
		
		return true;	
	}


}
