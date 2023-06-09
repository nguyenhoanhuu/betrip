package fit.iuh.dulichgiare.service;

import org.springframework.stereotype.Service;

import fit.iuh.dulichgiare.entity.Booking;
import jakarta.mail.MessagingException;

@Service
public interface MailService {
    public String sendEmailWhenBookingIsSuccess(String from, String to, String subject, String fullName, Booking booking,
            String linkOrderId) throws MessagingException;

}
