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
        helper.setFrom("oueslati.jihed@esprit.tn"); 
        helper.setTo("oueslati.jihed@esprit.tn"); 
        helper.setSubject("New Project Added: " + project.getNomProjet());

        // Create the HTML email content
        String htmlContent = createEmailTemplate(project);
        helper.setText(htmlContent, true); // Set HTML content

        // Send the email
        mailSender.send(message);
    }

    private String createEmailTemplate(Project project) {
        // Logo path from static resources
        String logoPath = "/constructify-logo.png";

        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html dir=\"ltr\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">" +
                "<head>" +
                "  <meta charset=\"UTF-8\">" +
                "  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">" +
                "  <meta name=\"x-apple-disable-message-reformatting\">" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "  <meta content=\"telephone=no\" name=\"format-detection\">" +
                "  <title>Project Added - Constructify</title>" +
                "  <style type=\"text/css\">" +
                "    body {margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none; background-color: #ead1dc;}" +
                "    .es-wrapper-color {background-color: #ead1dc;}" +
                "    .es-header {background-color: transparent;}" +
                "    .es-header-body {background-color: transparent;}" +
                "    .es-content-body {background-color: transparent;}" +
                "    .es-footer {background-color: #efefef;}" +
                "    .es-footer-body {background-color: transparent;}" +
                "    .es-button-border {background: #dbbacc; border-style: solid solid solid solid; border-width: 0px 0px 0px 0px;}" +
                "    .es-button {text-decoration: none; font-size: 16px; color: #ffffff; background: #dbbacc; border-color: #dbbacc;}" +
                "    a.es-button, button.es-button {border-style: solid; border-width: 10px 20px 10px 10px;}" +
                "    .es-menu td a {font-size: 14px; text-decoration: none; color: #333333;}" +
                "    .es-menu img {vertical-align: middle;}" +
                "    .es-social td {padding-bottom: 10px;}" +
                "    .es-left {float: left;}" +
                "    .es-right {float: right;}" +
                "    .es-p5r {padding-right: 5px;}" +
                "    .es-p5l {padding-left: 5px;}" +
                "    .es-p10t {padding-top: 10px;}" +
                "    .es-p10b {padding-bottom: 10px;}" +
                "    .es-p10r {padding-right: 10px;}" +
                "    .es-p10l {padding-left: 10px;}" +
                "    .es-p20 {padding: 20px;}" +
                "    .es-p20t {padding-top: 20px;}" +
                "    .es-p20r {padding-right: 20px;}" +
                "    .es-p20l {padding-left: 20px;}" +
                "    @media only screen and (max-width:600px) {" +
                "      .es-m-p0b {padding-bottom: 0px !important;}" +
                "      .es-m-p20b {padding-bottom: 20px !important;}" +
                "      .es-m-p40b {padding-bottom: 40px !important;}" +
                "      .es-m-txt-l {text-align: left !important;}" +
                "      .es-m-txt-c {text-align: center !important;}" +
                "      .es-left, .es-right {width: 100% !important; float: none;}" +
                "      .es-button-border, .es-button {display: block !important; width: auto !important;}" +
                "    }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class=\"es-wrapper-color\">" +
                "    <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">" +
                "      <tbody>" +
                "        <tr>" +
                "          <td class=\"esd-email-paddings\" valign=\"top\">" +

                // Header Section
                "            <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\">" +
                "              <tbody>" +
                "                <tr>" +
                "                  <td class=\"esd-stripe\" align=\"center\" style=\"background-color:#ead1dc;\">" +
                "                    <table class=\"es-header-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color: transparent;\">" +
                "                      <tbody>" +
                "                        <tr>" +
                "                          <td class=\"esd-structure es-p20\" align=\"left\" bgcolor=\"#ead1dc\" style=\"background-color:#ead1dc\">" +
                "                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                              <tbody>" +
                "                                <tr>" +
                "                                  <td width=\"560\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                                      <tbody>" +
                "                                        <tr>" +
                "                                          <td align=\"center\" class=\"esd-block-image\" style=\"font-size: 0px;\">" +
                "                                            <img src=\"" + logoPath + "\" alt=\"Constructify Logo\" style=\"display:block\" width=\"100\" title=\"Constructify\">" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                      </tbody>" +
                "                                    </table>" +
                "                                  </td>" +
                "                                </tr>" +
                "                              </tbody>" +
                "                            </table>" +
                "                          </td>" +
                "                        </tr>" +

                // Main Content Section
                "                        <tr>" +
                "                          <td class=\"esd-structure es-p20r es-p20l\" align=\"left\">" +
                "                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                              <tbody>" +
                "                                <tr>" +
                "                                  <td width=\"560\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                                      <tbody>" +
                "                                        <tr>" +
                "                                          <td align=\"center\" class=\"esd-block-spacer es-p10t es-p10b\" style=\"font-size:0\">" +
                "                                            <table border=\"0\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\">" +
                "                                              <tbody>" +
                "                                                <tr>" +
                "                                                  <td style=\"border-bottom:2px solid #f5e9ee;background:none;height:1px;width:100%;margin:0px\"></td>" +
                "                                                </tr>" +
                "                                              </tbody>" +
                "                                            </table>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td align=\"left\" class=\"esd-block-text es-p20t es-p5b es-m-txt-l\">" +
                "                                            <h2 style=\"line-height: 120%;\">New Project Added to CONSTRUCTIFY!</h2>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td align=\"left\" class=\"esd-block-text es-p10t es-p10b es-m-txt-l\">" +
                "                                            <p>Greetings,</p>" +
                "                                            <p>We are thrilled to inform you that a new project has been successfully added to our system. Here are the details:</p>" +
                "                                            <p><strong>Project Name:</strong> " + project.getNomProjet() + "</p>" +
                "                                            <p><strong>Description:</strong> " + project.getDescriptionProjet() + "</p>" +
                "                                            <p><strong>Start Date:</strong> " + project.getDateDebut() + "</p>" +
                "                                            <p><strong>End Date:</strong> " + project.getDateFin() + "</p>" +
                "                                            <p><strong>Status:</strong> " + project.getEtatProjet() + "</p>" +
                "                                            <p>You can access the project details in your dashboard. Feel free to make modifications or add tasks to ensure the best project management experience.</p>" +
                "                                            <p>Best regards!</p>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td align=\"center\" class=\"esd-block-button es-p10t es-p10b es-m-p40b es-m-txt-c\">" +
                "                                            <span class=\"es-button-border\" style=\"background:#dbbacc\">" +
                "                                              <a href=\"#\" class=\"es-button\" target=\"_blank\" style=\"background:#dbbacc;padding:10px 20px 10px 10px\">" +
                "                                                <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/65391614697135358.png\" alt=\"icon\" width=\"30\" class=\"esd-icon-left\" style=\"margin-right:10px;\" align=\"absmiddle\">View Project" +
                "                                              </a>" +
                "                                            </span>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                      </tbody>" +
                "                                    </table>" +
                "                                  </td>" +
                "                                </tr>" +
                "                              </tbody>" +
                "                            </table>" +
                "                          </td>" +
                "                        </tr>" +
                "                      </tbody>" +
                "                    </table>" +
                "                  </td>" +
                "                </tr>" +
                "              </tbody>" +
                "            </table>" +

                // Contact Info Section
                "            <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">" +
                "              <tbody>" +
                "                <tr>" +
                "                  <td class=\"esd-stripe\" align=\"center\">" +
                "                    <table class=\"es-content-body\" style=\"background-color: transparent;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">" +
                "                      <tbody>" +
                "                        <tr>" +
                "                          <td class=\"esd-structure es-p20\" align=\"left\">" +
                "                            <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-right\" align=\"right\">" +
                "                              <tbody>" +
                "                                <tr>" +
                "                                  <td width=\"322\" align=\"left\" class=\"esd-container-frame\">" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                                      <tbody>" +
                "                                        <tr>" +
                "                                          <td align=\"left\" class=\"esd-block-text es-m-txt-l\">" +
                "                                            <h3><strong>Jihed Oueslati</strong></h3>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td class=\"esd-block-menu\">" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"es-menu\">" +
                "                                              <tbody>" +
                "                                                <tr>" +
                "                                                  <td align=\"left\" valign=\"top\" width=\"50.00%\" class=\"es-p10t es-p10b\" style=\"padding-top: 10px; padding-bottom: 5px;\">" +
                "                                                    <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/95711614763048218.png\" alt=\"+216 94 270 055\" title=\"+216 94 270 055\" align=\"absmiddle\" class=\"es-p5r\" width=\"20\">+216 94 270 055" +
                "                                                  </td>" +
                "                                                  <td align=\"left\" valign=\"top\" width=\"50.00%\" class=\"es-p10t es-p10b\" style=\"padding-top: 10px; padding-bottom: 5px;\">" +
                "                                                    <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/69541614947093393.png\" alt=\"+216 71 300 200\" title=\"+216 71 300 200\" align=\"absmiddle\" class=\"es-p5r\" width=\"20\">+216 71 300 200" +
                "                                                  </td>" +
                "                                                </tr>" +
                "                                              </tbody>" +
                "                                            </table>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td class=\"esd-block-menu\">" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"es-menu\">" +
                "                                              <tbody>" +
                "                                                <tr>" +
                "                                                  <td align=\"left\" valign=\"top\" width=\"50%\" class=\"es-p10t es-p10b\" style=\"padding-top: 10px; padding-bottom: 5px;\">" +
                "                                                    <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/58641614773761370.png\" alt=\"oueslati.jihed@esprit.tn\" title=\"oueslati.jihed@esprit.tn\" align=\"absmiddle\" class=\"es-p5r\" width=\"20\">oueslati.jihed@esprit.tn" +
                "                                                  </td>" +
                "                                                  <td align=\"left\" valign=\"top\" width=\"50%\" class=\"es-p10t es-p10b\" style=\"padding-top: 10px; padding-bottom: 5px;\">" +
                "                                                    <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/39781614763048410.png\" alt=\"Ariana Borj Louzir\" title=\"Ariana Borj Louzir\" align=\"absmiddle\" class=\"es-p5r\" width=\"20\">Ariana Borj Louzir" +
                "                                                  </td>" +
                "                                                </tr>" +
                "                                              </tbody>" +
                "                                            </table>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                      </tbody>" +
                "                                    </table>" +
                "                                  </td>" +
                "                                </tr>" +
                "                              </tbody>" +
                "                            </table>" +
                "                          </td>" +
                "                        </tr>" +
                "                      </tbody>" +
                "                    </table>" +
                "                  </td>" +
                "                </tr>" +
                "              </tbody>" +
                "            </table>" +

                // Footer Section
                "            <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\">" +
                "              <tbody>" +
                "                <tr>" +
                "                  <td class=\"esd-stripe\" align=\"center\" bgcolor=\"#efefef\" style=\"background-color: #efefef;\">" +
                "                    <table class=\"es-footer-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color: transparent;\">" +
                "                      <tbody>" +
                "                        <tr>" +
                "                          <td class=\"esd-structure es-p20t es-p20r es-p20l\" align=\"left\">" +
                "                            <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-left\" align=\"left\">" +
                "                              <tbody>" +
                "                                <tr>" +
                "                                  <td width=\"245\" class=\"es-m-p20b esd-container-frame\" align=\"left\">" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                                      <tbody>" +
                "                                        <tr>" +
                "                                          <td align=\"left\" class=\"esd-block-image es-m-txt-c\" style=\"font-size: 0px;\">" +
                "                                            <img src=\"" + logoPath + "\" alt=\"Constructify Logo\" style=\"display:block\" width=\"100\">" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td align=\"left\" class=\"esd-block-text es-p10t es-p10b\">" +
                "                                            <p>We work with a passion of taking challenges and creating new ones in order to succeed together.</p>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                      </tbody>" +
                "                                    </table>" +
                "                                  </td>" +
                "                                </tr>" +
                "                              </tbody>" +
                "                            </table>" +
                "                            <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-right\" align=\"right\">" +
                "                              <tbody>" +
                "                                <tr>" +
                "                                  <td width=\"295\" align=\"left\" class=\"esd-container-frame\">" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                                      <tbody>" +
                "                                        <tr>" +
                "                                          <td align=\"left\" class=\"esd-block-text es-p20t\">" +
                "                                            <h3>Official info</h3>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td class=\"esd-block-menu\">" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"es-menu\">" +
                "                                              <tbody>" +
                "                                                <tr>" +
                "                                                  <td align=\"left\" valign=\"top\" width=\"100%\" class=\"es-p10t es-p10b es-p5r es-p5l\" style=\"padding-bottom: 7px;\">" +
                "                                                    <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/39781614763048410.png\" alt=\"Technopole el Ghazela, Raoued\" title=\"Technopole el Ghazela, Raoued\" align=\"absmiddle\" class=\"es-p5r\" width=\"20\">Technopole el Ghazela, Raoued" +
                "                                                  </td>" +
                "                                                </tr>" +
                "                                              </tbody>" +
                "                                            </table>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td class=\"esd-block-menu\">" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"es-menu\">" +
                "                                              <tbody>" +
                "                                                <tr>" +
                "                                                  <td align=\"left\" valign=\"top\" width=\"100%\" class=\"es-p10t es-p10b es-p5r es-p5l\" style=\"padding-top: 7px; padding-bottom: 7px;\">" +
                "                                                    <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/95711614763048218.png\" alt=\"+216 70 300 200\" title=\"+216 70 300 200\" align=\"absmiddle\" class=\"es-p5r\" width=\"20\">+216 70 300 200" +
                "                                                  </td>" +
                "                                                </tr>" +
                "                                              </tbody>" +
                "                                            </table>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                        <tr>" +
                "                                          <td class=\"esd-block-menu\">" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"es-menu\">" +
                "                                              <tbody>" +
                "                                                <tr>" +
                "                                                  <td align=\"left\" valign=\"top\" width=\"100%\" class=\"es-p10t es-p10b es-p5r es-p5l\" style=\"padding-top: 7px;\">" +
                "                                                    <img src=\"https://eekgguq.stripocdn.email/content/guids/CABINET_3b670d78779801705eef224a1b9fbd70/images/97961614763048410.png\" alt=\"Mon - Sat: 8am - 5pm, Sunday: CLOSED\" title=\"Mon - Sat: 8am - 5pm, Sunday: CLOSED\" align=\"absmiddle\" class=\"es-p5r\" width=\"20\">Mon - Sat: 8am - 5pm, Sunday: CLOSED" +
                "                                                  </td>" +
                "                                                </tr>" +
                "                                              </tbody>" +
                "                                            </table>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                      </tbody>" +
                "                                    </table>" +
                "                                  </td>" +
                "                                </tr>" +
                "                              </tbody>" +
                "                            </table>" +
                "                          </td>" +
                "                        </tr>" +
                "                        <tr>" +
                "                          <td class=\"esd-structure es-p20\" align=\"left\" bgcolor=\"#ead1dc\" style=\"background-color:#ead1dc\">" +
                "                            <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                              <tbody>" +
                "                                <tr>" +
                "                                  <td width=\"560\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "                                      <tbody>" +
                "                                        <tr>" +
                "                                          <td align=\"center\" class=\"esd-block-text es-infoblock\">" +
                "                                            <p style=\"line-height: 150%;\">" +
                "                                              <span style=\"color:#741b47\">You are receiving this email as a notification for a new project added to the system. This is an automated email, please do not reply.</span>" +
                "                                            </p>" +
                "                                          </td>" +
                "                                        </tr>" +
                "                                      </tbody>" +
                "                                    </table>" +
                "                                  </td>" +
                "                                </tr>" +
                "                              </tbody>" +
                "                            </table>" +
                "                          </td>" +
                "                        </tr>" +
                "                      </tbody>" +
                "                    </table>" +
                "                  </td>" +
                "                </tr>" +
                "              </tbody>" +
                "            </table>" +
                "          </td>" +
                "        </tr>" +
                "      </tbody>" +
                "    </table>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }
}
