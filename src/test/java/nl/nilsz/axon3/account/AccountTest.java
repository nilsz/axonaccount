package nl.nilsz.axon3.account;

import nl.nilsz.axon3.account.coreapi.AccountCreatedEvent;
import nl.nilsz.axon3.account.coreapi.CreateAccountCommand;
import nl.nilsz.axon3.account.coreapi.MoneyWithdrawnEvent;
import nl.nilsz.axon3.account.coreapi.WithDrawMoneyCommand;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;


/**
 *
 */
public class AccountTest {


    private FixtureConfiguration fixture;

    @Before
    public void setup() {
        fixture = new AggregateTestFixture<>(Account.class);
    }

    @Test
    public void testCreateAccount() throws Exception {
        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand("1234", 1000))
                .expectEvents(new AccountCreatedEvent("1234", 1000));
    }

    @Test
    public void testWithdrawReasonableAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithDrawMoneyCommand("1234", 400))
                .expectEvents(new MoneyWithdrawnEvent("1234", 400, -400));
    }

    @Test
    public void testWithdrawAbsurdAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithDrawMoneyCommand("1234", 1001))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException.class);
    }

    @Test
    public void testWithdrawTwice() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                new MoneyWithdrawnEvent("1234", 999, -999))
                .when(new WithDrawMoneyCommand("1234", 2))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException.class);
    }
}