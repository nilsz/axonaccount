package nl.nilsz.axon3.account;

import nl.nilsz.axon3.account.coreapi.AccountCreatedEvent;
import nl.nilsz.axon3.account.coreapi.CreateAccountCommand;
import nl.nilsz.axon3.account.coreapi.MoneyWithdrawnEvent;
import nl.nilsz.axon3.account.coreapi.WithDrawMoneyCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * Created by nilszonneveld on 07-02-17(w 6).
 */
public class Account {

    @AggregateIdentifier
    private String accountId;

    private int overdraftLimit;
    private int balance;

    public Account() {
    }

    @CommandHandler
    public Account(CreateAccountCommand command) {
        apply(new AccountCreatedEvent(command.getAccountId(), command.getOverdraftLimit()));
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        accountId = event.getAccountId();
        overdraftLimit = event.getOverdraftLimit();
    }

    @CommandHandler
    public void handle(WithDrawMoneyCommand command) throws OverdraftLimitExceededException {
        if (balance + overdraftLimit >= command.getAmount()) {
            apply((new MoneyWithdrawnEvent(command.getAccountId(), command.getAmount(), balance - command.getAmount())));
        }
        else {
            throw new OverdraftLimitExceededException();
        }

    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent event)  {
        this.balance = event.getBalance();
    }

}
