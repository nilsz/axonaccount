package nl.nilsz.axon3.account.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

class CreateAccountCommand(val accountId : String, val overdraftLimit : Int)
class WithDrawMoneyCommand(@TargetAggregateIdentifier val accountId: String, val amount : Int)

class AccountCreatedEvent(val accountId: String, val overdraftLimit: Int)
class MoneyWithdrawnEvent(val accountId: String, val amount: Int, val balance : Int)
