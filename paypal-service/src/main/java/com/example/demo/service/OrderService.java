package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderRepository;
import com.example.demo.utils.PaymentParams;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repo;

	@Autowired
	private MerchantService merchantService;

	// Ovo ce biti metnuto u bazu u jednom trenutku
	// Svaki merchant ce ima svoje. Ovo nabavimo za testiranje na paypal-ov sajt.
	String clientId = "ASbalrTsNQwyeFRT6r47HW23NQwDpF9V_4IRJIEkhWGmgI2uZ5L7lYgrspWWgWvEYqd8GT1SmF4hcRd4";
	String clientSecret = "EJZT7rVvs4wBMCghAlPnx96WC-Se44lmQTKuiAXRWNFvFxH-e69d_aSI8gESJPAbbys3CvOmLZttfGPb";

	// Napravimo narudzbu kod paypal-a
	// Klijent prodavnice onda treba da potvrdi placanje
	public Map<String, Object> createPayment(Order order) {
		Map<String, Object> response = new HashMap<String, Object>();

		Merchant merchant = merchantService.findOne(order.getMerchantId());

		Amount amount = new Amount();
		amount.setCurrency(order.getCurrency());
		amount.setTotal(order.getValue().toString());

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://localhost:4200/cancel");
		redirectUrls.setReturnUrl("http://localhost:4200/");
		payment.setRedirectUrls(redirectUrls);

		Payment createdPayment;
		try {
			String redirectUrl = "";
			APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");
			createdPayment = payment.create(context);
			if (createdPayment != null) {
				List<Links> links = createdPayment.getLinks();
				for (Links link : links) {
					if (link.getRel().equals("approval_url")) {
						redirectUrl = link.getHref();
						break;
					}
				}
				response.put("status", "success");
				response.put("redirect_url", redirectUrl);

				order.setStatus(OrderStatus.CREATED);
				order.setPayPalOrderId(createdPayment.getId());
				repo.save(order);
			}
		} catch (PayPalRESTException e) {
			System.out.println("Error happened during payment creation!");
		}
		return response;
	}

	// Poziva se nakon sto klijent odobri placanje
	public Map<String, Object> completePayment(PaymentParams params) {
		Map<String, Object> response = new HashMap<String, Object>();

		Order order = repo.findByPayPalOrderIdNotNull(params.getPaymentId());
		Merchant merchant = merchantService.findOne(order.getMerchantId());

		Payment payment = new Payment();
		payment.setId(params.getPaymentId());
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(params.getPayerId());

		try {
			APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");
			Payment createdPayment = payment.execute(context, paymentExecution);
			if (createdPayment != null) {
				response.put("status", "success");
				response.put("payment", createdPayment);
				order.setStatus(OrderStatus.COMPLETED);
			}
		} catch (PayPalRESTException e) {
			System.err.println(e.getDetails());
			order.setStatus(OrderStatus.FAILED);
		}
		repo.save(order);

		return response;
	}

}
