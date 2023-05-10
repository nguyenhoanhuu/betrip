package fit.iuh.dulichgiare.service.impl;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import fit.iuh.dulichgiare.entity.Booking;
import fit.iuh.dulichgiare.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public String sendEmailWhenBookingIsSuccess(String from, String to, String subject, String fullName,
            Booking booking, String linkOrderId) throws MessagingException {
        MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        String departureTime = booking.getDepartureTime() + " " + formatter.format(booking.getStartDayTour());
        String timeTour = booking.getTour().getNumberofday() + "N" + (booking.getTour().getNumberofday() - 1) + "Đ";
        String body = getContextEmail(fullName, booking.getTour().getName(), departureTime, timeTour,
                booking.getNumberofadbult() + booking.getNumberofchildren(), Double.toString(booking.getTotal()) , linkOrderId,
                booking.getStatus());
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body, true);
        emailSender.send(mimeMessage);
        return "oke";
    }

    private String getContextEmail(String fullName, String nameTour, String departureTime, String timeTour,
            int volumeCustomer, String totalBill, String linkOrderId, String status) {
        Context ctx = new Context();
        MimeMessage message = emailSender.createMimeMessage();
        ctx.setVariable("fullName", fullName);
        ctx.setVariable("nameTour", nameTour);
        ctx.setVariable("departureTime", departureTime);
        ctx.setVariable("timeTour", timeTour);
        ctx.setVariable("volumeCustomer", volumeCustomer);
        ctx.setVariable("totalBill", totalBill);
        ctx.setVariable("status", status);
        ctx.setVariable("linkOrderId", linkOrderId);
        return templateEngine.process("order-template.html", ctx);
    }

}
