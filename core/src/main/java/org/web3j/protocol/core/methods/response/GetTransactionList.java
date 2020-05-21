package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;

public class GetTransactionList extends Response<GetTransactionList.TransactionList> {

    public List<Transaction> getTransactionList() {
        return getResult().getTransactions();
    }

    public static class TransactionList {
        private List<Transaction> transactions;

        public TransactionList() {

        }

        public TransactionList(List<Transaction> transactions) {
            this.transactions = transactions;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<Transaction> transactions) {
            this.transactions = transactions;
        }
    }
}
