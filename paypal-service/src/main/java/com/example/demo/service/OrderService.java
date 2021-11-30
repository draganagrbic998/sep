package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderRepository;
import com.paypal.api.payments.Amount;
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

	public Order getOrder(Integer orderId) {
		return repo.getById(orderId);
	}

	public Order saveOrder(Order order) {
		return repo.save(order);
	}

	// Napravimo narudzbu kod paypal-a
	// Klijent prodavnice onda treba da potvrdi placanje
	public Order createPayment(Order order) {
		Merchant merchant = merchantService.findOneByApiKey(order.getMerchantApiKey());

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
		redirectUrls.setReturnUrl("http://localhost:8086/view/success_url/" + order.getId().toString());
		redirectUrls.setCancelUrl("http://localhost:8086/view/cancel_url/" + order.getId().toString());
		payment.setRedirectUrls(redirectUrls);

		Payment createdPayment;
		try {
			APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");
			createdPayment = payment.create(context);
			if (createdPayment != null) {
				order.setStatus(OrderStatus.CREATED);
				order.setPayPalOrderId(createdPayment.getId());
				order = repo.save(order);
			}
		} catch (PayPalRESTException e) {
			System.out.println("Error happened during payment creation!");
		}
		return order;
	}

	// Poziva se nakon sto klijent odobri placanje
	public String completePayment(String paymentId, String payerId) {
		Order order = repo.findByPayPalOrderIdNotNull(paymentId);
		Merchant merchant = merchantService.findOneByApiKey(order.getMerchantApiKey());

		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);

		Payment completedPayment = new Payment();

		try {
			APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");
			completedPayment = payment.execute(context, paymentExecution);
			if (completedPayment != null) {
				order.setStatus(OrderStatus.COMPLETED);
			}
		} catch (PayPalRESTException e) {
			System.err.println(e.getDetails());
			order.setStatus(OrderStatus.FAILED);
		}

		if (order.getStatus().toString().equalsIgnoreCase("COMPLETED")) {
			order.setExecuted(true);

			RestTemplate restTemplate = new RestTemplate();
			PaymentCompletedDTO paymentCompletedDTO = new PaymentCompletedDTO();
			paymentCompletedDTO.setOrderId(order.getShopOrderId());
			paymentCompletedDTO.setStatus("COMPLETED");
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentCompletedDTO>(paymentCompletedDTO), String.class);
		}

		repo.save(order);

		return completedPayment.toJSON();
	}

	public String getOrderDetails(int orderId) throws PayPalRESTException {
		Order order = repo.getById(orderId);
		Merchant merchant = merchantService.findOneByApiKey(order.getMerchantApiKey());

		APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

		Payment payment = Payment.get(context, order.getPayPalOrderId());

		return payment.toJSON();
	}
}
