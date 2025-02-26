package com.esprit.constuctify.project_ms;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendProjectDetailsEmail(Project project) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set email details
        helper.setFrom("oueslati.jihed@esprit.tn"); // Set "From" address
        helper.setTo("oueslati.jihed@esprit.tn"); // Set recipient email
        helper.setSubject("New Project Added: " + project.getNomProjet());

        // Create the HTML email content
        String htmlContent = createEmailTemplate(project);
        helper.setText(htmlContent, true); // Set HTML content

        // Send the email
        mailSender.send(message);
    }

    private String createEmailTemplate(Project project) {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Project Details</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; margin: 0; padding: 0; }" +
                "        .email-container { max-width: 600px; margin: 20px auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                "        h1 { color: #007BFF; text-align: center; }" +
                "        .project-details { margin-top: 20px; }" +
                "        .project-details p { margin: 10px 0; font-size: 16px; }" +
                "        .footer { margin-top: 20px; text-align: center; color: #777; font-size: 14px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='email-container'>" +
                "        <h1>New Project Added</h1>" +
                "        <div class='project-details'>" +
                "            <p><strong>Project Name:</strong> " + project.getNomProjet() + "</p>" +
                "            <p><strong>Description:</strong> " + project.getDescriptionProjet() + "</p>" +
                "            <p><strong>Start Date:</strong> " + project.getDateDebut() + "</p>" +
                "            <p><strong>End Date:</strong> " + project.getDateFin() + "</p>" +
                "            <p><strong>Status:</strong> " + project.getEtatProjet() + "</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>This is an automated email. Please do not reply.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}