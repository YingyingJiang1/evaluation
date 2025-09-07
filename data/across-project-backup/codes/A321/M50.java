    public Observable<CreditCardAuthorizationResult> observeSimulatedUserRequestForOrderConfirmationAndCreditCardPayment() {
        /* fetch user object with http cookies */
        try {
            Observable<UserAccount> user = new GetUserAccountCommand(new HttpCookie("mockKey", "mockValueFromHttpRequest")).observe();
            /* fetch the payment information (asynchronously) for the user so the credit card payment can proceed */
            Observable<PaymentInformation> paymentInformation = user.flatMap(new Func1<UserAccount, Observable<PaymentInformation>>() {
                @Override
                public Observable<PaymentInformation> call(UserAccount userAccount) {
                    return new GetPaymentInformationCommand(userAccount).observe();
                }
            });

            /* fetch the order we're processing for the user */
            int orderIdFromRequestArgument = 13579;
            final Observable<Order> previouslySavedOrder = new GetOrderCommand(orderIdFromRequestArgument).observe();

            return Observable.zip(paymentInformation, previouslySavedOrder, new Func2<PaymentInformation, Order, Pair<PaymentInformation, Order>>() {
                @Override
                public Pair<PaymentInformation, Order> call(PaymentInformation paymentInformation, Order order) {
                    return new Pair<PaymentInformation, Order>(paymentInformation, order);
                }
            }).flatMap(new Func1<Pair<PaymentInformation, Order>, Observable<CreditCardAuthorizationResult>>() {
                @Override
                public Observable<CreditCardAuthorizationResult> call(Pair<PaymentInformation, Order> pair) {
                    return new CreditCardCommand(pair.b(), pair.a(), new BigDecimal(123.45)).observe();
                }
            });
        } catch (IllegalArgumentException ex) {
            return Observable.error(ex);
        }


    }
