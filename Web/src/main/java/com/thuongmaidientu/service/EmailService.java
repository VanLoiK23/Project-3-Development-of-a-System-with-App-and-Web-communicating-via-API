package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.OrderItem;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSenderImpl mailSender;
	
	public void sendOrderEmail(String toEmail,List<OrderItem> orderItems,Integer total) {
	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        helper.setTo(toEmail);
	        helper.setSubject("Xác nhận đơn hàng của bạn");

	        StringBuilder htmlContent = new StringBuilder();
	        htmlContent.append("<div style='font-family:Arial,sans-serif;font-size:14px;'>")
	                   .append("<h2 style='color:#00BCD4;'>Cảm ơn bạn đã đặt hàng tại HVL App!</h2>")
	                   .append("<p>Dưới đây là thông tin đơn hàng của bạn:</p>")
	                   .append("<table style='border-collapse:collapse;width:100%;'>")
	                   .append("<thead>")
	                   .append("<tr style='background-color:#00BCD4;color:white;'>")
	                   .append("<th style='padding:10px;border:1px solid #ccc;'>Sản phẩm</th>")
	                   .append("<th style='padding:10px;border:1px solid #ccc;'>Số lượng</th>")
	                   .append("<th style='padding:10px;border:1px solid #ccc;'>Giá</th>")
	                   .append("</tr>")
	                   .append("</thead>")
	                   .append("<tbody>");

	        for (OrderItem item : orderItems) {
	            double itemTotal = item.getQuantity() * item.getPrice();
	            htmlContent.append("<tr>")
	                       .append("<td style='padding:10px;border:1px solid #ccc;'>").append(item.getProductName()).append("</td>")
	                       .append("<td style='padding:10px;border:1px solid #ccc;text-align:center;'>").append(item.getQuantity()).append("</td>")
	                       .append("<td style='padding:10px;border:1px solid #ccc;text-align:right;'>")
	                       .append(String.format("%,.0f", itemTotal)).append(" đ</td>")
	                       .append("</tr>");
	        }

	        htmlContent.append("<tr style='background-color:#f2f2f2;'>")
	                   .append("<td colspan='2' style='padding:10px;border:1px solid #ccc;text-align:right;'><strong>Tổng cộng:</strong></td>")
	                   .append("<td style='padding:10px;border:1px solid #ccc;text-align:right;'><strong>")
	                   .append(String.format("%,.0f", total.doubleValue())).append(" đ</strong></td>")
	                   .append("</tr>")
	                   .append("</tbody>")
	                   .append("</table>")
	                   .append("<p style='margin-top:20px;'>Nếu có bất kỳ thắc mắc nào, hãy liên hệ với chúng tôi qua email hoặc hotline hỗ trợ.</p>")
	                   .append("<p>Trân trọng,<br>Đội ngũ FPT shop</p>")
	                   .append("</div>");

	        helper.setText(htmlContent.toString(), true); // true = gửi HTML

	        mailSender.send(message);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
