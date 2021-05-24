package com.atlas.sdk.util

enum class OperationTypes(val value: Int) {

    WEB(1),
    H2H(2),
    RECURRENT(3),
    MOTO(4),
    A2C(5),
    CONFIRM_PAYMENT_HOLDING(6),
    CARD_TOKEN_NOT_WEB(7),
    PAY_BY_CARD_TOKEN(8),
    PAY_WITHOUT_3DS(9),
    PAY_HOLD(10)
}