package com.vatech.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; // Inject JavaMailSender for email sending

    /**
     * Sends withdrawal notification to the user's email.
     * @param toEmail The recipient's email address
     * @param username The recipient's username
     * @param accountNumber The account number from which the withdrawal occurred
     * @param amount The withdrawal amount
     */
    public void sendWithdrawalNotification(String toEmail, String username, String accountNumber, double amount) {
        // Ensure the account number is securely masked
        String maskedAccountNumber = (accountNumber != null && accountNumber.startsWith("ACC") && accountNumber.length() >= 4)
                ? "****" + accountNumber.substring(accountNumber.length() - 4) // Display last 4 digits
                : "Unknown Account";

        // Create the email content dynamically
        String emailContent = String.format(
                "Dear %s,\n\n" +
                        "A withdrawal of $%.2f has been made from your account (%s).\n" +
                        "If this wasn't you, please contact our support team immediately.\n\n" +
                        "Thank you,\nVaTech Payments Team",
                username, amount, maskedAccountNumber
        );

        // Log the email content for debugging purposes
        System.out.println("Email Content: " + emailContent);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Withdrawal Notification");
        message.setText(emailContent);

        // Send the email
        mailSender.send(message);
    }
}