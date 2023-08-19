package br.com.market.place.factory;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.payment.entity.Billet;
import br.com.market.place.domain.payment.entity.CreditCard;
import br.com.market.place.domain.payment.value.CardPan;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;

public class PaymentEntityMockFactory {
    private Address address;

    public PaymentEntityMockFactory() {
        this.address = Address.Builder.build()
                .withCity("London").withStreet("Baker Street").withNumber("221")
                .withComponent("B").withZipCode("37540232").now();
    }

    public CreditCard credCardFactory(Customer customer) {
        return CreditCard.Builder.build()
                .withStatusPending()
                .withCardPan(new CardPan("2720339563597456"))
                .withAmount(new Currency("10.0", "BRL"))
                .withCustomer(customer)
                .withAddress(address)
                .now();
    }

    public Billet billetFactory(Customer customer) {
        return Billet.Builder.build()
                .withDueDateExpireInDays(3)
                .withCustomer(customer)
                .withAmount(new Currency("10.0", "BRL"))
                .withPendingStatus()
                .withAddress(address)
                .withPayLine("09580928590923405023450230450234509234508080800").now();
    }
}
